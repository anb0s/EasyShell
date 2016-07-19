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

import de.anbos.eclipse.easyshell.plugin.Constants;

public class MenuDataStore {

    private IPreferenceStore store;
    private List<MenuData> items;
    private DataObjectComparator comparator;

    public MenuDataStore(IPreferenceStore store) {
        items = new ArrayList<MenuData>();
        this.store = store;
    }

    public List<MenuData> getCommandMenuDataList() {
        return items;
    }

    public MenuData[] getCommandMenuDataArray() {
        List<MenuData> allItems = getCommandMenuDataList();
        if(allItems.size() <= 0) {
            return new MenuData[0];
        }
        MenuData[] allArray = new MenuData[allItems.size()];
        for(int i = 0 ; i < allArray.length ; i++) {
            allArray[i] = allItems.get(i);
        }
        return allArray;
    }

    public List<MenuData> getEnabledCommandMenuDataList() {
        List<MenuData> checkedItems = new ArrayList<MenuData>();
        Iterator<MenuData> dataIterator = items.iterator();
        while(dataIterator.hasNext()) {
            MenuData data = (MenuData)dataIterator.next();
            if(data.isEnabled()) {
                checkedItems.add(data);
            }
        }
        return checkedItems;
    }

    public MenuData[] getEnabledCommandMenuDataArray() {
        List<MenuData> checkedItems = getEnabledCommandMenuDataList();
        if(checkedItems.size() <= 0) {
        	return new MenuData[0];
        }
        MenuData[] checked = new MenuData[checkedItems.size()];
        for(int i = 0 ; i < checked.length ; i++) {
            checked[i] = (MenuData)checkedItems.get(i);
        }
        return checked;
    }

    public MenuData getPreviousElement(MenuData data) {
    	sort();
        for(int i = 0 ; i < items.size() ; i++) {
            MenuData item = (MenuData)items.get(i);
            if(item.equals(data)) {
            	try {
            		return (MenuData)items.get(i - 1);
            	} catch(Throwable t) {
            		return null;
            	}
            }
        }
        return null;
    }

    public MenuData getNextElement(MenuData data) {
    	sort();
        for(int i = 0 ; i < items.size() ; i++) {
            MenuData item = (MenuData)items.get(i);
            if(item.equals(data)) {
            	try {
            		return (MenuData)items.get(i + 1);
            	} catch(Throwable t) {
            		return null;
            	}
            }
        }
        return null;
    }

    public MenuData getLastElement() {
    	sort();
    	int index = items.size() - 1;
    	if(index < 0) {
    		return null;
    	}
    	return (MenuData)items.get(index);
    }

    public void add(MenuData data) {
    	int position = 0;
    	MenuData lastElement = getLastElement();
    	if(lastElement != null) {
    		position = lastElement.getPosition() + 1;
    	}
    	data.setPosition(position);
        items.add(data);
        sort();
    }

    public void delete(MenuData data) {
        items.remove(data);
        sort();
    }

    public void replace(MenuData data) {
        items.set(data.getPosition(), data);
        sort();
    }

    public void save() {
        store.setValue(Constants.PREF_MENU,PreferenceValueConverter.asMenuDataString(getCommandMenuDataList()));
    }

    public void loadDefaults() {
        store.setToDefault(Constants.PREF_MENU);
        load();
    }

    public void load() {
        MenuData[] items = PreferenceValueConverter.asMenuDataArray(store.getString(Constants.PREF_MENU));
        this.items.clear();
        for(int i = 0 ; i < items.length ; i++) {
            this.items.add(items[i]);
        }
        sort();
    }

    public void removeAll() {
    	items.clear();
    }

    public boolean isMigrated() {
        return store.getBoolean(Constants.PREF_MIGRATED);
    }

    public void setMigrated(boolean migrated) {
        store.setValue(Constants.PREF_MIGRATED, migrated);
    }

    private void sort() {
    	if(comparator == null) {
    		comparator = new DataObjectComparator();
    	}
    	Collections.sort(items,comparator);
    	for (int i=0;i<items.size();i++) {
    		((MenuData)items.get(i)).setPosition(i);
    	}
    }

    private class DataObjectComparator implements Comparator<Object> {
		public int compare(Object object1, Object object2) {
		    MenuData data1 = null;
		    MenuData data2 = null;
			if(object1 instanceof MenuData) {
				data1 = (MenuData)object1;
			}
			if(object2 instanceof MenuData) {
				data2 = (MenuData)object2;
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
