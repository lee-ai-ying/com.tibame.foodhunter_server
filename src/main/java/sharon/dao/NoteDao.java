package sharon.dao;

import java.util.List;

import sharon.vo.Note;

// 創建接口，定義查詢方法
public interface NoteDao {
	Note selectById(int id); //根據 id 查詢筆記
    List<Note> getAllNotes();

}
