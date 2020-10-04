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

package de.anbos.eclipse.easyshell.plugin.editor;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorLauncher;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

public class LauncherAll implements IEditorLauncher {

    protected String getCommandId() {
        return "de.anbos.eclipse.easyshell.plugin.commands.all";
    }

    @Override
    public void open(IPath file) {
        IHandlerService handlerService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(IHandlerService.class);
        try {
            Event event = new Event();
            event.data = file;
            handlerService.executeCommand(getCommandId(), event);
        } catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
            e.printStackTrace();
        }
    }

}
