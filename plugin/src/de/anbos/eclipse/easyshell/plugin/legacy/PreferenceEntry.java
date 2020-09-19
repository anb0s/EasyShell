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
 * Strings for preference store.
 */
public enum PreferenceEntry {
    preferenceTargetOpen(0, "targetPreference"),
    preferenceTargetRun(1, "targetRunPreference"),
    preferenceTargetExplore(2, "targetExplorePreference"),
    preferenceTargetCopyPath(3, "targetCopyPathPreference"),
    preferenceListId(4, "listPreference"),
    preferenceListString(5, "IdStr"),
    preferenceQuotes(6, "QuotesStr"),
    preferenceDebug(7, "DebugStr"),
    preferenceTokenizer(8, "TokenizerStr"),
    preferenceTargetEnabled(9, "targetEnabled");
    // attributes
    private final int id;
    private final String preferenceString;
    // construct
    PreferenceEntry(int id, String pref) {
        this.id = id;
        this.preferenceString = pref;
    }
    public int getId() {
        return id;
    }
    public String getString() {
        return preferenceString;
    }
    public String getString(int instId) {
        if (instId == 0) {
            return getString();
        } else {
            return getString() + (new Integer(instId)).toString();
        }
    }
    public static PreferenceEntry getFromId(int id) {
        PreferenceEntry ret = preferenceTargetOpen;
        for(int i = 0; i < PreferenceEntry.values().length; i++) {
            if (PreferenceEntry.values()[i].getId() == id) {
                ret = PreferenceEntry.values()[i];
                break;
            }
        }
        return ret;
    }
};
