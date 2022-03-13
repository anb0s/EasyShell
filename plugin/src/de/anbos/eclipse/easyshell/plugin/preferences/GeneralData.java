/**
 * Copyright (c) 2014-2022 Andre Bossert <anb0s@anbos.de>.
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

import java.util.StringTokenizer;

import de.anbos.eclipse.easyshell.plugin.types.Debug;
import de.anbos.eclipse.easyshell.plugin.types.CheckBox;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class GeneralData {

    private Debug debug = Debug.debugNo;
    private CheckBox toolTipAll = CheckBox.yes;
    private CheckBox toolTipClipboard = CheckBox.yes;
    private CheckBox toolTipError = CheckBox.yes;
    private CheckBox menuAll = CheckBox.yes;
    private CheckBox menuPopup = CheckBox.yes;
    private CheckBox menuMain = CheckBox.no;

    public GeneralData(Debug debug, CheckBox toolTipAll, CheckBox toolTipClipboard, CheckBox toolTipError) {
        setDebug(debug);
        setToolTipAll(toolTipAll);
        setToolTipClipboard(toolTipClipboard);
        setToolTipError(toolTipError);
    }

    public GeneralData() {
    }

    public boolean equals(Object object) {
        if(!(object instanceof GeneralData)) {
            return false;
        }
        GeneralData data = (GeneralData)object;
        if(data.getDebug() == this.getDebug() &&
           data.getToolTipAll() == this.getToolTipAll() &&
           data.getToolTipClipboard() == this.getToolTipClipboard() &&
           data.getToolTipError() == this.getToolTipError() &&
           data.getMenuAll() == this.getMenuAll() &&
           data.getMenuPopup() == this.getMenuPopup() &&
           data.getMenuMain() == this.getMenuMain()
          )
        {
            return true;
        }
        return false;
    }

    public boolean deserialize(Version version, String value, StringTokenizer tokenizer, String delimiter) {
        if((value == null || value.length() <= 0) && tokenizer == null) {
            return false;
        }
        if (tokenizer == null) {
            tokenizer = new StringTokenizer(value,delimiter);
        }
        // set internal members
        setDebug(Debug.getFromEnum(tokenizer.nextToken()));
        // tooltip
        if (version.getId() >= Version.v2_1_002.getId()) {
            setToolTipAll(CheckBox.getFromEnum(tokenizer.nextToken()));
            setToolTipClipboard(CheckBox.getFromEnum(tokenizer.nextToken()));
            setToolTipError(CheckBox.getFromEnum(tokenizer.nextToken()));
        }
        // menu
        if (version.getId() >= Version.v2_2_001.getId()) {
            setMenuAll(CheckBox.getFromEnum(tokenizer.nextToken()));
            setMenuPopup(CheckBox.getFromEnum(tokenizer.nextToken()));
            setMenuMain(CheckBox.getFromEnum(tokenizer.nextToken()));
        }
        return true;
    }

    public boolean deserialize(String value, StringTokenizer tokenizer, String delimiter) {
        return deserialize(Version.actual, value, tokenizer, delimiter);
    }

    public String serialize(Version version, String delimiter) {
        String ret = "";
        ret += getDebug().toString() + delimiter;
        ret += getToolTipAll().toString() + delimiter;
        ret += getToolTipClipboard().toString() + delimiter;
        ret += getToolTipError().toString() + delimiter;
        ret += getMenuAll().toString() + delimiter;
        ret += getMenuPopup().toString() + delimiter;
        ret += getMenuMain().toString() + delimiter;
        return ret;
    }

    public String serialize(String delimiter) {
        return serialize(Version.actual, delimiter);
    }

    public Debug getDebug() {
        return debug;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }

    public CheckBox getToolTipAll() {
        return toolTipAll;
    }

    public void setToolTipAll(CheckBox tooltip) {
        this.toolTipAll = tooltip;
    }

    public CheckBox getToolTipClipboard() {
        return toolTipClipboard;
    }

    public void setToolTipClipboard(CheckBox tooltip) {
        this.toolTipClipboard = tooltip;
    }

    public CheckBox getToolTipError() {
        return toolTipError;
    }

    public void setToolTipError(CheckBox tooltip) {
        this.toolTipError = tooltip;
    }

    public CheckBox getMenuAll() {
        return menuAll;
    }

    public void setMenuAll(CheckBox menu) {
        this.menuAll = menu;
    }

    public CheckBox getMenuPopup() {
        return menuPopup;
    }

    public void setMenuPopup(CheckBox menu) {
        this.menuPopup = menu;
    }

    public CheckBox getMenuMain() {
        return menuMain;
    }

    public void setMenuMain(CheckBox menu) {
        this.menuMain = menu;
    }

}
