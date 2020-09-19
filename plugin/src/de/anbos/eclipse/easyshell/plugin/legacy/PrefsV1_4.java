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

package de.anbos.eclipse.easyshell.plugin.legacy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import de.anbos.eclipse.easyshell.plugin.preferences.CommandData;
import de.anbos.eclipse.easyshell.plugin.preferences.CommandDataList;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataList;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.MenuNameType;
import de.anbos.eclipse.easyshell.plugin.types.OS;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.CommandTokenizer;
import de.anbos.eclipse.easyshell.plugin.types.Version;

public class PrefsV1_4 {

    /**
     * Commands.
     */
    private enum Command {
        cmdUnknown(0, 	"Unknown", "shell", "file browser", null,
                        "open {1}",
                        "cd {1} && run ./''{3}''",
                        "explore {2}",
                        "{2}{5}"
        ),
        cmdWinDOS(1, 	"Windows", "DOS-Shell", "Explorer", null,
                        "cmd.exe /C start \"{4}\" /D {1} cmd.exe /K",
                        "cmd.exe /C start \"{4}\" /D {1} {3}",
                        "explorer.exe /select, {2}",
                        "{2}{5}"
        ),
        cmdWinPower(2,	"Windows", "PowerShell", "Explorer", null,
                        "cmd.exe /C start \"{4}\" /D {1} powershell.exe",
                        "cmd.exe /C start \"{4}\" /D {1} powershell.exe -command ./''{3}''",
                        "explorer.exe /select, {2}",
                        "{2}{5}"
        ),
        cmdWinCyg(3,	"Windows", "Cygwin (Bash)", "Explorer", null,
                        "cmd.exe /C start \"{4}\" /D {1} \"C:\\Cygwin\\bin\\bash.exe\"",
                        "cmd.exe /C start \"{4}\" /D {1} \"C:\\Cygwin\\bin\\bash.exe\" -c ./''{3}''",
                        "explorer.exe /select, {2} ",
                        "{2}{5}"
        ),
        cmdKonsoleKDEKonqueror(4, "Linux", "KDE Konsole", "Konqueror", null,
                        "konsole --noclose --workdir {1}",
                        "konsole --noclose --workdir {1} -e ./''{3}''",
                        "konqueror file:\"{2}\"",
                        "{2}{5}"
        ),
        cmdKonsoleGnome(5, "Linux", "Gnome Terminal", "Nautilus", null,
                        "gnome-terminal --working-directory=\"{1}\"",
                        "gnome-terminal --working-directory=\"{1}\" --command=./''{3}''",
                        "nautilus {2}",
                        "{2}{5}"
        ),
        cmdXtermDtfile(6, "Linux", "CDE Xterm", "Dtfile", null,
                        "cd {1} && xterm",
                        "cd {1} && xterm -e ./''{3}''",
                        "cd {1} && dtfile",
                        "{2}{5}"
        ),
        cmdTerminalFinder(7, "MAC OS X", "Terminal", "Finder", null,
                        "open -a Terminal {1}",
                        "open -a Terminal {2}",
                        "open -R {2}",
                        "{2}{5}"
        ),
        cmdKonsoleKDEDolphin(8, "Linux", "KDE Konsole", "Dolphin", null,
                        "konsole --workdir {1}",
                        "konsole --workdir {1} --noclose -e {2}",
                        "dolphin --select {2}",
                        "{2}{5}"
        ),
        cmdWinConsole(9, 	"Windows", "Console", "Explorer", null,
                        "console.exe -w \"{4}\" -d {1}",
                        "console.exe -w \"{4}\" -d {1} -r \"/k\\\"{3}\\\"\"",
                        "explorer.exe /select, {2}",
                        "{2}{5}"
        ),
        cmdWinTotalCommander(10, 	"Windows", "DOS-Shell", "TotalCommander", null,
                        "cmd.exe /C start \"{4}\" /D {1} cmd.exe /K",
                        "cmd.exe /C start \"{4}\" /D {1} {3}",
                        "totalcmd.exe /O /T {1}",
                        "{2}{5}"
        ),
        cmdWinGitBash(11,	"Windows", "Git-Bash", "Explorer", null,
                        "cmd.exe /C start \"{4}\" /D {1} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i",
                        "cmd.exe /C start \"{4}\" /D {1} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i -c ./''{3}''",
                        "explorer.exe /select, {2} ",
                        "{2}{5}"
        ),
        cmdWinConEmu(12,	"Windows", "ConEmu", "Explorer", null,
                "ConEmu.exe /Title \"{4}\" /Dir \"{1}\" /Single /cmd cmd",
                "ConEmu.exe /Title \"{4}\" /Dir \"{1}\" /Single /cmd \"{3}\"",
                "explorer.exe /select, {2} ",
                "{2}{5}"
        );
        // attributes
        private final int id;
        private final String os;
        private final String console;
        private final String explorer;
        private final String label;
        private final String openCmd;
        private final String runCmd;
        private final String exploreCmd;
        private final String copyPathCmd;
        // construct
        Command(int id, String os, String console, String explorer, String label, String openCmd, String runCmd, String exploreCmd, String copyPathCmd) {
            this.id = id;
            this.os = os;
            this.console  = console;
            this.explorer = explorer;
            if (label != null) {
                this.label = label;
            } else {
                this.label = os + " " + console + " / " + explorer;
            }
            this.openCmd = openCmd;
            this.runCmd = runCmd;
            this.exploreCmd = exploreCmd;
            this.copyPathCmd = copyPathCmd;
        }
        public int getId() {
            return id;
        }
        @SuppressWarnings("unused")
        public String getOS() {
            return os;
        }
        public String getConsole() {
            return console;
        }
        public String getExplorer() {
            return explorer;
        }
        @SuppressWarnings("unused")
        public String getLabel() {
            return label;
        }
        public String getOpenCmd() {
            return openCmd;
        }
        public String getRunCmd() {
            return runCmd;
        }
        public String getExploreCmd() {
            return exploreCmd;
        }
        public String getCopyPathCmd() {
            return copyPathCmd;
        }
        @SuppressWarnings("unused")
        static Command getFromId(int id) {
            Command ret = cmdUnknown;
            for(int i = 0; i < Command.values().length; i++) {
                if (Command.values()[i].getId() == id) {
                    ret = Command.values()[i];
                }
            }
            return ret;
        }
    }

