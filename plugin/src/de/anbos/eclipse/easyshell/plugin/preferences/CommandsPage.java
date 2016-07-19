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

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.anbos.eclipse.easyshell.plugin.Activator;

public class CommandsPage extends org.eclipse.jface.preference.PreferencePage
        implements IWorkbenchPreferencePage {

    private static final int TABLE_WIDTH = 400;

    private Table table;
    private MenuDataMover itemMover;
    private CommandDataStore cmdStore;
    List<CommandData> cmdList;
    private MenuDataStore menuStore;
    private CheckboxTableViewer tableViewer;
    private Button addNewButton;
    private Button addCopyButton;
    private Button editButton;
    private Button upButton;
    private Button downButton;
    private Button removeButton;

    @Override
    public void init(IWorkbench workbench) {
    }

    @Override
    public boolean performOk() {
        boolean save = true;
        if (!menuStore.isMigrated()) {
            String title = Activator.getResourceString("easyshell.page.table.dialog.migration.title");
            String question = Activator.getResourceString("easyshell.page.table.dialog.migration.question");
            MessageDialog dialog = new MessageDialog(
                    null, title, null, question,
                    MessageDialog.WARNING,
                    new String[] {"Yes", "No"},
                    1); // no is the default
            int result = dialog.open();
            if (result == 0) {
                menuStore.setMigrated(true);
            } else {
                save = false;
            }
        }
        if (save) {
            menuStore.save();
        }
        return save;
    }

    @Override
    protected void performDefaults() {
        String title = Activator.getResourceString("easyshell.page.table.dialog.defaults.title");
        String question = Activator.getResourceString("easyshell.page.table.dialog.defaults.question");
        MessageDialog dialog = new MessageDialog(
                null, title, null, question,
                MessageDialog.WARNING,
                new String[] {"Yes", "No"},
                1); // no is the default
        int result = dialog.open();
        if (result == 0) {
            menuStore.loadDefaults();
            tableViewer.refresh();
            for (MenuData item : menuStore.getCommandMenuDataList()) {
                tableViewer.setChecked(item, true);
            }
        }
    }

    @Override
    protected void performApply() {
        performOk();
    }

    @Override
    protected Control createContents(Composite parent) {
        Font font = parent.getFont();
        // define default grid
        Composite pageComponent = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        pageComponent.setLayout(layout);

        // list
        GridData gridData = new GridData(GridData.FILL_BOTH);
        // create table
        table = new Table(pageComponent, SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setFont(parent.getFont());

        TableColumn column1 = new TableColumn(table, SWT.LEFT);
        column1.setText(Activator.getResourceString("easyshell.page.table.header.title.column0")); //$NON-NLS-1$
        column1.setResizable(false);

        TableColumn column2 = new TableColumn(table, SWT.LEFT);
        column2.setText(Activator.getResourceString("easyshell.page.table.header.title.column1")); //$NON-NLS-1$
        column2.setResizable(false);

        int availableRows = availableRows(pageComponent);
        gridData.heightHint = table.getItemHeight() * (availableRows / 8);
        gridData.widthHint = TABLE_WIDTH;
        table.setLayoutData(gridData);

        tableViewer = new CheckboxTableViewer(table);
        tableViewer.setLabelProvider(new MenuDataLabelProvider());
        tableViewer.setContentProvider(new MenuDataContentProvider());

        // command store
        cmdStore = new CommandDataStore(Activator.getDefault().getPreferenceStore());
        cmdStore.load();

        // get the native commands list
        cmdList = CommandDataDefaultCollection.getCommandsNative(cmdStore.getAllCommands(), true);

        // menu store
        menuStore = new MenuDataStore(Activator.getDefault().getPreferenceStore());
        menuStore.load();
        tableViewer.setInput(menuStore);
        tableViewer.setAllChecked(false);
        tableViewer.setCheckedElements(menuStore.getEnabledCommandMenuDataArray());

        tableViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                editDialog();
            }
        });

        tableViewer.addCheckStateListener(new ICheckStateListener() {
            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                MenuData data = (MenuData) event.getElement();
                data.setEnabled(event.getChecked());
            }
        });

        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
                boolean selected = !selection.isEmpty();
                addCopyButton.setEnabled(selected);
                editButton.setEnabled(selected);
                removeButton.setEnabled(selected);
                upButton.setEnabled(selected);
                downButton.setEnabled(selected);
            }
        });

        tableViewer.setSorter(new ViewerSorter() {
            public int compare(Viewer viewer, Object object1, Object object2) {
                if (!(object1 instanceof MenuData) || !(object2 instanceof MenuData)) {
                    return super.compare(viewer, object1, object2);
                }
                MenuData data1 = (MenuData) object1;
                MenuData data2 = (MenuData) object2;
                if (data1.getPosition() > data2.getPosition()) {
                    return 1;
                }
                if (data1.getPosition() < data2.getPosition()) {
                    return -1;
                }
                if (data1.getPosition() == data2.getPosition()) {
                    return 0;
                }
                return super.compare(viewer, object1, object2);
            }

            public boolean isSorterProperty(Object element, String property) {
                return true;
            }
        });

        itemMover = new MenuDataMover(table, menuStore);

        // button pageComponent
        Composite groupComponent = new Composite(pageComponent, SWT.NULL);
        GridLayout groupLayout = new GridLayout();
        groupLayout.marginWidth = 0;
        groupLayout.marginHeight = 0;
        groupComponent.setLayout(groupLayout);
        gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalAlignment = GridData.FILL;
        groupComponent.setLayoutData(gridData);
        groupComponent.setFont(font);

        // buttons
        createNewButton(font, gridData, groupComponent);

        createCopyButton(font, gridData, groupComponent);

        createEditButton(font, gridData, groupComponent);

        createRemoveButton(font, gridData, groupComponent);

        createUpButton(font, gridData, groupComponent);

        createDownButton(font, gridData, groupComponent);

        configureTableResizing(table);

        Dialog.applyDialogFont(pageComponent);
        // trigger the resize
        table.getHorizontalBar().setVisible(true);

        // send event to refresh tableViewer
        Event event = new Event();
        event.item = null;
        tableViewer.refresh();
        tableViewer.getTable().notifyListeners(SWT.Selection, event);
        //tableViewer.getControl().setEnabled(true);

        return pageComponent;
    }

    private void createDownButton(Font font, GridData gridData, Composite groupComponent) {
        downButton = new Button(groupComponent, SWT.PUSH);
        downButton.setText(Activator.getResourceString("easyshell.page.table.button.text.down"));
        downButton.setToolTipText(Activator.getResourceString("easyshell.page.table.button.tooltip.down"));
        downButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                down();
            }
        });
        downButton.setLayoutData(gridData);
        downButton.setFont(font);
        setButtonLayoutData(downButton);
    }

    private void createUpButton(Font font, GridData gridData, Composite groupComponent) {
        upButton = new Button(groupComponent, SWT.PUSH);
        upButton.setText(Activator.getResourceString("easyshell.page.table.button.text.up"));
        upButton.setToolTipText(Activator.getResourceString("easyshell.page.table.button.tooltip.up"));
        upButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                up();
            }
        });
        upButton.setLayoutData(gridData);
        upButton.setFont(font);
        setButtonLayoutData(upButton);
    }

    private void createRemoveButton(Font font, GridData gridData, Composite groupComponent) {
        removeButton = new Button(groupComponent, SWT.PUSH);
        removeButton.setText(Activator.getResourceString("easyshell.page.table.button.text.remove"));
        removeButton.setToolTipText(Activator.getResourceString("easyshell.page.table.button.tooltip.remove"));
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                removeDialog();
            }
        });
        removeButton.setLayoutData(gridData);
        removeButton.setFont(font);
        setButtonLayoutData(removeButton);
    }

    private void createEditButton(Font font, GridData gridData, Composite groupComponent) {
        editButton = new Button(groupComponent, SWT.PUSH);
        editButton.setText(Activator.getResourceString("easyshell.page.table.button.text.edit"));
        editButton.setToolTipText(Activator.getResourceString("easyshell.page.table.button.tooltip.edit"));
        editButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                editDialog();
            }
        });
        editButton.setLayoutData(gridData);
        editButton.setFont(font);
        setButtonLayoutData(editButton);
    }

    private void createCopyButton(Font font, GridData gridData, Composite groupComponent) {
        addCopyButton = new Button(groupComponent, SWT.PUSH);
        addCopyButton.setText(Activator.getResourceString("easyshell.page.table.button.text.copy"));
        addCopyButton.setToolTipText(Activator.getResourceString("easyshell.page.table.button.tooltip.copy"));
        addCopyButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                addCopyDialog();
            }
        });
        addCopyButton.setLayoutData(gridData);
        addCopyButton.setFont(font);
        setButtonLayoutData(addCopyButton);
    }

    private void createNewButton(Font font, GridData gridData, Composite groupComponent) {
        addNewButton = new Button(groupComponent, SWT.PUSH);
        addNewButton.setText(Activator.getResourceString("easyshell.page.table.button.text.new"));
        addNewButton.setToolTipText(Activator.getResourceString("easyshell.page.table.button.tooltip.new"));
        addNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                addNewDialog();
            }
        });
        addNewButton.setLayoutData(gridData);
        addNewButton.setFont(font);
        setButtonLayoutData(addNewButton);
    }

    private int availableRows(Composite parent) {
        int fontHeight = (parent.getFont().getFontData())[0].getHeight();
        int displayHeight = parent.getDisplay().getClientArea().height;
        return displayHeight / fontHeight;
    }

    /**
     * Correctly resizes the table so no phantom columns appear
     *
     * @param table the table
     * @since 3.1
     */
    private void configureTableResizing(final Table table) {
        ControlAdapter resizer = new ControlAdapter() {
            private boolean fIsResizing = false;
            //                private final int[] fWidths= {100, 70, 70, 130, 45};
            private final int[] fWidths = { 120, 280};
            private int fSum = TABLE_WIDTH;

            public void controlResized(ControlEvent e) {
                if (fIsResizing)
                    return;
                try {
                    fIsResizing = true;
                    int clientAreaWidth = table.getClientArea().width;
                    TableColumn[] columns = table.getColumns();
                    int calculatedtableWidth = 0;

                    if (e.widget == table) {
                        int initial[] = { 120, 280 };
                        int minimums[] = new int[columns.length];
                        int minSum = 0;
                        for (int i = 0; i < columns.length; i++) {
                            // don't make a column narrower than the minimum,
                            // or than what it is currently if less than the minimum
                            minimums[i] = Math.min(fWidths[i], initial[i]);
                            minSum += minimums[i];
                        }

                        int newWidth = fSum < clientAreaWidth ? clientAreaWidth : Math.max(clientAreaWidth, minSum);
                        final int toDistribute = newWidth - fSum;
                        int lastPart = toDistribute;
                        if (toDistribute != 0) {
                            int[] iteration = { 0, 1 }; // give the description column all the rest
                            for (int i = 0; i < iteration.length; i++) {
                                int c = iteration[i];
                                int width;
                                if (fSum > 0) {
                                    int part;
                                    if (i == iteration.length - 1)
                                        part = lastPart;
                                    else
                                        // current width is the weight for the distribution of the extra space
                                        part = toDistribute * fWidths[c] / fSum;
                                    lastPart -= part;
                                    width = Math.max(minimums[c], fWidths[c] + part);
                                } else {
                                    width = toDistribute * initial[c] / TABLE_WIDTH;
                                }
                                columns[c].setWidth(width);
                                fWidths[c] = width;
                                calculatedtableWidth += width;
                            }
                            fSum = calculatedtableWidth;
                        }
                    } else {
                        // column being resized
                        // on GTK, the last column gets auto-adapted - ignore this
                        if (e.widget == columns[2])
                            return;
                        for (int i = 0; i < columns.length; i++) {
                            fWidths[i] = columns[i].getWidth();
                            calculatedtableWidth += fWidths[i];
                        }
                        fSum = calculatedtableWidth;
                    }

                    // set scroll bar visible
                    table.getHorizontalBar().setVisible(calculatedtableWidth > clientAreaWidth);
                } finally {
                    fIsResizing = false;
                }
            }
        };
        table.addControlListener(resizer);
        TableColumn[] columns = table.getColumns();
        for (int i = 0; i < columns.length; i++) {
            columns[i].addControlListener(resizer);
        }
    }

    private void addNewDialog() {
        MenuData dataNew = new MenuData(cmdList.get(0), true);
        MenuDataDialog dialog = new MenuDataDialog(getShell(), dataNew, cmdStore, cmdList, false);
        if (dialog.open() == Window.OK) {
            menuStore.add(dataNew);
            refreshTableViewer(dataNew);
        } else {
            dataNew = null;
        }
    }

    private void addCopyDialog() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        MenuData dataSelected = (MenuData)selection.getFirstElement();
        MenuData dataNew = new MenuData(dataSelected, true);
        MenuDataDialog dialog = new MenuDataDialog(getShell(), dataNew, cmdStore, cmdList, false);
        if (dialog.open() == Window.OK) {
            menuStore.add(dataNew);
            refreshTableViewer(dataNew);
        } else {
            dataNew = null;
        }
    }

    private void editDialog() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        MenuData dataSelected = (MenuData)selection.getFirstElement();
        MenuData dataNew = new MenuData(dataSelected, false);
        dataNew.setPosition(dataSelected.getPosition());
        MenuDataDialog dialog = new MenuDataDialog(getShell(), dataNew, cmdStore, cmdList, true);
        if (dialog.open() == Window.OK) {
            menuStore.replace(dataNew);
            refreshTableViewer(dataNew);
        } else {
            dataNew = null;
        }

    }

    private void removeDialog() {
        String title = Activator.getResourceString("easyshell.page.table.dialog.remove.title");
        String question = Activator.getResourceString("easyshell.page.table.dialog.remove.question");
        MessageDialog dialog = new MessageDialog(
                null, title, null, question,
                MessageDialog.QUESTION,
                new String[] {"Yes", "No"},
                1); // no is the default
        int result = dialog.open();
        if (result == 0) {
            IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
            Iterator<?> elements = selection.iterator();
            while (elements.hasNext()) {
                MenuData data = (MenuData) elements.next();
                menuStore.delete(data);
            }
            tableViewer.refresh();
        }
    }

    private void up() {
        itemMover.moveCurrentSelectionUp();
        tableViewer.refresh();
    }

    private void down() {
        itemMover.moveCurrentSelectionDown();
        tableViewer.refresh();
    }

    private void refreshTableViewer(MenuData data) {
        tableViewer.refresh();
        tableViewer.setChecked(data, data.isEnabled());
        tableViewer.setSelection(new StructuredSelection(data));
    }

}