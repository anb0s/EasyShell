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
