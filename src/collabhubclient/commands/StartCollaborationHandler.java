package collabhubclient.commands;


import java.io.FileNotFoundException;
import java.util.Vector;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

import collabhubclient.CollabUserActivity;
import collabhubclient.CollabUserActivityClient;
import collabhubclient.StartCollaborationClient;
import collabhubclient.StartCollaborationForm;

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



public class StartCollaborationHandler implements IHandler {

	boolean DEBUG= true;
	static Boolean success= false;
	IWorkbench workbench =null;
	IWorkbenchPage activePage = null;
	private UIJob activityMethodDataJob;
	private UIJob activityLineDataJob;
	String methodName=null;
	int lineNo;
	boolean compilable=false;
	static CollabUserActivityClient userClient;
	static ExecutionEvent eventObject;
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

		if (DEBUG) System.out.println("In Start Collaboration Project handletr");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub

		eventObject= event;
		if (DEBUG) System.out.println("In StartCollaboration handler");
		StartCollaborationForm eForm= new StartCollaborationForm();
		success= eForm.executeForm();
		if (DEBUG) System.out.println("In StartCollaboration handler");
		
		if (DEBUG) System.out.println("Before collabStarted in Handlers:: "+success);
		
		while (!success)
		{
			success = eForm.getCollabStatus();
			if (DEBUG) System.out.println("collabStarted in Handlers:: "+success);
		}
		
