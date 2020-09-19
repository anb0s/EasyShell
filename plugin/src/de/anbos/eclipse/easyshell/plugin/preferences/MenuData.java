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
import java.util.UUID;

import de.anbos.eclipse.easyshell.plugin.Constants;
import de.anbos.eclipse.easyshell.plugin.exceptions.UnknownCommandID;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.MenuNameType;
import de.anbos.eclipse.easyshell.plugin.types.Variable;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class MenuData extends Data {

    // menu
    private boolean enabled = true;
    private MenuNameType nameType = MenuNameType.menuNameTypeUnknown;
    private String namePattern = "";
    private String imageId = Constants.IMAGE_NONE;
    // copy of or reference to command
    private String commandId = null;

    public MenuData(String id, boolean enabled, MenuNameType nameType, String namePattern, String imageId, String commandId) {
        super(id);
        setEnabled(enabled);
        setNameType(nameType);
        setNamePattern(namePattern);
        setImageId(imageId);
        setCommandId(commandId);
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
        this.imageId = data.getImageIdOwn();
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
        for (Variable variable : Variable.getInternalVariables()) {
            try {
                expanded = expanded.replace(variable.getFullVariableName(), variable.getResolver().resolve(getCommandData(), null));
            } catch (UnknownCommandID e) {
                e.logInternalError();
                break;
            }
        }
        return expanded;
    }

    public String toString() {
        return getNameExpanded();
    }

    public String getImageId() {
        if (imageId.equals(Constants.IMAGE_NONE)) {
            return getCommandImageId();
        } else {
            return imageId;
        }
    }

    public String getCommandImageId() {
        try {
            return getCommandData().getImageId();
        } catch (UnknownCommandID e) {
            return Constants.IMAGE_NONE;
        }
    }

    public String getImageIdOwn() {
        return imageId;
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
        String imageIdStr = Constants.IMAGE_NONE;
        // -------------------------------------------------
        // read new imageId and commandId
        if (version.getId() >= Version.v2_0_003.getId()) {
            if (version.getId() >= Version.v2_1_005.getId()) {
                imageIdStr = tokenizer.nextToken();
            }
            // read the new one
            setCommandId(tokenizer.nextToken());
        } else {
            // read previous command data members
            CommandData oldData = new CommandData();
            oldData.deserialize(version, null, tokenizer, delimiter);
            setCommandId(oldData.getId());
            // menu name type handling
            // set name type and read the old name as pattern and convert to new
            if (version.getId() < Version.v2_0_002.getId()) {
                // check if readed name is the same, like expanded from patterns
                for (MenuNameType type : MenuNameType.getAsList()) {
                    setNamePattern(type.getPattern()); // set temporary
                    if (getNameExpanded().equals(namePatternReaded)) {
                        nameType = type;
                        break;
                    }
                }
            }
        }
        setNameType(nameType);
        if (nameType == MenuNameType.menuNameTypeUser) {
            setNamePattern(namePatternReaded);
        } else if (version.getId() < Version.v2_0_004.getId()) {
            // convert to new names types
            if (nameType == MenuNameType.menuNameTypeGeneric1) {
                setNameTypeFromCategory();
            }
        }
        setImageId(imageIdStr);
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
            if (version.getId() >= Version.v2_1_005.getId()) {
                ret += getImageIdOwn() + delimiter;
            }
            ret += getCommandId() + delimiter;
        } else {
            try {
                ret += getCommandData().serialize(version, delimiter);
            } catch (UnknownCommandID e) {
                e.logInternalError();
            }
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
        if (namePattern != null) {
            this.namePattern = namePattern;
        }
    }

    public void setImageId(String imageId) {
        if (imageId == null || imageId.equals(Constants.IMAGE_NONE) || imageId.equals(getCommandImageId())) {
            this.imageId = Constants.IMAGE_NONE;
        } else {
            this.imageId = imageId;
        }
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public CommandData getCommandData() throws UnknownCommandID {
        String commandIdStr = getCommandId();
        CommandData data = CommandDataStore.instance().getById(commandIdStr);
        if (data != null) {
            return data;
        } else {
            throw new UnknownCommandID(commandIdStr, true);
        }
    }

    public String getCommand() {
        try {
            return getCommandData().getCommand();
        } catch (UnknownCommandID e) {
            e.logInternalError();
            return "Unknown ID: " + e.getID();
        }
    }

    public void setNameTypeFromCategory() {
        try {
            setNameTypeFromCategory(getCommandData().getCategory());
        } catch (UnknownCommandID e) {
            e.logInternalError();
        }
    }

    public void setNameTypeFromCategory(Category category) {
        switch (category) {
        case categoryDefault:
            setNameType(MenuNameType.menuNameTypeDefaultApplication);
            break;
        case categoryOpen:
            setNameType(MenuNameType.menuNameTypeOpenHere);
            break;
        case categoryRun:
            setNameType(MenuNameType.menuNameTypeGeneric2);
            break;
        case categoryExplore:
            setNameType(MenuNameType.menuNameTypeShowIn);
            break;
        case categoryClipboard:
            setNameType(MenuNameType.menuNameTypeCopyToClipboard);
            break;
        case categoryUser:
            setNameType(MenuNameType.menuNameTypeGeneric1);
            break;
        case categoryUnknown:
            setNameType(MenuNameType.menuNameTypeGeneric1);
            break;
        default:
            setNameType(MenuNameType.menuNameTypeGeneric1);
            break;
        }
    }

    @Override
    public boolean verify() {
        try {
            return super.verify() && commandId != null && getCommandData() != null;
        } catch (UnknownCommandID e) {
            return false;
        }
    }

}
