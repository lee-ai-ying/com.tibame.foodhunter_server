package sharon.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import sharon.dao.NoteDao;
import sharon.vo.Note;

public class NoteDaoImpl implements NoteDao {
	private DataSource ds;
	
	public NoteDaoImpl() throws NamingException {
		ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/server/foodhunter");
	}
	
	@Override
	public Note selectById(int id) {
		String sql = "select * from note where note_id = ?";
		try(
			Connection conn = ds.getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(sql)
		) {
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					Note note = new Note();
					note.setNoteId(rs.getInt("note_id"));
					note.setTitle(rs.getString("title"));
                    note.setContent(rs.getString("content"));
                    note.setRestaurantId(rs.getInt("restaurant_id"));
                    note.setSelectedDate(rs.getDate("selected_date"));
                    note.setMemberId(rs.getInt("member_id"));
                	return note;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<Note> selectAllNotes() {
		String sql = "SELECT * FROM note";
		List<Note> notes = new ArrayList<>();

		try (
			Connection conn = ds.getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(sql);
		    ResultSet rs = pstmt.executeQuery()
		 ) {
			while (rs.next()) {
				Note note = new Note();
				note.setNoteId(rs.getInt("note_id"));
				note.setTitle(rs.getString("title"));
				note.setContent(rs.getString("content"));
				note.setRestaurantId(rs.getInt("restaurant_id"));
				note.setSelectedDate(rs.getDate("selected_date"));
				note.setMemberId(rs.getInt("member_id"));
				notes.add(note); // 添加到列表中
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notes;
	}

	@Override
	public int insertNote(Note note) {
		String sql = "insert into note(title, content, restaurant_id, selected_date, member_id)"
				+ " values(?, ?, ?, ?, ?)";
		try (
			Connection conn = ds.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					sql, 
					Statement.RETURN_GENERATED_KEYS) // 返回新增的筆記 ID
		) {
			int paramIndex = 1;
		    pstmt.setString(paramIndex++, note.getTitle());
		    pstmt.setString(paramIndex++, note.getContent());
		    pstmt.setInt(paramIndex++, note.getRestaurantId());
		    pstmt.setDate(paramIndex++, note.getSelectedDate());
		    pstmt.setInt(paramIndex++, note.getMemberId());
		    
		    int result = pstmt.executeUpdate();
	        if (result > 0) {
	            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    return generatedKeys.getInt(1);  // 返回生成的 noteId
	                }
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int updateNote(Note note) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteNoteById(int id) {
	    String sql = "DELETE FROM note WHERE note_id = ?";
	    try (
	        Connection conn = ds.getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(sql)
	    ) {
	        pstmt.setInt(1, id);
	        int rowsAffected = pstmt.executeUpdate(); // 執行刪除並獲取影響的行數
	        return rowsAffected; // 返回受影響的行數
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return 0; // 如果出現異常，返回 0 表示未刪除任何資料
	}

}
	
