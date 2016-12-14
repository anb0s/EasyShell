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
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.exceptions.UnknownCommandID;
import de.anbos.eclipse.easyshell.plugin.preferences.CommandData;
import de.anbos.eclipse.easyshell.plugin.preferences.CommandDataList;
import de.anbos.eclipse.easyshell.plugin.preferences.GeneralDataStore;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.LinuxDesktop;
import de.anbos.eclipse.easyshell.plugin.types.OS;
import de.anbos.eclipse.easyshell.plugin.types.Tooltip;

public class Utils {

    /* tooltip display parameters */
    static boolean TOOLTIP_USE_SWT_JFACE = true; /*bug: https://github.com/anb0s/EasyShell/issues/103*/
    static int TOOLTIP_HIDE_DELAY = 3000;

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
        LinuxDesktop resultCode = detectDesktopSession();
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
        Object desktop = isExpectedCommandOutput(command, desktops);
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
        return isExpectedCommandOutput(command, fileBrowsers);
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
            Activator.logDebug("isExpectedCommandOutput: answer: >" + expectedLine + "<");
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
    	if (GeneralDataStore.instance().getData().getToolTipAll() != Tooltip.tooltipYes) {
    		return;
    	}
        if (control == null) {
            control = Display.getDefault().getActiveShell();
        }
        /*bug: https://github.com/anb0s/EasyShell/issues/103*/
        if (TOOLTIP_USE_SWT_JFACE) {
            // the SWT JFace tooltip (hide delay customization)
            showToolTipSWTJface(control, style, title, message);
        } else {
            // the SWT to OS tooltip control (fixed delay)
            showToolTipSWTwidget(control, style, title, message);
        }
    }

    private static void showToolTipSWTwidget(Control control, int style, String title, String message) {
        ToolTip tooltip = new ToolTip(control.getShell(), /*SWT.BALLOON | */ style);
        tooltip.setAutoHide(true);
        tooltip.setLocation(control.toDisplay(control.getSize().x/2, control.getSize().y + 5));
        tooltip.setText(title);
        tooltip.setMessage(message);
        tooltip.setVisible(true);
    }

    private static void showToolTipSWTJface(Control control, int style, String title, String message) {
        DefaultToolTip tooltip = new DefaultToolTip(control, org.eclipse.jface.window.ToolTip.NO_RECREATE, true);
        tooltip.setHideDelay(TOOLTIP_HIDE_DELAY);
        tooltip.setText("[" + title+ "]\n\n" + message);
        tooltip.setImage(control.getDisplay().getSystemImage(/*SWT.ICON_INFORMATION*/ style));
        tooltip.show(control.toDisplay(control.getSize().x/2, 5));
    }

    public static Map<String, Object> getParameterMapFromMenuData(MenuData menuData) {
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("de.anbos.eclipse.easyshell.plugin.commands.parameter.resource",
                    menuData.getCommandData().getResourceType().toString());
            params.put("de.anbos.eclipse.easyshell.plugin.commands.parameter.type",
                    menuData.getCommandData().getCommandType().getAction());
            params.put("de.anbos.eclipse.easyshell.plugin.commands.parameter.value",
                    menuData.getCommandData().getCommand());
            params.put("de.anbos.eclipse.easyshell.plugin.commands.parameter.workingdir",
                    menuData.getCommandData().isUseWorkingDirectory() ? menuData.getCommandData().getWorkingDirectory() : "");
            params.put("de.anbos.eclipse.easyshell.plugin.commands.parameter.tokenizer",
            		menuData.getCommandData().getCommandTokenizer().toString());
		} catch (UnknownCommandID e) {
			e.logInternalError();
		}
        return params;
    }

    public static void executeCommand(final IWorkbench workbench, final MenuData menuData, boolean asynch) {
		//Activator.logInfo("executeCommand: " + menuData.getNameExpanded() + ", " + asynch, null);
        executeCommand(workbench, "de.anbos.eclipse.easyshell.plugin.commands.execute", getParameterMapFromMenuData(menuData), asynch);
    }

    public static void executeCommand(final IWorkbench workbench, final String commandName, final Map<String, Object> params, boolean asynch) {
        if (asynch) {
        	Display display = workbench == null ? Display.getDefault() : workbench.getDisplay();
        	display.asyncExec( new Runnable(){
                @Override
                public void run() {
                    executeCommand(workbench, commandName, params);
                }
            });
        } else {
            executeCommand(workbench, commandName, params);
        }
    }

    public static void executeCommands(final IWorkbench workbench, final List<MenuData> menuData, boolean asynch) {
        if (asynch) {
        	Display display = workbench == null ? Display.getDefault() : workbench.getDisplay();
        	display.asyncExec( new Runnable(){
                @Override
                public void run() {
                    for (MenuData element : menuData) {
                        executeCommand(workbench, element, false);
                    }
                }
            });
        } else {
            for (MenuData element : menuData) {
                executeCommand(workbench, element, false);
            }
        }
    }

    private static void executeCommand(IWorkbench workbench, String commandName, Map<String, Object> params) {
        if (workbench == null) {
            workbench = PlatformUI.getWorkbench();
        }
        // get command
        ICommandService commandService = (ICommandService)workbench.getService(ICommandService.class);
        Command command = commandService != null ? commandService.getCommand(commandName) : null;
        // get handler service
        //IBindingService bindingService = (IBindingService)workbench.getService(IBindingService.class);
        //TriggerSequence[] triggerSequenceArray = bindingService.getActiveBindingsFor("de.anbos.eclipse.easyshell.plugin.commands.open");
        IHandlerService handlerService = (IHandlerService)workbench.getService(IHandlerService.class);
        if (command != null && handlerService != null) {
            ParameterizedCommand paramCommand = ParameterizedCommand.generateCommand(command, params);
            try {
                handlerService.executeCommand(paramCommand, null);
            } catch (Exception e) {
                Activator.logError(Activator.getResourceString("easyshell.message.error.handlerservice.execution"), paramCommand.toString(), e, true);
            }
        }
    }

    public static String getFileExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > -1 && (fileName.length() > (i + 1))) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }

    public static String getFileBasename(String fileName) {
        String basename = fileName;
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            basename = fileName.substring(0, i);
        } else if (i == 0) {
            basename = "";
        }
        return basename;
    }

    public static String[] splitSpaces(String str) {
    	StringTokenizer st = new StringTokenizer(str);
    	String[] strings = new String[st.countTokens()];
    	for (int i=0;st.hasMoreElements();i++) {	        		
    		strings[i] = st.nextToken();
    	}
    	return strings;
    }

    /*
     * borrowed the idea from:
     * https://stackoverflow.com/questions/3366281/tokenizing-a-string-but-ignoring-delimiters-within-quotes/3366603#3366603
     */
    public static String[] splitSpacesAndQuotes(String str, boolean skipOuterQuotes) {
        str += " "; // To detect last token when not quoted...
        ArrayList<String> strings = new ArrayList<String>();
        boolean inQuote = false;
        char quote = '\"';
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\"' || c == '\'' || c == ' ' && !inQuote) {
                if (c == '\"' || c == '\'') {
                	if (!inQuote) {
                		quote = c;
                		inQuote=true;
                	} else {
                		if (quote == c) {
                			inQuote=false;                			
                		}
                	}
                    if (skipOuterQuotes || (inQuote && (quote != c))) {
                    	sb.append(c);
                    }
                }
                if (!inQuote && sb.length() > 0) {
               		strings.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            } else {
                sb.append(c);
            }
        }
        return strings.toArray(new String[strings.size()]);
    }

    public static void addNotNull(CommandDataList list, CommandData data) {
    	if (data != null) {
    		list.add(data);
    	}
    }

    public static CommandDataList getCommandData(CommandDataList list, OS os) {
        CommandDataList listOut = new CommandDataList();
        for (CommandData entry : list) {
            if (entry.getOs() == os) {
                CommandData newData = new CommandData(entry, false);
                listOut.add(newData);
            }
        }
        return listOut;
    }

    public static CommandData getCommandData(CommandDataList list, String name, Category category) {
        for (CommandData entry : list) {
            if (entry.getCategory() == category && entry.getName().matches(name)) {
                return entry;
            }
        }
        return null;
    }

    public static CommandDataList getCommandDataList(CommandDataList list, Category category) {
    	CommandDataList listOut = new CommandDataList();
        for (CommandData entry : list) {
            if (entry.getCategory() == category) {
            	CommandData newData = new CommandData(entry, false);
            	listOut.add(newData);
            }
        }
        return listOut;
    }

}
