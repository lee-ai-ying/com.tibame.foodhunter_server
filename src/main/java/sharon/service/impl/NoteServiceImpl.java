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
        if (id <= 0) {
            return null;
        }
        return noteDao.selectById(id);
    }

    @Override
    public List<Note> getAllNotes(int memberId) {
        return noteDao.selectAllNotes(memberId);
    }

    @Override
    public String createNote(Note note) {
        // 必填欄位檢查
        if (note == null || note.getTitle() == null || note.getTitle().isEmpty()) {
            throw new IllegalArgumentException("沒有標題");
        }
        if (note.getSelectedDate() == null) {
            throw new IllegalArgumentException("沒有日期");
        }
        
        int noteId = noteDao.insertNote(note);
        
        if (noteId > 0) {
            note.setNoteId(noteId);  // 設置生成的 noteId 回到 note 物件中
            return null;  // 返回 null 表示成功
        } else {
            return "創建筆記失敗";  // 返回錯誤訊息
        }
    }

    @Override
    public String updateNote(Note note) {
        // 檢查 note 物件和必填欄位
        if (note == null || note.getNoteId() <= 0) {
            throw new IllegalArgumentException("無效的筆記 ID");
        }
        if (note.getTitle() == null || note.getTitle().isEmpty()) {
            throw new IllegalArgumentException("標題不可為空");
        }
        if (note.getSelectedDate() == null) {
            throw new IllegalArgumentException("日期不可為空");
        }

        int result = noteDao.updateNote(note);
        
        // 返回 null 表示成功，返回錯誤訊息表示失敗
        return result > 0 ? null : "更新筆記失敗";
    }

    @Override
    public int deleteNoteById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("無效的筆記 ID");
        }

        // 調用 DAO 層刪除方法並返回影響的行數
        return noteDao.deleteNoteById(id);
    }
}
