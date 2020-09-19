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

public class DataMover {

    private IDataStore store;
    private IData data;

    public DataMover(IDataStore store) {
        this.store = store;
    }

    public void setData(IData data) {
        this.data = data;
    }

    public void moveCurrentSelectionUp() {
        if(data == null) {
            return;
        }
        IData previousElement = store.getPreviousElement(data);
        if(previousElement == null) {
            return;
        }
        int newPosition = previousElement.getPosition();
        int oldPosition = data.getPosition();
        previousElement.setPosition(oldPosition);
        data.setPosition(newPosition);
        store.sort();
    }

    public void moveCurrentSelectionDown() {
        if(data == null) {
            return;
        }
        IData nextElement = store.getNextElement(data);
        if(nextElement == null) {
            return;
        }
        int newPosition = nextElement.getPosition();
        int oldPosition = data.getPosition();
        nextElement.setPosition(oldPosition);
        data.setPosition(newPosition);
        store.sort();
    }
}
