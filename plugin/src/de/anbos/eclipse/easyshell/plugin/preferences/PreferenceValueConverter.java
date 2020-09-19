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

import java.util.List;
import java.util.StringTokenizer;

import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class PreferenceValueConverter {

    // Constant ----------------------------------------------------------------

    public static String VALUE_DELIMITER	= "|"; //$NON-NLS-1$
    public static String ITEM_DELIMITER		= "#"; //$NON-NLS-1$

    // Static ------------------------------------------------------------------

    public static String asCommandDataBasicString(List<CommandDataBasic> items) {
        StringBuffer buffer = new StringBuffer();
        for(CommandDataBasic item : items) {
            buffer.append(asCommandDataBasicString(item));
            buffer.append(ITEM_DELIMITER);
        }
        return buffer.toString();
    }

    public static String asCommandDataString(List<CommandData> items, boolean modifyDataOnly) {
        StringBuffer buffer = new StringBuffer();
        for(CommandData item : items) {
            buffer.append(asCommandDataString(item, modifyDataOnly));
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

    public static final String asCommandDataBasicString(CommandDataBasic data) {
           return data.serialize(VALUE_DELIMITER);
    }

    public static final String asCommandDataString(CommandData data, boolean modifyDataOnly) {
        if (modifyDataOnly) {
            return asCommandDataBasicString(data.getModifyData());
        } else {
            return data.serialize(VALUE_DELIMITER);
        }
    }

    public static final String asMenuDataString(MenuData data) {
        return data.serialize(VALUE_DELIMITER);
    }

    public static String asGeneralDataString(GeneralData data) {
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

    public static CommandDataBasic[] asCommandDataBasicArray(String value) {
        StringTokenizer tokenizer = new StringTokenizer(value,ITEM_DELIMITER);
        CommandDataBasic[] items = new CommandDataBasic[tokenizer.countTokens()];
        for(int i = 0 ; i < items.length ; i++) {
            items[i] = asCommandDataBasic(tokenizer.nextToken());
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

    public static CommandDataBasic asCommandDataBasic(String value) {
        CommandDataBasic data = new CommandDataBasic();
        data.deserialize(value, null, VALUE_DELIMITER);
        return data;
    }

    public static MenuData asMenuData(String value) {
        MenuData data = new MenuData();
        data.deserialize(value, null, VALUE_DELIMITER);
        return data;
    }

    public static GeneralData asGeneralData(String value) {
        GeneralData data = new GeneralData();
        data.deserialize(value, null, VALUE_DELIMITER);
        return data;
    }

    public static CommandData migrateCommandData(Version version, String value) {
        CommandData data = new CommandData();
        data.deserialize(version, value, null, VALUE_DELIMITER);
        // special handling
        if (version == Version.v2_0_001) {
            // skip commands from preset
            if (data.getPresetType() == PresetType.presetPlugin) {
                data = null;
            }
        }
        return data;
    }

    public static CommandDataBasic migrateCommandDataBasic(Version version, String value) {
        CommandDataBasic data = new CommandDataBasic();
        data.deserialize(version, value, null, VALUE_DELIMITER);
        return data;
    }

    public static MenuData migrateMenuData(Version version, String value) {
        MenuData data = new MenuData();
        data.deserialize(version, value, null, VALUE_DELIMITER);
        return data;
    }

    public static String migrateCommandDataList(Version version, String value, boolean modifyDataOnly) {
        StringBuffer buffer = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(value, ITEM_DELIMITER);
        int num = tokenizer.countTokens();
        for(int i = 0 ; i < num; i++) {
            CommandData data = migrateCommandData(version, tokenizer.nextToken());
            if (data != null) {
                buffer.append(asCommandDataString(data, modifyDataOnly));
                buffer.append(ITEM_DELIMITER);
            }
        }
        return buffer.toString();
    }

    public static String migrateCommandDataBasicList(Version version, String value) {
        StringBuffer buffer = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(value, ITEM_DELIMITER);
        int num = tokenizer.countTokens();
        for(int i = 0 ; i < num; i++) {
            CommandDataBasic data = migrateCommandDataBasic(version, tokenizer.nextToken());
            if (data != null) {
                buffer.append(asCommandDataBasicString(data));
                buffer.append(ITEM_DELIMITER);
            }
        }
        return buffer.toString();
    }

    public static String migrateMenuDataList(Version version, String value) {
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
