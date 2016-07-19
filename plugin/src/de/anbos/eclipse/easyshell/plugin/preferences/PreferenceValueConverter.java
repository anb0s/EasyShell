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

import de.anbos.eclipse.easyshell.plugin.types.PresetType;

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

    public static String asMenuDataString(List<MenuData> items) {
        StringBuffer buffer = new StringBuffer();
        for(MenuData item : items) {
            buffer.append(asMenuDataString(item));
            buffer.append(ITEM_DELIMITER);
        }
        return buffer.toString();
    }

    public static final String asCommandDataString(CommandData data) {
        return data.serialize(VALUE_DELIMITER);
    }

    public static final String asMenuDataString(MenuData data) {
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

    public static MenuData[] asMenuDataArray(String value) {
        StringTokenizer tokenizer = new StringTokenizer(value,ITEM_DELIMITER);
        MenuData[] items = new MenuData[tokenizer.countTokens()];
        for(int i = 0 ; i < items.length ; i++) {
            items[i] = asMenuData(tokenizer.nextToken());
        }
        return items;
    }

    public static CommandData asCommandData(String value) {
        CommandData data = new CommandData();
        data.deserialize(value, null, VALUE_DELIMITER);
        return data;
    }

    public static MenuData asMenuData(String value) {
        MenuData data = new MenuData();
        data.deserialize(value, null, VALUE_DELIMITER);
        return data;
    }

    public static CommandData migrateCommandData(String version, String value) {
        CommandData data = new CommandData();
        if (version.equals("v2_0_001")) {
            data.deserialize_v2_0_001(value, null, VALUE_DELIMITER);
            // skip commands from preset
            if (data.getPresetType() == PresetType.presetPlugin) {
                data = null;
            }
        }
        return data;
    }

    public static MenuData migrateMenuData(String version, String value) {
        MenuData data = new MenuData();
        if (version.equals("v2_0_001")) {
            data.deserialize_v2_0_001(value, null, VALUE_DELIMITER);
        }
        return data;
    }

    public static String migrateCommandDataList(String version, String value) {
        StringBuffer buffer = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(value, ITEM_DELIMITER);
        int num = tokenizer.countTokens();
        for(int i = 0 ; i < num; i++) {
            CommandData data = migrateCommandData(version, tokenizer.nextToken());
            if (data != null) {
                buffer.append(asCommandDataString(data));
                buffer.append(ITEM_DELIMITER);
            }
        }
        return buffer.toString();
    }

    public static String migrateMenuDataList(String version, String value) {
        StringBuffer buffer = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(value, ITEM_DELIMITER);
        int num = tokenizer.countTokens();
        for(int i = 0 ; i < num; i++) {
            MenuData data = migrateMenuData(version, tokenizer.nextToken());
            if (data != null) {
                buffer.append(asMenuDataString(data));
                buffer.append(ITEM_DELIMITER);
            }
        }
        return buffer.toString();
    }

}
