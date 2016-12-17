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

import java.util.HashMap;
import java.util.Map;

import de.anbos.eclipse.easyshell.plugin.Activator;
import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.types.Category;
import de.anbos.eclipse.easyshell.plugin.types.CommandType;
import de.anbos.eclipse.easyshell.plugin.types.LinuxDesktop;
import de.anbos.eclipse.easyshell.plugin.types.OS;
import de.anbos.eclipse.easyshell.plugin.types.PresetType;
import de.anbos.eclipse.easyshell.plugin.types.ResourceType;
import de.anbos.eclipse.easyshell.plugin.types.CommandTokenizer;

public class CommandDataDefaultCollectionLinux {

    static public void addCommandsAll(CommandDataList list) {
    	addCommandsConsole(list);
    	addCommandsFileBrowser(list);
    }

    static public void addCommandsDefault(CommandDataList list) {
    	CommandDataList allList = new CommandDataList();
    	addCommandsAll(allList);
        // add default preset for all desktops (XDG-open)
    	Utils.addNotNull(list, Utils.getCommandData(allList, ".*", Category.categoryDefault));
    	// try to detect the desktop
        LinuxDesktop desktop = Utils.detectLinuxDesktop();
        //Activator.getDefault().sysout(true, "Detected linux (Unix) desktop: >" + desktop.getName() + "<");
        switch (desktop) {
            case desktopKde:    Utils.addNotNull(list, Utils.getCommandData(allList, ".*KDE.*", Category.categoryOpen));
                                Utils.addNotNull(list, Utils.getCommandData(allList, ".*KDE.*", Category.categoryRun));
                                //Utils.addNotNull(listDefault, Utils.getCommandData(allList, "Dolphin", Category.categoryExplore));
            break;
            case desktopCinnamon:   Utils.addNotNull(list, Utils.getCommandData(allList, ".*Gnome.*", Category.categoryOpen));
                                    Utils.addNotNull(list, Utils.getCommandData(allList, ".*Gnome.*", Category.categoryRun));
                                    //Utils.addNotNull(listDefault, Utils.getCommandData(allList, "Nemo", Category.categoryExplore));
            break;
            case desktopGnome:  Utils.addNotNull(list, Utils.getCommandData(allList, ".*Gnome.*", Category.categoryOpen));
                                Utils.addNotNull(list, Utils.getCommandData(allList, ".*Gnome.*", Category.categoryRun));
                                //Utils.addNotNull(listDefault, Utils.getCommandData(allList, "Nautilus", Category.categoryExplore));
            break;
            case desktopXfce:   Utils.addNotNull(list, Utils.getCommandData(allList, ".*Xfce.*", Category.categoryOpen));
                                Utils.addNotNull(list, Utils.getCommandData(allList, ".*Xfce.*", Category.categoryRun));
                                //Utils.addNotNull(listDefault, Utils.getCommandData(allList, "Thunar", Category.categoryExplore));
            break;
            case desktopMate:   Utils.addNotNull(list, Utils.getCommandData(allList, ".*MATE.*", Category.categoryOpen));
            					Utils.addNotNull(list, Utils.getCommandData(allList, ".*MATE.*", Category.categoryRun));
            					//Utils.addNotNull(listDefault, Utils.getCommandData(allList, "Caja", Category.categoryExplore));
            break;
            case desktopLxde:   Utils.addNotNull(list, Utils.getCommandData(allList, ".*LX.*", Category.categoryOpen));
								Utils.addNotNull(list, Utils.getCommandData(allList, ".*LX.*", Category.categoryRun));
								//Utils.addNotNull(listDefault, Utils.getCommandData(allList, "PCManFM", Category.categoryExplore));
			break;
            default:;
        }
        // try to detect the default file browser
        if (desktop != LinuxDesktop.desktopUnknown) {
            Map<String, Object> fileBrowsers = new HashMap<String, Object>();
            for (CommandData data : Utils.getCommandDataList(allList, Category.categoryExplore)) {
            	fileBrowsers.put("(?i).*" + data.getName() + ".*", data);
            }
            Object fileBrowser = Utils.detectLinuxDefaultFileBrowser(fileBrowsers);
            if (fileBrowser != null) {
            	CommandData data = (CommandData)fileBrowser;
            	Activator.logInfo("Detected linux (Unix) default file browser: >" + data.getName() + "<", null);
            	Utils.addNotNull(list, data);
            }
        }
    }

