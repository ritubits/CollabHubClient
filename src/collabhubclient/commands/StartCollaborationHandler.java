package collabhubclient.commands;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;

import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.ui.ide.IDE;

import javax.swing.JOptionPane;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import collabhubclient.Activator;
import collabhubclient.CollabUserActivity;
import collabhubclient.CollabUserActivityClient;
import collabhubclient.StartCollaborationClient;
import collabhubclient.StartCollaborationForm;



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
	static StartCollaborationClient collabClient = null;
	static String regProjectName= null;
	static ExecutionEvent eventObject;
	
	private BrokerProvider provider = new BrokerProvider();
	
	
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

		if (DEBUG) System.out.println("In Start Collaboration Project handletr");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public StartCollaborationClient getCollabClient()
	{
		//after starting collaboration
		//all should use this client
		return collabClient;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub

		eventObject= event;
		if (DEBUG) System.out.println("In StartCollaboration handler");
		
		String projectName=Activator.getDefault().getPreferenceStore().getString("projectName");
		String collabName=Activator.getDefault().getPreferenceStore().getString("collabName");
		String tomcatIP=Activator.getDefault().getPreferenceStore().getString("tomcatIP");
		String mySQLIP=Activator.getDefault().getPreferenceStore().getString("mySQLIP");
		String simulationMode=Activator.getDefault().getPreferenceStore().getString("simulationMode");
		String simulationPath=Activator.getDefault().getPreferenceStore().getString("simulationPath");
		
		  
		System.out.println(projectName);
		System.out.println(collabName);
		System.out.println(tomcatIP);
		System.out.println(mySQLIP);
		System.out.println(simulationMode);
		System.out.println(simulationPath);
		
		if (projectName.equals("null") || collabName.equals("null"))
		{
			JOptionPane.showMessageDialog(null, "Project Name or Collaborator Name is NULL : Set using Windows -> Preferences -> CollabHub Connection Parameters", "Message Info", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
		JOptionPane.showMessageDialog(null, "Using Parameters:: Project Name: "+projectName+" Collaborator Name: "+collabName, "Message Info", JOptionPane.INFORMATION_MESSAGE);

		
		try {
					
			collabClient= new StartCollaborationClient();
			collabClient.setConfigProjectValues(projectName, collabName,tomcatIP,mySQLIP);
			
			boolean done = collabClient.createCollabClient();
			boolean status= false;
			boolean validCollab = false;
			// if client created
			if (done)
			{
				//check here for allowed number of clients
				
				//if valid then move ahead
				validCollab= collabClient.getAllowedCollaborators();
				System.out.println("Valid::"+validCollab);
				if (validCollab)
				{
					status= collabClient.updateUserTable();
					if (status)
					{
						regProjectName = projectName;
						JOptionPane.showMessageDialog(null, "Successfully Connected to CollabHub", "Message Info", JOptionPane.INFORMATION_MESSAGE);
						
					}
					else 
					{
						//close collabClient
						collabClient.closeClient();
						JOptionPane.showMessageDialog(null, "Unable to connect to CollabHub", "Message Info", JOptionPane.INFORMATION_MESSAGE);
					}
				}//validCollab
				else
				{
					//close collabClient
					collabClient.closeClient();
					JOptionPane.showMessageDialog(null, "You cannot join for collaboration: Allowable limit over", "Message Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}//if done
		
		
	
		  
			if (done && validCollab && status)
			{
				workbench = PlatformUI.getWorkbench();
		
				activePage = workbench.getActiveWorkbenchWindow().getActivePage();
				userClient = new CollabUserActivityClient();
				HashMap<String, Object> argmaps = new HashMap<>();
				argmaps.put("activepage", activePage);
				argmaps.put("workbench", workbench);
				argmaps.put("client", userClient);
				argmaps.put("simulationMode", simulationMode);
				argmaps.put("simulationPath", simulationPath);
				provider.getBroker().post(CollabEventsConstants.COLLAB_TOPIC_START, argmaps);
			}

	
		}catch (Exception ex)
		{
			if (DEBUG) System.out.println("Error calling collabClient");
			JOptionPane.showMessageDialog(null, "Unable to connect to CollabHub", "Message Info", JOptionPane.INFORMATION_MESSAGE);
			ex.printStackTrace();
		}
		}
		
		//test live preview
	/*	try{
			boolean status= false;
			if (collabClient!=null) 
				{
				status= LivePreviewFunction(collabClient);
				if (status== true) OpenNewIDE("DevR");
				}
		}catch (Exception e)
		{
			e.printStackTrace();
		}*/
		
				return null;
	}

	public void OpenNewIDE(String collabName)
	{
		 
		if (activePage != null) {
	        IEditorPart editor = activePage.getActiveEditor();
	        if (editor != null) {
	            IEditorInput input = editor.getEditorInput();
	            if (input instanceof IFileEditorInput) {
	            	
	            	File file = new File("/temp/"+collabName+"_artifact.txt");
	                IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.toURI());
          try {
              IDE.openEditorOnFileStore(activePage, fileStore);
          } catch (Exception e) {
              // TODO error handling
          }
	     }
	     }
		}
	}


	public boolean LivePreviewFunction( StartCollaborationClient collabClient) throws Exception
	{
		//invoke Servlet
		
		CloseableHttpClient httpclient= StartCollaborationClient.httpclient;
		HttpGet httpget = new HttpGet("http://"+"localhost:8080"+"/collabserver/LivePreviewServlet?&cName="+"DevR");
    	if (DEBUG) System.out.println("Invoking Servlet:: "+"http://"+"localhost:8080"+"/collabserver/LivePreviewServlet?&cName="+"DevR");
    	CloseableHttpResponse response = httpclient.execute(httpget);
    	  		
    	if (DEBUG) 
    		{
    		System.out.println(response.getProtocolVersion());
    		System.out.println(response.getStatusLine().getStatusCode());
	    	System.out.println(response.getStatusLine().getReasonPhrase());
    		}
    	
    	String status= response.getStatusLine().toString();
    	if (DEBUG) System.out.println("CollabUserActivityClient "+response.getStatusLine().toString());
    	HttpEntity entity= response.getEntity();

        response.close();
        
    	Vector projectVector = new Vector();
		  
	    	if (entity != null) {
	    		long len = entity.getContentLength();
	    		if (len != -1 && len < 2048) {
	    		projectVector.add(EntityUtils.toString(entity));
	    		
	    		} else {
	    		// Stream content out
	    			if (DEBUG) System.out.println("Received empty string from server");
	    		}
	    	}
	    	
	    	Enumeration enumVect = projectVector.elements();
	    	while (enumVect.hasMoreElements())
	    	{
	    		String fileContent= (String) enumVect.nextElement();
	    		if (DEBUG) System.out.println("From LivePreview servlet: "+fileContent);
	    		//write to file at temp/collabName_artifact.java
	    		
	    		 try {
	    	            FileWriter writer = new FileWriter("/temp/"+"DevR"+"_artifact.txt", false);
	    	            writer.write(fileContent);
	    	            
	    	            writer.close();
	    	        } catch (IOException e) {
	    	            e.printStackTrace();
	    	        }
	    		
	    	}
	    	
	    	
	    	
    	//check if returned status is not correct
    	if (status.contains("Error")) return false; 
    	else    	
    	return true;
	}
	
	  private void getUserActivityData() {
		  
		  CollabUserActivity userObject = new CollabUserActivity();

	//	  while (true)
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
			    	

			      //  Thread.sleep(1000*20);// pick activity data info every 20 second
			    	
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
				if (activePage !=null && (activePage.getActiveEditor()!=null) && (activePage.getActiveEditor().getEditorInput() !=null))
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
	                int line = textSelection.getStartLine()+1;
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
			            return ("M"+element.getElementName()+"()");			            			 
			        }	
			        
			        if(element.getElementType() == IJavaElement.FIELD){
			            return ("F"+element.getElementName());			            			 
			        }
			        
			        if(element.getElementType() == IJavaElement.IMPORT_DECLARATION){
			            return ("I"+element.getElementName());			            			 
			        }
			        
			        if(element.getElementType() == IJavaElement.PACKAGE_DECLARATION){
			            return ("P"+element.getElementName());			            			 
			        }
			        
			        if(element.getElementType() == IJavaElement.LOCAL_VARIABLE){
			            return ("L"+element.getElementName());			            			 
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

	
	private class ContentSimulationThread extends Thread{
		
		 public void run(){  
			 
			 while (true)
			 {
				
			 try {
			  			
				Thread.sleep(1000*60*2);//2 minutes
				//setCurrentWorkSpaceContent();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		 }
		 }
		 

	}
		
}
