/**
 * Copyright (c) 2014-2020 Andre Bossert <anb0s@anbos.de>.
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.misc.UtilsUI;
import de.anbos.eclipse.easyshell.plugin.types.Debug;
import de.anbos.eclipse.easyshell.plugin.types.Tooltip;


public class MainPage extends PreferencePage implements IWorkbenchPreferencePage {

    private IWorkbench workbench;

    private Button 	debug;
    private Button 	toolTipAll;
    private Button 	toolTipClipboard;
    private Button 	toolTipError;

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
            GeneralDataStore.instance().getData().setDebug(debug.getSelection() ? Debug.debugYes : Debug.debugNo);
            GeneralDataStore.instance().getData().setToolTipAll(toolTipAll.getSelection() ? Tooltip.tooltipYes : Tooltip.tooltipNo);
            GeneralDataStore.instance().getData().setToolTipClipboard(toolTipClipboard.getSelection() ? Tooltip.tooltipYes : Tooltip.tooltipNo);
            GeneralDataStore.instance().getData().setToolTipError(toolTipError.getSelection() ? Tooltip.tooltipYes : Tooltip.tooltipNo);
            GeneralDataStore.instance().save();
        }
        return save;
    }

    @Override
    protected void performApply() {
        performOk();
    }

    private boolean validateValues() {
        //final String title = Activator.getResourceString("easyshell.error.title.incompletedata");
        // check resource
        /*if ( (debugCombo.getText() == null) || (debugCombo.getText().length() <= 0)) {
            MessageDialog.openError(getShell(), title, Activator.getResourceString("easyshell.main.page.error.text.debug"));
            return false;
        }*/
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
            refreshWidgets();
        }
    }

    @Override
    protected Control createContents(Composite parent) {
        // forward to menu page
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("preferencePageId","de.anbos.eclipse.easyshell.plugin.preferences.MenuPage");
        Utils.executeCommand(workbench, "org.eclipse.ui.window.preferences", params, true);

        // create own
        Composite pageComponent = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3, false);
        pageComponent.setLayout(layout);
        createGroup(pageComponent);
        return pageComponent;
    }

    private void createGroup(Composite pageComponent) {
        Group pageGroup1 = createGroup1(pageComponent);
        createWidgets(pageGroup1);
        refreshWidgets();
    }

    private Group createGroup1(Composite pageComponent) {
        Group pageGroup = new Group(pageComponent, SWT.SHADOW_ETCHED_IN);
        pageGroup.setText(Activator.getResourceString("easyshell.main.page.title.group1"));
        pageGroup.setToolTipText(Activator.getResourceString("easyshell.main.page.tooltip.group1"));
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;
        layout.marginWidth = 5;
        layout.marginHeight = 4;
        pageGroup.setLayout(layout);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        pageGroup.setLayoutData(data);
        pageGroup.setFont(pageComponent.getFont());
        return pageGroup;
    }

    private Composite createGroup1_1(Composite pageComponent) {
        // define group1
        Composite pageGroup = new Composite(pageComponent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;
        layout.marginWidth = 5;
        layout.marginHeight = 4;
        pageGroup.setLayout(layout);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        pageGroup.setLayoutData(data);
        pageGroup.setFont(pageComponent.getFont());
        return pageGroup;
    }

    private void createWidgets(Composite parent) {
        createCheckBoxDebug(parent);
        createCheckBoxToolTipAll(parent);
        Composite group1_1 = createGroup1_1(parent);
        createCheckBoxToolTipClipboard(group1_1);
        createCheckBoxToolTipError(group1_1);
    }

    private void refreshWidgets() {
        UtilsUI.refreshWidget(debug);
        UtilsUI.refreshWidget(toolTipAll);
        UtilsUI.refreshWidget(toolTipClipboard);
        UtilsUI.refreshWidget(toolTipError);
    }

    private void createCheckBoxDebug(Composite parent) {
        // draw label
        //UtilsUI.createLabel(parent, "", null);
        // draw checkbox
        GeneralData data = GeneralDataStore.instance().getData();
        debug = new Button(parent,SWT.CHECK);
        debug.setSelection(data.getDebug() == Debug.debugYes);
        debug.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        debug.setText(Activator.getResourceString("easyshell.main.page.label.button.debug"));
        debug.setToolTipText(Activator.getResourceString("easyshell.main.page.label.button.tooltip.debug"));
        debug.setEnabled(true);
    }

    private void createCheckBoxToolTipAll(Composite parent) {
        // draw label
        //UtilsUI.createLabel(parent, "", null);
        // draw checkbox
        GeneralData data = GeneralDataStore.instance().getData();
        toolTipAll = new Button(parent,SWT.CHECK);
        toolTipAll.setSelection(data.getToolTipAll() == Tooltip.tooltipYes);
        toolTipAll.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean enabled = toolTipAll.getSelection();
                toolTipClipboard.setEnabled(enabled);
                toolTipError.setEnabled(enabled);
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
            }
        });
        toolTipAll.setText(Activator.getResourceString("easyshell.main.page.label.button.tooltipall"));
        toolTipAll.setToolTipText(Activator.getResourceString("easyshell.main.page.label.button.tooltip.tooltipall"));
        toolTipAll.setEnabled(true);
    }

    private void createCheckBoxToolTipClipboard(Composite parent) {
        // draw label
        UtilsUI.createLabel(parent, "", null);
        // draw checkbox
        GeneralData data = GeneralDataStore.instance().getData();
        toolTipClipboard = new Button(parent,SWT.CHECK);
        toolTipClipboard.setSelection(data.getToolTipClipboard() == Tooltip.tooltipYes);
        toolTipClipboard.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        toolTipClipboard.setText(Activator.getResourceString("easyshell.main.page.label.button.tooltipclip"));
        toolTipClipboard.setToolTipText(Activator.getResourceString("easyshell.main.page.label.button.tooltip.tooltipclip"));
        toolTipClipboard.setEnabled(true);
    }

    private void createCheckBoxToolTipError(Composite parent) {
        // draw label
        UtilsUI.createLabel(parent, "", null);
        // draw checkbox
        GeneralData data = GeneralDataStore.instance().getData();
        toolTipError = new Button(parent,SWT.CHECK);
        toolTipError.setSelection(data.getToolTipError() == Tooltip.tooltipYes);
        toolTipError.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        toolTipError.setText(Activator.getResourceString("easyshell.main.page.label.button.tooltiperror"));
        toolTipError.setToolTipText(Activator.getResourceString("easyshell.main.page.label.button.tooltip.tooltiperror"));
        toolTipError.setEnabled(true);
    }

}
