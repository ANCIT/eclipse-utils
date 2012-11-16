package org.ancit.utils.wexplorer.launch.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;

public class OpenInExplorerCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
			ILaunchConfigurationType launchConfigurationType = manager.getLaunchConfigurationType("org.eclipse.ui.externaltools.ProgramLaunchConfigurationType");
			ILaunchConfiguration[] configurations =
				      manager.getLaunchConfigurations(launchConfigurationType);
				   for (int i = 0; i < configurations.length; i++) {
				      ILaunchConfiguration configuration = configurations[i];
				      if (configuration.getName().equals("shell")) {
				         configuration.delete();
				         break;
				      }
				   }
				ILaunchConfigurationWorkingCopy workingCopy =
						launchConfigurationType.newInstance(null, "shell");
				workingCopy.setAttribute("org.eclipse.ui.externaltools.ATTR_LOCATION", "${env_var:SystemRoot}\\explorer.exe");
				workingCopy.setAttribute("org.eclipse.ui.externaltools.ATTR_TOOL_ARGUMENTS", "${container_loc}");
				ILaunchConfiguration configuration = workingCopy.doSave();
				DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
