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
 * Quotes.
 */
public enum Quotes {
    quotesNo(0, "No"),
    quotesSingle(1, "Single"),
    quotesDouble(2, "Double"),
    quotesAuto(3, "Double (automatic)"), // check if no quotes and space in string, then add
    quotesAutoSingle(4, "Single (automatic)"), // check if no quotes and space in string, then add
    quotesEscape(5, "Escape"); // check if no quotes and space in string, then escape
    // attributes
    private final int id;
    private final String mode;
    // construct
    Quotes(int id, String mode) {
        this.id = id;
        this.mode = mode;
    }
    public int getId() {
        return id;
    }
    public String getMode() {
        return mode;
    }
    public static Quotes getFromId(int id) {
        Quotes ret = quotesNo;
        for(int i = 0; i < Quotes.values().length; i++) {
            if (Quotes.values()[i].getId() == id) {
                ret = Quotes.values()[i];
            }
        }
        return ret;
    }
}
