/**
 * Copyright (c) 2014-2022 Andre Bossert <anb0s@anbos.de>.
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;

import de.anbos.eclipse.easyshell.plugin.preferences.CommandData;

public enum Variable {
    varUnknown(false, false, "unknown", "unknown", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return "unknown";
        };
    }),
    // ${easyshell:resource_loc} == {2}
    varResourceLoc(true, false, "resource_loc", "absolute path of file or directory", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getResourceLocation();
        };
    }),
    // ${easyshell:resource_name} == {3}
    varResourceName(true, false, "resource_name", "name of file or directory", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getResourceName();
        };
    }),
    // ${easyshell:resource_basename}
    varResourceBasename(true, false, "resource_basename", "name of file without extension", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getResourceBasename();
        };
    }),
    // ${easyshell:resource_extension}
    varResourceExtension(true, false, "resource_extension", "extension of file name (without '.')", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getResourceExtension();
        };
    }),
    // ${easyshell:resource_path}
    varResourcePath(true, false, "resource_path", "relative path of file or directory (to workspace)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getResourcePath();
        };
    }),
    // ${easyshell:resource_loc_path}
    varResourceLocPath(true, false, "resource_loc_path", "relative location path of file or directory (to workspace)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getResourceLocPath();
        };
    }),
    // ${easyshell:resource_project_path}
    varResourceProjectPath(true, false, "resource_project_path", "relative path of file or directory (to project)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getResourceProjectPath();
        };
    }),
    // ${easyshell:resource_project_loc_path}
    varResourceProjectLocPath(true, false, "resource_project_loc_path", "relative location path of file or directory (to project)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getResourceProjectLocPath();
        };
    }),
    // ${easyshell:resource_line_number}
    varResourceLineNumber(true, false, "resource_line_number", "selected text line number", new IVariableResolver() {
      public String resolve(Object object, Object parameter) {
          if(!(object instanceof Resource)) {
              return "";
          }
          return ((Resource)object).getSelectedTextStartLine();
      };
    }),
    // ${easyshell:selected_text_start_line} == ${easyshell:resource_line_number}
    varSelectedTextStartLine(true, false, "selected_text_start_line", "selected text start line", new IVariableResolver() {
      public String resolve(Object object, Object parameter) {
          if(!(object instanceof Resource)) {
              return "";
          }
          return ((Resource)object).getSelectedTextStartLine();
      };
    }),
    // ${easyshell:selected_text_end_line}
    varSelectedTextEndLine(true, false, "selected_text_end_line", "selected text end line", new IVariableResolver() {
      public String resolve(Object object, Object parameter) {
          if(!(object instanceof Resource)) {
              return "";
          }
          return ((Resource)object).getSelectedTextEndLine();
      };
    }),
    // ${easyshell:selected_text_length}
    varSelectedTextLength(true, false, "selected_text_length", "selected text length", new IVariableResolver() {
      public String resolve(Object object, Object parameter) {
          if(!(object instanceof Resource)) {
              return "";
          }
          return ((Resource)object).getSelectedTextLength();
      };
    }),
    // ${easyshell:selected_text_offset}
    varSelectedTextOffset(true, false, "selected_text_offset", "selected text offset", new IVariableResolver() {
      public String resolve(Object object, Object parameter) {
          if(!(object instanceof Resource)) {
              return "";
          }
          return ((Resource)object).getSelectedTextOffset();
      };
    }),
    // ${easyshell:selected_text}
    varSelectedText(true, false, "selected_text", "selected text", new IVariableResolver() {
      public String resolve(Object object, Object parameter) {
          if(!(object instanceof Resource)) {
              return "";
          }
          return ((Resource)object).getSelectedText();
      };
    }),
    // ${easyshell:container_loc} == {1}
    varContainerLoc(true, false, "container_loc", "absolute path of file directory or directory itself", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getContainerLocation();
        };
    }),
    // ${easyshell:container_name}
    varContainerName(true, false, "container_name", "name of file directory or directory itself", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getContainerName();
        };
    }),
    // ${easyshell:container_path}
    varContainerPath(true, false, "container_path", "relative path of file's parent directory or directory itself (to workspace)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getContainerPath();
        };
    }),
    // ${easyshell:container_loc_path}
    varContainerLocPath(true, false, "container_loc_path", "relative location path of file's parent directory or directory itself (to workspace)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getContainerLocPath();
        };
    }),
    // ${easyshell:container_project_path}
    varContainerProjectPath(true, false, "container_project_path", "relative path of file's parent directory or directory itself (to project)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getContainerProjectPath();
        };
    }),
    // ${easyshell:container_project_loc_path}
    varContainerProjectLocPath(true, false, "container_project_loc_path", "relative location path of file's parent directory or directory itself (to project)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getContainerProjectLocPath();
        };
    }),
    // ${easyshell:parent_loc} for file it's equal to ${easyshell:container_loc}
    varParentLoc(true, false, "parent_loc", "absolute path of parent directory\n\nfor files it's equal to ${easyshell:container_loc}", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getParentLocation();
        };
    }),
    // ${easyshell:parent_name} for file it's equal to ${easyshell:container_name}
    varParentName(true, false, "parent_name", "name of parent directory\n\nfor files it's equal to ${easyshell:container_name}", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getParentName();
        };
    }),
    // ${easyshell:parent_path} for file it's equal to ${easyshell:container_path}
    varParentPath(true, false, "parent_path", "relative path of parent directory (to workspace)\n\nfor files it's equal to ${easyshell:container_path}", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getParentPath();
        };
    }),
    // ${easyshell:parent_loc_path} for file it's equal to ${easyshell:container_loc_path}
    varParentLocPath(true, false, "parent_loc_path", "relative location path of parent directory (to workspace)\n\nfor files it's equal to ${easyshell:container_loc_path}", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getParentLocPath();
        };
    }),
    // ${easyshell:parent_project_path} for file it's equal to ${easyshell:container_project_path}
    varParentProjectPath(true, false, "parent_project_path", "relative path of parent directory (to project)\n\nfor files it's equal to ${easyshell:container_project_path}", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getParentProjectPath();
        };
    }),
    // ${easyshell:parent_project_loc_path} for file it's equal to ${easyshell:container_project_loc_path}
    varParentProjectLocPath(true, false, "parent_project_loc_path", "relative location path of parent directory (to project)\n\nfor files it's equal to ${easyshell:container_project_loc_path}", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getParentProjectLocPath();
        };
    }),
    // ${easyshell:project_loc}
    varProjectLoc(true, false, "project_loc", "absolute path of project", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getProjectLocation();
        };
    }),
    // ${easyshell:project_name} == {4}
    varProjectName(true, false, "project_name", "name of project", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            return ((Resource)object).getProjectName();
        };
    }),
    // ${easyshell:project_path}
    varProjectPath(true, false, "project_path", "relative path of project (to workspace)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getProjectPath();
        };
    }),
    // ${easyshell:project_loc_name} == {4}
    varProjectLocName(true, false, "project_loc_name", "location name (folder) of project", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            return ((Resource)object).getProjectLocName();
        };
    }),
/*
    // ${easyshell:project_loc_path}
    varProjectLocPath(true, false, "project_loc_path", "relative location path of project (to workspace)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getProjectLocPath();
        };
    }),
*/
    // ${easyshell:project_parent_loc}
    varProjectParentLoc(true, false, "project_parent_loc", "absolute path of project's parent", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getProjectParentLoc();
        };
    }),
    // ${easyshell:workspace_loc}
    varWorkspaceLoc(true, false, "workspace_loc", "absolute path of workspace", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getWorkspaceLocation();
        };
    }),
