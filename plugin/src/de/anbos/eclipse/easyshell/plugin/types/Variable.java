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

package de.anbos.eclipse.easyshell.plugin.types;

import java.util.ArrayList;
import java.util.List;

public enum Variable {
    varUnknown(        -1, "Unknown",          "Unknown"),
    varResourceLoc(     0, "resource_loc",     "absolute path of file or directory"),
    varResourceName(    1, "resource_name",    "name of file or directory"),
    varResourcePath(    2, "resource_path",    "relative path to workspace of file or directory"),
    varContainerLoc(    3, "container_loc",    "absolute path of file directory or directory itself"),
    varContainerName(   4, "container_name",   "name of file directory or directory itself"),
    varContainerPath(   5, "container_path",   "relative path to workspace of file directory or directory itself"),
    varProjectLoc(      6, "project_loc",      "absolute path of project"),
    varProjectName(     7, "project_name",     "name of project"),
    varProjectPath(     8, "project_path",     "relative path to workspace of project"),
    varLineSeparator(   9, "line_separator",   "line separator"),
    varWindowsDrive(   10, "windows_drive",    "drive letter of file or directory on Windows");
    // attributes
    private final int id;
    private final String name;
    private final String description;
    // construct
    Variable(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getFullVariableName() {
        return "${easyshell:" + name + "}";

    }
    public String getEclipseVariableName() {
        return "${" + name + "}";
    }
    public String getDescription() {
        return description;
    }
    public static Variable getFromId(int id) {
        Variable ret = varUnknown;
        for(int i = 0; i < Variable.values().length; i++) {
            if (Variable.values()[i].getId() == id) {
                ret = Variable.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Variable getFromName(String name) {
        Variable ret = varUnknown;
        for(int i = 0; i < Variable.values().length; i++) {
            if (Variable.values()[i].getName().equals(name)) {
                ret = Variable.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Variable getFromEnum(String name) {
        return Variable.valueOf(name);
    }
    public static List<String> getNamesAsList() {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < Variable.values().length; i++) {
            list.add(Variable.values()[i].getName());
        }
        return list;
    }
}
