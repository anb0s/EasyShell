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

package de.anbos.eclipse.easyshell.plugin.legacy;

/**
 * Linux desktops.
 */
public enum LinuxDesktop {
    desktopUnknown(0, "Unknown"),
    desktopKde(1, "KDE"),
    desktopGnome(2, "Gnome"),
    desktopCinnamon(3, "Cinnamon"),
    desktopCde(4, "CDE"),
    desktopXfce(5, "Xfce");
    // attributes
    private final int id;
    private final String name;
    // construct
    LinuxDesktop(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static LinuxDesktop getFromId(int id) {
        LinuxDesktop ret = desktopUnknown;
        for(int i = 0; i < LinuxDesktop.values().length; i++) {
            if (LinuxDesktop.values()[i].getId() == id) {
                ret = LinuxDesktop.values()[i];
            }
        }
        return ret;
    }
}
