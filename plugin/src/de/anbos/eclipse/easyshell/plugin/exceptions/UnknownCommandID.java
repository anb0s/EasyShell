/*******************************************************************************
 * Copyright (c) 2014 - 2017 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.exceptions;

import java.util.HashMap;
import java.util.Map;

public class UnknownCommandID extends General {

	private static final long serialVersionUID = -934375852865621734L;
	private String  id;
	private boolean logOnce;
	static private Map<String, Boolean> map = null;

	public UnknownCommandID(String commandID, boolean logOnce) {
		super("Cannot find command data with id '" + commandID + "'");
		this.id = commandID;
		this.logOnce = logOnce;
		UnknownCommandID.addUniqueID(commandID);
	}

	public String getID() {
		return id;
	}

	static void addUniqueID(String commandID) {
		if (map == null) {
			map = new HashMap<String, Boolean>();
		}
		if (!map.containsKey(commandID)) {
			map.put(commandID, true);
		}
	}

	public boolean logEnabled() {
		boolean enabled = true;
		if (logOnce) {
			enabled = map.get(id);
			if (enabled) {
				map.put(id, false);
			}
		}
		return enabled;
	}

}
