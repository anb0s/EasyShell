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

import de.anbos.eclipse.easyshell.plugin.types.Debug;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class GeneralData {

	private Debug debug = Debug.debugNo;
	
    public GeneralData(Debug debug) {
    	this.setDebug(debug);
    }

    public GeneralData() {
    }

	public boolean equals(Object object) {
    	if(!(object instanceof GeneralData)) {
    		return false;
    	}
    	GeneralData data = (GeneralData)object;
    	if(data.getDebug() == this.getDebug()
    	   /*data.getPosition() == this.getPosition() &&*/
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
        setDebug(Debug.getFromEnum(tokenizer.nextToken()));
		return true;
	}

    public boolean deserialize(String value, StringTokenizer tokenizer, String delimiter) {
        return deserialize(Version.actual, value, tokenizer, delimiter);
    }

    public String serialize(Version version, String delimiter) {
    	String ret = "";
        ret += getDebug().toString() + delimiter;
        return ret;
    }

    public String serialize(String delimiter) {
        return serialize(Version.actual, delimiter);
    }

	public Debug getDebug() {
		return debug;
	}

	public void setDebug(Debug debug) {
		this.debug = debug;
	}

}
