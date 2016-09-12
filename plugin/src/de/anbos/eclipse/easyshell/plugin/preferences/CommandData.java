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

import org.eclipse.swt.graphics.Image;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.OS;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class CommandData extends Data {

    // command
    private CommandDataBasic basicData = null;
    private PresetType presetType = PresetType.presetUnknown;
    private OS os = OS.osUnknown;
    private Category category = Category.categoryUnknown;
    private CommandType commandType = CommandType.commandTypeUnknown;
    private CommandDataBasic userData = null;

    public CommandData(String id, CommandDataBasic basicData, PresetType presetType, OS os, Category category, CommandType cmdType, CommandDataBasic userData) {
        super(id);
        setBasicData(basicData);
        setPresetType(presetType);
        setOs(os);
        setCategory(category);
        setCommandType(cmdType);
        setUserData(userData);
    }

    public CommandData(String id, PresetType presetType, OS os, String name, ResourceType resType, boolean useWorkingDirectory, String workingDirectory, Category category, CommandType cmdType, String command) {
        this(id, new CommandDataBasic(name, resType, useWorkingDirectory, workingDirectory, command), presetType, os, category, cmdType, null);
    }

    public CommandData(String id, PresetType presetType, OS os, String name, ResourceType resType, Category category, CommandType cmdType, String command) {
        this(id, presetType, os, name, resType, false, null, category, cmdType, command);
    }

    public CommandData(CommandData commandData, String newId) {
        this(newId, commandData.getBasicData(), commandData.getPresetType(), commandData.getOs(), commandData.getCategory(), commandData.getCommandType(), commandData.getUserData());
    }

    public CommandData(CommandData commandData, boolean generateNewId) {
        this(commandData, generateNewId ? UUID.randomUUID().toString() : commandData.getId());
    }

    public CommandData() {
        basicData = new CommandDataBasic();
    }

    public CommandDataBasic getBasicData() {
        return basicData;
    }

    public CommandDataBasic getUserData() {
        return userData;
    }

    public String getName() {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            return userData.getName();
        } else {
            return basicData.getName();
        }
    }

    public OS getOs() {
        return os;
    }

    public PresetType getPresetType() {
        return presetType;
    }

    public ResourceType getResourceType() {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            return userData.getResourceType();
        } else {
            return basicData.getResourceType();
        }
    }

    public boolean isUseWorkingDirectory() {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            return userData.isUseWorkingDirectory();
        } else {
            return basicData.isUseWorkingDirectory();
        }
    }

    public String getWorkingDirectory() {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            return userData.getWorkingDirectory();
        } else {
            return basicData.getWorkingDirectory();
        }
    }

    public Category getCategory() {
        return category;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getCommand() {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            return userData.getCommand();
        } else {
            return basicData.getCommand();
        }
    }

    public Image getCategoryImage() {
        return new Image(null, Activator.getImageDescriptor(getCategory().getIcon()).getImageData());
    }

    public String getCommandAsComboName() {
        return getCategory().getName() + " - " + getName() + " (" + getPresetType().getName() + ")" /*+ getOs().getName() + " - "*/;
    }

    public boolean checkIfUserDataOverridesPreset(CommandDataBasic userData) {
        if (getPresetType() == PresetType.presetPlugin) {
            return !basicData.equals(userData);
        }
        return false;
    }

    public void setBasicData(CommandDataBasic basicData) {
        this.basicData = basicData;
    }

    public void setUserData(CommandDataBasic userData) {
        this.userData = userData;
    }

    public void addUserData(CommandDataBasic userData) {
        if (getPresetType() != PresetType.presetPluginAndUser) {
            setPresetType(PresetType.presetPluginAndUser);
        }
        setUserData(userData);
    }

    public void removeUserData() {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            setPresetType(PresetType.presetPlugin);
        }
        setUserData(null);
    }

	public void setName(String name) {
	    if (getPresetType() == PresetType.presetPluginAndUser) {
	        userData.setName(name);
	    } else {
	        basicData.setName(name);
	    }
	}

    public void setOs(OS os) {
        this.os = os;
    }

    public void setPresetType(PresetType presetType) {
        this.presetType = presetType;
    }

    public void setResourceType(ResourceType resType) {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            userData.setResourceType(resType);
        } else {
            basicData.setResourceType(resType);
        }
    }

    public void setUseWorkingDirectory(boolean useWorkingDirectory) {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            userData.setUseWorkingDirectory(useWorkingDirectory);
        } else {
            basicData.setUseWorkingDirectory(useWorkingDirectory);
        }
    }

    public void setWorkingDirectory(String workingDirectory) {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            userData.setWorkingDirectory(workingDirectory);
        } else {
            basicData.setWorkingDirectory(workingDirectory);
        }
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCommandType(CommandType cmdType) {
        this.commandType = cmdType;
    }

	public void setCommand(String command) {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            userData.setCommand(command);
        } else {
            basicData.setCommand(command);
        }
	}

	public boolean equals(Object object) {
    	if(!(object instanceof CommandData)) {
    		return false;
    	}
    	CommandData data = (CommandData)object;
    	if(data.getId().equals(this.getId())
    	   /*data.getPosition() == this.getPosition() &&
    	     data.getBasicData().equals(this.getBasicData()) &&
    	     data.getPresetType() == this.getPresetType() &&
    	     data.getOS() == this.getOS() &&
    	     data.getCategory() == this.getCategory() &&
    	     data.getCommandType() == this.getCommandType() &&*/
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
        presetType = PresetType.getFromEnum(tokenizer.nextToken());
        setOs(OS.getFromEnum(tokenizer.nextToken()));
        basicData.setName(tokenizer.nextToken());
        basicData.setResourceType(ResourceType.getFromEnum(tokenizer.nextToken()));
		// handling of working directory
		if (version.getId() >= Version.v2_0_003.getId()) {
		    basicData.setUseWorkingDirectory(Boolean.valueOf(tokenizer.nextToken()).booleanValue());
		    basicData.setWorkingDirectory(tokenizer.nextToken());
		    setCategory(Category.getFromEnum(tokenizer.nextToken()));
		    setCommandType(CommandType.getFromEnum(tokenizer.nextToken()));
		} else {
		    basicData.setUseWorkingDirectory(false);
		    basicData.setWorkingDirectory("${easyshell:container_loc}");
		    String commandTypeStr = tokenizer.nextToken();
		    setCategory(Category.getFromDeprecatedCommandTypeEnum(commandTypeStr));
		    setCommandType(CommandType.getFromDeprecatedCommandTypeEnum(commandTypeStr));
		}
		// go on compatible
		basicData.setCommand(tokenizer.nextToken());
		if (version.getId() >= Version.v2_0_005.getId()) {
	        // let read userData if there
	        if (getPresetType() == PresetType.presetPluginAndUser) {
	            setUserData(new CommandDataBasic());
	            userData.deserialize(version, null, tokenizer, delimiter);
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
        ret += getPresetType().toString() + delimiter;
        ret += getOs().toString() + delimiter;
        ret += basicData.getName() + delimiter;
        ret += basicData.getResourceType().toString() + delimiter;
        if (version.getId() >= Version.v2_0_003.getId()) {
            ret += Boolean.toString(basicData.isUseWorkingDirectory()) + delimiter;
            ret += basicData.getWorkingDirectory() + delimiter;
            ret += getCategory().toString() + delimiter;
        }
        ret += getCommandType().toString() + delimiter;
        ret += basicData.getCommand() + delimiter;
        if (version.getId() >= Version.v2_0_005.getId()) {
            if (getPresetType() == PresetType.presetPluginAndUser) {
                ret += userData.serialize(version, delimiter);
            }
        }
        return ret;
    }

    public String serialize(String delimiter) {
        return serialize(Version.actual, delimiter);
    }

}
