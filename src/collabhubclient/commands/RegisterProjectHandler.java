package collabhubclient.commands;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

import collabhubclient.Activator;
import collabhubclient.EntryFormRegProject;
import collabhubclient.RegProjectClient;

public class RegisterProjectHandler implements IHandler {

	boolean DEBUG= false;
	static Boolean success= false;
	static RegProjectClient regObject = null;
	static Boolean registerSuceed = false;
	static String regProjectName= null;
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
		
		//do initialization here
		// executed only once
		if (DEBUG) System.out.println("In Reigister Project handletr");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		//this gets executed every time the button is pressed
		if (DEBUG) System.out.println("In Register Project");
		
		String projectName=Activator.getDefault().getPreferenceStore().getString("projectName");
		String ownerName=Activator.getDefault().getPreferenceStore().getString("ownerName");
		String tomcatIP=Activator.getDefault().getPreferenceStore().getString("tomcatIP");
		String mySQLIP=Activator.getDefault().getPreferenceStore().getString("mySQLIP");
		String levelNumber=Activator.getDefault().getPreferenceStore().getString("levelNumber");
		String collabNumber=Activator.getDefault().getPreferenceStore().getString("collabNumber");
		
		System.out.println(projectName);
		System.out.println(ownerName);
		System.out.println(tomcatIP);
		System.out.println(mySQLIP);
		System.out.println(levelNumber);
		System.out.println(collabNumber);
		
		if (projectName.equals("null") || ownerName.equals("null"))
		{
			JOptionPane.showMessageDialog(null, "Project Name or Owner Name is NULL : Set using Windows -> Preferences -> CollabHub Registration Parameters", "Message Info", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
		JOptionPane.showMessageDialog(null, "Using Parameters:: Project Name: "+projectName+" Owner Name: "+ownerName, "Message Info", JOptionPane.INFORMATION_MESSAGE);
		
		regObject= new RegProjectClient();
		try {
			regObject.setConfigProjectValues(projectName, ownerName, tomcatIP, mySQLIP, levelNumber, collabNumber);
			boolean status= regObject.executeClient();
			if (status)
			{
			// give message written to config file
				// dispose this window
				registerSuceed= true;
				regProjectName = projectName;
				JOptionPane.showMessageDialog(null, "Project Successfully Registered", "Message Info", JOptionPane.INFORMATION_MESSAGE);
				
			}
			else 
				JOptionPane.showMessageDialog(null, "Unable to register the project", "Message Info", JOptionPane.INFORMATION_MESSAGE);
		}
		catch (Exception ex)
		{
			if (DEBUG) System.out.println("Error calling RegClient");
			JOptionPane.showMessageDialog(null, "Unable to register the project", "Message Info", JOptionPane.INFORMATION_MESSAGE);
			ex.printStackTrace();
		}
		finally
		{
			//close client here
			try{
				if (regObject != null) regObject.closeClient();
			}
			catch (Exception ee){
				ee.printStackTrace();
			}
			
		}//finally
		}
	//	EntryFormRegProject eForm= new EntryFormRegProject();
	//	success= eForm.executeForm();
		
				return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
