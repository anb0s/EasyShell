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

import de.anbos.eclipse.easyshell.plugin.types.Quotes;

public class DynamicVariableResolver implements IDynamicVariableResolver {

    static private Resource resource = null;
    static Quotes quotes = Quotes.quotesNo;

	@Override
	public String resolveValue(IDynamicVariable variable, String argument)
			throws CoreException {
	    // easyshell own variables
	    String variableName = variable.getName();
		if (variableName.equals("easyshell")) {
		    return handleOwnVariable(argument);
		} else {
            return handleEclipseVariable(variableName, argument);
        }
	}

    private String handleOwnVariable(String argument) {
        if (argument.equals("windows_drive")) {
            return resource.getWindowsDrive();             // ${easyshell:windows_drive} == {0}
        } else if (argument.equals("line_separator")) {
        	return resource.getLineSeparator();            // ${easyshell:line_separator} == {5}
        } else if (argument.equals("qualified_name")) {
            return resource.getFullQualifiedName();        // ${easyshell:qualified_name}
        }
        // here we have a eclipse variable embedded in easyshell variable as parameter
        return handleEclipseVariable(argument, null);
    }

    private String handleEclipseVariable(String variable, String argument) {
        if (variable.equals("container_loc")) {
            return autoQuotes(resource.getContainerLocation());     // ${easyshell:container_loc} == {1}
        } else if (variable.equals("container_name")) {
            return autoQuotes(resource.getContainerName());         // ${easyshell:container_name}
        } else if (variable.equals("container_path")) {
            return autoQuotes(resource.getContainerPath());         // ${easyshell:container_path}
        } else if (variable.equals("resource_loc")) {
            return autoQuotes(resource.getResourceLocation());      // ${easyshell:resource_loc} == {2}
        } else if (variable.equals("resource_name")) {
            return autoQuotes(resource.getResourceName());          // ${easyshell:resource_name} == {3}
        } else if (variable.equals("resource_path")) {
            return autoQuotes(resource.getResourcePath());          // ${easyshell:resource_path}
        } else if (variable.equals("project_loc")) {
            return autoQuotes(resource.getProjectLocation());       // ${easyshell:project_loc_loc}
        } else if (variable.equals("project_name")) {
            return resource.getProjectName();                       // ${easyshell:project_name} == {4}
        } else if (variable.equals("project_path")) {
            return autoQuotes(resource.getProjectPath());           // ${easyshell:project_path}
        }
        return null;
    }

    public static Resource getResource() {
        return resource;
    }

    public static void setResource(Resource resource) {
        DynamicVariableResolver.resource = resource;
    }

    public static void setQuotes(Quotes quotes) {
        DynamicVariableResolver.quotes = quotes;
    }

    private String autoQuotes(String str) {
        String ret = str;
        if (quotes == Quotes.quotesSingle) {
            ret = "'" + str + "'";
        }
        else if (quotes == Quotes.quotesDouble) {
            ret = "\"" + str + "\"";
        }
        else if (quotes == Quotes.quotesEscape) {
            ret = str.replaceAll("\\s", "\\\\ ");
        }
        else if ( ((quotes == Quotes.quotesAuto) || (quotes == Quotes.quotesAutoSingle))
                    && (str.indexOf(" ") != -1) ) { // if space there
            if ((quotes == Quotes.quotesAutoSingle) && str.indexOf("\"") == -1) { // if no single quotes
                ret = "'" + str + "'";
            } else if (str.indexOf("'") == -1){ // if no double quotes
                ret = "\"" + str + "\"";
            }
        }
        return ret;
    }

}