/*
    // ${easyshell:workspace_name}
    varWorkspaceName(true, false, "workspace_name", "name of workspace (same as workspace_loc_name if not configured)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            return ((Resource)object).getWorkspaceName();
        };
    }),
*/
    // ${easyshell:workspace_loc_name}
    varWorkspaceLocName(true, false, "workspace_loc_name", "location name (folder) of workspace", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            return ((Resource)object).getWorkspaceLocName();
        };
    }),
    // ${easyshell:windows_drive} == {0}
    varWindowsDrive(true, false, "windows_drive", "drive letter of file or directory on Windows", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getWindowsDrive();
        };
    }),
    // ${easyshell:qualified_name}
    varQualifiedName(true, false, "qualified_name", "full qualified (class) name", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getFullQualifiedName();
        };
    }),
    // ${easyshell:line_separator} == {5}
    varLineSeparator(true, false, "line_separator", "line separator, e.g. '\\n' (Unix) or '\\r\\n' (Windows)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getLineSeparator(OS.osUnknown);
        };
    }),
    // ${easyshell:line_separator_unix}
    varLineSeparatorUnix(false, false, "line_separator_unix", "line separator '\\n' (Unix)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getLineSeparator(OS.osUnix);
        };
    }),
    // ${easyshell:line_separator_windows}
    varLineSeparatorWindows(false, false, "line_separator_windows", "line separator '\\r\\n' (Windows)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getLineSeparator(OS.osWindows);
        };
    }),
    // ${easyshell:path_separator}
    varPathSeparator(true, false, "path_separator", "path separator, e.g. ':' (Unix) or ';' (Windows)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getPathSeparator(OS.osUnknown);
        };
    }),
    // ${easyshell:path_separator_unix}
    varPathSeparatorUnix(false, false, "path_separator_unix", "path separator ':' (Unix)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getPathSeparator(OS.osUnix);
        };
    }),
    // ${easyshell:path_separator_windows}
    varPathSeparatorWindows(false, false, "path_separator_windows", "path separator, e.g. ':' (Unix) or ';' (Windows)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getPathSeparator(OS.osWindows);
        };
    }),
    // ${easyshell:file_separator}
    varFileSeparator(true, false, "file_separator", "file separator, e.g. '/' (Unix) or '\\' (Windows)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getFileSeparator(OS.osUnknown);
        };
    }),
    // ${easyshell:file_separator_unix}
    varFileSeparatorUnix(false, false, "file_separator_unix", "file separator '/' (Unix)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getFileSeparator(OS.osUnix);
        };
    }),
    // ${easyshell:file_separator_windows}
    varFileSeparatorWindows(false, false, "file_separator_windows", "file separator '\\' (Windows)", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getFileSeparator(OS.osWindows);
        };
    }),
    varScriptBash(true, false, "script_bash", "Bash script", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof Resource)) {
                return "";
            }
            return ((Resource)object).getScriptBash((String)parameter);
        };
    }),
    varCommandCategory(false, true, "command_category", "command category", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof CommandData)) {
                return "";
            }
            return ((CommandData)object).getCategory().getName();
        };
    }),
    varCommandType(false, true, "command_type", "command type", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof CommandData)) {
                return "";
            }
            return ((CommandData)object).getCommandType().getName();
        };
    }),
    varCommandName(false, true, "command_name", "command name", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof CommandData)) {
                return "";
            }
            return ((CommandData)object).getName();
        };
    }),
    varCommandOS(false, true, "command_os", "command operating system", new IVariableResolver() {
        public String resolve(Object object, Object parameter) {
            if(!(object instanceof CommandData)) {
                return "";
            }
            return ((CommandData)object).getOs().getName();
        };
    });
    // attributes
    private final boolean visible;
    private final boolean internal;
    private String name;
    private String description;
    private final IVariableResolver resolver;
    static private final String varBegin = "${";
    static private final String varEnd   = "}";
    static private final String varParamDelimiter = ":";
    static private final String varEasyShell = "easyshell";
    // construct
    Variable(boolean visible, boolean internal, String name, String description, IVariableResolver resolver) {
        this.visible = visible;
        this.internal=internal;
        this.name = name;
        this.description = description;
        this.resolver = resolver;
    }
    public boolean isVisible() {
        return visible;
    }
    public boolean isInternal() {
        return internal;
    }
    public String getName() {
        return name;
    }
    public String getFullVariableName() {
        return varBegin + varEasyShell + varParamDelimiter + name + varEnd;
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
    public static List<Variable> getVariables(boolean visibleOnly, boolean internal) {
        List<Variable> list = new ArrayList<>();
        for(int i = 0; i < Variable.values().length; i++) {
            if (!visibleOnly || (visibleOnly && Variable.values()[i].isVisible())) {
                if (internal == Variable.values()[i].isInternal()) {
                    list.add(Variable.values()[i]);
                }
            }
        }
        return list;
    }
    public static List<Variable> getVisibleVariables() {
        return getVariables(true, false);
    }
    public static List<Variable> getInternalVariables() {
        return getVariables(false, true);
    }
    public static Map<String, String> getVariableInfoMap(boolean visibleOnly, boolean internal) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for(Variable variable : getVariables(visibleOnly, internal)) {
            map.put(variable.getFullVariableName(), variable.getDescription());
        }
        return map;
    }
    public static Map<String, String> getVariableInfoMap() {
        return getVariableInfoMap(true, false);
    }
    public static Map<String, String> getInternalVariableInfoMap() {
        return getVariableInfoMap(false, true);
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
