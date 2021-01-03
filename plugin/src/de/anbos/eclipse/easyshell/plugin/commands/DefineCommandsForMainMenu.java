/**
 * Copyright (c) 2014-2021 Andre Bossert <anb0s@anbos.de>.
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

import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import de.anbos.eclipse.easyshell.plugin.preferences.GeneralDataStore;
import de.anbos.eclipse.easyshell.plugin.types.CheckBox;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

public class DefineCommandsForMainMenu extends DefineCommands {

    public DefineCommandsForMainMenu() {
    }

    @Override
    public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
        if (GeneralDataStore.instance().getData().getMenuMain() != CheckBox.yes) {
            return;
        }
        createContributionItemsForResType(ResourceType.resourceTypeFileOrDirectory, serviceLocator, additions);
    }

    @Override
    public boolean showMenuImage() {
        return false;
    }

}
