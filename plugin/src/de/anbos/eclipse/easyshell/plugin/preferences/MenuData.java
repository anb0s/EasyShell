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

	// status
    private int position = 0;
    private boolean enabled = true;

    // menu
    private String id = UUID.randomUUID().toString();
    private String name = null;

    // command data
    private CommandData commandData;

    public MenuData(CommandData commandData) {
        this.commandData = commandData;
        setDefaultName();
    }

    public MenuData(MenuData data) {
        this(data.getCommandData());
        this.name = data.getName();
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
    	if( data.getPosition() == this.getPosition() &&
    	    data.getName().equals(this.getName()) &&
    	    data.getCommandData().equals(this.getCommandData())
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
        // set members
        setPosition(Integer.parseInt(tokenizer.nextToken()));
        setEnabled(Boolean.valueOf(tokenizer.nextToken()).booleanValue());
        setId(tokenizer.nextToken());
        setName(tokenizer.nextToken());
        //
        setCommandData(new CommandData(), false);
        getCommandData().deserialize(null, tokenizer, delimiter);
        return true;
    }

    public String serialize(String delimiter) {
        return Integer.toString(getPosition()) + delimiter + Boolean.toString(isEnabled())+ delimiter + getId() + delimiter + getName() + delimiter + commandData.serialize(delimiter);
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
