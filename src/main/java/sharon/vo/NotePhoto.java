package sharon.vo;

import java.sql.Timestamp;
import java.util.Base64;

public class NotePhoto {
    private Integer notePhotoId;    // 照片編號
    private Integer noteId;         // 筆記編號
    private byte[] photoFile;       // 照片檔案，改用 byte[] 儲存
    private Timestamp createdTime;  // 建立時間
    private String imgBase64Str; 
    
    // 預設建構函數
    public NotePhoto() {
    }
    
    // 完整建構函數 (若不需要可以移除)
    public NotePhoto(Integer notePhotoId, Integer noteId, 
                    byte[] photoFile, Timestamp createdTime) {
        this.notePhotoId = notePhotoId;
        this.noteId = noteId;
        this.photoFile = photoFile;
        this.createdTime = createdTime;
    }
    
    // Getter 和 Setter 方法
    public Integer getNotePhotoId() {
        return notePhotoId;
    }
    
    public void setNotePhotoId(Integer notePhotoId) {
        this.notePhotoId = notePhotoId;
    }
    
    public Integer getNoteId() {
        return noteId;
    }
    
    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
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
        return "NotePhoto{" +
                "notePhotoId=" + notePhotoId +
                ", noteId=" + noteId +
                ", photoFile=" + (photoFile != null ? photoFile.length + " bytes" : "null") +
                ", createdTime=" + createdTime +
                '}';
    }
}
