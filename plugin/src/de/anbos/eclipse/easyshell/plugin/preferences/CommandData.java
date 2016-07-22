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
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class CommandData extends Data {

    // command
    private PresetType presetType = PresetType.presetUnknown;
    private OS os = OS.osUnknown;
    private String name = "";
    private ResourceType resourceType = ResourceType.resourceTypeUnknown;
    private boolean useWorkingDirectory = false;
    private String workingDirectory = "";
    private CommandType commandType = CommandType.commandTypeUnknown;
    private String command = "";

    public CommandData(String id, PresetType presetType, OS os, String name, ResourceType resType, boolean useWorkingDirectory, String workingDirectory, CommandType cmdType, String command) {
        super(id);
        this.presetType = presetType;
        this.os = os;
        this.name = name;
        this.resourceType = resType;
        this.useWorkingDirectory = useWorkingDirectory;
        this.workingDirectory = workingDirectory;
        this.commandType = cmdType;
        this.command = command;
    }

    public CommandData(String id, PresetType presetType, OS os, String name, ResourceType resType, CommandType cmdType, String command) {
        this(id, presetType, os, name, resType, false, "${easyshell:container_loc}", cmdType, command);
    }

    public CommandData(CommandData commandData, String newId) {
        this(newId, commandData.getPresetType(), commandData.getOs(), commandData.getName(), commandData.getResourceType(), commandData.isUseWorkingDirectory(), commandData.getWorkingDirectory(), commandData.getCommandType(), commandData.getCommand());
    }

    public CommandData(CommandData commandData, boolean generateNewId) {
        this(commandData, generateNewId ? UUID.randomUUID().toString() : commandData.getId());
    }

    public CommandData() {
    }

    public String getName() {
        return name;
    }

    public OS getOs() {
        return os;
    }

    public PresetType getPresetType() {
        return presetType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public boolean isUseWorkingDirectory() {
        return useWorkingDirectory;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getCommand() {
        return command;
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
        this.resourceType = resType;
    }

    public void setUseWorkingDirectory(boolean useWorkingDirectory) {
        this.useWorkingDirectory = useWorkingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public void setCommandType(CommandType cmdType) {
        this.commandType = cmdType;
    }

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean equals(Object object) {
    	if(!(object instanceof CommandData)) {
    		return false;
    	}
    	CommandData data = (CommandData)object;
    	if(data.getId().equals(this.getId())
    	   /*data.getPosition() == this.getPosition() &&*/
    	   /*data.getName().equals(this.getName()) &&
    	   data.getOS() == this.getOS() &&
    	   data.getPresetType() == this.getPresetType() &&
    	   data.getResourceType() == this.getResourceType() &&
    	   data.getCommandType() == this.getCommandType() &&
    	   data.getCommand().equals(this.getCommand()*/
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
		// set command data members
        setPresetType(PresetType.getFromEnum(tokenizer.nextToken()));
        setOs(OS.getFromEnum(tokenizer.nextToken()));
		setName(tokenizer.nextToken());
		setResourceType(ResourceType.getFromEnum(tokenizer.nextToken()));
		// handling of working directory
		if (version.getId() >= Version.v2_0_003.getId()) {
		    setUseWorkingDirectory(Boolean.valueOf(tokenizer.nextToken()).booleanValue());
		    setWorkingDirectory(tokenizer.nextToken());
		} else {
		    setUseWorkingDirectory(false);
		    setWorkingDirectory("${easyshell:container_loc}");
		}
		// go on compatible
		setCommandType(CommandType.getFromEnum(tokenizer.nextToken()));
		setCommand(tokenizer.nextToken());
		return true;
	}

    public boolean deserialize(String value, StringTokenizer tokenizer, String delimiter) {
        return deserialize(Version.actual, value, tokenizer, delimiter);
    }

    public String serialize(Version version, String delimiter) {
        String ret = Integer.toString(getPosition()) + delimiter;
        ret += getId() + delimiter;
        ret += getPresetType().toString() + delimiter;
        ret += getOs().toString() + delimiter;
        ret += getName() + delimiter;
        ret += getResourceType().toString() + delimiter;
        if (version.getId() >= Version.v2_0_003.getId()) {
            ret += Boolean.toString(isUseWorkingDirectory()) + delimiter;
            ret += getWorkingDirectory() + delimiter;
        }
        ret += getCommandType().toString() + delimiter;
        ret += getCommand() + delimiter;
        return ret;
    }

    public String serialize(String delimiter) {
        return serialize(Version.actual, delimiter);
    }

}
