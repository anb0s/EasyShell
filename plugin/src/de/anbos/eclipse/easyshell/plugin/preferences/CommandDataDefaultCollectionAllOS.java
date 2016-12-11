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
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.Variable;

public class CommandDataDefaultCollectionAllOS {

    static public void addCommandsAll(CommandDataList list) {
        // Clipboard - Full Path
        list.add(new CommandData("33043fe3-1a5f-46d7-b94e-9a02ef204e7d", PresetType.presetPlugin, Utils.getOS(), "Full Path", ResourceType.resourceTypeFileOrDirectory, Category.categoryClipboard, CommandType.commandTypeClipboard,
                "${easyshell:resource_loc}${easyshell:line_separator}"));
        // Clipboard - Full Path with quotes
        list.add(new CommandData("67aa9dff-6bbb-4b47-8b43-8a82a7a279fa", PresetType.presetPlugin, Utils.getOS(), "\"Full Path\"", ResourceType.resourceTypeFileOrDirectory, Category.categoryClipboard, CommandType.commandTypeClipboard,
                "\"${easyshell:resource_loc}\"${easyshell:line_separator}"));
        // Clipboard - Full Path Unix
        list.add(new CommandData("95cacf98-9dfc-473f-b5d3-fe4961e66ae1", PresetType.presetPlugin, Utils.getOS(), "Full Path Unix", ResourceType.resourceTypeFileOrDirectory, Category.categoryClipboard, CommandType.commandTypeClipboard,
                "${easyshell:resource_loc:unix}${easyshell:line_separator}"));
        // Clipboard - Qualified Name
        list.add(new CommandData("88989d78-cf17-4750-91fc-6260055743ae", PresetType.presetPlugin, Utils.getOS(), "Qualified Name", ResourceType.resourceTypeFileOrDirectory, Category.categoryClipboard, CommandType.commandTypeClipboard,
                "${easyshell:qualified_name}${easyshell:line_separator}"));
        // Clipboard - Qualified Name with quotes
        list.add(new CommandData("cd32fa5a-34d7-4551-8bd0-3aae0dc444d0", PresetType.presetPlugin, Utils.getOS(), "\"Qualified Name\"", ResourceType.resourceTypeFileOrDirectory, Category.categoryClipboard, CommandType.commandTypeClipboard,
                "\"${easyshell:qualified_name}\"${easyshell:line_separator}"));
        // Clipboard - Variables Test
        String varTestString = "";
        for(int i=Variable.getFirstIndex();i<Variable.values().length;i++) {
            varTestString += "easyshell:" + Variable.values()[i].getName() + "=" + Variable.values()[i].getFullVariableName() + "${easyshell:line_separator}";
        }
        list.add(new CommandData("e6de32cc-342a-46a0-a766-ac74e7e4000d", PresetType.presetPlugin, Utils.getOS(), "Variables Test", ResourceType.resourceTypeFileOrDirectory, Category.categoryClipboard, CommandType.commandTypeClipboard,
                varTestString));
    }

    static public void addCommandsDefault(CommandDataList list) {
    	CommandDataList allList = new CommandDataList();
    	addCommandsAll(allList);
        // add clipboard
    	Utils.addNotNull(list, Utils.getCommandData(allList, ".*Path", Category.categoryClipboard));
    	Utils.addNotNull(list, Utils.getCommandData(allList, ".*Name", Category.categoryClipboard));
    }

}
