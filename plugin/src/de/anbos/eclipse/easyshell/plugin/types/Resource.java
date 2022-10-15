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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.text.IBlockTextSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.misc.ResourceUtils;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;

public class Resource {

    // internal
    private File file = null;
    private IResource resource = null;
    private ISelection textSelection = null;

    // resolved
    private String defaultName = Activator.getResourceString("easyshell.plugin.name");

    public Resource(Resource myRes) {
        this.file = myRes.getFile();
        this.resource = myRes.getResource();
        this.textSelection = myRes.getTextSelection();
    }

    public Resource(File file, IResource resource, ISelection textSelection) {
        this.file = file;
        this.resource = resource;
        this.textSelection = textSelection;
    }

    public Resource(File file) {
        this(file, null, null);
    }

    public Resource(IResource resource) {
        this(resource.getLocation().toFile(), resource, null);
    }

    public void setTextSelection(ISelection textSelection) {
        this.textSelection = textSelection;
    }

    public File getFile() {
        return file;
    }

    public IResource getResource() {
        return resource;
    }

    public ISelection getTextSelection() {
      return textSelection;
    }

    public String getWindowsDrive() {
        String loc = getResourceLocation();
        if (loc != null) {
            int index = loc.indexOf(":");
            if (index != -1) {
                return loc.substring(0, index);
            }
        }
        return "";
    }

    public String getContainerLocation() {
        if (file.isDirectory()) {
            return file.getPath();
        }
        return file.getParent();
    }

    public String getContainerName() {
        if (resource != null) {
            if (resource.getType() == IResource.FILE) {
                return resource.getParent().getName();
            } else {
                return resource.getName();
            }
        } else {
            if (file.isFile()) {
                return file.getParentFile().getName();
            } else {
                return file.getName();
            }
        }
    }

    public String getContainerPath() {
        if (resource != null) {
            if (resource.getType() == IResource.FILE) {
                return resource.getParent().getFullPath().toString();
            } else {
                return resource.getFullPath().toString();
            }
        }
        return "";
    }

    public String getContainerLocPath() {
        if (resource != null) {
            Path basePath = Paths.get(resource.getProject().getRawLocation().toFile().getParentFile().toURI());
            Path resourcePath = null;
            if (resource.getType() == IResource.FILE) {
                resourcePath = Paths.get(resource.getRawLocation().toFile().getParentFile().toURI());
            } else {
                resourcePath = Paths.get(resource.getRawLocationURI());
            }
            return basePath.relativize(resourcePath).toString();
        }
        return "";
    }

    public String getContainerProjectPath() {
        if (resource != null) {
            if (resource.getType() == IResource.FILE) {
                return resource.getParent().getProjectRelativePath().toString();
            } else {
                return resource.getProjectRelativePath().toString();
            }
        }
        return "";
    }

    public String getContainerProjectLocPath() {
        if (resource != null) {
            Path basePath = Paths.get(resource.getProject().getRawLocation().toFile().toURI());
            Path resourcePath = null;
            if (resource.getType() == IResource.FILE) {
                resourcePath = Paths.get(resource.getRawLocation().toFile().getParentFile().toURI());
            } else {
                resourcePath = Paths.get(resource.getRawLocationURI());
            }
            return basePath.relativize(resourcePath).toString();
        }
        return "";
    }

    public String getParentLocation() {
        return file.getParent();
    }

    public String getParentName() {
        if (resource != null) {
            return resource.getParent().getName();
        } else {
            return file.getParentFile().getName();
        }
    }

    public String getParentPath() {
        if (resource != null) {
            return resource.getParent().getFullPath().toString();
        }
        return "";
    }

    public String getParentLocPath() {
        if (resource != null) {
            Path basePath = Paths.get(resource.getProject().getRawLocation().toFile().getParentFile().toURI());
            Path resourcePath = Paths.get(resource.getRawLocation().toFile().getParentFile().toURI());
            return basePath.relativize(resourcePath).toString();
        }
        return "";
    }

    public String getParentProjectPath() {
        if (resource != null) {
            return resource.getParent().getProjectRelativePath().toString();
        }
        return "";
    }

    public String getParentProjectLocPath() {
        if (resource != null) {
            Path basePath = Paths.get(resource.getProject().getRawLocation().toFile().toURI());
            Path resourcePath = Paths.get(resource.getRawLocation().toFile().getParentFile().toURI());
            return basePath.relativize(resourcePath).toString();
        }
        return "";
    }

    public String getResourceLocation() {
        return file.getPath();
    }

    public String getResourceName() {
        if (resource != null) {
            return resource.getName();
        } else {
            return file.getName();
        }
    }

    public String getResourceBasename() {
        return Utils.getFileBasename(getResourceName());
    }

    public String getResourceExtension() {
        return Utils.getFileExtension(getResourceName());
    }

    public String getResourcePath() {
        if (resource != null) {
            return resource.getFullPath().toString();
        }
        return "";
    }

    public String getResourceLocPath() {
        if (resource != null) {
            Path basePath = Paths.get(resource.getProject().getRawLocation().toFile().getParentFile().toURI());
            Path resourcePath = Paths.get(resource.getRawLocation().toFile().toURI());
            return basePath.relativize(resourcePath).toString();
        }
        return "";
    }

