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

package de.anbos.eclipse.easyshell.plugin.legacy;

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
