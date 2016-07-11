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

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class CommandMenuDataContentProvider implements IStructuredContentProvider {

    private CommandMenuDataStore store;

    public Object[] getElements(Object inputElement) {
        return store.getCommandMenuDataArray();
    }

    public void dispose() {
        store = null;
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if(newInput instanceof CommandMenuDataStore) {
            store = (CommandMenuDataStore)newInput;
        }
    }

}
