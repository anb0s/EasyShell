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

import de.anbos.eclipse.easyshell.plugin.Activator;

public class General extends Exception {

    private static final long serialVersionUID = -4388439082977608451L;

    public General(String string) {
        super(string);
    }

    public void logInternalError() {
        if (logEnabled()) {
            Activator.logError(Activator.getResourceString("easyshell.message.error.store.load"), this);
        }
    }

    public boolean logEnabled() {
        return true;
    }
}
