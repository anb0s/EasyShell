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

import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class CommandDataBasic {

    // command
    private String name = "";
    private ResourceType resourceType = ResourceType.resourceTypeUnknown;
    private boolean useWorkingDirectory = false;
    private String workingDirectory = "";
    private String command = "";

    public CommandDataBasic(String name, ResourceType resType, boolean useWorkingDirectory, String workingDirectory, String command) {
        setName(name);
        setResourceType(resType);
        setUseWorkingDirectory(useWorkingDirectory);
        setWorkingDirectory(workingDirectory);
        setCommand(command);
    }


    public CommandDataBasic(CommandDataBasic commandData) {
        this(commandData.getName(), commandData.getResourceType(), commandData.isUseWorkingDirectory(), commandData.getWorkingDirectory(), commandData.getCommand());
    }

    public CommandDataBasic() {
    }

    public String getName() {
        return name;
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

    public String getCommand() {
        return command;
    }

	public void setName(String name) {
	    this.name = name;
	}

    public void setResourceType(ResourceType resType) {
        this.resourceType = resType;
    }

    public void setUseWorkingDirectory(boolean useWorkingDirectory) {
        this.useWorkingDirectory = useWorkingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        if (workingDirectory != null) {
            this.workingDirectory = workingDirectory;
        } else {
            this.workingDirectory = "${easyshell:container_loc}";
        }
    }

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean equals(Object object) {
    	if(!(object instanceof CommandDataBasic)) {
    		return false;
    	}
    	CommandDataBasic data = (CommandDataBasic)object;
    	if(data.getName().equals(this.getName()) &&
    	   data.getResourceType() == this.getResourceType() &&
    	   data.isUseWorkingDirectory() == this.isUseWorkingDirectory() &&
    	   data.getWorkingDirectory().equals(this.getWorkingDirectory()) &&
    	   data.getCommand().equals(this.getCommand())
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
		// set command data members
        setName(tokenizer.nextToken());
		setResourceType(ResourceType.getFromEnum(tokenizer.nextToken()));
		// handling of working directory
	    setUseWorkingDirectory(Boolean.valueOf(tokenizer.nextToken()).booleanValue());
	    setWorkingDirectory(tokenizer.nextToken());
		// command
		setCommand(tokenizer.nextToken());
		return true;
	}

    public boolean deserialize(String value, StringTokenizer tokenizer, String delimiter) {
        return deserialize(Version.actual, value, tokenizer, delimiter);
    }

    public String serialize(Version version, String delimiter) {
        String ret = getName() + delimiter;
        ret += getResourceType().toString() + delimiter;
        ret += Boolean.toString(isUseWorkingDirectory()) + delimiter;
        ret += getWorkingDirectory() + delimiter;
        ret += getCommand() + delimiter;
        return ret;
    }

    public String serialize(String delimiter) {
        return serialize(Version.actual, delimiter);
    }

}
