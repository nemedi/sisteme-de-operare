package cfs.client;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ClientConfiguration {

	private static final String PREFIX = "fs";
	private static final String NAME_KEY = PREFIX + ".name";
	private static final String ENDPOINT_SUFFIX = "endpoint";
	
	private static final String DEFAULT_NAME = "cfs";
	
	public static final String NAME;
	public static final Map<String, String> ENDPOINT_MAP;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		NAME = bundle.containsKey(NAME_KEY) ? bundle.getString(NAME_KEY) : DEFAULT_NAME;
		ENDPOINT_MAP = new HashMap<String, String>();
		for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements();) {
			String key = e.nextElement();
			int index = key.indexOf("." + ENDPOINT_SUFFIX);
			if (key.startsWith(PREFIX + ".") && index > PREFIX.length() + 1) {
				ENDPOINT_MAP.put(key.substring(PREFIX.length() + 1, index), bundle.getString(key));
			}
		}
	}
}
