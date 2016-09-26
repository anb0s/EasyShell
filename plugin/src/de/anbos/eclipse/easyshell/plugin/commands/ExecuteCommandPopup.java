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

    void init(IWorkbench workbench, MenuDataList menuDataList) {
        this.workbench = workbench;
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


    private void executeCommandFromList(int index) {
        long sleepTime = 500;
        int selIndex = listView.getSelectionIndex();
        if (index == -1 || index == selIndex) {
            sleepTime = 10;
            index = selIndex;
        }
        if (index < 0 || index >= menuDataList.size()) {
            return;
        }
        listView.setSelection(index);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MenuData item = menuDataList.get(index);
        // execute
        Utils.executeCommand(workbench, item, true);
        // close this dialog
        this.close();
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
