package ai_ying.vo;

public class FcmToken {
	private Integer memberId;
	private String username;
	private String fcmToken;
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public String getFcmToken() {
		return fcmToken;
	}
	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "FcmToken [memberId=" + memberId + ", username=" + username + ", fcmToken=" + fcmToken + "]";
	}
}