    public PrefsV1_4() {
    }

    /**
     * Sets the default values of the preferences.
     */
    private static void initializeDefaults(IPreferenceStore store, Command cmd) {
        // set default commands
        store.setDefault(PreferenceEntry.preferenceTargetOpen.getString(), cmd.getOpenCmd());
        store.setDefault(PreferenceEntry.preferenceTargetRun.getString(), cmd.getRunCmd());
        store.setDefault(PreferenceEntry.preferenceTargetExplore.getString(), cmd.getExploreCmd());
        store.setDefault(PreferenceEntry.preferenceTargetCopyPath.getString(), cmd.getCopyPathCmd());
        // set default selected preset
        store.setDefault(PreferenceEntry.preferenceListId.getString(), cmd.getId() - 1);
        store.setDefault(PreferenceEntry.preferenceListString.getString(), cmd.name());
        // set default
        store.setDefault(PreferenceEntry.preferenceQuotes.getString(), Quotes.quotesNo.name());
        store.setDefault(PreferenceEntry.preferenceDebug.getString(), Debug.debugNo.name());
        store.setDefault(PreferenceEntry.preferenceTokenizer.getString(), EasyShellTokenizer.EasyShellTokenizerYes.name());
    }

    public static List<String> getPreferenceList() {
        List<String> list = new ArrayList<String>();
        //list.add(PreferenceEntry.preferenceListId.getString());
        list.add(PreferenceEntry.preferenceListString.getString());
        list.add(PreferenceEntry.preferenceTargetOpen.getString());
        list.add(PreferenceEntry.preferenceTargetRun.getString());
        list.add(PreferenceEntry.preferenceTargetExplore.getString());
        list.add(PreferenceEntry.preferenceTargetCopyPath.getString());
        //list.add(PreferenceEntry.preferenceQuotes.getString());
        //list.add(PreferenceEntry.preferenceDebug.getString());
        //list.add(PreferenceEntry.preferenceTokenizer.getString());
        return list;
    }

