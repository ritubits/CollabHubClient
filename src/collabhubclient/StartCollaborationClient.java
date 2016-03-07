package collabhubclient;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class StartCollaborationClient {
	
	HttpEntity entity=null;
	
	static String projectName;
	static String collabName;
	static String ipAddTomcat;
	static String ipAddMySQL;
	static CloseableHttpClient httpclient;


	public void setConfigProjectValues(String pName, String cName, String ipT, String ipSQL )
	{
		projectName = pName;
		collabName= cName;
		ipAddTomcat = ipT;
		ipAddMySQL= ipSQL;
	}
		    
	public boolean createCollabClient()
	{
		 System.out.println("Before creating client: StartCollaborationClient");
		 httpclient = HttpClients.createDefault();
		 
		 System.out.println("After creating client: StartCollaborationClient");
		 
		 if (httpclient != null) return true; 
		 else return false;
		
	}
	
	public CloseableHttpClient getCollabClient()
	{
		
		return httpclient;
				
	}
	
	public static String getProjectName()
	{
		
		return projectName;
				
	}
	
	public static String getCollabName()
	{
		
		return collabName;
				
	}
	
	public static String getipAddTomcat()
	{
		
		return ipAddTomcat;
				
	}
	
	public static String getipAddMySQL()
	{
		
		return ipAddMySQL;
				
	}
	public boolean updateUserTable() throws Exception {
		
		  
	    
	      System.out.println("http://"+ipAddTomcat+"/examples/servlets/servlet/RegisterProjectServlet?pName="+projectName+"&cName="+collabName+"&ipAddT="+ipAddTomcat+"&ipAddSQL="+ipAddMySQL);
	    
	    	HttpGet httpget = new HttpGet("http://"+ipAddTomcat+"/examples/servlets/servlet/StartCollaborationServlet?pName="+projectName+"&cName="+collabName+"&ipAddT="+ipAddTomcat+"&ipAddSQL="+ipAddMySQL);
	    	
	    	CloseableHttpResponse response = httpclient.execute(httpget);
	    	  		
	    	System.out.println(response.getProtocolVersion());
	    	System.out.println(response.getStatusLine().getStatusCode());
	    	System.out.println(response.getStatusLine().getReasonPhrase());
	    	String status= response.getStatusLine().toString();
	    	System.out.println("StartCollaborationClient "+response.getStatusLine().toString());
	    	entity= response.getEntity();
	    	response.close();
	        
	  /*  	Vector projectVector = new Vector();
   		  
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
	    	return true;*/
	    	
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
