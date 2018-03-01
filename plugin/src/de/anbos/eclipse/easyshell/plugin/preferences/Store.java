/*******************************************************************************
 * Copyright (c) 2014 - 2018 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

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
