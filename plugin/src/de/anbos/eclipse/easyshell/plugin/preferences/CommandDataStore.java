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
                if (event.getProperty().equals(Constants.PREF_COMMANDS) || event.getProperty().equals(Constants.PREF_COMMANDS_PRESET)) {
                    load();
                }
            }
        });
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

    private CommandDataList getPresetCommands() {
        CommandDataList presetItems = new CommandDataList();
        for (CommandData data : getDataList()) {
            if (data.getPresetType() == PresetType.presetPlugin || data.getPresetType() == PresetType.presetPluginAndUser) {
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
        super.save();
        getStore().setValue(Constants.PREF_COMMANDS_PRESET,PreferenceValueConverter.asCommandDataString(getPresetCommands()));
        getStore().setValue(Constants.PREF_COMMANDS,PreferenceValueConverter.asCommandDataString(getUserCommands()));
    }

    @Override
    public void loadDefaults() {
        getStore().setToDefault(Constants.PREF_COMMANDS_PRESET);
        getStore().setToDefault(Constants.PREF_COMMANDS);
        load();
    }

    @Override
    public void load() {
        CommandData[] arrayPreset = PreferenceValueConverter.asCommandDataArray(getStore().getString(Constants.PREF_COMMANDS_PRESET));
        CommandData[] arrayUser   = PreferenceValueConverter.asCommandDataArray(getStore().getString(Constants.PREF_COMMANDS));
        removeAll();
        for(int i = 0 ; i < arrayPreset.length ; i++) {
            addInternal(arrayPreset[i]);
        }
        for(int i = 0 ; i < arrayUser.length ; i++) {
            addInternal(arrayUser[i]);
        }
        super.load();
    }

    public CommandData getCommandDataByName(String name) {
        for (CommandData data : getDataList()) {
            if (data.getName().equals(name)) {
                return data;
            }
        }
        return null;
    }

}
