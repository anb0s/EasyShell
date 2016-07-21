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
 * Quotes.
 */
public enum Quotes {
    quotesUnknown(-1, "Unknown"),
    quotesNo(0, "No"),
    quotesSingle(1, "Single"),
    quotesDouble(2, "Double"),
    quotesAuto(3, "Double (automatic)"), // check if no quotes and space in string, then add
    quotesAutoSingle(4, "Single (automatic)"), // check if no quotes and space in string, then add
	quotesEscape(5, "Escape"); // check if no quotes and space in string, then escape
    // attributes
    private final int id;
    private final String name;
    // construct
    Quotes(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static Quotes getFromId(int id) {
        Quotes ret = quotesUnknown;
        for(int i = 0; i < Quotes.values().length; i++) {
            if (Quotes.values()[i].getId() == id) {
                ret = Quotes.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Quotes getFromName(String name) {
        Quotes ret = quotesUnknown;
        for(int i = 0; i < Quotes.values().length; i++) {
            if (Quotes.values()[i].getName().equals(name)) {
                ret = Quotes.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Quotes getFromEnum(String name) {
        return Quotes.valueOf(name);
    }
}