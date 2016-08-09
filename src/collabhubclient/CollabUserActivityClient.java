package collabhubclient;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.NameValuePair; 
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


public class CollabUserActivityClient {
	
	boolean DEBUG= true;
	HttpEntity entity=null;
	
	CollabUserActivity userObject;
	static CloseableHttpClient httpclient;
		    
	public boolean getCollabClient()
	{
		httpclient= StartCollaborationClient.httpclient;
		
		if (DEBUG) System.out.println("Obtained client from StartCollaborationClient");
		 
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
	    	if (DEBUG) System.out.println("Invoking Servlet:: "+"http://"+ipAddT+"/collabserver/UserActivityServlet?&cName="+collabName+"&"+send);
	    	CloseableHttpResponse response = httpclient.execute(httpget);
	    	  		
	    	if (DEBUG) 
	    		{
	    		System.out.println(response.getProtocolVersion());
	    		System.out.println(response.getStatusLine().getStatusCode());
		    	System.out.println(response.getStatusLine().getReasonPhrase());
	    		}
	    	
	    	String status= response.getStatusLine().toString();
	    	if (DEBUG) System.out.println("CollabUserActivityClient "+response.getStatusLine().toString());
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
		    		if (DEBUG) System.out.println("From servlet: "+enumVect.nextElement());
		    	}
		    	
	    	//check if returned status is not correct
	    	if (status.contains("Error")) return false; 
	    	else    	
	    	return true;
	 
	    }
	
	
/*	public boolean sendUserArtifactGraphContent(String fileContent, String fileName) throws Exception {
		
	    String ipAddT= StartCollaborationClient.getipAddTomcat();
	    String collabName = StartCollaborationClient.getCollabName();
    
		fileContent="public classsecond";
		if (DEBUG) System.out.println("fileContent:: "+fileContent);

	    //	HttpGet httpget = new HttpGet("http://"+ipAddT+"/collabserver/UserArtifactGraphServlet?collabName="+collabName+"&fileName="+fileName+"&fileContent="+fileContent);
	  //  	HttpGet httpget = new HttpGet("http://"+ipAddT+"/collabserver/UserArtifactGraphServlet?&collabName="+collabName+"&fileName="+fileName);
	  //  	System.out.println("Invoking Servlet:: "+"http://"+ipAddT+"/collabserver/UserArtifactGraphServlet?&cName="+collabName+"&fileContent="+fileContent);
	    
	    //	CloseableHttpResponse response = httpclient.execute(httpget);
	        
	    	HttpPost post = new HttpPost("http://localhost:8080/ServletExample/SampleServlet");
	    	post.setHeader("Content-Type", "application/xml");
	    //	post.setEntity(new StringEntity(generateXML()));
	    //	HttpClient client = new DefaultHttpClient();
	    	HttpResponse response = client.execute(post);
	    	
	        
	    	if (DEBUG)
	    	{
	    	System.out.println(response.getProtocolVersion());
	    	System.out.println(response.getStatusLine().getStatusCode());
	    	System.out.println(response.getStatusLine().getReasonPhrase());
	    	}
	    	String status= response.getStatusLine().toString();
	    	if (DEBUG) System.out.println("sendUserArtifactGraphContent "+response.getStatusLine().toString());
	    	entity= response.getEntity();

	        response.close();
	        
	    	Vector projectVector = new Vector();
   		  
		    	if (entity != null) {
		    		long len = entity.getContentLength();
		    		if (len != -1 && len < 2048) {
		    		projectVector.add(EntityUtils.toString(entity));
		    		
		    		} else {
		    		// Stream content out
		    			if (DEBUG) System.out.println("Received empty string from server:: sendUserArtifactGraphContent");
		    		}
		    	}
		    	
		    	Enumeration enumVect = projectVector.elements();
		    	while (enumVect.hasMoreElements())
		    	{
		    		if (DEBUG) System.out.println("From servlet: sendUserArtifactGraphContent"+enumVect.nextElement());
		    	}
		    	
	    	//check if returned status is not correct
	    	if (status.contains("Error")) return false; 
	    	else    	
	    	return true;
	}*/
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
