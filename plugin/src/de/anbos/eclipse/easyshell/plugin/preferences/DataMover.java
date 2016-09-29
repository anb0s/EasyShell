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

public class DataMover {

	private IDataStore store;
	private IData data;

	public DataMover(IDataStore store) {
		this.store = store;
	}

	public void setData(IData data) {
	    this.data = data;
	}

	public void moveCurrentSelectionUp() {
		if(data == null) {
			return;
		}
		IData previousElement = store.getPreviousElement(data);
		if(previousElement == null) {
			return;
		}
		int newPosition = previousElement.getPosition();
		int oldPosition = data.getPosition();
		previousElement.setPosition(oldPosition);
		data.setPosition(newPosition);
		store.sort();
	}

	public void moveCurrentSelectionDown() {
		if(data == null) {
			return;
		}
		IData nextElement = store.getNextElement(data);
		if(nextElement == null) {
			return;
		}
		int newPosition = nextElement.getPosition();
		int oldPosition = data.getPosition();
		nextElement.setPosition(oldPosition);
		data.setPosition(newPosition);
		store.sort();
	}
}
