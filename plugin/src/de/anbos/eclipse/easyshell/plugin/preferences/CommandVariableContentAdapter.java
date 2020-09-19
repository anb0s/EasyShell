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

package de.anbos.eclipse.easyshell.plugin.preferences;

import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class CommandVariableContentAdapter extends TextContentAdapter {

    /*
    @Override
    public void setControlContents(Control control, String contents, int cursorPosition) {
        // TODO Auto-generated method stub

    }

    @Override
    public void insertControlContents(Control control, String contents, int cursorPosition) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getControlContents(Control control) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getCursorPosition(Control control) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Rectangle getInsertionBounds(Control control) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCursorPosition(Control control, int index) {
        // TODO Auto-generated method stub

    }
    */

    @Override
    public Point getSelection(Control control) {
        Point point = ((Text) control).getSelection();
        if (point.x > 0) {
            String text = ((Text) control).getText(point.x-1, point.y);
            if (text.equals("$") || text.equals("{")) {
                point.x = point.x-1;
                if (point.x > 0 && ((Text) control).getText(point.x-1, point.y).equals("${")) {
                    point.x = point.x-1;
                }
            }
            ((Text) control).setSelection(point);
        }
        return point;
    }

    /*
    @Override
    public void setSelection(Control control, Point range) {
        // TODO Auto-generated method stub

    }
    */

}
