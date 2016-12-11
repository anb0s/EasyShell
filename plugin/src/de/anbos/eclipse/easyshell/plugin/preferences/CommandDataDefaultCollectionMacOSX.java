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

import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.OS;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

public class CommandDataDefaultCollectionMacOSX {

    static public void addCommandsAll(CommandDataList list) {
    	addCommandsConsole(list);
    	addCommandsFileBrowser(list);
    }

    static public void addCommandsDefault(CommandDataList list) {
    	CommandDataList allList = new CommandDataList();
    	addCommandsAll(allList);
    	Utils.addNotNull(list, Utils.getCommandData(allList, "Terminal", Category.categoryOpen));
    	Utils.addNotNull(list, Utils.getCommandData(allList, "Terminal", Category.categoryRun));
    	Utils.addNotNull(list, Utils.getCommandData(allList, "Finder", Category.categoryExplore));
    }

    private static void addCommandsConsole(CommandDataList list) {
        // MAC OS X Open (default application)
        list.add(new CommandData("61c4fef4-470a-45b1-98df-ccf9d7d91143", PresetType.presetPlugin, OS.osMacOSX, "Open", ResourceType.resourceTypeFileOrDirectory, Category.categoryDefault, CommandType.commandTypeExecute,
                "open ${easyshell:resource_loc}"));
        // MAC OS X Terminal
        list.add(new CommandData("e6918fe0-38b8-450b-a4be-d9eecc0dc0b4", PresetType.presetPlugin, OS.osMacOSX, "Terminal", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "open -a Terminal ${easyshell:container_loc}"));
        // MAC OS X iTerm
        list.add(new CommandData("40eee8f2-b29e-490b-8612-59c2b76a704d", PresetType.presetPlugin, OS.osMacOSX, "iTerm", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "open -a iTerm ${easyshell:container_loc}"));
        //list.add(new CommandData("db61d61e-8bf4-41d0-a1d8-00379e4d1db1", PresetType.presetPlugin, OS.osMacOSX, "Terminal", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
        //        "open -a Terminal ${easyshell:container_loc}"));    	
    }
    
    private static void addCommandsFileBrowser(CommandDataList list) {
        // MAC OS X Finder
        list.add(new CommandData("f6bcdd71-4687-46d8-bf34-2780bafd762a", PresetType.presetPlugin, OS.osMacOSX, "Finder", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "open -R ${easyshell:resource_loc}"));    	
    }

}
