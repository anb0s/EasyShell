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
