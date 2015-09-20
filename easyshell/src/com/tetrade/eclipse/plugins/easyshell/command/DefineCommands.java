/*
 * Copyright (C) 2014 - 2015 by Andre Bossert
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

package com.tetrade.eclipse.plugins.easyshell.command;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import com.tetrade.eclipse.plugins.easyshell.EasyShellPlugin;
import com.tetrade.eclipse.plugins.easyshell.preferences.EasyShellCommand;
import com.tetrade.eclipse.plugins.easyshell.preferences.EasyShellPreferenceEntry;

public class DefineCommands extends ExtensionContributionFactory {

	public DefineCommands() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createContributionItems(IServiceLocator serviceLocator,
			IContributionRoot additions) {

		/*
		CommandContributionItemParameter p1 = new CommandContributionItemParameter(serviceLocator, "",
		        "org.eclipse.ui.file.exit",
		        SWT.PUSH);
		p1.label = "Exit the application";
		p1.icon = EasyShellPlugin.getImageDescriptor("icons/alt_window_16.gif");
	    CommandContributionItem item1 = new CommandContributionItem(p1);
        item1.setVisible(true);
        additions.addContributionItem(item1, null);
		*/

		for (int i = 0; i < EasyShellPlugin.getInstanceNumber(); i++) {
			String instanceId 		= Integer.toString(i);
			String enabled   		= EasyShellPlugin.getDefault().getTarget(EasyShellPreferenceEntry.preferenceTargetEnabled.getId(), i);
			String presetId   		= EasyShellPlugin.getDefault().getTarget(EasyShellPreferenceEntry.preferenceListString.getId(), i);
			EasyShellPlugin.getDefault().sysout(true,"instanceId : " + i + "; presetId : " + presetId);
			if (enabled == "false" || presetId == null || presetId.isEmpty())
				continue;
			EasyShellCommand cmd	= EasyShellCommand.valueOf(presetId);
			String consoleName 		= cmd.getOS() + " " + cmd.getConsole();
			String explorerName		= cmd.getOS() + " " + cmd.getExplorer();
			addItem(serviceLocator, additions,
					"Open with " + consoleName,
					"com.tetrade.eclipse.plugins.easyshell.command.shellOpen",
					"com.tetrade.eclipse.plugins.easyshell.Open.InstanceID",
					instanceId,
					EasyShellPlugin.IMAGE_OPEN_ID);
			addItem(serviceLocator, additions,
					"Run with " + consoleName,
					"com.tetrade.eclipse.plugins.easyshell.command.shellRun",
					"com.tetrade.eclipse.plugins.easyshell.Run.InstanceID",
					instanceId,
					EasyShellPlugin.IMAGE_RUN_ID);
			addItem(serviceLocator, additions,
					"Explore with " + explorerName,
					"com.tetrade.eclipse.plugins.easyshell.command.shellExplore",
					"com.tetrade.eclipse.plugins.easyshell.Explore.InstanceID",
					instanceId,
					EasyShellPlugin.IMAGE_EXPLORE_ID);
		}
		/*
		addItem(serviceLocator, additions,
				"EasyShell Open 0",
				"com.tetrade.eclipse.plugins.easyshell.command.shellOpen",
				"com.tetrade.eclipse.plugins.easyshell.ParameterInstanceID",
				"0",
				EasyShellPlugin.IMAGE_OPEN_ID);
		addItem(serviceLocator, additions,
				"EasyShell Run",
				"com.tetrade.eclipse.plugins.easyshell.command.shellRun",
				null, null,
				EasyShellPlugin.IMAGE_RUN_ID);
		addItem(serviceLocator, additions,
				"EasyShell Explore",
				"com.tetrade.eclipse.plugins.easyshell.command.shellExplore",
				null, null,
				EasyShellPlugin.IMAGE_EXPLORE_ID);
		*/
		addItem(serviceLocator, additions,
				"Copy Path",
				"com.tetrade.eclipse.plugins.easyshell.command.copyPath",
				null, null,
				EasyShellPlugin.IMAGE_COPYPATH_ID);
	}

    private void addItem(IServiceLocator serviceLocator, IContributionRoot additions,
    					 String commandLabel, String commandId,
    					 String paramId, String paramValue, String commandImageId) {
		CommandContributionItemParameter param = new CommandContributionItemParameter(serviceLocator, "", commandId, SWT.PUSH);
		param.label = commandLabel;
		param.icon = EasyShellPlugin.getImageDescriptor(commandImageId);
		if (paramId != null) {
			Map<String, Object> commandParamametersMap = new HashMap<String, Object>();
			commandParamametersMap.put(paramId,  paramValue);
			param.parameters = commandParamametersMap;
		}
	    CommandContributionItem item = new CommandContributionItem(param);
	    item.setVisible(true);
	    additions.addContributionItem(item, null);
    }

}
