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
import org.eclipse.ui.internal.decorators.DecoratorManager;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import collabhubclient.CollabUserActivity;
import collabhubclient.CollabUserActivityClient;
import collabhubclient.StartCollaborationForm;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
//import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;



public class StartCollaborationHandler implements IHandler {

	static Boolean success= false;
	IWorkbench workbench =null;
	IWorkbenchPage activePage = null;
	private UIJob activityMethodDataJob;
	private UIJob activityLineDataJob;
	String methodName=null;
	int lineNo;
	static CollabUserActivityClient userClient;

	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

		System.out.println("In Start Collaboration Project handletr");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub

		System.out.println("In StartCollaboration handler");
		StartCollaborationForm eForm= new StartCollaborationForm();
		success= eForm.executeForm();
		System.out.println("In StartCollaboration handler");
		
		System.out.println("Before collabStarted in Handlers:: "+success);
		
		while (!success)
		{
			success = eForm.getCollabStatus();
			System.out.println("collabStarted in Handlers:: "+success);
		}
		
		//		reaches here when collabStarted
		
		System.out.println("collabStarted outside Handlers:: "+success);
		userClient = new CollabUserActivityClient();
		  
		workbench = PlatformUI.getWorkbench();
		activePage = workbench.getActiveWorkbenchWindow().getActivePage();

	  
		  Job job = new Job("Activity Job") {
	
		      protected IStatus run(IProgressMonitor monitor) {
		        getUserActivityData();
		    
		        syncUI();
		       
		        return Status.OK_STATUS;
		      }

		    };
		    job.setUser(true);
		    job.schedule();
		
				return null;
	}

	
	  private void getUserActivityData() {
		  
		  CollabUserActivity userObject = new CollabUserActivity();
		
		  activePage.getActiveEditor().isDirty();
		  System.out.println(" Dirty:: "+activePage.getActiveEditor().isDirty());
		  
		  for (int i = 0; i < 100; i++)  {
		      try {
		   
		        Thread.sleep(1000*5);// pick activity data info every second
		        
												
				userObject.setCurrentFile(getCurrentFileName());
					activityLineData();
					userObject.setCurrentLine(lineNo);
					activityMethodNameData();
					System.out.println("MethodName from activityMethodNameData:: "+methodName);
					userObject.setCurrentAST(methodName);
					userObject.setEditFile(getAllFiles());
					
				/*	userObject.setCurrentFile("first.java");
					userObject.setCurrentLine(0);
					userObject.setCurrentAST("set");
					userObject.setEditFile(getAllFiles());*/
					
							Boolean exist= userClient.getCollabClient();
					System.out.println("Exist:: "+exist);
					if (exist) 
						{
						//if collaborating
						userClient.setUserObject(userObject);
						boolean output= userClient.updateUserActivityTable();
						System.out.println("From Servlet:: in StartHandler:: "+ output);
						}
					 //call activityClient here for sending information
					 //create userActivityObject
					 //cretae userActivityClient
					 //client sends the activity info to the activityservlet
					 //Activity servlet writes to the table														 
		        	       
		      } catch (Exception e) {
		        e.printStackTrace();
		      }
		      System.out.println("In getUserActivityData");
		    }
		  }
	  
	  private void syncUI() {
		    Display.getDefault().asyncExec(new Runnable() {
		      public void run() {		    	  
			      System.out.println("sync with UI");
		      }
		    });

		  }
	
	  
	  public boolean activityMethodNameData() {
		
			if (activityMethodDataJob==null) {
				activityMethodDataJob = new UIJob(Display.getDefault(), "Obtaining Method Name Data") {
					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {
						System.out.println("in runInUIThread");
					//	System.out.println("Cursor::"+ getCursorPosition());
						methodName= getCurrentMethod();
						System.out.println("MethodName in activityMethodNameData:: "+methodName);
						return Status.OK_STATUS;
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
						return Status.OK_STATUS;
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
				
		    System.out.println("CurrentFileName:: "+name);

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
			    System.out.println("All files:: "+name[j].getName());
			    fileNames.add(name[j].getName());
			  }
			//  fileNames.add("four.java");
			//  fileNames.add("second.java");
			  			  
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
		System.out.println("from removeHandlerListener");

	}
	

		
}
