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
 * Tokenizer.
 */
public enum EasyShellTokenizer {
    EasyShellTokenizerNo(0, "No"),
    EasyShellTokenizerYes(1, "Yes");
    // attributes
    private final int id;
    private final String mode;
    // construct
    EasyShellTokenizer(int id, String mode) {
        this.id = id;
        this.mode = mode;
    }
    public int getId() {
        return id;
    }
    public String getMode() {
        return mode;
    }
    public static EasyShellTokenizer getFromId(int id) {
        EasyShellTokenizer ret = EasyShellTokenizerYes;
        for(int i = 0; i < EasyShellTokenizer.values().length; i++) {
            if (EasyShellTokenizer.values()[i].getId() == id) {
                ret = EasyShellTokenizer.values()[i];
            }
        }
        return ret;
    }
};
