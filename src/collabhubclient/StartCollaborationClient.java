package collabhubclient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class StartCollaborationClient {
	
	boolean DEBUG= false;
	HttpEntity entity=null;
	
	static String projectName;
	static String collabName;
	static String ipAddTomcat;
	static String ipAddMySQL;
	public static CloseableHttpClient httpclient;


	public void setConfigProjectValues(String pName, String cName, String ipT, String ipSQL )
	{
		projectName = pName;
		collabName= cName;
		ipAddTomcat = ipT;
		ipAddMySQL= ipSQL;
		
		writeConfigDetailstoFileForDecorator();
	}
	
	public void writeConfigDetailstoFileForDecorator()
	{

			File configFile= new File("D://configDecorator.java");		

				try
				{				
					 if (configFile.exists()) {
						 configFile.delete();
						 configFile.createNewFile();
				        	}				
				FileWriter fw = new FileWriter(configFile.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("projectName|"+projectName);
				bw.newLine();
				
				bw.write("collabName|"+collabName);
				bw.newLine();
				
				bw.write("ipAddTomcat|"+ipAddTomcat);
				bw.newLine();
				
				bw.write("ipAddMySQL|"+ipAddMySQL);
				bw.newLine();
				
				bw.close();

				
				} catch (IOException e) {
					e.printStackTrace();
			
		}
	}
	public boolean createCollabClient()
	{
		if (DEBUG) System.out.println("Before creating client: StartCollaborationClient");
		 httpclient = HttpClients.createDefault();
		 
		 if (DEBUG) System.out.println("After creating client: StartCollaborationClient");
		 
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
		
		  
	    
		if (DEBUG) System.out.println("Invoking Servlet StartCollaboration"+"http://"+ipAddTomcat+"/collabserver/StartCollaborationServlet?pName="+projectName+"&cName="+collabName);
	    
	    	HttpGet httpget = new HttpGet("http://"+ipAddTomcat+"/collabserver/StartCollaborationServlet?pName="+projectName+"&cName="+collabName);
	    	
	    	CloseableHttpResponse response = httpclient.execute(httpget);
	    	  		
	    	if (DEBUG) System.out.println(response.getProtocolVersion());
	    	if (DEBUG) System.out.println(response.getStatusLine().getStatusCode());
	    	if (DEBUG) System.out.println(response.getStatusLine().getReasonPhrase());
	    	String status= response.getStatusLine().toString();
	    	if (DEBUG) System.out.println("StartCollaborationClient "+response.getStatusLine().toString());
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
