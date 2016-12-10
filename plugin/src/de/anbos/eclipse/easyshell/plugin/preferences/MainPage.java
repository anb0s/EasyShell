/*******************************************************************************
 * Copyright (c) 2014 - 2016 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.misc.UtilsUI;
import de.anbos.eclipse.easyshell.plugin.types.Debug;


public class MainPage extends PreferencePage implements IWorkbenchPreferencePage {

    private IWorkbench workbench;
    
    private Combo debugCombo;

    public MainPage() {
    }

    public MainPage(String title) {
        super(title);
    }

    public MainPage(String title, ImageDescriptor image) {
        super(title, image);
    }

    @Override
    public void init(IWorkbench workbench) {
        this.workbench = workbench;
    }

    @Override
    public boolean performOk() {
    	if (!validateValues()) {
    		return false;
    	}
    	boolean save = true;
        if (!GeneralDataStore.instance().isMigrated()) {
            String title = Activator.getResourceString("easyshell.main.page.dialog.migration.title");
            String question = Activator.getResourceString("easyshell.main.page.dialog.migration.question");
            MessageDialog dialog = new MessageDialog(
                    null, title, null, question,
                    MessageDialog.WARNING,
                    new String[] {"Yes", "No"},
                    1); // no is the default
            int result = dialog.open();
            if (result == 0) {
            	GeneralDataStore.instance().setMigrated(true);
            } else {
                save = false;
            }
        }
        if (save) {
        	GeneralDataStore.instance().getData().setDebug(Debug.getFromName(debugCombo.getText()));
        	GeneralDataStore.instance().save();
        }
        return save;
    }

    @Override
    protected void performApply() {
        performOk();
    }

    private boolean validateValues() {
    	final String title = Activator.getResourceString("easyshell.main.page.error.title.incompletedata");
        // check resource
        if ( (debugCombo.getText() == null) || (debugCombo.getText().length() <= 0)) {
            MessageDialog.openError(getShell(), title, Activator.getResourceString("easyshell.main.page.error.text.debug"));
            return false;
        }
        return true;
    }

    @Override
    protected void performDefaults() {
        String title = Activator.getResourceString("easyshell.main.page.dialog.defaults.title");
        String question = Activator.getResourceString("easyshell.main.page.dialog.defaults.question");
        MessageDialog dialog = new MessageDialog(
                null, title, null, question,
                MessageDialog.WARNING,
                new String[] {"Yes", "No"},
                1); // no is the default
        int result = dialog.open();
        if (result == 0) {
            GeneralDataStore.instance().loadDefaults();
            UtilsUI.refreshWidget(debugCombo);
        }
    }

    @Override
    protected Control createContents(Composite parent) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("preferencePageId","de.anbos.eclipse.easyshell.plugin.preferences.MenuPage");
        Utils.executeCommand(workbench, "org.eclipse.ui.window.preferences", params, true);
        
        Composite pageComponent = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3, false);
        pageComponent.setLayout(layout);

        createGroup(pageComponent);
        
        return pageComponent;
    }

    private void createGroup(Composite pageComponent) {
        // define group1
    	Group pageGroup1 = new Group(pageComponent, SWT.SHADOW_ETCHED_IN);
    	pageGroup1.setText(Activator.getResourceString("easyshell.main.page.title.group1"));
    	pageGroup1.setToolTipText(Activator.getResourceString("easyshell.main.page.tooltip.group1"));
        GridLayout layout1 = new GridLayout();
        layout1.numColumns = 3;
        layout1.makeColumnsEqualWidth = false;
        layout1.marginWidth = 5;
        layout1.marginHeight = 4;
        pageGroup1.setLayout(layout1);
        GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
        pageGroup1.setLayoutData(data1);
        pageGroup1.setFont(pageComponent.getFont());

        createDebugCombo(pageGroup1);
        
        UtilsUI.refreshWidget(debugCombo);
    }

    private void createDebugCombo(Composite parent) {
        // draw label
    	UtilsUI.createLabel(parent, Activator.getResourceString("easyshell.main.page.label.combo.debug"), Activator.getResourceString("easyshell.main.page.label.combo.tooltip.debug"));
    	UtilsUI.createLabel(parent, "", null);
        // draw combo
        debugCombo = new Combo(parent,SWT.BORDER | SWT.READ_ONLY);
        debugCombo.setToolTipText(Activator.getResourceString("easyshell.main.page.label.combo.tooltip.debug"));
        debugCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        debugCombo.setItems(Debug.getNamesAsArray());
        debugCombo.select(0);
        debugCombo.addSelectionListener(new SelectionListener() {
            @Override
			public void widgetSelected(SelectionEvent e) {
				//String text = resourceTypeCombo.getItem(resourceTypeCombo.getSelectionIndex());
			}
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
            }
		});
        GeneralData data = GeneralDataStore.instance().getData();
        String[] items = debugCombo.getItems();
        for(int i = 0 ; i < items.length ; i++) {
            if(items[i].equals(data.getDebug().getName())) {
            	debugCombo.select(i);
                break;
            }
        }
        debugCombo.setEnabled(true);
    }

}
