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

package de.anbos.eclipse.easyshell.plugin.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import de.anbos.eclipse.easyshell.plugin.Constants;

public class Store implements IStore {

    private IPreferenceStore store;

    public Store(IPreferenceStore store) {
        this.store = store;
    }

    public void save() {
    }

    public void loadDefaults() {
        load();
    }

    public void load() {
    }

    public boolean isMigrated() {
        return store.getBoolean(Constants.PREF_MIGRATED);
    }

    public void setMigrated(boolean migrated) {
        store.setValue(Constants.PREF_MIGRATED, migrated);
    }

    protected IPreferenceStore getStore() {
        return store;
    }

}
