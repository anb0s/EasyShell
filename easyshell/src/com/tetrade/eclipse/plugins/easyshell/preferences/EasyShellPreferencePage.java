/*
 * Copyright (C) 2004 - 2014 by Marcel Schoen and Andre Bossert
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
import org.eclipse.swt.widgets.Button;
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
     * Preference controls.
     */
	private Composite targetColumn = null;
	private Button targetEnabled = null;
    private Combo targetCombo = null;
    private Button targetOpenEnabled = null;
    private StringFieldEditor targetOpenEditor = null;
    private StringFieldEditor targetRunEditor = null;
    private StringFieldEditor targetExploreEditor = null;
    private StringFieldEditor targetCopyPathEditor = null;
    private Combo quotesCombo = null;
    private Combo debugCombo = null;
    private Combo tokenizerCombo = null;

    /**
     * locals
     */
    private int instanceId = 0;

    /**
     * Constructs the preference page.
     */
    public EasyShellPreferencePage(int instId) {
    	instanceId = instId;
        setPreferenceStore(EasyShellPlugin.getDefault().getPreferenceStore());
        setDescription("Set up a shell command window for instance number " + new Integer(instanceId).toString() + "!");
        initializeDefaults();
    }

    public static String getPreferenceString(int stringId, int instId) {
    	return EasyShellPreferenceEntry.getFromId(stringId).getString(instId);
    }

    /**
     * Sets the default values of the preferences.
     */
    private void initializeDefaults() {
        // get proper command (detect)
        EasyShellCommand cmd = getProperCommand();
        // get store
        IPreferenceStore store = getPreferenceStore();
        store.setDefault(EasyShellPreferenceEntry.preferenceTargetEnabled.getString(instanceId), false);
        // set default commands
        store.setDefault(EasyShellPreferenceEntry.preferenceTargetOpen.getString(instanceId), cmd.getOpenCmd());
        store.setDefault(EasyShellPreferenceEntry.preferenceTargetRun.getString(instanceId), cmd.getRunCmd());
        store.setDefault(EasyShellPreferenceEntry.preferenceTargetExplore.getString(instanceId), cmd.getExploreCmd());
        store.setDefault(EasyShellPreferenceEntry.preferenceTargetCopyPath.getString(instanceId), cmd.getCopyPathCmd());
        // set default selected preset
        store.setDefault(EasyShellPreferenceEntry.preferenceListId.getString(instanceId), cmd.getId() - 1);
        store.setDefault(EasyShellPreferenceEntry.preferenceListString.getString(instanceId), cmd.name());
        // set default
        store.setDefault(EasyShellPreferenceEntry.preferenceQuotes.getString(instanceId), EasyShellQuotes.quotesNo.name());
        store.setDefault(EasyShellPreferenceEntry.preferenceDebug.getString(instanceId), EasyShellDebug.debugNo.name());
        store.setDefault(EasyShellPreferenceEntry.preferenceTokenizer.getString(instanceId), EasyShellTokenizer.EasyShellTokenizerYes.name());
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

        targetColumn = new Composite(mainColumn, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        targetColumn.setFont(parent.getFont());
        targetColumn.setLayout(layout);

        // --------------------------------- Enabled -------------------------------------------------
        targetEnabled = new Button(targetColumn, SWT.TOGGLE); // SWT.CHECK
        targetEnabled.setText("Enabled");
        //new Label(targetColumn, 0);new Label(targetColumn, 0);
        targetEnabled.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	refreshEnabled();
            }
        });

        // --------------------------------- Presets -------------------------------------------------
        //Label label = new Label(targetColumn, 0);
        //label.setText("Presets");
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
//        targetOpenEnabled = new Button(targetColumn, SWT.CHECK); // SWT.CHECK
//        new Label(targetColumn, 0);
//        targetOpenEnabled.setText("Enabled");
        targetOpenEditor = new StringFieldEditor(
                EasyShellPreferenceEntry.preferenceTargetOpen.getString(instanceId),
                "&Shell Open command:",
                targetColumn);
        targetOpenEditor.getLabelControl(targetColumn).setToolTipText(targetTooltip);

        // ----------------------------------- Run ---------------------------------------------------
        targetRunEditor = new StringFieldEditor(
                EasyShellPreferenceEntry.preferenceTargetRun.getString(instanceId),
                "&Shell Run command:",
                targetColumn);
        targetRunEditor.getLabelControl(targetColumn).setToolTipText(targetTooltip);

        // --------------------------------- Explore -------------------------------------------------
        targetExploreEditor = new StringFieldEditor(
                EasyShellPreferenceEntry.preferenceTargetExplore.getString(instanceId),
                "&Explore command:",
                targetColumn);
        targetExploreEditor.getLabelControl(targetColumn).setToolTipText(targetTooltip);

        // --------------------------------- Copypath ------------------------------------------------
        targetCopyPathEditor = new StringFieldEditor(
                EasyShellPreferenceEntry.preferenceTargetCopyPath.getString(instanceId),
                "Copy Path string:",
                targetColumn);
        targetCopyPathEditor.getLabelControl(targetColumn).setToolTipText(targetTooltip);

        // ------------------------------------ Tokenizer --------------------------------------------
        Label tokenizer = new Label(targetColumn, 0);
        tokenizer.setText("Enable string tokenizer");
        tokenizer.setToolTipText("Constructs a string tokenizer for the specified string. The tokenizer uses the default delimiter set,\nwhich is \" \\t\\n\\r\\f\": the space character, the tab character, the newline character,\nthe carriage-return character, and the form-feed character.\nATTENTION: please use it carefully with Add quotes!");
        tokenizerCombo = new Combo(targetColumn, SWT.READ_ONLY | SWT.DROP_DOWN);
        for(int i = 0; i < EasyShellTokenizer.values().length; i++) {
        	tokenizerCombo.add(EasyShellTokenizer.getFromId(i).getMode());
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
        Label desc_label = new Label(mainColumn, 0);
        desc_label.setText("Argument {0} is the drive letter on Win32");
        desc_label = new Label(mainColumn, 0);
        desc_label.setText("Argument {1} is the parent path*");
        desc_label = new Label(mainColumn, 0);
        desc_label.setText("Argument {2} is the full path*");
        desc_label = new Label(mainColumn, 0);
        desc_label.setText("Argument {3} is the file name*");
        desc_label = new Label(mainColumn, 0);
        desc_label.setText("Argument {4} is the project name");
        desc_label = new Label(mainColumn, 0);
        desc_label.setText("Argument {5} is the line separator");

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
        targetEnabled.setSelection(store.getBoolean(EasyShellPreferenceEntry.preferenceTargetEnabled.getString(instanceId)));
        targetCombo.select(store.getInt(EasyShellPreferenceEntry.preferenceListId.getString(instanceId)) + 1); 	// get index: just for compatibility to old EasyShell version <= 1.2.0
        String IdStr = store.getString(EasyShellPreferenceEntry.preferenceListString.getString(instanceId)); 	// get name: new method >= 1.3.0
        if (IdStr != null && IdStr.length() > 0) {
            targetCombo.select(EasyShellCommand.valueOf(IdStr).getId());
        }
        EasyShellPlugin.getDefault().sysout(true, "Open Value      : >" + store.getString(EasyShellPreferenceEntry.preferenceTargetOpen.getString(instanceId)) + "<");
        EasyShellPlugin.getDefault().sysout(true, "Open Default    : >" + store.getDefaultString(EasyShellPreferenceEntry.preferenceTargetOpen.getString(instanceId)) + "<");
        targetOpenEditor.setStringValue(store.getString(EasyShellPreferenceEntry.preferenceTargetOpen.getString(instanceId)));
        EasyShellPlugin.getDefault().sysout(true, "Run Value       : >" + store.getString(EasyShellPreferenceEntry.preferenceTargetRun.getString(instanceId)) + "<");
        EasyShellPlugin.getDefault().sysout(true, "Run Default     : >" + store.getDefaultString(EasyShellPreferenceEntry.preferenceTargetRun.getString(instanceId)) + "<");
        targetRunEditor.setStringValue(store.getString(EasyShellPreferenceEntry.preferenceTargetRun.getString(instanceId)));
        EasyShellPlugin.getDefault().sysout(true, "Explore Value   : >" + store.getString(EasyShellPreferenceEntry.preferenceTargetExplore.getString(instanceId)) + "<");
        EasyShellPlugin.getDefault().sysout(true, "Explore Default : >" + store.getDefaultString(EasyShellPreferenceEntry.preferenceTargetExplore.getString(instanceId)) + "<");
        targetExploreEditor.setStringValue(store.getString(EasyShellPreferenceEntry.preferenceTargetExplore.getString(instanceId)));
        EasyShellPlugin.getDefault().sysout(true, "Copypath Value  : >" + store.getString(EasyShellPreferenceEntry.preferenceTargetCopyPath.getString(instanceId)) + "<");
        EasyShellPlugin.getDefault().sysout(true, "Copypath Default: >" + store.getDefaultString(EasyShellPreferenceEntry.preferenceTargetCopyPath.getString(instanceId)) + "<");
        targetCopyPathEditor.setStringValue(store.getString(EasyShellPreferenceEntry.preferenceTargetCopyPath.getString(instanceId)));
        quotesCombo.select(EasyShellQuotes.quotesNo.getId()); // just for the case it's new
        String QuotesStr = store.getString(EasyShellPreferenceEntry.preferenceQuotes.getString(instanceId));
        if (QuotesStr != null && QuotesStr.length() > 0) {
            quotesCombo.select(EasyShellQuotes.valueOf(QuotesStr).getId());
        }
        debugCombo.select(EasyShellDebug.debugNo.getId()); // just for the case it's new
        String DebugStr = store.getString(EasyShellPreferenceEntry.preferenceDebug.getString(instanceId));
        if (DebugStr != null && DebugStr.length() > 0) {
            debugCombo.select(EasyShellDebug.valueOf(DebugStr).getId());
        }
        tokenizerCombo.select(EasyShellTokenizer.EasyShellTokenizerYes.getId()); // just for the case it's new
        String TokenizerStr = store.getString(EasyShellPreferenceEntry.preferenceTokenizer.getString(instanceId));
        if (TokenizerStr != null && TokenizerStr.length() > 0) {
            tokenizerCombo.select(EasyShellTokenizer.valueOf(TokenizerStr).getId());
        }
        // update the enabled state
        refreshEnabled();
        return true;
    }

    /**
     * Saves the data from controls to the preference store.
     *
     * @return store loaded.
     */
    private boolean saveStore() {
        IPreferenceStore store = getPreferenceStore();
        store.setValue(EasyShellPreferenceEntry.preferenceTargetEnabled.getString(instanceId), targetEnabled.getSelection());
        store.setValue(EasyShellPreferenceEntry.preferenceTargetOpen.getString(instanceId), targetOpenEditor.getStringValue());
        store.setValue(EasyShellPreferenceEntry.preferenceTargetRun.getString(instanceId), targetRunEditor.getStringValue());
        store.setValue(EasyShellPreferenceEntry.preferenceTargetExplore.getString(instanceId), targetExploreEditor.getStringValue());
        store.setValue(EasyShellPreferenceEntry.preferenceTargetCopyPath.getString(instanceId), targetCopyPathEditor.getStringValue());
        store.setValue(EasyShellPreferenceEntry.preferenceListId.getString(instanceId), targetCombo.getSelectionIndex() - 1); // just for compatibility to old EasyShell version <= 1.2.0
        store.setValue(EasyShellPreferenceEntry.preferenceListString.getString(instanceId), EasyShellCommand.getFromId(targetCombo.getSelectionIndex()).name());
        store.setValue(EasyShellPreferenceEntry.preferenceQuotes.getString(instanceId), EasyShellQuotes.getFromId(quotesCombo.getSelectionIndex()).name());
        store.setValue(EasyShellPreferenceEntry.preferenceDebug.getString(instanceId), EasyShellDebug.getFromId(debugCombo.getSelectionIndex()).name());
        store.setValue(EasyShellPreferenceEntry.preferenceTokenizer.getString(instanceId), EasyShellTokenizer.getFromId(tokenizerCombo.getSelectionIndex()).name());
        return true;
    }

    /**
     * Refreshes the enablement according to enabled check.
     */
    private void refreshEnabled() {
    	boolean enabled = targetEnabled.getSelection();
    	targetEnabled.setText(enabled ? "Enabled" : "Disabled");
        targetCombo.setEnabled(enabled);
        targetOpenEditor.setEnabled(enabled, targetColumn);
        targetRunEditor.setEnabled(enabled, targetColumn);
        targetExploreEditor.setEnabled(enabled, targetColumn);
        targetCopyPathEditor.setEnabled(enabled, targetColumn);
        quotesCombo.setEnabled(enabled);
        debugCombo.setEnabled(enabled);
        tokenizerCombo.setEnabled(enabled);
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
        store.setToDefault(EasyShellPreferenceEntry.preferenceTargetEnabled.getString(instanceId));
        store.setToDefault(EasyShellPreferenceEntry.preferenceTargetOpen.getString(instanceId));
        store.setToDefault(EasyShellPreferenceEntry.preferenceTargetRun.getString(instanceId));
        store.setToDefault(EasyShellPreferenceEntry.preferenceTargetExplore.getString(instanceId));
        store.setToDefault(EasyShellPreferenceEntry.preferenceTargetCopyPath.getString(instanceId));
        store.setToDefault(EasyShellPreferenceEntry.preferenceListId.getString(instanceId)); // just for compatibility to old EasyShell version <= 1.2.0
        store.setToDefault(EasyShellPreferenceEntry.preferenceListString.getString(instanceId));
        store.setToDefault(EasyShellPreferenceEntry.preferenceQuotes.getString(instanceId));
        store.setToDefault(EasyShellPreferenceEntry.preferenceDebug.getString(instanceId));
        store.setToDefault(EasyShellPreferenceEntry.preferenceTokenizer.getString(instanceId));
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
