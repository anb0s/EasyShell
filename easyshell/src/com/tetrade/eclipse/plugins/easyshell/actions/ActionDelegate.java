/*
 * Copyright (C) 2004 - 2014 by Marcel Schoen and Andre Bossert
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.tetrade.eclipse.plugins.easyshell.actions;

import java.text.MessageFormat;
import java.util.StringTokenizer;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import com.tetrade.eclipse.plugins.easyshell.EasyShellPlugin;
import com.tetrade.eclipse.plugins.easyshell.Resource;
import com.tetrade.eclipse.plugins.easyshell.ResourceUtils;
import com.tetrade.eclipse.plugins.easyshell.preferences.EasyShellQuotes;

public class ActionDelegate implements IObjectActionDelegate {

    private Resource[] resource = null;
    private IStructuredSelection currentSelection;

    /**
     * Constructor for EasyExploreAction.
     */
    public ActionDelegate() {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action) {

        if (!isEnabled()) {
            MessageDialog.openInformation(
                new Shell(),
                "Easy Shell",
                "Wrong Selection");
            EasyShellPlugin.log("Wrong Selection");
            return;
        }
        
        // get the ID + instance
        String ActionIDStr = action.getId();
        EasyShellPlugin.getDefault().sysout(true, "Action ID: >" + ActionIDStr + "<");
        String[] EasyShellActionStr = { "com.tetrade.eclipse.plugins.easyshell.command.shellOpen",
                                        "com.tetrade.eclipse.plugins.easyshell.command.shellRun",
                                        "com.tetrade.eclipse.plugins.easyshell.command.shellExplore",
                                        "com.tetrade.eclipse.plugins.easyshell.command.copyPath"
        };

        // check instance
        int InstanceIDNum = 0;
        if (ActionIDStr.contains("-")) {
        	String[] parts 		 = ActionIDStr.split("-");
        	ActionIDStr   		 = parts[0]; // ID
        	InstanceIDNum = Integer.parseInt(parts[1]);
        }

        if (InstanceIDNum > 2) {
            MessageDialog.openInformation(
                new Shell(),
                "Easy Shell",
                "Wrong Instance ID");
            EasyShellPlugin.log("Wrong Instance ID");
            return;
        }

        int ActionIDNum = -1;
        for (int i=0;i<EasyShellActionStr.length;i++)
        {
            if (ActionIDStr.equals(EasyShellActionStr[i]))
            {
                ActionIDNum = i;
                break;
            }
        }

        if (ActionIDNum == -1) {
            MessageDialog.openInformation(
                new Shell(),
                "Easy Shell",
                "Wrong Action ID");
            EasyShellPlugin.log("Wrong Action ID");
            return;
        }

        // String for all commands
        String cmdAll = null;
        if (ActionIDNum == 3) {
            cmdAll = new String();
        }

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

                EasyShellPlugin.getDefault().sysout(true, "full_path  : >" + full_path + "<");
                EasyShellPlugin.getDefault().sysout(true, "parent_path: >" + parent_path + "<");
                EasyShellPlugin.getDefault().sysout(true, "file_name  : >" + file_name + "<");

                // Try to extract drive on Win32
                if (full_path.indexOf(":") != -1) {
                    drive = full_path.substring(0, full_path.indexOf(":"));
                }

                try {
                    String target = EasyShellPlugin.getDefault().getTarget(ActionIDNum, InstanceIDNum);
                    EasyShellQuotes quotes = EasyShellPlugin.getDefault().getQuotes(InstanceIDNum);
                    String[] args = new String[6];

                    args[0] = drive;
                    args[1] = autoQuotes(parent_path, quotes);
                    args[2] = autoQuotes(full_path, quotes);
                    args[3] = autoQuotes(file_name, quotes);
                    //args[4] = autoQuotes(resource[i].getProjectName(), EasyShellQuotes.quotesDouble);
                    args[4] = resource[i].getProjectName();
                    if (args[4] == null)
                        args[4] = "EasyShell";
                    args[5] = System.getProperty("line.separator");

                    // handling copy to clipboard
                    if (ActionIDNum == 3) {
                    	String cmd = fixQuotes(MessageFormat.format(target, (Object[])args), quotes);
                    	EasyShellPlugin.getDefault().sysout(true, "--- clp: >");
                        cmdAll += cmd;
                        EasyShellPlugin.getDefault().sysout(true, cmd);
                        EasyShellPlugin.getDefault().sysout(true, "--- clp: <");
                    }
                    // handling command line
                    else {
                    	// string tokenizer enabled ?
                    	if (EasyShellPlugin.getDefault().isTokenizer(InstanceIDNum))
                    	{
							StringTokenizer st = new StringTokenizer(target);
							String[] cmds = new String[st.countTokens()];
							int c = 0;
	                    	EasyShellPlugin.getDefault().sysout(true, "--- cmd: >");
							while (st.hasMoreElements()) {
								cmds[c] = fixQuotes(MessageFormat.format(st.nextToken(), (Object[])args), quotes);
								EasyShellPlugin.getDefault().sysout(true, cmds[c]);
								c++;
							}
							EasyShellPlugin.getDefault().sysout(true, "--- cmd: <");
							Runtime.getRuntime().exec(cmds);
                    	}
                    	// the old command line passing without string tokenizer
                    	else {
                        	String cmd = fixQuotes(MessageFormat.format(target, (Object[])args), quotes);
                        	EasyShellPlugin.getDefault().sysout(true, "--- cmd: >");
                        	Runtime.getRuntime().exec(cmd);
                            EasyShellPlugin.getDefault().sysout(true, cmd);
                            EasyShellPlugin.getDefault().sysout(true, "--- cmd: <");
                    	}
                    }

                } catch (Exception e) {
                    EasyShellPlugin.log(e);
                }

            } else {

                MessageDialog.openInformation(
                    new Shell(),
                    "Easy Shell",
                    "Unable to open shell");
                EasyShellPlugin.log("Unable to open shell");
                return;

            }
        }

        // handling copy to clipboard
        if ((ActionIDNum == 3) && (cmdAll != null) && (cmdAll.length() != 0)) {
            Clipboard clipboard = new Clipboard(Display.getCurrent());
            TextTransfer textTransfer = TextTransfer.getInstance();
            Transfer[] transfers = new Transfer[]{textTransfer};
            Object[] data = new Object[]{cmdAll};
            clipboard.setContents(data, transfers);
            clipboard.dispose();
        }

    }

    private String autoQuotes(String str, EasyShellQuotes quotes) {
        String ret;
        if (quotes == EasyShellQuotes.quotesSingle) {
            ret = "'" + str + "'";
        }
        else if (quotes == EasyShellQuotes.quotesDouble) {
            ret = "\"" + str + "\"";
        }
        else if (quotes == EasyShellQuotes.quotesAuto && (str.indexOf("\"") == -1) && (str.indexOf(" ") != -1) ) { // if no quotes and space add quotes
            ret = "\"" + str + "\"";
        } else {
            ret = str;
        }
        return ret;
    }

    private String fixQuotes(String str, EasyShellQuotes quotes) {
        String ret = str;
        /*if ( (quotes == EasyShellQuotes.quotesYes) ||
             (quotes == EasyShellQuotes.quotesAuto) ) {
            // try to replace known combinations
            //ret = Matcher.quoteReplacement(str).replaceAll("./\"", "\"./");
            ret = str.replaceAll("./\"", "\"./");
        }*/
        return ret;
    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
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

}
