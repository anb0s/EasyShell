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
import de.anbos.eclipse.easyshell.plugin.exceptions.UnknownCommandID;

public class MenuDataLabelProvider extends LabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        if(!(element instanceof MenuData)) {
            return null;
        }
        MenuData data = (MenuData)element;
		try {
	        switch(columnIndex) {
	            case 0:
	                return new Image(null, Activator.getImageDescriptor(data.getCommandData().getCategory().getIcon()).getImageData());
	            default:
	                return null;
	        }
		} catch (UnknownCommandID e) {
			e.logInternalError();
			return null;
		}
    }

    public String getColumnText(Object element, int columnIndex) {
        if(!(element instanceof MenuData)) {
            return ""; //$NON-NLS-1$
        }
        MenuData data = (MenuData)element;
		try {
	        switch(columnIndex) {
	            case 0:
	                return data.getNameExpanded();
	            case 1:
	            	return data.getCommandData().getCommand();
	            default:
	                return ""; //$NON-NLS-1$
	        }
		} catch (UnknownCommandID e) {
			e.logInternalError();
			return null;
		}

    }

}
