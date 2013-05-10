package org.ancit.search.web;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class GoogleSearchView extends ViewPart {

	public static final String ID = "org.ancit.spy.editors.GoogleSearchView"; //$NON-NLS-1$
	static String url = "https://www.google.co.in/#output=search&q=";
	private Browser browser;
	private Action action;

	public GoogleSearchView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		browser = new Browser(container, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		browser.setUrl("www.google.com");
		
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			action = new Action("New Action") {
				@Override
				public void run() {
					browser.back();
				}
			};
			action.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_BACK));
		}
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
		toolbarManager.add(action);
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	public void updateBrowser(String searchText) {
		this.setPartName(searchText + " - Google Search - ");
		String searchURL = url + searchText;
		browser.setUrl(searchURL);
	}

}
