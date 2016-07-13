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

/**
 * Debug.
 */
public enum Debug {
    debugUnknown(-1, "Unknown"),
    debugNo(0, "No"),
    debugYes(1, "Yes");
    // attributes
    private final int id;
    private final String name;
    // construct
    Debug(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static Debug getFromId(int id) {
        Debug ret = debugUnknown;
        for(int i = 0; i < Debug.values().length; i++) {
            if (Debug.values()[i].getId() == id) {
                ret = Debug.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Debug getFromName(String name) {
        Debug ret = debugUnknown;
        for(int i = 0; i < Debug.values().length; i++) {
            if (Debug.values()[i].getName().equals(name)) {
                ret = Debug.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Debug getFromEnum(String name) {
        Debug ret = debugUnknown;
        for(int i = 0; i < Debug.values().length; i++) {
            if (Debug.values()[i].toString().equals(name)) {
                ret = Debug.values()[i];
                break;
            }
        }
        return ret;
    }
}