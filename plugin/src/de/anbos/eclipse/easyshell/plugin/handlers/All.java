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

package de.anbos.eclipse.easyshell.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.ResourceUtils;
import de.anbos.eclipse.easyshell.plugin.actions.ActionDelegate;
import de.anbos.eclipse.easyshell.plugin.commands.ExecuteCommandPopup;
import de.anbos.eclipse.easyshell.plugin.exceptions.UnknownCommandID;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.commands.ExecuteCommandDialog;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataList;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataStore;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

public class All extends AbstractHandler {

    private Window dialog;
    private MenuDataList list;
    private boolean usePopup = true;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // get resource type
        IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
        ActionDelegate action = ResourceUtils.getActionExactResourceType(activePart,
                ResourceType.resourceTypeFileOrDirectory);
        if (action != null) {
            // load the preferences
            list = getMenuDataList(action.getCommonResourceType());
            if (list.size() > 0) {
                IWorkbenchWindow workbenchWindow = activePart.getSite().getWorkbenchWindow();
                if (list.size() == 1) {
                    Utils.executeCommand(workbenchWindow.getWorkbench(), list.get(0), false);
                } else {
                    // create and open a new dialog
                    // close the old dialog
                    if (dialog != null) {
                        dialog.close();
                        dialog = null;
                    }
                    if (usePopup) {
                        dialog = new ExecuteCommandPopup(workbenchWindow.getShell(), workbenchWindow.getWorkbench(),
                                list, getTitle());
                    } else {
                        dialog = new ExecuteCommandDialog(workbenchWindow.getShell(), workbenchWindow.getWorkbench(),
                                list, getTitle());
                    }
                    dialog.open();
                }
            }
        }
        return null;
    }

    public MenuDataList getMenuDataList(Category category) {
        MenuDataList menuDataList = MenuDataStore.instance().getEnabledCommandMenuDataListByCategory(category);
        return menuDataList;
    }

    public MenuDataList getMenuDataList(ResourceType resTypeWanted) {
        MenuDataList menuDataList = getMenuDataList(getCategory());
        MenuDataList menuDataListSupported = new MenuDataList();
        for (MenuData menuData : menuDataList) {
            ResourceType resTypeSupported;
            try {
                resTypeSupported = menuData.getCommandData().getResourceType();
                if ((resTypeSupported == ResourceType.resourceTypeFileOrDirectory)
                        || (resTypeSupported == resTypeWanted)) {
                    menuDataListSupported.add(menuData);
                }
            } catch (UnknownCommandID e) {
                e.logInternalError();
            }
        }
        return menuDataListSupported;
    }

    public Category getCategory() {
        return Category.categoryUnknown;
    }

    public String getTitle() {
        String postfix = getCategory() == Category.categoryUnknown ? "" : " - " + getCategory().getName();
        return Activator.getResourceString("easyshell.plugin.name") + postfix;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isHandled() {
        return true;
    }

    public void setUsePopup(boolean usePopup) {
        this.usePopup = usePopup;
    }

}
