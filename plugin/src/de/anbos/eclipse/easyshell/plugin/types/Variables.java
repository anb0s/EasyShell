/*******************************************************************************
 * Copyright (c) 2014 - 2017 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.types;

import java.util.HashMap;
import java.util.Map;

public class Variables {
    static Map<String, IVariableResolver> map = new HashMap<String, IVariableResolver>();
    public static Map<String, IVariableResolver> getMap() {
        return map;
    }
    static {
        for(int i=0;i<Variable.values().length;i++) {
            map.put(Variable.values()[i].getName(), Variable.values()[i].getResolver());
        }
    }
}
