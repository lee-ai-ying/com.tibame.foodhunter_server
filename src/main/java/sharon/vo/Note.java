package sharon.vo;

import java.sql.Timestamp;

public class Note {
    private int noteId; // note_id
    private String title; // title
    private String content; // content
    private int restaurantId; // restaurant_id
    private Timestamp selectedDate; // selected_date
    private int memberId; // member_id
    
	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}
	public Timestamp getSelectedDate() {
		return selectedDate;
	}
	public void setSelectedDate(Timestamp selectedDate) {
		this.selectedDate = selectedDate;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
}

