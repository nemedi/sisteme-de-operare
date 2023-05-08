package notes;

import java.util.List;

public interface NoteContract {
    NoteModel createNote(String title, String content) throws Exception;
    List<NoteModel> readNotes() throws Exception;
    boolean updateNote (NoteModel note) throws Exception;
    boolean deleteNote (String id) throws Exception;
}
