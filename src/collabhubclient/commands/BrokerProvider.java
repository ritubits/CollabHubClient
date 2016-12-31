package collabhubclient.commands;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.ui.PlatformUI;

public class BrokerProvider {
	private IEventBroker broker;

	public IEventBroker getBroker() {
		if (broker == null) {
			Object service = PlatformUI.getWorkbench().getService(
					IEventBroker.class);
			if (service instanceof IEventBroker) {
				broker = (IEventBroker) service;
			}
		}
		return broker;
	}
}
