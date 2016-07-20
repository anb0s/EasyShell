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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import de.anbos.eclipse.easyshell.plugin.Constants;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;

public class CommandDataStore {

    private IPreferenceStore store;
    private List<CommandData> items;
    private DataObjectComparator comparator;

    public CommandDataStore(IPreferenceStore store) {
        items = new ArrayList<CommandData>();
        this.store = store;
    }

    public List<CommandData> getAllCommands() {
       return items;
    }

    private List<CommandData> getUserCommands() {
        List<CommandData> userItems = new ArrayList<CommandData>();
        for (CommandData data : items) {
            if (data.getPresetType() == PresetType.presetUser) {
                userItems.add(data);
            }
        }
        return userItems;
    }

    private List<CommandData> getPresetCommands() {
        List<CommandData> presetItems = new ArrayList<CommandData>();
        for (CommandData data : items) {
            if (data.getPresetType() == PresetType.presetPlugin) {
                presetItems.add(data);
            }
        }
        return presetItems;
    }

    public CommandData[] getAllCommandsArray() {
        List<CommandData> allItems = getAllCommands();
        if(allItems.size() <= 0) {
            return new CommandData[0];
        }
        CommandData[] allArray = new CommandData[allItems.size()];
        for(int i = 0 ; i < allArray.length ; i++) {
            allArray[i] = (CommandData)allItems.get(i);
        }
        return allArray;
    }

    public CommandData getPreviousElement(CommandData data) {
        sort();
        for(int i = 0 ; i < items.size() ; i++) {
            CommandData item = (CommandData)items.get(i);
            if(item.equals(data)) {
            	try {
            		return (CommandData)items.get(i - 1);
            	} catch(Throwable t) {
            		return null;
            	}
            }
        }
        return null;
    }

    public CommandData getNextElement(CommandData data) {
        sort();
        for(int i = 0 ; i < items.size() ; i++) {
            CommandData item = (CommandData)items.get(i);
            if(item.equals(data)) {
            	try {
            		return (CommandData)items.get(i + 1);
            	} catch(Throwable t) {
            		return null;
            	}
            }
        }
        return null;
    }

    public CommandData getLastElement() {
        sort();
    	int index = items.size() - 1;
    	if(index < 0) {
    		return null;
    	}
    	return (CommandData)items.get(index);
    }

    public void add(CommandData data) {
        int position = 0;
        CommandData lastElement = getLastElement();
        if(lastElement != null) {
            position = lastElement.getPosition() + 1;
        }
        data.setPosition(position);
        items.add(data);
        sort();
    }

    public void replace(CommandData data) {
        items.set(data.getPosition(), data);
        //sort();
    }

    public void delete(CommandData data) {
        items.remove(data);
        sort();
    }

    public void save() {
        store.setValue(Constants.PREF_COMMANDS_PRESET,PreferenceValueConverter.asCommandDataString(getPresetCommands()));
        store.setValue(Constants.PREF_COMMANDS,PreferenceValueConverter.asCommandDataString(getUserCommands()));
    }

    public void loadDefaults() {
        store.setToDefault(Constants.PREF_COMMANDS_PRESET);
        store.setToDefault(Constants.PREF_COMMANDS);
        load();
    }

    public void load() {
        CommandData[] arrayPreset = PreferenceValueConverter.asCommandDataArray(store.getString(Constants.PREF_COMMANDS_PRESET));
        CommandData[] arrayUser   = PreferenceValueConverter.asCommandDataArray(store.getString(Constants.PREF_COMMANDS));
        this.items.clear();
        for(int i = 0 ; i < arrayPreset.length ; i++) {
            this.items.add(arrayPreset[i]);
        }
        for(int i = 0 ; i < arrayUser.length ; i++) {
            this.items.add(arrayUser[i]);
        }
        sort();
    }

    public void removeAll() {
        items.clear();
    }

    public CommandData getCommandDataByName(String name) {
        for (CommandData data : items) {
            if (data.getName().equals(name)) {
                return data;
            }
        }
        return null;
    }

    public CommandData getCommandDataByPosition(int position) {
        return items.get(position);
    }

    private void sort() {
        if(comparator == null) {
            comparator = new DataObjectComparator();
        }
        Collections.sort(items, comparator);
        for (int i=0;i<items.size();i++) {
            ((CommandData)items.get(i)).setPosition(i);
        }
    }

    private class DataObjectComparator implements Comparator<Object> {
        public int compare(Object object1, Object object2) {
            CommandData data1 = null;
            CommandData data2 = null;
            if(object1 instanceof CommandData) {
                data1 = (CommandData)object1;
            }
            if(object2 instanceof CommandData) {
                data2 = (CommandData)object2;
            }
            if(data1 == null || data2 == null) {
                return -1;
            }
            if(data1.getPosition() > data2.getPosition()) {
                return 1;
            }
            if(data1.getPosition() == data2.getPosition()) {
                return 0;
            }
            if(data1.getPosition() < data2.getPosition()) {
                return -1;
            }
            return -1;
        }
    }

}
