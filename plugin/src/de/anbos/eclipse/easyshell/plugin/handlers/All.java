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
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import de.anbos.eclipse.easyshell.plugin.commands.CategoryPopupDialog;
import de.anbos.eclipse.easyshell.plugin.preferences.CommandDataStore;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataList;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataStore;
import de.anbos.eclipse.easyshell.plugin.types.Category;

public class All extends AbstractHandler {

    private CategoryPopupDialog dialog;

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
        MenuDataList list = getMenuDataList();
        if (list.size() > 0) {
            IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
            if (list.size() == 1) {
                CategoryPopupDialog.executeCommand(activePart, list.get(0));
            } else {
                //create and open a new dialog
                dialog = new CategoryPopupDialog(Display.getCurrent().getActiveShell(), activePart, list);
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
        return getMenuDataList(Category.categoryUnknown);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isHandled() {
        return true;
    }

}
