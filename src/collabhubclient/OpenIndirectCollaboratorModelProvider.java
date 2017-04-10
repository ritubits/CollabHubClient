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

public enum OpenIndirectCollaboratorModelProvider {
  INSTANCE;

  private ArrayList<String> collaborators;
  CloseableHttpClient collabClient=null;
  String ipAddTomcat =null;
  String collabName=null;
  HttpEntity entity=null;
  
  private OpenIndirectCollaboratorModelProvider() {
	  collaborators = new ArrayList<String>();
    
    if (collaborators == null) collaborators.add("Collaboration Not Started");

    {
    	//create thread here
    	CollaboratorThread myThread= new CollaboratorThread();
    	myThread.start();
    }
    
       
  }

  public ArrayList<String> getCollaborators() {
	  
	  System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!"+collaborators);

    return collaborators;
  }
  
  public void getCollaboratorsFromServlet()
  {

	//invoke servlet //pass the collabName
	  HttpGet httpget = new HttpGet("http://"+ipAddTomcat+"/collabserver/OInCServlet?cName="+collabName);
  	
  	CloseableHttpResponse response;
	try {
		response = collabClient.execute(httpget);
	  		
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
    			System.out.println("EntityUtils.toString(entity): from OpenIndirect"+data);
    			if (data.equals("null")) 	
    				{
    				System.out.println("Data is null:: from OpenIndirect");
    				projectVector.add("No Collaborators");
    				}
    			else projectVector.add(data);
    		
    		} else {
    		// Stream content out
    			System.out.println("Received empty string from server: from OpenIndirect");
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
	  String data= null;
	  if (msg !=null)
	  {
		 
		  String cName=null;
		  String fName=null;
		  
		  String[] temp1;
		  String delimiter1 = "[|]";
		  temp1 = msg.split(delimiter1);
		  
		  String[] temp2;
		  String delimiter2 = "[,]";
		  
		  for(int i =0; i < temp1.length ; i++)
		  {
 
			  temp2 = temp1[i].split(delimiter2);
			  cName= temp2[0];
			  fName= temp2[1];
			  System.out.println("cName::: from OpenIndirect::"+cName);
			  System.out.println("fName::: from OpenIndirect::"+fName);
			  System.out.println("data::: from OpenIndirect::"+data);
			  
		/*	  if (data !=null && data.contains(cName)){
				  collaborators.add(data+"|");
				  System.out.println("data::"+data);
				// data=null;
			  }*/
			  
			  if (data ==null)
			  {
			  data = cName+","+fName;
			  System.out.println("data::"+data);
			  }
			  else 
				  {
				  if (data.contains(cName))
					  	data = data+","+fName;
				  else data = data+ "|"+cName+","+fName;
				  System.out.println("data::"+data);
				  }

	//	  System.out.println("i=" + i + temp1[i]);
		 
		  }
	  				
	  }	
	//  collaborators.add(data);
	//  System.out.println(collaborators.toString());
	  parseForFourArtifact(data);
  }
  
  public void parseForFourArtifact(String msg)
  {
	  if (msg !=null)
	  {
		  System.out.println("msg:: before parseForFourArtifact::"+msg);  
		  String[] temp1;
		  String delimiter1 = "[|]";
		  temp1 = msg.split(delimiter1);
		 
		  String[] temp2;
		  String delimiter2 = "[,]";
		 		  
		  String data=null;
		  String content=null;
		  for(int i =0; i < temp1.length ; i++)
		  {
          data= null;
		  temp2 = temp1[i].split(delimiter2);
		  int j=temp2.length;
		  if (j ==1) data= temp2[0]+","+ "--"+","+ "--"+","+"--"+","+"--"+"|";
		  if (j ==2) data= temp2[0]+","+ temp2[1]+","+ "--"+","+"--"+","+"--"+"|";
		  if (j ==3) data= temp2[0]+","+ temp2[1]+","+ temp2[2]+","+"--"+","+"--"+"|";
		  if (j ==4) data= temp2[0]+","+ temp2[1]+","+ temp2[2]+","+temp2[3]+","+"--"+"|";
		 
		  if (content == null) content = data; 
		  else content = content +data;
		  System.out.println("Data:: after parseForFourArtifact::"+data);
		  System.out.println("Content:: after parseForFourArtifact::"+content);
		  collaborators.add(data);
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
		    	System.out.println("Going to get data: from OpenIndirect");
		    	getCollaboratorsFromServlet();
		    	// if (ConflictMessagesView.viewer!=null) ConflictMessagesView.viewer.refresh();
		    	}
		    
		System.out.println("Going to sleep!!: from OpenIndirect");
			Thread.sleep(1000*5);//5 seconds
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
	 }
}

} 


