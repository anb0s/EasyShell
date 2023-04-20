package de.anbos.eclipse.easyshell.plugin.misc;

import org.eclipse.core.variables.IStringVariable;
import org.eclipse.debug.ui.stringsubstitution.IArgumentSelector;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import de.anbos.eclipse.easyshell.plugin.types.Variable;

public class DynamicVariableSelector implements IArgumentSelector {

	@Override
	public String selectArgument(IStringVariable stringVariable, Shell shell) {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, new LabelProvider());
		var varNames = Variable.getVisibleVariables().stream().map(variable -> variable.getName()).sorted()
				.toArray(String[]::new);
		dialog.setElements(varNames);
		dialog.setTitle("Select EasyShell Variable");
		dialog.setMessage("Select an EasyShell variable (? = any character, * = any String):");
		if (dialog.open() == Window.OK) {
			return (String) dialog.getResult()[0];
		}
		return null;
	}

}
