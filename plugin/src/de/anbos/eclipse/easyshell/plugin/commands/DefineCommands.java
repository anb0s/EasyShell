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

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;
import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.ResourceUtils;
import de.anbos.eclipse.easyshell.plugin.Constants;
import de.anbos.eclipse.easyshell.plugin.exceptions.UnknownCommandID;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataList;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataStore;

import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

public class DefineCommands extends ExtensionContributionFactory {

    public DefineCommands() {
    }

    @Override
    public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
        IWorkbenchPart activePart = serviceLocator.getService(IWorkbenchPart.class);
        boolean isResourceNavigator = false;
        isResourceNavigator = activePart instanceof org.eclipse.ui.views.navigator.ResourceNavigator;
        if ((isResourceNavigator == isForResourceNavigator())) {
            ResourceType resType = ResourceUtils.getCommonResourceType(activePart);
            if (resType != null) {
                createContributionItemsForResType(resType, serviceLocator, additions);
            }
        }
    }

    public boolean isForResourceNavigator() {
        return false;
    }

    private void createContributionItemsForResType(ResourceType resType, IServiceLocator serviceLocator, IContributionRoot additions) {
        MenuManager submenu = new MenuManager("EasyShell", Activator.getImageDescriptor(Constants.IMAGE_EASYSHELL), "de.anbos.eclipse.easyshell.plugin.menu");
        MenuDataList items = MenuDataStore.instance().getEnabledCommandMenuDataList();
        for (MenuData item : items) {
            ResourceType resTypeSupported;
            try {
                resTypeSupported = item.getCommandData().getResourceType();
                if ((resTypeSupported == ResourceType.resourceTypeFileOrDirectory)
                        || (resTypeSupported == resType)) {
                    addItem(serviceLocator, submenu, item.getNameExpanded(), item.getCommand(),
                            "de.anbos.eclipse.easyshell.plugin.commands.execute",
                            Utils.getParameterMapFromMenuData(item), item.getImageId(),
                            true);
                }
            } catch (UnknownCommandID e) {
                e.logInternalError();
            }
        }
        additions.addContributionItem(submenu, null);
    }

    private void addItem(IServiceLocator serviceLocator, IContributionManager submenu, String commandLabel, String commandToolTip,
            String commandId, Map<String, Object> commandParamametersMap, String commandImageId, boolean visible) {
        CommandContributionItemParameter param = new CommandContributionItemParameter(serviceLocator, "", commandId,
                SWT.PUSH);
        param.label = commandLabel;
        param.tooltip = commandToolTip;
        param.icon = Activator.getImageDescriptor(commandImageId);
        param.parameters = commandParamametersMap;
        CommandContributionItem item = new CommandContributionItem(param);
        item.setVisible(visible);
        submenu.add(item);
    }

}
