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

public enum PresetType {
    presetTypeUnknown(-1, "Unknown", Constants.IMAGE_UNKNOWN, Constants.ACTION_UNKNOWN),
    presetTypeOpen(0, "Open", Constants.IMAGE_OPEN, Constants.ACTION_OPEN),
    presetTypeRun(1, "Run", Constants.IMAGE_RUN, Constants.ACTION_RUN),
    presetTypeExplore(2, "Explore", Constants.IMAGE_EXPLORE, Constants.ACTION_EXPLORE),
    presetTypeClipboard(3, "Clipboard", Constants.IMAGE_CLIPBOARD, Constants.ACTION_CLIPBOARD),
    presetTypeOther(4, "Other", Constants.IMAGE_OTHER, Constants.ACTION_OTHER);
    // attributes
    private final int id;
    private final String name;
    private final String icon;
    private final String action;
    // construct
    PresetType(int id, String name, String icon, String action) {
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
    public static PresetType getFromId(int id) {
        PresetType ret = presetTypeUnknown;
        for(int i = 0; i < PresetType.values().length; i++) {
            if (PresetType.values()[i].getId() == id) {
                ret = PresetType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static PresetType getFromName(String name) {
        PresetType ret = presetTypeUnknown;
        for(int i = 0; i < PresetType.values().length; i++) {
            if (PresetType.values()[i].getName().equals(name)) {
                ret = PresetType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static PresetType getFromAction(String action) {
        PresetType ret = presetTypeUnknown;
        for(int i = 0; i < PresetType.values().length; i++) {
            if (PresetType.values()[i].getAction().equals(action)) {
                ret = PresetType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < PresetType.values().length; i++) {
            if (PresetType.values()[i] != presetTypeUnknown) {
                list.add(PresetType.values()[i].getName());
            }
        }
        return list;
    }
}