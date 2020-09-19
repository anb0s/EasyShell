/**
 * Copyright (c) 2014-2020 Andre Bossert <anb0s@anbos.de>.
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

package de.anbos.eclipse.easyshell.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.ResourceUtils;
import de.anbos.eclipse.easyshell.plugin.actions.Action;
import de.anbos.eclipse.easyshell.plugin.actions.ActionDelegate;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.CommandTokenizer;

public class Execute extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
        if (activePart != null) {
            String commandID  = event.getCommand().getId();
            ResourceType resourceType = ResourceType.getFromEnum(event.getParameter("de.anbos.eclipse.easyshell.plugin.commands.parameter.resource"));
            CommandType commandType = CommandType.getFromAction(event.getParameter("de.anbos.eclipse.easyshell.plugin.commands.parameter.type"));
            String commandValue = event.getParameter("de.anbos.eclipse.easyshell.plugin.commands.parameter.value");
            String commandWorkingDir = event.getParameter("de.anbos.eclipse.easyshell.plugin.commands.parameter.workingdir");
            CommandTokenizer commandTokenizer = CommandTokenizer.getFromEnum(event.getParameter("de.anbos.eclipse.easyshell.plugin.commands.parameter.tokenizer"));
            ActionDelegate action = ResourceUtils.getActionExactResourceType(activePart, resourceType);
            if (action != null) {
                action.setResourceType(resourceType);
                action.setCommandType(commandType);
                action.setCommandValue(commandValue);
                action.setCommandWorkingDir(commandWorkingDir);
                action.setCommandTokenizer(commandTokenizer);
                Action act = new Action(commandID);
                action.run((IAction)act);
            }
            action = null;
        } else {
            Activator.logError("HandlerUtil.getActivePart() returns null: see Eclipse platform bug: https://bugs.eclipse.org/bugs/show_bug.cgi?id=242246", null);
        }
        return null;
    }
}
