/*******************************************************************************
 * Copyright (c) 2014 - 2018 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

public interface IStore {

    IPreferenceStore getStore();

    void load();

    void loadDefaults();

    boolean verify();

    void save();

    boolean isMigrated();

    void setMigrated(boolean migrated);

}