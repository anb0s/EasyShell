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
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

public class CommandPage extends org.eclipse.jface.preference.PreferencePage
        implements IWorkbenchPreferencePage {

    private Text searchText;
    private CommandDataFilter filter;
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
        if (!CommandDataStore.instance().isMigrated()) {
            String title = Activator.getResourceString("easyshell.command.page.dialog.migration.title");
            String question = Activator.getResourceString("easyshell.command.page.dialog.migration.question");
            MessageDialog dialog = new MessageDialog(
                    null, title, null, question,
                    MessageDialog.WARNING,
                    new String[] {"Yes", "No"},
                    1); // no is the default
            int result = dialog.open();
            if (result == 0) {
                CommandDataStore.instance().setMigrated(true);
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
        // get the selected commands and referenced menus as lists
        CommandDataList commands = new CommandDataList(CommandDataStore.instance().getDataList());
        List<MenuData> menus = new ArrayList<MenuData>();
        for(CommandData command : commands) {
            if (command.getPresetType() == PresetType.presetUser) {
                List<MenuData> menusForOne = MenuDataStore.instance().getRefencedBy(command.getId());
                menus.addAll(menusForOne);
            }
        }
        String title = Activator.getResourceString("easyshell.command.page.dialog.defaults.title");
        String question = Activator.getResourceString("easyshell.command.page.dialog.defaults.question");
        int dialogImageType = MessageDialog.QUESTION;
        if (menus.size() >= 0) {
            dialogImageType = MessageDialog.WARNING;
            String menuNames = "";
            for (MenuData menu : menus) {
                menuNames += menu.getNameExpanded() + "\n";
            }
            question = MessageFormat.format(Activator.getResourceString("easyshell.command.page.dialog.defaults.menu.question"),
                    menuNames);
        }
        MessageDialog dialog = new MessageDialog(
                null, title, null, question,
                dialogImageType,
                new String[] {"Yes", "No"},
                1); // no is the default
        int result = dialog.open();
        if (result == 0) {
            if (menus.size() >= 0) {
                for (MenuData menu : menus) {
                    MenuDataStore.instance().delete(menu);
                }
                //MenuDataStore.instance().save();
            }
            CommandDataStore.instance().loadDefaults();
            tableViewer.refresh();
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
        //Label searchLabel = new Label(parent, SWT.NONE);
        //searchLabel.setText("Search: ");
        filter = new CommandDataFilter();
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
        createEditButton(font, gridData, groupComponent);
        createCopyButton(font, gridData, groupComponent);
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
        tableViewer.setInput(CommandDataStore.instance());

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
                int selected = selection.size();
                boolean presetSelectedOrNotEqualType = false;
                Iterator<?> elements = selection.iterator();
                PresetType type = PresetType.presetUnknown;
                while (elements.hasNext()) {
                    CommandData data = (CommandData) elements.next();
                    if (type == PresetType.presetUnknown) {
                        type = data.getPresetType();
                    }
                    if (data.getPresetType() == PresetType.presetPlugin || type != data.getPresetType()) {
                        presetSelectedOrNotEqualType = true;
                        break;
                    }
                }
                editButton.setEnabled(selected == 1);
                addCopyButton.setEnabled(selected == 1);
                removeButton.setEnabled(selected > 0 && !presetSelectedOrNotEqualType);
            }
        });

        tableViewer.setComparator(new ViewerComparator() {
            @Override
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
            @Override
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
        CommandDataDialog dialog = new CommandDataDialog(getShell(), data, title);
        if (dialog.open() == Window.OK) {
            CommandDataStore.instance().add(data);
            refreshTableViewer(data);
        } else {
            data = null;
        }
    }

    private void addNewDialog() {
        CommandData dataNew = new CommandData(null, PresetType.presetUser, Utils.getOS(), "MyNewCommand", ResourceType.resourceTypeFileOrDirectory, Category.categoryUser, CommandType.commandTypeExecute, "my_new_command");
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
        CommandData dataNew = new CommandData(dataSelected, false);
        dataNew.setPosition(dataSelected.getPosition());
        String title = MessageFormat.format(Activator.getResourceString("easyshell.command.editor.dialog.title.edit"), dataNew.getPresetType().getName());
        CommandDataDialog dialog = new CommandDataDialog(getShell(), dataNew, title);
        if (dialog.open() == Window.OK) {
            CommandDataStore.instance().replace(dataNew);
            refreshTableViewer(dataNew);
        } else {
            dataNew = null;
        }
    }

    private void removeDialog() {
        // get the selected commands and referenced menus as lists
        List<CommandData> commands = new ArrayList<CommandData>();
        List<MenuData> menus = new ArrayList<MenuData>();
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        Iterator<?> elements = selection.iterator();
        while (elements.hasNext()) {
            CommandData data = (CommandData) elements.next();
            commands.add(data);
            List<MenuData> menusForOne = MenuDataStore.instance().getRefencedBy(data.getId());
            menus.addAll(menusForOne);
        }
        // ask user
        String commandNames = "";
        PresetType type = PresetType.presetUnknown;
        for (CommandData command : commands) {
            if (type == PresetType.presetUnknown) {
                type = command.getPresetType();
            }
            commandNames += command.getCommandAsComboName() + "\n";
        }
        String title = null;
        String question = null;
        if (type == PresetType.presetPluginModify) {
            title = Activator.getResourceString("easyshell.command.page.dialog.remove.user.title");
            question = MessageFormat.format(
                    Activator.getResourceString("easyshell.command.page.dialog.remove.user.question"),
                    commandNames);
        } else {
            title = Activator.getResourceString("easyshell.command.page.dialog.remove.title");
            question = MessageFormat.format(
                    Activator.getResourceString("easyshell.command.page.dialog.remove.question"),
                    commandNames);
        }
        int dialogImageType = MessageDialog.QUESTION;
        if (menus.size() > 0) {
            dialogImageType = MessageDialog.WARNING;
            String menuNames = "";
            for (MenuData menu : menus) {
                menuNames += menu.getNameExpanded() + "\n";
            }
            if (type == PresetType.presetPluginModify) {
                title = Activator.getResourceString("easyshell.command.page.dialog.remove.menu.user.title");
                question = MessageFormat.format(Activator.getResourceString("easyshell.command.page.dialog.remove.menu.user.question"),
                        commandNames, menuNames);
            } else {
                title = Activator.getResourceString("easyshell.command.page.dialog.remove.menu.title");
                question = MessageFormat.format(Activator.getResourceString("easyshell.command.page.dialog.remove.menu.question"),
                        commandNames, menuNames);
            }
        }
        MessageDialog dialog = new MessageDialog(
                null, title, null, question,
                dialogImageType,
                new String[] {"Yes", "No"},
                1); // no is the default
        int result = dialog.open();
        if (result == 0) {
            if (menus.size() >= 0 && type == PresetType.presetUser) {
                for (MenuData menu : menus) {
                    MenuDataStore.instance().delete(menu);
                }
                //MenuDataStore.instance().save();
            }
            for (CommandData command : commands) {
                if (command.getPresetType() == PresetType.presetUser) {
                    CommandDataStore.instance().delete(command);
                } else if (command.getPresetType() == PresetType.presetPluginModify) {
                    command.removeModifyData();
                    CommandDataStore.instance().replace(command);
                }
            }
            tableViewer.refresh();
        }
    }

    private void refreshTableViewer(CommandData data) {
        tableViewer.refresh();
        tableViewer.setSelection(new StructuredSelection(data));
    }

}
