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

package de.anbos.eclipse.easyshell.plugin;

import java.net.URL;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import de.anbos.eclipse.easyshell.plugin.preferences.Constants;
import de.anbos.eclipse.easyshell.plugin.preferences.CommandType;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The shared instance
	private static Activator plugin;

    //Resource bundle.
    private ResourceBundle resourceBundle;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
        try {
            //resourceBundle = Platform.getResourceBundle(context.getBundle());
            resourceBundle = ResourceBundle.getBundle(Constants.PLUGIN_ID + ".UIMessages"); //$NON-NLS-1$
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
        getImageRegistry();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	/*
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(Constants.PLUGIN_ID, path);
	}
	*/
    public static ImageDescriptor getImageDescriptor(String id) {
        return getDefault().getImageRegistry().getDescriptor(id);
    }
    protected void initializeImageRegistry(ImageRegistry registry) {
        Bundle bundle = Platform.getBundle(Constants.PLUGIN_ID);
        for (String icon : CommandType.getIconsAsList()) {
            addImageToRegistry(registry, bundle, Constants.IMAGE_PATH + icon, icon);
        }
     }

    protected void addImageToRegistry(ImageRegistry registry, Bundle bundle, String imagePath, String image_id) {
        IPath path = new Path(imagePath);
        URL url = FileLocator.find(bundle, path, null);
        ImageDescriptor desc = ImageDescriptor.createFromURL(url);
        registry.put(image_id, desc);
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public static String getResourceString(String key) {
        ResourceBundle bundle = Activator.getDefault().getResourceBundle();
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static String getResourceString(String key, Object[] args) {
        return MessageFormat.format(getResourceString(key),args);
    }

}
