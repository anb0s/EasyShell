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

import java.io.File;

public class Resource {

    private File file = null;
    private String projectName = null;

    public Resource(Resource myRes) {
        file = myRes.getFile();
        projectName = myRes.getProjectName();
    }

    public Resource(File myFile, String myProjectName) {
        file = myFile;
        projectName = myProjectName;
    }

    public File getFile() {
        return file;
    }

    public String getProjectName() {
        return projectName;
    }

}
