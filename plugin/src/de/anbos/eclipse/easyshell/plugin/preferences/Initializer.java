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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
//import org.eclipse.core.runtime.preferences.IEclipsePreferences;
//import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
//import org.osgi.service.prefs.BackingStoreException;
//import org.osgi.service.prefs.Preferences;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Constants;

public class Initializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
	    // get the actual preference store
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		/*
		IEclipsePreferences instanceNode = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		Preferences preferences = instanceNode.node(Constants.PREF_VERSIONS[0]);
		preferences.put("test", "value");
		try {
            preferences.flush();
        } catch (BackingStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
		String defaultCommand = PreferenceValueConverter.asCommandDataString(CommandDataDefaultCollection.getCommandsNative(null, true));
		String defaultMenu    = PreferenceValueConverter.asCommandMenuDataString(CommandDataDefaultCollection.getCommandsNativeAsMenu(true));
		store.setDefault(Constants.PREF_COMMANDS, defaultCommand);
		store.setDefault(Constants.PREF_MENU, defaultMenu);
	}

}
