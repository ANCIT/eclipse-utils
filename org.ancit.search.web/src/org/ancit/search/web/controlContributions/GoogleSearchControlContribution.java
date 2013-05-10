package org.ancit.search.web.controlContributions;

import org.ancit.search.web.Activator;
import org.ancit.search.web.GoogleSearchView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

public class GoogleSearchControlContribution extends
		WorkbenchWindowControlContribution {

	private Text searchText;

	public GoogleSearchControlContribution() {
		// TODO Auto-generated constructor stub
	}

	public GoogleSearchControlContribution(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createControl(Composite parent) {
		 Composite container = new Composite(parent, SWT.NONE);
		    GridLayout glContainer = new GridLayout(2, false);
		    glContainer.marginTop = -1;
		    glContainer.marginHeight = 0;
		    glContainer.marginWidth = 0;
		    container.setLayout(glContainer);
		    GridData glReader = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		    glReader.widthHint = 280;
		    searchText = new Text(container, SWT.BORDER);
		    searchText.setLayoutData(glReader);
		    
		    searchText.addTraverseListener(new TraverseListener() {
				
				public void keyTraversed(TraverseEvent e) {
					if (e.detail == SWT.TRAVERSE_RETURN) {
						GoogleSearchView view;
						try {
							view = (GoogleSearchView) PlatformUI
									.getWorkbench().getActiveWorkbenchWindow().getActivePage()
									.showView(GoogleSearchView.ID);
							view.updateBrowser(searchText.getText()); 
						} catch (PartInitException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
				}
			});
		    
		    Button button = new Button(container, SWT.PUSH);
			button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
					false));
			button.setImage(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/google.png").createImage());
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					try {
						GoogleSearchView view = (GoogleSearchView) PlatformUI
								.getWorkbench().getActiveWorkbenchWindow().getActivePage()
								.showView(GoogleSearchView.ID);
						view.updateBrowser(searchText.getText());
					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		    
		    
		    
		    return container;
	}

}
