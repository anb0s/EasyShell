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

import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.OS;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

public class CommandData {

    // internal
    private int position = 0;
    private String id = UUID.randomUUID().toString();
    // command
    private PresetType presetType = PresetType.presetUser;
    private OS os = OS.osUnknown;
    private String name = "MyNewCommand";
    private ResourceType resType = ResourceType.resourceFileOrFolder;
    private CommandType cmdType = CommandType.commandTypeOther;
    private String command = "my_new_command";

    public CommandData(PresetType presetType, OS os, String name, ResourceType resType, CommandType cmdType, String command) {
        this.presetType = presetType;
        this.os = os;
        this.name = name;
        this.resType = resType;
        this.cmdType = cmdType;
        this.command = command;
    }

    public CommandData(CommandData commandData) {
        this.presetType = commandData.getPresetType();
        this.os = commandData.getOS();
        this.name = commandData.getName();
        this.resType = commandData.getResourceType();
        this.cmdType = commandData.getCommandType();
        this.command = commandData.getCommand();
    }

    public CommandData() {
    }

    public int getPosition() {
        return position;
    }

	public String getId() {
		return id;
	}

    public String getName() {
        return name;
    }

    public OS getOS() {
        return os;
    }

    public PresetType getPresetType() {
        return presetType;
    }

    public ResourceType getResourceType() {
        return resType;
    }

    public CommandType getCommandType() {
        return cmdType;
    }

    public String getCommand() {
        return command;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setId(String id) {
        this.id = id;
    }

	public void setName(String name) {
	    this.name = name;
	}

    public void setOs(OS os) {
        this.os = os;
    }

    public void setPresetType(PresetType presetType) {
        this.presetType = presetType;
    }

    public void setResourceType(ResourceType resType) {
        this.resType = resType;
    }

    public void setCommandType(CommandType cmdType) {
        this.cmdType = cmdType;
    }

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean equals(Object object) {
    	if(!(object instanceof CommandData)) {
    		return false;
    	}
    	CommandData data = (CommandData)object;
    	if(data.getPosition() == this.getPosition() &&
    	   data.getName().equals(this.getName()) &&
    	   data.getOS() == this.getOS() &&
    	   data.getPresetType() == this.getPresetType() &&
    	   data.getResourceType() == this.getResourceType() &&
    	   data.getCommandType() == this.getCommandType() &&
    	   data.getCommand().equals(this.getCommand()
    	   )
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
		//setId(tokenizer.nextToken());
        setPresetType(PresetType.getFromEnum(tokenizer.nextToken()));
        setOs(OS.getFromEnum(tokenizer.nextToken()));
		setName(tokenizer.nextToken());
		setResourceType(ResourceType.getFromEnum(tokenizer.nextToken()));
		setCommandType(CommandType.getFromEnum(tokenizer.nextToken()));
		setCommand(tokenizer.nextToken());
		return true;
	}

    public String serialize(String delimiter) {
        return Integer.toString(getPosition()) + delimiter + /*getId() + delimiter +*/ getPresetType().toString() + delimiter + getOS().toString() + delimiter + getName() + delimiter + getResourceType().toString() + delimiter + getCommandType().toString() + delimiter + getCommand() + delimiter;
    }

    public String getTypeIcon() {
        return getCommandType().getIcon();
    }

    public String getTypeAction() {
        return getCommandType().getAction();
    }

}
