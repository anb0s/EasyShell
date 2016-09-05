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

import java.util.ArrayList;
import java.util.List;

public class MenuDataList extends ArrayList<MenuData> {

    /**
     *
     */
    private static final long serialVersionUID = 4274345194237037872L;

    public MenuDataList() {
    }

    public MenuDataList(List<MenuData> list) {
        addAll(list);
    }

    @Override
    public boolean add(MenuData e) {
        e.setPosition(this.size());
        return super.add(e);
    }

}
