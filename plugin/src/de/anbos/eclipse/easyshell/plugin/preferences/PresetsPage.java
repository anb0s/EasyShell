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

public class PresetsPage extends org.eclipse.jface.preference.PreferencePage
        implements IWorkbenchPreferencePage {

    private static final int TABLE_WIDTH = 400;

    private Table table;
    private ItemMover itemMover;
    private PresetsStore store;
    private CheckboxTableViewer tableViewer;
    private Button addButton;
    private Button editButton;
    private Button upButton;
    private Button downButton;
    private Button removeButton;

    public void init(IWorkbench workbench) {
    }

    public boolean performOk() {
        store.save();
        return true;
    }

    public void performDefaults() {
        store.loadDefault();
        tableViewer.refresh();
    }

    protected void performApply() {
        performOk();
    }

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
        GridData data = new GridData(GridData.FILL_BOTH);
        // create table
        table = new Table(pageComponent, SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setFont(parent.getFont());

        TableColumn column1 = new TableColumn(table, SWT.LEFT);
        column1.setText(Activator.getResourceString("easyshell.preseteditor.table.header.column0.title")); //$NON-NLS-1$
        column1.setResizable(false);

        TableColumn column2 = new TableColumn(table, SWT.LEFT);
        column2.setText(Activator.getResourceString("easyshell.preseteditor.table.header.column1.title")); //$NON-NLS-1$
        column2.setResizable(false);

        int availableRows = availableRows(pageComponent);
        data.heightHint = table.getItemHeight() * (availableRows / 8);
        data.widthHint = TABLE_WIDTH;
        table.setLayoutData(data);

        tableViewer = new CheckboxTableViewer(table);
        tableViewer.setLabelProvider(new PresetLabelProvider());
        tableViewer.setContentProvider(new PresetContentProvider());
        store = new PresetsStore(Activator.getDefault().getPreferenceStore());
        store.load();
        tableViewer.setInput(store);
        tableViewer.setAllChecked(false);
        tableViewer.setCheckedElements(store.getAllEnabledPresets());

        tableViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                edit();
            }
        });

        tableViewer.addCheckStateListener(new ICheckStateListener() {
            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                PresetData data = (PresetData) event.getElement();
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
                if (!(object1 instanceof PresetData) || !(object2 instanceof PresetData)) {
                    return super.compare(viewer, object1, object2);
                }
                PresetData data1 = (PresetData) object1;
                PresetData data2 = (PresetData) object2;
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

        itemMover = new ItemMover(table, store);

        // button pageComponent
        Composite groupComponent = new Composite(pageComponent, SWT.NULL);
        GridLayout groupLayout = new GridLayout();
        groupLayout.marginWidth = 0;
        groupLayout.marginHeight = 0;
        groupComponent.setLayout(groupLayout);
        data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        groupComponent.setLayoutData(data);
        groupComponent.setFont(font);

        // buttons
        addButton = new Button(groupComponent, SWT.PUSH);
        addButton.setText(Activator.getResourceString("easyshell.preseteditor.button.add")); //$NON-NLS-1$
        addButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                add();
            }
        });
        addButton.setLayoutData(data);
        addButton.setFont(font);
        setButtonLayoutData(addButton);

        editButton = new Button(groupComponent, SWT.PUSH);
        editButton.setText(Activator.getResourceString("easyshell.preseteditor.button.edit")); //$NON-NLS-1$
        editButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                edit();
            }
        });
        editButton.setLayoutData(data);
        editButton.setFont(font);
        setButtonLayoutData(editButton);

        removeButton = new Button(groupComponent, SWT.PUSH);
        removeButton.setText(Activator.getResourceString("easyshell.preseteditor.button.remove")); //$NON-NLS-1$
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                remove();
            }
        });
        removeButton.setFont(font);
        setButtonLayoutData(removeButton);

        upButton = new Button(groupComponent, SWT.PUSH);
        upButton.setText(Activator.getResourceString("easyshell.preseteditor.button.up")); //$NON-NLS-1$
        upButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                up();
            }
        });
        upButton.setFont(font);
        setButtonLayoutData(upButton);

        downButton = new Button(groupComponent, SWT.PUSH);
        downButton.setText(Activator.getResourceString("easyshell.preseteditor.button.down")); //$NON-NLS-1$
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
        table.pack();
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
        PresetData data = new PresetData();
        PresetDialog dialog = new PresetDialog(getShell(), data, false);
        if (dialog.open() == Window.OK) {
            store.add(data);
            table.pack();
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
            PresetData data = (PresetData) elements.next();
            store.delete(data);
        }
        table.pack();
        tableViewer.refresh();
    }

    private void edit() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        PresetData data = (PresetData) selection.getFirstElement();
        PresetDialog dialog = new PresetDialog(getShell(), data, true);
        if (dialog.open() == Window.OK) {
            table.pack();
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