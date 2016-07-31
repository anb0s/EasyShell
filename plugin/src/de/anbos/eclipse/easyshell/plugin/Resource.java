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

package de.anbos.eclipse.easyshell.plugin;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

public class Resource {

    // internal
    private File file = null;
    private IResource resource = null;

    // resolved
    private String windowsDrive = null;
    private String resourceLocation = null;
    private String containerLocation = null;
    private String resourceName = null;
    private String projectName = Activator.getResourceString("easyshell.plugin.name");
    static private String lineSeparator = null;

    //Activator.logDebug("full_path  : >" + fullPath + "<");
    //Activator.logDebug("parent_path: >" + parentPath + "<");
    //Activator.logDebug("file_name  : >" + fileName + "<");

    public Resource(Resource myRes) {
        file = myRes.getFile();
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
        if (windowsDrive == null) {
            getResourceLocation();
            // Try to extract drive on Win32
            if (resourceLocation.indexOf(":") != -1) {
                windowsDrive = resourceLocation.substring(0, resourceLocation.indexOf(":"));
            } else {
                windowsDrive = "";
            }
        }
        return windowsDrive;
    }

    public String getContainerLocation() {
        if (containerLocation == null) {
            if (file.isDirectory()) {
                containerLocation = file.getPath();
            } else {
                containerLocation = file.getParent();
            }
        }
        return containerLocation;
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

    public String getResourceLocation() {
        if (resourceLocation == null) {
            resourceLocation = file.getPath();
        }
        return resourceLocation;
    }

    public String getResourceName() {
        if (resourceName == null) {
            if (resource != null) {
                resourceName = resource.getName();
            } else {
                /*if (file.isDirectory()) {
                    resourceName = "";
                } else {*/
                    resourceName = file.getName();
                //}                            
            }
        }
        return resourceName;
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

    public String getLineSeparator() {
        if (lineSeparator == null) {
            lineSeparator = System.getProperty("line.separator");
        }
        return lineSeparator;
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

}
