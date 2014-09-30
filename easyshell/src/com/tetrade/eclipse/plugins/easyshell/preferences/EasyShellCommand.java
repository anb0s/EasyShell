/*
 * Copyright (C) 2014 by Andre Bossert
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
    cmdUnknown(0, 	"Unknown shell / file browser",
                    "open {1}",
                    "cd {1} && run ./''{3}''",
                    "explore {2}",
                    "{2}{5}"
    ),
    cmdWinDOS(1, 	"Windows DOS-Shell / Explorer",
                    "cmd.exe /C start \"{4}\" /D {1} cmd.exe /K",
                    "cmd.exe /C start \"{4}\" /D {1} {3}",
                    "explorer.exe /select, {2}",
                    "{2}{5}"
    ),
    cmdWinPower(2,	"Windows PowerShell / Explorer",
                    "cmd.exe /C start \"{4}\" /D {1} powershell.exe",
                    "cmd.exe /C start \"{4}\" /D {1} powershell.exe -command ./''{3}''",
                    "explorer.exe /select, {2}",
                    "{2}{5}"
    ),
    cmdWinCyg(3,	"Windows Cygwin (Bash) / Explorer",
                    "cmd.exe /C start \"{4}\" /D {1} \"C:\\Cygwin\\bin\\bash.exe\"",
                    "cmd.exe /C start \"{4}\" /D {1} \"C:\\Cygwin\\bin\\bash.exe\" -c ./''{3}''",
                    "explorer.exe /select, {2} ",
                    "{2}{5}"
    ),
    cmdKonsoleKDEKonqueror(4, "KDE Konsole / Konqueror",
                    "konsole --noclose --workdir {1}",
                    "konsole --noclose --workdir {1} -e ./''{3}''",
                    "konqueror file:\"{2}\"",
                    "{2}{5}"
    ),
    cmdKonsoleGnome(5, "Gnome Terminal / Nautilus",
                    "gnome-terminal --working-directory=\"{1}\"",
                    "gnome-terminal --working-directory=\"{1}\" --command=./''{3}''",
                    "nautilus {2}",
                    "{2}{5}"
    ),
    cmdXtermDtfile(6, "CDE Xterm / Dtfile",
                    "cd {1} && xterm",
                    "cd {1} && xterm -e ./''{3}''",
                    "cd {1} && dtfile",
                    "{2}{5}"
    ),
    cmdTerminalFinder(7, "MAC OS X Terminal / Finder",
                    "open -a Terminal {1}",
                    "open -a Terminal {2}",
                    "open -R {2}",
                    "{2}{5}"
    ),
    cmdKonsoleKDEDolphin(8, "KDE Konsole / Dolphin",
	                "konsole --workdir {1}",
	                "konsole --workdir {1} --noclose -e {2}",
	                "dolphin --select {2}",
	                "{2}{5}"
    ),
    cmdWinConsole(9, 	"Windows Console / Explorer",
	                "console.exe -w \"{4}\" -d {1}",
	                "console.exe -w \"{4}\" -d {1} -r \"/k\\\"{3}\\\"\"",
	                "explorer.exe /select, {2}",
	                "{2}{5}"
    ),
    cmdWinTotalCommander(10, 	"Windows DOS-Shell / TotalCommander",
	                "cmd.exe /C start \"{4}\" /D {1} cmd.exe /K",
	                "cmd.exe /C start \"{4}\" /D {1} {3}",
	                "totalcmd.exe /O /T {1}",
	                "{2}{5}"
    ),
    cmdWinGitBash(11,	"Windows Git-Bash / Explorer",
	                "cmd.exe /C start \"{4}\" /D {1} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i",
	                "cmd.exe /C start \"{4}\" /D {1} \"C:\\Program Files (x86)\\Git\\bin\\bash.exe\" --login -i -c ./''{3}''",
	                "explorer.exe /select, {2} ",
	                "{2}{5}"
    ),
    cmdWinConEmu(12,	"Windows ConEmu / Explorer",
            "ConEmu.exe /Title \"{4}\" /Dir \"{1}\" /Single /cmd cmd",
            "ConEmu.exe /Title \"{4}\" /Dir \"{1}\" /Single /cmd \"{3}\"",
            "explorer.exe /select, {2} ",
            "{2}{5}"
    );
    // attributes
    private final int id;
    private final String label;
    private final String openCmd;
    private final String runCmd;
    private final String exploreCmd;
    private final String copyPathCmd;
    // construct
    EasyShellCommand(int id, String label, String openCmd, String runCmd, String exploreCmd, String copyPathCmd) {
        this.id = id;
        this.label = label;
        this.openCmd = openCmd;
        this.runCmd = runCmd;
        this.exploreCmd = exploreCmd;
        this.copyPathCmd = copyPathCmd;
    }
    public int getId() {
        return id;
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