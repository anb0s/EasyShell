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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.Utils;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.LinuxDesktop;
import de.anbos.eclipse.easyshell.plugin.types.OS;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

public class CommandDataDefaultCollection {

    private List<CommandData> list = new ArrayList<CommandData>();
    private static CommandDataDefaultCollection instance = new CommandDataDefaultCollection();

    public static List<CommandData> getAllCommandsStatic(boolean sorted) {
        List<CommandData> ret = instance.getCommands();
        if (sorted) {
            for (int i=0;i<ret.size();i++) {
                ret.get(i).setPosition(i);
            }
        }
        return ret;
    }

    public static List<MenuData> getCommandsNativeAsMenu(boolean sorted) {
        List<CommandData> list = getDefaultCommands();
        List<MenuData> ret = new ArrayList<MenuData>();
        for (int i=0;i<list.size();i++) {
            CommandData cmdData = list.get(i);
            MenuData newData = new MenuData(cmdData.getId(), cmdData); // use the same id like the default command to have same defaults
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
    }


    private void addWindowsCommands() {
        // Windows DOS-Shell
        list.add(new CommandData("cd361a40-bb37-4fa2-9d2e-b62794294a59", PresetType.presetPlugin, OS.osWindows, "DOS-Shell", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} cmd.exe /K"));
        list.add(new CommandData("f740984b-e9d5-4ebc-b9b5-c6b8527f886c", PresetType.presetPlugin, OS.osWindows, "DOS-Shell", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} ${easyshell:resource_name}"));
        // Windows Explorer
        list.add(new CommandData("ae0e5b6e-0b20-4c52-9708-ba7e317d1dee", PresetType.presetPlugin, OS.osWindows, "Explorer", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeExplore,
                "explorer.exe /select, ${easyshell:resource_loc}"));
        // Windows PowerShell
        list.add(new CommandData("9cb0d29e-93aa-4139-93d2-079ba63ff726", PresetType.presetPlugin, OS.osWindows, "PowerShell", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe"));
        list.add(new CommandData("af2968d0-3a1a-40db-b3cb-e2e16293a285", PresetType.presetPlugin, OS.osWindows, "PowerShell", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe -command ./''${easyshell:resource_name}''"));
        // Windows Cygwin (Bash)
        list.add(new CommandData("5b1e3806-a9ab-4866-b660-823ac388a575", PresetType.presetPlugin, OS.osWindows, "Cygwin (Bash)", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\""));
        list.add(new CommandData("2002e587-70a3-4204-b1a5-6faf6271ad08", PresetType.presetPlugin, OS.osWindows, "Cygwin (Bash)", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\" -c ./''${easyshell:resource_name}''"));
        // Windows Console
        list.add(new CommandData("60fd43d2-d837-41d1-aaa3-3f5cab6bf0fb", PresetType.presetPlugin, OS.osWindows, "Console", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeOpen,
                "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc}"));
        list.add(new CommandData("af6d97f2-f0a8-46e2-8234-74c0ee3e6007", PresetType.presetPlugin, OS.osWindows, "Cygwin (Bash)", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeRun,
                "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc} -r \"/k\\\"${easyshell:resource_name}\\\"\""));
        // Windows Git-Bash
        list.add(new CommandData("24419204-c8e5-4d79-a7b8-b14e93077cf0", PresetType.presetPlugin, OS.osWindows, "Git-Bash", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i"));
        list.add(new CommandData("ee790c7f-9c6d-40f9-84f6-51a948a59d45", PresetType.presetPlugin, OS.osWindows, "Cygwin (Bash)", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i -c ./''${easyshell:resource_name}''"));
        // Windows ConEmu
        list.add(new CommandData("1bd62e22-cd93-4136-b643-1cbb9579c195", PresetType.presetPlugin, OS.osWindows, "ConEmu", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeOpen,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd cmd"));
        list.add(new CommandData("c2b73077-ffd9-4fb7-9793-189be9f13ebb", PresetType.presetPlugin, OS.osWindows, "Cygwin (Bash)", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeRun,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd \"${easyshell:resource_name}\""));
        // Windows TotalCommander
        list.add(new CommandData("e487327c-dfdb-42e7-bf16-3b81a34e5703", PresetType.presetPlugin, OS.osWindows, "TotalCommander", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeExplore,
                "totalcmd.exe /O /T ${easyshell:container_loc}"));
        // Windows Clipboard
        list.add(new CommandData("67aa9dff-6bbb-4b47-8b43-8a82a7a279fa", PresetType.presetPlugin, OS.osWindows, "Full path", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeClipboard,
                "\"${easyshell:resource_loc}\"${easyshell:line_separator}"));
    }

    private void addLinuxCommands() {
        // Linux KDE Konsole
        list.add(new CommandData("51ed300a-35d0-4e67-a5f8-6ebd7012a564", PresetType.presetPlugin, OS.osLinux, "XDG Open", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeDefault,
                "xdg-open ${easyshell:resource_loc}"));
        // Linux KDE Konsole
        list.add(new CommandData("c2b1612b-9037-484a-a763-d013679bdbe7", PresetType.presetPlugin, OS.osLinux, "KDE Konsole", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeOpen,
                "konsole --workdir ${easyshell:container_loc}"));
        list.add(new CommandData("e634b20a-cf57-47f0-aa27-b8fc95917f35", PresetType.presetPlugin, OS.osLinux, "KDE Konsole", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeRun,
                "konsole --workdir ${easyshell:container_loc} --noclose  -e ${easyshell:resource_loc}"));
        // Linux Konqueror
        list.add(new CommandData("8873342e-e02b-4feb-8f56-9f52524c0f46", PresetType.presetPlugin, OS.osLinux, "Konqueror", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeExplore,
                "konqueror file:\"${easyshell:resource_loc}\""));
        // Linux Gnome Terminal
        list.add(new CommandData("53f7b6a3-f8d5-4682-b0ef-1d6ceec5cc8a", PresetType.presetPlugin, OS.osLinux, "Gnome Terminal", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeOpen,
                "gnome-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("c6126958-32f2-4f96-9933-69ddd956f2e9", PresetType.presetPlugin, OS.osLinux, "Gnome Terminal", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeRun,
                "gnome-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}''"));
        // Linux Xfce Terminal
        list.add(new CommandData("8175f9a7-4e54-4367-a6b6-251aedc187df", PresetType.presetPlugin, OS.osLinux, "Xfce Terminal", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeOpen,
                "xfce4-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("adf40e10-0ee9-4abe-8282-aff7d51bb68d", PresetType.presetPlugin, OS.osLinux, "Xfce Terminal", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeRun,
                "xfce4-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}'' --hold"));
        // Linux Pantheon Terminal
        list.add(new CommandData("e5b3b0f6-e27c-4a2d-aa1a-caef784dd3da", PresetType.presetPlugin, OS.osLinux, "Pantheon Terminal", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeOpen,
                "pantheon-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("22ec69ee-e39e-4fa6-a241-4e950d3235af", PresetType.presetPlugin, OS.osLinux, "Pantheon Terminal", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeRun,
                "pantheon-terminal --working-directory=${easyshell:container_loc} --execute=./''${easyshell:resource_name}''"));
        // Linux Sakura Terminal
        list.add(new CommandData("8e366a34-5ce2-4430-bc21-20e176e0128c", PresetType.presetPlugin, OS.osLinux, "Sakura Terminal", ResourceType.resourceTypeFileOrDirectory, true, "${easyshell:container_loc}", CommandType.commandTypeOpen,
                "sakura"));
        list.add(new CommandData("2a979af5-86a7-440e-b4f0-8442e85412e4", PresetType.presetPlugin, OS.osLinux, "Sakura Terminal", ResourceType.resourceTypeFileOrDirectory, true, "${easyshell:container_loc}", CommandType.commandTypeRun,
                "sakura --execute=./''${easyshell:resource_name}'' --hold"));
        // Linux Pantheon Filebrowser
        list.add(new CommandData("025e2f56-3d2e-47e1-8daa-c2c74049b150", PresetType.presetPlugin, OS.osLinux, "Pantheon", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeExplore,
                "pantheon-files ${easyshell:resource_loc}"));
        // Linux Nautilus
        list.add(new CommandData("1747b189-ed7f-4546-8c98-f99a3c1fb13b", PresetType.presetPlugin, OS.osLinux, "Nautilus", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeExplore,
                "nautilus ${easyshell:resource_loc}"));
        // Linux Dolphin
        list.add(new CommandData("4fbfa632-5455-4384-9f9e-773603a12bea", PresetType.presetPlugin, OS.osLinux, "Dolphin", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeExplore,
                "dolphin --select ${easyshell:resource_loc}"));
        // Linux Nemo
        list.add(new CommandData("8e14d26d-2981-4b81-b8e5-6a942c6f2c59", PresetType.presetPlugin, OS.osLinux, "Nemo", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeExplore,
                "nemo ${easyshell:resource_loc}"));
        // Linux Thunar
        list.add(new CommandData("cf8d4d60-10f4-4a31-a423-676d02d974e0", PresetType.presetPlugin, OS.osLinux, "Thunar", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeExplore,
                "thunar ${easyshell:container_loc}"));
        // Linux Clipboard
        list.add(new CommandData("33043fe3-1a5f-46d7-b94e-9a02ef204e7d", PresetType.presetPlugin, OS.osLinux, "Full path", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeClipboard,
                "${easyshell:resource_loc}${easyshell:line_separator}"));
    }

    private void addMacOSXCommands() {
        // MAC OS X Terminal
        list.add(new CommandData("e6918fe0-38b8-450b-a4be-d9eecc0dc0b4", PresetType.presetPlugin, OS.osMacOSX, "Terminal", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeOpen,
                "open -a Terminal ${easyshell:container_loc}"));
        list.add(new CommandData("db61d61e-8bf4-41d0-a1d8-00379e4d1db1", PresetType.presetPlugin, OS.osMacOSX, "Terminal", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeRun,
                "open -a Terminal ${easyshell:container_loc}"));
        // MAC OS X Finder
        list.add(new CommandData("f6bcdd71-4687-46d8-bf34-2780bafd762a", PresetType.presetPlugin, OS.osMacOSX, "Finder", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeExplore,
                "open -R ${easyshell:resource_loc}"));
        // MAC OS X Clipboard
        list.add(new CommandData("cd32fa5a-34d7-4551-8bd0-3aae0dc444d0", PresetType.presetPlugin, OS.osMacOSX, "Full path", ResourceType.resourceTypeFileOrDirectory, CommandType.commandTypeClipboard,
                "${easyshell:resource_loc}${easyshell:line_separator}"));
    }

    public List<CommandData> getCommands() {
        return list;
    }

    public static List<CommandData> getCommandsNative(List<CommandData> list, boolean sorted) {
        if (list == null) {
            list = getAllCommandsStatic(false);
        }
        return getCommandData(list, Utils.getOS(), sorted);
    }

    private static List<CommandData> getDefaultCommands() {
        List<CommandData> listAll = getAllCommandsStatic(false);
        List<CommandData> listOS = new ArrayList<CommandData>();
        List<CommandData> listDefault = new ArrayList<CommandData>();
        OS os = Utils.getOS();
        // now get all data by OS
        listOS = getCommandData(listAll, os, true);
        // now get by name
        switch(os)
        {
            case osUnknown:
                break;
            case osWindows:
            	addNotNull(listDefault, getCommandData(listOS, "DOS-Shell", CommandType.commandTypeOpen));
            	addNotNull(listDefault, getCommandData(listOS, "DOS-Shell", CommandType.commandTypeRun));
                addNotNull(listDefault, getCommandData(listOS, "Explorer", CommandType.commandTypeExplore));
                break;
            case osLinux:
                // add default preset for all desktops (XDG-open)
            	addNotNull(listDefault, getCommandData(listOS, ".*", CommandType.commandTypeDefault));
            	// try to detect the desktop
                LinuxDesktop desktop = Utils.detectLinuxDesktop();
                //Activator.getDefault().sysout(true, "Detected linux (Unix) desktop: >" + desktop.getName() + "<");
                switch (desktop) {
                    case desktopKde:    addNotNull(listDefault, getCommandData(listOS, ".*KDE.*", CommandType.commandTypeOpen));
                                        addNotNull(listDefault, getCommandData(listOS, ".*KDE.*", CommandType.commandTypeRun));
                                        //addNotNull(listDefault, getCommandData(listOS, "Dolphin", CommandType.commandTypeExplore));
                    break;
                    case desktopCinnamon:   addNotNull(listDefault, getCommandData(listOS, ".*Gnome.*", CommandType.commandTypeOpen));
                                            addNotNull(listDefault, getCommandData(listOS, ".*Gnome.*", CommandType.commandTypeRun));
                                            //addNotNull(listDefault, getCommandData(listOS, "Nemo", CommandType.commandTypeExplore));
                    break;
                    case desktopGnome:  addNotNull(listDefault, getCommandData(listOS, ".*Gnome.*", CommandType.commandTypeOpen));
                                        addNotNull(listDefault, getCommandData(listOS, ".*Gnome.*", CommandType.commandTypeRun));
                                        //addNotNull(listDefault, getCommandData(listOS, "Nautilus", CommandType.commandTypeExplore));
                    break;
                    case desktopXfce:   addNotNull(listDefault, getCommandData(listOS, ".*Xfce.*", CommandType.commandTypeOpen));
                                        addNotNull(listDefault, getCommandData(listOS, ".*Xfce.*", CommandType.commandTypeRun));
                                        //addNotNull(listDefault, getCommandData(listOS, "Thunar", CommandType.commandTypeExplore));
                    break;
                    default:;
                }
                // try to detect the default file browser
                if (desktop != LinuxDesktop.desktopUnknown) {
                    Map<String, Object> fileBrowsers = new HashMap<String, Object>();
                    for (CommandData data : getCommandDataList(listOS, CommandType.commandTypeExplore)) {
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
                addNotNull(listDefault, getCommandData(listOS, "Terminal", CommandType.commandTypeOpen));
                addNotNull(listDefault, getCommandData(listOS, "Terminal", CommandType.commandTypeRun));
                addNotNull(listDefault, getCommandData(listOS, "Finder", CommandType.commandTypeExplore));
                break;
        }
        // add clipboard
        addNotNull(listDefault, getCommandData(listOS, ".*path", CommandType.commandTypeClipboard));
        return listDefault;
    }

    private static void addNotNull(List<CommandData> list, CommandData data) {
    	if (data != null) {
    		list.add(data);
    	}
    }

    public static List<CommandData> getCommandData(List<CommandData> list, OS os, boolean sorted) {
        List<CommandData> listOut = new ArrayList<CommandData>();
        int position = 0;
        for (CommandData entry : list) {
            if (entry.getOs() == os) {
                CommandData newData = new CommandData(entry, false);
                if (sorted) {
                    newData.setPosition(position);
                }
                listOut.add(newData);
                position++;
            }
        }
        return listOut;
    }

    private static CommandData getCommandData(List<CommandData> list, String name, CommandType type) {
        for (CommandData entry : list) {
            if (entry.getCommandType() == type && entry.getName().matches(name)) {
                return entry;
            }
        }
        return null;
    }

    private static List<CommandData> getCommandDataList(List<CommandData> list, CommandType type) {
    	List<CommandData> listOut = new ArrayList<CommandData>();
        for (CommandData entry : list) {
            if (entry.getCommandType() == type) {
            	CommandData newData = new CommandData(entry, false);
            	listOut.add(newData);
            }
        }
        return listOut;
    }

}
