package org.ancit.util.zipmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.wizards.datatransfer.ArchiveFileExportOperation;
import org.eclipse.ui.internal.wizards.datatransfer.ZipFileExporter;
import org.eclipse.ui.wizards.datatransfer.ZipFileExportWizard;

public class ZipAndMailProjectAction implements IObjectActionDelegate,
		IViewActionDelegate {

	private IProject project;
	private File outFolder;

	public ZipAndMailProjectAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		zipProject();
		emailProject();

	}

	private void emailProject() {

		String from = Activator.getDefault().getPreferenceStore()
				.getString("EMAILID");

		EmailInformationDialog emailInformationDialog = new EmailInformationDialog(
				Display.getDefault().getActiveShell());
		if (IDialogConstants.OK_ID == emailInformationDialog.open()) {
			String to = emailInformationDialog.getEmailTo();
			String subject = emailInformationDialog.getSubject();
			String message = emailInformationDialog.getBodyText();
			outFolder = new File("c:/out/" + project.getName() + ".zip");
			SendMail sendMail = new SendMail(from, to, subject, message,
					outFolder.getAbsolutePath());
			sendMail.send();

		}

	}

	private void zipProject() {

		try {

			ArchiveFileExportOperation exportOperation = new ArchiveFileExportOperation(
					project, "c:/out/" + project.getName()+".zip");
			exportOperation.run(new NullProgressMonitor());
		}
		
		catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		
		String from = Activator.getDefault().getPreferenceStore()
				.getString("EMAILID");
		if(from.trim().length() > 0) {
			action.setEnabled(true);
		} else {
			action.setEnabled(false);
			return;
		}
		
		
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sSelection = (IStructuredSelection) selection;
			if (sSelection.getFirstElement() instanceof IProject) {
				project = (IProject) sSelection.getFirstElement();
			}
		}

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IViewPart view) {
		// TODO Auto-generated method stub

	}

}
