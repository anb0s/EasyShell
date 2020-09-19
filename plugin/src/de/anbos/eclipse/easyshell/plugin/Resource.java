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

package de.anbos.eclipse.easyshell.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.types.OS;

public class Resource {

    // internal
    private File file = null;
    private IResource resource = null;

    // resolved
    private String projectName = Activator.getResourceString("easyshell.plugin.name");

    public Resource(Resource myRes) {
        this.file = myRes.getFile();
        this.resource = myRes.getResource();
    }

    public Resource(File file, IResource resource) {
        this.file = file;
        this.resource = resource;
    }

    public Resource(File file) {
        this(file, null);
    }

    public Resource(IResource resource) {
        this(resource.getLocation().toFile(), resource);
    }

    public File getFile() {
        return file;
    }

    public IResource getResource() {
        return resource;
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

    public String getResourceLocation() {
        return file.getPath();
    }

    public String getResourceName() {
        if (resource != null) {
            return resource.getName();
        } else {
            /*if (file.isDirectory()) {
                resourceName = "";
            } else {*/
            return file.getName();
            //}
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
        return projectName;
    }

    public String getProjectPath() {
        if (resource != null) {
            return resource.getProject().getFullPath().toString();
        }
        return "";
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
            Bundle bundle = Platform.getBundle("org.eclipse.jdt.core");
            if (bundle != null) {
                IJavaElement element = JavaCore.create(resource);
                if (element instanceof IPackageFragment) {
                    return ((IPackageFragment)element).getElementName();
                } else if (element instanceof ICompilationUnit) {
                    IType type = ((ICompilationUnit)element).findPrimaryType();
                    if (type != null) {
                        return type.getFullyQualifiedName();
                    }
                }
            }
        }
        return getFullQualifiedPathName();
    }

    public String getFullQualifiedPathName() {
        String fqcn = "";
        if (resource != null) {

            return resource.getFullPath().toString();
            /*
            String[] segments = iRes.getProjectRelativePath().segments();
            for (int i=0;i<segments.length-1;i++) {
                fqcn += segments[i] + ".";
            }
            fqcn += segments[segments.length-1];
            */
            //fqcn += segments[segments.length-1].replaceAll("(\\..*)", "");
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
