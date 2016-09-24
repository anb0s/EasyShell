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

package de.anbos.eclipse.easyshell.plugin.misc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;
import de.anbos.eclipse.easyshell.plugin.types.LinuxDesktop;
import de.anbos.eclipse.easyshell.plugin.types.OS;

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
        Object desktop = Utils.isExpectedCommandOutput(command, desktops);
        if (desktop != null) {
            return (LinuxDesktop)desktop;
        }
        return LinuxDesktop.desktopUnknown;
    }

    /**
     * detects programs from $DESKTOP_SESSION
     */
    public static Object detectLinuxDefaultFileBrowser(Map<String, Object> fileBrowsers) {
        ArrayList<String> command = new ArrayList<String>();
        command.add("xdg-mime");
        command.add("query");
        command.add("default");
        command.add("inode/directory");
        // fill the map
        if (fileBrowsers == null) {
        	fileBrowsers = new HashMap<String, Object>();
        	fileBrowsers.put(".*", "*");
        }
        // execute
        return Utils.isExpectedCommandOutput(command, fileBrowsers);
    }

    private static Object isExpectedCommandOutput(ArrayList<String> command, Map<String, Object> expectedOutput) {
    	Object obj = null;
        boolean found = false;
        String expectedLine = null;
        try {
            Process proc = Runtime.getRuntime().exec(command.toArray(new String[1]));
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while((line = in.readLine()) != null && !found) {
                for(String key: expectedOutput.keySet()) {
                	if (line.matches(key)) {
                		obj = expectedOutput.get(key);
                		if (obj instanceof String && ((String) obj).indexOf("*") == 0) {
                			obj = line;
                		}
                		expectedLine = line;
                		break;
                	}
                }
            }
            Activator.logInfo("isExpectedCommandOutput: answer: >" + expectedLine + "<", null);
            line = null;
            BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            // If there is any error output, print it to
            // stdout for debugging purposes
            while((line = err.readLine()) != null) {
            	Activator.logError("isExpectedCommandOutput: stderr: >" + line + "<", null);
            }

            int result = proc.waitFor();
            if(result != 0) {
                // If there is any error code, print it to
                // stdout for debugging purposes
            	Activator.logError("isExpectedCommandOutput: return code: " + result, null);
            }
        } catch(Exception e) {
        	Activator.logError("isExpectedCommandOutput: exception", e);
        }
        return obj;
    }

    public static void copyToClipboard(String cmdAll) {
        Clipboard clipboard = new Clipboard(Display.getCurrent());
        TextTransfer textTransfer = TextTransfer.getInstance();
        Transfer[] transfers = new Transfer[]{textTransfer};
        Object[] data = new Object[]{cmdAll};
        clipboard.setContents(data, transfers);
        clipboard.dispose();
    }

    public static void showToolTipSuccess(Control control, String title, String message) {
        showToolTip(control, SWT.ICON_WORKING, title, message);
    }

    public static void showToolTipInfo(Control control, String title, String message) {
        showToolTip(control, SWT.ICON_INFORMATION, title, message);
    }

    public static void showToolTipWarning(Control control, String title, String message) {
        showToolTip(control, SWT.ICON_WARNING, title, message);
    }

    public static void showToolTipError(Control control, String title, String message) {
        showToolTip(control, SWT.ICON_ERROR, title, message);
    }

    public static void showToolTip(Control control, int style, String title, String message) {
        if (control == null) {
            control = Display.getDefault().getActiveShell();
        }
        ToolTip tooltip = new ToolTip(control.getShell(), /*SWT.BALLOON | */ style);
        tooltip.setAutoHide(true);
        tooltip.setLocation(control.toDisplay(control.getSize().x/2, control.getSize().y + 5));
        tooltip.setText(title);
        tooltip.setMessage(message);
        tooltip.setVisible(true);
    }

    public static void executeCommand(IWorkbenchPart activePart, MenuData menuData) {
        // get command
        //ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
        ICommandService commandService = (ICommandService)activePart.getSite().getService(ICommandService.class);
        Command command = commandService != null ? commandService.getCommand("de.anbos.eclipse.easyshell.plugin.commands.execute") : null;
        // get handler service
        IHandlerService handlerService = (IHandlerService)activePart.getSite().getService(IHandlerService.class);
        //IBindingService bindingService = (IBindingService)activePart.getSite().getService(IBindingService.class);
        //TriggerSequence[] triggerSequenceArray = bindingService.getActiveBindingsFor("de.anbos.eclipse.easyshell.plugin.commands.open");
        if (command != null && handlerService != null) {
            Map<String, Object> commandParamametersMap = new HashMap<String, Object>();
            commandParamametersMap.put("de.anbos.eclipse.easyshell.plugin.commands.parameter.type",
                    menuData.getCommandData().getCommandType().getAction());
            commandParamametersMap.put("de.anbos.eclipse.easyshell.plugin.commands.parameter.value",
                    menuData.getCommandData().getCommand());
            commandParamametersMap.put("de.anbos.eclipse.easyshell.plugin.commands.parameter.workingdir",
                    menuData.getCommandData().isUseWorkingDirectory() ? menuData.getCommandData().getWorkingDirectory() : "");
            ParameterizedCommand paramCommand = ParameterizedCommand.generateCommand(command, commandParamametersMap);
            try {
                handlerService.executeCommand(paramCommand, null);
            } catch (Exception e) {
                Activator.logError(Activator.getResourceString("easyshell.message.error.handlerservice.execution"), paramCommand.toString(), e, true);
            }
        }
    }

}
