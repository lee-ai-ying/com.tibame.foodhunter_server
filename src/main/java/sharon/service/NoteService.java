package sharon.service;

import java.util.List;

import sharon.vo.Note;

public interface NoteService {
    Note getNoteById(int id);
    
    List<Note> getAllNotes();

}
