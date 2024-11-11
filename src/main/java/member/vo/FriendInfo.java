package member.vo;

public class FriendInfo {
	private Integer member_id;
	

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	private String username;
    private String nickname;
    

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	private String profileImageBase64;
   

	// 构造函数
    public FriendInfo(Integer member_id,String username,String nickname, String profileImageBase64) {	
    	this.member_id = member_id;
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
