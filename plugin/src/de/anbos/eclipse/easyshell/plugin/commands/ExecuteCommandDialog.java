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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Constants;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataList;

public class ExecuteCommandDialog extends ElementListSelectionDialog {

    private IWorkbench workbench;

    public ExecuteCommandDialog(Shell parent, IWorkbench workbench, MenuDataList menuDataList, String title)
    {
        super(parent, new ExecuteCommandLabelProvider());
        setTitle(title);
        setHelpAvailable(false);
        setMessage("select commands to execute...");
        setIgnoreCase(true);
        setImage(getDefaultImage());
        setMatchEmptyString(true);
        setMultipleSelection(true);
        setImage(Activator.getImage(Constants.IMAGE_EASYSHELL));
        init(workbench, menuDataList);
    }

    @Override
    protected void okPressed() {
        executeCommandFromList();
        // close this dialog
        super.okPressed();
        //this.close();
    }

    void init(IWorkbench workbench, MenuDataList menuDataList) {
        this.workbench = workbench;
        Object[] elements = new MenuData[menuDataList.size()];
        for (int i=0;i<menuDataList.size();i++) {
            MenuData item = menuDataList.get(i);
            elements[i] = item;
        }
        setElements(elements);
    }

    private void executeCommandFromList() {
        Object[] objects = getSelectedElements();
        List<MenuData> menues = new ArrayList<MenuData>();
        for (Object element : objects) {
            menues.add((MenuData)element);
        }
        Utils.executeCommands(workbench, menues, true);
    }

}
