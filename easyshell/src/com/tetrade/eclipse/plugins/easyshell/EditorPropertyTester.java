/*
 * Copyright (C) 2004 - 2010 by Marcel Schoen and Andre Bossert
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

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
