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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.exceptions.UnknownCommandID;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.misc.UtilsUI;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.Variable;
import de.anbos.eclipse.easyshell.plugin.types.MenuNameType;

public class MenuDataDialog extends StatusDialog {

    private MenuData menuData;
    private List<CommandData> cmdList;

    private Button  enabledCheckBox;

    private Text searchText;
    private CommandDataFilter filter;
    private TypedComboBox<CommandData> commandComboViewer;

    private Combo   nameTypeCombo;
    private Text    namePatternText;
    ContentProposalAdapter namePatternTextAssist;
    private Button  menuImageButton;
    private Text    menuNameText;
    private Text    commandText;

    private Button addNewButton;
    private Button addCopyButton;
    private Button editButton;
    private Button removeButton;

    private boolean firstRefresh = true;

    public MenuDataDialog(Shell parent, MenuData menuData, List<CommandData> cmdList, boolean edit) {
        super(parent);
        this.menuData = menuData;
        this.cmdList = cmdList;
        // do layout and title
        setShellStyle(getShellStyle() | SWT.MAX);
        // set title
        String title = null;
        if(edit) {
            title = Activator.getResourceString("easyshell.menu.editor.dialog.title.edit"); //$NON-NLS-1$
        } else {
            title = Activator.getResourceString("easyshell.menu.editor.dialog.title.new"); //$NON-NLS-1$
        }
        setTitle(title);
    }

    @Override
    protected boolean isResizable() {
      return true;
    }

    @Override
    protected void okPressed() {
        if (!validateValues()) {
            return;
        }
        menuData.setEnabled(enabledCheckBox.getSelection());
        menuData.setNameType(getAllNameTypes()[nameTypeCombo.getSelectionIndex()]);
        menuData.setNamePattern(namePatternText.getText());
        menuData.setCommandId(commandComboViewer.getSelection().getId());
        super.okPressed();
    }

    @Override
    protected void cancelPressed() {
        super.cancelPressed();
    }

    public Control createDialogArea(Composite parent) {
        Composite pageComponent = createPageComponent(parent);

        Group pageGroupCommand = createGroupCommand(pageComponent);

        Group pageGroupMenu = createGroupMenu(pageComponent);

        createCommandControls(pageGroupCommand);

        createNameControls(pageGroupMenu);

        // TODO: to be enabled again, see https://github.com/anb0s/EasyShell/issues/61
        setHelpAvailable(false);

        refreshCommandCombo();

        return pageComponent;
    }

    public Composite createPageComponent(Composite parent) {
        Font font = parent.getFont();
        Composite pageComponent = new Composite(parent,SWT.NULL);
        GridLayout layoutComponent = new GridLayout();
        layoutComponent.numColumns = 1;
        layoutComponent.makeColumnsEqualWidth = false;
        layoutComponent.marginWidth = 5;
        layoutComponent.marginHeight = 4;
        pageComponent.setLayout(layoutComponent);
        GridData gridDataComponent = new GridData(GridData.FILL_HORIZONTAL);
        gridDataComponent.widthHint = 640;
        pageComponent.setLayoutData(gridDataComponent);
        pageComponent.setFont(font);
        return pageComponent;
    }

    public Group createGroupCommand(Composite parent) {
        Font font = parent.getFont();
        Group pageGroupCommand = new Group(parent, SWT.SHADOW_ETCHED_IN);
        pageGroupCommand.setText(Activator.getResourceString("easyshell.menu.editor.dialog.title.group.command"));
        pageGroupCommand.setToolTipText(Activator.getResourceString("easyshell.menu.editor.dialog.title.group.tooltip.command"));
        GridLayout layoutCommand = new GridLayout();
        layoutCommand.numColumns = 3;
        layoutCommand.makeColumnsEqualWidth = false;
        layoutCommand.marginWidth = 5;
        layoutCommand.marginHeight = 4;
        pageGroupCommand.setLayout(layoutCommand);
        GridData gridDataCommand = new GridData(GridData.FILL_HORIZONTAL);
        pageGroupCommand.setLayoutData(gridDataCommand);
        pageGroupCommand.setFont(font);
        return pageGroupCommand;
    }

