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
	
	HttpEntity entity=null;
	
	String projectName;
	String ownerName;
	String ipAddTomcat;
	String ipAddMySQL;
	CloseableHttpClient httpclient;


	public void setConfigProjectValues(String pName, String oName, String ipT, String ipSQL )
	{
		projectName = pName;
		ownerName= oName;
		ipAddTomcat = ipT;
		ipAddMySQL= ipSQL;
	}
		    
	public boolean executeClient() throws Exception {
		
		   System.out.println("Before creating client: regProject");
		
	  	httpclient = HttpClients.createDefault();
	    System.out.println("After creating client: regProject");
	    
	      System.out.println("http://"+ipAddTomcat+"/examples/servlets/servlet/RegisterProjectServlet?pName="+projectName+"&oName="+ownerName+"&ipAddT="+ipAddTomcat+"&ipAddSQL="+ipAddMySQL);
	    	//HttpGet httpget = new HttpGet("http://localhost:8080/examples/servlets/servlet/RegisterProjectServlet?pName="+projectName+"&oName="+ownerName+"&ipAdd="+ipAdd);
	    	HttpGet httpget = new HttpGet("http://"+ipAddTomcat+"/examples/servlets/servlet/RegisterProjectServlet?pName="+projectName+"&oName="+ownerName+"&ipAddT="+ipAddTomcat+"&ipAddSQL="+ipAddMySQL);
	    	
	    	CloseableHttpResponse response = httpclient.execute(httpget);
	    	  		
	    	System.out.println(response.getProtocolVersion());
	    	System.out.println(response.getStatusLine().getStatusCode());
	    	System.out.println(response.getStatusLine().getReasonPhrase());
	    	String status= response.getStatusLine().toString();
	    	System.out.println("regClient "+response.getStatusLine().toString());
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
