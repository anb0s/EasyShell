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

package com.tetrade.eclipse.plugins.easyshell.preferences;

/**
 * Tokenizer.
 */
public enum Tokenizer {
	EasyShellTokenizerNo(0, "No"),
	EasyShellTokenizerYes(1, "Yes");
    // attributes
    private final int id;
    private final String mode;
    // construct
    Tokenizer(int id, String mode) {
        this.id = id;
        this.mode = mode;
    }
    public int getId() {
        return id;
    }
    public String getMode() {
        return mode;
    }
    public static Tokenizer getFromId(int id) {
    	Tokenizer ret = EasyShellTokenizerYes;
        for(int i = 0; i < Tokenizer.values().length; i++) {
            if (Tokenizer.values()[i].getId() == id) {
                ret = Tokenizer.values()[i];
            }
        }
        return ret;
    }
};
