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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Utils;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.Variable;

public class CommandDataDialog extends StatusDialog {

    private CommandData data;
    private boolean edit;
    private Combo   resourceTypeCombo;
    private Label   categoryImage;
    private Combo   categoryCombo;
    private Combo   commandTypeCombo;
    private Text    nameText;
    private Button  dirCheckBox;
    private Text    dirText;
    private Text    valueText;

    @Override
    public void create() {
        super.create();
        getButton(IDialogConstants.OK_ID).setEnabled(edit);
    }

    public CommandDataDialog(Shell parent, CommandData data, String title, boolean edit) {
        super(parent);
        this.data = data;
        this.edit = edit;
        // do layout and title
        setShellStyle(getShellStyle() | SWT.MAX);
        // set title
        setTitle(title);
    }

    @Override
    protected boolean isResizable() {
      return true;
    }

    public Control createDialogArea(Composite parent) {
    	Composite pageComponent = new Composite(parent,SWT.NULL);
        GridLayout layout0 = new GridLayout();
        layout0.numColumns = 1;
        layout0.makeColumnsEqualWidth = false;
        layout0.marginWidth = 5;
        layout0.marginHeight = 4;
        pageComponent.setLayout(layout0);
        GridData data0 = new GridData(GridData.FILL_HORIZONTAL);
        data0.widthHint = 640;
        pageComponent.setLayoutData(data0);
        pageComponent.setFont(parent.getFont());
    	// define group1
    	Group pageGroup1 = new Group(pageComponent, SWT.SHADOW_ETCHED_IN);
    	pageGroup1.setText(Activator.getResourceString("easyshell.command.editor.dialog.title.group1"));
    	pageGroup1.setToolTipText(Activator.getResourceString("easyshell.command.editor.dialog.tooltip.group1"));
        GridLayout layout1 = new GridLayout();
        layout1.numColumns = 3;
        layout1.makeColumnsEqualWidth = false;
        layout1.marginWidth = 5;
        layout1.marginHeight = 4;
        pageGroup1.setLayout(layout1);
        GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
        pageGroup1.setLayoutData(data1);
        pageGroup1.setFont(parent.getFont());
        // create resource type combo
        createResourceTypeCombo(pageGroup1);
        // create category combo
        createCategoryCombo(pageGroup1);
        // create command type combo
        createCommandTypeCombo(pageGroup1);
        //create input nameText field
        nameText = createTextField(pageGroup1, Activator.getResourceString("easyshell.command.editor.dialog.label.name"), data.getName(), true);
        // create directory checkbox
        createDirCheckBox(pageGroup1);
        dirText = createTextField(pageGroup1, null, data.getWorkingDirectory(), false);
        // create input valueText field
        valueText = createTextField(pageGroup1, Activator.getResourceString("easyshell.command.editor.dialog.label.value"), data.getCommand(), true);

        // ------------------------------------ Description ------------------------------------------
        // define group2
        Group pageGroup2 = new Group(pageComponent, SWT.SHADOW_ETCHED_IN);
        pageGroup2.setText(Activator.getResourceString("easyshell.command.editor.dialog.title.group2"));
        pageGroup2.setToolTipText(Activator.getResourceString("easyshell.command.editor.dialog.tooltip.group2"));
        GridLayout layout2 = new GridLayout();
        layout2.numColumns = 2;
        layout2.makeColumnsEqualWidth = false;
        layout2.marginWidth = 5;
        layout2.marginHeight = 4;
        pageGroup2.setLayout(layout2);
        GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
        pageGroup2.setLayoutData(data2);
        pageGroup2.setFont(parent.getFont());

        // create variable labels
        for(int i=1;i<Variable.values().length;i++) {
            createVariableLabel(pageGroup2, Variable.values()[i].getFullVariableName(), Variable.values()[i].getDescription());
        }

        // TODO: to be enabled again, see https://github.com/anb0s/EasyShell/issues/61
        setHelpAvailable(false);

        refreshResourceTypeCombo();

        refreshDirCheckBox();

        refreshCategoryCombo();

        refreshCommandTypeCombo();

        return pageComponent;
    }

