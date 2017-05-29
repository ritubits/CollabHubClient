package collabhubclient;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class RegProjectClient {
	
	boolean DEBUG= true;
	HttpEntity entity=null;
	
	String projectName;
	String ownerName;
	String ipAddTomcat;
	String ipAddMySQL;
	String levelNumber;
	String collabNumber;
	CloseableHttpClient httpclient;


	public void setConfigProjectValues(String pName, String oName, String ipT, String ipSQL, String levelNo, String collabNo )
	{
		projectName = pName;
		ownerName= oName;
		ipAddTomcat = ipT;
		ipAddMySQL= ipSQL;
		levelNumber = levelNo;
		collabNumber= collabNo;
	}
		    
	public boolean executeClient() throws Exception {
		
		if (DEBUG) System.out.println("Before creating client: regProject");
		
	  	httpclient = HttpClients.createDefault();
	  	if (DEBUG) System.out.println("After creating client: regProject");
	    
	  	if (DEBUG)  System.out.println("Invoking Servlet RegisterProject"+"http://"+ipAddTomcat+"/collabserver/RegisterProjectServlet?pName="+projectName+"&oName="+ownerName);
	       	HttpGet httpget = new HttpGet("http://"+ipAddTomcat+"/collabserver/RegisterProjectServlet?pName="+projectName+"&oName="+ownerName+"&levelNumber="+levelNumber+"&collabNumber="+collabNumber);
	    	
	    	CloseableHttpResponse response = httpclient.execute(httpget);
	    	  		
	    	if (DEBUG) System.out.println(response.getProtocolVersion());
	    	if (DEBUG)	System.out.println(response.getStatusLine().getStatusCode());
	    	if (DEBUG) System.out.println(response.getStatusLine().getReasonPhrase());
	    	String status= response.getStatusLine().toString();
	    	if (DEBUG) System.out.println("regClient "+response.getStatusLine().toString());
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
	
	public void closeClient() throws Exception
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

}
