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

package de.anbos.eclipse.easyshell.plugin.exceptions;

public class UnknownCommandID extends General {

	private static final long serialVersionUID = -934375852865621734L;
	private String id;

	public UnknownCommandID(String commandID) {
		super("Cannot find command data with id '" + commandID + "'");
		this.id = commandID;
	}

	public String getID() {
		return id;
	}

}
