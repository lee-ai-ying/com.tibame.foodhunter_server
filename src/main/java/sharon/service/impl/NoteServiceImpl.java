package sharon.service.impl;

import java.util.List;

import javax.naming.NamingException;

import sharon.dao.NoteDao;
import sharon.dao.impl.NoteDaoImpl;
import sharon.service.NoteService;
import sharon.vo.Note;

public class NoteServiceImpl implements NoteService {
    private NoteDao noteDao;

    public NoteServiceImpl() throws NamingException {
        noteDao = new NoteDaoImpl();
    }

    @Override
    public Note getNoteById(int id) {
        return noteDao.selectById(id);
    }

	@Override
	public List<Note> getAllNotes() {
		// TODO Auto-generated method stub
		return noteDao.getAllNotes();
	}
}