package collabhubclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



public class CollabUserActivityClient {
	
	boolean DEBUG= true;
	HttpEntity entity=null;
	
	CollabUserActivity userObject;
	static CloseableHttpClient httpclient;
	
    String ipAddT= StartCollaborationClient.getipAddTomcat();
    String collabName = StartCollaborationClient.getCollabName();
    
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
	
	
	public boolean sendUserArtifactGraphContent(String fileContent, String fileName) throws Exception {

    
      File file= writeToTempFile(fileContent);
	//	File file = new File("D://CloneRemoteRepo.java");
      //  File file= new File(fileContent);
        System.out.println("FileContent:::"+fileContent);
        String response = executeMultiPartRequest("http://localhost:8080/collabserver/UserArtifactGraphServlet", file, fileName, "File Upload") ;
        System.out.println("Response : "+response+ "from client: "+collabName);
        
        return true;
	}
	
	
	public File writeToTempFile(String fileContent)
	{
		File tempFile= new File("D://temp_"+collabName+".java");		

			try
			{				
				 if (tempFile.exists()) {
					 tempFile.delete();
			        	tempFile.createNewFile();
			        	}				
			FileWriter fw = new FileWriter(tempFile.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(fileContent);
			bw.newLine();
			bw.close();

			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		return tempFile;
		
	}
	 private static String executeRequest(HttpRequestBase requestBase){
	        String responseString = "" ;

	        InputStream responseStream = null ;
	        CloseableHttpClient client = HttpClients.createDefault();
	        try{
	            HttpResponse response = client.execute(requestBase) ;
	            if (response != null){
	                HttpEntity responseEntity = response.getEntity() ;

	                if (responseEntity != null){
	                    responseStream = responseEntity.getContent() ;
	                    if (responseStream != null){
	                        BufferedReader br = new BufferedReader (new InputStreamReader (responseStream)) ;
	                        String responseLine = br.readLine() ;
	                        String tempResponseString = "" ;
	                        while (responseLine != null){
	                            tempResponseString = tempResponseString + responseLine + System.getProperty("line.separator") ;
	                            responseLine = br.readLine() ;
	                        }
	                        br.close() ;
	                        if (tempResponseString.length() > 0){
	                            responseString = tempResponseString ;
	                        }
	                    }
	                }
	            }
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IllegalStateException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally{
	            if (responseStream != null){
	                try {
	                    responseStream.close() ;
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        
	        try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        return responseString ;
	    }
	 
	 public String executeMultiPartRequest(String urlString, File file, String fileName, String fileDescription) {

	        HttpPost postRequest = new HttpPost (urlString) ;
	        try{

	        	   MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
	        	   multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	        	   multipartEntity.addBinaryBody("upfile", file, ContentType.DEFAULT_BINARY, fileName+"|"+collabName);
	            multipartEntity.addTextBody("text", "FileUpload", ContentType.DEFAULT_BINARY);
	            
	            postRequest.setEntity(multipartEntity.build()) ;
	        }catch (Exception ex){
	            ex.printStackTrace() ;
	        }

	        return executeRequest (postRequest) ;
	    }


}
