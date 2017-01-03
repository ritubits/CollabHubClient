package collabhubclient.commands;

import java.util.Map;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import collabhubclient.CollabUserActivityClient;

public class JobSchedulerClass implements EventHandler {

	private ScheduledUITask task;

	public JobSchedulerClass() {
		IEventBroker broker = new BrokerProvider().getBroker();
		broker.subscribe(CollabEventsConstants.COLLAB_TOPIC_START, this);
		broker.subscribe(CollabEventsConstants.COLLAB_TOPIC_CLOSE, this);
	}

	@Override
	public void handleEvent(Event arg0) {
		// TODO Auto-generated method stub
		// text1 is a SWT Text field
		// text1.setText(String.valueOf(i));
		System.out.println("In JobSchedukerClass" + arg0);
		String topic = arg0.getTopic();
		if (CollabEventsConstants.COLLAB_TOPIC_START.equals(topic)) {
			// handle start
			if (task == null) {
				CollabUserActivityClient userClient = (CollabUserActivityClient) arg0
						.getProperty("client");
				IWorkbench workbench = (IWorkbench) arg0
						.getProperty("workbench");
				 IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow().getActivePage();
				task = new ScheduledUITask(userClient, activePage);
			}

			task.enableRunning();
			task.schedule();
		} else if (CollabEventsConstants.COLLAB_TOPIC_CLOSE.equals(topic)) {
			// handle close.
			task.disableRunning();
		}
	}

}