    private void createLabel(Composite parent, String name) {
        Label label = new Label(parent, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        label.setText(name);
    }

    private Label createImageLabel(Composite parent, String image) {
        Label label = new Label(parent, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        label.setImage(new Image(null, Activator.getImageDescriptor(image).getImageData()));
        return label;
    }

    private void createDirCheckBox(Composite parent) {
        // draw label
        createLabel(parent, Activator.getResourceString("easyshell.command.editor.dialog.label.useworkdir"));
        // draw checkbox
        dirCheckBox = new Button(parent,SWT.CHECK);
        dirCheckBox.setSelection(this.data.isUseWorkingDirectory());
        dirCheckBox.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                //Button button = (Button)e.getSource();
                dirText.setEditable(dirCheckBox.getSelection());
                if (!dirText.getEnabled() && dirText.getText().isEmpty()) {
                    dirText.setText(data.getWorkingDirectory());
                }
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });
        dirCheckBox.setToolTipText(Activator.getResourceString("easyshell.command.editor.dialog.button.tooltip.useworkdir"));
        dirCheckBox.setEnabled(edit);
    }

    private void createVariableLabel(Composite parent, String varText, String labelText) {
        StyledText styledTextWidget = new StyledText(parent, SWT.NONE);
        styledTextWidget.setText(varText);
        styledTextWidget.setBackground(parent.getBackground());
        styledTextWidget.setEditable(false);
        styledTextWidget.setCaret(null); //Set caret null this will hide caret
        styledTextWidget.addMouseListener(new MouseListener() {
            @Override
            public void mouseUp(MouseEvent e) {
                // TODO Auto-generated method stub

            }
            @Override
            public void mouseDown(MouseEvent e) {
                // TODO Auto-generated method stub

            }
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                StyledText text = (StyledText)(e.getSource());
                Control control = text;
                text.selectAll();
                String title = Activator.getResourceString("easyshell.message.copytoclipboard");
                String message = text.getText();
                Utils.copyToClipboard(message);
                Utils.showToolTipInfo(control, title, message);
                text.setSelection(0, 0);
            }
        });
        Label label = new Label(parent, 0);
        label.setText(labelText);
    }

    private void refreshResourceTypeCombo() {
        // send event to refresh
        Event event = new Event();
        event.item = null;
        resourceTypeCombo.notifyListeners(SWT.Selection, event);
    }

    private void refreshCategoryCombo() {
        // send event to refresh
        Event event = new Event();
        event.item = null;
        categoryCombo.notifyListeners(SWT.Selection, event);
    }

    private void refreshCommandTypeCombo() {
        // send event to refresh
        Event event = new Event();
        event.item = null;
        commandTypeCombo.notifyListeners(SWT.Selection, event);
    }

    private void refreshDirCheckBox() {
        // send event to refresh
        Event event = new Event();
        event.item = null;
        dirCheckBox.notifyListeners(SWT.Selection, event);
    }

    protected void okPressed() {
        if (edit) {
            if (!validateValues()) {
                return;
            }
            data.setName(nameText.getText());
            data.setResourceType(ResourceType.getFromName(resourceTypeCombo.getText()));
            data.setUseWorkingDirectory(dirCheckBox.getSelection());
            data.setWorkingDirectory(dirText.getText());
            data.setCategory(Category.getFromName(categoryCombo.getText()));
            data.setCommandType(CommandType.getFromName(commandTypeCombo.getText()));
            data.setCommand(valueText.getText());
        }
        super.okPressed();
    }

    private boolean validateValues() {

    	final String title = Activator.getResourceString("easyshell.command.editor.dialog.error.title.incompletedata");

        // check resource
        if ( (resourceTypeCombo.getText() == null) || (resourceTypeCombo.getText().length() <= 0)) {
            MessageDialog.openError(getShell(), title, Activator.getResourceString("easyshell.command.editor.dialog.error.text.resource"));
            return false;
        }

        // check command type
        if ( (categoryCombo.getText() == null) || (categoryCombo.getText().length() <= 0)) {
            MessageDialog.openError(getShell(), title, Activator.getResourceString("easyshell.command.editor.dialog.error.text.category"));
            return false;
        }

    	// check command type
        if ( (commandTypeCombo.getText() == null) || (commandTypeCombo.getText().length() <= 0)) {
        	MessageDialog.openError(getShell(), title, Activator.getResourceString("easyshell.command.editor.dialog.error.text.type"));
        	return false;
        }

    	boolean valid = true;

        // check name
        String text  = Activator.getResourceString("easyshell.command.editor.dialog.error.text.name");
        if ( (nameText.getText() == null) || (nameText.getText().length() <= 0)) {
            valid = false;
        }

        // check working dir
        if (valid) {
            text  = Activator.getResourceString("easyshell.command.editor.dialog.error.text.workingdir");
            if ( (dirText.getText() == null) || (dirText.getText().length() <= 0)) {
                valid = false;
            }
        }

        // check value
        if (valid) {
        	text  = Activator.getResourceString("easyshell.command.editor.dialog.error.text.value");
            if ( (valueText.getText() == null) || (valueText.getText().length() <= 0)) {
            	valid = false;
            }
        }

        // show error message
        if (!valid) {
            MessageDialog.openError(getShell(), title, text);
        }
        return valid;
    }

    private void createResourceTypeCombo(Composite parent) {
        // draw label
        createLabel(parent, Activator.getResourceString("easyshell.command.editor.dialog.label.combo.resource"));
        createLabel(parent, "");
        // draw combo
        resourceTypeCombo = new Combo(parent,SWT.BORDER | SWT.READ_ONLY);
        resourceTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        //resourceTypeCombo.setEditable(false);
        resourceTypeCombo.setItems(ResourceType.getNamesAsArray());
        resourceTypeCombo.select(0);
        resourceTypeCombo.addSelectionListener(new SelectionListener() {
            @Override
			public void widgetSelected(SelectionEvent e) {
				//String text = resourceTypeCombo.getItem(resourceTypeCombo.getSelectionIndex());
			}
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
            }
		});
        String[] items = resourceTypeCombo.getItems();
        for(int i = 0 ; i < items.length ; i++) {
            if(items[i].equals(this.data.getResourceType().getName())) {
                resourceTypeCombo.select(i);
                return;
            }
        }
        resourceTypeCombo.setEnabled(edit);
    }

    private void createCategoryCombo(Composite parent) {
        // draw label
        createLabel(parent, Activator.getResourceString("easyshell.command.editor.dialog.label.combo.category"));
        categoryImage = createImageLabel(parent, Category.categoryDefault.getIcon());
        // draw combo
        categoryCombo = new Combo(parent,SWT.BORDER | SWT.READ_ONLY);
        categoryCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        categoryCombo.setItems(Category.getNamesAsArray());
        categoryCombo.select(0);
        categoryCombo.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String text = categoryCombo.getItem(categoryCombo.getSelectionIndex());
                categoryImage.setImage(new Image(null, Activator.getImageDescriptor(Category.getFromName(text).getIcon()).getImageData()));
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
            }
        });
        String[] items = categoryCombo.getItems();
        for(int i = 0 ; i < items.length ; i++) {
            if(items[i].equals(this.data.getCategory().getName())) {
                categoryCombo.select(i);
                return;
            }
        }
        categoryCombo.setEnabled(edit);
    }

    private void createCommandTypeCombo(Composite parent) {
        // draw label
        createLabel(parent, Activator.getResourceString("easyshell.command.editor.dialog.label.combo.type"));
        createLabel(parent, "");
        // draw combo
        commandTypeCombo = new Combo(parent,SWT.BORDER | SWT.READ_ONLY);
        commandTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        //commandTypeCombo.setEditable(false);
        commandTypeCombo.setItems(CommandType.getNamesAsArray());
        commandTypeCombo.select(0);
        commandTypeCombo.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                //String text = typeCombo.getItem(typeCombo.getSelectionIndex());
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
            }
        });
        String[] items = commandTypeCombo.getItems();
        for(int i = 0 ; i < items.length ; i++) {
            if(items[i].equals(this.data.getCommandType().getName())) {
                commandTypeCombo.select(i);
                return;
            }
        }
        commandTypeCombo.setEnabled(edit);
    }

    private Text createTextField(Composite parent, String labelText, String editValue, boolean emptyLabel) {
        // draw label
        if (labelText != null) {
            createLabel(parent, labelText);
        }
        if (emptyLabel) {
            createLabel(parent, "");
        }
        // draw textfield
        Text text = new Text(parent,SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.setText(editValue);
        text.setEditable(edit);
        return text;
    }

}
