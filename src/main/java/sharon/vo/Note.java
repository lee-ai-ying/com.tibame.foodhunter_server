package sharon.vo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Note {
    private int noteId; // note_id
    private String title; // title
    private String content; // content
    private int restaurantId; // restaurant_id
    private Date selectedDate; // selected_date
    private int memberId; // member_id
    
    private String restaurantName; 
    
//    private List<NotePhoto> photos = new ArrayList<>();
//    
//    public Note() {
//        this.photos = new ArrayList<>();
//    }

    
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
	
    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

	
	
	public Date getSelectedDate() {
		return selectedDate;
	}
	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	
	@Override
	public String toString() {
	    return "Note{" +
	           "noteId=" + noteId +
	           ", title='" + title + '\'' +
	           ", content='" + content + '\'' +
	           ", restaurantId=" + restaurantId +
	           ", selectedDate=" + selectedDate +
	           ", memberId=" + memberId +
	           '}';
	}

}

