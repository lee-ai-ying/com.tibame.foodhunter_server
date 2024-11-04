package zoe.vo;

import java.sql.Blob;
import java.sql.Timestamp;

public class PostPhoto {
    private Integer postPhotoId;    // 照片編號
    private Integer postId;         // 文章編號
    private Blob photoFile;         // 照片檔案
    private Timestamp createdTime;  // 建立時間

    // 預設建構函數
    public PostPhoto() {
    }

    // 完整建構函數
    public PostPhoto(Integer postPhotoId, Integer postId, 
                    Blob photoFile, Timestamp createdTime) {
        this.postPhotoId = postPhotoId;
        this.postId = postId;
        this.photoFile = photoFile;
        this.createdTime = createdTime;
    }

    // Getter 和 Setter 方法
    public Integer getPostPhotoId() {
        return postPhotoId;
    }

    public void setPostPhotoId(Integer postPhotoId) {
        this.postPhotoId = postPhotoId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Blob getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(Blob photoFile) {
        this.photoFile = photoFile;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "PostPhoto{" +
                "postPhotoId=" + postPhotoId +
                ", postId=" + postId +
                // 不直接輸出 Blob 資料，避免資料量過大
                ", photoFile=" + (photoFile != null ? "[BLOB]" : "null") +
                ", createdTime=" + createdTime +
                '}';
    }
}