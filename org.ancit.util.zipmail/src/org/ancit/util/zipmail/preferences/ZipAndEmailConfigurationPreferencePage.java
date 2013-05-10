package org.ancit.util.zipmail.preferences;

import org.ancit.util.zipmail.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;

public class ZipAndEmailConfigurationPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
	public ZipAndEmailConfigurationPreferencePage() {
		setTitle("Zip and Email Config");
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		
	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		
		StringFieldEditor emailEditor = new StringFieldEditor("EMAILID", "Email ID", -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent());
		addField(emailEditor);
		{
			StringFieldEditor stringFieldEditor = new StringFieldEditor("PASSWORD", "Password", -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent());
			addField(stringFieldEditor);
		}
	}

	

}
