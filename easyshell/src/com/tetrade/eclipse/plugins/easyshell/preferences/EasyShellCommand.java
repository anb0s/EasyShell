/*
 * Copyright (C) 2014 - 2015 by Andre Bossert
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.tetrade.eclipse.plugins.easyshell.preferences;

/**
 * Commands.
 */
public enum EasyShellCommand {
    cmdUnknown(0, 	"Unknown", "shell", "file browser", null,
                    "open ${easyshell:container_loc}",
                    "cd ${easyshell:container_loc} && run ./''${easyshell:resource_name}''",
                    "explore ${easyshell:resource_loc}",
                    "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
    ),
    cmdWinDOS(1, 	"Windows", "DOS-Shell", "Explorer", null,
                    "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} cmd.exe /K",
                    "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} ${easyshell:resource_name}",
                    "explorer.exe /select, ${easyshell:resource_loc}",
                    "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
    ),
    cmdWinPower(2,	"Windows", "PowerShell", "Explorer", null,
                    "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe",
                    "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} powershell.exe -command ./''${easyshell:resource_name}''",
                    "explorer.exe /select, ${easyshell:resource_loc}",
                    "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
    ),
    cmdWinCyg(3,	"Windows", "Cygwin (Bash)", "Explorer", null,
                    "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\"",
                    "cmd.exe /C start \"${easyshell:project_name}\" /D ${easyshell:container_loc} \"C:\\Cygwin\\bin\\bash.exe\" -c ./''${easyshell:resource_name}''",
                    "explorer.exe /select, ${easyshell:resource_loc} ",
                    "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
    ),
    cmdKonsoleKDEKonqueror(4, "Linux", "KDE Konsole", "Konqueror", null,
                    "konsole --noclose --workdir ${easyshell:container_loc}",
                    "konsole --noclose --workdir ${easyshell:container_loc} -e ./''${easyshell:resource_name}''",
                    "konqueror file:\"${easyshell:resource_loc}\"",
                    "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
    ),
    cmdKonsoleGnome(5, "Linux", "Gnome Terminal", "Nautilus", null,
                    "gnome-terminal --working-directory=\"${easyshell:container_loc}\"",
                    "gnome-terminal --working-directory=\"${easyshell:container_loc}\" --command=./''${easyshell:resource_name}''",
                    "nautilus ${easyshell:resource_loc}",
                    "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
    ),
    cmdXtermDtfile(6, "Linux" , "CDE Xterm", "Dtfile", null,
                    "cd ${easyshell:container_loc} && xterm",
                    "cd ${easyshell:container_loc} && xterm -e ./''${easyshell:resource_name}''",
                    "cd ${easyshell:container_loc} && dtfile",
                    "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
    ),
    cmdTerminalFinder(7, "MAC OS X", "Terminal", "Finder", null,
                    "open -a Terminal ${easyshell:container_loc}",
                    "open -a Terminal ${easyshell:resource_loc}",
                    "open -R ${easyshell:resource_loc}",
                    "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
    ),
    cmdKonsoleKDEDolphin(8, "Linux" , "KDE Konsole", "Dolphin", null,
	                "konsole --workdir ${easyshell:container_loc}",
	                "konsole --workdir ${easyshell:container_loc} --noclose -e ${easyshell:resource_loc}",
	                "dolphin --select ${easyshell:resource_loc}",
	                "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
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
    cmdGnomeTermNemo(13, "Linux", "Gnome Terminal", "Nemo", null,
            "gnome-terminal --working-directory=\"${easyshell:container_loc}\"",
            "gnome-terminal --working-directory=\"${easyshell:container_loc}\" --command=./''${easyshell:resource_name}''",
            "nemo ${easyshell:resource_loc}",
            "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
    ),
    // https://sourceforge.net/p/pluginbox/feature-requests/24/
    cmdGnomeTermThunar(14, "Linux", "Gnome Terminal", "Thunar", null,
            "gnome-terminal --working-directory=\"${easyshell:container_loc}\"",
            "gnome-terminal --working-directory=\"${easyshell:container_loc}\" --command=./''${easyshell:resource_name}''",
            "thunar ${easyshell:container_loc}",
            "\"${easyshell:resource_loc}\"${easyshell:line_separator}"
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
    EasyShellCommand(int id, String os, String console, String explorer, String label, String openCmd, String runCmd, String exploreCmd, String copyPathCmd) {
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
    public static EasyShellCommand getFromId(int id) {
        EasyShellCommand ret = cmdUnknown;
        for(int i = 0; i < EasyShellCommand.values().length; i++) {
            if (EasyShellCommand.values()[i].getId() == id) {
                ret = EasyShellCommand.values()[i];
            }
        }
        return ret;
    }
}