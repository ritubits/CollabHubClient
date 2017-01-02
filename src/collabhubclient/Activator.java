package collabhubclient;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import collabhubclient.commands.JobSchedulerClass;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	boolean DEBUG= false;
	public static final String PLUGIN_ID = "CollabHubClient"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		
//		PlatformUI.getWorkbench().getDecoratorManager().setEnabled("DecorationProject.myDecorator", false);
		super.start(context);
		plugin = this;
		Job job = new Job("Starting Publisher/Subscriber") {

			   @Override
			   protected IStatus run(IProgressMonitor monitor) {
			    if (PlatformUI.isWorkbenchRunning()) {

			     Object service = PlatformUI.getWorkbench().getService(IEventBroker.class);
			     if (service instanceof IEventBroker) {
			      // broker available, create publisher
			     System.out.println("IEventBroker found");
			     new JobSchedulerClass();
			     }
			     
			    } else
			     // workbench not running yet, try again in 1 second
			     schedule(1000);

			    return Status.OK_STATUS;
			   }
			  };

			  job.schedule();
			 
			  String projectName="null";//"Project Name";
			  String collabName="null";//"Collaborator Name";
			  String ownerName="null";//"Owner Name";
			  String tomcatIP="localhost:8080";
			  String mySQLIP="localhost:3306";
			  getPreferenceStore().setValue("projectName",projectName);
			  getPreferenceStore().setValue("collabName",collabName);
			  getPreferenceStore().setValue("ownerName",ownerName);
			  getPreferenceStore().setValue("tomcatIP",tomcatIP);
			  getPreferenceStore().setValue("mySQLIP",mySQLIP);
			  
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	


}
