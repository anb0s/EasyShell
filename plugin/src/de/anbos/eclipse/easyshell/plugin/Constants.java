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

package de.anbos.eclipse.easyshell.plugin;

public interface Constants {

    // Plugin
    public static final String PLUGIN_ID = "de.anbos.eclipse.easyshell.plugin";

    // Images
    public static final String IMAGE_PATH        = "icons/";
    public static final String IMAGE_UNKNOWN     = "sample.gif";
    public static final String IMAGE_OPEN        = "prompt.gif";
    public static final String IMAGE_RUN         = "run_exc.gif";
    public static final String IMAGE_EXPLORE     = "fldr_obj.gif";
    public static final String IMAGE_CLIPBOARD   = "copy.gif";
    public static final String IMAGE_OTHER       = "editor.gif";

	// Preferences
    // version with index = 0 is the actual one
    // do not delete old entries !!!
    public static final String[] PREF_VERSIONS   = {
            "v2_0_002",
            "v2_0_001",
            "v1_4"
    };
    public static final String PREF_COMMANDS_PRESET = "COMMANDS_PRESET";
	public static final String PREF_COMMANDS        = "COMMANDS";
	public static final String PREF_MENU            = "MENU";
	public static final String PREF_MIGRATED        = "MIGRATED";

	// Actions
	public static final String ACTION_UNKNOWN    = "de.anbos.eclipse.easyshell.plugin.commands.Unknown";
	public static final String ACTION_OPEN       = "de.anbos.eclipse.easyshell.plugin.commands.Open";
	public static final String ACTION_RUN        = "de.anbos.eclipse.easyshell.plugin.commands.Run";
	public static final String ACTION_EXPLORE    = "de.anbos.eclipse.easyshell.plugin.commands.Explore";
	public static final String ACTION_CLIPBOARD  = "de.anbos.eclipse.easyshell.plugin.commands.Clipboard";
	public static final String ACTION_OTHER      = "de.anbos.eclipse.easyshell.plugin.commands.Other";
}
