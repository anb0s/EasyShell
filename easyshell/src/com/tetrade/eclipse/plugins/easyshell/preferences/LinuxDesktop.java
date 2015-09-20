/*
 * Copyright (C) 2014 by Andre Bossert
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.tetrade.eclipse.plugins.easyshell.preferences;

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