    public String getResourceProjectPath() {
        if (resource != null) {
            return resource.getProjectRelativePath().toString();
        }
        return "";
    }

    public String getResourceProjectLocPath() {
        if (resource != null) {
            Path basePath = Paths.get(resource.getProject().getRawLocation().toFile().toURI());
            Path resourcePath = Paths.get(resource.getRawLocation().toFile().toURI());
            return basePath.relativize(resourcePath).toString();
        }
        return "";
    }

    public String getSelectedTextStartLine() {
        if (textSelection != null && textSelection instanceof ITextSelection) {
            return String.valueOf(((ITextSelection)textSelection).getStartLine()+1);
        }
        return "";
    }

    public String getSelectedTextEndLine() {
        if (textSelection != null && textSelection instanceof ITextSelection) {
            return String.valueOf(((ITextSelection)textSelection).getEndLine()+1);
        }
        return "";
    }

    public String getSelectedTextLength() {
        if (textSelection != null) {
            if (textSelection instanceof IBlockTextSelection) {
                return String.valueOf(((IBlockTextSelection)textSelection).getText().length());
            } else {
                return String.valueOf(((ITextSelection)textSelection).getLength());
            }
        }
        return "";
    }

    public String getSelectedTextOffset() {
        if (textSelection != null && textSelection instanceof ITextSelection) {
            return String.valueOf(((ITextSelection)textSelection).getOffset());
        }
        return "";
    }

    public String getSelectedText() {
        if (textSelection != null) {
            if (textSelection instanceof IBlockTextSelection) {
                return ((IBlockTextSelection)textSelection).getText();
            } else {
                return ((ITextSelection)textSelection).getText();
            }
        }
        return "";
    }

    public String getProjectLocation() {
        if (resource != null) {
            return resource.getProject().getLocation().toFile().toString();
        }
        return "";
    }

    public String getProjectName() {
        if (resource != null) {
            return resource.getProject().getName();
        }
        return defaultName;
    }

    public String getProjectPath() {
        if (resource != null) {
            return resource.getProject().getFullPath().toString();
        }
        return "";
    }

    public String getProjectLocName() {
        if (resource != null) {
            return resource.getProject().getRawLocation().toFile().getName();
        }
        return "";
    }

    public String getProjectLocPath() {
        if (resource != null) {
            return resource.getProject().getRawLocation().toFile().getName();
        }
        return "";
    }

    public String getProjectParentLoc() {
        if (resource != null) {
            return resource.getProject().getLocation().toFile().getParent().toString();
        }
        return "";
    }

    public String getWorkspaceLocation() {
        if (resource != null) {
            return resource.getWorkspace().getRoot().getLocation().toFile().toString();
        }
        return "";
    }

/*
    public String getWorkspaceName() {
        if (resource != null) {
            return resource.getWorkspace().getRoot().
        }
        return defaultName;
    }
*/

    public String getWorkspaceLocName() {
        if (resource != null) {
            return resource.getWorkspace().getRoot().getLocation().toFile().getName();
        }
        return defaultName;
    }

    public String getLineSeparator(OS os) {
        switch(os) {
        case osUnix:
        case osLinux:
        case osMacOSX:
            return new String ("\n");
        case osWindows:
            return new String ("\r\n");
        default:
            return System.getProperty("line.separator");
        }
    }

    public String getPathSeparator(OS os) {
        switch(os) {
        case osUnix:
        case osLinux:
        case osMacOSX:
            return new String (":");
        case osWindows:
            return new String (";");
        default:
            return File.pathSeparator; //System.getProperty("path.separator");
        }
    }

    public String getFileSeparator(OS os) {
        switch(os) {
        case osUnix:
        case osLinux:
        case osMacOSX:
            return new String ("/");
        case osWindows:
            return new String ("\\");
        default:
            return File.separator; //System.getProperty("file.separator");
        }
    }

    public String getFullQualifiedName() {
        if (resource != null) {
            String res = ResourceUtils.getFullQualifiedName(resource);
            if (res != null) {
                return res;
            }
        }
        return getFullQualifiedPathName();
    }

    public String getFullQualifiedPathName() {
        String fqcn = "";
        if (resource != null) {
            return resource.getFullPath().toString();
        }
        return fqcn;
    }

    public boolean resolve() {
        return getResourceLocation() != null ? true : false;
    }

    public boolean isFile() {
        if (resource != null) {
            if (resource.getType() == IResource.FILE) {
                return true;
            } else {
                return false;
            }
        } else {
            if (file.isFile()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isDirectory() {
        if (resource != null) {
            if (resource.getType() == IResource.FOLDER) {
                return true;
            } else {
                return false;
            }
        } else {
            if (file.isDirectory()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public String getScriptBash(String parameter) {
        String prefix = "scripts/";
        URL fileURL = Activator.getDefault().getBundle().getEntry(prefix + parameter);
        File file = null;
        try {
            URL resolvedFileURL = FileLocator.resolve(fileURL);
            if (resolvedFileURL != null) {
                file = new File(resolvedFileURL.toURI().normalize());
            }
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return file != null && file.exists() ? file.getAbsolutePath() : "file does not exists";
    }

}
