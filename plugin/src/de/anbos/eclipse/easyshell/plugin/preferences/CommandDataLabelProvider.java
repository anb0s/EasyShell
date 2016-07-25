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

package de.anbos.eclipse.easyshell.plugin.preferences;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.anbos.eclipse.easyshell.plugin.Activator;

public class CommandDataLabelProvider extends LabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        if(!(element instanceof CommandData)) {
            return null;
        }
        CommandData data = (CommandData)element;
        switch(columnIndex) {
            case 0:
                return new Image(null, Activator.getImageDescriptor(data.getCommandType().getIcon()).getImageData());
            default:
                return null;
        }
    }

    public String getColumnText(Object element, int columnIndex) {
        if(!(element instanceof CommandData)) {
            return "";
        }
        CommandData data = (CommandData)element;
        switch(columnIndex) {
            case 0:
                return data.getCommandType().getName();
            case 1:
                return data.getName();
            case 2:
                return data.getPresetType().getName();
            case 3:
            	return data.getCommand();
            default:
                return "";
        }
    }

}