    public Group createGroupMenu(Composite parent) {
        Font font = parent.getFont();
        Group pageGroupMenu = new Group(parent, SWT.SHADOW_ETCHED_IN);
        pageGroupMenu.setText(Activator.getResourceString("easyshell.menu.editor.dialog.title.group.menu"));
        pageGroupMenu.setToolTipText(Activator.getResourceString("easyshell.menu.editor.dialog.title.group.tooltip.menu"));
        GridLayout layoutMenu = new GridLayout();
        layoutMenu.numColumns = 3;
        layoutMenu.makeColumnsEqualWidth = false;
        layoutMenu.marginWidth = 5;
        layoutMenu.marginHeight = 4;
        pageGroupMenu.setLayout(layoutMenu);
        GridData gridDataMenu = new GridData(GridData.FILL_HORIZONTAL);
        pageGroupMenu.setLayoutData(gridDataMenu);
        pageGroupMenu.setFont(font);
        return pageGroupMenu;
    }

    public void createCommandControls(Composite parent) {
        // enable checkbox
        createEnabledCheckBox(parent);

        // search
        createSearchField(parent); createNewButton(parent);

        // create selected command combo
        createCommandCombo(parent); createEditButton(parent);

        // create input commandText field
        String commandStr;
        commandStr = menuData.getCommand();
        commandText = UtilsUI.createTextField(parent, Activator.getResourceString("easyshell.menu.editor.dialog.label.command"), Activator.getResourceString("easyshell.menu.editor.dialog.label.tooltip.command"), commandStr, false, false);

        //createLabel(parent, "");createLabel(parent, "");
        createCopyButton(parent);
        UtilsUI.createLabel(parent, "", null);
        UtilsUI.createLabel(parent, "", null);
        createRemoveButton(parent);
    }

