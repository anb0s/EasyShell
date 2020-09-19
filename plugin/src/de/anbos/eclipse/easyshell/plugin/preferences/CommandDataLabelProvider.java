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
                return Activator.getImage(data.getImageId());
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
                return data.getCategory().getName();
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
