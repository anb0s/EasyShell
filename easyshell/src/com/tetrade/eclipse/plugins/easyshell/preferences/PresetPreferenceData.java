package com.tetrade.eclipse.plugins.easyshell.preferences;

import java.util.StringTokenizer;

public class PresetPreferenceData {

	// Status
    private int position;
    private boolean enabled;

    // Preset
    private String id;
    private String name;
    private String value;
    private String os;

    public PresetPreferenceData() {
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

    public String getValue() {
        return value;
    }

    public String getOs() {
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

	public void setValue(String value) {
		this.value = value;
	}

    public void setOs(String os) {
        this.os = os;
    }

	public boolean equals(Object object) {
    	if(!(object instanceof PresetPreferenceData)) {
    		return false;
    	}
    	PresetPreferenceData data = (PresetPreferenceData)object;
    	if(data.getPosition() == this.getPosition() &
    	   data.getId().equals(this.getId()) &
    	   data.getName().equals(this.getName()) &
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
		String valueStr = tokenizer.nextToken();
		String osStr = tokenizer.nextToken();
		// set members
		setPosition(Integer.parseInt(positionStr));
		setEnabled(Boolean.valueOf(enabledStr).booleanValue());
		setId(idStr);
		setName(nameStr);
		setValue(valueStr);
		setOs(osStr);
		return true;
	}
}
