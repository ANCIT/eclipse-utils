package org.ancit.search.web;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class SearchOnGoogleAction implements IObjectActionDelegate,
		IViewActionDelegate {

	private String searchText;

	private static final String TITLE = "Do Stuff";
	private static final int TEXT_REPLACE_ALL = 0;
	private static final int TEXT_REPLACE_SELECTED = 1;
	private static final int TEXT_INSERT = 2;
	private int textOutputMode = 0;
	private String textOutput = null;
	private Shell shell;

	public SearchOnGoogleAction() {
		// TODO Auto-generated constructor stub
	}

	public void run(IAction action) {
		try {
			
			IViewPart part = PlatformUI
			.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.findView(GoogleSearchView.ID);
			GoogleSearchView view = null;
			if (part == null) {
				view = (GoogleSearchView) PlatformUI
						.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().showView(GoogleSearchView.ID);
				
			} else {
				boolean result = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "New Tab Confirmation", "Search View is already open. Do you want to open a new Tab ?");
				if(result) {
					 view = (GoogleSearchView) PlatformUI
							.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().showView(GoogleSearchView.ID,"viewid"+Math.random(), IWorkbenchPage.VIEW_ACTIVATE);
					
				} else {
					 view = (GoogleSearchView) PlatformUI
								.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage().showView(GoogleSearchView.ID);
					 
					
				}
			}
		
			view.updateBrowser(searchText);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void selectionChanged(IAction action, ISelection selection) {

		if (selection instanceof IStructuredSelection) {

			if (!selection.isEmpty()
					&& ((IStructuredSelection) selection).getFirstElement() instanceof IEditorInput) {
				// get active editor
				IWorkbenchPage activePage = Activator.getDefault()
						.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage();

				if (activePage == null) {
					return;
				}

				IEditorPart editorPart = activePage.getActiveEditor();

				if (editorPart instanceof AbstractTextEditor) {
					// check if there is text selection
					IEditorSite iEditorSite = editorPart.getEditorSite();
					if (iEditorSite != null) {
						ISelectionProvider selectionProvider = iEditorSite
								.getSelectionProvider();
						if (selectionProvider != null) {
							ISelection iSelection = selectionProvider
									.getSelection();
							if (!iSelection.isEmpty()) {
								searchText = ((ITextSelection) iSelection)
										.getText();
								if (searchText.trim().length() > 0) {
									action.setEnabled(true);
									action.setText("Search on Google for "
											+ searchText);
									// System.out.println(searchText);
								} else {
									action.setEnabled(false);
								}
							}
						}
					}
				}
			} else {
				action.setEnabled(false);
			}
		} else if (selection instanceof ITextSelection) {
			if (!selection.isEmpty()) {
				searchText = ((ITextSelection) selection).getText();
				if (searchText.trim().length() > 0) {
					action.setEnabled(true);
					action.setText("Search on Google for " + searchText);
					// System.out.println(searchText);
				} else {
					action.setEnabled(false);
				}
			}
		}
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	public void init(IViewPart view) {
		// TODO Auto-generated method stub

	}

}
