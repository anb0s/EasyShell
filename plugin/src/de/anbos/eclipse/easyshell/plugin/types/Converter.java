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

import java.util.ArrayList;
import java.util.List;

public enum Converter {
    converterUnknown(-1, false, OS.osUnknown, "unknown", "unknown", new IConverter() {
        @Override
        public String convert(String input) {
            return input;
        }
    }),
    converterUnix(0, OS.osUnknown, "unix", "converts all separators to Unix (line '\\n', file '/', path ':')", new IConverter() {
        @Override
        public String convert(String input) {
            String result = input;
            for (Converter converter : getFromOS(OS.osUnix)) {
                result = converter.getConverterImpl().convert(result);
            }
            return result;
        }
    }),
    converterWindows(1, OS.osUnknown, "windows", "converts all separators to Windows (line '\\r\\n', file '\\', path ';')", new IConverter() {
        @Override
        public String convert(String input) {
            String result = input;
            for (Converter converter : getFromOS(OS.osWindows)) {
                result = converter.getConverterImpl().convert(result);
            }
            return result;
        }
    }),
    converterLineSeparatorLF(2, OS.osUnix, "lf", "converts line separator to Unix '\\n'", new IConverter() {
        @Override
        public String convert(String input) {
            return input.replace("\r\n", "\n");
        }
    }),
    converterLineSeparatorCRLF(3, OS.osWindows, "crlf", "converts line separator to Windows '\\r\\n'", new IConverter() {
        @Override
        public String convert(String input) {
            return input.replace("\r\n", "\n").replace("\n", "\r\n");
        }
    }),
    converterFileSeparatorSlash(4, OS.osUnix, "slash", "converts file separator to Unix '/'", new IConverter() {
        @Override
        public String convert(String input) {
            return input.replace("\\", "/");
        }
    }),
    converterFileSeparatorBackslash(5, OS.osWindows, "backslash", "converts file separator to Windows '\\'", new IConverter() {
        @Override
        public String convert(String input) {
            return input.replace("/", "\\");
        }
    });
    // attributes
    private final int id;
    private final boolean visible;
    private final OS os;
    private final String name;
    private final String description;
    private IConverter converterImpl = null;
    // construct
    Converter(int id, boolean visible, OS os, String name, String description, IConverter converterImpl) {
        this.id = id;
        this.visible = visible;
        this.os = os;
        this.name = name;
        this.description = description;
        this.converterImpl = converterImpl;
    }
    Converter(int id, OS os, String name, String description, IConverter converterImpl) {
        this(id, true, os, name, description, converterImpl);
    }
    public int getId() {
        return id;
    }
    public boolean isVisible() {
        return visible;
    }
    public OS getOS() {
        return os;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public IConverter getConverterImpl() {
        return converterImpl;
    }
    public static Converter getFromId(int id) {
        Converter ret = converterUnknown;
        for(int i = 0; i < Converter.values().length; i++) {
            if (Converter.values()[i].getId() == id) {
                ret = Converter.values()[i];
                break;
            }
        }
        return ret;
    }
    public static List<Converter> getFromOS(OS os) {
        List<Converter> ret = new ArrayList<>();
        for(int i = 0; i < Converter.values().length; i++) {
            if (Converter.values()[i].getOS() == os) {
                ret.add(Converter.values()[i]);
            }
        }
        return ret;
    }
    public static Converter getFromName(String name) {
        Converter ret = converterUnknown;
        for(int i = 0; i < Converter.values().length; i++) {
            if (Converter.values()[i].getName().equals(name)) {
                ret = Converter.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Converter getFromEnum(String name) {
        return Converter.valueOf(name);
    }
    public static int getFirstIndex() {
        return 0;
    }
};
