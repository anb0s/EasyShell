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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.anbos.eclipse.easyshell.plugin.Constants;

public enum Category {
    categoryUnknown(-1, "Unknown", Constants.IMAGE_NONE),
    categoryDefault(0, "Default", Constants.IMAGE_DEFAULT),
    categoryOpen(1, "Open", Constants.IMAGE_OPEN),
    categoryRun(2, "Run", Constants.IMAGE_RUN),
    categoryExplore(3, "Explore", Constants.IMAGE_EXPLORE),
    categoryClipboard(4, "Clipboard", Constants.IMAGE_CLIPBOARD),
    categoryUser(5, "User", Constants.IMAGE_USER);
    // attributes
    private final int id;
    private final String name;
    private final String imageId;
    // construct
    Category(int id, String name, String imageId) {
        this.id = id;
        this.name = name;
        this.imageId = imageId;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getImageId() {
        return imageId;
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
        map.put("commandTypeOther", categoryUser);
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
    public static List<String> getImageIdsAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < Category.values().length; i++) {
            if (Category.values()[i] != categoryUnknown) {
                list.add(Category.values()[i].getImageId());
            }
        }
        return list;
    }
}
