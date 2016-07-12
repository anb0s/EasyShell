/*******************************************************************************
 * Copyright (c) 2014 - 2016 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.preferences;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolTip;

public class Utils {

    public static OS getOS() {
        OS os = OS.osUnknown;
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
            os = OS.osWindows;
        } else if (osname.indexOf("mac os x") != -1) {
            os = OS.osMacOSX;
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
            os = OS.osLinux;
        }
        return os;
    }

    public static LinuxDesktop detectLinuxDesktop() {
        LinuxDesktop resultCode = Utils.detectDesktopSession();
        /*
        if (resultCode == LinuxDesktop.desktopUnknown)
        {
            if (isCde())
                resultCode = LinuxDesktop.desktopCde;
        }
        */
        return resultCode;
    }

    /**
     * detects desktop from $DESKTOP_SESSION
     */
    public static LinuxDesktop detectDesktopSession() {
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
        String desktop = Utils.isExpectedCommandOutput(command, desktops, true);
        if (desktop != null && !desktop.isEmpty()) {
            return (LinuxDesktop)desktops.get(desktop);
        }
        return LinuxDesktop.desktopUnknown;
    }

    /**
     * detects programs from $DESKTOP_SESSION
     */
    public static String detectLinuxDefaultFileBrowser() {
        ArrayList<String> command = new ArrayList<String>();
        command.add("xdg-mime");
        command.add("query");
        command.add("default");
        command.add("inode/directory");
        // fill the map
        Map<String, Object> fileBrowsers = new HashMap<String, Object>();
        fileBrowsers.put("nemo.desktop", "nemo");
        // execute
        String fileBrowser = Utils.isExpectedCommandOutput(command, fileBrowsers, true);
        if (fileBrowser != null && !fileBrowser.isEmpty()) {
            return (String)fileBrowsers.get(fileBrowser);
        }
        return null;
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

    public static void copyToClipboard(String cmdAll) {
        Clipboard clipboard = new Clipboard(Display.getCurrent());
        TextTransfer textTransfer = TextTransfer.getInstance();
        Transfer[] transfers = new Transfer[]{textTransfer};
        Object[] data = new Object[]{cmdAll};
        clipboard.setContents(data, transfers);
        clipboard.dispose();
    }

    public static void showToolTip(Control control, String title, String message) {
        ToolTip tooltip = new ToolTip(control.getShell(), /*SWT.BALLOON | */SWT.ICON_INFORMATION);
        tooltip.setAutoHide(true);
        tooltip.setLocation(control.toDisplay(control.getSize().x/2, control.getSize().y + 5));
        tooltip.setText(title);
        tooltip.setMessage(message);
        tooltip.setVisible(true);
    }

}
