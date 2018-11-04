/*******************************************************************************
 * Copyright (c) 2014 - 2018 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.preferences;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class MenuDataMover extends DataMover implements SelectionListener {

    private Table table;

    public MenuDataMover(Table table, IDataStore store) {
        super(store);
        this.table = table;
        this.table.addSelectionListener(this);
    }

    public void widgetDefaultSelected(SelectionEvent e) {
    }

    public void widgetSelected(SelectionEvent e) {
        TableItem item = null;
        try {
            item = table.getSelection()[0];
        } catch(Throwable t) {
            setData(null);
            return;
        }
        if(item == null || !(item.getData() instanceof MenuData)) {
            setData(null);
            return;
        }
        setData((IData)item.getData());
    }
}
