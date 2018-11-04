/*******************************************************************************
 * Copyright (c) 2014 - 2018 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.preferences;

import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.types.OS;

public class CommandDataDefaultCollection {

    private CommandDataList list = new CommandDataList();
    private static CommandDataDefaultCollection instance = new CommandDataDefaultCollection();

    public static CommandDataList getAllCommandsStatic() {
        return instance.getCommands();
    }

    public static MenuDataList getCommandsNativeAsMenu(boolean sorted) {
        CommandDataList list = getCommandsNativeDefault();
        MenuDataList ret = new MenuDataList();
        for (int i=0;i<list.size();i++) {
            CommandData cmdData = list.get(i);
            MenuData newData = new MenuData(cmdData.getId(), cmdData.getId()); // use the same id like the default command to have same defaults
            newData.setNameTypeFromCategory(cmdData.getCategory());
            if (sorted) {
                newData.setPosition(i);
            }
            ret.add(newData);
        }
        return ret;
    }

    CommandDataDefaultCollection() {
        CommandDataDefaultCollectionWindows.addCommandsAll(list);
        CommandDataDefaultCollectionLinux.addCommandsAll(list);
        CommandDataDefaultCollectionMacOSX.addCommandsAll(list);
        CommandDataDefaultCollectionAllOS.addCommandsAll(list);
    }

    public CommandDataList getCommands() {
        return list;
    }

    public static CommandDataList getCommandsNativeAll(CommandDataList list) {
        if (list == null) {
            list = getAllCommandsStatic();
        }
        return Utils.getCommandData(list, Utils.getOS());
    }

    private static CommandDataList getCommandsNativeDefault() {
        CommandDataList listDefault = new CommandDataList();
        OS os = Utils.getOS();
        switch(os)
        {
            case osUnknown:
                break;
            case osWindows:
                CommandDataDefaultCollectionWindows.addCommandsDefault(listDefault);
                break;
            case osLinux:
                CommandDataDefaultCollectionLinux.addCommandsDefault(listDefault);
                break;
            case osMacOSX:
                CommandDataDefaultCollectionMacOSX.addCommandsDefault(listDefault);
                break;
            case osUnix:
                // no op
                break;
            default:
                break;
        }
        CommandDataDefaultCollectionAllOS.addCommandsDefault(listDefault);
        return listDefault;
    }

}
