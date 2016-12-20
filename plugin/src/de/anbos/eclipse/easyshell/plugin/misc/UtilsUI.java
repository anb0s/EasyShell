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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
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

    static public Label createImageLabel(Composite parent, String image) {
        Label label = new Label(parent, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        label.setImage(new Image(null, Activator.getImageDescriptor(image).getImageData()));
        return label;
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
