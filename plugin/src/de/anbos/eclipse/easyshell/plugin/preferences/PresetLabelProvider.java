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

public class PresetLabelProvider extends LabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        if(!(element instanceof PresetData)) {
            return null;
        }
        PresetData data = (PresetData)element;
        switch(columnIndex) {
            case 0:
                return new Image(null, Activator.getImageDescriptor(data.getMenuIcon()).getImageData());
            default:
                return null;
        }
    }

    public String getColumnText(Object element, int columnIndex) {
        if(!(element instanceof PresetData)) {
            return ""; //$NON-NLS-1$
        }
        PresetData data = (PresetData)element;
        switch(columnIndex) {
            case 0:
                return data.getName();
            case 1:
            	return data.getValue();
            default:
                return ""; //$NON-NLS-1$
        }
    }

}
