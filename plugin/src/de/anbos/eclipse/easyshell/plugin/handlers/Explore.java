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

package de.anbos.eclipse.easyshell.plugin.handlers;

import de.anbos.eclipse.easyshell.plugin.types.Category;

public class Explore extends AllSingle {

    @Override
    public Category getCategory() {
        return Category.categoryExplore;
    }

}
