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

import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.DynamicVariableResolver;
import de.anbos.eclipse.easyshell.plugin.Resource;
import de.anbos.eclipse.easyshell.plugin.ResourceUtils;
import de.anbos.eclipse.easyshell.plugin.preferences.CommandType;
import de.anbos.eclipse.easyshell.plugin.preferences.Quotes;
import de.anbos.eclipse.easyshell.plugin.preferences.Utils;

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
            MessageDialog.openInformation(
                new Shell(),
                "Easy Shell",
                "Wrong Selection");
            //Activator.log("Wrong Selection");
            return;
        }

        // get the ID
        //String ActionIDStr = action.getId();
        //Activator.getDefault().sysout(true, "Action ID: >" + ActionIDStr + "<");

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
                file_name = "dir"; // dummy cmd
            }else
            {
                parent_path = resource[i].getFile().getParent();
                file_name = resource[i].getFile().getName();
            }

            if (full_path != null) {

                //Activator.getDefault().sysout(true, "full_path  : >" + full_path + "<");
                //Activator.getDefault().sysout(true, "parent_path: >" + parent_path + "<");
                //Activator.getDefault().sysout(true, "file_name  : >" + file_name + "<");

                // Try to extract drive on Win32
                if (full_path.indexOf(":") != -1) {
                    drive = full_path.substring(0, full_path.indexOf(":"));
                }

                try {
                    String target = commandValue;
                    //Quotes quotes = Activator.getDefault().getQuotes(InstanceIDNum);
                    Quotes quotes = Quotes.quotesNo;
                    String[] args = new String[6];
                    // args format
                    args[0] = drive;							// {0} == ${easyshell:drive}
                    args[1] = autoQuotes(parent_path, quotes);	// {1} == ${easyshell:container_loc}
                    args[2] = autoQuotes(full_path, quotes);	// {2} == ${easyshell:resource_loc}
                    args[3] = autoQuotes(file_name, quotes);	// {3} == ${easyshell:resource_name}
                    args[4] = resource[i].getProjectName();		// {4} == ${easyshell:project_name}
                    if (args[4] == null)
                        args[4] = "EasyShell";
                    args[5] = System.getProperty("line.separator"); // {5} == ${easyshell:line_separator}
                    // variable format
                    DynamicVariableResolver.setArgs(args);
                    // handling copy to clipboard
                    if (commandType == CommandType.commandTypeClipboard) {
                    	String cmd = fixQuotes(variableManager.performStringSubstitution(target, false), quotes);
                    	//Activator.getDefault().sysout(true, "--- clp: >");
                        cmdAll += cmd;
                        //Activator.getDefault().sysout(true, cmd);
                        //Activator.getDefault().sysout(true, "--- clp: <");
                    }
                    // handling command line
                    else {
                    	// string tokenizer enabled ?
                    	//if (Activator.getDefault().isTokenizer(InstanceIDNum))
                        if (true)
                    	{
							StringTokenizer st = new StringTokenizer(target);
							String[] cmds = new String[st.countTokens()];
							int c = 0;
	                    	//Activator.getDefault().sysout(true, "--- cmd: >");
							while (st.hasMoreElements()) {
								cmds[c] = fixQuotes(variableManager.performStringSubstitution(st.nextToken(), false), quotes);
								//Activator.getDefault().sysout(true, cmds[c]);
								c++;
							}
							//Activator.getDefault().sysout(true, "--- cmd: <");
							//Utils.showToolTip(Display.getDefault().getActiveShell(), "EasyShell: executed", target);
							Runtime.getRuntime().exec(cmds);
                    	}
                    	// the old command line passing without string tokenizer
                    	else {
                        	String cmd = fixQuotes(variableManager.performStringSubstitution(target, false), quotes);
                        	//Activator.getDefault().sysout(true, "--- cmd: >");
                        	Runtime.getRuntime().exec(cmd);
                            //Activator.getDefault().sysout(true, cmd);
                            //Activator.getDefault().sysout(true, "--- cmd: <");
                    	}
                    }

                } catch (Exception e) {
                    //Activator.log(e);
                }

            } else {

                MessageDialog.openInformation(
                    new Shell(),
                    "Easy Shell",
                    "Unable to open shell");
                //Activator.log("Unable to open shell");
                return;

            }
        }

        // handling copy to clipboard
        if ((commandType == CommandType.commandTypeClipboard) && (cmdAll != null) && (cmdAll.length() != 0)) {
            Utils.copyToClipboard(cmdAll);
            Utils.showToolTip(Display.getDefault().getActiveShell(), Activator.getResourceString("easyshell.plugin.name") + ": " + Activator.getResourceString("easyshell.message.copytoclipboard"), cmdAll);
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
