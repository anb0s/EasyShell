package com.tetrade.eclipse.plugins.easyshell;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

import com.tetrade.eclipse.plugins.easyshell.actions.EasyShellAction;

public class EditorPropertyTester extends PropertyTester {

	public EditorPropertyTester() {
		super();
	}

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        if("hasResourceSelection".equals(property) && receiver instanceof IWorkbenchPart){
            return hasResourceSelection((IWorkbenchPart)receiver) != null;
        }
		return false;
	}

    static public EasyShellAction hasResourceSelection(IWorkbenchPart part) {
		ISelection selection = ResourceUtils.getResourceSelection(part);
		if (selection != null) {
			EasyShellAction action = new EasyShellAction();
			action.selectionChanged(null, selection);
			if (action.isEnabled())
				return action;
		}
    	return null;
    }

}
