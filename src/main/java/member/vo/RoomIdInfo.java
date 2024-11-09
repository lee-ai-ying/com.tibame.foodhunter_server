package member.vo;

public class RoomIdInfo {
	
    private String roomid;
    private String username;
    private String nickname;

	public String getRoomid() {
		return roomid;
	}

	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	private String profileImageBase64;
   

	// 构造函数
    public RoomIdInfo(String roomid,String username,String nickname, String profileImageBase64) {	
    	this.roomid = roomid;
    	this.username = username;
        this.nickname = nickname;
        this.profileImageBase64 = profileImageBase64;
    }

    // Getter 和 Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageBase64() {
        return profileImageBase64;
    }

    public void setProfileImageBase64(String profileImageBase64) {
        this.profileImageBase64 = profileImageBase64;
    }
}
