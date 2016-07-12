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

package de.anbos.eclipse.easyshell.plugin.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataStore;

public class DefineCommands extends ExtensionContributionFactory {

	public DefineCommands() {
	}

	@Override
	public void createContributionItems(IServiceLocator  serviceLocator,
			IContributionRoot additions) {

	    // load the preferences
	    MenuDataStore store = new MenuDataStore(Activator.getDefault().getPreferenceStore());
	    store.load();
	    List<MenuData> items = store.getEnabledCommandMenuDataList();
        for (MenuData item : items) {
            addItem(serviceLocator, additions,
                    item.getName(),
                    "de.anbos.eclipse.easyshell.plugin.commands.execute",
                    "de.anbos.eclipse.easyshell.plugin.commands.parameter.type",
                    item.getCommandData().getTypeAction(),
                    "de.anbos.eclipse.easyshell.plugin.commands.parameter.value",
                    item.getCommandData().getCommand(),
                    item.getCommandData().getTypeIcon());
        }
	}

    private void addItem(IServiceLocator serviceLocator, IContributionRoot additions,
    					 String commandLabel, String commandId,
    					 String paramId1, String paramValue1, String paramId2, String paramValue2, String commandImageId) {
		CommandContributionItemParameter param = new CommandContributionItemParameter(serviceLocator, "", commandId, SWT.PUSH);
		param.label = commandLabel;
		param.icon = Activator.getImageDescriptor(commandImageId);
		Map<String, Object> commandParamametersMap = new HashMap<String, Object>();
		commandParamametersMap.put(paramId1,  paramValue1);
		commandParamametersMap.put(paramId2,  paramValue2);
		param.parameters = commandParamametersMap;
	    CommandContributionItem item = new CommandContributionItem(param);
	    item.setVisible(true);
	    additions.addContributionItem(item, null);
    }
}
