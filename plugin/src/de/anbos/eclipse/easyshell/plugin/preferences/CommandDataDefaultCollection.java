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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandDataDefaultCollection {

    private List<CommandData> list = new ArrayList<CommandData>();
    private static CommandDataDefaultCollection instance = new CommandDataDefaultCollection();

    public static List<CommandData> getAllCommandsStatic() {
        return instance.getCommands();
    }

    public static List<CommandData> getDefaultCommandsStatic() {
        return getDefaultCommands();
    }

    public static List<CommandMenuData> getDefaultCommandsMenuStatic() {
        List<CommandData> list = getDefaultCommandsStatic();
        List<CommandMenuData> ret = new ArrayList<CommandMenuData>();
        for (CommandData data : list) {
            ret.add(new CommandMenuData(data));
        }
        return ret;
    }

    CommandDataDefaultCollection() {
        // Windows DOS-Shell
        list.add(new CommandData(OS.osWindows, "DOS-Shell", RessourceType.ressourceFileOrFolder, CommandType.commandTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} cmd.exe /K"));
        list.add(new CommandData(OS.osWindows, "DOS-Shell", RessourceType.ressourceFileOrFolder, CommandType.commandTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} ${easyshell:resource_name}"));
        // Windows Explorer
        list.add(new CommandData(OS.osWindows, "Explorer", RessourceType.ressourceFileOrFolder, CommandType.commandTypeExplore,
                "explorer.exe /select, ${easyshell:resource_loc}"));
        // Windows PowerShell
        list.add(new CommandData(OS.osWindows, "PowerShell", RessourceType.ressourceFileOrFolder, CommandType.commandTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe"));
        list.add(new CommandData(OS.osWindows, "PowerShell", RessourceType.ressourceFileOrFolder, CommandType.commandTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe -command ./''${easyshell:resource_name}''"));
        // Windows Cygwin (Bash)
        list.add(new CommandData(OS.osWindows, "Cygwin (Bash)", RessourceType.ressourceFileOrFolder, CommandType.commandTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\""));
        list.add(new CommandData(OS.osWindows, "Cygwin (Bash)", RessourceType.ressourceFileOrFolder, CommandType.commandTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\" -c ./''${easyshell:resource_name}''"));
        // Windows Console
        list.add(new CommandData(OS.osWindows, "Console", RessourceType.ressourceFileOrFolder, CommandType.commandTypeOpen,
                "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc}"));
        list.add(new CommandData(OS.osWindows, "Cygwin (Bash)", RessourceType.ressourceFileOrFolder, CommandType.commandTypeRun,
                "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc} -r \"/k\\\"${easyshell:resource_name}\\\"\""));
        // Windows Git-Bash
        list.add(new CommandData(OS.osWindows, "Git-Bash", RessourceType.ressourceFileOrFolder, CommandType.commandTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i"));
        list.add(new CommandData(OS.osWindows, "Cygwin (Bash)", RessourceType.ressourceFileOrFolder, CommandType.commandTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i -c ./''${easyshell:resource_name}''"));
        // Windows ConEmu
        list.add(new CommandData(OS.osWindows, "ConEmu", RessourceType.ressourceFileOrFolder, CommandType.commandTypeOpen,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd cmd"));
        list.add(new CommandData(OS.osWindows, "Cygwin (Bash)", RessourceType.ressourceFileOrFolder, CommandType.commandTypeRun,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd \"${easyshell:resource_name}\""));
        // Windows TotalCommander
        list.add(new CommandData(OS.osWindows, "TotalCommander", RessourceType.ressourceFileOrFolder, CommandType.commandTypeExplore,
                "totalcmd.exe /O /T ${easyshell:container_loc}"));
        // Windows Clipboard
        list.add(new CommandData(OS.osWindows, "Full path", RessourceType.ressourceFileOrFolder, CommandType.commandTypeClipboard,
                "\"${easyshell:resource_loc}\"${easyshell:line_separator}"));
        // Linux KDE Konsole
        list.add(new CommandData(OS.osLinux, "KDE Konsole", RessourceType.ressourceFileOrFolder, CommandType.commandTypeOpen,
                "konsole --workdir ${easyshell:container_loc}"));
        list.add(new CommandData(OS.osLinux, "KDE Konsole", RessourceType.ressourceFileOrFolder, CommandType.commandTypeRun,
                "konsole --workdir ${easyshell:container_loc} --noclose  -e ${easyshell:resource_loc}"));
        // Linux Konqueror
        list.add(new CommandData(OS.osLinux, "Konqueror", RessourceType.ressourceFileOrFolder, CommandType.commandTypeExplore,
                "konqueror file:\"${easyshell:resource_loc}\""));
        // Linux Gnome Terminal
        list.add(new CommandData(OS.osLinux, "Gnome Terminal", RessourceType.ressourceFileOrFolder, CommandType.commandTypeOpen,
                "gnome-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData(OS.osLinux, "Gnome Terminal", RessourceType.ressourceFileOrFolder, CommandType.commandTypeRun,
                "gnome-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}''"));
        // Linux Xfce Terminal
        list.add(new CommandData(OS.osLinux, "Xfce Terminal", RessourceType.ressourceFileOrFolder, CommandType.commandTypeOpen,
                "xfce4-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData(OS.osLinux, "Xfce Terminal", RessourceType.ressourceFileOrFolder, CommandType.commandTypeRun,
                "xfce4-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}'' --hold"));
        // Linux Nautilus
        list.add(new CommandData(OS.osLinux, "Nautilus", RessourceType.ressourceFileOrFolder, CommandType.commandTypeExplore,
                "nautilus ${easyshell:resource_loc}"));
        // Linux Dolphin
        list.add(new CommandData(OS.osLinux, "Dolphin", RessourceType.ressourceFileOrFolder, CommandType.commandTypeExplore,
                "dolphin --select ${easyshell:resource_loc}"));
        // Linux Nemo
        list.add(new CommandData(OS.osLinux, "Nemo", RessourceType.ressourceFileOrFolder, CommandType.commandTypeExplore,
                "nemo ${easyshell:resource_loc}"));
        // Linux Thunar
        list.add(new CommandData(OS.osLinux, "Thunar", RessourceType.ressourceFileOrFolder, CommandType.commandTypeExplore,
                "thunar ${easyshell:resource_loc}"));
        // Linux Clipboard
        list.add(new CommandData(OS.osLinux, "Full path", RessourceType.ressourceFileOrFolder, CommandType.commandTypeClipboard,
                "${easyshell:resource_loc}${easyshell:line_separator}"));
        // MAC OS X Terminal
        list.add(new CommandData(OS.osMacOSX, "Terminal", RessourceType.ressourceFileOrFolder, CommandType.commandTypeOpen,
                "open -a Terminal ${easyshell:container_loc}"));
        list.add(new CommandData(OS.osMacOSX, "Terminal", RessourceType.ressourceFileOrFolder, CommandType.commandTypeRun,
                "open -a Terminal ${easyshell:container_loc}"));
        // MAC OS X Finder
        list.add(new CommandData(OS.osMacOSX, "Finder", RessourceType.ressourceFileOrFolder, CommandType.commandTypeExplore,
                "open -R ${easyshell:resource_loc}"));
        // MAC OS X Clipboard
        list.add(new CommandData(OS.osMacOSX, "Full path", RessourceType.ressourceFileOrFolder, CommandType.commandTypeClipboard,
                "${easyshell:resource_loc}${easyshell:line_separator}"));
    }

    public List<CommandData> getCommands() {
        return list;
    }

    public static List<CommandData> getCommandsNative(List<CommandData> list) {
        if (list == null) {
            list = getAllCommandsStatic();
        }
        return getCommandData(list, getOS());
    }

    public static List<CommandData> getDefaultCommands() {
        List<CommandData> listAll = getAllCommandsStatic();
        List<CommandData> listOS = new ArrayList<CommandData>();
        List<CommandData> listDefault = new ArrayList<CommandData>();
        OS os = getOS();
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
                LinuxDesktop desktop = detectLinuxDesktop();
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
                    String fileBrowser = detectLinuxDefaultFileBrowser();
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

    public static OS getOS() {
        OS os = OS.osUnknown;
        /* possible OS string:
            AIX
            Digital UNIX
            FreeBSD
            HP UX
            Irix
            Linux
            Mac OS
            Mac OS X
            MPE/iX
            Netware 4.11
            OS/2
            Solaris
            Windows 95
            Windows 98
            Windows NT
            Windows Me
            Windows 2000
            Windows XP
            Windows 2003
            Windows CE
            Windows Vista
            Windows 7
         */
        String osname = System.getProperty("os.name", "").toLowerCase();
        if (osname.indexOf("windows") != -1) {
            os = OS.osWindows;
        } else if (osname.indexOf("mac os x") != -1) {
            os = OS.osMacOSX;
        } else if (
                   osname.indexOf("unix") != -1
                || osname.indexOf("irix") != -1
                || osname.indexOf("freebsd") != -1
                || osname.indexOf("hp-ux") != -1
                || osname.indexOf("aix") != -1
                || osname.indexOf("sunos") != -1
                || osname.indexOf("linux") != -1
                )
        {
            os = OS.osLinux;
        }
        return os;
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

    private static LinuxDesktop detectLinuxDesktop() {
        LinuxDesktop resultCode = detectDesktopSession();
        /*
        if (resultCode == LinuxDesktop.desktopUnknown)
        {
            if (isCde())
                resultCode = LinuxDesktop.desktopCde;
        }
        */
        return resultCode;
    }

    /**
     * detects desktop from $DESKTOP_SESSION
     */
    private static LinuxDesktop detectDesktopSession() {
        ArrayList<String> command = new ArrayList<String>();
        command.add("sh");
        command.add("-c");
        command.add("echo \"$DESKTOP_SESSION\"");
        // fill the map
        Map<String, Object> desktops = new HashMap<String, Object>();
        desktops.put("kde", LinuxDesktop.desktopKde);
        desktops.put("gnome", LinuxDesktop.desktopGnome);
        desktops.put("cinnamon", LinuxDesktop.desktopCinnamon);
        desktops.put("xfce", LinuxDesktop.desktopXfce);
        // execute
        String desktop = isExpectedCommandOutput(command, desktops, true);
        if (desktop != null && !desktop.isEmpty()) {
            return (LinuxDesktop)desktops.get(desktop);
        }
        return LinuxDesktop.desktopUnknown;
    }

    /**
     * detects programs from $DESKTOP_SESSION
     */
    private static String detectLinuxDefaultFileBrowser() {
        ArrayList<String> command = new ArrayList<String>();
        command.add("xdg-mime");
        command.add("query");
        command.add("default");
        command.add("inode/directory");
        // fill the map
        Map<String, Object> fileBrowsers = new HashMap<String, Object>();
        fileBrowsers.put("nemo.desktop", "nemo");
        // execute
        String fileBrowser = isExpectedCommandOutput(command, fileBrowsers, true);
        if (fileBrowser != null && !fileBrowser.isEmpty()) {
            return (String)fileBrowsers.get(fileBrowser);
        }
        return null;
    }

    /**
     * Detects which desktop is used on a unix / linux system.
     *
     * @todo use regex
     *
     * @return The type of desktop.
     * @see detectDesktop
     */
    private static String isExpectedCommandOutput(ArrayList<String> command, Map<String, Object> expectedOutput, boolean toLowerCase) {
        boolean found = false;
        String expectedLine = null;
        try {
            Process proc = Runtime.getRuntime().exec(command.toArray(new String[1]));
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while((line = in.readLine()) != null && !found) {
                for(String key: expectedOutput.keySet()) {
                    // in case of * just something should be returned
                    if (key.indexOf("*") != -1)
                    {
                        if (line.isEmpty()) {
                            found = false;
                            break;
                        } else {
                            found = true;
                        }
                    } else {
                        if (toLowerCase)
                            line = line.toLowerCase();
                        if(line.indexOf(key) != -1) {
                            found = true;
                        }
                    }
                    if (found) {
                        expectedLine = line;
                        break;
                    }
                }
            }
            line = null;
            BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            // If there is any error output, print it to
            // stdout for debugging purposes
            while((line = err.readLine()) != null) {
                //Activator.getDefault().sysout(true, "detectDesktop stderr >" + line + "<");
            }

            int result = proc.waitFor();
            if(result != 0) {
                // If there is any error code, print it to
                // stdout for debugging purposes
                //Activator.getDefault().sysout(true, "detectDesktop return code: " + result);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return expectedLine;
    }

}
