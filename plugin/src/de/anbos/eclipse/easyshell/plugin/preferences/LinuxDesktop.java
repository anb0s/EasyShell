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

/**
 * Linux desktops.
 */
public enum LinuxDesktop {
    desktopUnknown(-1, "Unknown"),
    desktopXfce(0, "Xfce"),
    desktopKde(1, "KDE"),
    desktopGnome(2, "Gnome"),
    desktopCinnamon(3, "Cinnamon");
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
                break;
            }
        }
        return ret;
    }
    public static LinuxDesktop getFromName(String name) {
        LinuxDesktop ret = desktopUnknown;
        for(int i = 0; i < LinuxDesktop.values().length; i++) {
            if (LinuxDesktop.values()[i].getName().equals(name)) {
                ret = LinuxDesktop.values()[i];
                break;
            }
        }
        return ret;
    }
    public static LinuxDesktop getFromEnum(String name) {
        LinuxDesktop ret = desktopUnknown;
        for(int i = 0; i < LinuxDesktop.values().length; i++) {
            if (LinuxDesktop.values()[i].toString().equals(name)) {
                ret = LinuxDesktop.values()[i];
                break;
            }
        }
        return ret;
    }
}
