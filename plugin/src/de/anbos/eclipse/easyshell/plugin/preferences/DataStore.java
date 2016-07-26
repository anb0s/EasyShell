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

public class DataStore<T extends Data> implements IDataStore {

    private IPreferenceStore store;
    private List<T> items;
    private DataObjectComparator comparator;

    public DataStore(IPreferenceStore store) {
        this.items = new ArrayList<T>();
        this.store = store;
    }

    public List<T> getDataList() {
        return items;
    }

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IDataStore#getPreviousElement(de.anbos.eclipse.easyshell.plugin.preferences.IData)
     */
    @Override
    public IData getPreviousElement(IData data) {
    	sort();
        for(int i = 0 ; i < items.size() ; i++) {
            IData item = (IData)items.get(i);
            if(item.equals(data)) {
            	try {
            		return (IData)items.get(i - 1);
            	} catch(Throwable t) {
            		return null;
            	}
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IDataStore#getNextElement(de.anbos.eclipse.easyshell.plugin.preferences.IData)
     */
    @Override
    public IData getNextElement(IData data) {
    	sort();
        for(int i = 0 ; i < items.size() ; i++) {
            IData item = (IData)items.get(i);
            if(item.equals(data)) {
            	try {
            		return (IData)items.get(i + 1);
            	} catch(Throwable t) {
            		return null;
            	}
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IDataStore#getLastElement()
     */
    @Override
    public IData getLastElement() {
    	sort();
    	int index = items.size() - 1;
    	if(index < 0) {
    		return null;
    	}
    	return (IData)items.get(index);
    }

    public void add(T data) {
    	int position = 0;
    	IData lastElement = getLastElement();
    	if(lastElement != null) {
    		position = lastElement.getPosition() + 1;
    	}
    	data.setPosition(position);
        items.add(data);
        sort();
    }

    public void replace(T data) {
        items.set(data.getPosition(), data);
        //sort();
    }

    public void delete(IData data) {
        items.remove(data);
        sort();
    }

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IDataStore#save()
     */
    @Override
    public void save() {
    }

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IDataStore#loadDefaults()
     */
    @Override
    public void loadDefaults() {
        load();
    }

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IDataStore#load()
     */
    @Override
    public void load() {
        sort();
    }

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IDataStore#removeAll()
     */
    @Override
    public void removeAll() {
    	items.clear();
    }

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IDataStore#isMigrated()
     */
    @Override
    public boolean isMigrated() {
        return store.getBoolean(Constants.PREF_MIGRATED);
    }

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IDataStore#setMigrated(boolean)
     */
    @Override
    public void setMigrated(boolean migrated) {
        store.setValue(Constants.PREF_MIGRATED, migrated);
    }

    public T getByPosition(int position) {
        return items.get(position);
    }

    public T getById(String id) {
        for (T data : getDataList()) {
            if (data.getId().equals(id)) {
                return data;
            }
        }
        return null;
    }

    protected IPreferenceStore getStore() {
        return store;
    }

    protected void addItem(T data) {
        items.add(data);
    }

    protected void sort() {
    	if(comparator == null) {
    		comparator = new DataObjectComparator();
    	}
    	Collections.sort(items, comparator);
    	for (int i=0;i<items.size();i++) {
    		((IData)items.get(i)).setPosition(i);
    	}
    }

    private class DataObjectComparator implements Comparator<Object> {
		public int compare(Object object1, Object object2) {
		    IData data1 = null;
		    IData data2 = null;
			data1 = (IData)object1;
			data2 = (IData)object2;
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
