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

    public static final String P_TARGET = "targetPreference";
    public static final String P_LIST = "listPreference";

    private static String[] defaultCmds = {
    		"cmd.exe /C start cmd.exe /K \"{0}: && cd {1} \"",
			"konsole --noclose --workdir {1}",
			"xterm"};
    private static String[] cmdLabels = {
    		"Windows DOS-Shell", "KDE Konsole", "Xterm"
    };
    private static int cmdWinDOS = 0;
    private static int cmdKonsole = 1;
    private static int cmdXterm = 2;
    
    private Combo targetCombo = null;
    private StringFieldEditor editor = null;

	// for unixes    
    private static final int DESKTOP_CDE = 0;
	private static final int DESKTOP_KDE = 1;
	private static final int DESKTOP_GNOME = 2;

    public EasyShellPreferencePage() {
        setPreferenceStore(EasyShellPlugin.getDefault().getPreferenceStore());
        setDescription("Set up a shell command window.");
        initializeDefaults();
    }

    /**
     * Sets the default values of the preferences.
     */
    private void initializeDefaults() {
        IPreferenceStore store = getPreferenceStore();

        // {0} is the (windows) drive letter, {1} is the directory

        String target = null;
        String osname = System.getProperty("os.name", "").toLowerCase();
        if (osname.indexOf("windows") != -1) {
            target = defaultCmds[cmdWinDOS];
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
				target = defaultCmds[cmdKonsole];
            } else if(desktop == DESKTOP_GNOME) {
				target = defaultCmds[cmdKonsole];
			} else if(desktop == DESKTOP_CDE) {
				target = defaultCmds[cmdXterm];
            }
        }
        store.setDefault(P_TARGET, target);
        
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

        targetCombo = new Combo(targetColumn, SWT.READ_ONLY);
        for(int i = 0; i < cmdLabels.length; i++) {
        	targetCombo.add(cmdLabels[i]);
        }
        targetCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                refreshTarget();
            }
        });        
        targetCombo.select(store.getInt(P_LIST));
        
        editor = new StringFieldEditor(
                P_TARGET,
                "&Shell command:",
                targetColumn);
        System.out.println("Value: " + store.getString(P_TARGET));
        System.out.println("Default: " + store.getDefaultString(P_TARGET));
        editor.setStringValue(store.getString(P_TARGET));
        
        label = new Label(mainColumn, 0);
        label.setText("Argument {0} is the drive letter on Win32.");
        label = new Label(mainColumn, 0);
        label.setText("Argument {1} is the path.");
        
        return mainColumn;
	}
	
    /**
     * Refreshes teh category.
     */
    private void refreshTarget() {
        int index = targetCombo.getSelectionIndex();
        String text = defaultCmds[index];
        System.out.println("Set text to " + text);
        editor.setStringValue(text);
        IPreferenceStore store = getPreferenceStore();
        store.setValue(P_TARGET, text);
    }
    
    public boolean performOk() {
        IPreferenceStore store = getPreferenceStore();
        store.setValue(P_TARGET, editor.getStringValue());
        store.setValue(P_LIST, targetCombo.getSelectionIndex());
    	return true;
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
				System.out.println("err>> " + line);
			}

			int result = proc.waitFor();
			if(result != 0) {
				// If there is any error code, print it to
				// stdout for debugging purposes
				System.out.println("ENV return code: " + result);
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