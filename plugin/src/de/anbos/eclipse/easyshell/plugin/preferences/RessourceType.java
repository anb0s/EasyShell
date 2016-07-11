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

public enum RessourceType {
    ressourceTypeUnknown(-1, "Unknown"),
    ressourceTypeFile(0, "File"),
    ressourceTypeFolder(1, "Folder"),
    ressourceFileOrFolder(2, "File or Folder");
    // attributes
    private final int id;
    private final String name;
    // construct
    RessourceType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static RessourceType getFromId(int id) {
        RessourceType ret = ressourceTypeUnknown;
        for(int i = 0; i < RessourceType.values().length; i++) {
            if (RessourceType.values()[i].getId() == id) {
                ret = RessourceType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static RessourceType getFromName(String name) {
        RessourceType ret = ressourceTypeUnknown;
        for(int i = 0; i < RessourceType.values().length; i++) {
            if (RessourceType.values()[i].getName().equals(name)) {
                ret = RessourceType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static RessourceType getFromEnum(String name) {
        RessourceType ret = ressourceTypeUnknown;
        for(int i = 0; i < RessourceType.values().length; i++) {
            if (RessourceType.values()[i].toString().equals(name)) {
                ret = RessourceType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < RessourceType.values().length; i++) {
            if (RessourceType.values()[i] != ressourceTypeUnknown) {
                list.add(RessourceType.values()[i].getName());
            }
        }
        return list;
    }
}