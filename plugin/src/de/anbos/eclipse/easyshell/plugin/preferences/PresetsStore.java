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

public class PresetsStore {

    private IPreferenceStore store;
    private List<PresetData> items;
    private DataObjectComparator comparator;

    public PresetsStore(IPreferenceStore store) {
        items = new ArrayList<PresetData>();
        this.store = store;
    }

    public List<PresetData> getAllPresets() {
        return items;
    }

    public PresetData[] getAllEnabledPresets() {
        List<PresetData> checkedItems = new ArrayList<PresetData>();
        Iterator<PresetData> dataIterator = items.iterator();
        while(dataIterator.hasNext()) {
            PresetData data = (PresetData)dataIterator.next();
            if(data.isEnabled()) {
                checkedItems.add(data);
            }
        }
        if(checkedItems.size() <= 0) {
        	return new PresetData[0];
        }
        PresetData[] checked = new PresetData[checkedItems.size()];
        for(int i = 0 ; i < checked.length ; i++) {
            checked[i] = (PresetData)checkedItems.get(i);
        }
        return checked;
    }

    public PresetData getPreviousElement(PresetData data) {
    	sort();
        for(int i = 0 ; i < items.size() ; i++) {
            PresetData item = (PresetData)items.get(i);
            if(item.equals(data)) {
            	try {
            		return (PresetData)items.get(i - 1);
            	} catch(Throwable t) {
            		return null;
            	}
            }
        }
        return null;
    }

    public PresetData getNextElement(PresetData data) {
    	sort();
        for(int i = 0 ; i < items.size() ; i++) {
            PresetData item = (PresetData)items.get(i);
            if(item.equals(data)) {
            	try {
            		return (PresetData)items.get(i + 1);
            	} catch(Throwable t) {
            		return null;
            	}
            }
        }
        return null;
    }

    public PresetData getLastElement() {
    	sort();
    	int index = items.size() - 1;
    	if(index < 0) {
    		return null;
    	}
    	return (PresetData)items.get(index);
    }

    public void add(PresetData data) {
    	int position = 0;
    	PresetData lastElement = getLastElement();
    	if(lastElement != null) {
    		position = lastElement.getPosition() + 1;
    	}
    	data.setPosition(position);
        items.add(data);
        sort();
    }

    public void delete(PresetData data) {
        items.remove(data);
        sort();
    }

    public void save() {
        store.setValue(Constants.PREF_PRESETS,PreferenceValueConverter.asString(getAllPresets()));
    }

    public void loadDefault() {
        PresetData[] items = PreferenceValueConverter.asPresetDataArray(store.getDefaultString(Constants.PREF_PRESETS));
        this.items.clear();
        for(int i = 0 ; i < items.length ; i++) {
            this.items.add(items[i]);
        }
        sort();
    }

    public void load() {
        PresetData[] items = PreferenceValueConverter.asPresetDataArray(store.getString(Constants.PREF_PRESETS));
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
    		((PresetData)items.get(i)).setPosition(i);
    	}
    }

    private class DataObjectComparator implements Comparator<Object> {
		public int compare(Object object1, Object object2) {
		    PresetData data1 = null;
		    PresetData data2 = null;
			if(object1 instanceof PresetData) {
				data1 = (PresetData)object1;
			}
			if(object2 instanceof PresetData) {
				data2 = (PresetData)object2;
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
