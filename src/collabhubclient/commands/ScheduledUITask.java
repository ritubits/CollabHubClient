package collabhubclient.commands;

import java.io.FileNotFoundException;
import java.util.TimerTask;
import java.util.Date;
import java.util.Vector;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import collabhubclient.CollabUserActivity;
import collabhubclient.CollabUserActivityClient;
import collabhubclient.StartCollaborationClient;

public class ScheduledUITask extends Job {

	boolean DEBUG = true;
	boolean compilable = false;
	int lineNo;
	String methodName = null;
	CollabUserActivityClient userClient = null;
	IWorkbenchPage activePage = null;
	private UIJob activityMethodDataJob;
	private UIJob activityLineDataJob;
	private static final int RESCHEDULE_TIME = 60 * 1000 * 1;//every minute
	private boolean running = true;

	public ScheduledUITask(CollabUserActivityClient uClient,
			IWorkbenchPage aPage) {
		super("collabclientjob");
		userClient = uClient;
		activePage = aPage;
	}

	private void getUserActivityData() {

		CollabUserActivity userObject = new CollabUserActivity();

		// while (true)
		{
			try {
				compilable = false;
				userObject.setCurrentFile(getCurrentFileName());
				activityLineData();
				userObject.setCurrentLine(lineNo);
				activityMethodNameData();
				if (DEBUG)
					System.out
							.println("MethodName from activityMethodNameData:: "
									+ methodName);
				userObject.setCurrentAST(methodName);
				userObject.setEditFile(getAllFiles());

				Boolean exist = userClient.getCollabClient();
				if (DEBUG)
					System.out.println("Exist:: " + exist);
				if (exist) {
					// if collaborating
					userClient.setUserObject(userObject);
					boolean output = userClient.updateUserActivityTable();
					if (DEBUG)
						System.out.println("From Servlet:: in StartHandler:: "
								+ output);
				}

				// if (activePage.getActiveEditor().isDirty())
				{
					if (DEBUG)
						System.out.println(" Dirty:: "
								+ activePage.getActiveEditor().isDirty());
					if (DEBUG)
						System.out.println("compilable:: " + compilable);
					if (compilable)
						sendCurrentArtifact(getCurrentFileName());
					// Thread.sleep(1000*30);// pick activity data info every 60
					// second
				}

				Thread.sleep(1000 * 20);// pick activity data info every 20
										// second


			} catch (Exception e) {
				e.printStackTrace();
			}
			if (DEBUG)
				System.out.println("In getUserActivityData");
		}
	}

	
			
	public String getCurrentFileName() throws FileNotFoundException {
		String name = null;
		try {
			if (activePage != null && (activePage.getActiveEditor() != null)
					&& (activePage.getActiveEditor().getEditorInput() != null))
				name = activePage.getActiveEditor().getEditorInput().getName();
			if (DEBUG)
				System.out.println("CurrentFileName:: " + name);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;

	}

	public boolean activityLineData() {

		if (activityLineDataJob == null) {
			activityLineDataJob = new UIJob(Display.getDefault(),
					"Obtaining Line Data") {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {

					lineNo = getCursorPosition();
					if (StartCollaborationClient.httpclient != null)
						return Status.OK_STATUS;
					else
						return Status.CANCEL_STATUS;
				}
			};
			activityLineDataJob.setSystem(true);
		}
		activityLineDataJob.schedule();
		return true;
	}

	public boolean activityMethodNameData() {

		if (activityMethodDataJob == null) {
			activityMethodDataJob = new UIJob(Display.getDefault(),
					"Obtaining Method Name Data") {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					if (DEBUG)
						System.out.println("in runInUIThread");
					// System.out.println("Cursor::"+ getCursorPosition());
					methodName = getCurrentMethod();
					if (DEBUG)
						System.out
								.println("MethodName in activityMethodNameData:: "
										+ methodName);
					// PlatformUI.getWorkbench().getDecoratorManager().update("DecorationProject.myDecorator");
					if (StartCollaborationClient.httpclient != null)
						return Status.OK_STATUS;
					else
						return Status.CANCEL_STATUS;
				}
			};
			activityMethodDataJob.setSystem(true);
		}
		activityMethodDataJob.schedule();
		return true;
	}

