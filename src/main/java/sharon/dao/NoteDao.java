package sharon.dao;

import java.util.List;

import sharon.vo.Note;

// 創建接口，定義查詢方法
public interface NoteDao {
	Note selectById(int id); //根據 id 查詢筆記
    List<Note> selectAllNotes(); //返回所有筆記的列表，如果沒有筆記則返回空列表
    
    int insertNote(Note note); // 返回新增的筆記 ID 
    int updateNote(Note note); // 返回更新的筆記 ID
    int deleteNoteById(int id); // 根據 id 刪除筆記
}
