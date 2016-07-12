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

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class MenuDataMover implements SelectionListener {

	private Table table;
	private MenuDataStore store;

	private MenuData currentSelection;

	public MenuDataMover(Table table, MenuDataStore store) {
		this.table = table;
		this.store = store;
		table.addSelectionListener(this);
	}

	public void moveCurrentSelectionUp() {
		if(currentSelection == null) {
			return;
		}
		MenuData previousElement = store.getPreviousElement(currentSelection);
		if(previousElement == null) {
			return;
		}
		int newPosition = previousElement.getPosition();
		int oldPosition = currentSelection.getPosition();
		previousElement.setPosition(oldPosition);
		currentSelection.setPosition(newPosition);
	}

	public void moveCurrentSelectionDown() {
		if(currentSelection == null) {
			return;
		}
		MenuData nextElement = store.getNextElement(currentSelection);
		if(nextElement == null) {
			return;
		}
		int newPosition = nextElement.getPosition();
		int oldPosition = currentSelection.getPosition();
		nextElement.setPosition(oldPosition);
		currentSelection.setPosition(newPosition);
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		TableItem item = null;
		try {
			item = table.getSelection()[0];
		} catch(Throwable t) {
			currentSelection = null;
			return;
		}
		if(item == null || !(item.getData() instanceof MenuData)) {
			currentSelection = null;
			return;
		}
		currentSelection = (MenuData)item.getData();
	}
}
