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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.exceptions.UnknownCommandID;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
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
    private Text    menuNameText;
    private Text    commandText;

    private Button addNewButton;
    private Button addCopyButton;
    private Button editButton;
    private Button removeButton;

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
        Font font = parent.getFont();
    	Composite pageComponent = new Composite(parent,SWT.NULL);
        GridLayout layout0 = new GridLayout();
        layout0.numColumns = 1;
        layout0.makeColumnsEqualWidth = false;
        layout0.marginWidth = 5;
        layout0.marginHeight = 4;
        pageComponent.setLayout(layout0);
        GridData gridData0 = new GridData(GridData.FILL_HORIZONTAL);
        gridData0.widthHint = 640;
        pageComponent.setLayoutData(gridData0);
        pageComponent.setFont(font);
    	// define group1
    	Group pageGroup1 = new Group(pageComponent, SWT.SHADOW_ETCHED_IN);
    	pageGroup1.setText(Activator.getResourceString("easyshell.menu.editor.dialog.title.group1"));
        GridLayout layout1 = new GridLayout();
        layout1.numColumns = 2;
        layout1.makeColumnsEqualWidth = false;
        layout1.marginWidth = 5;
        layout1.marginHeight = 4;
        pageGroup1.setLayout(layout1);
        GridData gridData1 = new GridData(GridData.FILL_HORIZONTAL);
        pageGroup1.setLayoutData(gridData1);
        pageGroup1.setFont(font);
        // create activity checkbox
        createEnabledCheckBox(pageGroup1);
        // type combo
        createNameTypeCombo(pageGroup1);
        // create input nameText field
        namePatternText = createTextField(pageGroup1, Activator.getResourceString("easyshell.menu.editor.dialog.label.pattern"), menuData.getNamePattern(), true);
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
        menuNameText = createTextField(pageGroup1, Activator.getResourceString("easyshell.menu.editor.dialog.label.name"), menuData.getNameExpanded(), false);

        // define group2
        Group pageGroup2 = new Group(pageComponent, SWT.SHADOW_ETCHED_IN);
        pageGroup2.setText(Activator.getResourceString("easyshell.menu.editor.dialog.title.group2"));
        GridLayout layout2 = new GridLayout();
        layout2.numColumns = 3;
        layout2.makeColumnsEqualWidth = false;
        layout2.marginWidth = 5;
        layout2.marginHeight = 4;
        pageGroup2.setLayout(layout2);
        GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
        pageGroup2.setLayoutData(gridData2);
        pageGroup2.setFont(font);

        // search
        createSearchField(pageGroup2);

        // create selected command combo
        createCommandCombo(pageGroup2); createNewButton(font, pageGroup2, gridData2);

        // create input commandText field
        String commandStr;
		try {
			commandStr = menuData.getCommandData().getCommand();
		} catch (UnknownCommandID e1) {
			e1.logInternalError();
			commandStr = "Unknown ID: " + e1.getID();
		}
        commandText = createTextField(pageGroup2, Activator.getResourceString("easyshell.menu.editor.dialog.label.command"), commandStr, false);
        createEditButton(font, pageGroup2, gridData2);
        createLabel(pageGroup2, "");createLabel(pageGroup2, "");
        createCopyButton(font, pageGroup2, gridData2);
        createLabel(pageGroup2, "");createLabel(pageGroup2, "");
        createRemoveButton(font, pageGroup2, gridData2);

        // TODO: to be enabled again, see https://github.com/anb0s/EasyShell/issues/61
        setHelpAvailable(false);

        refreshCommandCombo();

        return pageComponent;
    }

    private void createLabel(Composite parent, String name) {
        Label label = new Label(parent, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        label.setText(name);
    }

    private void createRemoveButton(Font font, Group pageGroup1, GridData gridData1) {
        removeButton = new Button(pageGroup1, SWT.PUSH);
        removeButton.setText(Activator.getResourceString("easyshell.menu.editor.dialog.button.text.remove"));
        removeButton.setToolTipText(Activator.getResourceString("easyshell.menu.editor.dialog.button.tooltip.remove"));
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                removeDialog();
            }
        });
        removeButton.setLayoutData(gridData1);
        removeButton.setFont(font);
        setButtonLayoutData(removeButton);
    }

    private void createEditButton(Font font, Group pageGroup1, GridData gridData1) {
        editButton = new Button(pageGroup1, SWT.PUSH);
        editButton.setText(Activator.getResourceString("easyshell.menu.editor.dialog.button.text.edit"));
        editButton.setToolTipText(Activator.getResourceString("easyshell.menu.editor.dialog.button.tooltip.edit"));
        editButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                editDialog();
            }
        });
        editButton.setLayoutData(gridData1);
        editButton.setFont(font);
        setButtonLayoutData(editButton);
    }

    private void createCopyButton(Font font, Group pageGroup1, GridData gridData1) {
        addCopyButton = new Button(pageGroup1, SWT.PUSH);
        addCopyButton.setText(Activator.getResourceString("easyshell.menu.editor.dialog.button.text.copy"));
        addCopyButton.setToolTipText(Activator.getResourceString("easyshell.menu.editor.dialog.button.tooltip.copy"));
        addCopyButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                addCopyDialog();
            }
        });
        addCopyButton.setLayoutData(gridData1);
        addCopyButton.setFont(font);
        setButtonLayoutData(addCopyButton);
    }

    private void createNewButton(Font font, Group pageGroup1, GridData gridData1) {
        addNewButton = new Button(pageGroup1, SWT.PUSH);
        addNewButton.setText(Activator.getResourceString("easyshell.menu.editor.dialog.button.text.new"));
        addNewButton.setToolTipText(Activator.getResourceString("easyshell.menu.editor.dialog.button.tooltip.new"));
        addNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                addNewDialog();
            }
        });
        addNewButton.setLayoutData(gridData1);
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

    	String title = Activator.getResourceString("easyshell.menu.editor.dialog.error.incompletedata.title");

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
        createLabel(parent, Activator.getResourceString("easyshell.menu.editor.dialog.label.active"));
        // draw checkbox
        enabledCheckBox = new Button(parent,SWT.CHECK);
        enabledCheckBox.setSelection(this.menuData.isEnabled());
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
    	createLabel(parent, Activator.getResourceString("easyshell.menu.editor.dialog.label.text.filter"));
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
        // fake
        Label label = new Label(parent, SWT.NONE);
        label.setText("");
    }

    private void createCommandCombo(Composite parent) {
        // draw label
        createLabel(parent, Activator.getResourceString("easyshell.menu.editor.dialog.label.combo.preset"));
        
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
					commandStr = menuData.getCommandData().getCommand();
					presetType = menuData.getCommandData().getPresetType();
				} catch (UnknownCommandID e) {
					e.logInternalError();
					commandStr = "Unknown ID: " + e.getID();
				}
				commandText.setText(commandStr);
				boolean presetSelected =  presetType == PresetType.presetPlugin;
				removeButton.setEnabled(!presetSelected);
				// updates & refreshes
				updateTypeComboSelection();
				refreshNameTypeCombo();
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
                return element.getCategoryImage();
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
        
        commandComboViewer.setContent(cmdList);                
    }

    private void createNameTypeCombo(Composite parent) {
        // draw label
        Label comboLabel = new Label(parent,SWT.LEFT);
        comboLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        comboLabel.setText(Activator.getResourceString("easyshell.menu.editor.dialog.label.combo.pattern")); //$NON-NLS-1$
        // draw combo
        nameTypeCombo = new Combo(parent,SWT.BORDER | SWT.READ_ONLY);
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

    private Text createTextField(Composite parent, String labelText, String editValue, boolean editable) {
        // draw label
        if (labelText != null) {
            Label label = new Label(parent,SWT.LEFT);
            label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
            label.setText(labelText);    //$NON-NLS-1$
        }
        // draw textfield
        Text text = new Text(parent,SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.setText(editValue);
        text.setEditable(editable);
        return text;
    }

}
