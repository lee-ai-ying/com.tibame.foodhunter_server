package sharon.vo;

import java.sql.Date;

public class Group {
	private int groupId;
    private String groupName;
    private String restaurantName;
    private String restaurantAddress;
    private int isPublic;
    private Date groupDate;
    private int memberId;

	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public String getRestaurantName() {
        return restaurantName;
    }
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
    
    public String getRestaurantAddress() {
        return restaurantAddress;
    }
    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }
    
    public int getIsPublic() {
        return isPublic;
    }
    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }
    
    public Date getGroupDate() {
        return groupDate;
    }
    public void setGroupDate(Date groupDate) {
        this.groupDate = groupDate;
    }
    
    public int getMemberId() {
        return memberId;
    }
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "Group {\n" +
               "  groupName='" + groupName + "',\n" +
               "  restaurantName='" + restaurantName + "',\n" +
               "  restaurantAddress='" + restaurantAddress + "',\n" +
               "  isPublic=" + isPublic + ",\n" +
               "  groupDate=" + groupDate + ",\n" +
               "  memberId=" + memberId + "\n" +
               "}";
    }

}