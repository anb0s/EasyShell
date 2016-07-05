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

public enum CommandType {
    commandTypeUnknown(-1, "Unknown", Constants.IMAGE_UNKNOWN, Constants.ACTION_UNKNOWN),
    commandTypeOpen(0, "Open", Constants.IMAGE_OPEN, Constants.ACTION_OPEN),
    commandTypeRun(1, "Run", Constants.IMAGE_RUN, Constants.ACTION_RUN),
    commandTypeExplore(2, "Explore", Constants.IMAGE_EXPLORE, Constants.ACTION_EXPLORE),
    commandTypeClipboard(3, "Clipboard", Constants.IMAGE_CLIPBOARD, Constants.ACTION_CLIPBOARD),
    commandTypeOther(4, "Other", Constants.IMAGE_OTHER, Constants.ACTION_OTHER);
    // attributes
    private final int id;
    private final String name;
    private final String icon;
    private final String action;
    // construct
    CommandType(int id, String name, String icon, String action) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.action = action;
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
    public static List<String> getIconsAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < CommandType.values().length; i++) {
            if (CommandType.values()[i] != commandTypeUnknown) {
                list.add(CommandType.values()[i].getIcon());
            }
        }
        return list;
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