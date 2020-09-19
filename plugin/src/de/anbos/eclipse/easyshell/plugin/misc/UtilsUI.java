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

package de.anbos.eclipse.easyshell.plugin.misc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import de.anbos.eclipse.easyshell.plugin.Activator;

public class UtilsUI {

    static public void createLabel(Composite parent, String name, String tooltip) {
        Label label = new Label(parent, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        label.setText(name);
        if (tooltip != null) {
            label.setToolTipText(tooltip);
        }
    }

    static public Label createLabel(Composite parent, String imageId, String text, String tooltip) {
        Label label = new Label(parent, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        if (text != null) {
            label.setText(text);
            label.setToolTipText(tooltip);
        }
        label.setImage(Activator.getImage(imageId));
        return label;
    }

    static public Label createImageLabel(Composite parent, String imageId) {
        return createLabel(parent, imageId, null, null);
    }

    static public Button createButton(Composite parent, String imageId, String text, String tooltip) {
        Button button = new Button(parent, SWT.LEFT);
        button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        if (text!=null) {
            button.setText(text);
        }
        if (tooltip != null) {
            button.setToolTipText(tooltip);
        }
        button.setImage(Activator.getImage(imageId));
        return button;
    }

    static public Button createImageButton(Composite parent, String imageId, String tooltip) {
        return createButton(parent, imageId, null, tooltip);
    }

    static public void refreshWidget(Widget widget) {
        // send event to refresh
        Event event = new Event();
        event.item = null;
        widget.notifyListeners(SWT.Selection, event);
    }

    static public Text createTextField(Composite parent, String labelText, String labelTooltip, String editValue, boolean emptyLabel, boolean editable) {
        // draw label
        if (labelText != null) {
            UtilsUI.createLabel(parent, labelText, labelTooltip);
        }
        if (emptyLabel) {
            UtilsUI.createLabel(parent, "", null);
        }
        // draw textfield
        Text text = new Text(parent,SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.setText(editValue);
        text.setEditable(editable);
        text.setToolTipText(labelTooltip);
        return text;
    }

}
