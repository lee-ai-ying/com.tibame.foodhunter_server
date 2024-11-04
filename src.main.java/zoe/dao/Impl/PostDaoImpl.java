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
import zoe.dao.PostDao;
import zoe.vo.Post;

public class PostDaoImpl implements PostDao {
    private DataSource ds;
    
    public PostDaoImpl() throws NamingException {
        try {
            Context context = new InitialContext();
            ds = (DataSource) context.lookup("java:comp/env/jdbc/server/foodhunter");
            System.out.println("資料源連接成功");
        } catch (NamingException e) {
            System.err.println("資料源連接失敗: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public List<Post> preLoadPost() {
        String sql = "SELECT p.post_id, p.post_tag, p.publisher, p.content, p.post_time, " +
                     "p.visibility, p.restaurantid, p.like_count, m.nickname, r.restaurant_name " +
                     "FROM post p " +
                     "LEFT JOIN member m ON p.publisher = m.member_id " +
                     "LEFT JOIN restaurant r ON p.restaurantid = r.restaurant_id " +
                     "ORDER BY p.post_time DESC LIMIT 10";

        List<Post> results = new ArrayList<>();

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("執行SQL查詢: " + sql);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post();
                    post.setPostId(rs.getInt("post_id"));
                    post.setPostTag(rs.getString("post_tag"));
                    post.setPublisher(rs.getInt("publisher"));
                    post.setContent(rs.getString("content"));
                    post.setPostTime(rs.getTimestamp("post_time"));
                    post.setVisibility(rs.getInt("visibility"));
                    post.setRestaurantId(rs.getInt("restaurantid"));
                    post.setLikeCount(rs.getInt("like_count"));
                    post.setPublisherNickname(rs.getString("nickname"));
                    post.setRestaurantName(rs.getString("restaurant_name"));

                    results.add(post);
                    System.out.println("讀取到貼文: ID=" + post.getPostId() +
                                     ", Content=" + post.getContent() +
                                     ", Publisher=" + post.getPublisherNickname() +
                                     ", Restaurant=" + post.getRestaurantName());
                }
            }

            System.out.println("總共讀取到 " + results.size() + " 筆貼文");
            return results;

        } catch (SQLException e) {
            System.err.println("SQL執行錯誤: " + e.getMessage());
            e.printStackTrace();
            return results;
        }
    }
    
    @Override
    public List<Post> selectByPostcontent(Post post) {
        String sql = "SELECT post_id, post_tag, publisher, content, post_time, " +
                    "visibility, restaurantid, like_count " +
                    "FROM post " +
                    "WHERE content LIKE ? OR post_tag LIKE ?";
        
        List<Post> results = new ArrayList<>();
        
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchContent = (post.getContent() != null) ? post.getContent() : "";
            String searchTag = (post.getPostTag() != null) ? post.getPostTag() : "";
            
            pstmt.setString(1, "%" + searchContent + "%");
            pstmt.setString(2, "%" + searchTag + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Post npost = new Post();
                    npost.setPostId(rs.getInt("post_id"));
                    npost.setPostTag(rs.getString("post_tag"));
                    npost.setPublisher(rs.getInt("publisher"));
                    npost.setContent(rs.getString("content"));
                    npost.setPostTime(rs.getTimestamp("post_time"));
                    npost.setVisibility(rs.getInt("visibility"));
                    npost.setRestaurantId(rs.getInt("restaurantid"));
                    npost.setLikeCount(rs.getInt("like_count"));
                    results.add(npost);
                }
            }
            return results;
        } catch (SQLException e) {
            System.err.println("執行查詢時發生錯誤: " + e.getMessage());
            e.printStackTrace();
            return results;
        }
    }

    @Override
    public boolean deletePost(Long postId) {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            // 開始交易
            conn.setAutoCommit(false);
            
            try {
                // 1. 先刪除相關的按讚記錄
                String deleteLikesSql = "DELETE FROM `like` WHERE post_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteLikesSql)) {
                    System.out.println("準備刪除貼文相關按讚，PostID: " + postId);
                    pstmt.setLong(1, postId);
                    int likesDeleted = pstmt.executeUpdate();
                    System.out.println("已刪除 " + likesDeleted + " 筆按讚記錄");
                }

                // 2. 刪除相關的留言
                String deleteMessagesSql = "DELETE FROM message WHERE post_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteMessagesSql)) {
                    System.out.println("準備刪除貼文相關留言，PostID: " + postId);
                    pstmt.setLong(1, postId);
                    int messagesDeleted = pstmt.executeUpdate();
                    System.out.println("已刪除 " + messagesDeleted + " 則相關留言");
                }

                // 3. 刪除相關的照片記錄
                String deletePhotosSql = "DELETE FROM post_photo WHERE post_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deletePhotosSql)) {
                    System.out.println("準備刪除貼文相關照片，PostID: " + postId);
                    pstmt.setLong(1, postId);
                    int photosDeleted = pstmt.executeUpdate();
                    System.out.println("已刪除 " + photosDeleted + " 張相關照片記錄");
                }

                // 4. 最後刪除貼文本身
                String deletePostSql = "DELETE FROM post WHERE post_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deletePostSql)) {
                    System.out.println("準備刪除貼文，ID: " + postId);
                    pstmt.setLong(1, postId);
                    int rowsAffected = pstmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        // 提交交易
                        conn.commit();
                        System.out.println("成功刪除貼文及其所有相關資料，ID: " + postId);
                        return true;
                    } else {
                        // 回滾交易
                        conn.rollback();
                        System.out.println("找不到要刪除的貼文，ID: " + postId);
                        return false;
                    }
                }
            } catch (SQLException e) {
                // 發生錯誤時回滾交易
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        System.err.println("回滾交易時發生錯誤: " + ex.getMessage());
                    }
                }
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("刪除貼文時發生錯誤，ID: " + postId + ", 錯誤訊息: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // 恢復自動提交設置並關閉連接
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("關閉資料庫連接時發生錯誤: " + e.getMessage());
                }
            }
        }
    }
    @Override
    public Post getPostById(Long postId) {
        String sql = "SELECT p.post_id, p.post_tag, p.publisher, p.content, p.post_time, " +
                    "p.visibility, p.restaurantid, p.like_count, m.nickname, r.restaurant_name " +
                    "FROM post p " +
                    "LEFT JOIN member m ON p.publisher = m.member_id " +
                    "LEFT JOIN restaurant r ON p.restaurantid = r.restaurant_id " +
                    "WHERE p.post_id = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            System.out.println("準備查詢貼文，ID: " + postId);
            pstmt.setLong(1, postId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Post post = new Post();
                    post.setPostId(rs.getInt("post_id"));
                    post.setPostTag(rs.getString("post_tag"));
                    post.setPublisher(rs.getInt("publisher"));
                    post.setContent(rs.getString("content"));
                    post.setPostTime(rs.getTimestamp("post_time"));
                    post.setVisibility(rs.getInt("visibility"));
                    post.setRestaurantId(rs.getInt("restaurantid"));
                    post.setLikeCount(rs.getInt("like_count"));
                    post.setPublisherNickname(rs.getString("nickname"));
                    post.setRestaurantName(rs.getString("restaurant_name"));
                    
                    System.out.println("成功查詢到貼文，ID: " + postId);
                    return post;
                } else {
                    System.out.println("找不到貼文，ID: " + postId);
                    return null;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("查詢貼文時發生錯誤，ID: " + postId + ", 錯誤訊息: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}