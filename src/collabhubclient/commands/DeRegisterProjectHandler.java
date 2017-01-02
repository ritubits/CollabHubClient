package collabhubclient.commands;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

import collabhubclient.Activator;
import collabhubclient.DeRegProjectClient;
import collabhubclient.EntryFormDeRegProject;

public class DeRegisterProjectHandler implements IHandler {

	
	boolean DEBUG= false;
	static DeRegProjectClient deRegObject = null;
	
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		// check if project registered
		if (DEBUG) System.out.println("In DeRegister Project");
		deRegObject= new DeRegProjectClient();
		
		String projectName=Activator.getDefault().getPreferenceStore().getString("projectName");
		String ownerName=Activator.getDefault().getPreferenceStore().getString("ownerName");
		String tomcatIP=Activator.getDefault().getPreferenceStore().getString("tomcatIP");
		String mySQLIP=Activator.getDefault().getPreferenceStore().getString("mySQLIP");
		
		try {
			deRegObject.setConfigProjectValues(projectName, ownerName, tomcatIP,mySQLIP);
			boolean status= deRegObject.executeClient();
			if (status)
			{
			// give message written to config file
				// dispose this window
			JOptionPane.showMessageDialog(null, "Project Successfully DeRegistered", "Message Info", JOptionPane.INFORMATION_MESSAGE);
				
			}
			else 
				JOptionPane.showMessageDialog(null, "Unable to deregister the project", "Message Info", JOptionPane.INFORMATION_MESSAGE);
		}
		catch (Exception ex)
		{
			if (DEBUG) System.out.println("Error calling DeRegClient");
			JOptionPane.showMessageDialog(null, "Unable to deregister the project", "Message Info", JOptionPane.INFORMATION_MESSAGE);
			ex.printStackTrace();
		}
		finally
		{
			//close client here
			try{
				if (deRegObject != null) deRegObject.closeClient();
			}
			catch (Exception ee){
				ee.printStackTrace();
			}
			
		}//finally
		
		
		
	//	EntryFormDeRegProject eForm= new EntryFormDeRegProject();
	//    eForm.executeForm();
		
		
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
