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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

public class ContributionItemDynamic extends ContributionItem {

	public ContributionItemDynamic() {
		// TODO Auto-generated constructor stub
	}

	public ContributionItemDynamic(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	protected IContributionItem[] getContributionItems() {
	    IContributionItem[] list = new IContributionItem[2];
	    Map<String, String> parms = new HashMap<String, String>();
	    parms.put("groupBy", "Severity");
	    CommandContributionItemParameter contributionParameters = null;
		list[0] = new CommandContributionItem(contributionParameters);
		list[1] = new CommandContributionItem(contributionParameters);
	    /*
	    list[0] = new CommandContributionItem(null,
	            "org.eclipse.ui.views.problems.grouping",
	            id, parms, null, null, null, "Severity", null,
	            null, CommandContributionItem.STYLE_PUSH);

	    parms = new HashMap();
	    parms.put("groupBy", "None");
	    list[1] = new CommandContributionItem(null,
	            "org.eclipse.ui.views.problems.grouping",
	            parms, null, null, null, "None", null, null,
	            CommandContributionItem.STYLE_PUSH);
        */
	    return list;
	}
}
