package com.tetrade.eclipse.plugins.easyshell.actions;

import java.io.File;
import java.net.URI;
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
import org.eclipse.ui.ide.FileStoreEditorInput;
import com.tetrade.eclipse.plugins.easyshell.EasyShellPlugin;

public class EasyShellAction implements IObjectActionDelegate {

	private boolean debug = false;

	private File[] resource = null;
	private String projectName = null;
	private IStructuredSelection currentSelection;

	/**
	 * Constructor for EasyExploreAction.
	 */
	public EasyShellAction() {
		super();
		debug = EasyShellPlugin.getDefault().isDebug();
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

		if (!isEnabled()) {
			MessageDialog.openInformation(
				new Shell(),
				"Easy Shell",
				"Wrong Selection");
			EasyShellPlugin.log("Wrong Selection");
			return;
		}

		String ActionIDStr = action.getId();
		if (debug) System.out.println("Action ID: [[" + ActionIDStr + "]]");
		String[] EasyShellActionStr = { "com.tetrade.eclipse.plugins.easyshell.actions.EasyShellActionOpen",
										"com.tetrade.eclipse.plugins.easyshell.actions.EasyShellActionRun",
										"com.tetrade.eclipse.plugins.easyshell.actions.EasyShellActionExplore"
		};
		int ActionIDNum = -1;
		for (int i=0;i<EasyShellActionStr.length;i++)
		{
			if (ActionIDStr.equals(EasyShellActionStr[i]))
			{
				ActionIDNum = i;
				break;
			}
		}

		if (ActionIDNum == -1) {
			MessageDialog.openInformation(
				new Shell(),
				"Easy Shell",
				"Wrong Action ID");
			EasyShellPlugin.log("Wrong Action ID");
			return;
		}

		for (int i=0;i<resource.length;i++) {

			if (resource[i] == null)
				continue;

			String drive = null;
			String full_path = null;
			String parent_path = null;
			String file_name = null;

			full_path = resource[i].toString();
			if (resource[i].isDirectory()) {
				parent_path = resource.toString();
				file_name = "dir"; // dummy cmd
			}else
			{
				parent_path = resource[i].getParent();
				file_name = resource[i].getName();
			}

			if (full_path != null) {

				if (debug) {
					System.out.println("full_path  : [[" + full_path + "]]");
					System.out.println("parent_path: [[" + parent_path + "]]");
				}
				// Try to extract drive on Win32
				if (full_path.indexOf(":") != -1) {
					drive = full_path.substring(0, full_path.indexOf(":"));
				}

				try {
					String target = EasyShellPlugin.getDefault().getTarget(ActionIDNum);
					String[] args = new String[5];

					args[0] = drive;
					args[1] = parent_path;
					args[2] = full_path;
					args[3] = file_name;
					args[4] = projectName == null ? "EasyShell" : projectName;

					String cmd = MessageFormat.format(target, (Object[])args);
					if (debug) System.out.println("cmd: [[" + cmd + "]]");

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
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	    currentSelection = selection instanceof IStructuredSelection ? (IStructuredSelection)selection : null;
	}

	protected boolean isEnabled()
	{
		boolean enabled = false;
		if (currentSelection != null)
		{
			Object[] selectedObjects = currentSelection.toArray();
			if (selectedObjects.length >= 1)
			{
				resource = new File[selectedObjects.length];
				for (int i=0;i<selectedObjects.length;i++) {
					resource[i] = getResource(selectedObjects[i]);
					if (resource != null)
						enabled=true;
				}
			}
		}
		return enabled;
	}

	protected File getResource(Object object) {
		projectName = null;
		if (object instanceof IFile) {
			projectName = ((IFile) object).getProject().getName();
			return ((IFile) object).getLocation().toFile();
		}
		if (object instanceof File) {
			return (File) object;
		}
		if (object instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) object;
			IFile ifile = (IFile) adaptable.getAdapter(IFile.class);
			if (ifile != null) {
				projectName = ifile.getProject().getName();
				return ifile.getLocation().toFile();
			}
			IResource ires = (IResource) adaptable.getAdapter(IResource.class);
			if (ires != null) {
				projectName = ires.getProject().getName();
				return ires.getLocation().toFile();
			}
			if (adaptable instanceof PackageFragment
					&& ((PackageFragment) adaptable).getPackageFragmentRoot() instanceof JarPackageFragmentRoot) {
				return getJarFile(((PackageFragment) adaptable)
						.getPackageFragmentRoot());
			} else if (adaptable instanceof JarPackageFragmentRoot) {
				return getJarFile(adaptable);
			} else if (adaptable instanceof FileStoreEditorInput) {
				URI fileuri = ((FileStoreEditorInput) adaptable).getURI();
				return new File(fileuri.getPath());
			}

			File file = (File) adaptable.getAdapter(File.class);
			if (file != null) {
				return file;
			}
		}
		return null;
	}

	protected File getJarFile(IAdaptable adaptable) {
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

}
