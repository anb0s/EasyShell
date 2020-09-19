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

package de.anbos.eclipse.easyshell.plugin.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;

public class Action implements IAction {

    private String id;

    public Action(String myId) {
        setId(myId);
    }

    @Override
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getAccelerator() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getActionDefinitionId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ImageDescriptor getDisabledImageDescriptor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HelpListener getHelpListener() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ImageDescriptor getHoverImageDescriptor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMenuCreator getMenuCreator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getStyle() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getText() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getToolTipText() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isChecked() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isHandled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removePropertyChangeListener(IPropertyChangeListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

    @Override
    public void runWithEvent(Event event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setActionDefinitionId(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setChecked(boolean checked) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDescription(String text) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDisabledImageDescriptor(ImageDescriptor newImage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setEnabled(boolean enabled) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setHelpListener(HelpListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setHoverImageDescriptor(ImageDescriptor newImage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setImageDescriptor(ImageDescriptor newImage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMenuCreator(IMenuCreator creator) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setText(String text) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setToolTipText(String text) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAccelerator(int keycode) {
        // TODO Auto-generated method stub

    }
}
