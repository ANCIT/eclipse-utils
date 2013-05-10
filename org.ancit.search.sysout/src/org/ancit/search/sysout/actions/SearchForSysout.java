package org.ancit.search.sysout.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.search.core.text.TextSearchEngine;
import org.eclipse.search.core.text.TextSearchMatchAccess;
import org.eclipse.search.core.text.TextSearchRequestor;
import org.eclipse.search.ui.text.FileTextSearchScope;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class SearchForSysout implements IWorkbenchWindowActionDelegate {
	
	List<ReplaceEdit> replaces = new ArrayList<ReplaceEdit>();
	HashMap changes = new HashMap();
	HashMap<String, String> patternMap = new HashMap<String, String>();
	private String inUse;
	IResource[] rootResources;

	public void run(IAction action) {
		
		changes.clear();
		  
		  patternMap.put("//System.out.println","System.out.println");
		  patternMap.put("System.out.println", "//System.out.println");
		  
		  String[] fileNamePatterns = { "*" }; //$NON-NLS-1$ // all files with file suffix 'special'
		  IResource[] roots = rootResources == null ? new IResource[]{ResourcesPlugin
				  .getWorkspace().getRoot()} : rootResources;

		  FileTextSearchScope scope = FileTextSearchScope.newSearchScope(roots,
		    fileNamePatterns, true);
		  
		  TextSearchRequestor collector = new TextSearchRequestor() {
			   public boolean acceptPatternMatch(TextSearchMatchAccess matchAccess)
			     throws CoreException {
			    IFile file = matchAccess.getFile();
			    
			    TextChange change = (TextChange)changes.get(file);
			    if(change == null) {
			    	change = new TextFileChange(file.getName(), file);
			    	change.setEdit(new MultiTextEdit());
			    	changes.put(file, change);
			    }
			    
			    
			    ReplaceEdit edit = null;
			    String patternString = patternMap.get(inUse);
			    edit = new ReplaceEdit(matchAccess.getMatchOffset(),
			      matchAccess.getMatchLength(), patternString);
			   
			    change.addEdit(edit);
			    return true;
			   }
			  };
			
			  for (String pattern : patternMap.keySet()) {
				  inUse = pattern;
				  Pattern regEx = Pattern.compile(pattern);
				  TextSearchEngine.createDefault().search(scope, collector, regEx,
						     new NullProgressMonitor());

				  CompositeChange result = new CompositeChange("Update SYSOUT");
				  for (Object changeObject : changes.values()) {
					result.add((Change)changeObject);
				}
				  
				  try {
					result.perform(new NullProgressMonitor());
				} catch (CoreException e) {
					e.printStackTrace();
				}

				  changes.clear();
			}
			  				// TODO Auto-generated catch block
	  
			  
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if(selection.isEmpty()) {
			rootResources = new IResource[]{ResourcesPlugin.getWorkspace().getRoot()};
		} else {
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection sSelection = (IStructuredSelection) selection;
				Object selectedObject = sSelection.getFirstElement();
				if (selectedObject instanceof IResource) {
					IResource resource = (IResource) selectedObject;
					rootResources = new IResource[]{resource};
					
				}
			}
		}

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

}
