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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.misc.UtilsUI;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.Converter;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.CommandTokenizer;
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
    private ContentProposalAdapter dirTextAssist;
    private Text    valueText;
    private Combo   tokenizerCombo;
    private ContentProposalAdapter valueTextAssist;

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
        Composite pageComponent = createPageComponent(parent);

        createCommandControls(pageComponent);

        if (showVariablesInfo) {
            createVariablesOverview(pageComponent);
        }

        if (showConvertersInfo) {
            createConvertersOverview(pageComponent);
        }

        setHelpAvailable(showHelpButton);

        UtilsUI.refreshWidget(resourceTypeCombo);

        UtilsUI.refreshWidget(categoryCombo);

        UtilsUI.refreshWidget(commandTypeCombo);

        UtilsUI.refreshWidget(dirCheckBox);

        UtilsUI.refreshWidget(tokenizerCombo);

        return pageComponent;
    }

    public Composite createPageComponent(Composite parent) {
        Font font = parent.getFont();
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
        pageComponent.setFont(font);
        return pageComponent;
    }

    public Group createGroupCommand(Composite parent) {
        Font font = parent.getFont();
        Group pageGroupCommand = new Group(parent, SWT.SHADOW_ETCHED_IN);
        pageGroupCommand.setText(Activator.getResourceString("easyshell.command.editor.dialog.title.group1"));
        pageGroupCommand.setToolTipText(Activator.getResourceString("easyshell.command.editor.dialog.tooltip.group1"));
        GridLayout layoutCommand = new GridLayout();
        layoutCommand.numColumns = 3;
        layoutCommand.makeColumnsEqualWidth = false;
        layoutCommand.marginWidth = 5;
        layoutCommand.marginHeight = 4;
        pageGroupCommand.setLayout(layoutCommand);
        GridData dataCommand = new GridData(GridData.FILL_HORIZONTAL);
        pageGroupCommand.setLayoutData(dataCommand);
        pageGroupCommand.setFont(font);
        return pageGroupCommand;
    }

    private void createCommandControls(Composite parent) {
        Group pageGroupCommand = createGroupCommand(parent);
        // create resource type combo
        createResourceTypeCombo(pageGroupCommand);
        // create category combo
        createCategoryCombo(pageGroupCommand);
        // create command type combo
        createCommandTypeCombo(pageGroupCommand);
        //create input nameText field
        nameText = UtilsUI.createTextField(pageGroupCommand, Activator.getResourceString("easyshell.command.editor.dialog.label.name"), Activator.getResourceString("easyshell.command.editor.dialog.label.tooltip.name"), data.getName(), true, true);
        // create directory checkbox
        createDirCheckBox(pageGroupCommand);
        // create input dirText field and add content assist
        dirText = UtilsUI.createTextField(pageGroupCommand, null, Activator.getResourceString("easyshell.command.editor.dialog.label.tooltip.useworkdir") + Activator.getResourceString("easyshell.command.editor.dialog.tooltip.content.assists"), data.getWorkingDirectory(), false, true);
        dirTextAssist = addContentAssist(dirText);
        // create input valueText field and add content assist
        valueText = UtilsUI.createTextField(pageGroupCommand, Activator.getResourceString("easyshell.command.editor.dialog.label.value"), Activator.getResourceString("easyshell.command.editor.dialog.label.tooltip.value") + Activator.getResourceString("easyshell.command.editor.dialog.tooltip.content.assists"), data.getCommand(), true, true);
        valueTextAssist = addContentAssist(valueText);
        valueTextAssist.setEnabled(true);
        // create tokenizer combo
        createTokenizerCombo(pageGroupCommand);
    }

    private ContentProposalAdapter addContentAssistSimple(Text textControl) {
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
        return adapter;
    }

    private ContentProposalAdapter addContentAssistExtended(Text textControl) {
        char[] autoActivationCharacters = new char[] { '$', '{' };
        Map<String, String> proposals = new LinkedHashMap<String, String>();
        // add own variables
        proposals.putAll(Variable.getVariableInfoMap());
        // add eclipse variables
        proposals.putAll(Variable.getEclipseVariableInfoMap());
        ContentAssistCommandAdapter adapter = new ContentAssistCommandAdapter(textControl, new CommandVariableContentAdapter(),
                new CommandVariableContentProposalProvider(proposals), null,
                autoActivationCharacters, true);
        adapter.setPropagateKeys(false);
        adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
        return adapter;
    }

    private ContentProposalAdapter addContentAssist(Text textControl) {
        // add content assist to command text editor field
        if (useExtendedContentAssists) {
            return addContentAssistExtended(textControl);
        } else {
            return addContentAssistSimple(textControl);
        }
    }

    private void createVariablesOverview(Composite parent) {
        Font font = parent.getFont();
        Group pageGroup2 = new Group(parent, SWT.SHADOW_ETCHED_IN);
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
        pageGroup2.setFont(font);
        // create variable labels
        for(int i=Variable.getFirstIndex();i<Variable.values().length;i++) {
            Variable var = Variable.values()[i];
            if (var.isVisible()) {
                createVariableLabel(pageGroup2, var.getFullVariableName(), ": " + var.getDescription());
            }
        }
    }

    private void createConvertersOverview(Composite parent) {
        Font font = parent.getFont();
        Group pageGroup3 = new Group(parent, SWT.SHADOW_ETCHED_IN);
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
        pageGroup3.setFont(font);
        // create converters labels
        for(int i=Converter.getFirstIndex();i<Converter.values().length;i++) {
            Converter conv = Converter.values()[i];
            if (conv.isVisible()) {
                createVariableLabel(pageGroup3, conv.getName(), ": " + conv.getDescription());
            }
        }
    }


    private void createDirCheckBox(Composite parent) {
        // draw label
        UtilsUI.createLabel(parent, Activator.getResourceString("easyshell.command.editor.dialog.label.useworkdir"), Activator.getResourceString("easyshell.command.editor.dialog.label.tooltip.useworkdir"));
        // draw checkbox
        dirCheckBox = new Button(parent,SWT.CHECK);
        dirCheckBox.setSelection(this.data.isUseWorkingDirectory());
        dirCheckBox.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                //Button button = (Button)e.getSource();
                boolean enabled = dirCheckBox.getSelection();
                dirText.setEditable(enabled);
                dirTextAssist.setEnabled(enabled);
                if (!enabled && dirText.getText().isEmpty()) {
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

    protected void okPressed() {
        if (!validateValues()) {
            return;
        }
        CommandDataBasic cmdDataBasic = new CommandDataBasic(null, nameText.getText(), ResourceType.getFromName(resourceTypeCombo.getText()), dirCheckBox.getSelection(), dirText.getText(), CommandTokenizer.getFromName(tokenizerCombo.getText()), valueText.getText());
        switch(data.getPresetType()) {
            case presetUser:
                data.setBasicData(cmdDataBasic);
                data.setCategory(Category.getFromName(categoryCombo.getText()));
                data.setCommandType(CommandType.getFromName(commandTypeCombo.getText()));
                break;
            case presetPlugin:
            case presetPluginModify:
                data.addOrRemoveModifyData(cmdDataBasic);
                break;
            default:
                break;
        }
        super.okPressed();
    }

    private boolean validateValues() {

        final String title = Activator.getResourceString("easyshell.error.title.incompletedata");

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
        UtilsUI.createLabel(parent, Activator.getResourceString("easyshell.command.editor.dialog.label.combo.resource"), Activator.getResourceString("easyshell.command.editor.dialog.label.tooltip.resource"));
        UtilsUI.createLabel(parent, "", null);
        // draw combo
        resourceTypeCombo = new Combo(parent,SWT.BORDER | SWT.READ_ONLY);
        resourceTypeCombo.setToolTipText(Activator.getResourceString("easyshell.command.editor.dialog.combo.tooltip.resource"));
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
        UtilsUI.createLabel(parent, Activator.getResourceString("easyshell.command.editor.dialog.label.combo.category"), Activator.getResourceString("easyshell.command.editor.dialog.label.tooltip.category"));
        categoryImage = UtilsUI.createImageLabel(parent, Category.categoryUnknown.getImageId());
        // draw combo
        categoryCombo = new Combo(parent,SWT.BORDER | SWT.READ_ONLY);
        categoryCombo.setToolTipText(Activator.getResourceString("easyshell.command.editor.dialog.combo.tooltip.category"));
        categoryCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        categoryCombo.setItems(Category.getNamesAsArray());
        categoryCombo.select(0);
        categoryCombo.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String text = categoryCombo.getItem(categoryCombo.getSelectionIndex());
                categoryImage.setImage(Activator.getImage(Category.getFromName(text).getImageId()));
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
        UtilsUI.createLabel(parent, Activator.getResourceString("easyshell.command.editor.dialog.label.combo.type"), Activator.getResourceString("easyshell.command.editor.dialog.label.tooltip.type"));
        UtilsUI.createLabel(parent, "", null);
        // draw combo
        commandTypeCombo = new Combo(parent,SWT.BORDER | SWT.READ_ONLY);
        commandTypeCombo.setToolTipText(Activator.getResourceString("easyshell.command.editor.dialog.combo.tooltip.type"));
        commandTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        //commandTypeCombo.setEditable(false);
        commandTypeCombo.setItems(CommandType.getNamesAsArray());
        commandTypeCombo.select(0);
        commandTypeCombo.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String text = commandTypeCombo.getItem(commandTypeCombo.getSelectionIndex());
                CommandType commandType = CommandType.getFromName(text);
                boolean enabledTokenizer = commandType == CommandType.commandTypeExecute;
                tokenizerCombo.setEnabled(enabledTokenizer);
                String tokenizerName = enabledTokenizer ? null : CommandTokenizer.commandTokenizerDisabled.getName();
                   selectTokenizerCombo(tokenizerName);
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

    private void createTokenizerCombo(Composite parent) {
        // draw label
        UtilsUI.createLabel(parent, Activator.getResourceString("easyshell.command.editor.dialog.label.combo.tokenizer"), Activator.getResourceString("easyshell.command.editor.dialog.label.tooltip.tokenizer"));
        UtilsUI.createLabel(parent, "", null);
        // draw combo
        tokenizerCombo = new Combo(parent,SWT.BORDER | SWT.READ_ONLY);
        tokenizerCombo.setToolTipText(Activator.getResourceString("easyshell.command.editor.dialog.combo.tooltip.tokenizer"));
        tokenizerCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        //tokenizerCombo.setEditable(false);
        tokenizerCombo.setItems(CommandTokenizer.getNamesAsArray());
        tokenizerCombo.select(0);
        tokenizerCombo.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                //String text = tokenizerCombo.getItem(tokenizerCombo.getSelectionIndex());
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
            }
        });
        selectTokenizerCombo(null);
        tokenizerCombo.setEnabled(true);
    }

    private void selectTokenizerCombo(String altName) {
        String[] items = tokenizerCombo.getItems();
        for(int i = 0 ; i < items.length ; i++) {
            String name = altName != null ? altName : this.data.getCommandTokenizer().getName();
            if(items[i].equals(name)) {
                tokenizerCombo.select(i);
                break;
            }
        }
    }

}
