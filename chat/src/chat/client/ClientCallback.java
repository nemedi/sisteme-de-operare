package chat.client;

public interface ClientCallback {
	
	void onAccept(String[] names);
	
	void onDeny();
	
	void onAddUser(String name);
	
	void onRemoveUser(String name);
	
	void onReceive(String from, String text);
	
	void onExit();

}
