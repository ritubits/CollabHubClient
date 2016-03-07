package collabhubclient.commands;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

import collabhubclient.StopCollaborationClient;

public class StopCollaborationHandler implements IHandler {

	static Boolean success= false;
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
		
		//do initialization here
		// executed only once
		System.out.println("In StopCollaborationHandler");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		//this gets executed every time the button is pressed

		System.out.println("In StopCollaborationHandler");
		
		try
		{
		StopCollaborationClient collab = new StopCollaborationClient();
		Boolean status= collab.executeClient();
		
		if (status)
		{
		
			JOptionPane.showMessageDialog(null, "Disconnected from CollabHub Successfully", "Message Info", JOptionPane.INFORMATION_MESSAGE);
			
		}
		else 
			JOptionPane.showMessageDialog(null, "Unable to disconnect from CollabHub", "Message Info", JOptionPane.INFORMATION_MESSAGE);
		
		collab.closeClient();
		} catch(Exception e)
		{
			System.out.println("Error disconnecting");
			JOptionPane.showMessageDialog(null, "Unable to disconnect from CollabHub", "Message Info", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
		
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
