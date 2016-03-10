package collabhubclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class DeRegProjectClient {
	
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
		
		   System.out.println("Before creating client: DeregProject");
		
	  	httpclient = HttpClients.createDefault();
	    System.out.println("After creating client: DeregProject");
	    
	        System.out.println("Invoking Sevlet DeRegister"+"http://"+ipAddTomcat+"/collabserver/DeRegisterProjectServlet?pName="+projectName+"&oName="+ownerName);
	    	HttpGet httpget = new HttpGet("http://"+ipAddTomcat+"/collabserver/DeRegisterProjectServlet?pName="+projectName+"&oName="+ownerName);
	    	
	    	CloseableHttpResponse response = httpclient.execute(httpget);
	    	  		
	    	System.out.println(response.getProtocolVersion());
	    	System.out.println(response.getStatusLine().getStatusCode());
	    	System.out.println(response.getStatusLine().getReasonPhrase());
	    	String status= response.getStatusLine().toString();
	    	System.out.println("DeregClient "+response.getStatusLine().toString());
	    	
	    	response.close();
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
