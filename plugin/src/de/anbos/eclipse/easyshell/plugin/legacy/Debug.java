/*******************************************************************************
 * Copyright (c) 2014 - 2018 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.legacy;

/**
 * Debug.
 */
public enum Debug {
    debugNo(0, "No"),
    debugYes(1, "Yes");
    // attributes
    private final int id;
    private final String mode;
    // construct
    Debug(int id, String mode) {
        this.id = id;
        this.mode = mode;
    }
    public int getId() {
        return id;
    }
    public String getMode() {
        return mode;
    }
    public static Debug getFromId(int id) {
        Debug ret = debugNo;
        for(int i = 0; i < Debug.values().length; i++) {
            if (Debug.values()[i].getId() == id) {
                ret = Debug.values()[i];
            }
        }
        return ret;
    }
}