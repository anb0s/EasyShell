/**
 * Copyright (c) 2014-2022 Andre Bossert <anb0s@anbos.de>.
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
public enum CheckBox {
    unknown(-1, "Unknown"),
    no(0, "No"),
    yes(1, "Yes");
    // attributes
    private final int id;
    private final String name;
    // construct
    CheckBox(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static CheckBox getFromId(int id) {
        CheckBox ret = unknown;
        for(int i = 0; i < CheckBox.values().length; i++) {
            if (CheckBox.values()[i].getId() == id) {
                ret = CheckBox.values()[i];
                break;
            }
        }
        return ret;
    }
    public static CheckBox getFromName(String name) {
        CheckBox ret = unknown;
        for(int i = 0; i < CheckBox.values().length; i++) {
            if (CheckBox.values()[i].getName().equals(name)) {
                ret = CheckBox.values()[i];
                break;
            }
        }
        return ret;
    }
    public static CheckBox getFromEnum(String name) {
        return CheckBox.valueOf(name);
    }
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < CheckBox.values().length; i++) {
            if (CheckBox.values()[i] != unknown) {
                list.add(CheckBox.values()[i].getName());
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
