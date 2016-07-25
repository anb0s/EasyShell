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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
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
import de.anbos.eclipse.easyshell.plugin.Utils;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

public class CommandPage extends org.eclipse.jface.preference.PreferencePage
        implements IWorkbenchPreferencePage {

    private CommandDataStore commandStore;
    private Text searchText;
    private CommandTableFilter filter;
    private TableViewer tableViewer;
    private Button addNewButton;
    private Button addCopyButton;
    private Button editButton;
    private Button removeButton;

    @Override
    public void init(IWorkbench workbench) {
    }

    @Override
    public boolean performOk() {
        boolean save = true;
        if (!commandStore.isMigrated()) {
            String title = Activator.getResourceString("easyshell.command.page.dialog.migration.title");
            String question = Activator.getResourceString("easyshell.command.page.dialog.migration.question");
            MessageDialog dialog = new MessageDialog(
                    null, title, null, question,
                    MessageDialog.WARNING,
                    new String[] {"Yes", "No"},
                    1); // no is the default
            int result = dialog.open();
            if (result == 0) {
                commandStore.setMigrated(true);
            } else {
                save = false;
            }
        }
        if (save) {
            commandStore.save();
        }
        return save;
    }

    @Override
    protected void performDefaults() {
        String title = Activator.getResourceString("easyshell.command.page.dialog.defaults.title");
        String question = Activator.getResourceString("easyshell.command.page.dialog.defaults.question");
        MessageDialog dialog = new MessageDialog(
                null, title, null, question,
                MessageDialog.WARNING,
                new String[] {"Yes", "No"},
                1); // no is the default
        int result = dialog.open();
        if (result == 0) {
            commandStore.loadDefaults();
            tableViewer.refresh();
        }
    }

    @Override
    protected void performApply() {
        performOk();
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

        // command store
        commandStore = new CommandDataStore(Activator.getDefault().getPreferenceStore());
        commandStore.load();

        // table viewer
        createTableViewer(pageComponent);

        // buttons
        createButtons(pageComponent);

        // send event to refresh tableViewer
        Event event = new Event();
        event.item = null;
        tableViewer.refresh();
        tableViewer.getTable().notifyListeners(SWT.Selection, event);

        return pageComponent;
    }

    private void createSearchField(Composite parent) {
        /*
        Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
        group.setText(Activator.getResourceString("easyshell.command.page.text.text.search"));
        group.setToolTipText(Activator.getResourceString("easyshell.command.page.text.tooltip.search"));
        GridLayout layout2 = new GridLayout();
        layout2.numColumns = 1;
        layout2.makeColumnsEqualWidth = false;
        layout2.marginWidth = 5;
        layout2.marginHeight = 4;
        group.setLayout(layout2);
        GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(data2);
        group.setFont(parent.getFont());*/

        //Label searchLabel = new Label(parent, SWT.NONE);
        //searchLabel.setText("Search: ");
        filter = new CommandTableFilter();
        searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
        searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
            | GridData.HORIZONTAL_ALIGN_FILL));
        searchText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
              filter.setSearchText(searchText.getText());
              tableViewer.refresh();
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
        createCopyButton(font, gridData, groupComponent);
        createEditButton(font, gridData, groupComponent);
        createRemoveButton(font, gridData, groupComponent);
    }

    private void createTableViewer(Composite parent) {
        tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
            | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, tableViewer);
        final Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        tableViewer.setLabelProvider(new CommandDataLabelProvider());
        tableViewer.setContentProvider(new CommandDataContentProvider());
        if (filter != null) {
            tableViewer.addFilter(filter);
        }

        // Get the content for the viewer, setInput will call getElements in the
        // contentProvider
        tableViewer.setInput(commandStore);

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

        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
                boolean selected = !selection.isEmpty();
                boolean presetSelected = false;
                Iterator<?> elements = selection.iterator();
                while (elements.hasNext()) {
                    CommandData data = (CommandData) elements.next();
                    if (data.getPresetType() == PresetType.presetPlugin) {
                        presetSelected = true;
                        break;
                    }
                }
                addCopyButton.setEnabled(selected);
                editButton.setEnabled(selected);
                removeButton.setEnabled(selected && !presetSelected);
            }
        });

        tableViewer.setSorter(new ViewerSorter() {
            public int compare(Viewer viewer, Object object1, Object object2) {
                if (!(object1 instanceof CommandData) || !(object2 instanceof CommandData)) {
                    return super.compare(viewer, object1, object2);
                }
                CommandData data1 = (CommandData) object1;
                CommandData data2 = (CommandData) object2;
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
      }

    private void createColumns(final Composite parent, final TableViewer viewer) {
        String[] titles = {
                Activator.getResourceString("easyshell.command.page.header.title.column0"),
                Activator.getResourceString("easyshell.command.page.header.title.column1"),
                Activator.getResourceString("easyshell.command.page.header.title.column2"),
                Activator.getResourceString("easyshell.command.page.header.title.column3")
        };
        int[] bounds = { 100, 100, 80, 400 };
        // create
        for (int i=0;i<titles.length;i++) {
            TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
                    SWT.NONE);
            TableColumn column = viewerColumn.getColumn();
            column.setText(titles[i]);
            column.setWidth(bounds[i]);
            column.setResizable(true);
            column.setMoveable(true);
        }
    }

    private void createRemoveButton(Font font, GridData gridData, Composite groupComponent) {
        removeButton = new Button(groupComponent, SWT.PUSH);
        removeButton.setText(Activator.getResourceString("easyshell.command.page.button.text.remove"));
        removeButton.setToolTipText(Activator.getResourceString("easyshell.command.page.button.tooltip.remove"));
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
        editButton.setText(Activator.getResourceString("easyshell.command.page.button.text.edit"));
        editButton.setToolTipText(Activator.getResourceString("easyshell.command.page.button.tooltip.edit"));
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
        addCopyButton.setText(Activator.getResourceString("easyshell.command.page.button.text.copy"));
        addCopyButton.setToolTipText(Activator.getResourceString("easyshell.command.page.button.tooltip.copy"));
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
        addNewButton.setText(Activator.getResourceString("easyshell.command.page.button.text.new"));
        addNewButton.setToolTipText(Activator.getResourceString("easyshell.command.page.button.tooltip.new"));
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

    private void addDialog(CommandData data, boolean copy) {
        String title = Activator.getResourceString("easyshell.command.editor.dialog.title.new");
        if (copy) {
            title = Activator.getResourceString("easyshell.command.editor.dialog.title.copy");
        }
        CommandDataDialog dialog = new CommandDataDialog(getShell(), data, title, true);
        if (dialog.open() == Window.OK) {
            commandStore.add(data);
            refreshTableViewer(data);
        } else {
            data = null;
        }
    }

    private void addNewDialog() {
        CommandData dataNew = new CommandData(null, PresetType.presetUser, Utils.getOS(), "MyNewCommand", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeOther, "my_new_command");
        addDialog(dataNew, false);
    }

    private void addCopyDialog() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        CommandData dataSelected = (CommandData)selection.getFirstElement();
        CommandData dataNew = new CommandData(dataSelected, true);
        dataNew.setPresetType(PresetType.presetUser);
        addDialog(dataNew, true);
    }

    private void editDialog() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        CommandData dataSelected = (CommandData)selection.getFirstElement();
        if (dataSelected.getPresetType() == PresetType.presetUser) {
            CommandData dataNew = new CommandData(dataSelected, false);
            dataNew.setPosition(dataSelected.getPosition());
            CommandDataDialog dialog = new CommandDataDialog(getShell(), dataNew, Activator.getResourceString("easyshell.command.editor.dialog.title.edit"), true);
            if (dialog.open() == Window.OK) {
                commandStore.replace(dataNew);
                refreshTableViewer(dataNew);
            } else {
                dataNew = null;
            }
        } else {
            CommandDataDialog dialog = new CommandDataDialog(getShell(), dataSelected, Activator.getResourceString("easyshell.command.editor.dialog.title.show"), false);
            dialog.open();
        }
    }

    private void removeDialog() {
        String title = Activator.getResourceString("easyshell.command.page.dialog.remove.title");
        String question = Activator.getResourceString("easyshell.command.page.dialog.remove.question");
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
                CommandData data = (CommandData) elements.next();
                commandStore.delete(data);
            }
            tableViewer.refresh();
        }
    }

    private void refreshTableViewer(CommandData data) {
        tableViewer.refresh();
        tableViewer.setSelection(new StructuredSelection(data));
    }

}