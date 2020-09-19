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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Constants;
import de.anbos.eclipse.easyshell.plugin.legacy.PrefsV1_4;
import de.anbos.eclipse.easyshell.plugin.legacy.PrefsV1_5;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class Initializer extends AbstractPreferenceInitializer {

    private int migrateState = -1; // -1 = old store not found, 0 (Yes) = migrated, 1 (No) = no migration wanted by user, 2 (Cancel) = try to migrate again
    private Version migrateVersion = Version.vUnknown;

    public void initializeDefaultPreferences() {
        // get the actual preference store
        Store store = new Store(Activator.getDefault().getPreferenceStore());
        // set default values
        setDefaults(store);
        // migrate from old store
        migrate(store);
        // verify and repair migrated store
        if (!verify(store)) {
            repair(store);
        }
        // inform user
        inform(store);
        // apply migrated store
        apply(store);
        // save (write) the store to disk
        save(store);
    }

    private void setDefaults(IStore store) {
        String defaultCommandsPreset = PreferenceValueConverter.asCommandDataString(CommandDataDefaultCollection.getCommandsNativeAll(null), false);
        String defaultCommandsModify = "";
        String defaultCommands = "";
        String defaultMenu    = PreferenceValueConverter.asMenuDataString(CommandDataDefaultCollection.getCommandsNativeAsMenu(true));
        String defaultGeneral = PreferenceValueConverter.asGeneralDataString(new GeneralData());
        store.getStore().setDefault(Constants.PREF_COMMANDS_PRESET, defaultCommandsPreset);
        store.getStore().setDefault(Constants.PREF_COMMANDS_MODIFY, defaultCommandsModify);
        store.getStore().setDefault(Constants.PREF_COMMANDS_USER, defaultCommands);
        store.getStore().setDefault(Constants.PREF_MENU, defaultMenu);
        store.getStore().setDefault(Constants.PREF_GENERAL, defaultGeneral);
        store.getStore().setDefault(Constants.PREF_MIGRATED, false);
    }

    private void migrate(IStore store) {
        migrateState = -1;
        if (!store.getStore().getBoolean(Constants.PREF_MIGRATED)) {
            for (int i=Version.values().length-2; (i > 0) && (migrateState == -1); i--) {
                migrateVersion = Version.values()[i];
                if (migrateVersion.toString().startsWith("v1_")) {
                    migrate_from_v1(store, migrateVersion);
                } else {
                    migrate_from_v2(store, migrateVersion);
                }
            }
        }
    }

    private boolean verify(IStore store) {
        boolean verified = true;
        if (!store.getStore().getBoolean(Constants.PREF_MIGRATED)) {
            verified = CommandDataStore.instance().verify() && MenuDataStore.instance().verify();
        }
        return verified;
    }

    private void repair(IStore store) {
        for (int i=Version.values().length-2; i > 0; i--) {
            // try to load the old commands
            Version reMigratedversion = Version.values()[i];
            boolean reMigrated = true;
            if (migrateVersion.toString().startsWith("v1_")) {
                reMigrated = reMigrate_from_v1(store, reMigratedversion);
            } else {
                reMigrated = reMigrate_from_v2(store, reMigratedversion);
            }
            if (reMigrated) {
                break;
            }
        }
    }

    private void apply(IStore store) {
        if (!store.isMigrated()) {
            // do not set migration flag if user canceled and want to do it later
            if (migrateState != 2) {
                store.setMigrated(true);
            }
        }
    }

    private void save(IStore store) {
        store.save();
    }

    private void inform(IStore store) {
        if (!store.getStore().getBoolean(Constants.PREF_MIGRATED)) {
            switch(migrateState) {
                // we have first startup without old store
                case -1:
                    Utils.showToolTipWarning(null, Activator.getResourceString("easyshell.plugin.name"), Activator.getResourceString("easyshell.message.warning.migrated.default"));
                break;
                case 0: Utils.showToolTipWarning(null, Activator.getResourceString("easyshell.plugin.name"), MessageFormat.format(
                             Activator.getResourceString("easyshell.message.warning.migrated.yes"),
                             migrateVersion.getName()));
                break;
                case 1: Utils.showToolTipWarning(null, Activator.getResourceString("easyshell.plugin.name"), MessageFormat.format(
                            Activator.getResourceString("easyshell.message.warning.migrated.no"),
                            migrateVersion.getName()));
                break;
                case 2: Utils.showToolTipWarning(null, Activator.getResourceString("easyshell.plugin.name"), MessageFormat.format(
                            Activator.getResourceString("easyshell.message.warning.migrated.cancel"),
                            migrateVersion.getName()));
                break;
            }
        }
    }

    private void migrate_from_v2(IStore store, Version version) {
        // get the old v2 store
        Store oldStore = new Store(Activator.getDefault().getPreferenceStoreByVersion(version.name()));
        // get the old preset with embedded modify
        migrate_check_pref_and_ask_user(oldStore, version, new ArrayList<String>(Arrays.asList(Constants.PREF_COMMANDS_PRESET)));
        String oldPresetsModified = null;
        if (migrateState == 0) {
            oldPresetsModified = PreferenceValueConverter.migrateCommandDataList(version, oldStore.getStore().getString(Constants.PREF_COMMANDS_PRESET), true);
            store.getStore().setValue(Constants.PREF_COMMANDS_MODIFY, oldPresetsModified);
        }
        // get the new modify
        migrate_check_pref_and_ask_user(oldStore, version, new ArrayList<String>(Arrays.asList(Constants.PREF_COMMANDS_MODIFY)));
        if (migrateState == 0) {
            String newPresetsModified = PreferenceValueConverter.migrateCommandDataBasicList(version, oldStore.getStore().getString(Constants.PREF_COMMANDS_MODIFY));
            if (oldPresetsModified == null || oldPresetsModified.isEmpty()) {
                store.getStore().setValue(Constants.PREF_COMMANDS_MODIFY, newPresetsModified);
            }
        }
        // get old user commands
        migrate_check_pref_and_ask_user(oldStore, version, new ArrayList<String>(Arrays.asList(Constants.PREF_COMMANDS_OLD)));
        String oldUserCommands = null;
        if (migrateState == 0) {
            oldUserCommands = PreferenceValueConverter.migrateCommandDataList(version, oldStore.getStore().getString(Constants.PREF_COMMANDS_OLD), false);
            store.getStore().setValue(Constants.PREF_COMMANDS_USER, oldUserCommands);
        }
        // get new user commands
        migrate_check_pref_and_ask_user(oldStore, version, new ArrayList<String>(Arrays.asList(Constants.PREF_COMMANDS_USER)));
        if (migrateState == 0) {
            String newUserCommands = PreferenceValueConverter.migrateCommandDataList(version, oldStore.getStore().getString(Constants.PREF_COMMANDS_USER), false);
            if (oldUserCommands == null || oldUserCommands.isEmpty()) {
                store.getStore().setValue(Constants.PREF_COMMANDS_USER, newUserCommands);
            }
        }
        // get menus
        migrate_check_pref_and_ask_user(oldStore, version, new ArrayList<String>(Arrays.asList(Constants.PREF_MENU)));
        if (migrateState == 0) {
            store.getStore().setValue(Constants.PREF_MENU, PreferenceValueConverter.migrateMenuDataList(version, oldStore.getStore().getString(Constants.PREF_MENU)));
        }
    }

    private void migrate_from_v1(IStore store, Version version) {
        // get the old v1_5 store
        Store oldStore = new Store(Activator.getDefault().getLegacyPreferenceStore());
        // check if we want version 1.5 or 1.4
        if (version == Version.v1_5) {
            // check preferences for default values
            migrate_check_pref_and_ask_user(oldStore, version, PrefsV1_5.getPreferenceList());
            if (migrateState == 0) {
                CommandDataList cmdDataList = new CommandDataList();
                MenuDataList menuDataList = CommandDataDefaultCollection.getCommandsNativeAsMenu(true);
                if (PrefsV1_5.loadStore(oldStore.getStore(), Utils.getOS(), cmdDataList, menuDataList)) {
                    store.getStore().setValue(Constants.PREF_COMMANDS_USER, PreferenceValueConverter.asCommandDataString(cmdDataList, false));
                    store.getStore().setValue(Constants.PREF_MENU, PreferenceValueConverter.asMenuDataString(menuDataList));
                }
            }
        } else if (version == Version.v1_4) {
            // check preferences for default values
            migrate_check_pref_and_ask_user(oldStore, version, PrefsV1_4.getPreferenceList());
            if (migrateState == 0) {
                CommandDataList cmdDataList = new CommandDataList();
                MenuDataList menuDataList = CommandDataDefaultCollection.getCommandsNativeAsMenu(true);
                if (PrefsV1_4.loadStore(oldStore.getStore(), Utils.getOS(), cmdDataList, menuDataList)) {
                    store.getStore().setValue(Constants.PREF_COMMANDS_USER, PreferenceValueConverter.asCommandDataString(cmdDataList, false));
                    store.getStore().setValue(Constants.PREF_MENU, PreferenceValueConverter.asMenuDataString(menuDataList));
                }
            }
        }
    }

    private boolean reMigrate_from_v1(IStore store, Version version) {
        boolean reMigrated = false;
        return reMigrated;
    }

    private boolean reMigrate_from_v2(IStore store, Version version) {
        boolean reMigrated = false;
        // get the old v2 store
        IPreferenceStore oldStore = Activator.getDefault().getPreferenceStoreByVersion(version.name());
        // get old user commands
        String oldUserCommands = null;
        if (migrateState == 0) {
            oldUserCommands = PreferenceValueConverter.migrateCommandDataList(version, oldStore.getString(Constants.PREF_COMMANDS_OLD), false);
            if (oldUserCommands != null && !oldUserCommands.isEmpty()) {
                store.getStore().setValue(Constants.PREF_COMMANDS_USER, oldUserCommands);
                reMigrated = true;
            }
        }
        // get new user commands
        if (migrateState == 0) {
            String newUserCommands = PreferenceValueConverter.migrateCommandDataList(version, oldStore.getString(Constants.PREF_COMMANDS_USER), false);
            if ((oldUserCommands == null || oldUserCommands.isEmpty()) && (newUserCommands != null && !newUserCommands.isEmpty())) {
                store.getStore().setValue(Constants.PREF_COMMANDS_USER, newUserCommands);
                reMigrated = true;
            }
        }
        return reMigrated;
    }

    private void migrate_check_pref_and_ask_user(IStore store, Version version, List<String> prefList) {
        // if cancel or no just skip this time
        if (migrateState == 1 || migrateState == 2) {
            return;
        }
        boolean migrationPossible = false;
        for (String pref : prefList) {
            if (!store.getStore().isDefault(pref)) {
                migrationPossible = true;
                break;
            }
        }
        if (migrationPossible) {
            // ask user if not already asked and said yes
            if (migrateState != 0) {
                String title = Activator.getResourceString("easyshell.plugin.name");
                String question = MessageFormat.format(
                        Activator.getResourceString("easyshell.question.migrate"),
                        version.getName());
                MessageDialog dialog = new MessageDialog(
                        null, title, null, question,
                        MessageDialog.QUESTION,
                        new String[] {"Yes", "No", "Cancel"},
                        0); // no is the default
                migrateState = dialog.open();
            }
        }
    }

}
