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
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

public class Resource {

    // internal
    private File file = null;
    private IResource iRes = null;

    // resolved
    private String windowsDrive = null;
    private String fullPath = null;
    private String parentPath = null;
    private String fileName = null;
    private String projectName = null;
    static private String lineSeparator = null;

    //Activator.logDebug("full_path  : >" + fullPath + "<");
    //Activator.logDebug("parent_path: >" + parentPath + "<");
    //Activator.logDebug("file_name  : >" + fileName + "<");

    public Resource(Resource myRes) {
        file = myRes.getFile();
        projectName = myRes.getProjectName();
    }

    public Resource(File file, IResource iRes, String projectName) {
        this.file = file;
        this.iRes = iRes;
        this.projectName = projectName;
    }

    public Resource(File file, String projectName) {
        this(file, null, projectName);
    }

    public Resource(IResource iRes) {
        this(iRes.getLocation().toFile(), iRes, iRes.getProject().getName());
    }

    public File getFile() {
        return file;
    }

    public IResource getIResource() {
        return iRes;
    }

    public String getWindowsDrive() {
        if (windowsDrive == null) {
            getFullPath();
            // Try to extract drive on Win32
            if (fullPath.indexOf(":") != -1) {
                windowsDrive = fullPath.substring(0, fullPath.indexOf(":"));
            } else {
                windowsDrive = "";
            }
        }
        return windowsDrive;
    }

    public String getFullPath() {
        if (fullPath == null) {
            fullPath = file.toString();
        }
        return fullPath;
    }

    public String getParentPath() {
        if (parentPath == null) {
            if (file.isDirectory()) {
                parentPath = getFile().getPath();
            } else {
                parentPath = file.getParent();
                fileName = file.getName();
            }
        }
        return parentPath;
    }

    public String getParentName() {
        if (iRes != null) {
            return iRes.getParent().getName();
        }
        return file.getParentFile().getName();
    }

    public String getFileName() {
        if (fileName == null) {
            if (file.isDirectory()) {
                fileName = "";
            } else {
                fileName = file.getName();
            }            
        }
        return fileName;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getLineSeparator() {
        if (lineSeparator == null) {
            lineSeparator = System.getProperty("line.separator");
        }
        return lineSeparator;
    }

    public String getFullQualifiedName() {        
        if (iRes != null) {
            Bundle bundle = Platform.getBundle("org.eclipse.jdt.core");
            if (bundle != null) {
                IJavaElement element = JavaCore.create(iRes);
                if (element instanceof IPackageFragment) {
                    return ((IPackageFragment)element).getElementName();
                } else if (element instanceof ICompilationUnit) {
                    return ((ICompilationUnit)element).findPrimaryType().getFullyQualifiedName();
                }
            }
        }
        return getFullQualifiedPathName();
    }

    public String getFullQualifiedPathName() {
        String fqcn = "";
        if (iRes != null) {
            return iRes.getFullPath().toString();
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
        return getFullPath() != null ? true : false;
    }
}
