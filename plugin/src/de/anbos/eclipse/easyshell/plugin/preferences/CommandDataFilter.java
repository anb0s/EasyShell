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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class CommandDataFilter extends ViewerFilter {

    private String searchString;

    public void setSearchText(String s) {
        // remove not valid chars
        s = s.replaceAll("[\\*|\\.|\\(|\\)|\\?]","");
        // add pre and post fix that it can be used for case-insensitive matching
        this.searchString = "(?i).*" + s + ".*";
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if(!(element instanceof CommandData)) {
            return false;
        }
        CommandData data = (CommandData) element;
        boolean selected = isSelected(data);
           data.setSelected(selected);
           return selected;
    }

    private boolean isSelected(CommandData data) {
        if (searchString == null || searchString.length() == 0) {
            return true;
        }
        if (data.getCategory().getName().matches(searchString)) {
            return true;
        }
        if (data.getName().matches(searchString)) {
            return true;
        }
        if (data.getPresetType().getName().matches(searchString)) {
            return true;
        }
        if (data.getCommand().matches(searchString)) {
            return true;
        }
        return false;
    }
}
