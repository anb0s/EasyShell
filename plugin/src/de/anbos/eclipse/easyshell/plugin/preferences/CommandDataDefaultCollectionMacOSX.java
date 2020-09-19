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

public class CommandDataDefaultCollectionMacOSX {

    static public void addCommandsAll(CommandDataList list) {
        addCommandsConsole(list);
        addCommandsFileBrowser(list);
        addCommandsClipboard(list);
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
        // https://developer.apple.com/legacy/library/documentation/Darwin/Reference/ManPages/man1/open.1.html
        // http://stackoverflow.com/questions/32675804/how-do-i-execute-a-command-in-an-iterm-window-from-the-command-line
        list.add(new CommandData("db61d61e-8bf4-41d0-a1d8-00379e4d1db1", PresetType.presetPlugin, OS.osMacOSX, "Terminal", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "open -a Terminal ${easyshell:container_loc} --args \"./${easyshell:resource_loc}; exit\""));
        list.add(new CommandData("ee2db486-4df3-42c1-b19e-03761ffac105", PresetType.presetPlugin, OS.osMacOSX, "Terminal (hold)", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "open -a Terminal ${easyshell:container_loc} --args \"./${easyshell:resource_loc}\""));
        // MAC OS X iTerm
        list.add(new CommandData("40eee8f2-b29e-490b-8612-59c2b76a704d", PresetType.presetPlugin, OS.osMacOSX, "iTerm", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "open -a iTerm ${easyshell:container_loc}"));
        list.add(new CommandData("e7d89190-95b3-4f03-bb79-c742597d1f6d", PresetType.presetPlugin, OS.osMacOSX, "iTerm", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "open -a iTerm ${easyshell:container_loc} --args \"./${easyshell:resource_loc}; exit\""));
        list.add(new CommandData("427b93dd-18b1-4ff1-bbea-daed164b7b93", PresetType.presetPlugin, OS.osMacOSX, "iTerm (hold)", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "open -a iTerm ${easyshell:container_loc} --args \"./${easyshell:resource_loc}\""));
    }

    private static void addCommandsFileBrowser(CommandDataList list) {
        // MAC OS X Finder
        list.add(new CommandData("f6bcdd71-4687-46d8-bf34-2780bafd762a", PresetType.presetPlugin, OS.osMacOSX, "Finder", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "open -R ${easyshell:resource_loc}"));
    }

    private static void addCommandsClipboard(CommandDataList list) {
    }

}
