/*******************************************************************************
 * Copyright (c) 2014 - 2018 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

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
