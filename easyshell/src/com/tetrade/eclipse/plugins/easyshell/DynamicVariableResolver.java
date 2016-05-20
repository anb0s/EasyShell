/*
 * Copyright (C) 2014 - 2016 by Andre Bossert
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
