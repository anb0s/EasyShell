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
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
//import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
//import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.ide.FileStoreEditorInput;


public class ResourceUtils {

    static public ISelection getResourceSelection(IWorkbenchPart part) {
        ISelection selection = null;
        if (part != null) {
            if (part instanceof IEditorPart) {
                Resource file = getResource((IEditorPart)part);
                if (file != null) {
                    selection = new StructuredSelection(file);
                }
            } else {
                try {
                    selection = part.getSite().getSelectionProvider().getSelection();
                } catch(Exception e) {
                    // no op
                }
            }
        }
        return selection;
    }

    static public Resource getResource(Object myObj) {
        Object object = null;
        if (myObj instanceof IEditorPart) {
            IEditorPart editorPart = (IEditorPart)myObj;
            IEditorInput input = editorPart.getEditorInput();
            Object adapter = input.getAdapter(IFile.class);
            if(adapter instanceof IFile){
                object = (IFile) adapter;
            } else {
                adapter = editorPart.getAdapter(IFile.class);
                if(adapter instanceof IFile){
                    object = (IFile) adapter;
                } else {
                    object = input;
                }
            }
        } else {
            object = myObj;
        }

        /*
		IEditorInput input  = activeEditorPart.getEditorInput();
        if (input instanceof IFileEditorInput) {
            ifile = ((IFileEditorInput)input).getFile();
        } else if (input instanceof IAdaptable) {
            IAdaptable adaptable = (IAdaptable) input;
            ifile = (IFile) adaptable.getAdapter(IFile.class);
            if (ifile == null) {
                if (adaptable instanceof FileStoreEditorInput) {
                    URI fileuri = ((FileStoreEditorInput) adaptable).getURI();
                    file = new File(fileuri.getPath());
                } else {
                    file = (File) adaptable.getAdapter(File.class);
                }
            }
        }
		*/

        if (object instanceof Resource) {
            return new Resource((Resource)object);
        }

        if (object instanceof IFile) {
            return new Resource(((IFile) object));
        }
        if (object instanceof File) {
            return new Resource((File) object);
        }
        if (object instanceof IAdaptable) {
            IAdaptable adaptable = (IAdaptable) object;
            IFile iFile = (IFile) adaptable.getAdapter(IFile.class);
            if (iFile != null) {
                return new Resource(iFile);
            }
            IResource ires = (IResource) adaptable.getAdapter(IResource.class);
            if (ires != null) {
                IPath path = ires.getLocation();
                if (path != null) {
                    return new Resource(ires);
                }
            }
            /*
            if (adaptable instanceof PackageFragment
                    && ((PackageFragment) adaptable).getPackageFragmentRoot() instanceof JarPackageFragmentRoot) {
                return new Resource(getJarFile(((PackageFragment) adaptable)
                        .getPackageFragmentRoot()),projectName);
            } else if (adaptable instanceof JarPackageFragmentRoot) {
                return new Resource(getJarFile(adaptable),projectName);
            }*/
            else if (adaptable instanceof FileStoreEditorInput) {
                URI fileuri = ((FileStoreEditorInput) adaptable).getURI();
                return new Resource(new File(fileuri.getPath()));
            }
            File file = (File) adaptable.getAdapter(File.class);
            if (file != null) {
                return new Resource(file);
            }
        }
        return null;
    }

    /*
    static public File getJarFile(IAdaptable adaptable) {
        JarPackageFragmentRoot jpfr = (JarPackageFragmentRoot) adaptable;
        File resource = (File) jpfr.getPath().makeAbsolute().toFile();
        if (!((File) resource).exists()) {
            File projectFile =
                new File(
                    jpfr
                        .getJavaProject()
                        .getProject()
                        .getLocation()
                        .toOSString());
            resource = new File(projectFile.getParent() + resource.toString());
        }
        return resource;
    }
    */
}
