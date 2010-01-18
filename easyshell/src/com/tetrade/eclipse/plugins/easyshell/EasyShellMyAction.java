package com.tetrade.eclipse.plugins.easyshell;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;

public class EasyShellMyAction implements IAction {

	private String id;
	
	public EasyShellMyAction(String myId) {
		id = myId;
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	public int getAccelerator() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getActionDefinitionId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageDescriptor getDisabledImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public HelpListener getHelpListener() {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageDescriptor getHoverImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() {		
		return id;
	}

	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public IMenuCreator getMenuCreator() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getStyle() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isChecked() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isHandled() {
		// TODO Auto-generated method stub
		return false;
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void runWithEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

	public void setAccelerator(int keycode) {
		// TODO Auto-generated method stub
		
	}

	public void setActionDefinitionId(String id) {
		// TODO Auto-generated method stub
		
	}

	public void setChecked(boolean checked) {
		// TODO Auto-generated method stub
		
	}

	public void setDescription(String text) {
		// TODO Auto-generated method stub
		
	}

	public void setDisabledImageDescriptor(ImageDescriptor newImage) {
		// TODO Auto-generated method stub
		
	}

	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	public void setHelpListener(HelpListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void setHoverImageDescriptor(ImageDescriptor newImage) {
		// TODO Auto-generated method stub
		
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public void setImageDescriptor(ImageDescriptor newImage) {
		// TODO Auto-generated method stub
		
	}

	public void setMenuCreator(IMenuCreator creator) {
		// TODO Auto-generated method stub
		
	}

	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}

	public void setToolTipText(String text) {
		// TODO Auto-generated method stub
		
	}

}
