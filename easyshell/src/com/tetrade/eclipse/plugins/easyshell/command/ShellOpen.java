package com.tetrade.eclipse.plugins.easyshell.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.tetrade.eclipse.plugins.easyshell.EasyShellMyAction;
import com.tetrade.eclipse.plugins.easyshell.EditorPropertyTester;
import com.tetrade.eclipse.plugins.easyshell.actions.EasyShellAction;

public class ShellOpen implements IHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		EasyShellAction action = EditorPropertyTester.hasResourceSelection(part);
		if (action != null) {
			EasyShellMyAction act = new EasyShellMyAction("com.tetrade.eclipse.plugins.easyshell.actions.EasyShellActionOpen");
			action.run((IAction)act);
		}
		return null;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isHandled() {
		return true;
	}

	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
