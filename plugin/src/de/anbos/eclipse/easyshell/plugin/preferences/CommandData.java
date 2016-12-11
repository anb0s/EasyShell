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
import de.anbos.eclipse.easyshell.plugin.types.CommandTokenizer;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class CommandData extends Data {

    // command
    private CommandDataBasic basicData = null;
    private PresetType presetType = PresetType.presetUnknown;
    private OS os = OS.osUnknown;
    private Category category = Category.categoryUnknown;
    private CommandType commandType = CommandType.commandTypeUnknown;
    private CommandDataBasic modifyData = null;

    public CommandData(String id, CommandDataBasic basicData, PresetType presetType, OS os, Category category, CommandType cmdType, CommandDataBasic modifyData) {
        super(id);
        setBasicData(basicData);
        setPresetType(presetType);
        setOs(os);
        setCategory(category);
        setCommandType(cmdType);
        setModifyData(modifyData);
    }

    public CommandData(String id, PresetType presetType, OS os, String name, ResourceType resType, boolean useWorkingDirectory, String workingDirectory, Category category, CommandType cmdType, CommandTokenizer tokenizer, String command) {
        this(id, new CommandDataBasic(null, name, resType, useWorkingDirectory, workingDirectory, tokenizer, command), presetType, os, category, cmdType, null);
    }

    public CommandData(String id, PresetType presetType, OS os, String name, ResourceType resType, Category category, CommandType cmdType, String command) {
        this(id, presetType, os, name, resType, false, null, category, cmdType, CommandTokenizer.commandTokenizerSpaces, command);
    }

    public CommandData(CommandData commandData, String newId) {
        this(newId, commandData.getBasicData(), commandData.getPresetType(), commandData.getOs(), commandData.getCategory(), commandData.getCommandType(), commandData.getModifyData());
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

    public CommandDataBasic getModifyData() {
        return modifyData;
    }

    public String getName() {
        if (getPresetType() == PresetType.presetPluginModify) {
            return modifyData.getName();
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
        if (getPresetType() == PresetType.presetPluginModify) {
            return modifyData.getResourceType();
        } else {
            return basicData.getResourceType();
        }
    }

    public boolean isUseWorkingDirectory() {
        if (getPresetType() == PresetType.presetPluginModify) {
            return modifyData.isUseWorkingDirectory();
        } else {
            return basicData.isUseWorkingDirectory();
        }
    }

    public String getWorkingDirectory() {
        if (getPresetType() == PresetType.presetPluginModify) {
            return modifyData.getWorkingDirectory();
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

    public CommandTokenizer getCommandTokenizer() {
        if (getPresetType() == PresetType.presetPluginModify) {
            return modifyData.getCommandTokenizer();
        } else {
            return basicData.getCommandTokenizer();
        }
    }

    public String getCommand() {
        if (getPresetType() == PresetType.presetPluginModify) {
            return modifyData.getCommand();
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

    public boolean checkIfUserDataOverridesPreset(CommandDataBasic modifyData) {
        return !basicData.equals(modifyData);
    }

    public void setBasicData(CommandDataBasic basicData) {
        this.basicData = basicData;
    }

    private void setModifyData(CommandDataBasic modifyData) {
        this.modifyData = modifyData;
    }

    private void addModifyData(CommandDataBasic modifyData) {
        if (getPresetType() != PresetType.presetPluginModify) {
            setPresetType(PresetType.presetPluginModify);
        }
        modifyData.setId(getId());
        setModifyData(modifyData);
    }

    public void removeModifyData() {
        if (getPresetType() != PresetType.presetPlugin) {
            setPresetType(PresetType.presetPlugin);
        }
        setModifyData(null);
    }

    public void addOrRemoveModifyData(CommandDataBasic modifyData) {
        if (checkIfUserDataOverridesPreset(modifyData)) {
        	addModifyData(modifyData);
        } else {
            removeModifyData();
        }    	
    }

/*
	public void setName(String name) {
	    if (getPresetType() == PresetType.presetPluginModify) {
	        modifyData.setName(name);
	    } else {
	        basicData.setName(name);
	    }
	}
*/
    public void setOs(OS os) {
        this.os = os;
    }

    public void setPresetType(PresetType presetType) {
        this.presetType = presetType;
    }
/*
    public void setResourceType(ResourceType resType) {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            modifyData.setResourceType(resType);
        } else {
            basicData.setResourceType(resType);
        }
    }

    public void setUseWorkingDirectory(boolean useWorkingDirectory) {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            modifyData.setUseWorkingDirectory(useWorkingDirectory);
        } else {
            basicData.setUseWorkingDirectory(useWorkingDirectory);
        }
    }

    public void setWorkingDirectory(String workingDirectory) {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            modifyData.setWorkingDirectory(workingDirectory);
        } else {
            basicData.setWorkingDirectory(workingDirectory);
        }
    }
*/
    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCommandType(CommandType cmdType) {
        this.commandType = cmdType;
    }
/*
    public void setCommandTokenizer(CommandTokenizer commandTokenizer) {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            modifyData.setCommandTokenizer(commandTokenizer);
        } else {
            basicData.setCommandTokenizer(commandTokenizer);
        }
    }

	public void setCommand(String command) {
        if (getPresetType() == PresetType.presetPluginAndUser) {
            modifyData.setCommand(command);
        } else {
            basicData.setCommand(command);
        }
	}
*/
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
		String presetTypeStr = tokenizer.nextToken();
        if (version.getId() < Version.v2_1_001.getId() && presetTypeStr.equals("presetPluginAndUser")) {
        	presetTypeStr = "presetPluginModify";
        }
        presetType = PresetType.getFromEnum(presetTypeStr);
        setOs(OS.getFromEnum(tokenizer.nextToken()));
        basicData.setName(tokenizer.nextToken());
        // handling of resource Type
        String resourceTypeStr = tokenizer.nextToken();
        if (version.getId() < Version.v2_0_006.getId() && resourceTypeStr.equals("resourceTypeFolder")) {
            resourceTypeStr = "resourceTypeDirectory";
        }
        basicData.setResourceType(ResourceType.getFromEnum(resourceTypeStr));
		// handling of working directory
		if (version.getId() >= Version.v2_0_003.getId()) {
		    basicData.setUseWorkingDirectory(Boolean.valueOf(tokenizer.nextToken()).booleanValue());
		    basicData.setWorkingDirectory(tokenizer.nextToken());
		    String categoryStr = tokenizer.nextToken();
		    if (version.getId() < Version.v2_0_005.getId() && categoryStr.equals("categoryOther")) {
		        categoryStr = "categoryUser";
		    }
		    setCategory(Category.getFromEnum(categoryStr));
		    setCommandType(CommandType.getFromEnum(tokenizer.nextToken()));
		} else {
		    basicData.setUseWorkingDirectory(false);
		    basicData.setWorkingDirectory("${easyshell:container_loc}");
		    String commandTypeStr = tokenizer.nextToken();
		    setCategory(Category.getFromDeprecatedCommandTypeEnum(commandTypeStr));
		    setCommandType(CommandType.getFromDeprecatedCommandTypeEnum(commandTypeStr));
		}
		// go on compatible
	    String commandTokenizer = CommandTokenizer.commandTokenizerSpaces.toString();
        if (version.getId() >= Version.v2_1_001.getId()) {
        	String oldCommandTokenizer = tokenizer.nextToken();
        	if (version.getId() >= Version.v2_1_003.getId()) {
        		commandTokenizer = oldCommandTokenizer;
        	}
        }
        basicData.setCommandTokenizer(CommandTokenizer.getFromEnum(commandTokenizer));
		basicData.setCommand(tokenizer.nextToken());
		if (version.getId() >= Version.v2_0_005.getId()) {
			if (version.getId() < Version.v2_1_001.getId()) {
		        // let read modifyData if there
		        if (getPresetType() == PresetType.presetPluginModify) {
		            setModifyData(new CommandDataBasic(getId()));
		            modifyData.deserialize(version, null, tokenizer, delimiter);
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
        PresetType presetTypeTemp = getPresetType();
        if (version.getId() >= Version.v2_1_001.getId()) {
        	if (presetTypeTemp == PresetType.presetPluginModify) {
        		presetTypeTemp = PresetType.presetPlugin;
        	}
        }
        ret += presetTypeTemp.toString() + delimiter;
        ret += getOs().toString() + delimiter;
        ret += basicData.getName() + delimiter;
        ret += basicData.getResourceType().toString() + delimiter;
        if (version.getId() >= Version.v2_0_003.getId()) {
            ret += Boolean.toString(basicData.isUseWorkingDirectory()) + delimiter;
            ret += basicData.getWorkingDirectory() + delimiter;
            ret += getCategory().toString() + delimiter;
        }
        ret += getCommandType().toString() + delimiter;
        if (version.getId() >= Version.v2_1_001.getId()) {
        	ret += basicData.getCommandTokenizer().toString() + delimiter;
        }
        ret += basicData.getCommand() + delimiter;
        if (version.getId() >= Version.v2_0_005.getId()) {
        	if (version.getId() < Version.v2_1_001.getId()) {
	            if (getPresetType() == PresetType.presetPluginModify) {
	                ret += modifyData.serialize(version, delimiter);
	            }
        	}
        }
        return ret;
    }

    public String serialize(String delimiter) {
        return serialize(Version.actual, delimiter);
    }

}
