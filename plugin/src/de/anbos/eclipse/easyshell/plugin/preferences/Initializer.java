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

import java.text.MessageFormat;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.core.runtime.preferences.IEclipsePreferences;
//import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
//import org.osgi.service.prefs.BackingStoreException;
//import org.osgi.service.prefs.Preferences;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Constants;
import de.anbos.eclipse.easyshell.plugin.Utils;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class Initializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
	    // get the actual preference store
	    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	    // set default values
		setDefaults(store);
		// migrate from old store
        migrate(store);
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
	}

    private void setDefaults(IPreferenceStore store) {
        String defaultCommandsPreset = PreferenceValueConverter.asCommandDataString(CommandDataDefaultCollection.getCommandsNative(null, true));
        String defaultCommands = "";
        String defaultMenu    = PreferenceValueConverter.asMenuDataString(CommandDataDefaultCollection.getCommandsNativeAsMenu(true));
        store.setDefault(Constants.PREF_COMMANDS_PRESET, defaultCommandsPreset);
        store.setDefault(Constants.PREF_COMMANDS, defaultCommands);
        store.setDefault(Constants.PREF_MENU, defaultMenu);
        store.setDefault(Constants.PREF_MIGRATED, false);
    }

    private void migrate(IPreferenceStore store) {
        if (!store.getBoolean(Constants.PREF_MIGRATED)) {
            int migrateState = -1; // -1 = old store not found, 0 (Yes) = migrated, 1 (No) = no migration wanted by user, 2 (Cancel) = try to migrate again
            for (int i=Version.values().length-2;i>0;i--) {
                Version version = Version.values()[i];
                String versionName = version.name();
                if (versionName.startsWith("v1_")) {
                    migrateState = migrate_from_v1(store, version, migrateState);
                } else {
                    migrateState = migrate_from_v2(store, version, migrateState);
                }
                // if no old store for this version found continue, else break
                if (migrateState != -1) {
                    switch(migrateState) {
                        case 0: Utils.showToolTipWarning(null, Activator.getResourceString("easyshell.plugin.name"), MessageFormat.format(
                                     Activator.getResourceString("easyshell.message.warning.migrated.yes"),
                                     versionName));

                        break;
                        case 1: Utils.showToolTipWarning(null, Activator.getResourceString("easyshell.plugin.name"), MessageFormat.format(
                                    Activator.getResourceString("easyshell.message.warning.migrated.no"),
                                    versionName));
                        break;
                        case 2: Utils.showToolTipWarning(null, Activator.getResourceString("easyshell.plugin.name"), MessageFormat.format(
                                    Activator.getResourceString("easyshell.message.warning.migrated.cancel"),
                                    versionName));
                        break;

                    }
                    break;
                }
            }
            // we have first startup without old store
            if (migrateState == -1) {
                Utils.showToolTipWarning(null, Activator.getResourceString("easyshell.plugin.name"), Activator.getResourceString("easyshell.message.warning.migrated.default"));
            }
            // do not set migration flag if user canceled and want to do it later
            if (migrateState != 2) {
                store.setValue(Constants.PREF_MIGRATED, true);
            }
        }
    }

    private int migrate_from_v2(IPreferenceStore store, Version version, int migrateState) {
        // get the old v2 store
        IPreferenceStore oldStore = Activator.getDefault().getNewPreferenceStoreByVersion(version.name());
        // check preferences for default values
        migrateState = migrate_check_pref_and_ask_user(oldStore, version, Constants.PREF_COMMANDS, migrateState);
        if (migrateState == 0) {
            store.setValue(Constants.PREF_COMMANDS, PreferenceValueConverter.migrateCommandDataList(version, oldStore.getString(Constants.PREF_COMMANDS)));
        }
        migrateState = migrate_check_pref_and_ask_user(oldStore, version, Constants.PREF_MENU, migrateState);
        if (migrateState == 0) {
            store.setValue(Constants.PREF_MENU, PreferenceValueConverter.migrateMenuDataList(version, oldStore.getString(Constants.PREF_MENU)));
        }
        return migrateState;
    }

    private int migrate_check_pref_and_ask_user(IPreferenceStore store, Version version, String pref, int migrateState) {
        // if cancel or no just skip this time
        if (migrateState == 1 || migrateState == 2) {
            return migrateState;
        }
        boolean migrationPossible = !store.isDefault(pref);
        if (migrationPossible) {
            // ask user if not already asked and said yes
            if (migrateState != 0) {
                String title = Activator.getResourceString("easyshell.plugin.name");
                String question = MessageFormat.format(
                        Activator.getResourceString("easyshell.question.migrate"),
                        version);
                MessageDialog dialog = new MessageDialog(
                        null, title, null, question,
                        MessageDialog.QUESTION,
                        new String[] {"Yes", "No", "Cancel"},
                        0); // no is the default
                migrateState = dialog.open();
            }
        }
        return migrateState;
    }

    private int migrate_from_v1(IPreferenceStore store, Version version, int migrateState) {
        // TODO:
        return -1;
    }

}
