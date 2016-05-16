/*
 * Copyright (C) 2004 - 2015 by Marcel Schoen and Andre Bossert
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
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

import com.tetrade.eclipse.plugins.easyshell.preferences.EasyShellDebug;
import com.tetrade.eclipse.plugins.easyshell.preferences.EasyShellPreferencePage;
import com.tetrade.eclipse.plugins.easyshell.preferences.EasyShellPreferenceEntry;
import com.tetrade.eclipse.plugins.easyshell.preferences.EasyShellQuotes;
import com.tetrade.eclipse.plugins.easyshell.preferences.EasyShellTokenizer;

/**
 * The main plugin class to be used in the desktop.
 */
public class EasyShellPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID 			= "com.tetrade.eclipse.plugins.easyshell";
    public static final String IMAGE_PATH 			= "icon/";
    public static final String IMAGE_OPEN_ID 		= "easyshell.gif";
    public static final String IMAGE_RUN_ID 		= "run_exc.gif";
    public static final String IMAGE_EXPLORE_ID 	= "fldr_obj.gif";
    public static final String IMAGE_COPYPATH_ID	= "copy_edit.gif";

    //The shared instance.
    private static EasyShellPlugin plugin;
    //Resource bundle.
    private ResourceBundle resourceBundle;

    /**
     * The constructor.
     */
    public EasyShellPlugin(IPluginDescriptor descriptor) {
        super(descriptor);
        plugin = this;
        try {
            resourceBundle= ResourceBundle.getBundle("org.easyexplore.EasyExplorePluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
    }

    /**
     * Returns the shared instance.
     */
    public static EasyShellPlugin getDefault() {
        return plugin;
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

 	public static ImageDescriptor getImageDescriptor(String id) {
 		return getDefault().getImageRegistry().getDescriptor(id);
 	}

    /**
     * Returns the workspace instance.
     */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    /**
     * Returns the string from the plugin's resource bundle,
     * or 'key' if not found.
     */
    public static String getResourceString(String key) {
        ResourceBundle bundle= EasyShellPlugin.getDefault().getResourceBundle();
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
     * Returns the plugin's resource bundle,
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    static public void log(Object msg) {
        ILog log = EasyShellPlugin.getDefault().getLog();
        Status status = new Status(IStatus.ERROR, EasyShellPlugin.getDefault().getDescriptor().getUniqueIdentifier(), IStatus.ERROR, msg + "\n", null);
        log.log(status);
    }

    static public void log(Throwable ex) {
        ILog log = EasyShellPlugin.getDefault().getLog();
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        String msg = stringWriter.getBuffer().toString();
        Status status = new Status(IStatus.ERROR, EasyShellPlugin.getDefault().getDescriptor().getUniqueIdentifier(), IStatus.ERROR, msg, null);
        log.log(status);
    }
    /**
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeDefaultPreferences(org.eclipse.jface.preference.IPreferenceStore)
     */
    protected void initializeDefaultPreferences(IPreferenceStore store) {
        EasyShellPreferencePage pref = new EasyShellPreferencePage(0);
        store = pref.getPreferenceStore();
        super.initializeDefaultPreferences(store);
    }

    /**
     * Return the target program setted in EasyExplorePreferencePage.
     * @return String
     */
    public String getTarget(int commandId, int instId) {
    	return getPreferenceStore().getString(EasyShellPreferencePage.getPreferenceString(commandId, instId));
    }

    /**
     * Return the quotes setted in EasyExplorePreferencePage.
     * @return EasyShellQuotes
     */
    public EasyShellQuotes getQuotes(int instId) {
    	String quotesStr = getPreferenceStore().getString(EasyShellPreferenceEntry.preferenceQuotes.getString(instId));
    	if (quotesStr != null && quotesStr.length() != 0)
    		return EasyShellQuotes.valueOf(quotesStr);
    	else
    		return EasyShellQuotes.quotesNo;
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
        String dbgStr = getPreferenceStore().getString(EasyShellPreferenceEntry.preferenceDebug.getString());
        if (dbgStr != null && dbgStr.length() != 0)
            return EasyShellDebug.valueOf(dbgStr) == EasyShellDebug.debugYes;
        else
            return false;
    }

    /**
     * Return the String Tokenizer Yes or No setted in EasyExplorePreferencePage.
     * @return boolean
     */
    public boolean isTokenizer(int instId) {
        String tokenizerStr = getPreferenceStore().getString(EasyShellPreferenceEntry.preferenceTokenizer.getString(instId));
        if (tokenizerStr != null && tokenizerStr.length() != 0)
            return EasyShellTokenizer.valueOf(tokenizerStr) == EasyShellTokenizer.EasyShellTokenizerYes;
        else
            return false;
    }

    public void sysout(boolean dbg, String str) {
        if (!dbg || (dbg && isDebug())) {
            System.out.println("[EasyShell] " + str);
        }
    }
}
