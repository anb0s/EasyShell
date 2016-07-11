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

public class CommandMenuDataStore {

    private IPreferenceStore store;
    private List<CommandMenuData> items;
    private DataObjectComparator comparator;

    public CommandMenuDataStore(IPreferenceStore store) {
        items = new ArrayList<CommandMenuData>();
        this.store = store;
    }

    public List<CommandMenuData> getCommandMenuDataList() {
        return items;
    }

    public CommandMenuData[] getCommandMenuDataArray() {
        List<CommandMenuData> allItems = getCommandMenuDataList();
        if(allItems.size() <= 0) {
            return new CommandMenuData[0];
        }
        CommandMenuData[] allArray = new CommandMenuData[allItems.size()];
        for(int i = 0 ; i < allArray.length ; i++) {
            allArray[i] = (CommandMenuData)allItems.get(i);
        }
        return allArray;
    }

    public List<CommandMenuData> getEnabledCommandMenuDataList() {
        List<CommandMenuData> checkedItems = new ArrayList<CommandMenuData>();
        Iterator<CommandMenuData> dataIterator = items.iterator();
        while(dataIterator.hasNext()) {
            CommandMenuData data = (CommandMenuData)dataIterator.next();
            if(data.isEnabled()) {
                checkedItems.add(data);
            }
        }
        return checkedItems;
    }

    public CommandMenuData[] getEnabledCommandMenuDataArray() {
        List<CommandMenuData> checkedItems = getEnabledCommandMenuDataList();
        if(checkedItems.size() <= 0) {
        	return new CommandMenuData[0];
        }
        CommandMenuData[] checked = new CommandMenuData[checkedItems.size()];
        for(int i = 0 ; i < checked.length ; i++) {
            checked[i] = (CommandMenuData)checkedItems.get(i);
        }
        return checked;
    }

    public CommandMenuData getPreviousElement(CommandMenuData data) {
    	sort();
        for(int i = 0 ; i < items.size() ; i++) {
            CommandMenuData item = (CommandMenuData)items.get(i);
            if(item.equals(data)) {
            	try {
            		return (CommandMenuData)items.get(i - 1);
            	} catch(Throwable t) {
            		return null;
            	}
            }
        }
        return null;
    }

    public CommandMenuData getNextElement(CommandMenuData data) {
    	sort();
        for(int i = 0 ; i < items.size() ; i++) {
            CommandMenuData item = (CommandMenuData)items.get(i);
            if(item.equals(data)) {
            	try {
            		return (CommandMenuData)items.get(i + 1);
            	} catch(Throwable t) {
            		return null;
            	}
            }
        }
        return null;
    }

    public CommandMenuData getLastElement() {
    	sort();
    	int index = items.size() - 1;
    	if(index < 0) {
    		return null;
    	}
    	return (CommandMenuData)items.get(index);
    }

    public void add(CommandMenuData data) {
    	int position = 0;
    	CommandMenuData lastElement = getLastElement();
    	if(lastElement != null) {
    		position = lastElement.getPosition() + 1;
    	}
    	data.setPosition(position);
        items.add(data);
        sort();
    }

    public void delete(CommandMenuData data) {
        items.remove(data);
        sort();
    }

    public void save() {
        store.setValue(Constants.PREF_MENU,PreferenceValueConverter.asCommandMenuDataString(getCommandMenuDataList()));
    }

    public void loadDefaults() {
        store.setToDefault(Constants.PREF_MENU);
        load();
    }

    public void load() {
        CommandMenuData[] items = PreferenceValueConverter.asCommandMenuDataArray(store.getString(Constants.PREF_MENU));
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
    		((CommandMenuData)items.get(i)).setPosition(i);
    	}
    }

    private class DataObjectComparator implements Comparator<Object> {
		public int compare(Object object1, Object object2) {
		    CommandMenuData data1 = null;
		    CommandMenuData data2 = null;
			if(object1 instanceof CommandMenuData) {
				data1 = (CommandMenuData)object1;
			}
			if(object2 instanceof CommandMenuData) {
				data2 = (CommandMenuData)object2;
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
