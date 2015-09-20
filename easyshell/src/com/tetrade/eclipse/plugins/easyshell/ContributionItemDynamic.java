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
