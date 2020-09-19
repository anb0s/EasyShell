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

public enum PresetType {
    presetUnknown(-1, "Unknown"),
    presetPlugin(0, "Plugin"),
    presetUser(1, "User"),
    presetPluginModify(2, "Plugin+User");
    // attributes
    private final int id;
    private final String name;
    // construct
    PresetType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static PresetType getFromId(int id) {
        PresetType ret = presetUnknown;
        for(int i = 0; i < PresetType.values().length; i++) {
            if (PresetType.values()[i].getId() == id) {
                ret = PresetType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static PresetType getFromName(String name) {
        PresetType ret = presetUnknown;
        for(int i = 0; i < PresetType.values().length; i++) {
            if (PresetType.values()[i].getName().equals(name)) {
                ret = PresetType.values()[i];
                break;
            }
        }
        return ret;
    }
    public static PresetType getFromEnum(String name) {
        return PresetType.valueOf(name);
    }
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < PresetType.values().length; i++) {
            list.add(PresetType.values()[i].getName());
        }
        return list;
    }
}
