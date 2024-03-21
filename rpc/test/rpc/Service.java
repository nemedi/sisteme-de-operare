package rpc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Service implements Contract {
	
	private Map<String, Note> notes;
	
	public Service() {
		notes = new HashMap<String, Note>();
	}

	@Override
	public Collection<Note> getNotes() {
		return Collections.unmodifiableCollection(notes.values());
	}

	@Override
	public Note getNote(String id) {
		return id != null && notes.containsKey(id)?
				notes.get(id) : null;
	}

	@Override
	public Note addNote(String title, String content) {
		Note note = null;
		if (title != null && content != null) {
			note = new Note(title, content);
			notes.put(note.getId(), note);
			return note;
		} else {
			return null;
		}
	}

	@Override
	public Note changeNote(String id, String title, String content) {
		if (id != null
				&& title != null
				&& content != null && notes.containsKey(id)) {
			Note note = notes.get(id);
			note.setTitle(title);
			note.setContent(content);
			return note;
		} else {
			return null;
		}
	}

	@Override
	public boolean removeNote(String id) {
		if (id != null && notes.containsKey(id)) {
			notes.remove(id);
			return true;
		} else {
			return false;
		}
	}

}
