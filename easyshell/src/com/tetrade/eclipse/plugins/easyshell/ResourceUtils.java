package com.tetrade.eclipse.plugins.easyshell;

import java.io.File;
//import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
//import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
//import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
//import org.eclipse.ui.ide.FileStoreEditorInput;


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

    	if (object instanceof Resource) {
    		return new Resource((Resource)object);
    	}

		String projectName = null;
		if (object instanceof IFile) {
			projectName = ((IFile) object).getProject().getName();
			return new Resource(((IFile) object).getLocation().toFile(),projectName);
		}
		if (object instanceof File) {
			return new Resource((File) object,projectName);
		}
		if (object instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) object;
			IFile ifile = (IFile) adaptable.getAdapter(IFile.class);
			if (ifile != null) {
				projectName = ifile.getProject().getName();
				return new Resource(ifile.getLocation().toFile(),projectName);
			}
			IResource ires = (IResource) adaptable.getAdapter(IResource.class);
			if (ires != null) {
				projectName = ires.getProject().getName();
				return new Resource(ires.getLocation().toFile(),projectName);
			}
			/*
			if (adaptable instanceof PackageFragment
					&& ((PackageFragment) adaptable).getPackageFragmentRoot() instanceof JarPackageFragmentRoot) {
				return new Resource(getJarFile(((PackageFragment) adaptable)
						.getPackageFragmentRoot()),projectName);
			} else if (adaptable instanceof JarPackageFragmentRoot) {
				return new Resource(getJarFile(adaptable),projectName);
			} else if (adaptable instanceof FileStoreEditorInput) {
				URI fileuri = ((FileStoreEditorInput) adaptable).getURI();
				return new Resource(new File(fileuri.getPath()),projectName);
			}
			*/
			File file = (File) adaptable.getAdapter(File.class);
			if (file != null) {
				return  new Resource(file,projectName);
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
