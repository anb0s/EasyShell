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

package de.anbos.eclipse.easyshell.plugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

public class DynamicVariableResolver implements IDynamicVariableResolver {

	private static String[] args = new String[6];

	@Override
	public String resolveValue(IDynamicVariable variable, String argument)
			throws CoreException {
		if (variable.getName().equals("easyshell")) {
		    if (argument.equals("drive")) {
			    return args[0];
		    } else if (argument.equals("container_loc")) {
				return args[1];
			} else if (argument.equals("resource_loc")) {
				return args[2];
			} else if (argument.equals("resource_name")) {
				return args[3];
			} else if (argument.equals("project_name")) {
				return args[4];
			} else if (argument.equals("line_separator")) {
				return args[5];
			}
		}
		return null;
	}

	public String[] getArgs() {
		return args;
	}

	public static void setArgs(String[] args) {
		DynamicVariableResolver.args = args;
	}

}
