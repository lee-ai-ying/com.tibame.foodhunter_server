package member.vo;



import java.io.InputStream;
import java.sql.Timestamp;





public class Member {
private Integer id;
private String username;
private String password;
private String nickname;
private Boolean pass;
private String creator;
private String cPassword;
private Timestamp registrationdate;
private String updater;
private String phone;
private String email;
private String gender;
private Timestamp birthday;
private String birthday2;

public String getBirthday2() {
	return birthday2;
}

public void setBirthday2(String birthday2) {
	this.birthday2 = birthday2;
}
private String profileImageBase64;
private InputStream profileImageInputStream;
private String friend;
private String ProfileImage;
private String roomid;
private String message_id;
private Timestamp message_time;
public Timestamp getMessage_time() {
	return message_time;
}

public void setMessage_time(Timestamp message_time) {
	this.message_time = message_time;
}

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
private String receiver_id;
private String message;



public String getRoomid() {
	return roomid;
}

public void setRoomid(String roomid) {
	this.roomid = roomid;
}


public String getProfileImage() {
	return ProfileImage;
}

public void setProfileImage(String profileImage) {
	ProfileImage = profileImage;
}



public String getFriend() {
	return friend;
}
public void setFriend(String friend) {
	this.friend = friend;
}
public InputStream getProfileImageInputStream() {
	return profileImageInputStream;
}
public void setProfileImageInputStream(InputStream profileImageInputStream) {
	this.profileImageInputStream = profileImageInputStream;
}
public String getProfileImageBase64() {
	return profileImageBase64;
}
public void setProfileImageBase64(String profileImageBase64) {
	this.profileImageBase64 = profileImageBase64;
}
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}


public Timestamp getRegistrationdate() {
	return registrationdate;
}
public void setRegistrationdate(Timestamp registrationdate) {
	this.registrationdate = registrationdate;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getGender() {
	return gender;
}
public void setGender(String gender) {
	this.gender = gender;
}
public Timestamp getBirthday() {
	return birthday;
}
public void setBirthday(Timestamp birthday) {
	this.birthday = birthday;
}
public String getcPassword() {
	return cPassword;
}
public void setcPassword(String cPassword) {
	this.cPassword = cPassword;
}

public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getNickname() {
	return nickname;
}
public void setNickname(String nickname) {
	this.nickname = nickname;
}
public Boolean getPass() {
	return pass;
}
public void setPass(Boolean pass) {
	this.pass = pass;
}
public String getCreator() {
	return creator;
}
public void setCreator(String creator) {
	this.creator = creator;
}


public String getUpdater() {
	return updater;
}
public void setUpdater(String updater) {
	this.updater = updater;
}






}




