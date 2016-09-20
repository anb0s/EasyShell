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

package de.anbos.eclipse.easyshell.plugin.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataList;

public class CategoryPopupDialog extends org.eclipse.jface.dialogs.PopupDialog implements SelectionListener, KeyListener {

    private IWorkbenchPart activePart;
    private MenuDataList menuDataList;
    private org.eclipse.swt.widgets.List listView;
    private List<Character> chars;

    public CategoryPopupDialog(Shell parent, IWorkbenchPart activePart, MenuDataList menuDataList, String title)
    {
        super(parent, INFOPOPUP_SHELLSTYLE, true, false, false, false, false, title, "...");
        this.activePart = activePart;
        this.menuDataList = menuDataList;
        chars = new ArrayList<Character>();
        for (Character ch='0';ch<='9';ch++) {
            chars.add(ch);
        }
        for (Character ch='a';ch<='z';ch++) {
            chars.add(ch);
        }
        int charsSize = menuDataList.size();
        String info = "use '0'";
        if (charsSize > 1) {
            if( charsSize <= 10) {
                info += " - '" + chars.get(charsSize-1) + "'";
            } else {
                info += " - '9' and 'a'";
                if (charsSize > 11) {
                    info += " - '" + chars.get(charsSize-1) + "'";
                }
            }
        }
        info += " to execute";
        setInfoText(info);
    }

    void init(IWorkbenchPart activePart, MenuDataList menuDataList) {
        this.activePart = activePart;
        this.menuDataList = menuDataList;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        final Composite listViewComposite = (Composite)super.createDialogArea(parent);
        listView = new org.eclipse.swt.widgets.List(listViewComposite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
        listView.addSelectionListener(this);
        listView.addKeyListener(this);
        for (int i=0;i<menuDataList.size();i++) {
            MenuData item = menuDataList.get(i);
            String prefix = Character.toString(chars.get(i)) + ": ";
            listView.add(prefix + item.getNameExpanded());
        }
        listView.select(0);
        return listViewComposite;
    }

    public static void executeCommand(IWorkbenchPart activePart, MenuData menuData) {
        // get command
        //ICommandService service = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
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
            } catch (Exception ex) {
            }
        }
    }

    private void executeCommandFromList(int index) {
        if (index == -1) {
            index = listView.getSelectionIndex();
        }
        if (index < 0 || index >= menuDataList.size()) {
            return;
        }
        listView.setSelection(index);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MenuData item = menuDataList.get(index);
        this.close();
        // execute
        executeCommand(activePart, item);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
            executeCommandFromList(-1);
        } else if (((e.stateMask & SWT.ALT) == 0) && ((e.stateMask & SWT.CTRL) == 0) && ((e.stateMask & SWT.SHIFT) == 0)) {
            if(e.keyCode >= '0' && e.keyCode <= '9') { //check digit
                executeCommandFromList(e.keyCode - '0');
            } else if(e.keyCode >= 'a' && e.keyCode <= 'z') { //check character
                executeCommandFromList((e.keyCode - 'a') + ('9' - '0' + 1));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        if (e.widget != listView) {
            return;
        }
        executeCommandFromList(-1);
    }

}
