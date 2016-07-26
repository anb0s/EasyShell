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
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class MenuData extends Data {

    // menu
    private boolean enabled = true;
    private MenuNameType nameType = MenuNameType.menuNameTypeUnknown;
    private String namePattern = "";
    // copy of or reference to command
    private String commandId = null;

    public MenuData(String id, boolean enabled, MenuNameType nameType, String namePattern, String commandId) {
        super(id);
        this.enabled = enabled;
        this.nameType = nameType;
        this.namePattern = namePattern;
        this.commandId = commandId;
    }

    public MenuData(String newId, String commandId) {
        super(newId);
        setNameType(MenuNameType.menuNameTypeGeneric1);
        setCommandId(commandId);
    }

    public MenuData(String commandId, boolean generateNewId) {
        this(generateNewId ? UUID.randomUUID().toString() : commandId, commandId);
    }

    public MenuData(String newId, MenuData data) {
        this(newId, data.getCommandId());
        this.enabled = data.isEnabled();
        this.nameType = data.getNameType();
        this.namePattern = data.getNamePattern();
    }

    public MenuData(MenuData data, boolean generateNewId) {
        this(generateNewId ? UUID.randomUUID().toString() : data.getId(), data);
    }

    public MenuData() {
    }

	public boolean isEnabled() {
		return enabled;
	}

    public MenuNameType getNameType() {
        return nameType;
    }

    public String getNamePattern() {
        return namePattern;
    }

    public String getNameExpanded() {
        String expanded = namePattern;
        expanded = expanded.replace("${easyshell:command_category}", getCommandData().getCategory().getName());
        expanded = expanded.replace("${easyshell:command_type}", getCommandData().getCommandType().getName());
        expanded = expanded.replace("${easyshell:command_name}", getCommandData().getName());
        expanded = expanded.replace("${easyshell:command_os}", getCommandData().getOs().getName());
        return expanded;
    }

    public String getCommandId() {
        return commandId;
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

    public boolean deserialize(Version version, String value, StringTokenizer tokenizer, String delimiter) {
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
        // menu name type handling
        MenuNameType nameType = MenuNameType.menuNameTypeUser;
        if (version.getId() >= Version.v2_0_002.getId()) {
            nameType = MenuNameType.getFromEnum(tokenizer.nextToken());
        }
        String namePatternReaded = tokenizer.nextToken();
        // -------------------------------------------------
        // read new id
        if (version.getId() >= Version.v2_0_003.getId()) {
            // read the new one
            setCommandId(tokenizer.nextToken());
            setNameType(nameType);
            setNamePattern(namePatternReaded);
        } else {
            // read previous command data members
            CommandData oldData = new CommandData();
            oldData.deserialize(version, null, tokenizer, delimiter);
            setCommandId(oldData.getId());
            // menu name type handling
            // set name type and read the old name as pattern and convert to new
            if (version.getId() >= Version.v2_0_002.getId()) {
                setNameType(nameType);
                if (nameType == MenuNameType.menuNameTypeUser) {
                    setNamePattern(namePatternReaded);
                } else {
                    setNamePattern(nameType.getPattern());
                }
            } else {
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
            }
        }
        return true;
    }

    public boolean deserialize(String value, StringTokenizer tokenizer, String delimiter) {
        return deserialize(Version.actual, value, tokenizer, delimiter);
    }

    public String serialize(Version version, String delimiter) {
        String ret = Integer.toString(getPosition()) + delimiter;
        ret += getId() + delimiter;
        ret += Boolean.toString(isEnabled()) + delimiter;
        if (version.getId() >= Version.v2_0_002.getId()) {
            ret += getNameType().toString() + delimiter;
        }
        ret += getNamePattern() + delimiter;
        if (version.getId() >= Version.v2_0_003.getId()) {
            ret += getCommandId() + delimiter;
        } else {
            ret += getCommandData().serialize(version, delimiter);
        }
        return ret;
    }

    public String serialize(String delimiter) {
        return serialize(Version.actual, delimiter);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public CommandData getCommandData() {
        return CommandDataStore.instance().getById(getCommandId());
    }

}
