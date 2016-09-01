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
    public static final String IMAGE_DEFAULT     = "editor.gif";
    public static final String IMAGE_OPEN        = "terminal_16x16.png";
    public static final String IMAGE_RUN         = "run_exc.gif";
    public static final String IMAGE_EXPLORE     = "fldr_obj.gif";
    public static final String IMAGE_CLIPBOARD   = "copy.gif";
    public static final String IMAGE_USER        = "environment_obj.gif";

	// Preferences
    public static final String PREF_COMMANDS_PRESET = "COMMANDS_PRESET";
	public static final String PREF_COMMANDS        = "COMMANDS";
	public static final String PREF_MENU            = "MENU";
	public static final String PREF_MIGRATED        = "MIGRATED";

	// Actions
	public static final String ACTION_UNKNOWN    = "de.anbos.eclipse.easyshell.plugin.commands.Unknown";
	public static final String ACTION_EXECUTE    = "de.anbos.eclipse.easyshell.plugin.commands.Execute";
	public static final String ACTION_CLIPBOARD  = "de.anbos.eclipse.easyshell.plugin.commands.Clipboard";
}
