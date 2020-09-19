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

package de.anbos.eclipse.easyshell.plugin;

public interface Constants {

    // Plugin
    public static final String PLUGIN_ID = "de.anbos.eclipse.easyshell.plugin";

    // Icons
    public static final String IMAGE_EXT         = ".png";
    public static final String IMAGE_PATH        = "images/";
    // categories
    public static final String IMAGE_NONE        = "none";
    public static final String IMAGE_DEFAULT     = "default";
    public static final String IMAGE_OPEN        = "open";
    public static final String IMAGE_RUN         = "run";
    public static final String IMAGE_EXPLORE     = "explore";
    public static final String IMAGE_CLIPBOARD   = "clipboard";
    public static final String IMAGE_USER        = "user";
    // other
    public static final String IMAGE_EASYSHELL   = "easyshell";
    public static final String IMAGE_ECLIPSE     = "eclipse";

    // Preferences
    public static final String PREF_COMMANDS_PRESET = "COMMANDS_PRESET";
    public static final String PREF_COMMANDS_MODIFY = "COMMANDS_MODIFY";
    public static final String PREF_COMMANDS_USER   = "COMMANDS_USER";
    public static final String PREF_COMMANDS_OLD	= "COMMANDS";
    public static final String PREF_GENERAL   		= "GENERAL";
    public static final String PREF_MENU            = "MENU";
    public static final String PREF_MIGRATED        = "MIGRATED";

    // Actions
    public static final String ACTION_UNKNOWN    = "de.anbos.eclipse.easyshell.plugin.commands.Unknown";
    public static final String ACTION_EXECUTE    = "de.anbos.eclipse.easyshell.plugin.commands.Execute";
    public static final String ACTION_CLIPBOARD  = "de.anbos.eclipse.easyshell.plugin.commands.Clipboard";


}
