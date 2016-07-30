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

package de.anbos.eclipse.easyshell.plugin.actions;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.DynamicVariableResolver;
import de.anbos.eclipse.easyshell.plugin.Resource;
import de.anbos.eclipse.easyshell.plugin.ResourceUtils;
import de.anbos.eclipse.easyshell.plugin.Utils;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.Quotes;
import de.anbos.eclipse.easyshell.plugin.types.Tokenizer;

public class ActionDelegate implements IObjectActionDelegate {

    private Resource[] resource = null;
    private IStructuredSelection currentSelection;
    private String commandValue = null;
    private String commandWorkingDir = null;
    private CommandType commandType = CommandType.commandTypeUnknown;

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public String getCommandValue() {
        return commandValue;
    }

    public void setCommandValue(String commandValue) {
        this.commandValue = commandValue;
    }

    public void setCommandWorkingDir(String commandWorkingDir) {
        this.commandWorkingDir = commandWorkingDir;
    }

    public ActionDelegate() {
        super();
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    public void run(IAction action) {
        if (!isEnabled()) {
            Activator.logError("Wrong Selection", null);
            return;
        }
        // String for all commands in case of clipboard
        String cmdAll = null;
        if (commandType == CommandType.commandTypeClipboard) {
            cmdAll = new String();
        }
        // get the manager for variables expansion
        IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
        // iterate over the reources
        for (int i=0;i<resource.length;i++) {
            if (resource[i] == null)
                continue;
            // TODO: get from preferences store
            //Quotes quotes = Activator.getQuotes(InstanceIDNum);
            Quotes quotes = Quotes.quotesNo;
            if (resource[i].resolve()) {
                try {
                    // set arguments for resolving
                    DynamicVariableResolver.setResource(resource[i]);
                    DynamicVariableResolver.setQuotes(quotes);
                    // validate the command
                    variableManager.validateStringVariables(commandValue);
                    Activator.logDebug(commandValue);
                    // handling copy to clipboard
                    if (commandType == CommandType.commandTypeClipboard) {
                    	String cmd = variableManager.performStringSubstitution(commandValue, false);
                    	Activator.logDebug("--- clp: >");
                        cmdAll += cmd;
                        Activator.logDebug(cmd);
                        Activator.logDebug("--- clp: <");
                    }
                    // handling command line
                    else {
                        handleExec(variableManager);
                    }
                } catch (CoreException e) {
                    Activator.logError(Activator.getResourceString("easyshell.message.error.validation"), commandValue, e, true);
                } catch (Exception e) {
                    Activator.logError(Activator.getResourceString("easyshell.message.error.execution"), commandValue, e, true);
                }
            } else {
                Activator.logError(Activator.getResourceString("easyshell.message.error.internal"), commandValue, null, true);
            }
        }
        // handling copy to clipboard
        if ((commandType == CommandType.commandTypeClipboard) && (cmdAll != null) && (cmdAll.length() != 0)) {
            Utils.copyToClipboard(cmdAll);
            Activator.tooltipInfo(Activator.getResourceString("easyshell.message.copytoclipboard"), cmdAll);
        }
    }

    private void handleExec(IStringVariableManager variableManager) throws CoreException, IOException {
        String[] cmds = null;
        // working directory
        if (commandWorkingDir != null && !commandWorkingDir.isEmpty()) {
            variableManager.validateStringVariables(commandWorkingDir);
        }
        Activator.logDebug(commandWorkingDir);
        // string tokenizer enabled ?
        // TODO: get from preferences store
        //Tokenizer tokenizer = Activator.isTokenizer(InstanceIDNum);
        Tokenizer tokenizer = Tokenizer.tokenizerYes;
        if (tokenizer == Tokenizer.tokenizerYes)
        {
        	StringTokenizer st = new StringTokenizer(commandValue);
        	cmds = new String[st.countTokens()];
        	int i = 0;
        	while (st.hasMoreElements()) {
        		cmds[i] = variableManager.performStringSubstitution(st.nextToken(), false);
        		i++;
        	}
        }
        // the old command line passing without string tokenizer
        else {
            cmds = new String[1];
        	cmds[0] = variableManager.performStringSubstitution(commandValue, false);
        }
        // log out
        for (int i=0;i<cmds.length;i++) {
            Activator.logDebug("--- cmd: >");
            Activator.logDebug(cmds[i]);
            Activator.logDebug("--- cmd: <");
        }
        // execute
        //Utils.showToolTip(Display.getDefault().getActiveShell(), "EasyShell: executed", commandValue);
        // ---------- RUN --------------
        //Runtime.getRuntime().exec(cmds);
        // create process builder with commands and
        ProcessBuilder pb = new ProcessBuilder(cmds);
        // set working directory and redirect error stream
        if (commandWorkingDir != null && !commandWorkingDir.isEmpty()) {
            pb.directory(new File(variableManager.performStringSubstitution(commandWorkingDir, false)));
        }
        // get passed system environment
        //Map<String, String> env = pb.environment();
        // add own variables
        pb.start();
    }

    public void selectionChanged(IAction action, ISelection selection) {
        currentSelection = selection instanceof IStructuredSelection ? (IStructuredSelection)selection : null;
    }

    public boolean isEnabled()
    {
        boolean enabled = false;
        if (currentSelection != null)
        {
            String projectName = Activator.getResourceString("easyshell.plugin.name");
            Object[] selectedObjects = currentSelection.toArray();
            if (selectedObjects.length >= 1)
            {
                resource = new Resource[selectedObjects.length];
                for (int i=0;i<selectedObjects.length;i++) {
                    resource[i] = ResourceUtils.getResource(selectedObjects[i], projectName);
                    if (resource[i] != null) {
                        enabled=true;
                    }
                }
            }
        }
        return enabled;
    }

}
