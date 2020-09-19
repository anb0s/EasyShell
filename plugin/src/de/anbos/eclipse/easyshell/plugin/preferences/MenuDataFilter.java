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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import de.anbos.eclipse.easyshell.plugin.exceptions.UnknownCommandID;

public class MenuDataFilter extends ViewerFilter {

    private String searchString;

    public void setSearchText(String s) {
        // remove not valid chars
        s = s.replaceAll("[\\*|\\.|\\(|\\)|\\?]","");
        // add pre and post fix that it can be used for case-insensitive matching
        this.searchString = "(?i).*" + s + ".*";
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (searchString == null || searchString.length() == 0) {
            return true;
        }
        MenuData data = (MenuData) element;
        if (data.getNameExpanded().matches(searchString)) {
            return true;
        }
        try {
            if (data.getCommandData().getName().matches(searchString)) {
                return true;
            }
            if (data.getCommandData().getCommand().matches(searchString)) {
                return true;
            }
        } catch (UnknownCommandID e) {
            e.logInternalError();
        }
        return false;
    }

}
