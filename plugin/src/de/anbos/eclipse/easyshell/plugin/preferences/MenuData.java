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

import de.anbos.eclipse.easyshell.plugin.types.MenuNameType;

public class MenuData {

	// internal
    private int position = 0;
    private String id = null;

    // menu
    private boolean enabled = true;
    private MenuNameType nameType;
    private String namePattern = null;
    // copy of or reference to command
    private CommandData commandData = null;

    public MenuData(String id, boolean enabled, MenuNameType nameType, String namePattern, CommandData commandData) {
        if (id == null || id.isEmpty()) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
        this.enabled = enabled;
        this.nameType = nameType;
        this.namePattern = namePattern;
        setCommandData(commandData);
    }

    public MenuData(String newId, CommandData commandData) {
        this.id = newId;
        setNameType(MenuNameType.menuNameTypeGeneric1);
        setCommandData(commandData);
    }

    public MenuData(CommandData commandData, boolean generateNewId) {
        this(generateNewId ? UUID.randomUUID().toString() : commandData.getId(), commandData);
    }

    public MenuData(String newId, MenuData data) {
        this(newId, data.getCommandData());
        this.nameType = data.getNameType();
        this.namePattern = data.getNamePattern();
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

    public MenuNameType getNameType() {
        return nameType;
    }

    public String getNamePattern() {
        return namePattern;
    }

    public String getNameExpanded() {
        return namePattern.replace("${easyshell:command_type}", getCommandData().getCommandType().getName()).replace("${easyshell:command_name}", getCommandData().getName()).replace("${easyshell:command_os}", getCommandData().getOS().getName());
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
        setNameType(MenuNameType.getFromEnum(tokenizer.nextToken()));
        setNamePattern(tokenizer.nextToken());
        // set command data members
        setCommandData(new CommandData());
        getCommandData().deserialize(null, tokenizer, delimiter);
        return true;
    }

    public boolean deserialize_v2_0_001(String value, StringTokenizer tokenizer, String delimiter) {
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
        // -------------------------------------------------
        // convert handling
        MenuNameType nameType = MenuNameType.menuNameTypeUser;
        String namePatternReaded = tokenizer.nextToken();
        // -------------------------------------------------
        // set command data members
        setCommandData(new CommandData());
        getCommandData().deserialize(null, tokenizer, delimiter);
        // -------------------------------------------------
        // convert handling
        // check if readed name is the same, like expanded from patterns
        for (MenuNameType type : MenuNameType.getAsList()) {
            setNamePattern(type.getPattern()); // set temporary
            if (getNameExpanded().equals(namePatternReaded)) {
                nameType = type;
                break;
            }
        }
        setNameType(nameType);
        // if not found set the readed value
        if (nameType == MenuNameType.menuNameTypeUser) {
            setNamePattern(namePatternReaded);
        }
        // -------------------------------------------------
        return true;
    }

    public String serialize(String delimiter) {
        return Integer.toString(getPosition()) + delimiter + getId() + delimiter + Boolean.toString(isEnabled()) + delimiter + getNameType().toString() + delimiter + getNamePattern() + delimiter + commandData.serialize(delimiter);
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

    public void setNameType(MenuNameType nameType) {
        this.nameType = nameType;
        if (nameType != MenuNameType.menuNameTypeUser) {
            setNamePattern(nameType.getPattern());
        }
    }

    public void setNamePattern(String namePattern) {
        this.namePattern = namePattern;
    }

    public void setCommandData(CommandData commandData) {
        this.commandData = commandData;
    }

}
