package zoe.vo;



import java.sql.Timestamp;

public class Message {
    private Integer messageId;    // 留言編號
    private Integer postId;       // 貼文編號
    private Integer member;       // 會員編號
    private String content;       // 留言內容
    private Timestamp messageTime; // 留言時間

    // 預設建構函數
    public Message() {
    }

    // 完整建構函數
    public Message(Integer messageId, Integer postId, Integer member, 
                  String content, Timestamp messageTime) {
        this.messageId = messageId;
        this.postId = postId;
        this.member = member;
        this.content = content;
        this.messageTime = messageTime;
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

    public Integer getMember() {
        return member;
    }

    public void setMember(Integer member) {
        this.member = member;
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

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", postId=" + postId +
                ", member=" + member +
                ", content='" + content + '\'' +
                ", messageTime=" + messageTime +
                '}';
    }
}