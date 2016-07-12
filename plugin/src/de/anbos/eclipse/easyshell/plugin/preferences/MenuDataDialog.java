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
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.anbos.eclipse.easyshell.plugin.Activator;

public class MenuDataDialog extends StatusDialog {

    private MenuData data;
    private CommandDataStore cmdStore;
    private List<CommandData> cmdList;

    private Button  enabledCheckBox;
    private Text    nameText;
    private Text    commandText;
    private CCombo  commandCombo;

    private Button addNewButton;
    private Button addCopyButton;
    private Button editButton;
    private Button removeButton;

    public MenuDataDialog(Shell parent, MenuData data, CommandDataStore cmdStore, List<CommandData> cmdList, boolean edit) {
        super(parent);
        this.data = data;
        this.cmdStore = cmdStore;
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
        layout1.numColumns = 3;
        layout1.makeColumnsEqualWidth = false;
        layout1.marginWidth = 5;
        layout1.marginHeight = 4;
        pageGroup1.setLayout(layout1);
        GridData gridData1 = new GridData(GridData.FILL_HORIZONTAL);
        pageGroup1.setLayoutData(gridData1);
        pageGroup1.setFont(font);
        // create activity checkbox
        createEnabledCheckBox(pageGroup1);createNewButton(font, pageGroup1, gridData1);
        // create selected command combo
        createCommandCombo(pageGroup1);createCopyButton(font, pageGroup1, gridData1);
        //create input commandText field
        commandText = createTextField(pageGroup1, Activator.getResourceString("easyshell.menu.editor.dialog.label.command"), data.getCommandData().getCommand(), false);
        // buttons
        createEditButton(font, pageGroup1, gridData1);
        //create input nameText field
        nameText = createTextField(pageGroup1, Activator.getResourceString("easyshell.menu.editor.dialog.label.name"), data.getName(), true);
        createRemoveButton(font, pageGroup1, gridData1);

        refreshCommandCombo();

        return pageComponent;
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

    protected void okPressed() {
        if (!validateValues()) {
            return;
        }
        data.setEnabled(enabledCheckBox.getSelection());
        data.setCommandData(cmdList.get(commandCombo.getSelectionIndex()), false);
        data.setName(nameText.getText());
        super.okPressed();
    }

    private void addDialog(CommandData data) {
        CommandDataDialog dialog = new CommandDataDialog(getShell(), data, false);
        if (dialog.open() == Window.OK) {
            addCommand(data);
            refreshCommandCombo();
        }
    }

    private void addCommand(CommandData data) {
        cmdStore.add(data);
        cmdStore.save();
        cmdList.add(data);
        String[] names = getAllCommandsAsComboNames(cmdList);
        commandCombo.setItems(names);
        commandCombo.select(names.length-1);
    }

    private void removeCommand(int index, CommandData data) {
        cmdStore.delete(data);
        cmdStore.save();
        //cmdList.remove(data); // no need to search for data, use index instead:
        cmdList.remove(index);
        String[] names = getAllCommandsAsComboNames(cmdList);
        commandCombo.setItems(names);
        commandCombo.select(names.length-1);
    }

    private void addNewDialog() {
        CommandData data = new CommandData(PresetType.presetUser, Utils.getOS(), "MyNewCommand", RessourceType.ressourceFileOrFolder, CommandType.commandTypeOther, "my_new_command");
        addDialog(data);
    }

    private void addCopyDialog() {
        int index = commandCombo.getSelectionIndex();
        CommandData data = new CommandData(cmdList.get(index));
        data.setPresetType(PresetType.presetUser);
        addDialog(data);
    }

    private void editDialog() {
        int index = commandCombo.getSelectionIndex();
        CommandData data = cmdList.get(index);
        CommandDataDialog dialog = new CommandDataDialog(getShell(), data, true);
        if (dialog.open() == Window.OK) {
            replaceCommand(index, data);
            refreshCommandCombo();
        }
    }

    private void removeDialog() {
        int index = commandCombo.getSelectionIndex();
        String title = Activator.getResourceString("easyshell.menu.editor.dialog.title.remove");
        String question = MessageFormat.format(
                Activator.getResourceString("easyshell.menu.editor.dialog.question.remove"),
                commandCombo.getItem(index));
        MessageDialog dialog = new MessageDialog(
                null, title, null, question,
                MessageDialog.QUESTION,
                new String[] {"Yes", "No"},
                1); // no is the default
        int result = dialog.open();
        if (result == 0) {
            CommandData data = cmdList.get(index);
            removeCommand(index, data);
            refreshCommandCombo();
        }
    }

    private void replaceCommand(int index, CommandData data) {
        commandCombo.setItem(index, getCommandAsComboName(data));
    }

    private void refreshCommandCombo() {
        // send event to refresh
        Event event = new Event();
        event.item = null;
        commandCombo.notifyListeners(SWT.Selection, event);
    }

    private boolean validateValues() {

    	final String title = Activator.getResourceString("easyshell.menu.editor.dialog.error.incompletedata.title");

    	// check type
        if ( (commandCombo.getText() == null) || (commandCombo.getText().length() <= 0)) {
        	MessageDialog.openError(getShell(), title, Activator.getResourceString("easyshell.menu.editor.dialog.error.type.text"));
        	return false;
        }

    	boolean valid = true;

        // check name
        String text  = Activator.getResourceString("easyshell.menu.editor.dialog.error.text.name");
        if ( (nameText.getText() == null) || (nameText.getText().length() <= 0)) {
            valid = false;
        }

        // show error message
        if (!valid) {
            MessageDialog.openError(getShell(), title, text);
        }
        return valid;
    }

    private void createEnabledCheckBox(Composite parent) {
        // draw label
        Label comboLabel = new Label(parent,SWT.LEFT);
        comboLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        comboLabel.setText(Activator.getResourceString("easyshell.menu.editor.dialog.label.active")); //$NON-NLS-1$
        // draw checkbox
        enabledCheckBox = new Button(parent,SWT.CHECK);
        enabledCheckBox.setSelection(this.data.isEnabled());
    }

    private String getCommandAsComboName(CommandData data) {
        return data.getPresetType().getName() + " - " + data.getOS().getName() + " - " + data.getCommandType().getName() + " - " + data.getName();
    }

    private String[] getAllCommandsAsComboNames(List<CommandData> list) {
        String[] arr = new String[list.size()];
        for (int i=0;i<list.size();i++) {
            arr[i] = getCommandAsComboName(list.get(i));
        }
        return arr;
    }

    private void createCommandCombo(Composite parent) {
        // draw label
        Label comboLabel = new Label(parent,SWT.LEFT);
        comboLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        comboLabel.setText(Activator.getResourceString("easyshell.menu.editor.dialog.label.combo")); //$NON-NLS-1$
        // draw combo
        commandCombo = new CCombo(parent,SWT.BORDER);
        commandCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        commandCombo.setEditable(false);
        commandCombo.setItems(getAllCommandsAsComboNames(cmdList));
        commandCombo.select(0);
        commandCombo.addSelectionListener(new SelectionListener() {
            @Override
			public void widgetSelected(SelectionEvent e) {
                int index = commandCombo.getSelectionIndex();
				//String text = commandCombo.getItem(index);
				data.setCommandData(cmdList.get(index), true);
				nameText.setText(data.getName());
				commandText.setText(data.getCommandData().getCommand());
				boolean isUserDefined = data.getCommandData().getPresetType() == PresetType.presetUser;
				editButton.setEnabled(isUserDefined);
				removeButton.setEnabled(isUserDefined);
			}
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
            }
		});
        for(int i = 0 ; i < cmdList.size() ; i++) {
            if (cmdList.get(i).equals(this.data.getCommandData())) {
                commandCombo.select(i);
                return;
            }
        }
    }

    private Text createTextField(Composite parent, String labelText, String editValue, boolean editable) {
        // draw label
        Label label = new Label(parent,SWT.LEFT);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        label.setText(labelText);    //$NON-NLS-1$
        // draw textfield
        Text text = new Text(parent,SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.setText(editValue);
        text.setEditable(editable);
        return text;
    }

}
