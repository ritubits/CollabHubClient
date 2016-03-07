package collabhubclient;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import collabhubclient.StartCollaborationClient;

public class StopCollaborationClient {
	
	HttpEntity entity=null;
	
	String projectName;
	String collabName;
	String ipAddTomcat;
	String ipAddMySQL;
	static CloseableHttpClient  collabClient;


	public void setConfigProjectValues(String pName, String cName, String ipT, String ipSQL )
	{
		projectName = pName;
		collabName= cName;
		ipAddTomcat = ipT;
		ipAddMySQL= ipSQL;
	}
		    

	public boolean executeClient() throws Exception {
		
		  //drops table UserActivity- will be implemented later
		//insert COLLAB_STOP status in userDetails table
	    // invoke StopCollaborationServlet
		
		collabClient = StartCollaborationClient.httpclient;
		
		if (collabClient !=null)
		{
			
			projectName = StartCollaborationClient.projectName;
			collabName =StartCollaborationClient.collabName;
			ipAddTomcat = StartCollaborationClient.ipAddTomcat;
			ipAddMySQL = StartCollaborationClient.ipAddMySQL;
			
	    //  System.out.println("http://"+ipAddTomcat+"/examples/servlets/servlet/RegisterProjectServlet?pName="+projectName+"&cName="+collabName+"&ipAddT="+ipAddTomcat+"&ipAddSQL="+ipAddMySQL);
	    	
	    	HttpGet httpget = new HttpGet("http://"+ipAddTomcat+"/examples/servlets/servlet/StopCollaborationServlet?pName="+projectName+"&cName="+collabName+"&ipAddT="+ipAddTomcat+"&ipAddSQL="+ipAddMySQL);
	    	
	    	CloseableHttpResponse response = collabClient.execute(httpget);
	    	  		
	    	System.out.println(response.getProtocolVersion());
	    	System.out.println(response.getStatusLine().getStatusCode());
	    	System.out.println(response.getStatusLine().getReasonPhrase());
	    	String status= response.getStatusLine().toString();
	    	System.out.println("StopCollaborationClient "+response.getStatusLine().toString());
	    	entity= response.getEntity();

	    	response.close();
	        
	    	Vector projectVector = new Vector();
   		  
		    	if (entity != null) {
		    		long len = entity.getContentLength();
		    		if (len != -1 && len < 2048) {
		    		projectVector.add(EntityUtils.toString(entity));
		    		
		    		} else {
		    		// Stream content out
		    			System.out.println("Received empty string from server");
		    		}
		    	}
		    	
		    	Enumeration enumVect = projectVector.elements();
		    	while (enumVect.hasMoreElements())
		    	{
		    		System.out.println("From servlet: "+enumVect.nextElement());
		    	}
		    	
	    	//check if returned status is not correct
	    	if (status.contains("Error")) return false; 
	    	else    	
	    	return true;
		}
		else return false;
	    }
	
	public void closeClient() throws Exception
	{
		try
		{
			collabClient.close();
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
		}
	}

}
