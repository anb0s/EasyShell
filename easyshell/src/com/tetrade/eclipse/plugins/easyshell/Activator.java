/*
 * Copyright (C) 2004 - 2008 by Marcel Schoen
 * Copyright (C) 2009 - 2016 by Andre Bossert
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.tetrade.eclipse.plugins.easyshell;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.tetrade.eclipse.plugins.easyshell.preferences.Debug;
import com.tetrade.eclipse.plugins.easyshell.preferences.PreferencePage;
import com.tetrade.eclipse.plugins.easyshell.preferences.PreferenceEntry;
import com.tetrade.eclipse.plugins.easyshell.preferences.Quotes;
import com.tetrade.eclipse.plugins.easyshell.preferences.Tokenizer;

/**
 * The main plugin class to be used in the desktop.
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID 			= "com.tetrade.eclipse.plugins.easyshell";
    public static final String IMAGE_PATH 			= "icon/";
    public static final String IMAGE_OPEN_ID 		= "easyshell.gif";
    public static final String IMAGE_RUN_ID 		= "run_exc.gif";
    public static final String IMAGE_EXPLORE_ID 	= "fldr_obj.gif";
    public static final String IMAGE_COPYPATH_ID	= "copy_edit.gif";

    //The shared instance.
    private static Activator plugin;

    /*
     * The constructor.
     */
    public Activator() {
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        getImageRegistry();
        plugin = this;
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
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
*/
    public static ImageDescriptor getImageDescriptor(String id) {
        return getDefault().getImageRegistry().getDescriptor(id);
    }

    protected void initializeImageRegistry(ImageRegistry registry) {
        Bundle bundle = Platform.getBundle(PLUGIN_ID);
        addImageToRegistry(registry, bundle, IMAGE_PATH + IMAGE_OPEN_ID, IMAGE_OPEN_ID);
        addImageToRegistry(registry, bundle, IMAGE_PATH + IMAGE_RUN_ID, IMAGE_RUN_ID);
        addImageToRegistry(registry, bundle, IMAGE_PATH + IMAGE_EXPLORE_ID, IMAGE_EXPLORE_ID);
        addImageToRegistry(registry, bundle, IMAGE_PATH + IMAGE_COPYPATH_ID, IMAGE_COPYPATH_ID);
     }

    protected void addImageToRegistry(ImageRegistry registry, Bundle bundle, String imagePath, String image_id) {
        IPath path = new Path(imagePath);
        URL url = FileLocator.find(bundle, path, null);
        ImageDescriptor desc = ImageDescriptor.createFromURL(url);
        registry.put(image_id, desc);
    }

    /**
     * Returns the workspace instance.
     */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    static public void log(Object msg) {
        ILog log = Activator.getDefault().getLog();
        Status status = new Status(IStatus.ERROR, Activator.getDefault().getBundle().getSymbolicName(), IStatus.ERROR, msg + "\n", null);
        log.log(status);
    }

    static public void log(Throwable ex) {
        ILog log = Activator.getDefault().getLog();
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        String msg = stringWriter.getBuffer().toString();
        Status status = new Status(IStatus.ERROR, Activator.getDefault().getBundle().getSymbolicName(), IStatus.ERROR, msg, null);
        log.log(status);
    }

    /**
     * Return the target program setted in EasyExplorePreferencePage.
     * @return String
     */
    public String getTarget(int commandId, int instId) {
    	return getPreferenceStore().getString(PreferencePage.getPreferenceString(commandId, instId));
    }

    /**
     * Return the quotes setted in EasyExplorePreferencePage.
     * @return EasyShellQuotes
     */
    public Quotes getQuotes(int instId) {
    	String quotesStr = getPreferenceStore().getString(PreferenceEntry.preferenceQuotes.getString(instId));
    	if (quotesStr != null && quotesStr.length() != 0)
    		return Quotes.valueOf(quotesStr);
    	else
    		return Quotes.quotesNo;
    }

    static public int getInstanceNumber() {
    	return 3;
    }

    /**
     * Return the Debug Yes or No setted in EasyExplorePreferencePage.
     * @return boolean
     */
    public boolean isDebug() {
        //return debug;
        String dbgStr = getPreferenceStore().getString(PreferenceEntry.preferenceDebug.getString());
        if (dbgStr != null && dbgStr.length() != 0)
            return Debug.valueOf(dbgStr) == Debug.debugYes;
        else
            return false;
    }

    /**
     * Return the String Tokenizer Yes or No setted in EasyExplorePreferencePage.
     * @return boolean
     */
    public boolean isTokenizer(int instId) {
        String tokenizerStr = getPreferenceStore().getString(PreferenceEntry.preferenceTokenizer.getString(instId));
        if (tokenizerStr != null && tokenizerStr.length() != 0)
            return Tokenizer.valueOf(tokenizerStr) == Tokenizer.EasyShellTokenizerYes;
        else
            return false;
    }

    public void sysout(boolean dbg, String str) {
        if (!dbg || (dbg && isDebug())) {
            System.out.println("[EasyShell] " + str);
        }
    }
}
