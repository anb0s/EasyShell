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

package de.anbos.eclipse.easyshell.plugin.preferences;

import java.io.IOException;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Constants;

public class Store implements IStore {

    private IPreferenceStore store = null;

    public Store(IPreferenceStore store) {
        this.store = store;
    }

    public IPreferenceStore getStore() {
        return store;
    }

    public void load() {
    }

    public boolean verify() {
        return store != null;
    }

    public void save() {
        if (store.needsSaving() && store instanceof IPersistentPreferenceStore) {
            try {
                ((IPersistentPreferenceStore)store).save();
            } catch (IOException e) {
                Activator.logError(Activator.getResourceString("easyshell.message.error.store.save"), e);
            }
        }
    }

    public void loadDefaults() {
        load();
    }

    public boolean isMigrated() {
        return store.getBoolean(Constants.PREF_MIGRATED);
    }

    public void setMigrated(boolean migrated) {
        store.setValue(Constants.PREF_MIGRATED, migrated);
    }


}
