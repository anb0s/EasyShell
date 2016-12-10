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
        super.save();
        getStore().setValue(Constants.PREF_GENERAL,PreferenceValueConverter.asGeneralDataString(getData()));
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