    public void createNameControls(Composite parent) {
        // type combo
        createNameTypeCombo(parent);
        // create input nameText field
        namePatternText = UtilsUI.createTextField(parent, Activator.getResourceString("easyshell.menu.editor.dialog.label.pattern"), Activator.getResourceString("easyshell.menu.editor.dialog.label.tooltip.pattern"), menuData.getNamePattern(), true, true);
        namePatternTextAssist = addContentAssistExtended(namePatternText);
        namePatternText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                boolean isUserDefined = menuData.getNameType() == MenuNameType.menuNameTypeUser;
                if (isUserDefined) {
                    Text text = (Text)e.widget;
                    menuData.setNamePattern(text.getText());
                    menuNameText.setText(menuData.getNameExpanded());
                }
            }
        });
        // create output menuNameText field
        UtilsUI.createLabel(parent, Activator.getResourceString("easyshell.menu.editor.dialog.label.name"), Activator.getResourceString("easyshell.menu.editor.dialog.label.tooltip.name"));
        //categoryImageButton = UtilsUI.createImageButton(parent, Category.categoryDefault.getImageId());
        menuImageButton = UtilsUI.createImageButton(parent, menuData.getImageId(), Activator.getResourceString("easyshell.menu.editor.dialog.button.tooltip.image"));
        menuImageButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String newImageId = imageDialog();
                if (newImageId != null) {
                    menuData.setImageId(newImageId); // may reset to command / category
                    refreshMenuImageButton();
                }
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
            }
        });
        menuNameText = UtilsUI.createTextField(parent, null, null, menuData.getNameExpanded(), false, false);
    }

    private void refreshMenuImageButton() {
        menuImageButton.setImage(Activator.getImage(menuData.getImageId()));
    }

    private void createRemoveButton(Composite parent) {
        Font font = parent.getFont();
        removeButton = new Button(parent, SWT.PUSH);
        removeButton.setText(Activator.getResourceString("easyshell.menu.editor.dialog.button.text.remove"));
        removeButton.setToolTipText(Activator.getResourceString("easyshell.menu.editor.dialog.button.tooltip.remove"));
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                removeDialog();
            }
        });
        removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        removeButton.setFont(font);
        setButtonLayoutData(removeButton);
    }

    private void createEditButton(Composite parent) {
        Font font = parent.getFont();
        editButton = new Button(parent, SWT.PUSH);
        editButton.setText(Activator.getResourceString("easyshell.menu.editor.dialog.button.text.edit"));
        editButton.setToolTipText(Activator.getResourceString("easyshell.menu.editor.dialog.button.tooltip.edit"));
        editButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                editDialog();
            }
        });
        editButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        editButton.setFont(font);
        setButtonLayoutData(editButton);
    }

    private void createCopyButton(Composite parent) {
        Font font = parent.getFont();
        addCopyButton = new Button(parent, SWT.PUSH);
        addCopyButton.setText(Activator.getResourceString("easyshell.menu.editor.dialog.button.text.copy"));
        addCopyButton.setToolTipText(Activator.getResourceString("easyshell.menu.editor.dialog.button.tooltip.copy"));
        addCopyButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                addCopyDialog();
            }
        });
        addCopyButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        addCopyButton.setFont(font);
        setButtonLayoutData(addCopyButton);
    }

    private void createNewButton(Composite parent) {
        Font font = parent.getFont();
        addNewButton = new Button(parent, SWT.PUSH);
        addNewButton.setText(Activator.getResourceString("easyshell.menu.editor.dialog.button.text.new"));
        addNewButton.setToolTipText(Activator.getResourceString("easyshell.menu.editor.dialog.button.tooltip.new"));
        addNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                addNewDialog();
            }
        });
        addNewButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        addNewButton.setFont(font);
        setButtonLayoutData(addNewButton);
    }

    private ContentProposalAdapter addContentAssistExtended(Text textControl) {
        char[] autoActivationCharacters = new char[] { '$', '{' };
        Map<String, String> proposals = new LinkedHashMap<String, String>();
        // add internal variables
        proposals.putAll(Variable.getInternalVariableInfoMap());
        ContentAssistCommandAdapter adapter = new ContentAssistCommandAdapter(textControl, new CommandVariableContentAdapter(),
                new CommandVariableContentProposalProvider(proposals), null,
                autoActivationCharacters, true);
        adapter.setPropagateKeys(false);
        adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
        return adapter;
    }

    private void addDialog(CommandData data, boolean copy) {
        String title = Activator.getResourceString("easyshell.command.editor.dialog.title.new");
        if (copy) {
            title = Activator.getResourceString("easyshell.command.editor.dialog.title.copy");
        }
        CommandDataDialog dialog = new CommandDataDialog(getShell(), data, title);
        if (dialog.open() == Window.OK) {
            addCommand(data);
        }
    }

    private void addCommand(CommandData data) {
        CommandDataStore.instance().add(data);
        cmdList.add(data);
        commandComboViewer.setContent(cmdList);
        commandComboViewer.selectLastItem();
    }

    private void replaceCommand(CommandData data) {
        CommandDataStore.instance().replace(data);
        cmdList = CommandDataStore.instance().getDataList();
        commandComboViewer.setContent(cmdList);
        commandComboViewer.setSelection(data);
    }

    private void removeCommand(CommandData data) {
        if (data.getPresetType() == PresetType.presetUser) {
            CommandDataStore.instance().delete(data);
            cmdList = CommandDataStore.instance().getDataList();
            commandComboViewer.setContent(cmdList);
            commandComboViewer.selectLastItem();
        } else if (data.getPresetType() == PresetType.presetPluginModify) {
            data.removeModifyData();
            replaceCommand(data);
        }
    }

    private void addNewDialog() {
        CommandData data = new CommandData(null, PresetType.presetUser, Utils.getOS(), "MyNewCommand", ResourceType.resourceTypeFileOrDirectory, Category.categoryUser, CommandType.commandTypeExecute, "my_new_command");
        addDialog(data, false);
    }

    private void addCopyDialog() {
        CommandData data = new CommandData(commandComboViewer.getSelection(), true);
        data.setPresetType(PresetType.presetUser);
        addDialog(data, true);
    }

    private void editDialog() {
        CommandData dataSelected = commandComboViewer.getSelection();
        CommandData dataNew = new CommandData(dataSelected, false);
        dataNew.setPosition(dataSelected.getPosition());
        String title = MessageFormat.format(Activator.getResourceString("easyshell.command.editor.dialog.title.edit"), dataNew.getPresetType().getName());
        CommandDataDialog dialog = new CommandDataDialog(getShell(), dataNew, title);
        if (dialog.open() == Window.OK) {
            replaceCommand(dataNew);
        } else {
            dataNew = null;
        }
    }

    private void removeDialog() {
        // get the selected commands and referenced menus as lists
        List<CommandData> commands = new ArrayList<CommandData>();
        List<MenuData> menus = new ArrayList<MenuData>();
        // get the selected
        CommandData data = commandComboViewer.getSelection();
        commands.add(data);
        // get referenced menus and remove the the actual menus
        menus.addAll(MenuDataStore.instance().getRefencedBy(data.getId()));
        menus.remove(this.menuData);
        // ask user
        String commandNames = data.getCommandAsComboName();
        String title = null;
        String question = null;
        if (data.getPresetType() == PresetType.presetPluginModify) {
            title = Activator.getResourceString("easyshell.menu.editor.dialog.title.user.remove");
            question = MessageFormat.format(
                    Activator.getResourceString("easyshell.menu.editor.dialog.question.user.remove"),
                    commandNames);
        } else {
            title = Activator.getResourceString("easyshell.menu.editor.dialog.title.remove");
            question = MessageFormat.format(
                    Activator.getResourceString("easyshell.menu.editor.dialog.question.remove"),
                    commandNames);
        }
        int dialogImageType = MessageDialog.QUESTION;
        if (menus.size() > 0) {
            dialogImageType = MessageDialog.WARNING;
            String menuNames = "";
            for (MenuData menu : menus) {
                menuNames += menu.getNameExpanded() + "\n";
            }
            if (data.getPresetType() == PresetType.presetPluginModify) {
                title = Activator.getResourceString("easyshell.menu.editor.dialog.title.remove.user.menu");
                question = MessageFormat.format(Activator.getResourceString("easyshell.menu.editor.dialog.question.remove.user.menu"),
                        commandNames, menuNames);
            } else {
                title = Activator.getResourceString("easyshell.menu.editor.dialog.title.remove.menu");
                question = MessageFormat.format(Activator.getResourceString("easyshell.menu.editor.dialog.question.remove.menu"),
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
            for (MenuData menu : menus) {
                MenuDataStore.instance().delete(menu);
            }
            removeCommand(data);
        }
    }

    private String imageDialog() {
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), new ILabelProvider() {

            @Override
            public void removeListener(ILabelProviderListener listener) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean isLabelProperty(Object element, String property) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void dispose() {
                // TODO Auto-generated method stub

            }

            @Override
            public void addListener(ILabelProviderListener listener) {
                // TODO Auto-generated method stub

            }

            @Override
            public String getText(Object element) {
                String name = (String)element;
                if (name.equals(menuData.getCommandImageId())) {
                    name = name + " (" + Activator.getResourceString("easyshell.menu.editor.dialog.image.none") +")";
                }
                return name;
            }

            @Override
            public Image getImage(Object element) {
                String name = (String)element;
                return Activator.getImage(name);
            }
        }, new ITreeContentProvider() {

            @Override
            public boolean hasChildren(Object element) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Object getParent(Object element) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Object[] getElements(Object inputElement) {
                return ((List<?>)inputElement).toArray();
            }

            @Override
            public Object[] getChildren(Object parentElement) {
                // TODO Auto-generated method stub
                return null;
            }
        });
        dialog.setTitle(Activator.getResourceString("easyshell.menu.editor.dialog.image.title"));
        dialog.setMessage(Activator.getResourceString("easyshell.menu.editor.dialog.image.text"));
        dialog.setInput(Activator.getImageNames());
        int ret = dialog.open();
        if (ret == OK) {
            Object res[] = dialog.getResult();
            String imageNew = (String)res[0];
            if (imageNew != null && imageNew.length() > 0) {
                return imageNew;
            }
        }
        return null;
    }

    private void refreshCommandCombo() {
        try {
            commandComboViewer.setSelection(this.menuData.getCommandData());
        } catch (UnknownCommandID e) {
            e.logInternalError();
            commandComboViewer.selectFirstItem();
        }
    }

    private void refreshNameTypeCombo() {
        // send event to refresh
        Event event = new Event();
        event.item = null;
        nameTypeCombo.notifyListeners(SWT.Selection, event);
    }

    private boolean validateValues() {

        String title = Activator.getResourceString("easyshell.error.title.incompletedata");

        // check type
        CommandData data = commandComboViewer.getSelection();
        if (data == null) {
            MessageDialog.openError(getShell(), title, Activator.getResourceString("easyshell.menu.editor.dialog.error.type.text"));
            return false;
        }

        boolean valid = true;

        // check name
        String text  = Activator.getResourceString("easyshell.menu.editor.dialog.error.text.name");
        if ( (namePatternText.getText() == null) || (namePatternText.getText().length() <= 0)) {
            valid = false;
        }

        // show error message
        if (!valid) {
            MessageDialog.openError(getShell(), title, text);
        } else {
            List<MenuData> menus = MenuDataStore.instance().getRefencedBy(data.getId());
            menus.remove(this.menuData);
            if (menus.size() >0) {
                title = Activator.getResourceString("easyshell.menu.editor.dialog.title.duplicate");
                String commandNames = data.getCommandAsComboName();
                String menuNames = "";
                for (MenuData menu : menus) {
                    menuNames += menu.getNameExpanded() + "\n";
                }
                String question = MessageFormat.format(Activator.getResourceString("easyshell.menu.editor.dialog.question.duplicate"),
                        commandNames, menuNames);
                MessageDialog dialog = new MessageDialog(
                        null, title, null, question,
                        MessageDialog.WARNING,
                        new String[] {"Yes", "No"},
                        1); // no is the default
                int result = dialog.open();
                if (result != 0) {
                    valid = false;
                }
            }
        }

        return valid;
    }

    private void createEnabledCheckBox(Composite parent) {
        // draw label
        UtilsUI.createLabel(parent, Activator.getResourceString("easyshell.menu.editor.dialog.label.active"), Activator.getResourceString("easyshell.menu.editor.dialog.label.tooltip.active"));
        // draw checkbox
        enabledCheckBox = new Button(parent,SWT.CHECK);
        enabledCheckBox.setSelection(this.menuData.isEnabled());
        UtilsUI.createLabel(parent, "", null);
    }

    private String[] getAllNameTypesAsComboNames() {
        return MenuNameType.getNamesAsArray();
    }

    private MenuNameType[] getAllNameTypes() {
        return MenuNameType.getAsArray();
    }

    private CommandData getFirstSelected() {
        CommandData retData = null;
        for (Object object : (Object[])commandComboViewer.getViewer().getInput()) {
            CommandData data = (CommandData)object;
            if (data.isSelected()) {
                retData = data;
                break;
            }
        }
        return retData;
    }

    private void createSearchField(Composite parent) {
        UtilsUI.createLabel(parent, Activator.getResourceString("easyshell.menu.editor.dialog.label.text.filter"), Activator.getResourceString("easyshell.menu.editor.dialog.label.tooltip.filter"));
        filter = new CommandDataFilter();
        searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
        searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        searchText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
              filter.setSearchText(searchText.getText());
              commandComboViewer.getViewer().refresh();
              commandComboViewer.getViewer().getCombo().select(0);
              commandComboViewer.setSelection(getFirstSelected());
            }
        });
        searchText.setToolTipText(Activator.getResourceString("easyshell.command.page.text.tooltip.search"));
    }

    private void createCommandCombo(Composite parent) {
        // draw label
        UtilsUI.createLabel(parent, Activator.getResourceString("easyshell.menu.editor.dialog.label.combo.preset"), Activator.getResourceString("easyshell.menu.editor.dialog.combo.tooltip.preset"));

        commandComboViewer = new TypedComboBox<CommandData>(parent);

        commandComboViewer.addSelectionListener(new TypedComboBoxSelectionListener<CommandData>() {

            @Override
            public void selectionChanged(TypedComboBox<CommandData> typedComboBox, CommandData newSelection) {
                menuData.setCommandId(newSelection.getId());
                if (menuData.getNameType() != MenuNameType.menuNameTypeUser) {
                    menuData.setNameTypeFromCategory(newSelection.getCategory());
                }
                String commandStr = null;
                PresetType presetType = PresetType.presetPlugin;
                try {
                    commandStr = menuData.getCommand();
                    presetType = menuData.getCommandData().getPresetType();
                } catch (UnknownCommandID e) {
                    e.logInternalError();
                }
                if (firstRefresh) {
                    firstRefresh = false;
                } else {
                    menuData.setImageId(null); // reset to command / category
                }
                commandText.setText(commandStr);
                boolean presetSelected =  presetType == PresetType.presetPlugin;
                removeButton.setEnabled(!presetSelected);
                // updates & refreshes
                updateTypeComboSelection();
                refreshNameTypeCombo();
                refreshMenuImageButton();
            }

        });

        commandComboViewer.setLabelProvider(new TypedComboBoxLabelProvider<CommandData>() {

            @Override
            public String getSelectedLabel(CommandData element) {
                return element.getCommandAsComboName();
            }

            @Override
            public String getListLabel(CommandData element) {
                return element.getCommandAsComboName();
            }

            @Override
            public Image getImage(CommandData element) {
                return Activator.getImage(element.getImageId());
            }

        });

        if (filter != null) {
            commandComboViewer.getViewer().addFilter(filter);
        }

        /*Combo combo = commandComboViewer.getViewer().getCombo();
        combo.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (refreshing) {
                    refreshing = false;
                    return;
                }
                Combo combo = commandComboViewer.getViewer().getCombo();
                if (combo != null && filter != null) {
                    String text = combo.getText();
                    CommandData data = commandComboViewer.getSelection();
                    if (text != null && data != null && !data.getCommandAsComboName().equals(text)) {
                        filter.setSearchText(text);
                        refreshing = true;
                        //commandComboViewer.getViewer().refresh();
                    }
                }
            }
        });*/

        commandComboViewer.getViewer().getCombo().setToolTipText(Activator.getResourceString("easyshell.menu.editor.dialog.combo.tooltip.preset"));
        commandComboViewer.setContent(cmdList);
    }

    private void createNameTypeCombo(Composite parent) {
        UtilsUI.createLabel(parent, Activator.getResourceString("easyshell.menu.editor.dialog.label.combo.pattern"), Activator.getResourceString("easyshell.menu.editor.dialog.combo.tooltip.pattern"));
        UtilsUI.createLabel(parent, "", null);
        // draw combo
        nameTypeCombo = new Combo(parent,SWT.BORDER | SWT.READ_ONLY);
        nameTypeCombo.setToolTipText(Activator.getResourceString("easyshell.menu.editor.dialog.combo.tooltip.pattern"));
        nameTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        //commandCombo.setEditable(false);
        nameTypeCombo.setItems(getAllNameTypesAsComboNames());
        nameTypeCombo.select(0);
        nameTypeCombo.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int index = nameTypeCombo.getSelectionIndex();
                menuData.setNameType(MenuNameType.getAsArray()[index]);
                namePatternText.setText(menuData.getNamePattern());
                menuNameText.setText(menuData.getNameExpanded());
                boolean isUserDefined = menuData.getNameType() == MenuNameType.menuNameTypeUser;
                namePatternText.setEditable(isUserDefined);
                namePatternTextAssist.setEnabled(isUserDefined);
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
            }
        });
        updateTypeComboSelection();
    }

    private void updateTypeComboSelection() {
        for(int i = 0 ; i < MenuNameType.values().length ; i++) {
            if (MenuNameType.values()[i].equals(this.menuData.getNameType())) {
                nameTypeCombo.select(MenuNameType.values()[i].getId());
                return;
            }
        }
    }

}
