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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;

public class ExecuteCommandLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
        if(!(element instanceof MenuData)) {
            return null;
        }
        MenuData data = (MenuData)element;
        return Activator.getImage(data.getImageId());
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
