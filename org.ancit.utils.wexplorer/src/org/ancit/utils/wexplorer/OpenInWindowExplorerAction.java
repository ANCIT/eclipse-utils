package org.ancit.utils.wexplorer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class OpenInWindowExplorerAction implements IObjectActionDelegate {


	private IContainer folder;

	public OpenInWindowExplorerAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		try {
			
			File file = new File(folder.getLocation().toOSString());
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();						
				desktop.open(file);						
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		Object selectedElement = ((IStructuredSelection)selection).getFirstElement();
		if (selectedElement instanceof IContainer) {
			folder = (IContainer) selectedElement;
			
		}
		
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

}
