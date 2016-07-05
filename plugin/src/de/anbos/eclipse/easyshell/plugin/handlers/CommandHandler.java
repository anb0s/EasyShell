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

package de.anbos.eclipse.easyshell.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import de.anbos.eclipse.easyshell.plugin.EditorPropertyTester;
import de.anbos.eclipse.easyshell.plugin.actions.Action;
import de.anbos.eclipse.easyshell.plugin.actions.ActionDelegate;
import de.anbos.eclipse.easyshell.plugin.preferences.CommandType;

public class CommandHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
        ActionDelegate action = EditorPropertyTester.hasResourceSelection(activePart);
        if (action != null) {
        	String commandID  = event.getCommand().getId();
        	String commandType = event.getParameter("de.anbos.eclipse.easyshell.plugin.commands.parameter.type");
        	String commanValue = event.getParameter("de.anbos.eclipse.easyshell.plugin.commands.parameter.value");
        	action.setCommandType(CommandType.getFromAction(commandType));
        	action.setCommandValue(commanValue);
            Action act = new Action(commandID);
            action.run((IAction)act);
        }
        return null;
    }
}
