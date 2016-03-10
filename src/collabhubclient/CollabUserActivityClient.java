package collabhubclient;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;


public class CollabUserActivityClient {
	
	HttpEntity entity=null;
	
	CollabUserActivity userObject;
	static CloseableHttpClient httpclient;

		    
	public boolean getCollabClient()
	{
		httpclient= StartCollaborationClient.httpclient;
		
		 System.out.println("Obtained client from StartCollaborationClient");
		 
		 if (httpclient != null) return true; 
		 else return false;
		
	}
	

	public void setUserObject(CollabUserActivity user)
	{
		
		userObject = user;
				
	}
	
	public boolean updateUserActivityTable() throws Exception {
		
		  
	    String send = userObject.getUserActivityString();
	    String ipAddT= StartCollaborationClient.getipAddTomcat();
	    String ipAddS = StartCollaborationClient.getipAddMySQL();
	    String collabName = StartCollaborationClient.getCollabName();
	  
	    	HttpGet httpget = new HttpGet("http://"+ipAddT+"/collabserver/UserActivityServlet?&cName="+collabName+"&"+send);
	    	System.out.println("Invoking Servlet:: "+"http://"+ipAddT+"/collabserver/UserActivityServlet?&cName="+collabName+"&"+send);
	    	CloseableHttpResponse response = httpclient.execute(httpget);
	    	  		
	    	System.out.println(response.getProtocolVersion());
	    	System.out.println(response.getStatusLine().getStatusCode());
	    	System.out.println(response.getStatusLine().getReasonPhrase());
	    	String status= response.getStatusLine().toString();
	    	System.out.println("CollabUserActivityClient "+response.getStatusLine().toString());
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
	
/*	public void closeClient() throws Exception
	{
		try
		{
			httpclient.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
*/
}
