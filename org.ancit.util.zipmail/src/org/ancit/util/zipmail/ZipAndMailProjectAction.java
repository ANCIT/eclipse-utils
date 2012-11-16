package org.ancit.util.zipmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;

public class ZipAndMailProjectAction implements IObjectActionDelegate, IViewActionDelegate {

	private IProject project;
	private String projectRelativePath;
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
		
		String from = Activator.getDefault().getPreferenceStore().getString("EMAILID");
		
		EmailInformationDialog emailInformationDialog = new EmailInformationDialog(Display.getDefault().getActiveShell());
		if(IDialogConstants.OK_ID == emailInformationDialog.open()) {
			String to =  emailInformationDialog.getEmailTo();
			String subject = emailInformationDialog.getSubject();
			String message = emailInformationDialog.getBodyText();
			
			SendMail sendMail = new SendMail(from, to, subject, message, outFolder.getAbsolutePath());
			sendMail.send();
			
		}
		
	}

	private void zipProject() {
		try {
			String projectPath = project.getLocation().toOSString();
			projectRelativePath = project.getFullPath().toOSString();
			
//			projectPath.substring(projectPath.indexOf(projectRelativePath)+1);

			File inFolder = new File(projectPath);
			outFolder = new File("c:/out/" +project.getName()+".zip");
			//create ZipOutputStream object
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFolder));
			
			
			//get path prefix so that the zip file does not contain the whole path
			// eg. if folder to be zipped is /home/lalit/test
			// the zip file when opened will have test folder and not home/lalit/test folder
			int len = outFolder.getAbsolutePath().lastIndexOf(File.separator);
			String baseName = outFolder.getAbsolutePath().substring(0,len+1);
			
			addFolderToZip(inFolder, out, baseName);
			
			out.flush();
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addFolderToZip(File folder, ZipOutputStream zip, String baseName) throws IOException {
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				addFolderToZip(file, zip, baseName);
			} else {
				String name = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(projectRelativePath)+1);
				ZipEntry zipEntry = new ZipEntry(name);
				zip.putNextEntry(zipEntry);
				IOUtils.copy(new FileInputStream(file), zip);
				zip.closeEntry();
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
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
