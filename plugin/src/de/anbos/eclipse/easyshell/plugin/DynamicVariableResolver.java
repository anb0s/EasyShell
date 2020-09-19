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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

import de.anbos.eclipse.easyshell.plugin.types.Converter;
import de.anbos.eclipse.easyshell.plugin.types.Converters;
import de.anbos.eclipse.easyshell.plugin.types.IConverter;
import de.anbos.eclipse.easyshell.plugin.types.IVariableResolver;
import de.anbos.eclipse.easyshell.plugin.types.Quotes;
import de.anbos.eclipse.easyshell.plugin.types.Variable;
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
        String variableArg = argument;
        String variableArgExt = null;
        int converterIndex = argument.indexOf(":");
        if (converterIndex != -1) {
            // first try to find a variable that has same pattern
            // e.g. var:conv = var_conv
            String variableSubs = variableArg.replace(":", "_");
            IVariableResolver variableSubsResolver = Variables.getMap().get(variableSubs);
            if (variableSubsResolver != null) {
                variableArg   = variableSubs;
            } else {
                variableArg    = argument.substring(0, converterIndex);
                variableArgExt = argument.substring(converterIndex + 1);
            }
        }
        // find converter
        IConverter converter = null;
        if (variableArgExt != null) {
            converter = Converters.getMap().get(variableArgExt);
        }
        if (converter != null) {
            variableArgExt = null;
        } else {
            converter = Converter.converterUnknown.getConverterImpl();
        }
        // find resolver
        IVariableResolver resolver = Variables.getMap().get(variableArg);
        if (resolver == null) {
            resolver = Variable.varUnknown.getResolver();
        }
        return autoQuotes(converter.convert(resolver.resolve(resource, variableArgExt)));
    }

    private String handleEclipseVariable(String variable, String argument) {
        return autoQuotes(Variables.getMap().get(argument).resolve(resource, null));
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
