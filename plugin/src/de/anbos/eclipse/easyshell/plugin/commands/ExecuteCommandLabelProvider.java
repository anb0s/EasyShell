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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.exceptions.UnknownCommandID;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;

public class ExecuteCommandLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
        if(!(element instanceof MenuData)) {
            return null;
        }
        MenuData data = (MenuData)element;
        try {
			return new Image(null, Activator.getImageDescriptor(data.getCommandData().getCategory().getIcon()).getImageData());
		} catch (UnknownCommandID e) {
			e.logInternalError();
			return null;
		}
    }

    @Override
    public String getText(Object element) {
        if(!(element instanceof MenuData)) {
            return element.toString();
        }
        MenuData data = (MenuData)element;
        return data.getNameExpanded();
    }

}
