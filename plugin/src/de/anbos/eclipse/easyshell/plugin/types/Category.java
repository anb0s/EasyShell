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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.anbos.eclipse.easyshell.plugin.Constants;

public enum Category {
    categoryUnknown(-1, "Unknown", Constants.IMAGE_UNKNOWN),
    categoryDefault(0, "Default", Constants.IMAGE_DEFAULT),
    categoryOpen(1, "Open", Constants.IMAGE_OPEN),
    categoryRun(2, "Run", Constants.IMAGE_RUN),
    categoryExplore(3, "Explore", Constants.IMAGE_EXPLORE),
    categoryClipboard(4, "Clipboard", Constants.IMAGE_CLIPBOARD),
    categoryOther(5, "Other", Constants.IMAGE_OTHER);
    // attributes
    private final int id;
    private final String name;
    private final String icon;
    // construct
    Category(int id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getIcon() {
        return icon;
    }
    public static Category getFromId(int id) {
        Category ret = categoryUnknown;
        for(int i = 0; i < Category.values().length; i++) {
            if (Category.values()[i].getId() == id) {
                ret = Category.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Category getFromName(String name) {
        Category ret = categoryUnknown;
        for(int i = 0; i < Category.values().length; i++) {
            if (Category.values()[i].getName().equals(name)) {
                ret = Category.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Category getFromEnum(String name) {
        return Category.valueOf(name);
    }
    public static Category getFromDeprecatedCommandTypeEnum(String name) {
        // set mapping
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("commandTypeOpen", categoryOpen);
        map.put("commandTypeRun", categoryRun);
        map.put("commandTypeExplore", categoryExplore);
        map.put("commandTypeClipboard", categoryClipboard);
        map.put("commandTypeOther", categoryOther);
        // find the key
        for(String key: map.keySet()) {
            if (name.equals(key)) {
                return (Category)map.get(key);
            }
        }
        return categoryUnknown;
    }

    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < Category.values().length; i++) {
            if (Category.values()[i] != categoryUnknown) {
                list.add(Category.values()[i].getName());
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
    public static List<String> getIconsAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < Category.values().length; i++) {
            if (Category.values()[i] != categoryUnknown) {
                list.add(Category.values()[i].getIcon());
            }
        }
        return list;
    }
}