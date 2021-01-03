/**
 * Copyright (c) 2014-2021 Andre Bossert <anb0s@anbos.de>.
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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.osgi.framework.Bundle;

import de.anbos.eclipse.easyshell.plugin.actions.ActionDelegate;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

import org.eclipse.cdt.internal.core.model.ExternalTranslationUnit;
import org.eclipse.cdt.internal.core.model.IncludeReference;
import org.eclipse.cdt.internal.ui.cview.IncludeReferenceProxy;
import org.eclipse.cdt.internal.ui.includebrowser.IBNode;
import org.eclipse.cdt.core.model.IIncludeReference;


@SuppressWarnings("restriction")
public class ResourceUtils {

    static public ISelection getResourceSelection(Object obj) {
        ISelection selection = null;
        if (obj != null) {
            Resource resource = getResource(obj);
            if (obj instanceof IEditorPart) {
                IEditorPart part = (IEditorPart)obj;
                ITextEditor editor = null;
                if (part instanceof ITextEditor) {
                    editor = (ITextEditor) part;
                } else if (part instanceof MultiPageEditorPart) {
                    Object page = ((MultiPageEditorPart) part).getSelectedPage();
                    if (page instanceof ITextEditor) {
                        editor = (ITextEditor) page;
                    }
                }
                if (editor != null) {
                    ITextSelection sel = (ITextSelection)editor.getSelectionProvider().getSelection();
                    String text = sel.getText();
                    selection = getSelectionFromText(resource, text);
                }
            } else if (obj instanceof IWorkbenchPart) {
                try {
                    selection = ((IWorkbenchPart)obj).getSite().getSelectionProvider().getSelection();
                } catch(Exception e) {
                    // no op
                }
            }
            // fallback to the selected part if still no selection
            if ((selection == null) && (resource != null)) {
                selection = new StructuredSelection(resource);
            }
        }
        return selection;
    }

    private static ISelection getSelectionFromText(Resource partFile, String text) {
        ISelection selection = null;
        if (text != null && !text.isEmpty()) {
            String lines[] = text.split("\\r?\\n");
            List<Resource> resources = new ArrayList<Resource>();
            for (String line : lines) {
                String fileName = line.trim();
                if (fileName != null && !fileName.isEmpty()) {
                    File file = new File(fileName);
                    if (!file.isAbsolute()) {
                        file = new File(partFile.getParentLocation(), file.getPath());
                    }
                    resources.add(new Resource(file));
                }
            }
            if (!resources.isEmpty()) {
                selection = new StructuredSelection(resources);
            }
        }
        return selection;
    }

    static public Resource getResource(Object myObj) {
        Object object = null;

        // handle IEditorPart
        if (myObj instanceof IEditorPart) {
            IEditorPart editorPart = (IEditorPart)myObj;
            IEditorInput input = editorPart.getEditorInput();
            if (input instanceof IFileEditorInput) {
                object = ((IFileEditorInput)input).getFile();
            } else {
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
            }
        } else {
            object = myObj;
        }

        // the most input types first
        if (object instanceof Resource) {
            return new Resource((Resource)object);
        }
        if (object instanceof IFile) {
            return new Resource((IFile)object);
        }
        if (object instanceof IPath) {
            return new Resource(((IPath)object).toFile());
        }
        if (object instanceof File) {
            return new Resource((File)object);
        }

        // still adaptable
        if (object instanceof IAdaptable) {
            IAdaptable adaptable = (IAdaptable) object;
            // IFile
            IFile iFile = (IFile) adaptable.getAdapter(IFile.class);
            if (iFile != null) {
                return new Resource(iFile);
            }
            // IResource
            IResource ires = (IResource) adaptable.getAdapter(IResource.class);
            if (ires != null) {
                IPath path = ires.getLocation();
                if (path != null) {
                    return new Resource(ires);
                }
            }
            // FileStoreEditorInput
            if (adaptable instanceof FileStoreEditorInput) {
                URI fileuri = ((FileStoreEditorInput) adaptable).getURI();
                return new Resource(new File(fileuri.getPath()));
            }
            // optional org.eclipse.jdt.core
            Bundle bundle = Platform.getBundle("org.eclipse.jdt.core");
            if (bundle != null) {
                if ( (adaptable instanceof PackageFragment) &&
                     ( ((PackageFragment) adaptable).getPackageFragmentRoot() instanceof JarPackageFragmentRoot) ) {
                    return new Resource(getJarFile(((PackageFragment) adaptable)
                            .getPackageFragmentRoot()));
                } else if (adaptable instanceof JarPackageFragmentRoot) {
                    return new Resource(getJarFile(adaptable));
                }
            }
            // optional org.eclipse.cdt.core
            bundle = Platform.getBundle("org.eclipse.cdt.core");
            if (bundle != null) {
                IPath path = null;
                if (adaptable instanceof IncludeReferenceProxy) {
                    IIncludeReference includeRef = ((IncludeReferenceProxy) adaptable).getReference();
                    path = includeRef.getPath();
                } else if (adaptable instanceof IncludeReference) {
                    IIncludeReference includeRef = ((IncludeReference) adaptable);
                    path = includeRef.getPath();
                } else if (adaptable instanceof ExternalTranslationUnit) {
                    path = ((ExternalTranslationUnit) adaptable).getLocation();
                } else if (adaptable instanceof IBNode) {
                    path = ((IBNode) adaptable).getRepresentedPath();
                }
                if (path != null) {
                    return new Resource(path.toFile());
                }
            }
            // File
            File file = (File) adaptable.getAdapter(File.class);
            if (file != null) {
                return new Resource(file);
            }
        }
        return null;
    }

    static private File getJarFile(IAdaptable adaptable) {
        JarPackageFragmentRoot jpfr = (JarPackageFragmentRoot) adaptable;
        File file = (File)jpfr.getPath().makeAbsolute().toFile();
        if (!((File)file).exists()) {
            File projectFile = new File(jpfr.getJavaProject().getProject().getLocation().toOSString());
            file = new File(projectFile.getParent() + file.toString());
        }
        return file;
    }

    static public ActionDelegate getActionCommonResourceType(Object obj, ResourceType resType) {
        ISelection selection = getResourceSelection(obj);
        if (selection != null) {
            ActionDelegate action = new ActionDelegate();
            action.selectionChanged(null, selection);
            if (action.isEnabled(ResourceType.resourceTypeFileOrDirectory) && resType == action.getCommonResourceType()) {
                return action;
            }
        }
        return null;
    }

    static public ActionDelegate getActionExactResourceType(Object obj, ResourceType resType) {
        ISelection selection = getResourceSelection(obj);
        if (selection != null) {
            ActionDelegate action = new ActionDelegate();
            action.selectionChanged(null, selection);
            if (action.isEnabled(resType)) {
                return action;
            }
        }
        return null;
    }

    static public ResourceType getCommonResourceType(Object obj) {
        ISelection selection = getResourceSelection(obj);
        if (selection != null) {
            ActionDelegate action = new ActionDelegate();
            action.selectionChanged(null, selection);
            if (action.isEnabled(ResourceType.resourceTypeFileOrDirectory)) {
                return action.getCommonResourceType();
            }
        }
        return null;
    }

    public static Object getEventData(ExecutionEvent event) {
        Object triggerEventData = null;
        Object trigger = event.getTrigger();
        if ((trigger != null) && (trigger instanceof Event)) {
            triggerEventData = ((Event)trigger).data;
        }
        if (triggerEventData == null) {
            triggerEventData = HandlerUtil.getActivePart(event);
        }
        return triggerEventData;
    }

}
