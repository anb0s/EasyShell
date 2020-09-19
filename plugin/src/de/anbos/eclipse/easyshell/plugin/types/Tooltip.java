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

package de.anbos.eclipse.easyshell.plugin.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Debug.
 */
public enum Tooltip {
    tooltipUnknown(-1, "Unknown"),
    tooltipNo(0, "No"),
    tooltipYes(1, "Yes");
    // attributes
    private final int id;
    private final String name;
    // construct
    Tooltip(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static Tooltip getFromId(int id) {
        Tooltip ret = tooltipUnknown;
        for(int i = 0; i < Tooltip.values().length; i++) {
            if (Tooltip.values()[i].getId() == id) {
                ret = Tooltip.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Tooltip getFromName(String name) {
        Tooltip ret = tooltipUnknown;
        for(int i = 0; i < Tooltip.values().length; i++) {
            if (Tooltip.values()[i].getName().equals(name)) {
                ret = Tooltip.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Tooltip getFromEnum(String name) {
        return Tooltip.valueOf(name);
    }
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < Tooltip.values().length; i++) {
            if (Tooltip.values()[i] != tooltipUnknown) {
                list.add(Tooltip.values()[i].getName());
            }
        }
        return list;
    }
    public static String[] getNamesAsArray() {
        List<String> list = getNamesAsList();
        String[] arr = new String[list.size()];
        for (int i=0;i<list.size();i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
}
