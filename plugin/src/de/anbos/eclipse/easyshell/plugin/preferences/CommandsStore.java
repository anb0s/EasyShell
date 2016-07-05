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
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

public class CommandsStore {

    private IPreferenceStore store;
    private List<CommandData> items;
    private DataObjectComparator comparator;

    public CommandsStore(IPreferenceStore store) {
        items = new ArrayList<CommandData>();
        this.store = store;
    }

    public List<CommandData> getAllCommands() {
        return items;
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

    public List<CommandData> getAllEnabledCommands() {
        List<CommandData> checkedItems = new ArrayList<CommandData>();
        Iterator<CommandData> dataIterator = items.iterator();
        while(dataIterator.hasNext()) {
            CommandData data = (CommandData)dataIterator.next();
            if(data.isEnabled()) {
                checkedItems.add(data);
            }
        }
        return checkedItems;
    }

    public CommandData[] getAllEnabledCommandsArray() {
        List<CommandData> checkedItems = getAllEnabledCommands();
        if(checkedItems.size() <= 0) {
        	return new CommandData[0];
        }
        CommandData[] checked = new CommandData[checkedItems.size()];
        for(int i = 0 ; i < checked.length ; i++) {
            checked[i] = (CommandData)checkedItems.get(i);
        }
        return checked;
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

    public void delete(CommandData data) {
        items.remove(data);
        sort();
    }

    public void save() {
        store.setValue(Constants.PREF_PRESETS,PreferenceValueConverter.asString(getAllCommands()));
    }

    public void loadDefaults() {
        store.setToDefault(Constants.PREF_PRESETS);
        load();
    }

    public void load() {
        CommandData[] items = PreferenceValueConverter.asPresetDataArray(store.getString(Constants.PREF_PRESETS));
        this.items.clear();
        for(int i = 0 ; i < items.length ; i++) {
            this.items.add(items[i]);
        }
        sort();
    }

    public void removeAll() {
    	items.clear();
    }

    private void sort() {
    	if(comparator == null) {
    		comparator = new DataObjectComparator();
    	}
    	Collections.sort(items,comparator);
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
			if(data1 == null | data2 == null) {
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
