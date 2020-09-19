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
