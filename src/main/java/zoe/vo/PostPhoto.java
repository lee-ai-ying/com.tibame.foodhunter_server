package zoe.vo;

import java.sql.Timestamp;
import java.util.Base64;

public class PostPhoto {
    private Integer postPhotoId;    // 照片編號
    private Integer postId;         // 文章編號
    private byte[] photoFile;       // 照片檔案，改用 byte[] 儲存
    private Timestamp createdTime;  // 建立時間
    private String imgBase64Str; 
    
    // 預設建構函數
    public PostPhoto() {
    }
    
    // 完整建構函數
    public PostPhoto(Integer postPhotoId, Integer postId, 
                    byte[] photoFile, Timestamp createdTime) {
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
    
    public byte[] getPhotoFile() {
        return photoFile;
    }
    
    public void setPhotoFile(byte[] photoFile) {
        this.photoFile = photoFile;
    }
    
    // 新增：直接取得 Base64 格式的圖片字串
    public String getPhotoBase64() {
        if (photoFile != null) {
            return Base64.getEncoder().encodeToString(photoFile);
        }
        return null;
    }
    
    public Timestamp getCreatedTime() {
        return createdTime;
    }
    
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
    
    public String getImgBase64Str() {
        return imgBase64Str;
    }

    public void setImgBase64Str(String imgBase64Str) {
        this.imgBase64Str = imgBase64Str;
    }
    
    @Override
    public String toString() {
        return "PostPhoto{" +
                "postPhotoId=" + postPhotoId +
                ", postId=" + postId +
                // 顯示圖片大小而不是內容
                ", photoFile=" + (photoFile != null ? photoFile.length + " bytes" : "null") +
                ", createdTime=" + createdTime +
                '}';
    }
}