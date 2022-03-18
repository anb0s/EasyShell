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
        addCommandsEditor(list);
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
      // Windows Pwsh - PowerShell 7
      list.add(new CommandData("6245f20a-cfd0-457b-a101-a29eeefd22b5", PresetType.presetPlugin, OS.osWindows, "Pwsh - PowerShell 7", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
              "pwsh -command \"Start-Process -WorkingDirectory '${easyshell:container_loc}' pwsh\""));
      list.add(new CommandData("9fbc5abb-0b82-4fbb-9907-99d6709d0126", PresetType.presetPlugin, OS.osWindows, "Pwsh - PowerShell 7", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
              "pwsh -command \"Start-Process pwsh -ArgumentList '-Command', 'cd ${easyshell:container_loc} ; ./${easyshell:resource_name}'\""));
      list.add(new CommandData("5543176e-e29c-4a7a-b388-f393531d78dc", PresetType.presetPlugin, OS.osWindows, "Pwsh - PowerShell 7 (hold)", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
              "pwsh -command \"Start-Process pwsh -ArgumentList '-NoExit', '-Command', 'cd ${easyshell:container_loc} ; ./${easyshell:resource_name}'\""));
      list.add(new CommandData("d84633fa-57cb-4965-b92e-2b164d47a6ed", PresetType.presetPlugin, OS.osWindows, "Pwsh - PowerShell 7 as Admin", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
              "pwsh -command \"Start-Process pwsh -Verb RunAs -ArgumentList '-NoExit', '-Command', 'cd ${easyshell:container_loc}'\""));
      list.add(new CommandData("1e47f558-7e01-4c69-9ffb-927d67fcbff5", PresetType.presetPlugin, OS.osWindows, "Pwsh - PowerShell 7 as Admin", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
              "pwsh -command \"Start-Process pwsh -Verb RunAs -ArgumentList '-Command', 'cd ${easyshell:container_loc} ; ./${easyshell:resource_name}'\""));
      list.add(new CommandData("d26d52ce-46b1-49bc-a84b-f5e5c6d7a705", PresetType.presetPlugin, OS.osWindows, "Pwsh - PowerShell 7 as Admin (hold)", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
              "pwsh -command \"Start-Process pwsh -Verb RunAs -ArgumentList '-NoExit', '-Command', 'cd ${easyshell:container_loc} ; ./${easyshell:resource_name}'\""));
    }

    private static void addCommandsEditor(CommandDataList list) {
      OS os = Utils.getOS();
      // Eclipse Editor
      list.add(new CommandData("dc8dd567-6ac0-49ec-8fcd-ed61e677ea3d", PresetType.presetPlugin, os, "Eclipse", ResourceType.resourceTypeFile, false, null, Category.categoryEdit, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
              "eclipse -name Eclipse --launcher.openFile ${easyshell:resource_loc}:${easyshell:resource_line_number}"));
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
        // Clipboard - Full Path with line number = selected text start line
        list.add(new CommandData("434dfc65-4efd-42e1-be5a-f108661e51a1", PresetType.presetPlugin, os, "Full Path : line number", ResourceType.resourceTypeFile, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
            "${easyshell:resource_loc}:${easyshell:resource_line_number}"));
        // Clipboard - Full Path with selected text end line
        list.add(new CommandData("840831f3-fb2f-45f3-8db4-14007757d576", PresetType.presetPlugin, os, "Full Path : selected text end line", ResourceType.resourceTypeFile, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
            "${easyshell:resource_loc}:${easyshell:selected_text_end_line}"));
        // Clipboard - Full Path with with selected text start and end line
        list.add(new CommandData("65f40ff6-e920-4bd4-96d9-33e0a6fb8725", PresetType.presetPlugin, os, "Full Path : selected text start line : end line", ResourceType.resourceTypeFile, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
            "${easyshell:resource_loc}:${easyshell:selected_text_start_line}:${easyshell:selected_text_end_line}"));
        // Clipboard - selected text length
        list.add(new CommandData("74e122dc-64fa-4444-8f58-f4ab04ebc9e3", PresetType.presetPlugin, os, "Selected text length", ResourceType.resourceTypeFile, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
            "${easyshell:selected_text_length}"));
        // Clipboard - selected text offset
        list.add(new CommandData("ab5cb337-60da-429c-a0a7-0b37071b6a50", PresetType.presetPlugin, os, "Selected text offset", ResourceType.resourceTypeFile, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
            "${easyshell:selected_text_offset}"));
        // Clipboard - selected text
        list.add(new CommandData("93442258-28b7-4297-9611-34733fa76161", PresetType.presetPlugin, os, "Selected text", ResourceType.resourceTypeFile, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
            "${easyshell:selected_text}"));

        // Clipboard - Variables Test
        String varTestString = "";
        for(int i=Variable.getFirstIndex();i<Variable.values().length;i++) {
            varTestString += "easyshell:" + Variable.values()[i].getName() + "=" + Variable.values()[i].getFullVariableName() + "${easyshell:line_separator}";
        }
        list.add(new CommandData("e6de32cc-342a-46a0-a766-ac74e7e4000d", PresetType.presetPlugin, os, "Variables Test", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerDisabled,
                varTestString));
    }

}
