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

import java.util.Iterator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Constants;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;

public class CommandDataStore extends DataStore<CommandData> {

    private static CommandDataStore instance = null;

    public static CommandDataStore instance() {
        if (instance == null) {
            instance = new CommandDataStore(Activator.getDefault().getPreferenceStore());
        }
        return instance;
    }

    public CommandDataStore(IPreferenceStore store) {
        super(store);
        store.addPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getProperty().equals(Constants.PREF_COMMANDS_USER) 	||
                    event.getProperty().equals(Constants.PREF_COMMANDS_PRESET)	||
                    event.getProperty().equals(Constants.PREF_COMMANDS_MODIFY)
                ) {
                    load();
                }
            }
        });
        load();
    }

    private CommandDataList getUserCommands() {
        CommandDataList userItems = new CommandDataList();
        for (CommandData data : getDataList()) {
            if (data.getPresetType() == PresetType.presetUser) {
                userItems.add(data);
            }
        }
        return userItems;
    }

    private CommandDataList getPresetCommands(boolean modifyOnly) {
        CommandDataList presetItems = new CommandDataList();
        for (CommandData data : getDataList()) {
            if ( ( modifyOnly && (data.getPresetType() == PresetType.presetPluginModify)) ||
                 (!modifyOnly && (data.getPresetType() == PresetType.presetPlugin || data.getPresetType() == PresetType.presetPluginModify))
               ) {
                presetItems.add(data);
            }
        }
        return presetItems;
    }

    public CommandData[] getAllCommandsArray() {
        CommandDataList allItems = new CommandDataList(getDataList());
        if(allItems.size() <= 0) {
            return new CommandData[0];
        }
        CommandData[] allArray = new CommandData[allItems.size()];
        for(int i = 0 ; i < allArray.length ; i++) {
            allArray[i] = (CommandData)allItems.get(i);
        }
        return allArray;
    }

    @Override
    public void save() {
        // do not safe the presets
        //getStore().setValue(Constants.PREF_COMMANDS_PRESET,PreferenceValueConverter.asCommandDataString(getPresetCommands(false), false));
        getStore().setValue(Constants.PREF_COMMANDS_MODIFY,PreferenceValueConverter.asCommandDataString(getPresetCommands(true), true));
        getStore().setValue(Constants.PREF_COMMANDS_USER,PreferenceValueConverter.asCommandDataString(getUserCommands(), false));
        super.save();
    }

    @Override
    public void loadDefaults() {
        getStore().setToDefault(Constants.PREF_COMMANDS_PRESET);
        getStore().setToDefault(Constants.PREF_COMMANDS_MODIFY);
        getStore().setToDefault(Constants.PREF_COMMANDS_USER);
        load();
    }

    @Override
    public void load() {
        CommandData[] arrayPreset = PreferenceValueConverter.asCommandDataArray(getStore().getString(Constants.PREF_COMMANDS_PRESET));
        CommandDataBasic[] arrayModify = PreferenceValueConverter.asCommandDataBasicArray(getStore().getString(Constants.PREF_COMMANDS_MODIFY));
        CommandData[] arrayUser   = PreferenceValueConverter.asCommandDataArray(getStore().getString(Constants.PREF_COMMANDS_USER));
        removeAll();
        addModifyToPreset(arrayModify, arrayPreset);
        for(int i = 0 ; i < arrayPreset.length ; i++) {
            addInternal(arrayPreset[i]);
        }
        for(int i = 0 ; i < arrayUser.length ; i++) {
            addInternal(arrayUser[i]);
        }
        super.load();
    }

    private void addModifyToPreset(CommandDataBasic[] arrayModify, CommandData[] arrayPreset) {
        for(int i = 0 ; i < arrayModify.length ; i++) {
            for(int j = 0 ; j < arrayPreset.length ; j++) {
                if (arrayModify[i].getId().equals(arrayPreset[j].getId())) {
                    arrayPreset[j].addOrRemoveModifyData(arrayModify[i]);
                    break;
                }
            }
        }
    }

    public CommandData getCommandDataByName(String name) {
        for (CommandData data : getDataList()) {
            if (data.getName().equals(name)) {
                return data;
            }
        }
        return null;
    }

    private boolean verifyInternal() {
        boolean valid = true;
        Iterator<CommandData> dataIterator = getDataList().iterator();
        while(valid && dataIterator.hasNext()) {
            CommandData data = (CommandData)dataIterator.next();
               valid = data.verify();
        }
        return valid;
    }

    @Override
    public boolean verify() {
        return super.verify() && verifyInternal();
    }

}
