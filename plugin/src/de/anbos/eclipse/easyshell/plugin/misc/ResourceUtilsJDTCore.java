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

package de.anbos.eclipse.easyshell.plugin.misc;

import java.io.File;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragment;

import de.anbos.eclipse.easyshell.plugin.types.Resource;

@SuppressWarnings("restriction")
public class ResourceUtilsJDTCore {

    static private File getJarFile(IAdaptable adaptable) {
        JarPackageFragmentRoot jpfr = (JarPackageFragmentRoot) adaptable;
        File file = (File)jpfr.getPath().makeAbsolute().toFile();
        if (!((File)file).exists()) {
            File projectFile = new File(jpfr.getJavaProject().getProject().getLocation().toOSString());
            file = new File(projectFile.getParent() + file.toString());
        }
        return file;
    }

    static public Resource getResource(IAdaptable adaptable) {
        if ((adaptable instanceof PackageFragment)
                && (((PackageFragment) adaptable).getPackageFragmentRoot() instanceof JarPackageFragmentRoot)) {
            return new Resource(getJarFile(((PackageFragment) adaptable).getPackageFragmentRoot()));
        } else if (adaptable instanceof JarPackageFragmentRoot) {
            return new Resource(getJarFile(adaptable));
        }
        return null;
    }

    static public String getFullQualifiedName(IResource resource) {
        IJavaElement element = JavaCore.create(resource);
        if (element instanceof IPackageFragment) {
            return ((IPackageFragment)element).getElementName();
        } else if (element instanceof ICompilationUnit) {
            IType type = ((ICompilationUnit)element).findPrimaryType();
            if (type != null) {
                return type.getFullyQualifiedName();
            }
        }
        return null;
    }

}
