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

import java.util.UUID;

public class Data implements IData {

	// internal
    private int position = 0;
    private String id = null;

    public Data(String id) {
        if (id == null || id.isEmpty()) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
    }

    public Data(String id, int position) {
        this(id);
        setPosition(position);
    }

    public Data(IData data, boolean generateNewId) {
        this(generateNewId ? null : data.getId());
    }

    public Data(IData data) {
        this(data.getId());
    }

    public Data() {
        this(null, true);
    }

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IData#getPosition()
     */
    @Override
    public int getPosition() {
		return position;
	}

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IData#getId()
     */
    @Override
    public String getId() {
        return id;
    }

	/* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IData#equals(java.lang.Object)
     */
	@Override
    public boolean equals(Object object) {
    	if(!(object instanceof Data)) {
    		return false;
    	}
    	IData data = (IData)object;
    	if( data.getId().equals(this.getId())
    	    /*data.getPosition() == this.getPosition()*/
    	  )
    	{
    		return true;
    	}
    	return false;
    }

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IData#setPosition(int)
     */
    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    /* (non-Javadoc)
     * @see de.anbos.eclipse.easyshell.plugin.preferences.IData#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

}
