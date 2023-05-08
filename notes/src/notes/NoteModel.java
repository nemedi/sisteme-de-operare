package notes;

import java.io.Serializable;
import java.util.UUID;

public class NoteModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
    private String title;
    private String content;

    public NoteModel() {
        id = UUID.randomUUID().toString();
    }
    public NoteModel id(String id) {
        this.id = id;
        return this;
    }
    public NoteModel title(String title) {
        this.title = title;
        return this;
    }
    public NoteModel content(String content) {
        this.content = content;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return title;
    }

    public String toCsvString() {
        return String.format("%s|%s|%s", id, title, content);
    }
}
