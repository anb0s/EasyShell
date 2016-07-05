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

public class PresetData {

	// Status
    private int position = 0;
    private boolean enabled = true;

    // Preset
    private String id = UUID.randomUUID().toString();
    private String name = "Name";
    private PresetType type = PresetType.presetTypeOpen;
    private String command = "command";
    private OS os = OS.osWindows;

    public PresetData(OS os, String name, PresetType type, String command) {
        this.os = os;
        this.name = name;
        this.type = type;
        this.command = command;
    }

    public PresetData() {
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

    public PresetType getType() {
        return type;
    }

    public String getValue() {
        return command;
    }

    public OS getOs() {
        return os;
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

    public void setType(PresetType type) {
        this.type = type;
    }

	public void setValue(String value) {
		this.command = value;
	}

    public void setOs(OS os) {
        this.os = os;
    }

	public boolean equals(Object object) {
    	if(!(object instanceof PresetData)) {
    		return false;
    	}
    	PresetData data = (PresetData)object;
    	if(data.getPosition() == this.getPosition() &
    	   data.getId().equals(this.getId()) &
    	   data.getName().equals(this.getName()) &
    	   data.getType() == this.getType() &
    	   data.getValue().equals(this.getValue()) &
    	   data.getOs().equals(this.getOs())
    	  )
    	{
    		return true;
    	}
    	return false;
    }

	public boolean fillTokens(String value, String delimiter) {
		if(value == null || value.length() <= 0) {
			return false;
		}
		StringTokenizer tokenizer = new StringTokenizer(value,delimiter);
        String positionStr = tokenizer.nextToken();
        String enabledStr =  tokenizer.nextToken();
        String idStr = tokenizer.nextToken();
        String nameStr = tokenizer.nextToken();
        String typeStr = tokenizer.nextToken();
		String valueStr = tokenizer.nextToken();
		String osStr = tokenizer.nextToken();
		// set members
		setPosition(Integer.parseInt(positionStr));
		setEnabled(Boolean.valueOf(enabledStr).booleanValue());
		setId(idStr);
		setName(nameStr);
		setType(PresetType.getFromName(typeStr));
		setValue(valueStr);
		setOs(OS.getFromName(osStr));
		return true;
	}

    public String getMenuName() {
        return getType().getName() + ": " + getName();
    }

    public String getMenuIcon() {
        return getType().getIcon();
    }

    public String getTypeAction() {
        return getType().getAction();
    }

}
