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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.anbos.eclipse.easyshell.plugin.misc.Utils;

public class MainPage extends PreferencePage implements IWorkbenchPreferencePage {

    private IWorkbench workbench;

    public MainPage() {
    }

    public MainPage(String title) {
        super(title);
    }

    public MainPage(String title, ImageDescriptor image) {
        super(title, image);
    }

    @Override
    public void init(IWorkbench workbench) {
        this.workbench = workbench;
    }

    @Override
    protected Control createContents(Composite parent) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("preferencePageId","de.anbos.eclipse.easyshell.plugin.preferences.MenuPage");
        Utils.executeCommand(workbench, "org.eclipse.ui.window.preferences", params, true);
        return null;
    }

}
