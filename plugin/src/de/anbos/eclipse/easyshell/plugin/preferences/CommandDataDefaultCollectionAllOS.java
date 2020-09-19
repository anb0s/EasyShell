/**
 * Copyright (c) 2014-2020 Andre Bossert <anb0s@anbos.de>.
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

package de.anbos.eclipse.easyshell.plugin.preferences;

import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.CommandTokenizer;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.OS;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.Variable;

public class CommandDataDefaultCollectionAllOS {

    static public void addCommandsAll(CommandDataList list) {
        addCommandsConsole(list);
        addCommandsFileBrowser(list);
        addCommandsClipboard(list);
    }

    static public void addCommandsDefault(CommandDataList list) {
        CommandDataList allList = new CommandDataList();
        addCommandsAll(allList);
        // add clipboard commands
        Utils.addNotNull(list, Utils.getCommandData(allList, ".*Path", Category.categoryClipboard));
        Utils.addNotNull(list, Utils.getCommandData(allList, ".*Name", Category.categoryClipboard));
    }

    private static void addCommandsConsole(CommandDataList list) {
    }

    private static void addCommandsFileBrowser(CommandDataList list) {
        OS os = Utils.getOS();
        String cmdPrefix = "";
        if (os == OS.osWindows) {
            cmdPrefix = "cmd.exe /C ";
        }
        // DoubleCommander: http://doublecmd.sourceforge.net
        // https://doublecmd.sourceforge.io/mediawiki/index.php/Changes_in_version_0.5.5
        list.add(new CommandData("1cb91d59-02b7-4245-b783-fd963d597c47", PresetType.presetPlugin, os, "DoubleCommander (left new)", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryExplore, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                cmdPrefix + "doublecmd -T -P L -L ${easyshell:container_loc}"));
        list.add(new CommandData("b3f7360a-6a37-4eb9-9b70-9387caffe969", PresetType.presetPlugin, os, "DoubleCommander (left replace)", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryExplore, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                cmdPrefix + "doublecmd -P L -L ${easyshell:container_loc}"));
        list.add(new CommandData("53455bec-58b3-4d21-ab6f-bdfb4fb1a950", PresetType.presetPlugin, os, "DoubleCommander (right new)", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryExplore, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                cmdPrefix + "doublecmd -T -P R -R ${easyshell:container_loc}"));
        list.add(new CommandData("4d7c100b-3918-4fc9-9f39-9c3d188abd0b", PresetType.presetPlugin, os, "DoubleCommander (right replace)", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryExplore, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                cmdPrefix + "doublecmd -P R -R ${easyshell:container_loc}"));
        // Midnight Commander: http://midnight-commander.org
        list.add(new CommandData("5ca27361-092a-4955-b8b4-0db7b6d6f1ba", PresetType.presetPlugin, os, "Midnight Commander", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryExplore, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                cmdPrefix + "mc ${easyshell:container_loc}"));
    }

    private static void addCommandsClipboard(CommandDataList list) {
        OS os = Utils.getOS();
        // Clipboard - Full Path
        list.add(new CommandData("33043fe3-1a5f-46d7-b94e-9a02ef204e7d", PresetType.presetPlugin, os, "Full Path", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerDisabled,
                "${easyshell:resource_loc}${easyshell:line_separator}"));
        // Clipboard - Full Path with quotes
        list.add(new CommandData("67aa9dff-6bbb-4b47-8b43-8a82a7a279fa", PresetType.presetPlugin, os, "\"Full Path\"", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerDisabled,
                "\"${easyshell:resource_loc}\"${easyshell:line_separator}"));
        // Clipboard - Full Path Unix
        list.add(new CommandData("95cacf98-9dfc-473f-b5d3-fe4961e66ae1", PresetType.presetPlugin, os, "Full Path Unix", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerDisabled,
                "${easyshell:resource_loc:unix}${easyshell:line_separator}"));
        // Clipboard - Qualified Name
        list.add(new CommandData("88989d78-cf17-4750-91fc-6260055743ae", PresetType.presetPlugin, os, "Qualified Name", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerDisabled,
                "${easyshell:qualified_name}${easyshell:line_separator}"));
        // Clipboard - Qualified Name with quotes
        list.add(new CommandData("cd32fa5a-34d7-4551-8bd0-3aae0dc444d0", PresetType.presetPlugin, os, "\"Qualified Name\"", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerDisabled,
                "\"${easyshell:qualified_name}\"${easyshell:line_separator}"));
        // Clipboard - Variables Test
        String varTestString = "";
        for(int i=Variable.getFirstIndex();i<Variable.values().length;i++) {
            varTestString += "easyshell:" + Variable.values()[i].getName() + "=" + Variable.values()[i].getFullVariableName() + "${easyshell:line_separator}";
        }
        list.add(new CommandData("e6de32cc-342a-46a0-a766-ac74e7e4000d", PresetType.presetPlugin, os, "Variables Test", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerDisabled,
                varTestString));
    }

}
