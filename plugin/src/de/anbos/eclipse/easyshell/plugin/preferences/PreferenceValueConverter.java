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

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.internal.preferences.Base64;

public class PreferenceValueConverter {

	// Constant ----------------------------------------------------------------

	public static String VALUE_DELIMITER	= "|"; //$NON-NLS-1$
	public static String ITEM_DELIMITER		= "#"; //$NON-NLS-1$

	// Static ------------------------------------------------------------------

    public static String asString(List<PresetData> items) {
        StringBuffer buffer = new StringBuffer();
        for(PresetData item : items) {
            buffer.append(asString(item));
            buffer.append(ITEM_DELIMITER);
        }
        return buffer.toString();
    }

    public static final String asString(PresetData data) {
        String position = Integer.toString(data.getPosition());
        String enabled = Boolean.toString(data.isEnabled());
        String id = data.getId();
        String name = data.getName();
        String type = data.getType().getName();
        String value = data.getValue();
        String os = data.getOs().getName();
        return position + VALUE_DELIMITER + enabled + VALUE_DELIMITER + id + VALUE_DELIMITER + name + VALUE_DELIMITER + type + VALUE_DELIMITER + value + VALUE_DELIMITER + os + VALUE_DELIMITER;
    }

    /*
    public static final String asStringBase64(PresetData data) {
        String position = Base64.encode(Integer.toString(data.getPosition()).getBytes(StandardCharsets.UTF_8)).toString();
        String checked = Base64.encode(Boolean.toString(data.isEnabled()).getBytes(StandardCharsets.UTF_8)).toString();
        String name = Base64.encode(data.getName().getBytes(StandardCharsets.UTF_8)).toString();
        String type = Base64.encode(data.getType().getBytes(StandardCharsets.UTF_8)).toString();
        String value = Base64.encode(data.getValue().getBytes(StandardCharsets.UTF_8)).toString();
        return position + VALUE_DELIMITER + checked + VALUE_DELIMITER + name + VALUE_DELIMITER + type + VALUE_DELIMITER + value + VALUE_DELIMITER;
    }
    */

    public static PresetData[] asPresetDataArray(String value) {
        StringTokenizer tokenizer = new StringTokenizer(value,ITEM_DELIMITER);
        PresetData[] items = new PresetData[tokenizer.countTokens()];
        for(int i = 0 ; i < items.length ; i++) {
            items[i] = asPresetData(tokenizer.nextToken());
        }
        return items;
    }

    public static PresetData asPresetData(String value) {
        PresetData data = new PresetData();
        data.fillTokens(value, VALUE_DELIMITER);
        return data;
    }
}
