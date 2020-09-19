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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Constants;

public class GeneralDataStore extends Store {

    private GeneralData data = null;

    private static GeneralDataStore instance = null;

    public static GeneralDataStore instance() {
        if (instance == null) {
            instance = new GeneralDataStore(Activator.getDefault().getPreferenceStore());
        }
        return instance;
    }

    public GeneralDataStore(IPreferenceStore store) {
        super(store);
        store.addPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getProperty().equals(Constants.PREF_GENERAL)
                ) {
                    loadInternal((String)event.getNewValue());
                }
            }
        });
        load();
    }

    @Override
    public void save() {
        getStore().setValue(Constants.PREF_GENERAL,PreferenceValueConverter.asGeneralDataString(getData()));
        super.save();
    }

    public GeneralData getData() {
        return data;
    }

    @Override
    public void loadDefaults() {
        getStore().setToDefault(Constants.PREF_GENERAL);
        load();
    }

    private void loadInternal(String pref) {
        if (pref == null) {
            pref = getStore().getString(Constants.PREF_GENERAL);
        }
        data = PreferenceValueConverter.asGeneralData(pref);
        super.load();
    }

    @Override
    public void load() {
        loadInternal(null);
    }

}
