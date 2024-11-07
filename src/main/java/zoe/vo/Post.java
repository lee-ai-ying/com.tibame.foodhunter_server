package zoe.vo;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    private String publisherNickname;
    private String restaurantName; 
    private List<PostPhoto> photos = new ArrayList<>();
	private byte[] publisherProfileImage;

    public Post() {
        this.photos = new ArrayList<>();
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
        this.photos = new ArrayList<>();
    }


    public List<PostPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PostPhoto> photos) {
        this.photos = photos;
    }


    public byte[] getPublisherProfileImage() {
        return publisherProfileImage;
    }

    public void setPublisherProfileImage(byte[] publisherProfileImage) {
        this.publisherProfileImage = publisherProfileImage;
    }
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



    public String getPublisherNickname() {
        return publisherNickname;
    }

    public void setPublisherNickname(String publisherNickname) {
        this.publisherNickname = publisherNickname;
    }
    
    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public void addPhoto(PostPhoto photo) {
        if (photo != null) {
            if (this.photos == null) {
                this.photos = new ArrayList<>();
            }
            System.out.println("添加照片到貼文 - Post ID: " + this.postId + 
                             ", Photo ID: " + photo.getPostPhotoId());
            this.photos.add(photo);
            System.out.println("當前照片數量: " + this.photos.size());
        }
    }

    

}