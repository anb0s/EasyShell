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
    }

    @Override
    public int getAccelerator() {
        return 0;
    }

    @Override
    public String getActionDefinitionId() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public ImageDescriptor getDisabledImageDescriptor() {
        return null;
    }

    @Override
    public HelpListener getHelpListener() {
        return null;
    }

    @Override
    public ImageDescriptor getHoverImageDescriptor() {
        return null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    @Override
    public IMenuCreator getMenuCreator() {
        return null;
    }

    @Override
    public int getStyle() {
        return 0;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public String getToolTipText() {
        return null;
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isHandled() {
        return false;
    }

    @Override
    public void removePropertyChangeListener(IPropertyChangeListener listener) {
    }

    @Override
    public void run() {
    }

    @Override
    public void runWithEvent(Event event) {
    }

    @Override
    public void setActionDefinitionId(String id) {
    }

    @Override
    public void setChecked(boolean checked) {
    }

    @Override
    public void setDescription(String text) {
    }

    @Override
    public void setDisabledImageDescriptor(ImageDescriptor newImage) {
    }

    @Override
    public void setEnabled(boolean enabled) {
    }

    @Override
    public void setHelpListener(HelpListener listener) {
    }

    @Override
    public void setHoverImageDescriptor(ImageDescriptor newImage) {
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setImageDescriptor(ImageDescriptor newImage) {
    }

    @Override
    public void setMenuCreator(IMenuCreator creator) {
    }

    @Override
    public void setText(String text) {
    }

    @Override
    public void setToolTipText(String text) {
    }

    @Override
    public void setAccelerator(int keycode) {
    }
}
