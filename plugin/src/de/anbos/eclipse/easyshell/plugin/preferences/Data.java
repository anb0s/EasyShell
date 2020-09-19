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

import java.util.UUID;

public class Data implements IData {

    // internal
    private int position = 0;
    private String id = null;

    public Data(String id) {
        if (id == null || id.isEmpty()) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
    }

    public Data(String id, int position) {
        this(id);
        setPosition(position);
    }

    public Data(IData data, boolean generateNewId) {
        this(generateNewId ? null : data.getId());
    }

    public Data(IData data) {
        this(data.getId());
    }

    public Data() {
        this(null, true);
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof Data)) {
            return false;
        }
        IData data = (IData)object;
        if( data.getId().equals(this.getId())
            /*data.getPosition() == this.getPosition()*/
          )
        {
            return true;
        }
        return false;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean verify() {
        return id != null && !id.isEmpty();
    }

}