	private static void addCommandsConsole(CommandDataList list) {
        // Linux XDG Open (default application): https://linux.die.net/man/1/xdg-open 
        list.add(new CommandData("51ed300a-35d0-4e67-a5f8-6ebd7012a564", PresetType.presetPlugin, OS.osLinux, "XDG Open", ResourceType.resourceTypeFileOrDirectory, Category.categoryDefault, CommandType.commandTypeExecute,
                "xdg-open ${easyshell:resource_loc}"));
        // Linux KDE Konsole
        list.add(new CommandData("c2b1612b-9037-484a-a763-d013679bdbe7", PresetType.presetPlugin, OS.osLinux, "KDE Konsole", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "konsole --workdir ${easyshell:container_loc}"));
        list.add(new CommandData("e634b20a-cf57-47f0-aa27-b8fc95917f35", PresetType.presetPlugin, OS.osLinux, "KDE Konsole", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "konsole --workdir ${easyshell:container_loc} --noclose  -e ${easyshell:resource_loc}"));
        // Linux Gnome Terminal
        list.add(new CommandData("53f7b6a3-f8d5-4682-b0ef-1d6ceec5cc8a", PresetType.presetPlugin, OS.osLinux, "Gnome Terminal", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "gnome-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("c6126958-32f2-4f96-9933-69ddd956f2e9", PresetType.presetPlugin, OS.osLinux, "Gnome Terminal", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotes,
                "gnome-terminal --working-directory=${easyshell:container_loc} --command=./\"'${easyshell:resource_name}'\""));
        // Linux Xfce Terminal: http://docs.xfce.org/apps/terminal/command-line
        list.add(new CommandData("8175f9a7-4e54-4367-a6b6-251aedc187df", PresetType.presetPlugin, OS.osLinux, "Xfce Terminal", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "xfce4-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("adf40e10-0ee9-4abe-8282-aff7d51bb68d", PresetType.presetPlugin, OS.osLinux, "Xfce Terminal", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotes,
                "xfce4-terminal --working-directory=${easyshell:container_loc} --command=./\"'${easyshell:resource_name}'\" --hold"));
        // Linux MATE Terminal
        list.add(new CommandData("9a11b3eb-497c-44dd-9813-a841a32465c1", PresetType.presetPlugin, OS.osLinux, "MATE Terminal", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "mate-terminal --title=${easyshell:project_name} --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("d07df65d-ccc5-4c71-82f8-3a17e608516b", PresetType.presetPlugin, OS.osLinux, "MATE Terminal", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotes,
                "mate-terminal --title=${easyshell:project_name} --working-directory=${easyshell:container_loc} --command=./\"'${easyshell:resource_name}'\""));
        // Linux LX Terminal (LXDE): http://manpages.ubuntu.com/manpages/precise/en/man1/lxterminal.1.html
        list.add(new CommandData("8770e3c5-cd60-4c01-b1c0-8b7754b91b27", PresetType.presetPlugin, OS.osLinux, "LX Terminal", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "lxterminal --title=${easyshell:project_name} --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("94b0c584-0316-4dac-bd40-2ab1a0428d32", PresetType.presetPlugin, OS.osLinux, "LX Terminal", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotes,
                "lxterminal --title=${easyshell:project_name} --working-directory=${easyshell:container_loc} --command=./\"'${easyshell:resource_name}'\""));
        // Linux Sakura Terminal
        list.add(new CommandData("8e366a34-5ce2-4430-bc21-20e176e0128c", PresetType.presetPlugin, OS.osLinux, "Sakura Terminal", ResourceType.resourceTypeFileOrDirectory, true, "${easyshell:container_loc}", Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpaces,
                "sakura"));
        list.add(new CommandData("2a979af5-86a7-440e-b4f0-8442e85412e4", PresetType.presetPlugin, OS.osLinux, "Sakura Terminal", ResourceType.resourceTypeFile, true, "${easyshell:container_loc}", Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotes,
                "sakura --execute=./\"'${easyshell:resource_name}'\" --hold"));
        // Linux ROXTerm
        list.add(new CommandData("f573a4de-22fa-467f-a433-042d0992ab28", PresetType.presetPlugin, OS.osLinux, "ROXTerm", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpaces,
                "roxterm --title=${easyshell:project_name} --directory=${easyshell:container_loc}"));
        list.add(new CommandData("1cc39e61-8d8d-4493-8baa-7a11ff01c06c", PresetType.presetPlugin, OS.osLinux, "ROXTerm", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotes,
                "roxterm --title=${easyshell:project_name} --directory=${easyshell:container_loc} --execute ./\"'${easyshell:resource_name}'\""));
        // Linux Pantheon Terminal
        list.add(new CommandData("e5b3b0f6-e27c-4a2d-aa1a-caef784dd3da", PresetType.presetPlugin, OS.osLinux, "Pantheon Terminal", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "pantheon-terminal --working-directory=${easyshell:container_loc}"));
        list.add(new CommandData("22ec69ee-e39e-4fa6-a241-4e950d3235af", PresetType.presetPlugin, OS.osLinux, "Pantheon Terminal", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotes,
                "pantheon-terminal --working-directory=${easyshell:container_loc} --execute=./\"'${easyshell:resource_name}'\""));
        // Linux Guake: http://guake-project.org, http://askubuntu.com/questions/152193/how-to-open-directory-in-guake-from-nautilus
        list.add(new CommandData("554d1dc7-4f26-4be9-bc2a-86d5a41ec606", PresetType.presetPlugin, OS.osLinux, "Guake", ResourceType.resourceTypeFileOrDirectory, false, null, Category.categoryOpen, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotes,
                "guake --show --execute-command=\"cd '${easyshell:container_loc}'\""));
        // Linux Enlightenment (Terminology emulator): https://www.enlightenment.org/about-terminology
        list.add(new CommandData("837716a1-5475-42fa-b40b-6d3da56197a1", PresetType.presetPlugin, OS.osLinux, "Enlightenment Terminology", ResourceType.resourceTypeFileOrDirectory, Category.categoryOpen, CommandType.commandTypeExecute,
                "terminology --current-directory=${easyshell:container_loc}"));
        list.add(new CommandData("7137aada-940a-4b03-90b2-f468d696019a", PresetType.presetPlugin, OS.osLinux, "Enlightenment Terminology", ResourceType.resourceTypeFile, false, null, Category.categoryRun, CommandType.commandTypeExecute, CommandTokenizer.commandTokenizerSpacesAndQuotes,
                "terminology --hold --current-directory=${easyshell:container_loc} -e ./\"'${easyshell:resource_name}'\""));
	}

