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

public class Converters {
    static Map<String, IConverter> map = new HashMap<String, IConverter>();
    public static Map<String, IConverter> getMap() {
        return map;
    }
    static {
        for(int i=0;i<Converter.values().length;i++) {
            map.put(Converter.values()[i].getName(), Converter.values()[i].getConverterImpl());
        }
    }
}
