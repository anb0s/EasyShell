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

        /* TODO: remove
        // get the ID
        String ActionIDStr = action.getId();
        Activator.logDebug("Action ID: >" + ActionIDStr + "<");
        */

        // String for all commands in case of clipboard
        String cmdAll = null;
        if (commandType == CommandType.commandTypeClipboard) {
            cmdAll = new String();
        }

        IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();

        for (int i=0;i<resource.length;i++) {

            if (resource[i] == null)
                continue;

            String drive = null;
            String full_path = null;
            String parent_path = null;
            String file_name = null;

            full_path = resource[i].getFile().toString();
            if (resource[i].getFile().isDirectory()) {
                parent_path = resource[i].getFile().getPath();
                //file_name = "dir"; // dummy cmd
            }else
            {
                parent_path = resource[i].getFile().getParent();
                file_name = resource[i].getFile().getName();
            }

            if (full_path != null) {

                Activator.logDebug("full_path  : >" + full_path + "<");
                Activator.logDebug("parent_path: >" + parent_path + "<");
                Activator.logDebug("file_name  : >" + file_name + "<");

                // Try to extract drive on Win32
                if (full_path.indexOf(":") != -1) {
                    drive = full_path.substring(0, full_path.indexOf(":"));
                }

                try {
                    // TODO: get from preferences store
                    //Quotes quotes = Activator.getQuotes(InstanceIDNum);
                    Quotes quotes = Quotes.quotesNo;
                    String[] args = new String[6];
                    // args format
                    args[0] = drive;							// {0} == ${easyshell:drive}
                    args[1] = autoQuotes(parent_path, quotes);	// {1} == ${easyshell:container_loc}
                    args[2] = autoQuotes(full_path, quotes);	// {2} == ${easyshell:resource_loc}
                    args[3] = autoQuotes(file_name, quotes);	// {3} == ${easyshell:resource_name}
                    args[4] = resource[i].getProjectName();		// {4} == ${easyshell:project_name}
                    if (args[4] == null)
                        args[4] = Activator.getResourceString("easyshell.plugin.name");
                    args[5] = System.getProperty("line.separator"); // {5} == ${easyshell:line_separator}
                    // variable format
                    DynamicVariableResolver.setArgs(args);
                    variableManager.validateStringVariables(commandValue);
                    Activator.logDebug(commandValue);
                    // handling copy to clipboard
                    if (commandType == CommandType.commandTypeClipboard) {
                    	String cmd = fixQuotes(variableManager.performStringSubstitution(commandValue, false), quotes);
                    	Activator.logDebug("--- clp: >");
                        cmdAll += cmd;
                        Activator.logDebug(cmd);
                        Activator.logDebug("--- clp: <");
                    }
                    // handling command line
                    else {
                    	// string tokenizer enabled ?
                        // TODO: get from preferences store
                        //Tokenizer tokenizer = Activator.isTokenizer(InstanceIDNum);
                        Tokenizer tokenizer = Tokenizer.tokenizerYes;
                        if (tokenizer == Tokenizer.tokenizerYes)
                    	{
							StringTokenizer st = new StringTokenizer(commandValue);
							String[] cmds = new String[st.countTokens()];
							int c = 0;
							Activator.logDebug("--- cmd: >");
							while (st.hasMoreElements()) {
								cmds[c] = fixQuotes(variableManager.performStringSubstitution(st.nextToken(), false), quotes);
								Activator.logDebug(cmds[c]);
								c++;
							}
							Activator.logDebug("--- cmd: <");
							//Utils.showToolTip(Display.getDefault().getActiveShell(), "EasyShell: executed", commandValue);
							// ---------- RUN --------------
							Runtime.getRuntime().exec(cmds);
                    	}
                    	// the old command line passing without string tokenizer
                    	else {
                        	String cmd = fixQuotes(variableManager.performStringSubstitution(commandValue, false), quotes);
                        	Activator.logDebug("--- cmd: >");
                        	Activator.logDebug(cmd);
                        	Activator.logDebug("--- cmd: <");
                        	Runtime.getRuntime().exec(cmd);
                    	}
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

    public void selectionChanged(IAction action, ISelection selection) {
        currentSelection = selection instanceof IStructuredSelection ? (IStructuredSelection)selection : null;
    }

    public boolean isEnabled()
    {
        boolean enabled = false;
        if (currentSelection != null)
        {
            Object[] selectedObjects = currentSelection.toArray();
            if (selectedObjects.length >= 1)
            {
                resource = new Resource[selectedObjects.length];
                for (int i=0;i<selectedObjects.length;i++) {
                    resource[i] = ResourceUtils.getResource(selectedObjects[i]);
                    if (resource[i] != null)
                        enabled=true;
                }
            }
        }
        return enabled;
    }

    private String autoQuotes(String str, Quotes quotes) {
        String ret = str;
        if (quotes == Quotes.quotesSingle) {
            ret = "'" + str + "'";
        }
        else if (quotes == Quotes.quotesDouble) {
            ret = "\"" + str + "\"";
        }
        else if (quotes == Quotes.quotesEscape) {
            ret = str.replaceAll("\\s", "\\\\ ");
        }
        else if ( ((quotes == Quotes.quotesAuto) || (quotes == Quotes.quotesAutoSingle))
                    && (str.indexOf(" ") != -1) ) { // if space there
            if ((quotes == Quotes.quotesAutoSingle) && str.indexOf("\"") == -1) { // if no single quotes
                ret = "'" + str + "'";
            } else if (str.indexOf("'") == -1){ // if no double quotes
                ret = "\"" + str + "\"";
            }
        }
        return ret;
    }

    private String fixQuotes(String str, Quotes quotes) {
        String ret = str;
        /*if ( (quotes == EasyShellQuotes.quotesYes) ||
             (quotes == EasyShellQuotes.quotesAuto) ) {
            // try to replace known combinations
            //ret = Matcher.quoteReplacement(str).replaceAll("./\"", "\"./");
            ret = str.replaceAll("./\"", "\"./");
        }*/
        return ret;
    }

}
