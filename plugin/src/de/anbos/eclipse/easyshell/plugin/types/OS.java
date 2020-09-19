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

public enum OS {
    osUnknown("unknown", "Unknown"),
    osWindows("windows", "Windows"),
    osLinux("linux", "Linux"),
    osMacOSX("macosx", "MAC OS X"),
    osUnix("unix", "Unix"); // all Unix based, e.g. Linux and Max OS X etc.
    // attributes
    private final String id;
    private final String name;
    // construct
    OS(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static OS getFromId(String id) {
        OS ret = osUnknown;
        for(int i = 0; i < OS.values().length; i++) {
            if (OS.values()[i].getId().equals(id)) {
                ret = OS.values()[i];
                break;
            }
        }
        return ret;
    }
    public static OS getFromName(String name) {
        OS ret = osUnknown;
        for(int i = 0; i < OS.values().length; i++) {
            if (OS.values()[i].getName().equals(name)) {
                ret = OS.values()[i];
                break;
            }
        }
        return ret;
    }
    public static OS getFromEnum(String name) {
        return OS.valueOf(name);
    }
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < OS.values().length; i++) {
            list.add(OS.values()[i].getName());
        }
        return list;
    }
}
