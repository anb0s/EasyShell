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

package de.anbos.eclipse.easyshell.plugin;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

import de.anbos.eclipse.easyshell.plugin.actions.ActionDelegate;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

public class EditorPropertyTester extends PropertyTester {

    public EditorPropertyTester() {
        super();
    }

    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        if("hasResourceSelection".equals(property) && receiver instanceof IWorkbenchPart){
            if (args.length > 0 && args[0].equals("resourceType")) {
                return getActionCommonResourceType((IWorkbenchPart)receiver, ResourceType.getFromEnum((String)expectedValue)) != null;
            }
        }
        return false;
    }

    static public ActionDelegate getActionCommonResourceType(IWorkbenchPart part, ResourceType resType) {
        ISelection selection = ResourceUtils.getResourceSelection(part);
        if (selection != null) {
            ActionDelegate action = new ActionDelegate();
            action.selectionChanged(null, selection);
            if (action.isEnabled(ResourceType.resourceTypeFileOrDirectory) && resType == action.getCommonResourceType()) {
                return action;
            }
        }
        return null;
    }

    static public ActionDelegate getActionExactResourceType(IWorkbenchPart part, ResourceType resType) {
        ISelection selection = ResourceUtils.getResourceSelection(part);
        if (selection != null) {
            ActionDelegate action = new ActionDelegate();
            action.selectionChanged(null, selection);
            if (action.isEnabled(resType)) {
                return action;
            }
        }
        return null;
    }

}