    /**
     * Loads the preference store and sets the data to controls.
     *
     * @return store loaded.
     */
    public static boolean loadStore(IPreferenceStore store, OS os, CommandDataList cmdDataList, MenuDataList menuDataList) {
        // get proper command (detect)
        Command cmdProper = getProperCommand();
        // set defaults first
        initializeDefaults(store, cmdProper);
        // get the properties now
        final String postfix = " (" + Version.v1_4.getName() + ")";
        String IdStr = store.getString(PreferenceEntry.preferenceListString.getString());
        Command command = Command.valueOf(IdStr);
        int position = menuDataList.size();
        // open
        String openCmd = store.getString(PreferenceEntry.preferenceTargetOpen.getString());
        CommandData cmdDataOpen = new CommandData(null, PresetType.presetUser, os, command.getConsole(), ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpaces, migrateCommandVariables(openCmd));
        cmdDataList.add(cmdDataOpen);
        MenuData menuDataOpen = new MenuData(cmdDataOpen.getId(), true, MenuNameType.menuNameTypeOpenHere, null, null, cmdDataOpen.getId());
        menuDataOpen.setPosition(position++);
        menuDataOpen.setNamePattern(menuDataOpen.getNamePattern() + postfix);
        menuDataOpen.setNameType(MenuNameType.menuNameTypeUser);
        menuDataList.add(menuDataOpen);
        // run
        String runCmd = store.getString(PreferenceEntry.preferenceTargetRun.getString());
        CommandData cmdDataRun = new CommandData(null, PresetType.presetUser, os, command.getConsole(), ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpaces, migrateCommandVariables(runCmd));
        cmdDataList.add(cmdDataRun);
        MenuData menuDataRun = new MenuData(cmdDataRun.getId(), true, MenuNameType.menuNameTypeRunWith, null, null, cmdDataRun.getId());
        menuDataRun.setPosition(position++);
        menuDataRun.setNamePattern(menuDataRun.getNamePattern() + postfix);
        menuDataRun.setNameType(MenuNameType.menuNameTypeUser);
        menuDataList.add(menuDataRun);
        // explore
        String exploreCmd = store.getString(PreferenceEntry.preferenceTargetExplore.getString());
        CommandData cmdDataExplore = new CommandData(null, PresetType.presetUser, os, command.getExplorer(), ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryExplore, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpaces, migrateCommandVariables(exploreCmd));
        cmdDataList.add(cmdDataExplore);
        MenuData menuDataExplore = new MenuData(cmdDataExplore.getId(), true, MenuNameType.menuNameTypeShowIn, null, null, cmdDataExplore.getId());
        menuDataExplore.setPosition(position++);
        menuDataExplore.setNamePattern(menuDataExplore.getNamePattern() + postfix);
        menuDataExplore.setNameType(MenuNameType.menuNameTypeUser);
        menuDataList.add(menuDataExplore);
        // copy to clipboard
        String copyPathCmd = store.getString(PreferenceEntry.preferenceTargetCopyPath.getString());
        CommandData cmdDataCopyPath = new CommandData(null, PresetType.presetUser, os, "Full Path", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerSpaces, migrateCommandVariables(copyPathCmd));
        cmdDataList.add(cmdDataCopyPath);
        MenuData menuDataCopyPath = new MenuData(cmdDataCopyPath.getId(), true, MenuNameType.menuNameTypeCopyToClipboard, null, null, cmdDataCopyPath.getId());
        menuDataCopyPath.setPosition(position++);
        menuDataCopyPath.setNamePattern(menuDataCopyPath.getNamePattern() + postfix);
        menuDataCopyPath.setNameType(MenuNameType.menuNameTypeUser);
        menuDataList.add(menuDataCopyPath);
        /*
        String QuotesStr = store.getString(PreferenceEntry.preferenceQuotes.getString());
        Quotes quotes = Quotes.valueOf(QuotesStr);
        String DebugStr = store.getString(PreferenceEntry.preferenceDebug.getString());
        Debug debug = Debug.valueOf(DebugStr);
        String TokenizerStr = store.getString(PreferenceEntry.preferenceTokenizer.getString());
        Tokenizer tokenizer = Tokenizer.valueOf(TokenizerStr);
        */
        return true;
    }

