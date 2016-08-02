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
import de.anbos.eclipse.easyshell.plugin.types.Variables;

public class DynamicVariableResolver implements IDynamicVariableResolver {

    static private Resource resource = null;
    static Quotes quotes = Quotes.quotesNo;

	@Override
	public String resolveValue(IDynamicVariable variable, String argument)
			throws CoreException {
	    String variableName = variable.getName();
		if (variableName.equals("easyshell")) {
		    return handleOwnVariable(argument);
		} else {
            return handleEclipseVariable(variableName, argument);
        }
	}

    private String handleOwnVariable(String argument) {
        return autoQuotes(Variables.getMap().get(argument).resolve(resource));
    }

    private String handleEclipseVariable(String variable, String argument) {
        return autoQuotes(Variables.getMap().get(argument).resolve(resource));
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
