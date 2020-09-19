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

public enum Version {
    // do not delete any versions and append new versions!
    vUnknown(-1, "Unknown"),
    v1_4(0, "v1.4.x"),
    v1_5(1, "v1.5.x"),
    v2_0_001(2, "v2.0 beta 1"),
    v2_0_002(3, "v2.0 beta 2"),
    v2_0_003(4, "v2.0 beta 3"),
    v2_0_004(5, "v2.0 beta 4"),
    v2_0_005(6, "v2.0 RC1 / RC2"),
    v2_0_006(7, "v2.0 RC3 / Final"),
    v2_1_001(8, "v2.1 alpha 1"),
    v2_1_002(9, "v2.1 alpha 2"),
    v2_1_003(10, "v2.1 beta 1 / 2"),
    v2_1_004(11, "v2.1 beta 3"),
    v2_1_005(12, "v2.1 beta 4 / RC1");
    // actual version is always the last one!
    public static Version actual =  Version.values()[Version.values().length-1];
    // attributes
    private final int id;
    private final String name;
    // construct
    Version(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static Version getFromId(int id) {
        Version ret = vUnknown;
        for(int i = 0; i < Version.values().length; i++) {
            if (Version.values()[i].getId() == id) {
                ret = Version.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Version getFromName(String name) {
        Version ret = vUnknown;
        for(int i = 0; i < Version.values().length; i++) {
            if (Version.values()[i].getName().equals(name)) {
                ret = Version.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Version getFromEnum(String name) {
        return Version.valueOf(name);
    }
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < Version.values().length; i++) {
            list.add(Version.values()[i].getName());
        }
        return list;
    }
}
