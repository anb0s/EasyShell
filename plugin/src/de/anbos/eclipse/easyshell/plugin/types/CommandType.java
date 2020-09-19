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

public enum CommandType {
    commandTypeUnknown(-1, "Unknown", Constants.ACTION_UNKNOWN),
    commandTypeExecute(0, "Execute", Constants.ACTION_EXECUTE),
    commandTypeClipboard(1, "Clipboard", Constants.ACTION_CLIPBOARD);
    // attributes
    private final int id;
    private final String name;
    private final String action;
    // construct
    CommandType(int id, String name, String action) {
        this.id = id;
        this.name = name;
        this.action = action;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAction() {
        return action;
    }
    public static CommandType getFromId(int id) {
        CommandType ret = commandTypeUnknown;
        for(int i = 0; i < CommandType.values().length; i++) {
            if (CommandType.values()[i].getId() == id) {
                ret = CommandType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static CommandType getFromName(String name) {
        CommandType ret = commandTypeUnknown;
        for(int i = 0; i < CommandType.values().length; i++) {
            if (CommandType.values()[i].getName().equals(name)) {
                ret = CommandType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static CommandType getFromEnum(String name) {
        return CommandType.valueOf(name);
    }
    public static CommandType getFromDeprecatedCommandTypeEnum(String name) {
        // set mapping
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("commandTypeOpen", commandTypeExecute);
        map.put("commandTypeRun", commandTypeExecute);
        map.put("commandTypeExplore", commandTypeExecute);
        map.put("commandTypeClipboard", commandTypeClipboard);
        map.put("commandTypeOther", commandTypeExecute);
        // find the key
        for(String key: map.keySet()) {
            if (name.equals(key)) {
                return (CommandType)map.get(key);
            }
        }
        return commandTypeUnknown;
    }
    public static CommandType getFromAction(String action) {
        CommandType ret = commandTypeUnknown;
        for(int i = 0; i < CommandType.values().length; i++) {
            if (CommandType.values()[i].getAction().equals(action)) {
                ret = CommandType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < CommandType.values().length; i++) {
            if (CommandType.values()[i] != commandTypeUnknown) {
                list.add(CommandType.values()[i].getName());
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
    public static List<String> getActionsAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < CommandType.values().length; i++) {
            if (CommandType.values()[i] != commandTypeUnknown) {
                list.add(CommandType.values()[i].getAction());
            }
        }
        return list;
    }
}
