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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;

import de.anbos.eclipse.easyshell.plugin.Resource;

public enum Variable {
    // ${easyshell:resource_loc} == {2}
    varUnknown(        -1, false, "unknown", "unknown", new IVariableResolver() {
        public String resolve(Resource resource) {
            return "unknown";
        };
    }),
    varResourceLoc(     0, "resource_loc",     "absolute path of file or directory", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getResourceLocation();
        };
    }),
    // ${easyshell:resource_name} == {3}
    varResourceName(    1, "resource_name",    "name of file or directory", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getResourceName();
        };
    }),
    // ${easyshell:resource_path}
    varResourcePath(    2, "resource_path",    "relative path to workspace of file or directory", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getResourcePath();
        };
    }),
    // ${easyshell:container_loc} == {1}
    varContainerLoc(    3, "container_loc",    "absolute path of file directory or directory itself", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getContainerLocation();
        };
    }),
    // ${easyshell:container_name}
    varContainerName(   4, "container_name",   "name of file directory or directory itself", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getContainerName();
        };
    }),
    // ${easyshell:container_path}
    varContainerPath(   5, "container_path",   "relative path to workspace of file directory or directory itself", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getContainerPath();
        };
    }),
    // ${easyshell:project_loc_loc}
    varProjectLoc(      6, "project_loc",      "absolute path of project", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getProjectLocation();
        };
    }),
    // ${easyshell:project_name} == {4}
    varProjectName(     7, "project_name",     "name of project", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getProjectName();
        };
    }),
    // ${easyshell:project_path}
    varProjectPath(     8, "project_path",     "relative path to workspace of project", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getProjectPath();
        };
    }),
    // ${easyshell:windows_drive} == {0}
    varWindowsDrive(    9, "windows_drive",    "drive letter of file or directory on Windows", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getWindowsDrive();
        };
    }),
    // ${easyshell:qualified_name}
    varQualifiedName(  10, "qualified_name",   "full qualified (class) name", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getFullQualifiedName();
        };
    }),
    // ${easyshell:line_separator} == {5}
    varLineSeparator(  11, "line_separator",   "line separator, e.g. '\\n' (Unix) or '\\r\\n' (Windows)", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getLineSeparator(OS.osUnknown);
        };
    }),
    varLineSeparatorUnix(  12, false, "line_separator_unix",   "line separator '\\n' (Unix)", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getLineSeparator(OS.osUnix);
        };
    }),
    varLineSeparatorWindows(  13, false, "line_separator_windows",   "line separator '\\r\\n' (Windows)", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getLineSeparator(OS.osWindows);
        };
    }),
    varPathSeparator(  14, "path_separator",   "path separator, e.g. ':' (Unix) or ';' (Windows)", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getPathSeparator(OS.osUnknown);
        };
    }),
    varPathSeparatorUnix(  15, false, "path_separator_unix",   "path separator ':' (Unix)", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getPathSeparator(OS.osUnix);
        };
    }),
    varPathSeparatorWindows(  16, false, "path_separator_windows",   "path separator, e.g. ':' (Unix) or ';' (Windows)", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getPathSeparator(OS.osWindows);
        };
    }),
    varFileSeparator(  17, "file_separator",   "file separator, e.g. '/' (Unix) or '\\' (Windows)", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getFileSeparator(OS.osUnknown);
        };
    }),
    varFileSeparatorUnix(  18, false, "file_separator_unix",   "file separator '/' (Unix)", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getFileSeparator(OS.osUnix);
        };
    }),
    varFileSeparatorWindows(  19, false, "file_separator_windows",   "file separator '\\' (Windows)", new IVariableResolver() {
        public String resolve(Resource resource) {
            return resource.getFileSeparator(OS.osWindows);
        };
    });
    // attributes
    private final int id;
    private final boolean visible;
    private final boolean eclipse;
    private String name;
    private String description;
    private final IVariableResolver resolver;
    static private final String varBegin = "${";
    static private final String varEnd   = "}";
    static private final String varParamDelimiter = ":";
    static private final String varEasyShell = "easyshell";
    // construct
    Variable(int id, boolean visible, boolean eclipse, String name, String description, IVariableResolver resolver) {
        this.id = id;
        this.visible = visible;
        this.eclipse=eclipse;
        this.name = name;
        this.description = description;
        this.resolver = resolver;
    }
    Variable(int id, boolean visible, String name, String description, IVariableResolver resolver) {
        this(id, visible, false, name, description, resolver);
    }
    Variable(int id, String name, String description, IVariableResolver resolver) {
        this(id, true, name, description, resolver);
    }
    public int getId() {
        return id;
    }
    public boolean isVisible() {
        return visible;
    }
    public boolean isEclipse() {
        return eclipse;
    }
    public String getName() {
        return name;
    }
    public String getFullVariableName() {
        if (eclipse) {
            return getEclipseVariableName();
        } else {
            return varBegin + varEasyShell + varParamDelimiter + name + varEnd;
        }
    }
    public String getEclipseVariableName() {
        return varBegin + name + varEnd;
    }
    public String getDescription() {
        return description;
    }
    public IVariableResolver getResolver() {
        return resolver;
    }
    public Variable setName(String name) {
        this.name = name;
        return this;
    }
    public Variable setDescription(String description) {
        this.description = description;
        return this;
    }
    public static Variable getFromId(int id) {
        Variable ret = null;
        for(int i = 0; i < Variable.values().length; i++) {
            if (Variable.values()[i].getId() == id) {
                ret = Variable.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Variable getFromName(String name) {
        Variable ret = null;
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
    public static List<Variable> getVariables(boolean visibleOnly) {
        List<Variable> list = new ArrayList<>();
        for(int i = 0; i < Variable.values().length; i++) {
            if (!visibleOnly || (visibleOnly && Variable.values()[i].isVisible())) {
                list.add(Variable.values()[i]);
            }
        }
        return list;
    }
    public static List<Variable> getVisibleVariables() {
        return getVariables(true);
    }
    public static Map<String, String> getVariableInfoMap(boolean visibleOnly) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for(Variable variable : getVariables(visibleOnly)) {
            map.put(variable.getFullVariableName(), variable.getDescription());
        }
        return map;
    }
    public static Map<String, String> getVariableInfoMap() {
        return getVariableInfoMap(true);
    }
    public static Map<String, String> getEclipseVariableInfoMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
        IValueVariable[] valueVariables = variableManager.getValueVariables();
        for (IValueVariable variable : valueVariables) {
            map.put(varBegin + variable.getName() + varEnd, variable.getDescription());
        }
        IDynamicVariable[] dynamicVariables = variableManager.getDynamicVariables();
        for (IDynamicVariable variable : dynamicVariables) {
            map.put(varBegin + variable.getName() + varEnd, variable.getDescription());
        }
        return map;
    }
    public static int getFirstIndex() {
        return 0;
    }
}
