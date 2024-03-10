package ipc;

import java.util.ResourceBundle;

public class Settings {

	public static final String FILE_NAME;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("settings");
		FILE_NAME = bundle.getString("file");
	}
}
