package collabhubclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

public enum SemiDirectModelProvider {
  INSTANCE;

  private ArrayList<String> collaborators;
  CloseableHttpClient collabClient=null;
  String ipAddTomcat =null;
  String collabName=null;
  HttpEntity entity=null;
  
  private SemiDirectModelProvider() {
	  collaborators = new ArrayList<String>();
    
    if (collaborators == null) collaborators.add("Collaboration Not Started");

    {
    	//create thread here
    	CollaboratorThread myThread= new CollaboratorThread();
    	myThread.start();
    }
    
       
  }

  public ArrayList<String> getCollaborators() {
	  
    return collaborators;
  }
  
  public void getCollaboratorsFromServlet()
  {

	//invoke servlet //pass the collabName
	  HttpGet httpget = new HttpGet("http://"+ipAddTomcat+"/collabserver/ODCServlet?cName="+collabName);
  	
  	CloseableHttpResponse response=null;
	try {
		if (collabClient!=null) response = collabClient.execute(httpget);
	  		
	System.out.println(response.getProtocolVersion());
	System.out.println(response.getStatusLine().getStatusCode());
	System.out.println(response.getStatusLine().getReasonPhrase());
	
	String status= response.getStatusLine().toString();
	entity= response.getEntity();
	response.close();
	
	//check if returned status is not correct
		if (status.contains("Error")) System.out.println("Error::");

		Vector projectVector = new Vector();
 		  String data; 		  
    	if (entity != null) {
    		long len = entity.getContentLength();
    		if (len != -1 && len < 2048) {
    			data= EntityUtils.toString(entity);
    			System.out.println("EntityUtils.toString(entity)"+data);
    			if (data.equals("null")) 	
    				{
    				System.out.println("Data is null");
    				projectVector.add("No Collaborators");
    				}
    			else projectVector.add(data);
    		
    		} else {
    		// Stream content out
    			System.out.println("Received empty string from server");
    		}
    	}
    	
    	Enumeration enumVect = projectVector.elements();
    	while (enumVect.hasMoreElements())
    	{
    		parse(enumVect.nextElement().toString());
    	//	System.out.println("From servlet: "+enumVect.nextElement());
    	}
	
    	 
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	

  }
  
  private void parse(String msg)
  {
	  if (msg !=null)
	  {
	  String[] temp1;
	  String delimiter1 = "[,]";
	  temp1 = msg.split(delimiter1);
	  for(int i =0; i < temp1.length ; i++)
	  {
	  System.out.println("i=" + i + temp1[i]);
	  collaborators.add(temp1[i]);
	  }
	  				
	  }	
	  
  }
  

class CollaboratorThread extends Thread{
	
	 public void run(){  
		 
		 while (true)
		 {
			 collaborators.clear();
		 try {
		    if (collabClient ==null)
		    {
		    	collaborators.add("No Collaborators");
		    	collabClient= StartCollaborationClient.httpclient;
		        ipAddTomcat=StartCollaborationClient.ipAddTomcat;
		        collabName=StartCollaborationClient.collabName;
		        System.out.println("CollabClient::"+collabClient);
		    }
		    
		    if (collabClient !=null) 
		    	{
		    	collaborators.clear();
		    	System.out.println("Going to get data&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		    	getCollaboratorsFromServlet();
		    	// if (ConflictMessagesView.viewer!=null) ConflictMessagesView.viewer.refresh();
		    	}
		    
		System.out.println("Going to sleep!!!!!!!!!!!!!!!!!!!!!!!!!");
			Thread.sleep(1000*10);//5 seconds
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
	 }
}

} 


