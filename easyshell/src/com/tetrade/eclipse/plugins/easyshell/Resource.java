package com.tetrade.eclipse.plugins.easyshell;

import java.io.File;

public class Resource {
	
	private File file = null;
	private String projectName = null;

	public Resource(Resource myRes) {
		file = myRes.getFile();
		projectName = myRes.getProjectName();
	}

	public Resource(File myFile, String myProjectName) {
		file = myFile;
		projectName = myProjectName;
	}
	
	public File getFile() {
		return file;
	}

	public String getProjectName() {
		return projectName;
	}

}
