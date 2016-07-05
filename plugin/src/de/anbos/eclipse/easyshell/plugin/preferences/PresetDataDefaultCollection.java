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

public class PresetDataDefaultCollection {

    private List<PresetData> list = new ArrayList<PresetData>();

    static public List<PresetData> getAllPresetsStatic() {
        return new PresetDataDefaultCollection().getAllPresets();
    }

    static public List<PresetData> getDefaultPresetsStatic() {
        return new PresetDataDefaultCollection().getDefaultPresets();
    }

    PresetDataDefaultCollection() {
        // Windows DOS-Shell
        list.add(new PresetData(OS.osWindows, "DOS-Shell", PresetType.presetTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} cmd.exe /K"));
        list.add(new PresetData(OS.osWindows, "DOS-Shell", PresetType.presetTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} ${easyshell:resource_name}"));
        // Windows Explorer
        list.add(new PresetData(OS.osWindows, "Explorer", PresetType.presetTypeExplore,
                "explorer.exe /select, ${easyshell:resource_loc}"));
        // Windows PowerShell
        list.add(new PresetData(OS.osWindows, "PowerShell", PresetType.presetTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe"));
        list.add(new PresetData(OS.osWindows, "PowerShell", PresetType.presetTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe -command ./''${easyshell:resource_name}''"));
        // Windows Cygwin (Bash)
        list.add(new PresetData(OS.osWindows, "Cygwin (Bash)", PresetType.presetTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\""));
        list.add(new PresetData(OS.osWindows, "Cygwin (Bash)", PresetType.presetTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\" -c ./''${easyshell:resource_name}''"));
        // Windows Console
        list.add(new PresetData(OS.osWindows, "Console", PresetType.presetTypeOpen,
                "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc}"));
        list.add(new PresetData(OS.osWindows, "Cygwin (Bash)", PresetType.presetTypeRun,
                "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc} -r \"/k\\\"${easyshell:resource_name}\\\"\""));
        // Windows Git-Bash
        list.add(new PresetData(OS.osWindows, "Git-Bash", PresetType.presetTypeOpen,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i"));
        list.add(new PresetData(OS.osWindows, "Cygwin (Bash)", PresetType.presetTypeRun,
                "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i -c ./''${easyshell:resource_name}''"));
        // Windows ConEmu
        list.add(new PresetData(OS.osWindows, "ConEmu", PresetType.presetTypeOpen,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd cmd"));
        list.add(new PresetData(OS.osWindows, "Cygwin (Bash)", PresetType.presetTypeRun,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd \"${easyshell:resource_name}\""));
        // Windows TotalCommander
        list.add(new PresetData(OS.osWindows, "TotalCommander", PresetType.presetTypeExplore,
                "totalcmd.exe /O /T ${easyshell:container_loc}"));
        // Windows Clipboard
        list.add(new PresetData(OS.osWindows, "Full path", PresetType.presetTypeClipboard,
                "\"${easyshell:resource_loc}\"${easyshell:line_separator}"));
        // Linux KDE Konsole
        list.add(new PresetData(OS.osLinux, "KDE Konsole", PresetType.presetTypeOpen,
                "konsole --workdir ${easyshell:container_loc}"));
        list.add(new PresetData(OS.osLinux, "KDE Konsole", PresetType.presetTypeRun,
                "konsole --workdir ${easyshell:container_loc} --noclose  -e ${easyshell:resource_loc}"));
        // Linux Konqueror
        list.add(new PresetData(OS.osLinux, "Konqueror", PresetType.presetTypeExplore,
                "konqueror file:\"${easyshell:resource_loc}\""));
        // Linux Gnome Terminal
        list.add(new PresetData(OS.osLinux, "Gnome Terminal", PresetType.presetTypeOpen,
                "gnome-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new PresetData(OS.osLinux, "Gnome Terminal", PresetType.presetTypeRun,
                "gnome-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}''"));
        // Linux Xfce Terminal
        list.add(new PresetData(OS.osLinux, "Xfce Terminal", PresetType.presetTypeOpen,
                "xfce4-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new PresetData(OS.osLinux, "Xfce Terminal", PresetType.presetTypeRun,
                "xfce4-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}'' --hold"));
        // Linux Nautilus
        list.add(new PresetData(OS.osLinux, "Nautilus", PresetType.presetTypeExplore,
                "nautilus ${easyshell:resource_loc}"));
        // Linux Dolphin
        list.add(new PresetData(OS.osLinux, "Dolphin", PresetType.presetTypeExplore,
                "dolphin --select ${easyshell:resource_loc}"));
        // Linux Nemo
        list.add(new PresetData(OS.osLinux, "Nemo", PresetType.presetTypeExplore,
                "nemo ${easyshell:resource_loc}"));
        // Linux Thunar
        list.add(new PresetData(OS.osLinux, "Thunar", PresetType.presetTypeExplore,
                "thunar ${easyshell:resource_loc}"));
        // Linux Clipboard
        list.add(new PresetData(OS.osLinux, "Full path", PresetType.presetTypeClipboard,
                "${easyshell:resource_loc}${easyshell:line_separator}"));
        // MAC OS X Terminal
        list.add(new PresetData(OS.osMacOSX, "Terminal", PresetType.presetTypeOpen,
                "open -a Terminal ${easyshell:container_loc}"));
        list.add(new PresetData(OS.osMacOSX, "Terminal", PresetType.presetTypeRun,
                "open -a Terminal ${easyshell:container_loc}"));
        // MAC OS X Finder
        list.add(new PresetData(OS.osMacOSX, "Finder", PresetType.presetTypeExplore,
                "open -R ${easyshell:resource_loc}"));
        // MAC OS X Clipboard
        list.add(new PresetData(OS.osMacOSX, "Full path", PresetType.presetTypeClipboard,
                "${easyshell:resource_loc}${easyshell:line_separator}"));
    }

    public List<PresetData> getAllPresets() {
        return list;
    }

    public List<PresetData> getDefaultPresets() {
        List<PresetData> listAll = getAllPresets();
        List<PresetData> listOS = new ArrayList<PresetData>();
        List<PresetData> listDefault = new ArrayList<PresetData>();
        OS os = getOS();
        // now get all data by OS
        listOS = getPresetData(listAll, os);
        // now get by name
        switch(os)
        {
            case osWindows:
                listDefault.add(getPresetData(listOS, "DOS-Shell", PresetType.presetTypeOpen));
                listDefault.add(getPresetData(listOS, "DOS-Shell", PresetType.presetTypeRun));
                listDefault.add(getPresetData(listOS, "Explorer", PresetType.presetTypeExplore));
                break;
            case osLinux:
                // try to detect the desktop
                LinuxDesktop desktop = detectLinuxDesktop();
                //Activator.getDefault().sysout(true, "Detected linux (Unix) desktop: >" + desktop.getName() + "<");
                switch (desktop) {
                    case desktopKde:    listDefault.add(getPresetData(listOS, "KDE", PresetType.presetTypeOpen));
                                        listDefault.add(getPresetData(listOS, "KDE", PresetType.presetTypeRun));
                                        listDefault.add(getPresetData(listOS, "Dolphin", PresetType.presetTypeExplore));
                    break;
                    case desktopCinnamon:   listDefault.add(getPresetData(listOS, "Gnome", PresetType.presetTypeOpen));
                                            listDefault.add(getPresetData(listOS, "Gnome", PresetType.presetTypeRun));
                                            listDefault.add(getPresetData(listOS, "Nemo", PresetType.presetTypeExplore));
                    break;
                    case desktopGnome:  listDefault.add(getPresetData(listOS, "Gnome", PresetType.presetTypeOpen));
                                        listDefault.add(getPresetData(listOS, "Gnome", PresetType.presetTypeRun));
                                        listDefault.add(getPresetData(listOS, "Nautilus", PresetType.presetTypeExplore));
                    break;
                    case desktopXfce:   listDefault.add(getPresetData(listOS, "Xfce", PresetType.presetTypeOpen));
                                        listDefault.add(getPresetData(listOS, "Xfce", PresetType.presetTypeRun));
                                        listDefault.add(getPresetData(listOS, "Thunar", PresetType.presetTypeExplore));
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
                listDefault.add(getPresetData(listOS, "Terminal", PresetType.presetTypeOpen));
                listDefault.add(getPresetData(listOS, "Terminal", PresetType.presetTypeRun));
                listDefault.add(getPresetData(listOS, "Finder", PresetType.presetTypeExplore));
                break;
        }
        // add clipboard
        listDefault.add(getPresetData(listOS, ".*path", PresetType.presetTypeClipboard));
        return listDefault;
    }

    private OS getOS() {
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

    private static List<PresetData> getPresetData(List<PresetData> list, OS os) {
        List<PresetData> listOut = new ArrayList<PresetData>();
        for (PresetData entry : list) {
            if (entry.getOs() == os) {
                listOut.add(entry);
            }
        }
        return listOut;
    }

    private static PresetData getPresetData(List<PresetData> list, String name, PresetType type) {
        for (PresetData entry : list) {
            if (entry.getType() == type && entry.getName().matches(name)) {
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
