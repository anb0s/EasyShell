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

public enum OS {
    osUnknown(0, "Unknown"),
    osWindows(1, "Windows"),
    osLinux(2, "Linux"),
    osMacOSX(3, "MAC OS X");
    // attributes
    private final int id;
    private final String name;
    // construct
    OS(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static OS getFromId(int id) {
        OS ret = osUnknown;
        for(int i = 0; i < OS.values().length; i++) {
            if (OS.values()[i].getId() == id) {
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
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < OS.values().length; i++) {
            list.add(OS.values()[i].getName());
        }
        return list;
    }
}
