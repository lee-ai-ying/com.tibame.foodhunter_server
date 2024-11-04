package zoe.vo;

import java.sql.Timestamp;

public class Comment {
    private Integer messageId; // 留言編號
    private Integer postId; // 貼文編號
    private Integer memberId; // 會員編號
    private String content; // 留言內容
    private Timestamp messageTime; // 留言時間
    private String memberNickname; // 會員暱稱

    // 預設建構函數
    public Comment() {
    }

    // 完整建構函數
    public Comment(Integer messageId, Integer postId, Integer memberId, 
                  String content, Timestamp messageTime, String memberNickname) {
        this.messageId = messageId;
        this.postId = postId;
        this.memberId = memberId;
        this.content = content;
        this.messageTime = messageTime;
        this.memberNickname = memberNickname;
    }

    // Getter 和 Setter 方法
    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Timestamp messageTime) {
        this.messageTime = messageTime;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }
}