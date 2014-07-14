/*
 * Copyright (C) 2004 - 2013 by Marcel Schoen and Andre Bossert
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

    /**
     * Strings for preference store.
     */
    public static final String P_TARGET = "targetPreference";
    public static final String P_TARGET_RUN = "targetRunPreference";
    public static final String P_TARGET_EXPLORE = "targetExplorePreference";
    public static final String P_TARGET_COPYPATH = "targetCopyPathPreference";
    public static final String P_LIST = "listPreference";
    public static final String P_LIST_STR = "IdStr";
    public static final String P_QUOTES_LIST_STR = "QuotesStr";
    public static final String P_DEBUG_LIST_STR = "DebugStr";
    public static final String P_TOKENIZER_LIST_STR = "TokenizerStr";

    /**
     * Commands.
     */
    private enum EasyShellCommand {
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
        cmdWinConEmu(11,	"Windows ConEmu / Explorer",
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
        static EasyShellCommand getFromId(int id) {
            EasyShellCommand ret = cmdUnknown;
            for(int i = 0; i < EasyShellCommand.values().length; i++) {
                if (EasyShellCommand.values()[i].getId() == id) {
                    ret = EasyShellCommand.values()[i];
                }
            }
            return ret;
        }
    }

    /**
     * Quotes.
     */
    public enum EasyShellQuotes {
        quotesNo(0, "No"),
        quotesSingle(1, "Single"),
        quotesDouble(2, "Double"),
        quotesAuto(3, "Automatic"); // check if no quotes and space in string, then add
        // attributes
        private final int id;
        private final String mode;
        // construct
        EasyShellQuotes(int id, String mode) {
            this.id = id;
            this.mode = mode;
        }
        public int getId() {
            return id;
        }
        public String getMode() {
            return mode;
        }
        static EasyShellQuotes getFromId(int id) {
            EasyShellQuotes ret = quotesNo;
            for(int i = 0; i < EasyShellQuotes.values().length; i++) {
                if (EasyShellQuotes.values()[i].getId() == id) {
                    ret = EasyShellQuotes.values()[i];
                }
            }
            return ret;
        }
    }

    /**
     * Debug.
     */
    public enum EasyShellDebug {
        debugNo(0, "No"),
        debugYes(1, "Yes");
        // attributes
        private final int id;
        private final String mode;
        // construct
        EasyShellDebug(int id, String mode) {
            this.id = id;
            this.mode = mode;
        }
        public int getId() {
            return id;
        }
        public String getMode() {
            return mode;
        }
        static EasyShellDebug getFromId(int id) {
            EasyShellDebug ret = debugNo;
            for(int i = 0; i < EasyShellDebug.values().length; i++) {
                if (EasyShellDebug.values()[i].getId() == id) {
                    ret = EasyShellDebug.values()[i];
                }
            }
            return ret;
        }
    }

    /**
     * Tokenizer.
     */
    public enum EasyShellTokenizer {
    	EasyShellTokenizerNo(0, "No"),
    	EasyShellTokenizerYes(1, "Yes");
        // attributes
        private final int id;
        private final String mode;
        // construct
        EasyShellTokenizer(int id, String mode) {
            this.id = id;
            this.mode = mode;
        }
        public int getId() {
            return id;
        }
        public String getMode() {
            return mode;
        }
        static EasyShellTokenizer getFromId(int id) {
        	EasyShellTokenizer ret = EasyShellTokenizerYes;
            for(int i = 0; i < EasyShellTokenizer.values().length; i++) {
                if (EasyShellTokenizer.values()[i].getId() == id) {
                    ret = EasyShellTokenizer.values()[i];
                }
            }
            return ret;
        }
    }

    /**
     * Linux desktops.
     */
    private enum LinuxDesktop {
        desktopUnknown(0, "Unknown"),
        desktopCde(1, "CDE"),
        desktopKde(2, "KDE"),
        desktopGnome(3, "Gnome");
        //desktopXfce(4, "Xfce");
        // attributes
        private final int id;
        private final String name;
        // construct
        LinuxDesktop(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        static LinuxDesktop getFromId(int id) {
            LinuxDesktop ret = desktopCde;
            for(int i = 0; i < LinuxDesktop.values().length; i++) {
                if (LinuxDesktop.values()[i].getId() == id) {
                    ret = LinuxDesktop.values()[i];
                }
            }
            return ret;
        }
    }

    /**
     * Preference controls.
     */
    private Combo targetCombo = null;
    private StringFieldEditor targetOpenEditor = null;
    private StringFieldEditor targetRunEditor = null;
    private StringFieldEditor targetExploreEditor = null;
    private StringFieldEditor targetCopyPathEditor = null;
    private Combo quotesCombo = null;
    private Combo debugCombo = null;
    private Combo TokenizerCombo = null;

    /**
     * Sonstructs the preference page.
     */
    public EasyShellPreferencePage() {
        setPreferenceStore(EasyShellPlugin.getDefault().getPreferenceStore());
        setDescription("Set up a shell command window.");
        initializeDefaults();
    }

    /**
     * Sets the default values of the preferences.
     */
    private void initializeDefaults() {
        // get proper command (detect)
        EasyShellCommand cmd = getProperCommand();
        // get store
        IPreferenceStore store = getPreferenceStore();
        // set default commands
        store.setDefault(P_TARGET, cmd.getOpenCmd());
        store.setDefault(P_TARGET_RUN, cmd.getRunCmd());
        store.setDefault(P_TARGET_EXPLORE, cmd.getExploreCmd());
        store.setDefault(P_TARGET_COPYPATH, cmd.getCopyPathCmd());
        // set default selected preset
        store.setDefault(P_LIST, cmd.getId() - 1);
        store.setDefault(P_LIST_STR, cmd.name());
        // set default
        store.setDefault(P_QUOTES_LIST_STR, EasyShellQuotes.quotesNo.name());
        store.setDefault(P_DEBUG_LIST_STR, EasyShellDebug.debugNo.name());
        store.setDefault(P_TOKENIZER_LIST_STR, EasyShellTokenizer.EasyShellTokenizerYes.name());
    }

    /**
     * Creates the field editors. Field editors are abstractions of
     * the common GUI blocks needed to manipulate various types
     * of preferences. Each field editor knows how to save and
     * restore itself.
     *
     * @param parent the parent Composite
     * @return Control
     */
    protected Control createContents(Composite parent) {
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

        // --------------------------------- Presets -------------------------------------------------
        Label label = new Label(targetColumn, 0);
        label.setText("Presets");
        targetCombo = new Combo(targetColumn, SWT.READ_ONLY | SWT.DROP_DOWN);
        for(int i = 0; i < EasyShellCommand.values().length; i++) {
            targetCombo.add(EasyShellCommand.getFromId(i).getLabel());
        }
        targetCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                refreshTarget();
            }
        });
        String targetTooltip = "Please enable string tokenizer to pass parameters automatically to command line (prefered solution - quotes not needed).\nOR/AND use double quotes or two single quotes if you want enclose expanded arguments.";

        // ---------------------------------- Open ---------------------------------------------------
        targetOpenEditor = new StringFieldEditor(
                P_TARGET,
                "&Shell Open command:",
                targetColumn);
        targetOpenEditor.getLabelControl(targetColumn).setToolTipText(targetTooltip);

        // ----------------------------------- Run ---------------------------------------------------
        targetRunEditor = new StringFieldEditor(
                P_TARGET_RUN,
                "&Shell Run command:",
                targetColumn);
        targetRunEditor.getLabelControl(targetColumn).setToolTipText(targetTooltip);

        // --------------------------------- Explore -------------------------------------------------
        targetExploreEditor = new StringFieldEditor(
                P_TARGET_EXPLORE,
                "&Explore command:",
                targetColumn);
        targetExploreEditor.getLabelControl(targetColumn).setToolTipText(targetTooltip);

        // --------------------------------- Copypath ------------------------------------------------
        targetCopyPathEditor = new StringFieldEditor(
                P_TARGET_COPYPATH,
                "Copy Path string:",
                targetColumn);
        targetCopyPathEditor.getLabelControl(targetColumn).setToolTipText(targetTooltip);

        // ------------------------------------ Tokenizer --------------------------------------------
        Label tokenizer = new Label(targetColumn, 0);
        tokenizer.setText("Enable string tokenizer");
        tokenizer.setToolTipText("Constructs a string tokenizer for the specified string. The tokenizer uses the default delimiter set,\nwhich is \" \\t\\n\\r\\f\": the space character, the tab character, the newline character,\nthe carriage-return character, and the form-feed character.\nATTENTION: please use it carefully with Add quotes!");
        TokenizerCombo = new Combo(targetColumn, SWT.READ_ONLY | SWT.DROP_DOWN);
        for(int i = 0; i < EasyShellTokenizer.values().length; i++) {
        	TokenizerCombo.add(EasyShellTokenizer.getFromId(i).getMode());
        }

        // ----------------------------------- Quotes ------------------------------------------------
        Label quotes = new Label(targetColumn, 0);
        quotes.setText("Add quotes to {1}, {2}, {3}*");
        quotes.setToolTipText("Quotes will be added to given parameters.\nATTENTION: please use it carefully with enabled string tokenizer!");
        quotesCombo = new Combo(targetColumn, SWT.READ_ONLY | SWT.DROP_DOWN);
        for(int i = 0; i < EasyShellQuotes.values().length; i++) {
            quotesCombo.add(EasyShellQuotes.getFromId(i).getMode());
        }

        // ------------------------------------ Debug ------------------------------------------------
        Label debug = new Label(targetColumn, 0);
        debug.setText("Enable debug output");
        debug.setToolTipText("Debug messages will be printed to error console.");
        debugCombo = new Combo(targetColumn, SWT.READ_ONLY | SWT.DROP_DOWN);
        for(int i = 0; i < EasyShellDebug.values().length; i++) {
            debugCombo.add(EasyShellDebug.getFromId(i).getMode());
        }

        // ------------------------------------ Description ------------------------------------------
        label = new Label(mainColumn, 0);
        label.setText("Argument {0} is the drive letter on Win32");
        label = new Label(mainColumn, 0);
        label.setText("Argument {1} is the parent path*");
        label = new Label(mainColumn, 0);
        label.setText("Argument {2} is the full path*");
        label = new Label(mainColumn, 0);
        label.setText("Argument {3} is the file name*");
        label = new Label(mainColumn, 0);
        label.setText("Argument {4} is the project name");
        label = new Label(mainColumn, 0);
        label.setText("Argument {5} is the line separator");

        // -------------------------------------------------------------------------------------------
        // now load the stored data
        loadStore();
        // -------------------------------------------------------------------------------------------

        return mainColumn;
    }

    /**
     * Loads the preference store and sets the data to controls.
     *
     * @return store loaded.
     */
    private boolean loadStore() {
        IPreferenceStore store = getPreferenceStore();
        targetCombo.select(store.getInt(P_LIST) + 1); // get index: just for compatibility to old EasyShell version <= 1.2.0
        String IdStr = store.getString(P_LIST_STR); // get name: new method >= 1.3.0
        if (IdStr != null && IdStr.length() > 0) {
            targetCombo.select(EasyShellCommand.valueOf(IdStr).getId());
        }
        EasyShellPlugin.getDefault().sysout(true, "Open Value      : >" + store.getString(P_TARGET) + "<");
        EasyShellPlugin.getDefault().sysout(true, "Open Default    : >" + store.getDefaultString(P_TARGET) + "<");
        targetOpenEditor.setStringValue(store.getString(P_TARGET));
        EasyShellPlugin.getDefault().sysout(true, "Run Value       : >" + store.getString(P_TARGET_RUN) + "<");
        EasyShellPlugin.getDefault().sysout(true, "Run Default     : >" + store.getDefaultString(P_TARGET_RUN) + "<");
        targetRunEditor.setStringValue(store.getString(P_TARGET_RUN));
        EasyShellPlugin.getDefault().sysout(true, "Explore Value   : >" + store.getString(P_TARGET_EXPLORE) + "<");
        EasyShellPlugin.getDefault().sysout(true, "Explore Default : >" + store.getDefaultString(P_TARGET_EXPLORE) + "<");
        targetExploreEditor.setStringValue(store.getString(P_TARGET_EXPLORE));
        EasyShellPlugin.getDefault().sysout(true, "Copypath Value  : >" + store.getString(P_TARGET_COPYPATH) + "<");
        EasyShellPlugin.getDefault().sysout(true, "Copypath Default: >" + store.getDefaultString(P_TARGET_COPYPATH) + "<");
        targetCopyPathEditor.setStringValue(store.getString(P_TARGET_COPYPATH));
        quotesCombo.select(EasyShellQuotes.quotesNo.getId()); // just for the case it's new
        String QuotesStr = store.getString(P_QUOTES_LIST_STR);
        if (QuotesStr != null && QuotesStr.length() > 0) {
            quotesCombo.select(EasyShellQuotes.valueOf(QuotesStr).getId());
        }
        debugCombo.select(EasyShellDebug.debugNo.getId()); // just for the case it's new
        String DebugStr = store.getString(P_DEBUG_LIST_STR);
        if (DebugStr != null && DebugStr.length() > 0) {
            debugCombo.select(EasyShellDebug.valueOf(DebugStr).getId());
        }
        TokenizerCombo.select(EasyShellTokenizer.EasyShellTokenizerYes.getId()); // just for the case it's new
        String TokenizerStr = store.getString(P_TOKENIZER_LIST_STR);
        if (TokenizerStr != null && TokenizerStr.length() > 0) {
            TokenizerCombo.select(EasyShellTokenizer.valueOf(TokenizerStr).getId());
        }
        return true;
    }

    /**
     * Saves the data from controls to the preference store.
     *
     * @return store loaded.
     */
    private boolean saveStore() {
        IPreferenceStore store = getPreferenceStore();
        store.setValue(P_TARGET, targetOpenEditor.getStringValue());
        store.setValue(P_TARGET_RUN, targetRunEditor.getStringValue());
        store.setValue(P_TARGET_EXPLORE, targetExploreEditor.getStringValue());
        store.setValue(P_TARGET_COPYPATH, targetCopyPathEditor.getStringValue());
        store.setValue(P_LIST, targetCombo.getSelectionIndex() - 1); // just for compatibility to old EasyShell version <= 1.2.0
        store.setValue(P_LIST_STR, EasyShellCommand.getFromId(targetCombo.getSelectionIndex()).name());
        store.setValue(P_QUOTES_LIST_STR, EasyShellQuotes.getFromId(quotesCombo.getSelectionIndex()).name());
        store.setValue(P_DEBUG_LIST_STR, EasyShellDebug.getFromId(debugCombo.getSelectionIndex()).name());
        store.setValue(P_TOKENIZER_LIST_STR, EasyShellTokenizer.getFromId(TokenizerCombo.getSelectionIndex()).name());
        return true;
    }

    /**
     * Refreshes the fields according to selected preset.
     */
    private void refreshTarget() {
        int index = targetCombo.getSelectionIndex();
        EasyShellCommand cmd = EasyShellCommand.getFromId(index);
        targetOpenEditor.setStringValue(cmd.getOpenCmd());
        targetRunEditor.setStringValue(cmd.getRunCmd());
        targetExploreEditor.setStringValue(cmd.getExploreCmd());
        targetCopyPathEditor.setStringValue(cmd.getCopyPathCmd());
        /* we do not want set preferences now, will be set with apply or OK (performOk)
         * saveStore();
         */
    }

    /**
     * Set actual values to their defaults.
     */
    private void setToDefaults() {
        IPreferenceStore store = getPreferenceStore();
        store.setToDefault(P_TARGET);
        store.setToDefault(P_TARGET_RUN);
        store.setToDefault(P_TARGET_EXPLORE);
        store.setToDefault(P_TARGET_COPYPATH);
        store.setToDefault(P_LIST); // just for compatibility to old EasyShell version <= 1.2.0
        store.setToDefault(P_LIST_STR);
        store.setToDefault(P_QUOTES_LIST_STR);
        store.setToDefault(P_DEBUG_LIST_STR);
        store.setToDefault(P_TOKENIZER_LIST_STR);
    }

    public boolean performOk() {
        return saveStore();
    }

    public void performDefaults() {
        initializeDefaults();
        setToDefaults();
        //refreshTarget();
        loadStore();
    }

    public void init(IWorkbench workbench) {
    }

    private EasyShellCommand getProperCommand() {
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
        } else if (osname.indexOf("mac os x") != -1) {
            cmd = EasyShellCommand.cmdTerminalFinder;
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
                cmd = EasyShellCommand.cmdKonsoleKDEDolphin;
            } else if(desktop == LinuxDesktop.desktopGnome) {
                cmd = EasyShellCommand.cmdKonsoleGnome;
            } else if(desktop == LinuxDesktop.desktopCde) {
                cmd = EasyShellCommand.cmdXtermDtfile;
            }
        }
        return cmd;
    }

    /**
     * Detects which desktop is used on a unix / linux system.
     *
     * @return The type of desktop.
     * @see detectDesktop
     * @deprecated use detectDesktopNew istead
     */
    private LinuxDesktop detectDesktop() {
        LinuxDesktop resultCode = LinuxDesktop.desktopUnknown;
        try {
            String[] cmd = new String[1];
            cmd[0] = "env"; // or echo $DESKTOP_SESSION
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while((line = in.readLine()) != null && resultCode == LinuxDesktop.desktopUnknown) {
                if(line.indexOf("KDE") != -1) {
                    resultCode = LinuxDesktop.desktopKde;
                } else if(line.toLowerCase().indexOf("gnome") != -1) {
                    resultCode = LinuxDesktop.desktopGnome;
                }
            }
            BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            line = null;
            // If there is any error output, print it to
            // stdout for debugging purposes
            while((line = err.readLine()) != null) {
                EasyShellPlugin.getDefault().sysout(true, "detectDesktop stderr >" + line + "<");
            }

            int result = proc.waitFor();
            if(result != 0) {
                // If there is any error code, print it to
                // stdout for debugging purposes
                EasyShellPlugin.getDefault().sysout(true, "detectDesktop return code: " + result);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(resultCode == LinuxDesktop.desktopUnknown) {
            resultCode = LinuxDesktop.desktopCde;
        }
        return resultCode;
    }

    private LinuxDesktop detectDesktopNew() {
        LinuxDesktop resultCode = LinuxDesktop.desktopUnknown;
        if (isKDE()) {
            resultCode = LinuxDesktop.desktopKde;
        } else if (isGnome()) {
            resultCode = LinuxDesktop.desktopGnome;
        } else if (isCde()){
            resultCode = LinuxDesktop.desktopCde;
        }
        return resultCode;
    }

    /**
     * detects KDE desktop
     *
     * @see http://techbase.kde.org/KDE_System_Administration/Environment_Variables#KDE_FULL_SESSION
     */
    private boolean isKDE() {
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
    private boolean isGnome() {
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
     * TODO: detects CDE desktop
     */
    private boolean isCde() {
        return false;
    }

    /**
     * TODO: detects Xfce desktop
    private boolean isXfce() {
        return false;
    }
     */

    /**
     * Detects which desktop is used on a unix / linux system.
     *
     * @todo use regex
     *
     * @return The type of desktop.
     * @see detectDesktop
     */
    private boolean isExpectedCommandOutput(String[] command, String expectedOutput, boolean toLowerCase) {
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
                EasyShellPlugin.getDefault().sysout(true, "detectDesktop stderr >" + line + "<");
            }

            int result = proc.waitFor();
            if(result != 0) {
                // If there is any error code, print it to
                // stdout for debugging purposes
                EasyShellPlugin.getDefault().sysout(true, "detectDesktop return code: " + result);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return found;
    }

}
