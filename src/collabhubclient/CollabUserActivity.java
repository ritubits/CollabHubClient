package collabhubclient;

import java.util.Enumeration;
import java.util.Vector;

public class CollabUserActivity extends Object{
	
	boolean DEBUG= false;
	public String currentFile=null;
	public int currentline=0;
	public String currentAST=null;
	
	public Vector editFile = null;
	
	public CollabUserActivity()
	{
		
	}
	
	public void setCurrentFile(String name)
	{
		currentFile= name;
	}

	public void setCurrentLine(int line)
	{
		currentline= line;
	}
	
	public void setCurrentAST(String name)
	{
		currentAST= name;
	}
	
	public void setEditFile(Vector name)
	{
		editFile= name;
	}
	
	public String getUserActivityString()
	{
	//first remove current file from editfile list
		//set modes
		String send = null;
		send = "cFile="+currentFile+"&cLine="+currentline+"&cAST="+currentAST+"&eFile=";
		String newString =null;
		if (editFile != null)
		{
			Enumeration enumFiles = editFile.elements();
			while (enumFiles.hasMoreElements())
			{
				//see the cFile--- is same do not add
				Object file = enumFiles.nextElement();
				if (!currentFile.equals(file))
				{
					if (newString != null)
					newString = newString +file.toString()+",";
					else 	newString = file.toString()+",";
				}
			}
			
			send = send + newString;
		}
		if (DEBUG) System.out.println("Sending activity string:: "+send);
		return (send);
	}
}
