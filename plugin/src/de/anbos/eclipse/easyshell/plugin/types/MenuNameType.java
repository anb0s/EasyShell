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

public enum MenuNameType {
    menuNameTypeUnknown(-1, "Unknown", ""),
    menuNameTypeUser(0, "User defined", "${easyshell:command_name}"),
    menuNameTypeGeneric1(1, "<type>: <name>", "${easyshell:command_type}: ${easyshell:command_name}"),
    menuNameTypeGeneric2(2, "<type> with <name>", "${easyshell:command_type} with ${easyshell:command_name}"),
    menuNameTypeGeneric3(3, "<type> with <os> <name>", "${easyshell:command_type} with ${easyshell:command_os} ${easyshell:command_name}");
    // attributes
    private final int id;
    private final String name;
    private final String pattern;
    // construct
    MenuNameType(int id, String name, String pattern) {
        this.id = id;
        this.name = name;
        this.pattern = pattern;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPattern() {
        return pattern;
    }
    public static MenuNameType getFromId(int id) {
        MenuNameType ret = menuNameTypeUnknown;
        for(int i = 0; i < MenuNameType.values().length; i++) {
            if (MenuNameType.values()[i].getId() == id) {
                ret = MenuNameType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static MenuNameType getFromName(String name) {
        MenuNameType ret = menuNameTypeUnknown;
        for(int i = 0; i < MenuNameType.values().length; i++) {
            if (MenuNameType.values()[i].getName().equals(name)) {
                ret = MenuNameType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static MenuNameType getFromEnum(String name) {
        return MenuNameType.valueOf(name);
    }
    public static MenuNameType getFromPattern(String pattern) {
        MenuNameType ret = menuNameTypeUnknown;
        for(int i = 0; i < MenuNameType.values().length; i++) {
            if (MenuNameType.values()[i].getPattern().equals(pattern)) {
                ret = MenuNameType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < MenuNameType.values().length; i++) {
            if (MenuNameType.values()[i] != menuNameTypeUnknown) {
                list.add(MenuNameType.values()[i].getName());
            }
        }
        return list;
    }
    public static List<MenuNameType> getAsList() {
        List<MenuNameType> list = new ArrayList<MenuNameType>();
        for(int i = 0; i < MenuNameType.values().length; i++) {
            if (MenuNameType.values()[i] != menuNameTypeUnknown) {
                list.add(MenuNameType.values()[i]);
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
    public static MenuNameType[] getAsArray() {
        List<MenuNameType> list = getAsList();
        MenuNameType[] arr = new MenuNameType[list.size()];
        for (int i=0;i<list.size();i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
    public static List<String> getPatternsAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < MenuNameType.values().length; i++) {
            if (MenuNameType.values()[i] != menuNameTypeUnknown) {
                list.add(MenuNameType.values()[i].getPattern());
            }
        }
        return list;
    }
}