/*
 * Copyright (C) 2014 - 2016 by Andre Bossert
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

package de.anbos.eclipse.easyshell.plugin.preferences;

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
        Quotes ret = quotesUnknown;
        for(int i = 0; i < Quotes.values().length; i++) {
            if (Quotes.values()[i].toString().equals(name)) {
                ret = Quotes.values()[i];
                break;
            }
        }
        return ret;
    }
}