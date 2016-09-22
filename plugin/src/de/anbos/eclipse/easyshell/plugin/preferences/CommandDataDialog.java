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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
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
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Utils;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.Converter;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.Variable;

public class CommandDataDialog extends StatusDialog {

    // experimental features
    boolean useExtendedContentAssists = true;
    boolean showVariablesInfo = false;
    boolean showConvertersInfo = false;
    // TODO: to be enabled again, see https://github.com/anb0s/EasyShell/issues/61
    boolean showHelpButton = false;

    private CommandData data;
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
        getButton(IDialogConstants.OK_ID).setEnabled(true);
    }

    public CommandDataDialog(Shell parent, CommandData data, String title) {
        super(parent);
        this.data = data;
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

    	createCommandGroup(pageComponent);

    	if (showVariablesInfo) {
    	    createVariablesOverview(pageComponent);
    	}

    	if (showConvertersInfo) {
    	    createConvertersOverview(pageComponent);
    	}

        setHelpAvailable(showHelpButton);

        refreshResourceTypeCombo();

        refreshDirCheckBox();

        refreshCategoryCombo();

        refreshCommandTypeCombo();

        return pageComponent;
    }

    private void createCommandGroup(Composite pageComponent) {
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
        pageGroup1.setFont(pageComponent.getFont());

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

        // add content assist to command text editor field
        if (useExtendedContentAssists) {
            addContentAssistExtended(valueText);
        } else {
            addContentAssistSimple(valueText);
        }
    }

    private void addContentAssistSimple(Text textControl) {
        char[] autoActivationCharacters = new char[] { '$', '{' };
        KeyStroke keyStroke = null;
        try {
            keyStroke = KeyStroke.getInstance("Ctrl+Space");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // assume that myTextControl has already been created in some way
        List<Variable> variables = Variable.getVisibleVariables();
        String[] proposals = new String [variables.size()];
        for(int i=0;i<variables.size();i++) {
            proposals[i] = variables.get(i).getFullVariableName();
        }
        ContentProposalAdapter adapter = new ContentProposalAdapter(
                textControl , new TextContentAdapter(),
            new SimpleContentProposalProvider(proposals),
            keyStroke, autoActivationCharacters);
        adapter.setPropagateKeys(false);
        adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
        //adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
    }

    private void addContentAssistExtended(Text textControl) {
        char[] autoActivationCharacters = new char[] { '$', '{' };
        Map<String, String> proposals = new LinkedHashMap<String, String>();
        // add own variables
        proposals.putAll(Variable.getVariableInfoMap());
        // add eclipse variables
        proposals.putAll(Variable.getEclipseVariableInfoMap());
        ContentAssistCommandAdapter adapter = new ContentAssistCommandAdapter(textControl, new TextContentAdapter(),
                new CommandVariableContentProposalProvider(proposals), null,
                autoActivationCharacters, true);
        adapter.setPropagateKeys(false);
        adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
    }

    private void createVariablesOverview(Composite pageComponent) {
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
        pageGroup2.setFont(pageComponent.getFont());
        // create variable labels
        for(int i=Variable.getFirstIndex();i<Variable.values().length;i++) {
            Variable var = Variable.values()[i];
            if (var.isVisible()) {
                createVariableLabel(pageGroup2, var.getFullVariableName(), ": " + var.getDescription());
            }
        }
    }

    private void createConvertersOverview(Composite pageComponent) {
        // define group3
        Group pageGroup3 = new Group(pageComponent, SWT.SHADOW_ETCHED_IN);
        pageGroup3.setText(Activator.getResourceString("easyshell.command.editor.dialog.title.group3"));
        pageGroup3.setToolTipText(Activator.getResourceString("easyshell.command.editor.dialog.tooltip.group3"));
        GridLayout layout3 = new GridLayout();
        layout3.numColumns = 2;
        layout3.makeColumnsEqualWidth = false;
        layout3.marginWidth = 5;
        layout3.marginHeight = 4;
        pageGroup3.setLayout(layout3);
        GridData data3 = new GridData(GridData.FILL_HORIZONTAL);
        pageGroup3.setLayoutData(data3);
        pageGroup3.setFont(pageComponent.getFont());
        // create converters labels
        for(int i=Converter.getFirstIndex();i<Converter.values().length;i++) {
            Converter conv = Converter.values()[i];
            if (conv.isVisible()) {
                createVariableLabel(pageGroup3, conv.getName(), ": " + conv.getDescription());
            }
        }
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
        dirCheckBox.setEnabled(true);
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
        if (!validateValues()) {
            return;
        }
        CommandDataBasic cmdDataBasic = new CommandDataBasic(nameText.getText(), ResourceType.getFromName(resourceTypeCombo.getText()), dirCheckBox.getSelection(), dirText.getText(), valueText.getText());
        switch(data.getPresetType()) {
            case presetUser:
                data.setBasicData(cmdDataBasic);
                data.setCategory(Category.getFromName(categoryCombo.getText()));
                data.setCommandType(CommandType.getFromName(commandTypeCombo.getText()));
                break;
            case presetPlugin:
                if (data.checkIfUserDataOverridesPreset(cmdDataBasic)) {
                    data.addUserData(cmdDataBasic);
                }
                break;
            case presetPluginAndUser:
                if (data.checkIfUserDataOverridesPreset(cmdDataBasic)) {
                    data.setUserData(cmdDataBasic);
                } else {
                    data.removeUserData();
                }
                break;
            default:
                break;
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
                break;
            }
        }
        resourceTypeCombo.setEnabled(true);
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
                break;
            }
        }
        categoryCombo.setEnabled(data.getPresetType() == PresetType.presetUser);
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
                break;
            }
        }
        commandTypeCombo.setEnabled(data.getPresetType() == PresetType.presetUser);
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
        text.setEditable(true);
        return text;
    }

}
