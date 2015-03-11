package com.tetrade.eclipse.plugins.easyshell;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;
import org.eclipse.core.variables.IValueVariable;

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
