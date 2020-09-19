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

package de.anbos.eclipse.easyshell.plugin.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.preferences.GeneralDataStore;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.Quotes;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.Tooltip;
import de.anbos.eclipse.easyshell.plugin.types.CommandTokenizer;

public class ActionDelegate implements IObjectActionDelegate {

    private List<Resource> resources = null;
    private IStructuredSelection currentSelection;
    private CommandTokenizer commandTokenizer = CommandTokenizer.commandTokenizerUnknown;
    private String commandValue = null;
    private String commandWorkingDir = null;
    private CommandType commandType = CommandType.commandTypeUnknown;
    private ResourceType resourceType = ResourceType.resourceTypeUnknown;

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public CommandTokenizer getCommandTokenizer() {
        return commandTokenizer;
    }

    public void setCommandTokenizer(CommandTokenizer commandTokenizer) {
        this.commandTokenizer = commandTokenizer;
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
        // String for all commands in case of clipboard
        String cmdAll = null;
        if (commandType == CommandType.commandTypeClipboard) {
            cmdAll = new String();
        }
        // get the manager for variables expansion
        IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
        // iterate over the resources
        for (Resource resource : resources) {
            // TODO: get from preferences store
            //Quotes quotes = Activator.getQuotes(InstanceIDNum);
            Quotes quotes = Quotes.quotesNo;
            if (resource.resolve()) {
                Activator.logDebug("res:>" + resource.getResourceLocation() + "<");
                try {
                    // set arguments for resolving
                    DynamicVariableResolver.setResource(resource);
                    DynamicVariableResolver.setQuotes(quotes);
                    // validate the command
                    variableManager.validateStringVariables(commandValue);
                    Activator.logDebug("cmd:>" + commandValue + "<");
                    // handling copy to clipboard
                    if (commandType == CommandType.commandTypeClipboard) {
                        String cmd = variableManager.performStringSubstitution(commandValue, false);
                        Activator.logDebug("clp:>" + cmd + "<");
                        cmdAll += cmd;
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
            if (GeneralDataStore.instance().getData().getToolTipClipboard() == Tooltip.tooltipYes) {
                Activator.tooltipInfo(Activator.getResourceString("easyshell.message.copytoclipboard"), cmdAll);
            }
        }
    }

    private String[] getCommandResolved(IStringVariableManager variableManager) throws CoreException {
        String[] commandArray = null;
        switch(commandTokenizer) {
            case commandTokenizerSpaces:
                commandArray = Utils.splitSpaces(commandValue);
            break;
            case commandTokenizerSpacesAndQuotes:
                commandArray = Utils.splitSpacesAndQuotes(commandValue, false);
            break;
            case commandTokenizerSpacesAndQuotesSkip:
                commandArray = Utils.splitSpacesAndQuotes(commandValue, true);
            break;
            case commandTokenizerDisabled:
                commandArray = new String[1];
                commandArray[0] = commandValue;
            break;
            default:
                throw new IllegalArgumentException();
        }
        // resolve the variables
        for (int i=0;i<commandArray.length;i++) {
            commandArray[i] = variableManager.performStringSubstitution(commandArray[i], false);
            Activator.logDebug("exc" + i + ":>" + commandArray[i]+ "<");
        }
        return commandArray;
    }

    private File getWorkingDirectoryResolved(IStringVariableManager variableManager) throws CoreException {
        // working directory
        if (commandWorkingDir != null && !commandWorkingDir.isEmpty()) {
            variableManager.validateStringVariables(commandWorkingDir);
            Activator.logDebug("cwd: >" + commandWorkingDir + "<");
            return new File(variableManager.performStringSubstitution(commandWorkingDir, false));
        }
        return null;
    }

    private void handleExec(IStringVariableManager variableManager) throws CoreException, IOException {
        Activator.logDebug("exc:>---");
        // get working directory
        File workingDirectory = getWorkingDirectoryResolved(variableManager);
        // get command
        String[] command = getCommandResolved(variableManager);
        // create process builder with command and ...
        ProcessBuilder pb = new ProcessBuilder(command);
        // ... set working directory and redirect error stream
        if (workingDirectory != null) {
            pb.directory(workingDirectory);
        }
        Activator.logDebug("exc:<---");
        // get passed system environment
        //Map<String, String> env = pb.environment();
        // add own variables
        pb.start();
    }

    public void selectionChanged(IAction action, ISelection selection) {
        currentSelection = selection instanceof IStructuredSelection ? (IStructuredSelection)selection : null;
    }

    public boolean isEnabled(ResourceType resType)
    {
        resources = new ArrayList<Resource>();
        if (currentSelection != null)
        {
            Object[] selectedObjects = currentSelection.toArray();
            if (selectedObjects.length >= 1)
            {
                for (Object object : selectedObjects) {
                    Resource resource = ResourceUtils.getResource(object);
                    if (resource != null) {
                        boolean resourceValid = true;
                        switch(resType) {
                        case resourceTypeFile:
                            resourceValid = resource.isFile();
                            break;
                        case resourceTypeDirectory:
                            resourceValid = resource.isDirectory();
                            break;
                        default:
                            break;
                        }
                        if (resourceValid) {
                            resources.add(resource);
                        }
                    }
                }
            }
        }
        return resources.size() > 0;
    }

    public ResourceType getCommonResourceType()
    {
        ResourceType resType = ResourceType.resourceTypeUnknown;
        for (Resource resource : resources) {
            ResourceType actResType = resource.isFile() ? ResourceType.resourceTypeFile : ResourceType.resourceTypeDirectory;
            if (resType == ResourceType.resourceTypeUnknown) {
                resType = actResType; // store the first valid type
            } else if (resType != actResType) {
                resType = ResourceType.resourceTypeFileOrDirectory; // if it changes then we have both types and can quit
                break;
            }
        }
        return resType;
    }

}
