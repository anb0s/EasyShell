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

package de.anbos.eclipse.easyshell.plugin.types;

import java.util.ArrayList;
import java.util.List;

public enum ResourceType {
    resourceTypeUnknown(-1, "Unknown"),
    resourceTypeFile(0, "File"),
    resourceTypeDirectory(1, "Directory"),
    resourceTypeFileOrDirectory(2, "File or Directory");
    // attributes
    private final int id;
    private final String name;
    // construct
    ResourceType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static ResourceType getFromId(int id) {
        ResourceType ret = resourceTypeUnknown;
        for(int i = 0; i < ResourceType.values().length; i++) {
            if (ResourceType.values()[i].getId() == id) {
                ret = ResourceType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static ResourceType getFromName(String name) {
        ResourceType ret = resourceTypeUnknown;
        for(int i = 0; i < ResourceType.values().length; i++) {
            if (ResourceType.values()[i].getName().equals(name)) {
                ret = ResourceType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static ResourceType getFromEnum(String name) {
        return ResourceType.valueOf(name);
    }
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < ResourceType.values().length; i++) {
            if (ResourceType.values()[i] != resourceTypeUnknown) {
                list.add(ResourceType.values()[i].getName());
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