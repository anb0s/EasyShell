/*
 * Copyright (C) 2004 - 2011 by Marcel Schoen and Andre Bossert
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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tetrade.eclipse.plugins.easyshell.EasyShellPlugin;

/**
 * Preference for program used by Easy Explore
 */

public class EasyShellPreferencePage
    extends PreferencePage
    implements IWorkbenchPreferencePage {

	private boolean debug = false;

    public static final String P_TARGET = "targetPreference";
    public static final String P_TARGET_RUN = "targetRunPreference";
    public static final String P_TARGET_EXPLORE = "targetExplorePreference";
    public static final String P_TARGET_COPYPATH = "targetCopyPathPreference";
    public static final String P_LIST = "listPreference";
    public static final String P_LIST_STR = "IdStr";

    //public static final int numConfigs = 6;

    // Commands
    private enum EasyShellCommand {
        cmdUnknown(0, "Unknown shell / file browser", 		"open {1}", 											"cd {1} && run {3}",													"explore {2}",					"{2}{5}"),
        cmdWinDOS(1, "Windows DOS-Shell / Explorer", 		"cmd.exe /C start \"{4}\" /D\"{1}\" cmd.exe /K", 		"cmd.exe /C start \"{4}\" /D\"{1}\" \"{3}\"", 							"explorer.exe /select,\"{2}\"",	"{2}{5}"),
        cmdWinPower(2, "Windows PowerShell / Explorer", 	"cmd.exe /C start \"{4}\" /D\"{1}\" powershell.exe",	"cmd.exe /C start \"{4}\" /D\"{1}\" powershell.exe -command \"./{3}\"", "explorer.exe /select,\"{2}\"", "{2}{5}"),
        cmdWinCyg(3, "Windows Cygwin (Bash) / Explorer", 	"cmd.exe /C start \"{4}\" /D\"{1}\" bash.exe", 			"cmd.exe /C start \"{4}\" /D\"{1}\" bash.exe -c \"./{3}\"", 			"explorer.exe /select,\"{2}\"", "{2}{5}"),
        cmdKonsoleKDE(4, "KDE Konsole / Konqueror", 		"konsole --noclose --workdir {1}", 						"konsole --noclose --workdir {1} -e ./{3}", 							"konqueror file:{2}", 			"{2}{5}"),
        cmdKonsoleGnome(5, "Gnome Terminal / Nautilus", 	"gnome-terminal --working-directory={1}", 				"gnome-terminal --working-directory={1} --command=./{3}", 				"nautilus {2}", 				"{2}{5}"),
        cmdXtermDtfile(6, "CDE Xterm / Dtfile", 			"cd {1} && xterm", 										"cd {1} && xterm -e ./{3}", 											"cd {1} && dtfile", 			"{2}{5}"),
        cmdTerminalFinder(7, "MAC OS X Terminal / Finder",	"open -a Terminal {1}", 								"cd {1} && open {3}",													"open -a Finder {2}",			"{2}{5}");
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
		static EasyShellCommand getCommandFromId(int id) {
			EasyShellCommand ret = cmdUnknown;
	        for(int i = 0; i < EasyShellCommand.values().length; i++) {
	        	if (EasyShellCommand.values()[i].getId() == id) {
	        		ret = EasyShellCommand.values()[i];
	        	}
	        }
	        return ret;
		}
    }

    private Combo targetCombo = null;
    private StringFieldEditor targetOpenEditor = null;
    private StringFieldEditor targetRunEditor = null;
    private StringFieldEditor targetExploreEditor = null;
    private StringFieldEditor targetCopyPathEditor = null;

	// for unixes
    private static final int DESKTOP_CDE = 0;
	private static final int DESKTOP_KDE = 1;
	private static final int DESKTOP_GNOME = 2;

    public EasyShellPreferencePage() {
    	debug = EasyShellPlugin.getDefault().isDebug();
        setPreferenceStore(EasyShellPlugin.getDefault().getPreferenceStore());
        setDescription("Set up a shell command window.");
        initializeDefaults();
    }

    /**
     * Sets the default values of the preferences.
     */
    private void initializeDefaults() {
        IPreferenceStore store = getPreferenceStore();

        EasyShellCommand cmd = EasyShellCommand.cmdUnknown;
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
        	cmd = EasyShellCommand.cmdWinDOS;
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
            int desktop = detectDesktop();
            if(desktop == DESKTOP_KDE) {
            	cmd = EasyShellCommand.cmdKonsoleKDE;
            } else if(desktop == DESKTOP_GNOME) {
            	cmd = EasyShellCommand.cmdKonsoleGnome;
			} else if(desktop == DESKTOP_CDE) {
				cmd = EasyShellCommand.cmdXtermDtfile;
            }
        } else if (osname.indexOf("mac os x") != -1) {
        	cmd = EasyShellCommand.cmdTerminalFinder;
        }
        // set default commands
        store.setDefault(P_TARGET, cmd.getOpenCmd());
        store.setDefault(P_TARGET_RUN, cmd.getRunCmd());
        store.setDefault(P_TARGET_EXPLORE, cmd.getExploreCmd());
        store.setDefault(P_TARGET_COPYPATH, cmd.getCopyPathCmd());
    }

    /**
     * Creates the field editors. Field editors are abstractions of
     * the common GUI blocks needed to manipulate various types
     * of preferences. Each field editor knows how to save and
     * restore itself.
     */
	protected Control createContents(Composite parent) {

		IPreferenceStore store = getPreferenceStore();

		Composite mainColumn = new Composite(parent, SWT.NONE);
		GridLayout mainLayout = new GridLayout(1, false);
		mainColumn.setFont(parent.getFont());
		mainColumn.setLayout(mainLayout);

		Composite targetColumn = new Composite(mainColumn, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		targetColumn.setFont(parent.getFont());
		targetColumn.setLayout(layout);

        Label label = new Label(targetColumn, 0);
        label.setText("Presets");

        targetCombo = new Combo(targetColumn, SWT.READ_ONLY | SWT.DROP_DOWN);
        for(int i = 0; i < EasyShellCommand.values().length; i++) {
        	targetCombo.add(EasyShellCommand.getCommandFromId(i).getLabel());
        }
        targetCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                refreshTarget();
            }
        });
        targetCombo.select(store.getInt(P_LIST) + 1);
        // get new string value
        String IdStr = store.getString(P_LIST_STR);
        if (IdStr != null && IdStr.length() > 0) {
        	targetCombo.select(EasyShellCommand.valueOf(IdStr).getId());
        }

        targetOpenEditor = new StringFieldEditor(
                P_TARGET,
                "&Shell Open command:",
                targetColumn);
        if (debug) System.out.println("Value: " + store.getString(P_TARGET));
        if (debug) System.out.println("Default: " + store.getDefaultString(P_TARGET));
        targetOpenEditor.setStringValue(store.getString(P_TARGET));

        targetRunEditor = new StringFieldEditor(
                P_TARGET_RUN,
                "&Shell Run command:",
                targetColumn);
        if (debug) System.out.println("Value: " + store.getString(P_TARGET_RUN));
        if (debug) System.out.println("Default: " + store.getDefaultString(P_TARGET_RUN));
        targetRunEditor.setStringValue(store.getString(P_TARGET_RUN));

        targetExploreEditor = new StringFieldEditor(
                P_TARGET_EXPLORE,
                "&Explore command:",
                targetColumn);
        if (debug) System.out.println("Value: " + store.getString(P_TARGET_EXPLORE));
        if (debug) System.out.println("Default: " + store.getDefaultString(P_TARGET_EXPLORE));
        targetExploreEditor.setStringValue(store.getString(P_TARGET_EXPLORE));

        targetCopyPathEditor = new StringFieldEditor(
                P_TARGET_COPYPATH,
                "Copy Path string:",
                targetColumn);
        if (debug) System.out.println("Value: " + store.getString(P_TARGET_COPYPATH));
        if (debug) System.out.println("Default: " + store.getDefaultString(P_TARGET_COPYPATH));
        targetCopyPathEditor.setStringValue(store.getString(P_TARGET_COPYPATH));

        label = new Label(mainColumn, 0);
        label.setText("Argument {0} is the drive letter on Win32.");
        label = new Label(mainColumn, 0);
        label.setText("Argument {1} is the parent path.");
        label = new Label(mainColumn, 0);
        label.setText("Argument {2} is the full path.");
        label = new Label(mainColumn, 0);
        label.setText("Argument {3} is the file name.");
        label = new Label(mainColumn, 0);
        label.setText("Argument {4} is the project name.");
        label = new Label(mainColumn, 0);
        label.setText("Argument {5} is the line separator.");

        return mainColumn;
	}

    /**
     * Refreshes the category.
     */
    private void refreshTarget() {
        int index = targetCombo.getSelectionIndex();
        EasyShellCommand cmd = EasyShellCommand.getCommandFromId(index);
        String textOpen = cmd.getOpenCmd();
        String textRun = cmd.getRunCmd();
        String textExplore = cmd.getExploreCmd();
        String textCopyPath = cmd.getCopyPathCmd();
        if (debug) System.out.println("Set open text to " + textOpen);
        targetOpenEditor.setStringValue(textOpen);
        if (debug) System.out.println("Set run text to " + textRun);
        targetRunEditor.setStringValue(textRun);
        if (debug) System.out.println("Set run text to " + textExplore);
        targetExploreEditor.setStringValue(textExplore);
        if (debug) System.out.println("Set run text to " + textCopyPath);
        targetCopyPathEditor.setStringValue(textCopyPath);
        IPreferenceStore store = getPreferenceStore();
        store.setValue(P_TARGET, textOpen);
        store.setValue(P_TARGET_RUN, textRun);
        store.setValue(P_TARGET_EXPLORE, textExplore);
        store.setValue(P_TARGET_COPYPATH, textCopyPath);
    }

    public boolean performOk() {
        IPreferenceStore store = getPreferenceStore();
        store.setValue(P_TARGET, targetOpenEditor.getStringValue());
        store.setValue(P_TARGET_RUN, targetRunEditor.getStringValue());
        store.setValue(P_TARGET_EXPLORE, targetExploreEditor.getStringValue());
        store.setValue(P_TARGET_COPYPATH, targetCopyPathEditor.getStringValue());
        store.setValue(P_LIST, targetCombo.getSelectionIndex() - 1);
        store.setValue(P_LIST_STR, EasyShellCommand.getCommandFromId(targetCombo.getSelectionIndex()).name());
    	return true;
    }

    public void performDefaults() {
    	initializeDefaults();
    	refreshTarget();
    }

    public void init(IWorkbench workbench) {
    }

	/**
	 * Detects which desktop is used on a unix / linux system.
	 *
	 * @return The type of desktop.
	 */
	private int detectDesktop() {
		int resultCode = -1;
		try {
			String[] cmd = new String[1];
			cmd[0] = "env"; // or echo $DESKTOP_SESSION
			Process proc = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while((line = in.readLine()) != null && resultCode == -1) {
				if(line.indexOf("KDE") != -1) {
					resultCode = DESKTOP_KDE;
				} else if(line.toLowerCase().indexOf("gnome") != -1) {
					resultCode = DESKTOP_GNOME;
				}
			}

			BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			line = null;
			// If there is any error output, print it to
			// stdout for debugging purposes
			while((line = err.readLine()) != null) {
				if (debug) System.out.println("err>> " + line);
			}

			int result = proc.waitFor();
			if(result != 0) {
				// If there is any error code, print it to
				// stdout for debugging purposes
				if (debug) System.out.println("ENV return code: " + result);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		if(resultCode == -1) {
			resultCode = DESKTOP_CDE;
		}
		return resultCode;
	}
}