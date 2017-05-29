package collabhubclient.commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
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

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;

public class ScheduledSimulationUIJob extends Job {

	boolean DEBUG = true;
	boolean compilable = false;
	int lineNo;
	String methodName = null;
	CollabUserActivityClient userClient = null;
	IWorkbenchPage activePage = null;
	private UIJob setContentDataJob;
	private static final int RESCHEDULE_TIME = 60 * 1000 * 1;//every 1 minute
	private boolean running = true;
	static int fileNo=0;
	String simPath=null;

	public ScheduledSimulationUIJob(CollabUserActivityClient uClient,
			IWorkbenchPage aPage, String simulationPath) {
		super("collabclientSimulationjob");
		userClient = uClient;
		activePage = aPage;
		simPath=simulationPath;
	}
			
	public boolean setContentData() {
		
		if (setContentDataJob == null) {
			setContentDataJob = new UIJob(Display.getDefault(),
					"Setting Data") {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					if (DEBUG)
						System.out.println("in runInUIThread:::Simulation");
					setCurrentWorkSpaceContent();
					if (StartCollaborationClient.httpclient != null)
						return Status.OK_STATUS;
					else
						return Status.CANCEL_STATUS;
				}
			};
			setContentDataJob.setSystem(true);
		}
		setContentDataJob.schedule();
		return true;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		long starttime = System.currentTimeMillis();
		
		try {
			if (running) {
				System.out.println("fileNo is initially:: "+fileNo);
				if (fileNo<5) setContentData();
				fileNo++;
				System.out.println("i is finally:: "+fileNo);
			} else {
				System.out
						.println("Not running ScheduledSimulationUIJob since running is false");
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
						.println("Not scheduling ScheduledSimulationUIJob since running is false");
			}
		}
		return Status.OK_STATUS;
	}

	public void enableRunning() {
		 System.out.println("enabling simulation");
		running = true;
	}

	public void disableRunning() {
		running = false;
	}
	
	public void setCurrentWorkSpaceContent()
	{
	
		IAdaptable editorPart = null;

		IEditorPart activeEditor = activePage.getActiveEditor();

		if (activeEditor instanceof JavaEditor) {
			ITextEditor editor = (ITextEditor) activeEditor
					.getAdapter(ITextEditor.class);

			if (editor != null) {
				IDocumentProvider provider = editor.getDocumentProvider();
				IDocument document = provider.getDocument(editor
						.getEditorInput());
				document.set(readFileContent());
				//editor.selectAndReveal(6, 0);
			//	activePage.activate(editor);
				
				IRegion lineInfo = null;
				int lineNumber= Integer.parseInt(getLineNumberFromFile());// set to a random value between 1 and fileLength
				  try {
				  // line count internaly starts with 0, and not with 1 like in
				  // GUI
				   if (lineNumber==0) lineNumber=1; 
				lineInfo = document.getLineInformation(lineNumber - 1);
				   
				 } catch (BadLocationException e) {
				  // ignored because line number may not really exist in document,
				  // we guess this...
				 }
				  if (lineInfo != null) {
				//  editor.selectAndReveal(lineInfo.getOffset(), lineInfo.getLength());//set to 0
				  editor.selectAndReveal(lineInfo.getOffset(), 0);//set to 0
				   }
				  
				if (DEBUG)
					System.out.println("file Content::" + document.get());
			}
		}
	}
	
	public String readFileContent()
	{
		String content= null;
		System.out.println("Reading content from "+fileNo+" file");
		BufferedReader br = null;
		FileReader fr = null;
		String fileName=simPath+"/Sim"+fileNo+".java";
		//String fileName="D:/COGsimulation/Sim"+fileNo+".java";
		System.out.println("Reading from file:: "+fileName);
		try {

			fr = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
	        StringBuilder outBuilder = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	        	outBuilder.append(line);
	        	outBuilder.append('\n');
	        }
	        reader.close();
	        content= outBuilder.toString();
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
		return content;
	}
	
	public String getLineNumberFromFile()
	{
		String content= null;
		System.out.println("Reading content from line No "+fileNo+" file");
		BufferedReader br = null;
		FileReader fr = null;
		String fileName=simPath+"/Line"+fileNo+".txt";
		System.out.println("Reading from file:: "+fileName);
		try {

			fr = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
	        StringBuilder outBuilder = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	        	outBuilder.append(line);
	        	//outBuilder.append('\n');
	        }
	        reader.close();
	        content= outBuilder.toString();
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
		
		if (content == null) content="0";
		System.out.println("Line Number Content::"+content);
		return content;
	}
}
