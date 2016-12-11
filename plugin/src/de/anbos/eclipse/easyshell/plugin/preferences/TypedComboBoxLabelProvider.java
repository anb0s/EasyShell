/*******************************************************************************
 * Copyright (c) 2014 - 2016 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *    
 *    adapted code from:
 *    http://stackoverflow.com/questions/12511959/is-there-swt-combo-box-with-any-object-as-data-and-labelprovider-for-display
 *    
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.preferences;

import org.eclipse.swt.graphics.Image;

public interface TypedComboBoxLabelProvider<T> {

    public String getSelectedLabel(T element);

    public String getListLabel(T element);
    
    public Image getImage(T element);

}
