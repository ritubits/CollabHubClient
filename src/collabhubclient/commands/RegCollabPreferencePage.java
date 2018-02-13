package collabhubclient.commands;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import collabhubclient.Activator;

public class RegCollabPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	protected void createFieldEditors() {
		addField(new StringFieldEditor("projectName", "Enter Project Name:",
				getFieldEditorParent()));
		addField(new StringFieldEditor("ownerName", "Enter Owner Name:",
				getFieldEditorParent()));
		addField(new StringFieldEditor("tomcatIP", "Enter Tomcat IP address:",
				getFieldEditorParent()));
		// addField(new ListEditor("levelNumber", "Specify Level Number:",
		// getFieldEditorParent()));
		addField(new StringFieldEditor("mySQLIP", "Enter MySQL IP address:",
				getFieldEditorParent()));
		addField(new StringFieldEditor("levelNumber", "Specify Level Number:",
				getFieldEditorParent()));
		addField(new IntegerFieldEditor("collabNumber",
				"Specify Number of Collaborators Allowed:",
				getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	public RegCollabPreferencePage() {
		super(GRID);
	}

	public boolean performOk() {
		super.performOk();
		// OK pressed

		System.out.println(getPreferenceStore().getString("projectName"));
		System.out.println(getPreferenceStore().getString("ownerName"));
		System.out.println(getPreferenceStore().getString("tomcatIP"));
		System.out.println(getPreferenceStore().getString("mySQLIP"));
		return true;
	}

}
