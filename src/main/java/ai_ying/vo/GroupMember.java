package ai_ying.vo;

public class GroupMember {
	private Integer groupId;
	private Integer memberId;
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
	@Override
	public String toString() {
		return "GroupMember [groupId=" + groupId + ", memberId=" + memberId + "]";
	}
}
