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

package de.anbos.eclipse.easyshell.plugin.handlers;

import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataList;
import de.anbos.eclipse.easyshell.plugin.types.Category;

public class User extends All {

    @Override
    public MenuDataList getMenuDataList() {
        return super.getMenuDataList(Category.categoryUser);
    }

}
