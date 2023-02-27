package chat.common;

import java.util.ResourceBundle;

public class Settings {
	
	public static final String HOST;
	public static final int PORT;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("settings");
		HOST = bundle.containsKey("host")
				? bundle.getString("host")
				: "localhost";
		PORT = bundle.containsKey("port")
				? Integer.parseInt(bundle.getString("port"))
				: 1979;
	}

}
