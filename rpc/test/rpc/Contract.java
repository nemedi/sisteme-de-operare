package rpc;

import java.util.Collection;

public interface Contract {
	
	Collection<Note> getNotes();
	Note getNote(String id);
	Note addNote(String title, String content);
	Note changeNote(String id, String title, String content);
	boolean removeNote(String id);
	
}
