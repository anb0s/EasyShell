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

public class MenuDataLabelProvider extends LabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        if(!(element instanceof MenuData)) {
            return null;
        }
        MenuData data = (MenuData)element;
        switch(columnIndex) {
            case 0:
                return Activator.getImage(data.getImageId());
            default:
                return null;
        }
    }

    public String getColumnText(Object element, int columnIndex) {
        if(!(element instanceof MenuData)) {
            return ""; //$NON-NLS-1$
        }
        MenuData data = (MenuData)element;
        switch(columnIndex) {
            case 0:
                return data.getNameExpanded();
            case 1:
                return data.getCommand();
            default:
                return ""; //$NON-NLS-1$
        }
    }

}
