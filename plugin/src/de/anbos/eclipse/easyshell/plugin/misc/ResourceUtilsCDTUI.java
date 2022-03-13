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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.cdt.core.model.IIncludeReference;
import org.eclipse.cdt.internal.ui.cview.IncludeReferenceProxy;
import org.eclipse.cdt.internal.ui.includebrowser.IBNode;

import de.anbos.eclipse.easyshell.plugin.types.Resource;

@SuppressWarnings("restriction")
public class ResourceUtilsCDTUI {

    static public Resource getResource(IAdaptable adaptable) {
        IPath path = null;
        if (adaptable instanceof IncludeReferenceProxy) {
            IIncludeReference includeRef = ((IncludeReferenceProxy) adaptable).getReference();
            path = includeRef.getPath();
        } else if (adaptable instanceof IBNode) {
            path = ((IBNode) adaptable).getRepresentedPath();
        }
        if (path != null) {
            return new Resource(path.toFile());
        }
        return null;
    }

}
