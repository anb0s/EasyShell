/*
 * Copyright (C) 2014 by Andre Bossert
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
 * Strings for preference store.
 */
public enum EasyShellPreferenceEntry {
    preferenceTargetOpen(0, "targetPreference"),
    preferenceTargetRun(1, "targetRunPreference"),
    preferenceTargetExplore(2, "targetExplorePreference"),
    preferenceTargetCopyPath(3, "targetCopyPathPreference"),
    preferenceListId(4, "listPreference"),
    preferenceListString(5, "IdStr"),
    preferenceQuotes(6, "QuotesStr"),
    preferenceDebug(7, "DebugStr"),
    preferenceTokenizer(8, "TokenizerStr");
    // attributes
    private final int id;
    private final String preferenceString;
    // construct
    EasyShellPreferenceEntry(int id, String pref) {
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
    public static EasyShellPreferenceEntry getFromId(int id) {
    	EasyShellPreferenceEntry ret = preferenceTargetOpen;
        for(int i = 0; i < EasyShellPreferenceEntry.values().length; i++) {
            if (EasyShellPreferenceEntry.values()[i].getId() == id) {
                ret = EasyShellPreferenceEntry.values()[i];
                break;
            }
        }
        return ret;
    }
}