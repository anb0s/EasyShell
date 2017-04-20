/*******************************************************************************
 * Copyright (c) 2014 - 2017 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.commands;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.exceptions.UnknownCommandID;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataList;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataStore;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

public class DefineCommands extends ExtensionContributionFactory {

	public DefineCommands() {
	}

	public ResourceType getWantedResourceType() {
		return ResourceType.resourceTypeFileOrDirectory;
	}

	@Override
	public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
		MenuDataList items = MenuDataStore.instance().getEnabledCommandMenuDataList();
		for (MenuData item : items) {
			ResourceType resTypeWanted = getWantedResourceType();
			ResourceType resTypeSupported;
			try {
				resTypeSupported = item.getCommandData().getResourceType();
				if ((resTypeSupported == ResourceType.resourceTypeFileOrDirectory)
						|| (resTypeSupported == resTypeWanted)) {
					addItem(serviceLocator, additions, item.getNameExpanded(),
							"de.anbos.eclipse.easyshell.plugin.commands.execute",
							Utils.getParameterMapFromMenuData(item), item.getImageId(),
							true);
				}
			} catch (UnknownCommandID e) {
				e.logInternalError();
			}
		}
	}

	private void addItem(IServiceLocator serviceLocator, IContributionRoot additions, String commandLabel,
			String commandId, Map<String, Object> commandParamametersMap, String commandImageId, boolean visible) {
		CommandContributionItemParameter param = new CommandContributionItemParameter(serviceLocator, "", commandId,
				SWT.PUSH);
		param.label = commandLabel;
		param.icon = Activator.getImageDescriptor(commandImageId);
		param.parameters = commandParamametersMap;
		CommandContributionItem item = new CommandContributionItem(param);
		item.setVisible(visible);
		additions.addContributionItem(item, null);
	}
}
