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
public enum Debug {
    debugUnknown(-1, "Unknown"),
    debugNo(0, "No"),
    debugYes(1, "Yes");
    // attributes
    private final int id;
    private final String name;
    // construct
    Debug(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static Debug getFromId(int id) {
        Debug ret = debugUnknown;
        for(int i = 0; i < Debug.values().length; i++) {
            if (Debug.values()[i].getId() == id) {
                ret = Debug.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Debug getFromName(String name) {
        Debug ret = debugUnknown;
        for(int i = 0; i < Debug.values().length; i++) {
            if (Debug.values()[i].getName().equals(name)) {
                ret = Debug.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Debug getFromEnum(String name) {
        return Debug.valueOf(name);
    }
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < Debug.values().length; i++) {
            if (Debug.values()[i] != debugUnknown) {
                list.add(Debug.values()[i].getName());
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
