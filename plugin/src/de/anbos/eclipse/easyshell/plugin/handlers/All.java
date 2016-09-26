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

package de.anbos.eclipse.easyshell.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.commands.ExecuteCommandPopup;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.commands.ExecuteCommandDialog;
import de.anbos.eclipse.easyshell.plugin.preferences.CommandDataStore;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataList;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataStore;
import de.anbos.eclipse.easyshell.plugin.types.Category;

public class All extends AbstractHandler {

    private Window dialog;
    private MenuDataList list;
    private boolean usePopup = true;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        // close the old dialog
        if (dialog != null)
        {
            dialog.close();
            dialog = null;
        }
        // load the preferences
        list = getMenuDataList();
        if (list.size() > 0) {
            IWorkbenchWindow workbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
            if (list.size() == 1) {
                Utils.executeCommand(workbenchWindow.getWorkbench(), list.get(0), false);
            } else {
                //create and open a new dialog
                if (usePopup) {
                    dialog = new ExecuteCommandPopup(workbenchWindow.getShell(), workbenchWindow.getWorkbench(), list, getTitle());
                } else {
                    dialog = new ExecuteCommandDialog(workbenchWindow.getShell(), workbenchWindow.getWorkbench(), list, getTitle());
                }
                dialog.open();
            }
        }
        return null;
    }

    public MenuDataList getMenuDataList(Category category) {
        CommandDataStore.instance().load();
        MenuDataStore.instance().load();
        MenuDataList menuDataList = MenuDataStore.instance().getEnabledCommandMenuDataListByCategory(category);
        return menuDataList;
    }

    public MenuDataList getMenuDataList() {
        return getMenuDataList(getCategory());
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
