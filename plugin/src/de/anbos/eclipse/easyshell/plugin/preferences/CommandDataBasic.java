/**
 * Copyright (c) 2014-2020 Andre Bossert <anb0s@anbos.de>.
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package de.anbos.eclipse.easyshell.plugin.preferences;

import java.util.StringTokenizer;

import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.CommandTokenizer;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class CommandDataBasic {

    // command
    private String id = null;
    private String name = "";
    private ResourceType resourceType = ResourceType.resourceTypeUnknown;
    private boolean useWorkingDirectory = false;
    private String workingDirectory = "";
    private CommandTokenizer commandTokenizer = CommandTokenizer.commandTokenizerSpaces;
    private String command = "";

    public CommandDataBasic(String id, String name, ResourceType resType, boolean useWorkingDirectory, String workingDirectory, CommandTokenizer tokenizer, String command) {
        setId(id);
        setName(name);
        setResourceType(resType);
        setUseWorkingDirectory(useWorkingDirectory);
        setWorkingDirectory(workingDirectory);
        setCommandTokenizer(tokenizer);
        setCommand(command);
    }

    public CommandDataBasic(CommandDataBasic commandData) {
        this(commandData.getId(), commandData.getName(), commandData.getResourceType(), commandData.isUseWorkingDirectory(), commandData.getWorkingDirectory(), commandData.getCommandTokenizer(), commandData.getCommand());
    }

    public CommandDataBasic(String id) {
        setId(id);
    }

    public CommandDataBasic() {
    }

    public String getId() {
        return id;
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

    public CommandTokenizer getCommandTokenizer() {
        return commandTokenizer;
    }

    public String getCommand() {
        return command;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setCommandTokenizer(CommandTokenizer commandTokenizer) {
        this.commandTokenizer = commandTokenizer;
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
           data.getCommandTokenizer() == this.getCommandTokenizer() &&
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
        if (version.getId() >= Version.v2_1_001.getId()) {
            if (getId() == null) {
                setId(tokenizer.nextToken());
            }
        }
        setName(tokenizer.nextToken());
        // handling of resource Type
        String resourceTypeStr = tokenizer.nextToken();
        if (version.getId() < Version.v2_0_006.getId() && resourceTypeStr.equals("resourceTypeFolder")) {
            resourceTypeStr = "resourceTypeDirectory";
        }
        setResourceType(ResourceType.getFromEnum(resourceTypeStr));
        // handling of working directory
        setUseWorkingDirectory(Boolean.valueOf(tokenizer.nextToken()).booleanValue());
        setWorkingDirectory(tokenizer.nextToken());
        // command
        String commandTokenizer = CommandTokenizer.commandTokenizerSpaces.toString();
        if (version.getId() >= Version.v2_1_001.getId()) {
            String oldCommandTokenizer = tokenizer.nextToken();
            if (version.getId() >= Version.v2_1_003.getId()) {
                commandTokenizer = oldCommandTokenizer;
            }
        }
        setCommandTokenizer(CommandTokenizer.getFromEnum(commandTokenizer));
        setCommand(tokenizer.nextToken());
        return true;
    }

    public boolean deserialize(String value, StringTokenizer tokenizer, String delimiter) {
        return deserialize(Version.actual, value, tokenizer, delimiter);
    }

    public String serialize(Version version, String delimiter) {
        String ret = getId();
        if (ret != null) {
            ret += delimiter;
        } else {
            ret = "";
        }
        ret += getName() + delimiter;
        ret += getResourceType().toString() + delimiter;
        ret += Boolean.toString(isUseWorkingDirectory()) + delimiter;
        ret += getWorkingDirectory() + delimiter;
        if (version.getId() >= Version.v2_1_001.getId()) {
            ret += getCommandTokenizer().toString() + delimiter;
        }
        ret += getCommand() + delimiter;
        return ret;
    }

    public String serialize(String delimiter) {
        return serialize(Version.actual, delimiter);
    }

    public boolean verify() {
        return id != null;
    }

}
