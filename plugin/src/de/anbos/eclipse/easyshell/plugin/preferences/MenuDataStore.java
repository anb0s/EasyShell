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
import de.anbos.eclipse.easyshell.plugin.exceptions.UnknownCommandID;
import de.anbos.eclipse.easyshell.plugin.types.Category;

public class MenuDataStore extends DataStore<MenuData> {

    private static MenuDataStore instance = null;

    public static MenuDataStore instance() {
        if (instance == null) {
            instance = new MenuDataStore(Activator.getDefault().getPreferenceStore());
        }
        return instance;
    }

    public MenuDataStore(IPreferenceStore store) {
        super(store);
        store.addPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getProperty().equals(Constants.PREF_MENU)) {
                    loadInternal((String)event.getNewValue());
                }
            }
        });
        load();
    }

    public MenuData[] getCommandMenuDataArray() {
        MenuDataList allItems = new MenuDataList(getDataList());
        if(allItems.size() <= 0) {
            return new MenuData[0];
        }
        MenuData[] allArray = new MenuData[allItems.size()];
        for(int i = 0 ; i < allArray.length ; i++) {
            allArray[i] = allItems.get(i);
        }
        return allArray;
    }

    public MenuDataList getEnabledCommandMenuDataListByCategory(Category category) {
        MenuDataList checkedItems = new MenuDataList();
        Iterator<MenuData> dataIterator = getDataList().iterator();
        while(dataIterator.hasNext()) {
            MenuData data = (MenuData)dataIterator.next();
            try {
                if(data.isEnabled() && (category == Category.categoryUnknown || data.getCommandData().getCategory() == category)) {
                    checkedItems.add(data);
                }
            } catch (UnknownCommandID e) {
                e.logInternalError();
            }
        }
        return checkedItems;
    }

    public MenuDataList getEnabledCommandMenuDataList() {
        return getEnabledCommandMenuDataListByCategory(Category.categoryUnknown);
    }

    public MenuData[] getEnabledCommandMenuDataArray() {
        MenuDataList checkedItems = getEnabledCommandMenuDataList();
        if(checkedItems.size() <= 0) {
            return new MenuData[0];
        }
        MenuData[] checked = new MenuData[checkedItems.size()];
        for(int i = 0 ; i < checked.length ; i++) {
            checked[i] = (MenuData)checkedItems.get(i);
        }
        return checked;
    }

    @Override
    public void save() {
        super.save();
        getStore().setValue(Constants.PREF_MENU,PreferenceValueConverter.asMenuDataString(getDataList()));
    }

    @Override
    public void loadDefaults() {
        getStore().setToDefault(Constants.PREF_MENU);
        load();
    }

    private void loadInternal(String prefMenu) {
        if (prefMenu == null) {
            prefMenu = getStore().getString(Constants.PREF_MENU);
        }
        MenuData[] items = PreferenceValueConverter.asMenuDataArray(prefMenu);
        removeAll();
        for(int i = 0 ; i < items.length ; i++) {
            addInternal(items[i]);
        }
        super.load();
    }

    @Override
    public void load() {
        loadInternal(null);
    }

    private boolean verifyInternal() {
        boolean valid = true;
        Iterator<MenuData> dataIterator = getDataList().iterator();
        while(valid && dataIterator.hasNext()) {
            MenuData data = (MenuData)dataIterator.next();
               valid = data.verify();
        }
        return valid;
    }

    @Override
    public boolean verify() {
        return super.verify() && verifyInternal();
    }

    public MenuDataList getRefencedBy(String id) {
        MenuDataList ref = new MenuDataList();
        Iterator<MenuData> dataIterator = getDataList().iterator();
        while(dataIterator.hasNext()) {
            MenuData data = (MenuData)dataIterator.next();
            if(data.getCommandId().equals(id)) {
                ref.add(data);
            }
        }
        return ref;
    }

}
