package org.ancit.search.sysout.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

public class SearchForSysout implements IWorkbenchWindowPulldownDelegate {
	
	List<ReplaceEdit> replaces = new ArrayList<ReplaceEdit>();
	HashMap changes = new HashMap();
	HashMap<String, String> patternMap = new HashMap<String, String>();
	private String inUse;
	IResource[] rootResources;

	public void run(IAction action) {
		
		  changes.clear();	 
		  patternMap.clear();
		  
		  patternMap.put("//System.out.println","System.out.println");
		  patternMap.put("System.out.println", "//System.out.println");
		  searchAndReplace(1);
			  
	}

	private void searchAndReplace(int type) {
		String[] fileNamePatterns = { "*" }; //$NON-NLS-1$ // all files with file suffix 'special'
		  IResource[] roots = rootResources == null ? new IResource[]{ResourcesPlugin
				  .getWorkspace().getRoot()} : rootResources;

		  FileTextSearchScope scope = FileTextSearchScope.newSearchScope(roots,
		    fileNamePatterns, true);
		  
		  TextSearchRequestor collector = new TextSearchRequestor() {
			   public boolean acceptPatternMatch(TextSearchMatchAccess matchAccess)
			     throws CoreException {
			    final IFile file = matchAccess.getFile();
			    
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
				  Pattern regEx = null;
				  if(type == 1) {
					regEx = Pattern.compile(pattern);  
				  } 
				  else {
				    regEx = TextSearchEngine.createPattern(pattern, true, true);
				  }
				  TextSearchEngine.createDefault().search(scope, collector, regEx,
						     new NullProgressMonitor());

				  CompositeChange result = new CompositeChange("Update SYSOUT");
				  for (Object changeObject : changes.values()) {
					result.add((Change)changeObject);
				}
				  
				  try {
					result.perform(new NullProgressMonitor());
					final ICommandService cmdService = (ICommandService)PlatformUI.getWorkbench().getService    (ICommandService.class);
				    if (cmdService != null) {
				        final Command cmd = cmdService.getCommand(IJavaEditorActionDefinitionIds.ORGANIZE_IMPORTS);
				        final ExecutionEvent execEvt = new ExecutionEvent(cmd, Collections.EMPTY_MAP, ((IStructuredSelection)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection(IPageLayout.ID_PROJECT_EXPLORER)).getFirstElement(), null);
				        Display.getDefault().syncExec(new Runnable() {
				            public void run() {
				                try {
				                    cmd.executeWithChecks(execEvt);
//				                    cmd.execute(execEvt);
				                } catch (Exception e) {
				                    e.printStackTrace();
				                }
				            }
				        });
				    }
				} catch (CoreException e) {
					e.printStackTrace();
				}
				  
				  

				  changes.clear();
			}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if(selection.isEmpty()) {
			action.setEnabled(false);
		} else {
			action.setEnabled(true);
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

	public Menu getMenu(Control parent) {
		Menu menu = new Menu(parent);

		MenuItem searchAndCommentMenu = new MenuItem(menu, SWT.PUSH);
		searchAndCommentMenu.setText("Search and Comment");
		searchAndCommentMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changes.clear();
				patternMap.clear();
				patternMap.put("//System.out.println","System.out.println");
				  patternMap.put("System.out.println", "//System.out.println");
				  searchAndReplace(1);
			}
		});

		MenuItem searchAndDeleteMenu = new MenuItem(menu, SWT.PUSH);
		searchAndDeleteMenu.setText("Search and Delete");
		searchAndDeleteMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changes.clear();
				patternMap.clear();
				patternMap.put("//System.out.println","System.out.println");
				  patternMap.put("System.out.println.*", "");
				  searchAndReplace(2);
			}
		});
		
		MenuItem searchPrintStackTraceAndReplaceMenu = new MenuItem(menu, SWT.PUSH);
		searchPrintStackTraceAndReplaceMenu.setText("Search and Replace PrintStackTrace");
		searchPrintStackTraceAndReplaceMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changes.clear();
				patternMap.clear();
				  patternMap.put("e.printStackTrace(.*.);", "StatusManager.getManager().handle(new Status(IStatus.ERROR, \"\", e.getMessage(), e));");
				  searchAndReplace(2);
				  
				  
			
				  
			}
		});
		
		return menu;
	}

}
