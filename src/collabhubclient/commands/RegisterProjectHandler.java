package collabhubclient.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

import collabhubclient.EntryFormRegProject;

public class RegisterProjectHandler implements IHandler {

	boolean DEBUG= false;
	static Boolean success= false;
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
		EntryFormRegProject eForm= new EntryFormRegProject();
		success= eForm.executeForm();
		
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
