/**
 * Copyright (c) 2014-2020 Andre Bossert <anb0s@anbos.de>.
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package de.anbos.eclipse.easyshell.plugin.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;

import de.anbos.eclipse.easyshell.plugin.misc.Utils;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuData;
import de.anbos.eclipse.easyshell.plugin.preferences.MenuDataList;

public class ExecuteCommandPopup extends org.eclipse.jface.dialogs.PopupDialog implements SelectionListener, KeyListener {

    private IWorkbench workbench;
    private MenuDataList menuDataList;
    private org.eclipse.swt.widgets.List listView;
    private List<Character> chars;

    public ExecuteCommandPopup(Shell parent, IWorkbench workbench, MenuDataList menuDataList, String title)
    {
        super(parent, INFOPOPUP_SHELLSTYLE, true, false, false, false, false, title, "...");
        this.workbench = workbench;
        this.menuDataList = menuDataList;
        chars = new ArrayList<Character>();
        for (Character ch='0';ch<='9';ch++) {
            chars.add(ch);
        }
        for (Character ch='a';ch<='z';ch++) {
            chars.add(ch);
        }
        int charsSize = chars.size();
        if (menuDataList.size() < charsSize)
            charsSize = menuDataList.size();
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

    @Override
    protected Control createDialogArea(Composite parent) {
        final Composite listViewComposite = (Composite)super.createDialogArea(parent);
        listView = new org.eclipse.swt.widgets.List(listViewComposite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
        listView.addSelectionListener(this);
        listView.addKeyListener(this);
        for (int i=0;i<menuDataList.size();i++) {
            MenuData item = menuDataList.get(i);
            String prefix = (i < chars.size() ? Character.toString(chars.get(i)) : "?") + ": ";
            listView.add(prefix + item.getNameExpanded());
        }
        listView.select(0);
        return listViewComposite;
    }


    private void executeCommandFromList(int index) {
        long sleepTime = 400;
        int selIndex = listView.getSelectionIndex();
        if (index == -1 || index == selIndex) {
            sleepTime = 10;
            index = selIndex;
        }
        if (index < 0 || index >= menuDataList.size()) {
            //Activator.logError("executeCommandFromList: bad index " + index, null);
            return;
        }
        listView.setSelection(index);
        listView.update();
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // close this dialog first, because of bug:
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=242246
        this.close();
        // and wait until the context is right again
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // execute
        Utils.executeCommand(workbench, menuDataList.get(index), true);
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
        } else {
            //Activator.logError("keyPressed", null);
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
