/*
 * Copyright (C) 2004 - 2010 by Marcel Schoen and Andre Bossert
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
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.ShowInMenu;

import com.tetrade.eclipse.plugins.easyshell.EasyShellPlugin;
import com.tetrade.eclipse.plugins.easyshell.Resource;
import com.tetrade.eclipse.plugins.easyshell.ResourceUtils;

public class EasyShellAction implements IObjectActionDelegate {

	private boolean debug = false;

	private Resource[] resource = null;
	private IStructuredSelection currentSelection;

	/**
	 * Constructor for EasyExploreAction.
	 */
	public EasyShellAction() {
		super();
		debug = EasyShellPlugin.getDefault().isDebug();
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

		String ActionIDStr = action.getId();
		if (debug) System.out.println("Action ID: [[" + ActionIDStr + "]]");
		String[] EasyShellActionStr = { "com.tetrade.eclipse.plugins.easyshell.command.shellOpen",
										"com.tetrade.eclipse.plugins.easyshell.command.shellRun",
										"com.tetrade.eclipse.plugins.easyshell.command.shellExplore",
										"com.tetrade.eclipse.plugins.easyshell.command.copyPath"
		};
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

				if (debug) {
					System.out.println("full_path  : [[" + full_path + "]]");
					System.out.println("parent_path: [[" + parent_path + "]]");
					System.out.println("file_name  : [[" + file_name + "]]");
				}
				// Try to extract drive on Win32
				if (full_path.indexOf(":") != -1) {
					drive = full_path.substring(0, full_path.indexOf(":"));
				}

				try {
					String target = EasyShellPlugin.getDefault().getTarget(ActionIDNum);
					String[] args = new String[6];

					args[0] = drive;
					args[1] = parent_path;
					args[2] = full_path;
					args[3] = file_name;
					args[4] = resource[i].getProjectName();
					if (args[4] == null)
						args[4] = "EasyShell";
					args[5] = System.getProperty("line.separator");

					String cmd = MessageFormat.format(target, (Object[])args);
					if (debug) System.out.println("cmd: [[" + cmd + "]]");

					if (ActionIDNum == 3) {
						cmdAll += cmd;
					} else {
						Runtime.getRuntime().exec(cmd);
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

		if ((ActionIDNum == 3) && (cmdAll != null) && (cmdAll.length() != 0)) {
			Clipboard clipboard = new Clipboard(Display.getCurrent());
			TextTransfer textTransfer = TextTransfer.getInstance();
			Transfer[] transfers = new Transfer[]{textTransfer};
			Object[] data = new Object[]{cmdAll};
			clipboard.setContents(data, transfers);
			clipboard.dispose();
		}
		
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
