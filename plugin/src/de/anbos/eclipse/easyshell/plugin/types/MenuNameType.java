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

public enum MenuNameType {
    menuNameTypeUnknown(           -1, "Unknown", ""),
    menuNameTypeUser(               0, "User defined", "${easyshell:command_name}"),
    menuNameTypeDefaultApplication( 1, "Open with default application", "Open with default Application"),
    menuNameTypeOpenHere(           2, "Open <name> Here", "Open ${easyshell:command_name} Here"),
    menuNameTypeRunWith(            3, "Run with <name>", "Run with ${easyshell:command_name}"),
    menuNameTypeShowIn(             4, "Show in <name>", "Show in ${easyshell:command_name}"),
    menuNameTypeCopyToClipboard(    5, "Copy <name> to Clipboard", "Copy ${easyshell:command_name} to Clipboard"),
    menuNameTypeGeneric1(           6, "<category>: <name>", "${easyshell:command_category}: ${easyshell:command_name}"),
    menuNameTypeGeneric2(           7, "<category> with <name>", "${easyshell:command_category} with ${easyshell:command_name}"),
    menuNameTypeGeneric3(           9, "<category> with <os> <name>", "${easyshell:command_category} with ${easyshell:command_os} ${easyshell:command_name}");
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