		//		reaches here when collabStarted
	/*	try {
			PlatformUI.getWorkbench().getDecoratorManager().setEnabled("DecorationProject.myDecorator", true);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if (DEBUG) System.out.println("collabStarted outside Handlers:: "+success);
		userClient = new CollabUserActivityClient();
		  
		workbench = PlatformUI.getWorkbench();

		activePage = workbench.getActiveWorkbenchWindow().getActivePage();

		
		
		if (activePage.CHANGE_EDITOR_CLOSE != null)
		{
			
		}
		  Job job = new Job("Activity Job") {
	
		      protected IStatus run(IProgressMonitor monitor) {
		        getUserActivityData();
		    
		        syncUI();
		       
				if (StartCollaborationClient.httpclient != null) return Status.OK_STATUS;
				else return Status.CANCEL_STATUS;
		      }

		    };
		    job.setUser(true);
		    job.schedule();
		
				return null;
	}

	
	  private void getUserActivityData() {
		  
		  CollabUserActivity userObject = new CollabUserActivity();

		  while (true)
		  {
		      try {
		    	  compilable= false;
					userObject.setCurrentFile(getCurrentFileName());
					activityLineData();
					userObject.setCurrentLine(lineNo);
					activityMethodNameData();
					if (DEBUG) System.out.println("MethodName from activityMethodNameData:: "+methodName);
					userObject.setCurrentAST(methodName);
					userObject.setEditFile(getAllFiles());
					
					Boolean exist= userClient.getCollabClient();
					if (DEBUG) System.out.println("Exist:: "+exist);
					if (exist) 
						{
						//if collaborating
						userClient.setUserObject(userObject);
						boolean output= userClient.updateUserActivityTable();
						if (DEBUG) System.out.println("From Servlet:: in StartHandler:: "+ output);
						}

					  //  	if (activePage.getActiveEditor().isDirty())
			    	{
			    		if (DEBUG) System.out.println(" Dirty:: "+activePage.getActiveEditor().isDirty());
			    		if (DEBUG) System.out.println("compilable:: "+compilable);			    	
			    		if (compilable) sendCurrentArtifact(getCurrentFileName());
			    		//Thread.sleep(1000*30);// pick activity data info every 60 second
			    	}
			    	

			        Thread.sleep(1000*20);// pick activity data info every 20 second
			    	
		      } catch (Exception e) {
		        e.printStackTrace();
		      }
		      if (DEBUG) System.out.println("In getUserActivityData");
		    }
		  }
	  
	  private void syncUI() {
		    Display.getDefault().asyncExec(new Runnable() {
		      public void run() {		    	  
		    	  if (DEBUG) System.out.println("sync with UI");
		      }
		    });

		  }
	
	  
	  public boolean activityMethodNameData() {
		
			if (activityMethodDataJob==null) {
				activityMethodDataJob = new UIJob(Display.getDefault(), "Obtaining Method Name Data") {
					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {
						if (DEBUG) System.out.println("in runInUIThread");
					//	System.out.println("Cursor::"+ getCursorPosition());
						methodName= getCurrentMethod();
						if (DEBUG) System.out.println("MethodName in activityMethodNameData:: "+methodName);
				//		PlatformUI.getWorkbench().getDecoratorManager().update("DecorationProject.myDecorator");
						if (StartCollaborationClient.httpclient != null) return Status.OK_STATUS;
						else return Status.CANCEL_STATUS;
					}
				};
				activityMethodDataJob.setSystem(true);
			}
			activityMethodDataJob.schedule();
			return true;
		}
	  
	 public boolean activityLineData() {
			
			if (activityLineDataJob==null) {
				activityLineDataJob = new UIJob(Display.getDefault(), "Obtaining Line Data") {
					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {
					
						lineNo= getCursorPosition();
						if (StartCollaborationClient.httpclient != null) return Status.OK_STATUS;
						else return Status.CANCEL_STATUS;
					}
				};
				activityLineDataJob.setSystem(true);
			}
			activityLineDataJob.schedule();
			return true;
		}
	  
	  
public String getCurrentFileName() throws FileNotFoundException
		{
			String name=null;
			try{
				if (activePage !=null)
				  name = activePage.getActiveEditor().getEditorInput().getName();
				if (DEBUG) System.out.println("CurrentFileName:: "+name);

			} catch(Exception e)
			{
				e.printStackTrace();
			}
			 return name;
			
		}
		
		
public Vector getAllFiles() throws FileNotFoundException
		{
			IEditorReference[] name=null;
			Vector fileNames= new Vector();
		try{
				if (activePage !=null)
					name = activePage.getEditorReferences();
		 
			  int i =name.length;
			  for (int j=0; j<i; j++)
			  {
				  if (DEBUG) System.out.println("All files:: "+name[j].getName());
			    fileNames.add(name[j].getName());
			  }		  			  
			} catch(Exception e)
			{
				e.printStackTrace();
			}		
			return fileNames;
		}
		
public int getCursorPosition()
		{

	        IEditorPart editor = activePage.getActiveEditor();
	        if(editor instanceof ITextEditor){
	            ISelectionProvider selectionProvider = ((ITextEditor)editor).getSelectionProvider();
	            ISelection selection = selectionProvider.getSelection();
	            if (selection instanceof ITextSelection) {
	                ITextSelection textSelection = (ITextSelection)selection;
	                IDocumentProvider provider = ((ITextEditor)editor).getDocumentProvider();
	                IDocument document = provider.getDocument(editor.getEditorInput());
	                int line = textSelection.getStartLine();
	//                int column =0;
	//                try {
	//                    column = textSelection.getOffset() - document.getLineOffset(line);
	//                } catch (Exception e) {
	//                    e.printStackTrace();
	//                }
	                return line;
	            }
	        }
	        
	        return 0;
		}//getcursor
		
	
public String getCurrentMethod()
		{
		IEditorPart activeEditor  = activePage.getActiveEditor();

		if(activeEditor instanceof JavaEditor) {
		    
			ICompilationUnit root = (ICompilationUnit) EditorUtility.getEditorInputJavaElement(activeEditor, false);
		    try {
		        ITextSelection sel = (ITextSelection) ((JavaEditor) activeEditor).getSelectionProvider().getSelection();
		        int offset = sel.getOffset();
		       // compilable= root.getJavaProject().hasBuildState();
		      //Get compilation Unit
		        ASTParser parser = ASTParser.newParser(AST.JLS4);
		        parser.setKind(ASTParser.K_COMPILATION_UNIT);
		        parser.setSource(root);
		        parser.setResolveBindings(true); 
		        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		        int error=0;
		        IProblem[] problemList= cu.getProblems();
		        if (problemList.length != 0)
		        {
		        	
		        	for ( int i=0; i< problemList.length && error ==0; i++) {
		        		IProblem iProblem = problemList[i];
		        	   //If it is an error
		        	   if (iProblem.isError())
		        	   {
		        		   compilable= false;
		        		   error ++;
		        		   
		        	   }
		        	}
		        	if (error ==0) compilable= true;
		        	
		        }
		        else compilable=true;
		        
		        if (DEBUG) System.out.println("compilable from getCurrentMethod():: "+compilable);	
		        IJavaElement element = root.getElementAt(offset);
		        
		        if (element !=null)
		        {
			        if(element.getElementType() == IJavaElement.METHOD){
			            return element.getElementName();			            			 
			        }			        
		        }
		    } catch (JavaModelException e) {
		        e.printStackTrace();
		    }
		}
		return null;
	}

 public void sendCurrentArtifact(String currentFileName)
 {
	 if (DEBUG) System.out.println("Sending Artifact Graph");
	 IAdaptable editorPart = null;
	 
	 IEditorPart activeEditor  = activePage.getActiveEditor();

		if(activeEditor instanceof JavaEditor) {
			ITextEditor editor = (ITextEditor) activeEditor.getAdapter(ITextEditor.class);

		 if (editor != null) {
		   IDocumentProvider provider = editor.getDocumentProvider();
		   IDocument document = provider.getDocument(editor.getEditorInput());
		   if (DEBUG) System.out.println("file Content::"+document.get());
		   
		   Boolean exist= userClient.getCollabClient();
	//	   if (DEBUG) System.out.println("Exist:: "+exist);
			if (exist) 
				{
				//if collaborating
				try
				{
				boolean output= userClient.sendUserArtifactGraphContent(document.get(), currentFileName);
				if (DEBUG) System.out.println("From Servlet:: in StartHandler:: sending artifact graph");
				}
				catch (Exception e)
					{
						e.printStackTrace();
					}
				}

		 }
		}
 }

		@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
		if (DEBUG) System.out.println("from removeHandlerListener");

	}

		
}
