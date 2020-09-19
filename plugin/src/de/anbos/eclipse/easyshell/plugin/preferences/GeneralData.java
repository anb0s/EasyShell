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

import java.util.StringTokenizer;

import de.anbos.eclipse.easyshell.plugin.types.Debug;
import de.anbos.eclipse.easyshell.plugin.types.Tooltip;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class GeneralData {

    private Debug debug = Debug.debugNo;
    private Tooltip toolTipAll = Tooltip.tooltipYes;
    private Tooltip toolTipClipboard = Tooltip.tooltipYes;
    private Tooltip toolTipError = Tooltip.tooltipYes;

    public GeneralData(Debug debug, Tooltip toolTipAll, Tooltip toolTipClipboard, Tooltip toolTipError) {
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
           data.getToolTipError() == this.getToolTipError()
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
            setToolTipAll(Tooltip.getFromEnum(tokenizer.nextToken()));
            setToolTipClipboard(Tooltip.getFromEnum(tokenizer.nextToken()));
            setToolTipError(Tooltip.getFromEnum(tokenizer.nextToken()));
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

    public Tooltip getToolTipAll() {
        return toolTipAll;
    }

    public void setToolTipAll(Tooltip tooltip) {
        this.toolTipAll = tooltip;
    }

    public Tooltip getToolTipClipboard() {
        return toolTipClipboard;
    }

    public void setToolTipClipboard(Tooltip tooltip) {
        this.toolTipClipboard = tooltip;
    }

    public Tooltip getToolTipError() {
        return toolTipError;
    }

    public void setToolTipError(Tooltip tooltip) {
        this.toolTipError = tooltip;
    }

}
