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
                    addItem(serviceLocator, additions, item.getNameExpanded(), item.getCommand(),
                            "de.anbos.eclipse.easyshell.plugin.commands.execute",
                            Utils.getParameterMapFromMenuData(item), item.getImageId(),
                            true);
                }
            } catch (UnknownCommandID e) {
                e.logInternalError();
            }
        }
    }

    private void addItem(IServiceLocator serviceLocator, IContributionRoot additions, String commandLabel, String commandToolTip,
            String commandId, Map<String, Object> commandParamametersMap, String commandImageId, boolean visible) {
        CommandContributionItemParameter param = new CommandContributionItemParameter(serviceLocator, "", commandId,
                SWT.PUSH);
        param.label = commandLabel;
        param.tooltip = commandToolTip;
        param.icon = Activator.getImageDescriptor(commandImageId);
        param.parameters = commandParamametersMap;
        CommandContributionItem item = new CommandContributionItem(param);
        item.setVisible(visible);
        additions.addContributionItem(item, null);
    }
}