	private static void addCommandsFileBrowser(CommandDataList list) {
        // Linux Konqueror
        list.add(new CommandData("8873342e-e02b-4feb-8f56-9f52524c0f46", PresetType.presetPlugin, OS.osLinux, "Konqueror", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "konqueror --select file:${easyshell:resource_loc}"));
        list.add(new CommandData("3c3358b2-3541-4d13-b5bc-137ba239dae7", PresetType.presetPlugin, OS.osLinux, "Konqueror", ResourceType.resourceTypeFile, Category.categoryRun, CommandType.commandTypeExecute,
                "konqueror file:${easyshell:resource_loc}"));
        // Linux Pantheon Filebrowser
        list.add(new CommandData("025e2f56-3d2e-47e1-8daa-c2c74049b150", PresetType.presetPlugin, OS.osLinux, "Pantheon", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "pantheon-files ${easyshell:resource_loc}"));
        // Linux PCManFM
        list.add(new CommandData("cb9a8c00-89bc-453c-aeff-ae94c0d9e44a", PresetType.presetPlugin, OS.osLinux, "PCManFM", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "pcmanfm ${easyshell:container_loc}"));
        // Linux Nautilus
        list.add(new CommandData("1747b189-ed7f-4546-8c98-f99a3c1fb13b", PresetType.presetPlugin, OS.osLinux, "Nautilus", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "nautilus ${easyshell:resource_loc}"));
        // Linux Dolphin
        list.add(new CommandData("4fbfa632-5455-4384-9f9e-773603a12bea", PresetType.presetPlugin, OS.osLinux, "Dolphin", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "dolphin --select file:${easyshell:resource_loc}"));
        // Linux Nemo
        list.add(new CommandData("8e14d26d-2981-4b81-b8e5-6a942c6f2c59", PresetType.presetPlugin, OS.osLinux, "Nemo", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "nemo ${easyshell:resource_loc}"));
        // Linux Thunar
        list.add(new CommandData("cf8d4d60-10f4-4a31-a423-676d02d974e0", PresetType.presetPlugin, OS.osLinux, "Thunar", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "thunar ${easyshell:container_loc}"));
        // Linux Caja (MATE): http://manpages.ubuntu.com/manpages/wily/man1/caja.1.html
        list.add(new CommandData("f2b970ed-ef98-4ce2-861c-20f44a471f49", PresetType.presetPlugin, OS.osLinux, "Caja", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "caja ${easyshell:container_loc}"));
        // Linux Krusader:
        list.add(new CommandData("d93cd39e-1f78-47e2-90d3-88b8c8495c61", PresetType.presetPlugin, OS.osLinux, "Krusader (left panel)", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "krusader --left=${easyshell:container_loc}"));
        list.add(new CommandData("1df6da5c-5455-4372-a2ae-6b1b3f910e76", PresetType.presetPlugin, OS.osLinux, "Krusader (right panel)", ResourceType.resourceTypeFileOrDirectory, Category.categoryExplore, CommandType.commandTypeExecute,
                "krusader --right=${easyshell:container_loc}"));		
	}

}