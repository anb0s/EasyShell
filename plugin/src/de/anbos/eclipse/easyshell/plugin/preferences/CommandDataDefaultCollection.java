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

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.LinuxDesktop;
import de.anbos.eclipse.easyshell.plugin.types.OS;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.Variable;

public class CommandDataDefaultCollection {

    private CommandDataList list = new CommandDataList();
    private static CommandDataDefaultCollection instance = new CommandDataDefaultCollection();

    public static CommandDataList getAllCommandsStatic() {
        return instance.getCommands();
    }

    public static MenuDataList getCommandsNativeAsMenu(boolean sorted) {
        CommandDataList list = getDefaultCommands();
        MenuDataList ret = new MenuDataList();
        for (int i=0;i<list.size();i++) {
            CommandData cmdData = list.get(i);
            // use the same id like the default command to have same defaults
            MenuData newData = new MenuData(cmdData.getId(), cmdData.getId());
            newData.setNameTypeFromCategory(cmdData.getCategory());
            if (sorted) {
                newData.setPosition(i);
            }
            ret.add(newData);
        }
        return ret;
    }

    CommandDataDefaultCollection() {
        addWindowsCommands();
        addLinuxCommands();
        addMacOSXCommands();
        addAllOSCommands();
    }


    private void addWindowsCommands() {
        // Windows DOS-Shell
        list.add(new CommandData("cd361a40-bb37-4fa2-9d2e-b62794294a59", PresetType.presetPlugin, OS.osWindows, "Command Prompt", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} cmd.exe /K"));
        list.add(new CommandData("f740984b-e9d5-4ebc-b9b5-c6b8527f886c", PresetType.presetPlugin, OS.osWindows, "Command Prompt", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} ${easyshell:resource_name}"));
        // Windows Explorer
        list.add(new CommandData("ae0e5b6e-0b20-4c52-9708-ba7e317d1dee", PresetType.presetPlugin, OS.osWindows, "Explorer", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "explorer.exe /select, ${easyshell:resource_loc}"));
        // Windows PowerShell
        list.add(new CommandData("9cb0d29e-93aa-4139-93d2-079ba63ff726", PresetType.presetPlugin, OS.osWindows, "PowerShell", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe"));
        list.add(new CommandData("af2968d0-3a1a-40db-b3cb-e2e16293a285", PresetType.presetPlugin, OS.osWindows, "PowerShell", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe -command ./''${easyshell:resource_name}''"));
        // Windows Cygwin (Bash)
        list.add(new CommandData("5b1e3806-a9ab-4866-b660-823ac388a575", PresetType.presetPlugin, OS.osWindows, "Cygwin Bash", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\""));
        list.add(new CommandData("2002e587-70a3-4204-b1a5-6faf6271ad08", PresetType.presetPlugin, OS.osWindows, "Cygwin Bash", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\" -c ./''${easyshell:resource_name}''"));
        // Windows Console
        list.add(new CommandData("60fd43d2-d837-41d1-aaa3-3f5cab6bf0fb", PresetType.presetPlugin, OS.osWindows, "Console", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc}"));
        list.add(new CommandData("af6d97f2-f0a8-46e2-8234-74c0ee3e6007", PresetType.presetPlugin, OS.osWindows, "Console", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc} -r \"/k\\\"${easyshell:resource_name}\\\"\""));
        // Windows Git-Bash
        list.add(new CommandData("24419204-c8e5-4d79-a7b8-b14e93077cf0", PresetType.presetPlugin, OS.osWindows, "Git Bash v1.x", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files\\Git\\bin\\bash.exe\" --login -i"));
        list.add(new CommandData("ee790c7f-9c6d-40f9-84f6-51a948a59d45", PresetType.presetPlugin, OS.osWindows, "Git Bash v1.x", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files\\Git\\bin\\bash.exe\" --login -i -c ./''${easyshell:resource_name}''"));
        list.add(new CommandData("d2726c3f-6da3-46b5-8029-1c63d0ff6bd2", PresetType.presetPlugin, OS.osWindows, "Git Bash v2.x", ResourceType.resourceTypeFileOrDirectory, true, "${easyshell:container_loc}", Category.categoryOpen, CommandType.commandTypeExecute,
                "C:\\Program Files\\Git\\git-bash.exe"));
        list.add(new CommandData("03e6678b-f67f-42ed-b65f-6b6f06ec0e8f", PresetType.presetPlugin, OS.osWindows, "Git Bash v2.x", ResourceType.resourceTypeFile, true, "${easyshell:container_loc}", Category.categoryRun, CommandType.commandTypeExecute,
                "C:\\Program Files\\Git\\git-bash.exe -c ./''${easyshell:resource_name}''"));
        // Windows ConEmu
        list.add(new CommandData("1bd62e22-cd93-4136-b643-1cbb9579c195", PresetType.presetPlugin, OS.osWindows, "ConEmu", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd cmd"));
        list.add(new CommandData("c2b73077-ffd9-4fb7-9793-189be9f13ebb", PresetType.presetPlugin, OS.osWindows, "ConEmu", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd \"${easyshell:resource_name}\""));
        list.add(new CommandData("c57a5d9f-491a-4b21-8a8b-9941b01cc049", PresetType.presetPlugin, OS.osWindows, "Cmder", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "cmder.exe /START \"${easyshell:container_loc}\""));
        // Windows TotalCommander
        list.add(new CommandData("e487327c-dfdb-42e7-bf16-3b81a34e5703", PresetType.presetPlugin, OS.osWindows, "TotalCommander", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "cmd.exe /C totalcmd64.exe /O /T ${easyshell:container_loc}"));
        // PowerCmd
        list.add(new CommandData("771c0bac-cdb2-47fe-a030-6b830d366da1", PresetType.presetPlugin, OS.osWindows, "PowerCmd", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "cmd.exe /C start \"${easyshell:project_name}\" PowerCmd.exe /P ${easyshell:container_loc}"));
    }

    private void addLinuxCommands() {
        // Linux XDG Open (default application): https://linux.die.net/man/1/xdg-open 
        list.add(new CommandData("51ed300a-35d0-4e67-a5f8-6ebd7012a564", PresetType.presetPlugin, OS.osLinux, "XDG Open", ResourceType.resourceTypeFileOrDirectory, Category.categoryDefault, CommandType.commandTypeExecute,
                "xdg-open ${easyshell:resource_loc}"));
        // Linux KDE Konsole
        list.add(new CommandData("c2b1612b-9037-484a-a763-d013679bdbe7", PresetType.presetPlugin, OS.osLinux, "KDE Konsole", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "konsole --workdir ${easyshell:container_loc}"));
        list.add(new CommandData("e634b20a-cf57-47f0-aa27-b8fc95917f35", PresetType.presetPlugin, OS.osLinux, "KDE Konsole", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "konsole --workdir ${easyshell:container_loc} --noclose  -e ${easyshell:resource_loc}"));
        // Linux Konqueror
        list.add(new CommandData("8873342e-e02b-4feb-8f56-9f52524c0f46", PresetType.presetPlugin, OS.osLinux, "Konqueror", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "konqueror file:\"${easyshell:resource_loc}\""));
        // Linux Gnome Terminal
        list.add(new CommandData("53f7b6a3-f8d5-4682-b0ef-1d6ceec5cc8a", PresetType.presetPlugin, OS.osLinux, "Gnome Terminal", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "gnome-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("c6126958-32f2-4f96-9933-69ddd956f2e9", PresetType.presetPlugin, OS.osLinux, "Gnome Terminal", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "gnome-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}''"));
        // Linux Xfce Terminal: http://docs.xfce.org/apps/terminal/command-line
        list.add(new CommandData("8175f9a7-4e54-4367-a6b6-251aedc187df", PresetType.presetPlugin, OS.osLinux, "Xfce Terminal", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "xfce4-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("adf40e10-0ee9-4abe-8282-aff7d51bb68d", PresetType.presetPlugin, OS.osLinux, "Xfce Terminal", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "xfce4-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}'' --hold"));
        // Linux MATE Terminal
        list.add(new CommandData("9a11b3eb-497c-44dd-9813-a841a32465c1", PresetType.presetPlugin, OS.osLinux, "MATE Terminal", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "mate-terminal --title=${easyshell:project_name} --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("d07df65d-ccc5-4c71-82f8-3a17e608516b", PresetType.presetPlugin, OS.osLinux, "MATE Terminal", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "mate-terminal --title=${easyshell:project_name} --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}''"));
        // Linux LX Terminal (LXDE): http://manpages.ubuntu.com/manpages/precise/en/man1/lxterminal.1.html
        list.add(new CommandData("8770e3c5-cd60-4c01-b1c0-8b7754b91b27", PresetType.presetPlugin, OS.osLinux, "LX Terminal", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "lxterminal --title=${easyshell:project_name} --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("94b0c584-0316-4dac-bd40-2ab1a0428d32", PresetType.presetPlugin, OS.osLinux, "LX Terminal", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "lxterminal --title=${easyshell:project_name} --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}''"));
        // Linux Sakura Terminal
        list.add(new CommandData("8e366a34-5ce2-4430-bc21-20e176e0128c", PresetType.presetPlugin, OS.osLinux, "Sakura Terminal", ResourceType.resourceTypeFileOrDirectory, true, "${easyshell:container_loc}", Category.categoryOpen, CommandType.commandTypeExecute,
                "sakura"));
        list.add(new CommandData("2a979af5-86a7-440e-b4f0-8442e85412e4", PresetType.presetPlugin, OS.osLinux, "Sakura Terminal", ResourceType.resourceTypeFile, true, "${easyshell:container_loc}", Category.categoryRun, CommandType.commandTypeExecute,
                "sakura --execute=./''${easyshell:resource_name}'' --hold"));
        // Linux ROXTerm
        list.add(new CommandData("f573a4de-22fa-467f-a433-042d0992ab28", PresetType.presetPlugin, OS.osLinux, "ROXTerm", ResourceType.resourceTypeFileOrDirectory, true, "${easyshell:container_loc}", Category.categoryOpen, CommandType.commandTypeExecute,
                "roxterm --title=${easyshell:project_name} --directory=${easyshell:container_loc}"));
        list.add(new CommandData("1cc39e61-8d8d-4493-8baa-7a11ff01c06c", PresetType.presetPlugin, OS.osLinux, "ROXTerm", ResourceType.resourceTypeFile, true, "${easyshell:container_loc}", Category.categoryRun, CommandType.commandTypeExecute,
                "roxterm --title=${easyshell:project_name} --directory=${easyshell:container_loc} --execute ./''${easyshell:resource_name}''"));
        // Linux Pantheon Terminal
        list.add(new CommandData("e5b3b0f6-e27c-4a2d-aa1a-caef784dd3da", PresetType.presetPlugin, OS.osLinux, "Pantheon Terminal", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "pantheon-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("22ec69ee-e39e-4fa6-a241-4e950d3235af", PresetType.presetPlugin, OS.osLinux, "Pantheon Terminal", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "pantheon-terminal --working-directory=${easyshell:container_loc} --execute=./''${easyshell:resource_name}''"));
        // Linux Pantheon Filebrowser
        list.add(new CommandData("025e2f56-3d2e-47e1-8daa-c2c74049b150", PresetType.presetPlugin, OS.osLinux, "Pantheon", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "pantheon-files ${easyshell:resource_loc}"));
        // Linux PCManFM
        list.add(new CommandData("cb9a8c00-89bc-453c-aeff-ae94c0d9e44a", PresetType.presetPlugin, OS.osLinux, "PCManFM", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "pcmanfm ${easyshell:container_loc}"));
        // Linux Nautilus
        list.add(new CommandData("1747b189-ed7f-4546-8c98-f99a3c1fb13b", PresetType.presetPlugin, OS.osLinux, "Nautilus", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "nautilus ${easyshell:resource_loc}"));
        // Linux Dolphin
        list.add(new CommandData("4fbfa632-5455-4384-9f9e-773603a12bea", PresetType.presetPlugin, OS.osLinux, "Dolphin", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "dolphin --select ${easyshell:resource_loc}"));
        // Linux Nemo
        list.add(new CommandData("8e14d26d-2981-4b81-b8e5-6a942c6f2c59", PresetType.presetPlugin, OS.osLinux, "Nemo", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "nemo ${easyshell:resource_loc}"));
        // Linux Thunar
        list.add(new CommandData("cf8d4d60-10f4-4a31-a423-676d02d974e0", PresetType.presetPlugin, OS.osLinux, "Thunar", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "thunar ${easyshell:container_loc}"));
        // Linux Caja (MATE): http://manpages.ubuntu.com/manpages/wily/man1/caja.1.html
        list.add(new CommandData("f2b970ed-ef98-4ce2-861c-20f44a471f49", PresetType.presetPlugin, OS.osLinux, "Caja", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "caja ${easyshell:container_loc}"));        
    }

    private void addMacOSXCommands() {
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
        // MAC OS X Finder
        list.add(new CommandData("f6bcdd71-4687-46d8-bf34-2780bafd762a", PresetType.presetPlugin, OS.osMacOSX, "Finder", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "open -R ${easyshell:resource_loc}"));
    }

    private void addAllOSCommands() {
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

    public CommandDataList getCommands() {
        return list;
    }

    public static CommandDataList getCommandsNative(CommandDataList list) {
        if (list == null) {
            list = getAllCommandsStatic();
        }
        return getCommandData(list, Utils.getOS());
    }

    private static CommandDataList getDefaultCommands() {
        CommandDataList listAll = getAllCommandsStatic();
        CommandDataList listOS = new CommandDataList();
        CommandDataList listDefault = new CommandDataList();
        OS os = Utils.getOS();
        // now get all data by OS
        listOS = getCommandData(listAll, os);
        // now get by name
        switch(os)
        {
            case osUnknown:
                break;
            case osWindows:
            	addNotNull(listDefault, getCommandData(listOS, ".*Prompt.*", Category.categoryOpen));
            	addNotNull(listDefault, getCommandData(listOS, ".*Prompt.*", Category.categoryRun));
                addNotNull(listDefault, getCommandData(listOS, "Explorer", Category.categoryExplore));
                break;
            case osLinux:
                // add default preset for all desktops (XDG-open)
            	addNotNull(listDefault, getCommandData(listOS, ".*", Category.categoryDefault));
            	// try to detect the desktop
                LinuxDesktop desktop = Utils.detectLinuxDesktop();
                //Activator.getDefault().sysout(true, "Detected linux (Unix) desktop: >" + desktop.getName() + "<");
                switch (desktop) {
                    case desktopKde:    addNotNull(listDefault, getCommandData(listOS, ".*KDE.*", Category.categoryOpen));
                                        addNotNull(listDefault, getCommandData(listOS, ".*KDE.*", Category.categoryRun));
                                        //addNotNull(listDefault, getCommandData(listOS, "Dolphin", Category.categoryExplore));
                    break;
                    case desktopCinnamon:   addNotNull(listDefault, getCommandData(listOS, ".*Gnome.*", Category.categoryOpen));
                                            addNotNull(listDefault, getCommandData(listOS, ".*Gnome.*", Category.categoryRun));
                                            //addNotNull(listDefault, getCommandData(listOS, "Nemo", Category.categoryExplore));
                    break;
                    case desktopGnome:  addNotNull(listDefault, getCommandData(listOS, ".*Gnome.*", Category.categoryOpen));
                                        addNotNull(listDefault, getCommandData(listOS, ".*Gnome.*", Category.categoryRun));
                                        //addNotNull(listDefault, getCommandData(listOS, "Nautilus", Category.categoryExplore));
                    break;
                    case desktopXfce:   addNotNull(listDefault, getCommandData(listOS, ".*Xfce.*", Category.categoryOpen));
                                        addNotNull(listDefault, getCommandData(listOS, ".*Xfce.*", Category.categoryRun));
                                        //addNotNull(listDefault, getCommandData(listOS, "Thunar", Category.categoryExplore));
                    break;
                    case desktopMate:   addNotNull(listDefault, getCommandData(listOS, ".*MATE.*", Category.categoryOpen));
                    					addNotNull(listDefault, getCommandData(listOS, ".*MATE.*", Category.categoryRun));
                    					//addNotNull(listDefault, getCommandData(listOS, "Caja", Category.categoryExplore));
                    break;
                    case desktopLxde:   addNotNull(listDefault, getCommandData(listOS, ".*LX.*", Category.categoryOpen));
										addNotNull(listDefault, getCommandData(listOS, ".*LX.*", Category.categoryRun));
										//addNotNull(listDefault, getCommandData(listOS, "PCManFM", Category.categoryExplore));
					break;
                    default:;
                }
                // try to detect the default file browser
                if (desktop != LinuxDesktop.desktopUnknown) {
                    Map<String, Object> fileBrowsers = new HashMap<String, Object>();
                    for (CommandData data : getCommandDataList(listOS, Category.categoryExplore)) {
                    	fileBrowsers.put("(?i).*" + data.getName() + ".*", data);
                    }
                    Object fileBrowser = Utils.detectLinuxDefaultFileBrowser(fileBrowsers);
                    if (fileBrowser != null) {
                    	CommandData data = (CommandData)fileBrowser;
                    	Activator.logInfo("Detected linux (Unix) default file browser: >" + data.getName() + "<", null);
                    	addNotNull(listDefault, data);
                    }
                }
                break;
            case osMacOSX:
                addNotNull(listDefault, getCommandData(listOS, "Terminal", Category.categoryOpen));
                addNotNull(listDefault, getCommandData(listOS, "Terminal", Category.categoryRun));
                addNotNull(listDefault, getCommandData(listOS, "Finder", Category.categoryExplore));
                break;
            case osUnix:
                // no op
                break;
            default:
                break;
        }
        // add clipboard
        addNotNull(listDefault, getCommandData(listOS, ".*Path", Category.categoryClipboard));
        addNotNull(listDefault, getCommandData(listOS, ".*Name", Category.categoryClipboard));
        return listDefault;
    }

    private static void addNotNull(CommandDataList list, CommandData data) {
    	if (data != null) {
    		list.add(data);
    	}
    }

    public static CommandDataList getCommandData(CommandDataList list, OS os) {
        CommandDataList listOut = new CommandDataList();
        for (CommandData entry : list) {
            if (entry.getOs() == os) {
                CommandData newData = new CommandData(entry, false);
                listOut.add(newData);
            }
        }
        return listOut;
    }

    private static CommandData getCommandData(CommandDataList list, String name, Category category) {
        for (CommandData entry : list) {
            if (entry.getCategory() == category && entry.getName().matches(name)) {
                return entry;
            }
        }
        return null;
    }

    private static CommandDataList getCommandDataList(CommandDataList list, Category category) {
    	CommandDataList listOut = new CommandDataList();
        for (CommandData entry : list) {
            if (entry.getCategory() == category) {
            	CommandData newData = new CommandData(entry, false);
            	listOut.add(newData);
            }
        }
        return listOut;
    }

}
