package com.tetrade.eclipse.plugins.easyshell.actions;

import java.io.File;
import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.tetrade.eclipse.plugins.easyshell.EasyShellPlugin;

public class EasyShellAction implements IObjectActionDelegate {

	private Object selected = null;
	//	private Class selectedClass=null;
	/**
	 * Constructor for EasyExploreAction.
	 */
	public EasyShellAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		if (selected == null) {
			MessageDialog.openInformation(
				new Shell(),
				"Easy Shell",
				"Unable to open shell");
			EasyShellPlugin.log("Unable to open shell");
			return;
		}

		String drive = null;
		String path = null;

	    if (selected instanceof IFile) {
			path = ((IFile) selected).getParent().getLocation().toOSString();
	    } else if (selected instanceof IResource) {
			path = ((IResource) selected).getLocation().toOSString();
		}

		if (path != null) {

			//            System.out.println("path: [[" + path + "]]");
			// Try to extract drive on Win32
			if (path.indexOf(":") != -1) {
				drive = path.substring(0, path.indexOf(":"));
			}

			try {
				String target = EasyShellPlugin.getDefault().getTarget();

				String[] args = null;
				if (target.indexOf("{0}") != -1
					&& target.indexOf("{1}") != -1
					&& drive != null) {
					args = new String[2];
					args[0] = drive;
					args[1] = path;
				} else if (target.indexOf("{0}") != -1) {
					args = new String[1];
					args[0] = drive;
				} else if (target.indexOf("{1}") != -1) {
					args = new String[2];
					args[0] = "";
					args[1] = path;
				}

				String cmd = MessageFormat.format(target, args);
//				System.out.println("cmd: [[" + cmd + "]]");

				Runtime.getRuntime().exec(cmd);
			} catch (Exception e) {
				EasyShellPlugin.log(e);
			}

		} else {

			MessageDialog.openInformation(
				new Shell(),
				"Easy Shell",
				"Unable to open shell");
			EasyShellPlugin.log("Unable to open shell");
			return;

		}

	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		IAdaptable adaptable = null;
		this.selected = null;
		if (selection instanceof IStructuredSelection) {
			adaptable =
				(IAdaptable) ((IStructuredSelection) selection)
					.getFirstElement();
			//			this.selectedClass = adaptable.getClass();
			if (adaptable instanceof IResource) {
				this.selected = (IResource) adaptable;
			} else if (
				adaptable instanceof PackageFragment
					&& ((PackageFragment) adaptable).getPackageFragmentRoot()
						instanceof JarPackageFragmentRoot) {
				this.selected =
					getJarFile(
						((PackageFragment) adaptable).getPackageFragmentRoot());
			} else if (adaptable instanceof JarPackageFragmentRoot) {
				this.selected = getJarFile(adaptable);
			} else {
				this.selected =
					(IResource) adaptable.getAdapter(IResource.class);
			}
		}
	}

	protected File getJarFile(IAdaptable adaptable) {
		JarPackageFragmentRoot jpfr = (JarPackageFragmentRoot) adaptable;
		File selected = (File) jpfr.getPath().makeAbsolute().toFile();
		if (!((File) selected).exists()) {
			File projectFile =
				new File(
					jpfr
						.getJavaProject()
						.getProject()
						.getLocation()
						.toOSString());
			selected = new File(projectFile.getParent() + selected.toString());
		}
		return selected;
	}

}
