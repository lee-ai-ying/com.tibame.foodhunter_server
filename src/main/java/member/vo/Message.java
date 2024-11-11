package member.vo;

import java.sql.Timestamp;

public class Message {
	private String message_id;
    private String receiver_id;
    private String message;
    private Timestamp message_time;
    
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public String getReceiver_id() {
		return receiver_id;
	}
	public void setReceiver_id(String receiver_id) {
		this.receiver_id = receiver_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getMessage_time() {
		return message_time;
	}
	public void setMessage_time(Timestamp message_time) {
		this.message_time = message_time;
	}

	// 构造函数
    public Message(String message_id,String receiver_id,String message,Timestamp message_time) {	
    	this.message_id = message_id;
    	this.receiver_id = receiver_id;
        this.message = message;
        this.message_time = message_time;
    }
}
