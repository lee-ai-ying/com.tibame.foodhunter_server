package sharon.service;

import sharon.vo.Note;
import java.util.List;

public interface NoteService {
	 
    Note getNoteById(int id);

    List<Note> getAllNotes(int memberId);
    
    String createNote(Note note);

    String updateNote(Note note);

    int deleteNoteById(int id);
}
