package collabhubclient.commands;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import collabhubclient.StopCollaborationClient;

public class StopCollaborationHandler implements IHandler {

	static Boolean success = false;
	boolean DEBUG = false;
	private BrokerProvider provider = new BrokerProvider();

	public void addHandlerListener(IHandlerListener handlerListener) {

		// do initialization here
		// executed only once
		if (DEBUG)
			System.out.println("In StopCollaborationHandler");
	}

	public void dispose() {

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		// this gets executed every time the button is pressed

		if (DEBUG)
			System.out.println("In StopCollaborationHandler");

		try {
			provider.getBroker().post(CollabEventsConstants.COLLAB_TOPIC_CLOSE,
					1);
			StopCollaborationClient collab = new StopCollaborationClient();

			Boolean status = collab.executeClient();

			if (status) {

				JOptionPane.showMessageDialog(null,
						"Disconnected from CollabHub Successfully",
						"Message Info", JOptionPane.INFORMATION_MESSAGE);

			} else
				JOptionPane.showMessageDialog(null,
						"Unable to disconnect from CollabHub", "Message Info",
						JOptionPane.INFORMATION_MESSAGE);

			collab.closeClient();

			// stop all UI JOBS here

		} catch (Exception e) {
			if (DEBUG)
				System.out.println("Error disconnecting");
			JOptionPane.showMessageDialog(null,
					"Unable to disconnect from CollabHub", "Message Info",
					JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}

		return null;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isHandled() {
		return true;
	}

	public void removeHandlerListener(IHandlerListener handlerListener) {
	}

}
