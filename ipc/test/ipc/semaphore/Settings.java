package ipc.semaphore;

import java.util.ResourceBundle;

public class Settings {

	public static final String SEMAPHORE_NAME;
	public static final int SEMAPHORE_CAPACITY;
	public static final int WORK_DURATION;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle(Settings.class.getName().toLowerCase());
		SEMAPHORE_NAME = bundle.getString("semaphore.name");
		SEMAPHORE_CAPACITY = Integer.parseInt(bundle.getString("semaphore.capacity"));
		WORK_DURATION = Integer.parseInt(bundle.getString("work.duration"));
	}
}