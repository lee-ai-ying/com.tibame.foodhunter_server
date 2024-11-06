package sharon.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		try(Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					Note note = new Note();
					note.setNoteId(rs.getInt("note_id"));
					note.setTitle(rs.getString("title"));
                    note.setContent(rs.getString("content"));
                    note.setRestaurantId(rs.getInt("restaurant_id"));
                    note.setSelectedDate(rs.getTimestamp("selected_date"));
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
	public List<Note> getAllNotes() {
		String sql = "SELECT * FROM note";
		List<Note> notes = new ArrayList<>();

		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
		     ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				Note note = new Note();
				note.setNoteId(rs.getInt("note_id"));
				note.setTitle(rs.getString("title"));
				note.setContent(rs.getString("content"));
				note.setRestaurantId(rs.getInt("restaurant_id"));
				note.setSelectedDate(rs.getTimestamp("selected_date"));
				note.setMemberId(rs.getInt("member_id"));
				notes.add(note); // 添加到列表中
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notes;
	}
}
	
