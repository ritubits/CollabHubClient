package collabhubclient.commands;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import collabhubclient.CollabUserActivityClient;

public class JobSchedulerClass implements EventHandler {

	private ScheduledUITask task;
	private ScheduledSimulationUIJob simTask;

	public JobSchedulerClass() {
		IEventBroker broker = new BrokerProvider().getBroker();
		broker.subscribe(CollabEventsConstants.COLLAB_TOPIC_START, this);
		broker.subscribe(CollabEventsConstants.COLLAB_TOPIC_CLOSE, this);
	}

	@Override
	public void handleEvent(Event arg0) {

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
				IWorkbenchPage activePage = workbench
						.getActiveWorkbenchWindow().getActivePage();
				task = new ScheduledUITask(userClient, activePage);
			}

			task.enableRunning();
			task.schedule();

			String simulationMode = (String) arg0.getProperty("simulationMode");
			String simulationPath = (String) arg0.getProperty("simulationPath");

			if (simulationMode.equalsIgnoreCase("Y")) {
				if (simTask == null) {

					CollabUserActivityClient userClient = (CollabUserActivityClient) arg0
							.getProperty("client");
					IWorkbench workbench = (IWorkbench) arg0
							.getProperty("workbench");
					IWorkbenchPage activePage = workbench
							.getActiveWorkbenchWindow().getActivePage();
					simTask = new ScheduledSimulationUIJob(userClient,
							activePage, simulationPath);
					System.out.println("scheduling simulation");
				}

				simTask.enableRunning();
				simTask.schedule();
			}

		} else if (CollabEventsConstants.COLLAB_TOPIC_CLOSE.equals(topic)) {
			// handle close.
			if (task != null)
				task.disableRunning();
			if (simTask != null)
				simTask.disableRunning();
		}
	}

}
