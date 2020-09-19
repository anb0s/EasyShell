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

package de.anbos.eclipse.easyshell.plugin.exceptions;

import java.util.HashMap;
import java.util.Map;

public class UnknownCommandID extends General {

    private static final long serialVersionUID = -934375852865621734L;
    private String  id;
    private boolean logOnce;
    static private Map<String, Boolean> map = null;

    public UnknownCommandID(String commandID, boolean logOnce) {
        super("Cannot find command data with id '" + commandID + "'");
        this.id = commandID;
        this.logOnce = logOnce;
        UnknownCommandID.addUniqueID(commandID);
    }

    public String getID() {
        return id;
    }

    static void addUniqueID(String commandID) {
        if (map == null) {
            map = new HashMap<String, Boolean>();
        }
        if (!map.containsKey(commandID)) {
            map.put(commandID, true);
        }
    }

    public boolean logEnabled() {
        boolean enabled = true;
        if (logOnce) {
            enabled = map.get(id);
            if (enabled) {
                map.put(id, false);
            }
        }
        return enabled;
    }

}
