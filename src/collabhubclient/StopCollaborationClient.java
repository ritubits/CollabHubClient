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
	
	boolean DEBUG= true;
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
	    	
	    	HttpGet httpget = new HttpGet("http://"+ipAddTomcat+"/collabserver/StopCollaborationServlet?pName="+projectName+"&cName="+collabName);
	    	
	    	CloseableHttpResponse response = collabClient.execute(httpget);
	    	  		
	    	if (DEBUG) System.out.println(response.getProtocolVersion());
	    	if (DEBUG) System.out.println(response.getStatusLine().getStatusCode());
	    	if (DEBUG) System.out.println(response.getStatusLine().getReasonPhrase());
	    	
	    	String status= response.getStatusLine().toString();
	    	if (DEBUG) System.out.println("StopCollaborationClient "+response.getStatusLine().toString());
	    	entity= response.getEntity();

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
			StartCollaborationClient.httpclient=null;
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
		}
	}

}
