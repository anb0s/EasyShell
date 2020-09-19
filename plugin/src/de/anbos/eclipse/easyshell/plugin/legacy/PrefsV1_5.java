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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class PrefsV1_5 {

    /**
     * Commands.
     */
    public enum Command {
        cmdUnknown(0,   "Unknown", "shell", "file browser", null,
                        "open ${easyshell:container_loc}",
                        "cd ${easyshell:container_loc} && run ./''${easyshell:resource_name}''",
                        "explore ${easyshell:resource_loc}",
                        "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
        ),
        cmdWinDOS(1,    "Windows", "DOS-Shell", "Explorer", null,
                        "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} cmd.exe /K",
                        "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} ${easyshell:resource_name}",
                        "explorer.exe /select, ${easyshell:resource_loc}",
                        "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
        ),
        cmdWinPower(2,  "Windows", "PowerShell", "Explorer", null,
                        "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe",
                        "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe -command ./''${easyshell:resource_name}''",
                        "explorer.exe /select, ${easyshell:resource_loc}",
                        "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
        ),
        cmdWinCyg(3,    "Windows", "Cygwin (Bash)", "Explorer", null,
                        "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\"",
                        "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\" -c ./''${easyshell:resource_name}''",
                        "explorer.exe /select, ${easyshell:resource_loc} ",
                        "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
        ),
        cmdKonsoleKDEKonqueror(4, "Linux", "KDE Konsole", "Konqueror", null,
                        "konsole --noclose --workdir ${easyshell:container_loc}",
                        "konsole --noclose --workdir ${easyshell:container_loc} -e ./''${easyshell:resource_name}''",
                        "konqueror file:\"${easyshell:resource_loc}\"",
                        "${easyshell:resource_loc}${easyshell:line_separator}"
        ),
        cmdKonsoleGnome(5, "Linux", "Gnome Terminal", "Nautilus", null,
                        "gnome-terminal --working-directory=${easyshell:container_loc}",
                        "gnome-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}''",
                        "nautilus ${easyshell:resource_loc}",
                        "${easyshell:resource_loc}${easyshell:line_separator}"
        ),
        cmdXtermDtfile(6, "Linux" , "CDE Xterm", "Dtfile", null,
                        "cd ${easyshell:container_loc} && xterm",
                        "cd ${easyshell:container_loc} && xterm -e ./''${easyshell:resource_name}''",
                        "cd ${easyshell:container_loc} && dtfile",
                        "${easyshell:resource_loc}${easyshell:line_separator}"
        ),
        cmdTerminalFinder(7, "MAC OS X", "Terminal", "Finder", null,
                        "open -a Terminal ${easyshell:container_loc}",
                        "open -a Terminal ${easyshell:resource_loc}",
                        "open -R ${easyshell:resource_loc}",
                        "${easyshell:resource_loc}${easyshell:line_separator}"
        ),
        cmdKonsoleKDEDolphin(8, "Linux" , "KDE Konsole", "Dolphin", null,
                        "konsole --workdir ${easyshell:container_loc}",
                        "konsole --workdir ${easyshell:container_loc} --noclose -e ${easyshell:resource_loc}",
                        "dolphin --select ${easyshell:resource_loc}",
                        "${easyshell:resource_loc}${easyshell:line_separator}"
        ),
        cmdWinConsole(9, "Windows", "Console", "Explorer", null,
                        "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc}",
                        "console.exe -w \"${easyshell:project_name}\" -d ${easyshell:container_loc} -r \"/k\\\"${easyshell:resource_name}\\\"\"",
                        "explorer.exe /select, ${easyshell:resource_loc}",
                        "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
        ),
        cmdWinTotalCommander(10, "Windows", "DOS-Shell", "TotalCommander", null,
                        "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} cmd.exe /K",
                        "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} ${easyshell:resource_name}",
                        "totalcmd.exe /O /T ${easyshell:container_loc}",
                        "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
        ),
        cmdWinGitBash(11, "Windows", "Git-Bash", "Explorer", null,
                        "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i",
                        "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i -c ./''${easyshell:resource_name}''",
                        "explorer.exe /select, ${easyshell:resource_loc} ",
                        "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
        ),
        cmdWinConEmu(12, "Windows", "ConEmu", "Explorer", null,
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd cmd",
                "ConEmu.exe /Title \"${easyshell:project_name}\" /Dir \"${easyshell:container_loc}\" /Single /cmd \"${easyshell:resource_name}\"",
                "explorer.exe /select, ${easyshell:resource_loc} ",
                "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
        ),
        // https://sourceforge.net/p/pluginbox/feature-requests/18/
        // https://github.com/anb0s/EasyShell/issues/17
        cmdGnomeTermNemo(13, "Linux", "Gnome Terminal", "Nemo", null,
                "gnome-terminal --working-directory=${easyshell:container_loc}",
                "gnome-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}''",
                "nemo ${easyshell:resource_loc}",
                "${easyshell:resource_loc}${easyshell:line_separator}"
        ),
        // https://sourceforge.net/p/pluginbox/feature-requests/24/
        // https://github.com/anb0s/EasyShell/issues/23
        cmdGnomeTermThunar(14, "Linux", "Gnome Terminal", "Thunar", null,
                "gnome-terminal --working-directory=${easyshell:container_loc}",
                "gnome-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}''",
                "thunar ${easyshell:container_loc}",
                "${easyshell:resource_loc}${easyshell:line_separator}"
        ),
        // https://sourceforge.net/p/pluginbox/feature-requests/22/
        // https://github.com/anb0s/EasyShell/issues/21
        cmdXfceTermThunar(15, "Linux", "Xfce Terminal", "Thunar", null,
                "xfce4-terminal --working-directory=${easyshell:container_loc}",
                "xfce4-terminal --working-directory=${easyshell:container_loc} --command=./''${easyshell:resource_name}'' --hold",
                "thunar ${easyshell:container_loc}",
                "${easyshell:resource_loc}${easyshell:line_separator}"
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
        public String getOS() {
            return os;
        }
        public String getConsole() {
            return console;
        }
        public String getExplorer() {
            return explorer;
        }
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
        public static Command getFromId(int id) {
            Command ret = cmdUnknown;
            for(int i = 0; i < Command.values().length; i++) {
                if (Command.values()[i].getId() == id) {
                    ret = Command.values()[i];
                }
            }
            return ret;
        }
    }

    /**
     * Constructs the preference page.
     */
    public PrefsV1_5() {
    }

    /**
     * Sets the default values of the preferences.
     */
    public static void initializeDefaults(IPreferenceStore store, Command cmd, int instId) {
        // set default commands
        store.setDefault(PreferenceEntry.preferenceTargetEnabled.getString(instId), false);
        store.setDefault(PreferenceEntry.preferenceTargetOpen.getString(instId), cmd.getOpenCmd());
        store.setDefault(PreferenceEntry.preferenceTargetRun.getString(instId), cmd.getRunCmd());
        store.setDefault(PreferenceEntry.preferenceTargetExplore.getString(instId), cmd.getExploreCmd());
        store.setDefault(PreferenceEntry.preferenceTargetCopyPath.getString(instId), cmd.getCopyPathCmd());
        // set default selected preset
        store.setDefault(PreferenceEntry.preferenceListId.getString(instId), cmd.getId() - 1);
        store.setDefault(PreferenceEntry.preferenceListString.getString(instId), cmd.name());
        // set default
        store.setDefault(PreferenceEntry.preferenceQuotes.getString(instId), Quotes.quotesNo.name());
        store.setDefault(PreferenceEntry.preferenceDebug.getString(instId), Debug.debugNo.name());
        store.setDefault(PreferenceEntry.preferenceTokenizer.getString(instId), EasyShellTokenizer.EasyShellTokenizerYes.name());
    }

    public static List<String> getPreferenceList() {
        List<String> list = new ArrayList<String>();
        list.add(PreferenceEntry.preferenceTargetEnabled.getString());
        //list.add(PreferenceEntry.preferenceListId.getString());
        //list.add(PreferenceEntry.preferenceListString.getString());
        //list.add(PreferenceEntry.preferenceTargetOpen.getString());
        //list.add(PreferenceEntry.preferenceTargetRun.getString());
        //list.add(PreferenceEntry.preferenceTargetExplore.getString());
        //list.add(PreferenceEntry.preferenceTargetCopyPath.getString());
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
        /*
        DebugStr=debugYes
        DebugStr1=debugNo
        DebugStr2=debugNo
        IdStr1=cmdWinPower
        IdStr2=cmdWinCyg
        QuotesStr=quotesAuto
        QuotesStr1=quotesNo
        QuotesStr2=quotesNo
        TokenizerStr=EasyShellTokenizerNo
        TokenizerStr1=EasyShellTokenizerYes
        TokenizerStr2=EasyShellTokenizerYes
        eclipse.preferences.version=1
        listPreference1=1
        listPreference2=2
        targetCopyPathPreference={2}{5} copy
        targetEnabled=true
        targetEnabled1=true
        targetEnabled2=true
        targetExplorePreference=explorer.exe /select, {2} my
        targetExplorePreference2=explorer.exe /select, ${easyshell\:resource_loc}
        targetPreference=cmd.exe /C start "{4}" /D {1} cmd.exe /K test
        targetPreference1=cmd.exe /C start "${easyshell\:project_name}" /D ${easyshell\:container_loc} powershell.exe
        targetPreference2=cmd.exe /C start "${easyshell\:project_name}" /D ${easyshell\:container_loc} "C\:\\Cygwin\\bin\\bash.exe"
        targetRunPreference=cmd.exe /C start "{4}" /D {1} {3} hello
        targetRunPreference1=cmd.exe /C start "${easyshell\:project_name}" /D ${easyshell\:container_loc} powershell.exe -command ./''${easyshell\:resource_name}''
        targetRunPreference2=cmd.exe /C start "${easyshell\:project_name}" /D ${easyshell\:container_loc} "C\:\\Cygwin\\bin\\bash.exe" -c ./''${easyshell\:resource_name}''
        */
        // get proper command (detect)
        Command cmdProper = getProperCommand();
        // duplicate cache
        List<String> openCmdList = new ArrayList<String>();
        List<String> runCmdList = new ArrayList<String>();
        List<String> exploreCmdList = new ArrayList<String>();
        List<String> copyPathList = new ArrayList<String>();
        // iterate over the instances
        for (int instanceId=0;instanceId<3;instanceId++) {
            // set defaults first
            initializeDefaults(store, cmdProper, instanceId);
            // get the properties now
            if (store.getBoolean(PreferenceEntry.preferenceTargetEnabled.getString(instanceId))) {
                final String postfix = " (" + Version.v1_5.getName() + ")";
                String IdStr = store.getString(PreferenceEntry.preferenceListString.getString(instanceId));
                Command command = Command.valueOf(IdStr);
                int position = menuDataList.size();
                // open
                String openCmd = store.getString(PreferenceEntry.preferenceTargetOpen.getString(instanceId));
                if (!openCmdList.contains(openCmd)) {
                    openCmdList.add(openCmd);
                    CommandData cmdDataOpen = new CommandData(null, PresetType.presetUser, os, command.getConsole(), ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpaces, PrefsV1_4.migrateCommandVariables(openCmd));
                    cmdDataList.add(cmdDataOpen);
                    MenuData menuDataOpen = new MenuData(cmdDataOpen.getId(), true, MenuNameType.menuNameTypeOpenHere, null, null, cmdDataOpen.getId());
                    menuDataOpen.setPosition(position++);
                    menuDataOpen.setNamePattern(menuDataOpen.getNamePattern() + postfix);
                    menuDataOpen.setNameType(MenuNameType.menuNameTypeUser);
                    menuDataList.add(menuDataOpen);
                }
                // run
                String runCmd = store.getString(PreferenceEntry.preferenceTargetRun.getString(instanceId));
                if (!runCmdList.contains(runCmd)) {
                    runCmdList.add(runCmd);
                    CommandData cmdDataRun = new CommandData(null, PresetType.presetUser, os, command.getConsole(), ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpaces, PrefsV1_4.migrateCommandVariables(runCmd));
                    cmdDataList.add(cmdDataRun);
                    MenuData menuDataRun = new MenuData(cmdDataRun.getId(), true, MenuNameType.menuNameTypeRunWith, null, null, cmdDataRun.getId());
                    menuDataRun.setPosition(position++);
                    menuDataRun.setNamePattern(menuDataRun.getNamePattern() + postfix);
                    menuDataRun.setNameType(MenuNameType.menuNameTypeUser);
                    menuDataList.add(menuDataRun);
                }
                // explore
                String exploreCmd = store.getString(PreferenceEntry.preferenceTargetExplore.getString(instanceId));
                if (!exploreCmdList.contains(exploreCmd)) {
                    exploreCmdList.add(exploreCmd);
                    CommandData cmdDataExplore = new CommandData(null, PresetType.presetUser, os, command.getExplorer(), ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryExplore, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpaces, PrefsV1_4.migrateCommandVariables(exploreCmd));
                    cmdDataList.add(cmdDataExplore);
                    MenuData menuDataExplore = new MenuData(cmdDataExplore.getId(), true, MenuNameType.menuNameTypeShowIn, null, null, cmdDataExplore.getId());
                    menuDataExplore.setPosition(position++);
                    menuDataExplore.setNamePattern(menuDataExplore.getNamePattern() + postfix);
                    menuDataExplore.setNameType(MenuNameType.menuNameTypeUser);
                    menuDataList.add(menuDataExplore);
                }
                // copy to clipboard
                String copyPathCmd = store.getString(PreferenceEntry.preferenceTargetCopyPath.getString(instanceId));
                if (!copyPathList.contains(copyPathCmd)) {
                    copyPathList.add(copyPathCmd);
                    CommandData cmdDataCopyPath = new CommandData(null, PresetType.presetUser, os, "Full Path", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryClipboard, CommandType.commandTypeClipboard, CommandTokenizer.commandTokenizerSpaces, PrefsV1_4.migrateCommandVariables(copyPathCmd));
                    cmdDataList.add(cmdDataCopyPath);
                    MenuData menuDataCopyPath = new MenuData(cmdDataCopyPath.getId(), true, MenuNameType.menuNameTypeCopyToClipboard, null, null, cmdDataCopyPath.getId());
                    menuDataCopyPath.setPosition(position++);
                    menuDataCopyPath.setNamePattern(menuDataCopyPath.getNamePattern() + postfix);
                    menuDataCopyPath.setNameType(MenuNameType.menuNameTypeUser);
                    menuDataList.add(menuDataCopyPath);
                }
                /*
                String QuotesStr = store.getString(PreferenceEntry.preferenceQuotes.getString(instanceId));
                Quotes quotes = Quotes.valueOf(QuotesStr);
                String DebugStr = store.getString(PreferenceEntry.preferenceDebug.getString(instanceId));
                Debug debug = Debug.valueOf(DebugStr);
                String TokenizerStr = store.getString(PreferenceEntry.preferenceTokenizer.getString(instanceId));
                Tokenizer tokenizer = Tokenizer.valueOf(TokenizerStr);
                */
            }
        }
        return true;
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
            // try to detect the desktop
            LinuxDesktop desktop = detectLinuxDesktop();
            //Activator.getDefault().sysout(true, "Detected linux (Unix) desktop: >" + desktop.getName() + "<");
            switch (desktop) {
                case desktopKde: cmd = Command.cmdKonsoleKDEDolphin; break;
                case desktopCinnamon: cmd = Command.cmdGnomeTermNemo; break;
                case desktopGnome: cmd = Command.cmdKonsoleGnome; break;
                case desktopCde: cmd = Command.cmdXtermDtfile; break;
                case desktopXfce: cmd = Command.cmdXfceTermThunar; break;
                default: cmd = Command.cmdUnknown;
            }
        }
        return cmd;
    }

    private static LinuxDesktop detectLinuxDesktop() {
        LinuxDesktop resultCode = detectDesktopSession();
        if (resultCode == LinuxDesktop.desktopUnknown)
        {
            if (isCde())
                resultCode = LinuxDesktop.desktopCde;
        }
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
     * TODO: detects CDE desktop
     */
    private static boolean isCde() {
        return false;
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
