package ai_ying.vo;

import java.sql.Timestamp;

public class GroupChat {
	private Integer groupId;
	private Integer memberId;
	private String username;
	private String memberName;
	private String message;
	private Timestamp sendTime;
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "GroupChat [groupId=" + groupId + ", memberId=" + memberId + ", username=" + username + ", memberName="
				+ memberName + ", message=" + message + ", sendTime=" + sendTime + "]";
	}
}
