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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

public class DataStore<ITEMS_TYPE extends Data> extends Store implements IDataStore {

    private List<ITEMS_TYPE> items;
    private DataObjectComparator comparator;

    public DataStore(IPreferenceStore store) {
        super(store);
        this.items = new ArrayList<ITEMS_TYPE>();
    }

    public List<ITEMS_TYPE> getDataList() {
        return items;
    }

    @Override
    public IData getPreviousElement(IData data) {
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

    @Override
    public IData getNextElement(IData data) {
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

    @Override
    public IData getLastElement() {
        int index = items.size() - 1;
        if(index < 0) {
            return null;
        }
        return (IData)items.get(index);
    }

    public void add(ITEMS_TYPE data) {
        int position = 0;
        IData lastElement = getLastElement();
        if(lastElement != null) {
            position = lastElement.getPosition() + 1;
        }
        data.setPosition(position);
        addInternal(data);
    }

    public void replace(ITEMS_TYPE data) {
        items.set(items.indexOf(data), data);
    }

    public void delete(IData data) {
        items.remove(data);
    }

    @Override
    public void save() {
        sort();
        renumber();
        super.save();
    }

    @Override
    public void load() {
        sort();
        renumber();
    }

    @Override
    public void removeAll() {
        items.clear();
    }

    public ITEMS_TYPE getByPosition(int position) {
        return items.get(position);
    }

    public ITEMS_TYPE getById(String id) {
        for (ITEMS_TYPE data : getDataList()) {
            if (data.getId().equals(id)) {
                return data;
            }
        }
        return null;
    }

    @Override
    public void sort() {
        if(comparator == null) {
            comparator = new DataObjectComparator();
        }
        Collections.sort(items, comparator);
    }

    @Override
    public void renumber() {
        for (int i=0;i<items.size();i++) {
            ((IData)items.get(i)).setPosition(i);
        }
    }

    protected void addInternal(ITEMS_TYPE data) {
        items.add(data);
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
