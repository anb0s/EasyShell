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

    private static String[] defaultCmds = {
    		"cmd.exe /C start cmd.exe /K \"{0}: && cd {1} \"",
			"konsole --noclose --workdir {1}"};
    private static String[] cmdLabels = {
    		"Windows DOS-Shell", "KDE Konsole"
    };
    
    private Combo categoryCombo = null;
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
            target = "cmd.exe /C start cmd.exe /K \"{0}: && cd {1} \"";
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
				target = "konsole --noclose --workdir {1}";
            } else if(desktop == DESKTOP_GNOME) {
				target = "konsole --noclose --workdir {1}";
			} else if(desktop == DESKTOP_CDE) {
				target = "xterm";
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
		Composite mainColumn = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		mainColumn.setFont(parent.getFont());
		mainColumn.setLayout(layout);
		
        categoryCombo = new Combo(mainColumn, SWT.READ_ONLY);
        for(int i = 0; i < cmdLabels.length; i++) {
        	categoryCombo.add(cmdLabels[i]);
        }
        
        editor = new StringFieldEditor(
                P_TARGET,
                "&Target:",
                mainColumn);
        
        hookListeners();
        
        return mainColumn;
	}
	
	private void hookListeners() {
        categoryCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                refreshCategory();
            }
        });        
	}
	
    /**
     * Refreshes teh category.
     */
    private void refreshCategory() {
        int index = categoryCombo.getSelectionIndex();
        String text = defaultCmds[index];
        System.out.println("Set text to " + text);
        editor.setStringValue(text);
    }

    /**
     * Creates the field editors. Field editors are abstractions of
     * the common GUI blocks needed to manipulate various types
     * of preferences. Each field editor knows how to save and
     * restore itself.
     */
/*
    public void createFieldEditors() {

        addField(
            new StringFieldEditor(
                P_TARGET,
                "&Target:",
                getFieldEditorParent()));
    }
*/
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