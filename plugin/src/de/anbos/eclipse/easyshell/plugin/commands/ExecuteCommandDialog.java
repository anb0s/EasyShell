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

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Constants;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataList;

public class ExecuteCommandDialog extends ElementListSelectionDialog {

    private IWorkbenchPart activePart;

    public ExecuteCommandDialog(Shell parent, IWorkbenchPart activePart, MenuDataList menuDataList, String title)
    {
        super(parent, new ExecuteCommandLabelProvider());
        setTitle(title);
        setHelpAvailable(false);
        setMessage("select commands to execute...");
        setIgnoreCase(true);
        setImage(getDefaultImage());
        setMatchEmptyString(true);
        setMultipleSelection(true);
        setImage(new Image(null, Activator.getImageDescriptor(Constants.IMAGE_EASYSHELL).getImageData()));
        init(activePart, menuDataList);
    }

    @Override
    protected void okPressed() {
        executeCommandFromList();
        super.okPressed();
        //close();
    }

    void init(IWorkbenchPart activePart, MenuDataList menuDataList) {
        this.activePart = activePart;
        Object[] elements = new MenuData[menuDataList.size()];
        for (int i=0;i<menuDataList.size();i++) {
            MenuData item = menuDataList.get(i);
            elements[i] = item;
        }
        setElements(elements);
    }

    private void executeCommandFromList() {
        Object[] elements = getSelectedElements();
        for (Object element : elements) {
            Utils.executeCommand(activePart, (MenuData)element);
        }
    }

}
