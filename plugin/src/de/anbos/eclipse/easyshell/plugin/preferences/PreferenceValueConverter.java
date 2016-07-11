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

import java.util.List;
import java.util.StringTokenizer;

public class PreferenceValueConverter {

	// Constant ----------------------------------------------------------------

	public static String VALUE_DELIMITER	= "|"; //$NON-NLS-1$
	public static String ITEM_DELIMITER		= "#"; //$NON-NLS-1$

	// Static ------------------------------------------------------------------

    public static String asCommandDataString(List<CommandData> items) {
        StringBuffer buffer = new StringBuffer();
        for(CommandData item : items) {
            buffer.append(asCommandDataString(item));
            buffer.append(ITEM_DELIMITER);
        }
        return buffer.toString();
    }

    public static String asCommandMenuDataString(List<CommandMenuData> items) {
        StringBuffer buffer = new StringBuffer();
        for(CommandMenuData item : items) {
            buffer.append(asCommandMenuDataString(item));
            buffer.append(ITEM_DELIMITER);
        }
        return buffer.toString();
    }

    public static final String asCommandDataString(CommandData data) {
        return data.serialize(VALUE_DELIMITER);
    }

    public static final String asCommandMenuDataString(CommandMenuData data) {
        return data.serialize(VALUE_DELIMITER);
    }

    public static CommandData[] asCommandDataArray(String value) {
        StringTokenizer tokenizer = new StringTokenizer(value,ITEM_DELIMITER);
        CommandData[] items = new CommandData[tokenizer.countTokens()];
        for(int i = 0 ; i < items.length ; i++) {
            items[i] = asCommandData(tokenizer.nextToken());
        }
        return items;
    }

    public static CommandMenuData[] asCommandMenuDataArray(String value) {
        StringTokenizer tokenizer = new StringTokenizer(value,ITEM_DELIMITER);
        CommandMenuData[] items = new CommandMenuData[tokenizer.countTokens()];
        for(int i = 0 ; i < items.length ; i++) {
            items[i] = asCommandMenuData(tokenizer.nextToken());
        }
        return items;
    }

    public static CommandData asCommandData(String value) {
        CommandData data = new CommandData();
        data.deserialize(value, null, VALUE_DELIMITER);
        return data;
    }

    public static CommandMenuData asCommandMenuData(String value) {
        CommandMenuData data = new CommandMenuData();
        data.deserialize(value, null, VALUE_DELIMITER);
        return data;
    }
}
