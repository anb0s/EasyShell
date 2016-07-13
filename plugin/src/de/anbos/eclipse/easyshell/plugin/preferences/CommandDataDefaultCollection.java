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
import java.util.List;

import de.anbos.eclipse.easyshell.plugin.Utils;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.LinuxDesktop;
import de.anbos.eclipse.easyshell.plugin.types.OS;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;

public class CommandDataDefaultCollection {

    private List<CommandData> list = new ArrayList<CommandData>();
    private static CommandDataDefaultCollection instance = new CommandDataDefaultCollection();

    public static List<CommandData> getAllCommandsStatic() {
        return instance.getCommands();
    }

    public static List<CommandData> getDefaultCommandsStatic() {
        return getDefaultCommands();
    }

    public static List<MenuData> getDefaultCommandsMenuStatic() {
        List<CommandData> list = getDefaultCommandsStatic();
        List<MenuData> ret = new ArrayList<MenuData>();
        for (CommandData data : list) {
            ret.add(new MenuData(data));
        }
        return ret;
    }

    CommandDataDefaultCollection() {
        // Windows DOS-Shell
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "DOS-Shell", ResourceType.resourceFileOrFolder, CommandType.commandTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} cmd.exe /K"));
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "DOS-Shell", ResourceType.resourceFileOrFolder, CommandType.commandTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} ${easyshell:resource_name}"));
        // Windows Explorer
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "Explorer", ResourceType.resourceFileOrFolder, CommandType.commandTypeExplore,
                "explorer.exe /select, ${easyshell:resource_loc}"));
        // Windows PowerShell
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "PowerShell", ResourceType.resourceFileOrFolder, CommandType.commandTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe"));
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "PowerShell", ResourceType.resourceFileOrFolder, CommandType.commandTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe -command ./''${easyshell:resource_name}''"));
        // Windows Cygwin (Bash)
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "Cygwin (Bash)", ResourceType.resourceFileOrFolder, CommandType.commandTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\""));
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "Cygwin (Bash)", ResourceType.resourceFileOrFolder, CommandType.commandTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\" -c ./''${easyshell:resource_name}''"));
        // Windows Console
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "Console", ResourceType.resourceFileOrFolder, CommandType.commandTypeOpen,
                "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc}"));
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "Cygwin (Bash)", ResourceType.resourceFileOrFolder, CommandType.commandTypeRun,
                "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc} -r \"/k\\\"${easyshell:resource_name}\\\"\""));
        // Windows Git-Bash
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "Git-Bash", ResourceType.resourceFileOrFolder, CommandType.commandTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i"));
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "Cygwin (Bash)", ResourceType.resourceFileOrFolder, CommandType.commandTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i -c ./''${easyshell:resource_name}''"));
        // Windows ConEmu
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "ConEmu", ResourceType.resourceFileOrFolder, CommandType.commandTypeOpen,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd cmd"));
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "Cygwin (Bash)", ResourceType.resourceFileOrFolder, CommandType.commandTypeRun,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd \"${easyshell:resource_name}\""));
        // Windows TotalCommander
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "TotalCommander", ResourceType.resourceFileOrFolder, CommandType.commandTypeExplore,
                "totalcmd.exe /O /T ${easyshell:container_loc}"));
        // Windows Clipboard
        list.add(new CommandData(PresetType.presetPlugin, OS.osWindows, "Full path", ResourceType.resourceFileOrFolder, CommandType.commandTypeClipboard,
                "\"${easyshell:resource_loc}\"${easyshell:line_separator}"));
        // Linux KDE Konsole
        list.add(new CommandData(PresetType.presetPlugin, OS.osLinux, "KDE Konsole", ResourceType.resourceFileOrFolder, CommandType.commandTypeOpen,
                "konsole --workdir ${easyshell:container_loc}"));
        list.add(new CommandData(PresetType.presetPlugin, OS.osLinux, "KDE Konsole", ResourceType.resourceFileOrFolder, CommandType.commandTypeRun,
                "konsole --workdir ${easyshell:container_loc} --noclose  -e ${easyshell:resource_loc}"));
        // Linux Konqueror
        list.add(new CommandData(PresetType.presetPlugin, OS.osLinux, "Konqueror", ResourceType.resourceFileOrFolder, CommandType.commandTypeExplore,
                "konqueror file:\"${easyshell:resource_loc}\""));
        // Linux Gnome Terminal
        list.add(new CommandData(PresetType.presetPlugin, OS.osLinux, "Gnome Terminal", ResourceType.resourceFileOrFolder, CommandType.commandTypeOpen,
                "gnome-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData(PresetType.presetPlugin, OS.osLinux, "Gnome Terminal", ResourceType.resourceFileOrFolder, CommandType.commandTypeRun,
                "gnome-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}''"));
        // Linux Xfce Terminal
        list.add(new CommandData(PresetType.presetPlugin, OS.osLinux, "Xfce Terminal", ResourceType.resourceFileOrFolder, CommandType.commandTypeOpen,
                "xfce4-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData(PresetType.presetPlugin, OS.osLinux, "Xfce Terminal", ResourceType.resourceFileOrFolder, CommandType.commandTypeRun,
                "xfce4-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}'' --hold"));
        // Linux Nautilus
        list.add(new CommandData(PresetType.presetPlugin, OS.osLinux, "Nautilus", ResourceType.resourceFileOrFolder, CommandType.commandTypeExplore,
                "nautilus ${easyshell:resource_loc}"));
        // Linux Dolphin
        list.add(new CommandData(PresetType.presetPlugin, OS.osLinux, "Dolphin", ResourceType.resourceFileOrFolder, CommandType.commandTypeExplore,
                "dolphin --select ${easyshell:resource_loc}"));
        // Linux Nemo
        list.add(new CommandData(PresetType.presetPlugin, OS.osLinux, "Nemo", ResourceType.resourceFileOrFolder, CommandType.commandTypeExplore,
                "nemo ${easyshell:resource_loc}"));
        // Linux Thunar
        list.add(new CommandData(PresetType.presetPlugin, OS.osLinux, "Thunar", ResourceType.resourceFileOrFolder, CommandType.commandTypeExplore,
                "thunar ${easyshell:resource_loc}"));
        // Linux Clipboard
        list.add(new CommandData(PresetType.presetPlugin, OS.osLinux, "Full path", ResourceType.resourceFileOrFolder, CommandType.commandTypeClipboard,
                "${easyshell:resource_loc}${easyshell:line_separator}"));
        // MAC OS X Terminal
        list.add(new CommandData(PresetType.presetPlugin, OS.osMacOSX, "Terminal", ResourceType.resourceFileOrFolder, CommandType.commandTypeOpen,
                "open -a Terminal ${easyshell:container_loc}"));
        list.add(new CommandData(PresetType.presetPlugin, OS.osMacOSX, "Terminal", ResourceType.resourceFileOrFolder, CommandType.commandTypeRun,
                "open -a Terminal ${easyshell:container_loc}"));
        // MAC OS X Finder
        list.add(new CommandData(PresetType.presetPlugin, OS.osMacOSX, "Finder", ResourceType.resourceFileOrFolder, CommandType.commandTypeExplore,
                "open -R ${easyshell:resource_loc}"));
        // MAC OS X Clipboard
        list.add(new CommandData(PresetType.presetPlugin, OS.osMacOSX, "Full path", ResourceType.resourceFileOrFolder, CommandType.commandTypeClipboard,
                "${easyshell:resource_loc}${easyshell:line_separator}"));
    }

    public List<CommandData> getCommands() {
        return list;
    }

    public static List<CommandData> getCommandsNative(List<CommandData> list) {
        if (list == null) {
            list = getAllCommandsStatic();
        }
        return getCommandData(list, Utils.getOS());
    }

    public static List<CommandData> getDefaultCommands() {
        List<CommandData> listAll = getAllCommandsStatic();
        List<CommandData> listOS = new ArrayList<CommandData>();
        List<CommandData> listDefault = new ArrayList<CommandData>();
        OS os = Utils.getOS();
        // now get all data by OS
        listOS = getCommandData(listAll, os);
        // now get by name
        switch(os)
        {
            case osUnknown:
                break;
            case osWindows:
                listDefault.add(getCommandData(listOS, "DOS-Shell", CommandType.commandTypeOpen));
                listDefault.add(getCommandData(listOS, "DOS-Shell", CommandType.commandTypeRun));
                listDefault.add(getCommandData(listOS, "Explorer", CommandType.commandTypeExplore));
                break;
            case osLinux:
                // try to detect the desktop
                LinuxDesktop desktop = Utils.detectLinuxDesktop();
                //Activator.getDefault().sysout(true, "Detected linux (Unix) desktop: >" + desktop.getName() + "<");
                switch (desktop) {
                    case desktopKde:    listDefault.add(getCommandData(listOS, "KDE", CommandType.commandTypeOpen));
                                        listDefault.add(getCommandData(listOS, "KDE", CommandType.commandTypeRun));
                                        listDefault.add(getCommandData(listOS, "Dolphin", CommandType.commandTypeExplore));
                    break;
                    case desktopCinnamon:   listDefault.add(getCommandData(listOS, "Gnome", CommandType.commandTypeOpen));
                                            listDefault.add(getCommandData(listOS, "Gnome", CommandType.commandTypeRun));
                                            listDefault.add(getCommandData(listOS, "Nemo", CommandType.commandTypeExplore));
                    break;
                    case desktopGnome:  listDefault.add(getCommandData(listOS, "Gnome", CommandType.commandTypeOpen));
                                        listDefault.add(getCommandData(listOS, "Gnome", CommandType.commandTypeRun));
                                        listDefault.add(getCommandData(listOS, "Nautilus", CommandType.commandTypeExplore));
                    break;
                    case desktopXfce:   listDefault.add(getCommandData(listOS, "Xfce", CommandType.commandTypeOpen));
                                        listDefault.add(getCommandData(listOS, "Xfce", CommandType.commandTypeRun));
                                        listDefault.add(getCommandData(listOS, "Thunar", CommandType.commandTypeExplore));
                    break;
                    default:;
                }
                // try to detect the default file browser
                if (desktop != LinuxDesktop.desktopUnknown) {
                    String fileBrowser = Utils.detectLinuxDefaultFileBrowser();
                    //Activator.getDefault().sysout(true, "Detected linux (Unix) default file browser: >" + fileBrowser + "<");
                }
                break;
            case osMacOSX:
                listDefault.add(getCommandData(listOS, "Terminal", CommandType.commandTypeOpen));
                listDefault.add(getCommandData(listOS, "Terminal", CommandType.commandTypeRun));
                listDefault.add(getCommandData(listOS, "Finder", CommandType.commandTypeExplore));
                break;
        }
        // add clipboard
        listDefault.add(getCommandData(listOS, ".*path", CommandType.commandTypeClipboard));
        return listDefault;
    }

    public static List<CommandData> getCommandData(List<CommandData> list, OS os) {
        List<CommandData> listOut = new ArrayList<CommandData>();
        for (CommandData entry : list) {
            if (entry.getOS() == os) {
                listOut.add(entry);
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

}
