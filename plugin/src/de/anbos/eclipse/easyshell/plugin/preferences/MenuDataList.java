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
