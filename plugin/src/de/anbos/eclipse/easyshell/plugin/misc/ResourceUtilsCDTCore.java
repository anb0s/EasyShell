/**
 * Copyright (c) 2014-2022 Andre Bossert <anb0s@anbos.de>.
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

package de.anbos.eclipse.easyshell.plugin.misc;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.cdt.core.model.IIncludeReference;
import org.eclipse.cdt.internal.core.model.ExternalTranslationUnit;
import org.eclipse.cdt.internal.core.model.IncludeReference;

import de.anbos.eclipse.easyshell.plugin.types.Resource;

@SuppressWarnings("restriction")
public class ResourceUtilsCDTCore {

    static public Resource getResource(IAdaptable adaptable) {
        IPath path = null;
        if (adaptable instanceof IncludeReference) {
            IIncludeReference includeRef = ((IncludeReference) adaptable);
            path = includeRef.getPath();
        } else if (adaptable instanceof ExternalTranslationUnit) {
            path = ((ExternalTranslationUnit) adaptable).getLocation();
        }
        if (path != null) {
            return new Resource(path.toFile());
        }
        return null;
    }

    static public String getFullQualifiedName(IResource resource) {
        // TODO: implement for CDT elements
        return null;
    }

}
