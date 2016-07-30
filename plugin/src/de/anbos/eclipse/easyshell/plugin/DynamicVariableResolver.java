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
        if (argument.equals("drive")) {
            return resource.getWindowsDrive();   // {0} == ${easyshell:drive}
        } else if (argument.equals("line_separator")) {
        	return resource.getLineSeparator();  // {5} == ${easyshell:line_separator}
        }
        // here we have eclipse variables embedded in easyshell variable as parameter
        return handleEclipseVariable(argument, null);
    }

    private String handleEclipseVariable(String variable, String argument) {
        if (variable.equals("container_loc")) {
            return autoQuotes(resource.getParentPath());    // {1} == ${easyshell:container_loc}
        } else if (variable.equals("resource_loc")) {
            return autoQuotes(resource.getFullPath());      // {2} == ${easyshell:resource_loc}
        } else if (variable.equals("resource_name")) {
            return autoQuotes(resource.getFileName());      // {3} == ${easyshell:resource_name}
        } else if (variable.equals("project_name")) {
            return resource.getProjectName();               // {4} == ${easyshell:project_name}
        } else if (variable.equals("qualified_name")) {
            return resource.getFullQualifiedName();         // {6} == ${easyshell:qualified_name} == qualified name
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
