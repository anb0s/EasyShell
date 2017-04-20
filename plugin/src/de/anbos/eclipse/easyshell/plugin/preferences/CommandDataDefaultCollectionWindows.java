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
import de.anbos.eclipse.easyshell.plugin.types.CommandTokenizer;

public class CommandDataDefaultCollectionWindows {

    static public void addCommandsAll(CommandDataList list) {
        addCommandsConsole(list);
        addCommandsFileBrowser(list);
    }

    static public void addCommandsDefault(CommandDataList list) {
        CommandDataList allList = new CommandDataList();
        addCommandsAll(allList);
        Utils.addNotNull(list, Utils.getCommandData(allList, ".*Prompt.*", Category.categoryOpen));
        Utils.addNotNull(list, Utils.getCommandData(allList, ".*Prompt.*", Category.categoryRun));
        Utils.addNotNull(list, Utils.getCommandData(allList, "Explorer", Category.categoryExplore));
    }

    private static void addCommandsConsole(CommandDataList list) {
        // Windows DOS-Shell
        list.add(new CommandData("cd361a40-bb37-4fa2-9d2e-b62794294a59", PresetType.presetPlugin, OS.osWindows, "Command Prompt", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} cmd.exe /K"));
        list.add(new CommandData("f740984b-e9d5-4ebc-b9b5-c6b8527f886c", PresetType.presetPlugin, OS.osWindows, "Command Prompt", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} ${easyshell:resource_name}"));
        list.add(new CommandData("213e0ae1-d548-4362-bb97-9b27e615d939", PresetType.presetPlugin, OS.osWindows, "Command Prompt (hold)", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} cmd.exe /K ${easyshell:resource_name}"));
        // Windows PowerShell
        list.add(new CommandData("9cb0d29e-93aa-4139-93d2-079ba63ff726", PresetType.presetPlugin, OS.osWindows, "PowerShell", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe"));
        list.add(new CommandData("af2968d0-3a1a-40db-b3cb-e2e16293a285", PresetType.presetPlugin, OS.osWindows, "PowerShell", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe -command ./\"${easyshell:resource_name}\""));
        list.add(new CommandData("939ff127-1b7d-4528-8d40-bd9607f5b3d2", PresetType.presetPlugin, OS.osWindows, "PowerShell (hold)", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe -noexit -command ./\"${easyshell:resource_name}\""));
        // Windows Cygwin (Bash)
        list.add(new CommandData("5b1e3806-a9ab-4866-b660-823ac388a575", PresetType.presetPlugin, OS.osWindows, "Cygwin Bash", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\""));
        list.add(new CommandData("2002e587-70a3-4204-b1a5-6faf6271ad08", PresetType.presetPlugin, OS.osWindows, "Cygwin Bash", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\" -c ./\"'${easyshell:resource_name}\""));
        // Windows Git-Bash
        list.add(new CommandData("24419204-c8e5-4d79-a7b8-b14e93077cf0", PresetType.presetPlugin, OS.osWindows, "Git Bash v1.x", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files\\Git\\bin\\bash.exe\" --login -i"));
        list.add(new CommandData("ee790c7f-9c6d-40f9-84f6-51a948a59d45", PresetType.presetPlugin, OS.osWindows, "Git Bash v1.x", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files\\Git\\bin\\bash.exe\" --login -i -c ./\"${easyshell:resource_name}\""));
        list.add(new CommandData("d2726c3f-6da3-46b5-8029-1c63d0ff6bd2", PresetType.presetPlugin, OS.osWindows, "Git Bash v2.x", ResourceType.resourceTypeFileOrDirectory, true, "${easyshell:container_loc}", Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "\"C:\\Program Files\\Git\\git-bash.exe\""));
        list.add(new CommandData("03e6678b-f67f-42ed-b65f-6b6f06ec0e8f", PresetType.presetPlugin, OS.osWindows, "Git Bash v2.x", ResourceType.resourceTypeFile, true, "${easyshell:container_loc}", Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "\"C:\\Program Files\\Git\\git-bash.exe\" -c ./\"${easyshell:resource_name}\""));
        // Windows Console
        // Console2: https://sourceforge.net/projects/console/
        // ConsoleZ: https://github.com/cbucher/console
        list.add(new CommandData("60fd43d2-d837-41d1-aaa3-3f5cab6bf0fb", PresetType.presetPlugin, OS.osWindows, "Console2/Z", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc}"));
        list.add(new CommandData("af6d97f2-f0a8-46e2-8234-74c0ee3e6007", PresetType.presetPlugin, OS.osWindows, "Console2/Z", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc} -r \"/k\\\"${easyshell:resource_name}\\\"\""));
        // Windows ConEmu
        list.add(new CommandData("1bd62e22-cd93-4136-b643-1cbb9579c195", PresetType.presetPlugin, OS.osWindows, "ConEmu", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd cmd"));
        list.add(new CommandData("c2b73077-ffd9-4fb7-9793-189be9f13ebb", PresetType.presetPlugin, OS.osWindows, "ConEmu", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd \"${easyshell:resource_name}\""));
        // Windows Cmder
        list.add(new CommandData("c57a5d9f-491a-4b21-8a8b-9941b01cc049", PresetType.presetPlugin, OS.osWindows, "Cmder", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmder.exe /START \"${easyshell:container_loc}\""));
        // PowerCmd
        list.add(new CommandData("771c0bac-cdb2-47fe-a030-6b830d366da1", PresetType.presetPlugin, OS.osWindows, "PowerCmd", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C start \"${easyshell:project_name}\" PowerCmd.exe /P ${easyshell:container_loc}"));
    }

    private static void addCommandsFileBrowser(CommandDataList list) {
        // Windows Explorer
        list.add(new CommandData("ae0e5b6e-0b20-4c52-9708-ba7e317d1dee", PresetType.presetPlugin, OS.osWindows, "Explorer", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryExplore, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "explorer.exe /select, ${easyshell:resource_loc}"));
        // Windows TotalCommander
        list.add(new CommandData("e487327c-dfdb-42e7-bf16-3b81a34e5703", PresetType.presetPlugin, OS.osWindows, "TotalCommander", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryExplore, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C totalcmd64.exe /O /T ${easyshell:container_loc}"));
        list.add(new CommandData("d13ad020-aeb8-4b48-acc6-4e09dea4913a", PresetType.presetPlugin, OS.osWindows, "TotalCommander (left)", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryExplore, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C totalcmd64.exe /O /L=${easyshell:container_loc}"));
        list.add(new CommandData("a6728fb1-89f3-4c3a-8ab0-5c57b6d7ee48", PresetType.presetPlugin, OS.osWindows, "TotalCommander (right)", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryExplore, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotesSkip,
                "cmd.exe /C totalcmd64.exe /O /R=${easyshell:container_loc}"));
    }

}
