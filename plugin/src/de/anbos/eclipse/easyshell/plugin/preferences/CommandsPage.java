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
    private CommandMenuDataMover itemMover;
    private CommandDataStore cmdStore;
    List<CommandData> cmdList;
    private CommandMenuDataStore menuStore;
    private CheckboxTableViewer tableViewer;
    private Button addButton;
    private Button editButton;
    private Button upButton;
    private Button downButton;
    private Button removeButton;

    @Override
    public void init(IWorkbench workbench) {
    }

    @Override
    public boolean performOk() {
        menuStore.save();
        return true;
    }

    @Override
    protected void performDefaults() {
        menuStore.loadDefaults();
        tableViewer.refresh();
        for (CommandMenuData item : menuStore.getCommandMenuDataList()) {
            tableViewer.setChecked(item, true);
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
        column1.setText(Activator.getResourceString("easyshell.page.table.header.column0.title")); //$NON-NLS-1$
        column1.setResizable(false);

        TableColumn column2 = new TableColumn(table, SWT.LEFT);
        column2.setText(Activator.getResourceString("easyshell.page.table.header.column1.title")); //$NON-NLS-1$
        column2.setResizable(false);

        int availableRows = availableRows(pageComponent);
        gridData.heightHint = table.getItemHeight() * (availableRows / 8);
        gridData.widthHint = TABLE_WIDTH;
        table.setLayoutData(gridData);

        tableViewer = new CheckboxTableViewer(table);
        tableViewer.setLabelProvider(new CommandMenuDataLabelProvider());
        tableViewer.setContentProvider(new CommandMenuDataContentProvider());

        // command store
        cmdStore = new CommandDataStore(Activator.getDefault().getPreferenceStore());
        cmdStore.load();

        // get the native commands list
        cmdList = CommandDataDefaultCollection.getCommandsNative(cmdStore.getAllCommands());

        // menu store
        menuStore = new CommandMenuDataStore(Activator.getDefault().getPreferenceStore());
        menuStore.load();
        tableViewer.setInput(menuStore);
        tableViewer.setAllChecked(false);
        tableViewer.setCheckedElements(menuStore.getEnabledCommandMenuDataArray());

        tableViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                edit();
            }
        });

        tableViewer.addCheckStateListener(new ICheckStateListener() {
            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                CommandMenuData data = (CommandMenuData) event.getElement();
                data.setEnabled(event.getChecked());
            }
        });

        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
                boolean selected = !selection.isEmpty();
                editButton.setEnabled(selected);
                removeButton.setEnabled(selected);
                upButton.setEnabled(selected);
                downButton.setEnabled(selected);
            }
        });

        tableViewer.setSorter(new ViewerSorter() {
            public int compare(Viewer viewer, Object object1, Object object2) {
                if (!(object1 instanceof CommandMenuData) || !(object2 instanceof CommandMenuData)) {
                    return super.compare(viewer, object1, object2);
                }
                CommandMenuData data1 = (CommandMenuData) object1;
                CommandMenuData data2 = (CommandMenuData) object2;
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

        itemMover = new CommandMenuDataMover(table, menuStore);

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
        addButton = new Button(groupComponent, SWT.PUSH);
        addButton.setText(Activator.getResourceString("easyshell.page.table.button.add")); //$NON-NLS-1$
        addButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                add();
            }
        });
        addButton.setLayoutData(gridData);
        addButton.setFont(font);
        setButtonLayoutData(addButton);

        editButton = new Button(groupComponent, SWT.PUSH);
        editButton.setText(Activator.getResourceString("easyshell.page.table.button.edit")); //$NON-NLS-1$
        editButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                edit();
            }
        });
        editButton.setLayoutData(gridData);
        editButton.setFont(font);
        setButtonLayoutData(editButton);

        removeButton = new Button(groupComponent, SWT.PUSH);
        removeButton.setText(Activator.getResourceString("easyshell.page.table.button.remove")); //$NON-NLS-1$
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                remove();
            }
        });
        removeButton.setFont(font);
        setButtonLayoutData(removeButton);

        upButton = new Button(groupComponent, SWT.PUSH);
        upButton.setText(Activator.getResourceString("easyshell.page.table.button.up")); //$NON-NLS-1$
        upButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                up();
            }
        });
        upButton.setFont(font);
        setButtonLayoutData(upButton);

        downButton = new Button(groupComponent, SWT.PUSH);
        downButton.setText(Activator.getResourceString("easyshell.page.table.button.down")); //$NON-NLS-1$
        downButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                down();
            }
        });
        downButton.setFont(font);
        setButtonLayoutData(downButton);

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

    private void add() {
        CommandMenuData data = new CommandMenuData();
        data.setCommandData(cmdList.get(0), false);
        CommandMenuDataDialog dialog = new CommandMenuDataDialog(getShell(), data, cmdStore, cmdList, false);
        if (dialog.open() == Window.OK) {
            menuStore.add(data);
            tableViewer.refresh();
            tableViewer.setChecked(data, data.isEnabled());
            tableViewer.setSelection(new StructuredSelection(data));
            return;
        }
    }

    private void remove() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        Iterator<?> elements = selection.iterator();
        while (elements.hasNext()) {
            CommandMenuData data = (CommandMenuData) elements.next();
            menuStore.delete(data);
        }
        tableViewer.refresh();
    }

    private void edit() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        CommandMenuData data = (CommandMenuData) selection.getFirstElement();
        CommandMenuDataDialog dialog = new CommandMenuDataDialog(getShell(), data, cmdStore, cmdList, true);
        if (dialog.open() == Window.OK) {
            tableViewer.refresh();
            tableViewer.setChecked(data, data.isEnabled());
            tableViewer.setSelection(new StructuredSelection(data));
            return;
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

}