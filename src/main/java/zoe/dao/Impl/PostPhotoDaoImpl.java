package zoe.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import zoe.dao.PostPhotoDao;
import zoe.vo.PostPhoto;

public class PostPhotoDaoImpl implements PostPhotoDao {
    private DataSource ds;
    
    public PostPhotoDaoImpl() throws NamingException {
        try {
            Context context = new InitialContext();
            ds = (DataSource) context.lookup("java:comp/env/jdbc/server/foodhunter");
            System.out.println("PostPhotoDao 資料源連接成功");
        } catch (NamingException e) {
            System.err.println("PostPhotoDao 資料源連接失敗: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public boolean insertPostPhoto(PostPhoto photo) {
        String sql = "INSERT INTO post_photo (post_id, photo_file, created_time) VALUES (?, ?, ?)";
        
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, photo.getPostId());
            pstmt.setBytes(2, photo.getPhotoFile());
            pstmt.setTimestamp(3, photo.getCreatedTime());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("成功新增照片 - Post ID: " + photo.getPostId() + 
                                 ", Size: " + photo.getPhotoFile().length + " bytes");
                return true;
            } else {
                System.out.println("新增照片失敗 - Post ID: " + photo.getPostId());
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("新增照片時發生錯誤 - Post ID: " + photo.getPostId() + 
                             ", 錯誤訊息: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deletePhotosByPostId(Long postId) {
        String sql = "DELETE FROM post_photo WHERE post_id = ?";
        
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, postId);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("已刪除 " + rowsAffected + " 張照片 - Post ID: " + postId);
            
            return true;
            
        } catch (SQLException e) {
            System.err.println("刪除照片時發生錯誤 - Post ID: " + postId + 
                             ", 錯誤訊息: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<PostPhoto> getPhotosByPostId(Integer postId) {
        List<PostPhoto> photos = new ArrayList<>();
        String sql = "SELECT post_photo_id, post_id, photo_file, created_time " +
                    "FROM post_photo WHERE post_id = ?";
        
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, postId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PostPhoto photo = new PostPhoto();
                    photo.setPostPhotoId(rs.getInt("post_photo_id"));
                    photo.setPostId(rs.getInt("post_id"));
                    photo.setPhotoFile(rs.getBytes("photo_file"));
                    photo.setCreatedTime(rs.getTimestamp("created_time"));
                    
                    photos.add(photo);
                }
            }
            
            System.out.println("取得 " + photos.size() + " 張照片 - Post ID: " + postId);
            
        } catch (SQLException e) {
            System.err.println("查詢照片時發生錯誤 - Post ID: " + postId + 
                             ", 錯誤訊息: " + e.getMessage());
            e.printStackTrace();
        }
        
        return photos;
    }
}