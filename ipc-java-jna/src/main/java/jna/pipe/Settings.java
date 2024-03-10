package jna.pipe;

import java.util.ResourceBundle;

public class Settings {

	public static final String PIPE_NAME;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle(Settings.class.getName().toLowerCase());
		PIPE_NAME = bundle.getString("pipe.name");
	}
}