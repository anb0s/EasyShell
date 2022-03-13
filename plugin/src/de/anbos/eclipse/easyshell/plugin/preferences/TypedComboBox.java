/**
 * Copyright (c) 2014-2022 Andre Bossert <anb0s@anbos.de>.
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class TypedComboBox<T> {

    private ComboViewer viewer;

    private TypedComboBoxLabelProvider<T> labelProvider;
    private List<T> content;
    private List<TypedComboBoxSelectionListener<T>> selectionListeners;
    private T currentSelection;

    public TypedComboBox(Composite parent) {
        this.viewer = new ComboViewer(parent, SWT.READ_ONLY);
        this.viewer.setContentProvider(ArrayContentProvider.getInstance());
        this.viewer.getCombo().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        viewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                T typedElement = getTypedObject(element);
                if (labelProvider != null && typedElement != null) {
                    if (typedElement == currentSelection) {
                        return labelProvider.getSelectedLabel(typedElement);
                    } else {
                        return labelProvider.getListLabel(typedElement);
                    }

                } else {
                    return element.toString();
                }
            }
            @Override
            public Image getImage(Object element) {
                T typedElement = getTypedObject(element);
                if (labelProvider != null && typedElement != null) {
                    return labelProvider.getImage(typedElement);
                } else {
                    return null;
                }
            }
        });

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                T typedSelection = getTypedObject(selection.getFirstElement());
                if (typedSelection != null) {
                    currentSelection = typedSelection;
                    viewer.refresh();
                    notifySelectionListeners(typedSelection);
                }

            }
        });

        this.content = new ArrayList<T>();
        this.selectionListeners = new ArrayList<TypedComboBoxSelectionListener<T>>();
    }

    public void setLabelProvider(TypedComboBoxLabelProvider<T> labelProvider) {
        this.labelProvider = labelProvider;
    }

    public void setContent(List<T> content) {
        this.content = content;
        this.viewer.setInput(content.toArray());
    }

    public T getSelection() {
        return currentSelection;
    }

    public void setSelection(T selection) {
        if (content.contains(selection)) {
            viewer.setSelection(new StructuredSelection(selection), true);
        }
    }

    public void selectFirstItem() {
        if (content.size() > 0) {
            setSelection(content.get(0));
        }
    }

    public void selectLastItem() {
        if (content.size() > 0) {
            setSelection(content.get(content.size()-1));
        }
    }

    public void addSelectionListener(TypedComboBoxSelectionListener<T> listener) {
        this.selectionListeners.add(listener);
    }

    public void removeSelectionListener(TypedComboBoxSelectionListener<T> listener) {
        this.selectionListeners.remove(listener);
    }

    private T getTypedObject(Object o) {
        if (content.contains(o)) {
            return content.get(content.indexOf(o));
        } else {
            return null;
        }
    }

    private void notifySelectionListeners(T newSelection) {
        for (TypedComboBoxSelectionListener<T> listener : selectionListeners) {
            listener.selectionChanged(this, newSelection);
        }
    }

    public ComboViewer getViewer() {
        return viewer;
    }

}
