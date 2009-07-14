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
    public static final String P_LIST = "listPreference";

    private static String[] defaultCmdsOpen = {
    	"cmd.exe /C start \"{4}\" /D\"{1}\" cmd.exe /K",
    	"cmd.exe /C start \"{4}\" /D\"{1}\" cmd.exe /K \"bash.exe\"",
    	//"cmd.exe /C start \"{4}\" /D\"{1}\" cmd.exe /K \"set HOME={1} && bash.exe --login -i\"",
		"konsole --noclose --workdir \"{1}\"",
		"gnome-terminal --working-directory=\"{1}\"",
		"cd \"{1}\" && xterm"};
    private static String[] defaultCmdsRun = {
		"cmd.exe /C start \"{4}\" /D\"{1}\" \"{3}\"",
		"cmd.exe /C start \"{4}\" /D\"{1}\" cmd.exe /K \"bash.exe -c ./{3}\"",
		"konsole --noclose --workdir \"{1}\" -e \"./{3}\"",
		"gnome-terminal --working-directory=\"{1}\" --command=\"./{3}\"",
		"cd \"{1}\" && xterm -e \"./{3}\""};
    private static String[] defaultCmdsExplore = {
		"explorer.exe /select,\"{2}\"",
		"explorer.exe /select,\"{2}\"",
		"konqueror file:\"{1}\"",
		"nautilus \"{1}\"",
		//"gnome-open \"{1}\""
    	"cd \"{1}\" && dtfile"};
    private static String[] cmdLabels = {
    		"Windows DOS-Shell / Explorer", "Windows Cygwin (Bash) / Explorer", "KDE Konsole / Konqueror", "Gnome Terminal / Nautilus", "CDE Xterm / Dtfile"
    };
    private static int cmdWinDOS = 0;
    private static int cmdWinCyg = 1;
    private static int cmdKonsoleKDE = 2;
    private static int cmdKonsoleGnome = 3;
    private static int cmdXterm = 4;

    private Combo targetCombo = null;
    private StringFieldEditor targetOpenEditor = null;
    private StringFieldEditor targetRunEditor = null;
    private StringFieldEditor targetExploreEditor = null;

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

        int cmdNum = -1;
        String osname = System.getProperty("os.name", "").toLowerCase();
        if (osname.indexOf("windows") != -1) {
        	cmdNum = cmdWinDOS;
        } else if (
            osname.indexOf("unix") != -1
                || osname.indexOf("irix") != -1
                || osname.indexOf("freebsd") != -1
                || osname.indexOf("hp-ux") != -1
                || osname.indexOf("aix") != -1
                || osname.indexOf("sunos") != -1
                || osname.indexOf("linux") != -1) {
            int desktop = detectDesktop();
            if(desktop == DESKTOP_KDE) {
            	cmdNum = cmdKonsoleKDE;
            } else if(desktop == DESKTOP_GNOME) {
            	cmdNum = cmdKonsoleGnome;
			} else if(desktop == DESKTOP_CDE) {
				cmdNum = cmdXterm;
            }
        }
        store.setDefault(P_TARGET, defaultCmdsOpen[cmdNum]);
        store.setDefault(P_TARGET_RUN, defaultCmdsRun[cmdNum]);
        store.setDefault(P_TARGET_EXPLORE, defaultCmdsExplore[cmdNum]);
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
        for(int i = 0; i < cmdLabels.length; i++) {
        	targetCombo.add(cmdLabels[i]);
        }
        targetCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                refreshTarget();
            }
        });
        targetCombo.select(store.getInt(P_LIST));

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

        return mainColumn;
	}

    /**
     * Refreshes the category.
     */
    private void refreshTarget() {
        int index = targetCombo.getSelectionIndex();
        String textOpen = defaultCmdsOpen[index];
        String textRun = defaultCmdsRun[index];
        String textExplore = defaultCmdsExplore[index];
        if (debug) System.out.println("Set open text to " + textOpen);
        targetOpenEditor.setStringValue(textOpen);
        if (debug) System.out.println("Set run text to " + textRun);
        targetRunEditor.setStringValue(textRun);
        if (debug) System.out.println("Set run text to " + textExplore);
        targetExploreEditor.setStringValue(textExplore);
        IPreferenceStore store = getPreferenceStore();
        store.setValue(P_TARGET, textOpen);
        store.setValue(P_TARGET_RUN, textRun);
        store.setValue(P_TARGET_EXPLORE, textExplore);
    }

    public boolean performOk() {
        IPreferenceStore store = getPreferenceStore();
        store.setValue(P_TARGET, targetOpenEditor.getStringValue());
        store.setValue(P_TARGET_RUN, targetRunEditor.getStringValue());
        store.setValue(P_TARGET_EXPLORE, targetExploreEditor.getStringValue());
        store.setValue(P_LIST, targetCombo.getSelectionIndex());
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
			cmd[0] = "env";
			Process proc = Runtime.getRuntime().exec(cmd);

			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while((line = in.readLine()) != null && resultCode == -1) {
				if(line.indexOf("KDE") != -1) {
					resultCode = DESKTOP_KDE;
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