	public Vector getAllFiles() throws FileNotFoundException {
		IEditorReference[] name = null;
		Vector fileNames = new Vector();
		try {
			if (activePage != null)
				name = activePage.getEditorReferences();

			int i = name.length;
			for (int j = 0; j < i; j++) {
				if (DEBUG)
					System.out.println("All files:: " + name[j].getName());
				fileNames.add(name[j].getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileNames;
	}

	public void sendCurrentArtifact(String currentFileName) {
		if (DEBUG)
			System.out.println("Sending Artifact Graph");
		IAdaptable editorPart = null;

		IEditorPart activeEditor = activePage.getActiveEditor();

		if (activeEditor instanceof JavaEditor) {
			ITextEditor editor = (ITextEditor) activeEditor
					.getAdapter(ITextEditor.class);

			if (editor != null) {
				IDocumentProvider provider = editor.getDocumentProvider();
				IDocument document = provider.getDocument(editor
						.getEditorInput());
				if (DEBUG)
					System.out.println("file Content::" + document.get());

				Boolean exist = userClient.getCollabClient();
				// if (DEBUG) System.out.println("Exist:: "+exist);
				if (exist) {
					// if collaborating
					try {
						boolean output = userClient
								.sendUserArtifactGraphContent(document.get(),
										currentFileName);
						if (DEBUG)
							System.out
									.println("From Servlet:: in StartHandler:: sending artifact graph");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	public int getCursorPosition() {

		IEditorPart editor = activePage.getActiveEditor();
		if (editor instanceof ITextEditor) {
			ISelectionProvider selectionProvider = ((ITextEditor) editor)
					.getSelectionProvider();
			ISelection selection = selectionProvider.getSelection();
			if (selection instanceof ITextSelection) {
				ITextSelection textSelection = (ITextSelection) selection;
				IDocumentProvider provider = ((ITextEditor) editor)
						.getDocumentProvider();
				IDocument document = provider.getDocument(editor
						.getEditorInput());
				int line = textSelection.getStartLine() + 1;
				// int column =0;
				// try {
				// column = textSelection.getOffset() -
				// document.getLineOffset(line);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				return line;
			}
		}

		return 0;
	}// getcursor

	public String getCurrentMethod() {
		IEditorPart activeEditor = activePage.getActiveEditor();

		if (activeEditor instanceof JavaEditor) {

			ICompilationUnit root = (ICompilationUnit) EditorUtility
					.getEditorInputJavaElement(activeEditor, false);
			try {
				ITextSelection sel = (ITextSelection) ((JavaEditor) activeEditor)
						.getSelectionProvider().getSelection();
				int offset = sel.getOffset();
				// compilable= root.getJavaProject().hasBuildState();
				// Get compilation Unit
				ASTParser parser = ASTParser.newParser(AST.JLS4);
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				parser.setSource(root);
				parser.setResolveBindings(true);
				CompilationUnit cu = (CompilationUnit) parser.createAST(null);
				int error = 0;
				IProblem[] problemList = cu.getProblems();
				if (problemList.length != 0) {

					for (int i = 0; i < problemList.length && error == 0; i++) {
						IProblem iProblem = problemList[i];
						// If it is an error
						if (iProblem.isError()) {
							compilable = false;
							error++;

						}
					}
					if (error == 0)
						compilable = true;

				} else
					compilable = true;

				if (DEBUG)
					System.out.println("compilable from getCurrentMethod():: "
							+ compilable);
				IJavaElement element = root.getElementAt(offset);

				if (element != null) {
					if (element.getElementType() == IJavaElement.METHOD) {
						return ("M" + element.getElementName() + "()");
					}

					if (element.getElementType() == IJavaElement.FIELD) {
						return ("F" + element.getElementName());
					}

					if (element.getElementType() == IJavaElement.IMPORT_DECLARATION) {
						return ("I" + element.getElementName());
					}

					if (element.getElementType() == IJavaElement.PACKAGE_DECLARATION) {
						return ("P" + element.getElementName());
					}

					if (element.getElementType() == IJavaElement.LOCAL_VARIABLE) {
						return ("L" + element.getElementName());
					}
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		long starttime = System.currentTimeMillis();
		try {
			if (running) {
				getUserActivityData();
			} else {
				System.out
						.println("Not running ScheduledUITask since running is false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (running) {
				long endtime = System.currentTimeMillis();
				long rescheduletime = RESCHEDULE_TIME - (endtime - starttime);
				if (rescheduletime > 0) {
					schedule(rescheduletime);
				} else {
					schedule();
				}
			}
			else {
				System.out
						.println("Not scheduling ScheduledUITask since running is false");
			}
		}
		return Status.OK_STATUS;
	}

	public void enableRunning() {
		running = true;
	}

	public void disableRunning() {
		running = false;
	}
}
