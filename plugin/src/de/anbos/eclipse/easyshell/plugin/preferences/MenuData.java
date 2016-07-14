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

import java.util.StringTokenizer;
import java.util.UUID;

public class MenuData {

	// internal
    private int position = 0;
    private String id = null;

    // menu
    private boolean enabled = true;
    private String name = null;
    // copy of or reference to command
    private CommandData commandData = null;

    public MenuData(String id, boolean enabled, String name, CommandData commandData, boolean setDefaultName) {
        if (id == null || id.isEmpty()) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
        this.enabled = enabled;
        this.name = name;
        setCommandData(commandData, setDefaultName);
    }

    public MenuData(String newId, CommandData commandData, boolean setDefaultName) {
        this.id = newId;
        setCommandData(commandData, setDefaultName);
    }

    public MenuData(CommandData commandData, boolean setDefaultName, boolean generateNewId) {
        this(generateNewId ? UUID.randomUUID().toString() : commandData.getId(), commandData, setDefaultName);
    }

    public MenuData(String newId, MenuData data) {
        this(newId, data.getCommandData(), false);
        this.name = data.getName();
    }

    public MenuData(MenuData data, boolean generateNewId) {
        this(generateNewId ? UUID.randomUUID().toString() : data.getId(), data);
    }

    public MenuData() {
    }

    public int getPosition() {
		return position;
	}

	public boolean isEnabled() {
		return enabled;
	}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

	public CommandData getCommandData() {
		return commandData;
	}

	public boolean equals(Object object) {
    	if(!(object instanceof MenuData)) {
    		return false;
    	}
    	MenuData data = (MenuData)object;
    	if( data.getId().equals(this.getId())
    	    /*data.getPosition() == this.getPosition() &&
    	    data.getName().equals(this.getName()) &&
    	    data.getCommandData().equals(this.getCommandData())*/
    	  )
    	{
    		return true;
    	}
    	return false;
    }

    public boolean deserialize(String value, StringTokenizer tokenizer, String delimiter) {
        if((value == null || value.length() <= 0) && tokenizer == null) {
            return false;
        }
        if (tokenizer == null) {
            tokenizer = new StringTokenizer(value,delimiter);
        }
        // set internal members
        setPosition(Integer.parseInt(tokenizer.nextToken()));
        setId(tokenizer.nextToken());
        // set menu data members
        setEnabled(Boolean.valueOf(tokenizer.nextToken()).booleanValue());
        setName(tokenizer.nextToken());
        // set command data members
        setCommandData(new CommandData(), false);
        getCommandData().deserialize(null, tokenizer, delimiter);
        return true;
    }

    public String serialize(String delimiter) {
        return Integer.toString(getPosition()) + delimiter + getId() + delimiter + Boolean.toString(isEnabled())+ delimiter + getName() + delimiter + commandData.serialize(delimiter);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultName() {
        this.name = getCommandData().getCommandType().getName() + ": " + getCommandData().getName();
    }

    public void setCommandData(CommandData commandData, boolean setDefaultName) {
        this.commandData = commandData;
        if (setDefaultName) {
            setDefaultName();
        }
    }

}
