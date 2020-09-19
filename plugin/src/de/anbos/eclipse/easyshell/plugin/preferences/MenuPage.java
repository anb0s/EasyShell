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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.anbos.eclipse.easyshell.plugin.Activator;

public class MenuPage extends org.eclipse.jface.preference.PreferencePage
        implements IWorkbenchPreferencePage {

    List<CommandData> commandList;
    private MenuDataMover itemMover;
    private Text searchText;
    private MenuDataFilter filter;
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
        if (!MenuDataStore.instance().isMigrated()) {
            String title = Activator.getResourceString("easyshell.menu.page.dialog.migration.title");
            String question = Activator.getResourceString("easyshell.menu.page.dialog.migration.question");
            MessageDialog dialog = new MessageDialog(
                    null, title, null, question,
                    MessageDialog.WARNING,
                    new String[] {"Yes", "No"},
                    1); // no is the default
            int result = dialog.open();
            if (result == 0) {
                MenuDataStore.instance().setMigrated(true);
            } else {
                save = false;
            }
        }
        if (save) {
            CommandDataStore.instance().save();
            MenuDataStore.instance().save();
        }
        return save;
    }

    @Override
    protected void performApply() {
        performOk();
    }

    @Override
    protected void performDefaults() {
        String title = Activator.getResourceString("easyshell.menu.page.dialog.defaults.title");
        String question = Activator.getResourceString("easyshell.menu.page.dialog.defaults.question");
        MessageDialog dialog = new MessageDialog(
                null, title, null, question,
                MessageDialog.WARNING,
                new String[] {"Yes", "No"},
                1); // no is the default
        int result = dialog.open();
        if (result == 0) {
            MenuDataStore.instance().loadDefaults();
            refreshTableViewer();
        }
    }

    @Override
    protected Control createContents(Composite parent) {
        // main page composite
        Composite pageComponent = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3, false);
        pageComponent.setLayout(layout);
        //parent.setLayout(layout);

        // search
        createSearchField(pageComponent);

        // get the native commands list
        commandList = CommandDataDefaultCollection.getCommandsNativeAll(new CommandDataList(CommandDataStore.instance().getDataList()));

        // table viewer
        createTableViewer(pageComponent);

        // buttons
        createButtons(pageComponent);

        // refresh the viewer
        refreshTableViewer();

        // send event to refresh tableViewer selection
        Event event = new Event();
        event.item = null;
        tableViewer.getTable().notifyListeners(SWT.Selection, event);

        return pageComponent;
    }

    private void createSearchField(Composite parent) {
        //Label searchLabel = new Label(parent, SWT.NONE);
        //searchLabel.setText("Search: ");
        filter = new MenuDataFilter();
        searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
        searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
            | GridData.HORIZONTAL_ALIGN_FILL));
        searchText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
              filter.setSearchText(searchText.getText());
              refreshTableViewer();
            }

        });
        searchText.setToolTipText(Activator.getResourceString("easyshell.command.page.text.tooltip.search"));
        // fake
        Label label = new Label(parent, SWT.NONE);
        label.setText("");
    }

    private void createButtons(Composite pageComponent) {
        Font font = pageComponent.getFont();
        // button pageComponent
        Composite groupComponent = new Composite(pageComponent, SWT.NULL);
        GridLayout groupLayout = new GridLayout();
        groupLayout.marginWidth = 0;
        groupLayout.marginHeight = 0;
        groupComponent.setLayout(groupLayout);
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalAlignment = GridData.FILL;
        groupComponent.setLayoutData(gridData);
        groupComponent.setFont(font);

        // buttons
        createNewButton(font, gridData, groupComponent);
        createEditButton(font, gridData, groupComponent);
        createCopyButton(font, gridData, groupComponent);
        createRemoveButton(font, gridData, groupComponent);
        createUpButton(font, gridData, groupComponent);
        createDownButton(font, gridData, groupComponent);
    }

    private void createTableViewer(Composite parent) {
        tableViewer = CheckboxTableViewer.newCheckList(parent, SWT.MULTI | SWT.H_SCROLL
            | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, tableViewer);
        final Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        tableViewer.setLabelProvider(new MenuDataLabelProvider());
        tableViewer.setContentProvider(new MenuDataContentProvider());
        if (filter != null) {
            tableViewer.addFilter(filter);
        }

        // Get the content for the viewer, setInput will call getElements in the
        // contentProvider
        tableViewer.setInput(MenuDataStore.instance());

        // Layout the viewer
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        tableViewer.getControl().setLayoutData(gridData);

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
                int selected = selection.size();
                addCopyButton.setEnabled(selected == 1);
                editButton.setEnabled(selected == 1);
                removeButton.setEnabled(selected > 0);
                upButton.setEnabled(selected == 1);
                downButton.setEnabled(selected == 1);
            }
        });

        tableViewer.setComparator(new ViewerComparator() {
            @Override
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
            @Override
            public boolean isSorterProperty(Object element, String property) {
                return true;
            }
        });

        itemMover = new MenuDataMover(table, MenuDataStore.instance());
      }

    private void createColumns(final Composite parent, final TableViewer viewer) {
        TableViewerColumn viewerColumn1 = new TableViewerColumn(viewer,
                SWT.NONE);
        TableColumn column1 = viewerColumn1.getColumn();
        column1.setText(Activator.getResourceString("easyshell.menu.page.header.title.column0"));
        column1.setWidth(200);
        column1.setResizable(true);
        column1.setMoveable(true);

        TableViewerColumn viewerColumn2 = new TableViewerColumn(viewer,
                SWT.NONE);
        TableColumn column2 = viewerColumn2.getColumn();
        column2.setText(Activator.getResourceString("easyshell.menu.page.header.title.column1"));
        column2.setWidth(400);
        column2.setResizable(true);
        column2.setMoveable(true);
    }

    private void createDownButton(Font font, GridData gridData, Composite groupComponent) {
        downButton = new Button(groupComponent, SWT.PUSH);
        downButton.setText(Activator.getResourceString("easyshell.menu.page.button.text.down"));
        downButton.setToolTipText(Activator.getResourceString("easyshell.menu.page.button.tooltip.down"));
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
        upButton.setText(Activator.getResourceString("easyshell.menu.page.button.text.up"));
        upButton.setToolTipText(Activator.getResourceString("easyshell.menu.page.button.tooltip.up"));
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
        removeButton.setText(Activator.getResourceString("easyshell.menu.page.button.text.remove"));
        removeButton.setToolTipText(Activator.getResourceString("easyshell.menu.page.button.tooltip.remove"));
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
        editButton.setText(Activator.getResourceString("easyshell.menu.page.button.text.edit"));
        editButton.setToolTipText(Activator.getResourceString("easyshell.menu.page.button.tooltip.edit"));
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
        addCopyButton.setText(Activator.getResourceString("easyshell.menu.page.button.text.copy"));
        addCopyButton.setToolTipText(Activator.getResourceString("easyshell.menu.page.button.tooltip.copy"));
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
        addNewButton.setText(Activator.getResourceString("easyshell.menu.page.button.text.new"));
        addNewButton.setToolTipText(Activator.getResourceString("easyshell.menu.page.button.tooltip.new"));
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

    private void addDialog(MenuData data) {
        MenuDataDialog dialog = new MenuDataDialog(getShell(), data, commandList, false);
        if (dialog.open() == Window.OK) {
            MenuDataStore.instance().add(data);
            refreshTableViewer();
        } else {
            data = null;
        }
    }

    private void addNewDialog() {
        MenuData dataNew = new MenuData(commandList.get(0).getId(), true);
        addDialog(dataNew);
    }

    private void addCopyDialog() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        MenuData dataSelected = (MenuData)selection.getFirstElement();
        MenuData dataNew = new MenuData(dataSelected, true);
        addDialog(dataNew);
    }

    private void editDialog() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        MenuData dataSelected = (MenuData)selection.getFirstElement();
        MenuData dataNew = new MenuData(dataSelected, false);
        dataNew.setPosition(dataSelected.getPosition());
        MenuDataDialog dialog = new MenuDataDialog(getShell(), dataNew, commandList, true);
        if (dialog.open() == Window.OK) {
            MenuDataStore.instance().replace(dataNew);
            refreshTableViewer();
        } else {
            dataNew = null;
        }
    }

    private void removeDialog() {
        // get the selected menus as lists
        List<MenuData> menus = new ArrayList<MenuData>();
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        Iterator<?> elements = selection.iterator();
        while (elements.hasNext()) {
            MenuData data = (MenuData) elements.next();
            menus.add(data);
        }
        if (menus.size() > 0) {
            String title = Activator.getResourceString("easyshell.menu.page.dialog.remove.title");
            String menuNames = "";
            for (MenuData menu : menus) {
                menuNames += menu.getNameExpanded() + "\n";
            }
            String question = MessageFormat.format(Activator.getResourceString("easyshell.menu.page.dialog.remove.question"),
                    menuNames);
            MessageDialog dialog = new MessageDialog(
                    null, title, null, question,
                    MessageDialog.QUESTION,
                    new String[] {"Yes", "No"},
                    1); // no is the default
            int result = dialog.open();
            if (result == 0) {
                for (MenuData menu : menus) {
                    MenuDataStore.instance().delete(menu);
                }
                refreshTableViewer();
            }
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

    private void refreshTableViewer() {
        tableViewer.refresh();
        tableViewer.setAllChecked(false);
        tableViewer.setCheckedElements(MenuDataStore.instance().getEnabledCommandMenuDataArray());
    }

}
