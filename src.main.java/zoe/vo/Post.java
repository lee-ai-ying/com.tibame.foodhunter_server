package zoe.vo;


import java.sql.Timestamp;

public class Post {
    private Integer postId;        // 留言編號
    private String postTag;        // 文章標籤
    private Integer publisher;     // 發佈者
    private String content;        // 文章內容
    private Timestamp messageTime; // 留言時間
    private Timestamp postTime;    // 建立時間
    private Integer visibility;    // 可見性
    private Integer restaurantId;  // 餐廳編號
    private Integer likeCount;     // 按讚總數

    // 預設建構函數
    public Post() {
    }

    // 完整建構函數
    public Post(Integer postId, String postTag, Integer publisher, String content,
                Timestamp messageTime, Timestamp postTime, Integer visibility,
                Integer restaurantId, Integer likeCount) {
        this.postId = postId;
        this.postTag = postTag;
        this.publisher = publisher;
        this.content = content;
        this.messageTime = messageTime;
        this.postTime = postTime;
        this.visibility = visibility;
        this.restaurantId = restaurantId;
        this.likeCount = likeCount;
    }

    // Getter 和 Setter 方法
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getPostTag() {
        return postTag;
    }

    public void setPostTag(String postTag) {
        this.postTag = postTag;
    }

    public Integer getPublisher() {
        return publisher;
    }

    public void setPublisher(Integer publisher) {
        this.publisher = publisher;
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

    public Timestamp getPostTime() {
        return postTime;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", postTag='" + postTag + '\'' +
                ", publisher=" + publisher +
                ", content='" + content + '\'' +
                ", messageTime=" + messageTime +
                ", postTime=" + postTime +
                ", visibility=" + visibility +
                ", restaurantId=" + restaurantId +
                ", likeCount=" + likeCount +
                '}';
    }
}