    public static String migrateCommandVariables(String cmdString) {
        /*
        {0} == ${easyshell:drive}
        {1} == ${easyshell:container_loc}
        {2} == ${easyshell:resource_loc}
        {3} == ${easyshell:resource_name}
        {4} == ${easyshell:project_name}
        {5} == ${easyshell:line_separator}
        */
        String migratedString = cmdString;
        migratedString = migratedString.replace("{0}", "${easyshell:drive}");
        migratedString = migratedString.replace("{1}", "${easyshell:container_loc}");
        migratedString = migratedString.replace("{2}", "${easyshell:resource_loc}");
        migratedString = migratedString.replace("{3}", "${easyshell:resource_name}");
        migratedString = migratedString.replace("{4}", "${easyshell:project_name}");
        migratedString = migratedString.replace("{5}", "${easyshell:line_separator}");
        return migratedString;
    }

    private static Command getProperCommand() {
        Command cmd = Command.cmdUnknown;
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
            cmd = Command.cmdWinDOS;
        } else if (osname.indexOf("mac os x") != -1) {
            cmd = Command.cmdTerminalFinder;
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
            LinuxDesktop desktop = detectDesktopNew(); // old: detectDesktop()
            if(desktop == LinuxDesktop.desktopKde) {
                cmd = Command.cmdKonsoleKDEDolphin;
            } else if(desktop == LinuxDesktop.desktopGnome) {
                cmd = Command.cmdKonsoleGnome;
            } else if(desktop == LinuxDesktop.desktopCde) {
                cmd = Command.cmdXtermDtfile;
            }
        }
        return cmd;
    }

    private static LinuxDesktop detectDesktopNew() {
        LinuxDesktop resultCode = LinuxDesktop.desktopUnknown;
        if (isKDE()) {
            resultCode = LinuxDesktop.desktopKde;
        } else if (isGnome()) {
            resultCode = LinuxDesktop.desktopGnome;
        }
        return resultCode;
    }

    /**
     * detects KDE desktop
     *
     * @see http://techbase.kde.org/KDE_System_Administration/Environment_Variables#KDE_FULL_SESSION
     */
    private static boolean isKDE() {
        boolean kde = false;
        String[] cmd = new String[2];
        cmd [0] = "echo";
        cmd [1] = "$KDE_FULL_SESSION";
        kde = isExpectedCommandOutput(cmd, "true", true);
        // if not found try another env
        if (!kde) {
            cmd [1] = "$DESKTOP_SESSION";
            kde = isExpectedCommandOutput(cmd, "kde", true);
        }
        return kde;
    }

    /**
     * detects Gnome desktop
     */
    private static boolean isGnome() {
        boolean gnome = false;
        String[] cmd = new String[2];
        cmd [0] = "echo";
        cmd [1] = "$GNOME_DESKTOP_SESSION_ID";
        gnome = isExpectedCommandOutput(cmd, "*", true);
        // if not found try another env
        if (!gnome) {
            cmd [1] = "$DESKTOP_SESSION";
            gnome = isExpectedCommandOutput(cmd, "gnome", true);
        }
        return gnome;
    }

    /**
     * Detects which desktop is used on a unix / linux system.
     *
     * @todo use regex
     *
     * @return The type of desktop.
     * @see detectDesktop
     */
    private static boolean isExpectedCommandOutput(String[] command, String expectedOutput, boolean toLowerCase) {
        boolean found = false;
        try {
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while((line = in.readLine()) != null && !found) {
                // in case of * just something should be returned
                if (expectedOutput.indexOf("*") != -1)
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
                    if(line.indexOf(expectedOutput) != -1) {
                        found = true;
                    }
                }
            }
            BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            line = null;
            // If there is any error output, print it to
            // stdout for debugging purposes
            while((line = err.readLine()) != null) {
                //EasyShellPlugin.getDefault().sysout(true, "detectDesktop stderr >" + line + "<");
            }

            int result = proc.waitFor();
            if(result != 0) {
                // If there is any error code, print it to
                // stdout for debugging purposes
                //EasyShellPlugin.getDefault().sysout(true, "detectDesktop return code: " + result);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return found;
    }

}
