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

package de.anbos.eclipse.easyshell.plugin.preferences;

import java.util.Map;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

public class CommandVariableContentProposalProvider implements IContentProposalProvider {

    private ContentProposal[] contentProposals;

    public CommandVariableContentProposalProvider(Map<String, String> proposals) {
        setProposals(proposals);
    }

    @Override
    public IContentProposal[] getProposals(String contents, int position) {
        return contentProposals;
    }

    private void setProposals(Map<String, String> proposals) {
        contentProposals = new ContentProposal[proposals.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : proposals.entrySet())
        {
          ContentProposal proposal = new ContentProposal(entry.getKey(), entry.getValue());
          contentProposals[i++] = proposal;
        }
    }

}
