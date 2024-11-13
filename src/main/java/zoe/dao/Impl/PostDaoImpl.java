package zoe.dao.Impl;


import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import zoe.dao.PostDao;
import zoe.vo.Post;
import zoe.vo.PostPhoto;

public class PostDaoImpl implements PostDao {
    private DataSource ds;
    
    
    
    private static final String PHOTO_SQL = 
            "SELECT post_photo_id, post_id, photo_file, created_time " +
            "FROM post_photo WHERE post_id = ?";
        
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
        
        // 為貼文加載照片的輔助方法
        private void loadPhotosForPost(Connection conn, Post post) throws SQLException {
            try (PreparedStatement photoStmt = conn.prepareStatement(PHOTO_SQL)) {
                photoStmt.setInt(1, post.getPostId());
                ResultSet photoRs = photoStmt.executeQuery();
                
                while (photoRs.next()) {
                    PostPhoto photo = new PostPhoto();
                    photo.setPostPhotoId(photoRs.getInt("post_photo_id"));
                    photo.setPostId(photoRs.getInt("post_id"));
                    
                    // 直接讀取為 byte[]
                    byte[] photoBytes = photoRs.getBytes("photo_file");
                    photo.setPhotoFile(photoBytes);
                    
                    photo.setCreatedTime(photoRs.getTimestamp("created_time"));
                    post.addPhoto(photo);
                }
            }
        }
    
        @Override
        public List<Post> preLoadPost() {
            String sql = "SELECT p.post_id, p.post_tag, p.publisher, p.content, p.post_time, " +
                        "p.visibility, p.restaurantid, p.like_count, m.nickname, r.restaurant_name, " +
                        "m.profileimage " +
                        "FROM post p " +
                        "LEFT JOIN member m ON p.publisher = m.member_id " +
                        "LEFT JOIN restaurant r ON p.restaurantid = r.restaurant_id " +
                        "ORDER BY p.post_time DESC LIMIT 20";

            List<Post> results = new ArrayList<>();

            try (Connection conn = ds.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                System.out.println("執行貼文查詢 SQL: " + sql);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Post post = new Post();
                        int postId = rs.getInt("post_id");
                        post.setPostId(postId);
                        post.setPostTag(rs.getString("post_tag"));
                        post.setPublisher(rs.getInt("publisher"));
                        post.setContent(rs.getString("content"));
                        post.setPostTime(rs.getTimestamp("post_time"));
                        post.setVisibility(rs.getInt("visibility"));
                        post.setRestaurantId(rs.getInt("restaurantid"));
                        post.setLikeCount(rs.getInt("like_count"));
                        post.setPublisherNickname(rs.getString("nickname"));
                        post.setRestaurantName(rs.getString("restaurant_name"));

                        // 處理頭像並加入日誌
                        byte[] profileImage = rs.getBytes("profileimage");
                        if (profileImage != null) {
                            post.setPublisherProfileImage(profileImage);
                            System.out.println("成功讀取用戶頭像 - " +
                                "User ID: " + post.getPublisher() + 
                                ", Nickname: " + post.getPublisherNickname() + 
                                ", Image Size: " + profileImage.length + " bytes");
                        } else {
                            System.out.println("用戶 " + post.getPublisher() + " (" + 
                                post.getPublisherNickname() + ") 沒有頭像數據");
                        }

                        // 加載貼文照片
                        loadPhotosForPost(conn, post);

                        System.out.println("完成貼文處理 - " +
                            "ID: " + postId + 
                            ", Publisher: " + post.getPublisherNickname() +
                            ", Has Profile Image: " + (post.getPublisherProfileImage() != null) +
                            ", Photos Count: " + post.getPhotos().size());

                        results.add(post);
                    }
                }

                System.out.println("\n總共讀取到 " + results.size() + " 篇貼文");
                return results;

            } catch (SQLException e) {
                System.err.println("SQL執行錯誤: " + e.getMessage());
                e.printStackTrace();
                return results;
            }
        }
        @Override
        public List<Post> selectByPostcontent(Post post) {
            String sql = "SELECT p.post_id, p.post_tag, p.publisher, p.content, p.post_time, " +
                        "p.visibility, p.restaurantid, p.like_count, " +
                        "m.nickname as publisher_nickname, r.restaurant_name, " +
                        "m.profileimage " +  // 添加 profileimage
                        "FROM post p " +
                        "LEFT JOIN member m ON p.publisher = m.member_id " +
                        "LEFT JOIN restaurant r ON p.restaurantid = r.restaurant_id " +
                        "WHERE p.content LIKE ? OR p.post_tag LIKE ?";
            
            List<Post> results = new ArrayList<>();
            
            try (Connection conn = ds.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 PreparedStatement photoStmt = conn.prepareStatement(PHOTO_SQL)) {
                
                String searchContent = (post.getContent() != null) ? post.getContent() : "";
                String searchTag = (post.getPostTag() != null) ? post.getPostTag() : "";
                
                pstmt.setString(1, "%" + searchContent + "%");
                pstmt.setString(2, "%" + searchTag + "%");
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Post npost = new Post();
                        // ... 原有的設置保持不變 ...
                        npost.setPostId(rs.getInt("post_id"));
                        npost.setPostTag(rs.getString("post_tag"));
                        npost.setPublisher(rs.getInt("publisher"));
                        npost.setContent(rs.getString("content"));
                        npost.setPostTime(rs.getTimestamp("post_time"));
                        npost.setVisibility(rs.getInt("visibility"));
                        npost.setRestaurantId(rs.getInt("restaurantid"));
                        npost.setLikeCount(rs.getInt("like_count"));
                        npost.setPublisherNickname(rs.getString("publisher_nickname"));
                        npost.setRestaurantName(rs.getString("restaurant_name"));
                        
                        // 添加頭像
                        byte[] profileImage = rs.getBytes("profileimage");
                        npost.setPublisherProfileImage(profileImage);
                        
                        // 加載貼文照片
                        loadPhotosForPost(conn, npost);
                        
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
        public Post getPostById(Long postId) {
            String sql = "SELECT p.post_id, p.post_tag, p.publisher, p.content, p.post_time, " +
                        "p.visibility, p.restaurantid, p.like_count, m.nickname, r.restaurant_name, " +
                        "m.profileimage " +  // 添加 profileimage
                        "FROM post p " +
                        "LEFT JOIN member m ON p.publisher = m.member_id " +
                        "LEFT JOIN restaurant r ON p.restaurantid = r.restaurant_id " +
                        "WHERE p.post_id = ?";

            try (Connection conn = ds.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
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

                        // 添加頭像
                        byte[] profileImage = rs.getBytes("profileimage");
                        post.setPublisherProfileImage(profileImage);

                        // 加載照片
                        loadPhotosForPost(conn, post);
                        
                        return post;
                    }
                    return null;
                }
                
            } catch (SQLException e) {
                System.err.println("查詢貼文時發生錯誤，ID: " + postId + ", 錯誤訊息: " + e.getMessage());
                e.printStackTrace();
                return null;
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
    public Integer insertPost(Post post) {
        String sql = "INSERT INTO post (post_tag, publisher, content, post_time, visibility, restaurantid, like_count) " +
                     "VALUES (?, ?, ?, ?, 0, ?, 0)";
        
        Integer newPostId = null;
        
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // 設置參數，直接使用原始字串，不需要額外編碼轉換
            int paramIndex = 1;
            pstmt.setString(paramIndex++, post.getPostTag());
            pstmt.setInt(paramIndex++, post.getPublisher());
            pstmt.setString(paramIndex++, post.getContent());
            pstmt.setTimestamp(paramIndex++, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(paramIndex++, post.getRestaurantId());
            
            System.out.println("準備新增貼文 - " + 
                             "\nTag: " + post.getPostTag() + 
                             "\nPublisher: " + post.getPublisher() + 
                             "\nContent: " + post.getContent() +
                             "\nRestaurantId: " + post.getRestaurantId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        newPostId = rs.getInt(1);
                        System.out.println("成功新增貼文，ID: " + newPostId);
                    }
                }
            } else {
                System.out.println("新增貼文失敗，沒有行被影響");
            }
            
        } catch (SQLException e) {
            System.err.println("新增貼文時發生錯誤: " + e.getMessage());
            e.printStackTrace();
        }
        
        return newPostId;
    }
    
    @Override
    public Integer updatePost(Post post) {
        String sql = "UPDATE post SET " +
                    "post_tag = ?, " +
                    "content = ? " +
                    "WHERE post_id = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 設置更新參數 - 只更新標籤和內容
            pstmt.setString(1, post.getPostTag());
            pstmt.setString(2, post.getContent());
            pstmt.setInt(3, post.getPostId());

            System.out.println("準備更新貼文 - " +
                    "\nID: " + post.getPostId() +
                    "\nTag: " + post.getPostTag() +
                    "\nContent: " + post.getContent() +
                    "\n原有資料保持不變: " +
                    "\nPublisher: " + post.getPublisher() +
                    "\nVisibility: " + post.getVisibility() +
                    "\nRestaurantId: " + post.getRestaurantId() +
                    "\nLike Count: " + post.getLikeCount() +
                    "\n照片數量: " + (post.getPhotos() != null ? post.getPhotos().size() : 0));

            // 執行更新
            int rowsAffected = pstmt.executeUpdate();

            // 移除照片更新相關代碼，保持原有照片不變
            
            if (rowsAffected > 0) {
                System.out.println("成功更新貼文內容和標籤，ID: " + post.getPostId());
                return rowsAffected;
            } else {
                System.out.println("更新貼文失敗，找不到指定的貼文，ID: " + post.getPostId());
                return 0;
            }

        } catch (SQLException e) {
            System.err.println("更新貼文時發生錯誤，ID: " + post.getPostId() + ", 錯誤訊息: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
	@Override
	public List<Post> selectPostByRestId(Integer restId) {
		String sql = "SELECT p.post_id, p.post_tag, p.publisher, p.content, p.post_time, " +
                "p.visibility, p.restaurantid, p.like_count, m.nickname, r.restaurant_name, " +
                "m.profileimage " +  // 添加 profileimage
                "FROM post p " +
                "LEFT JOIN member m ON p.publisher = m.member_id " +
                "LEFT JOIN restaurant r ON p.restaurantid = r.restaurant_id " +
                "WHERE p.restaurantid = ?";

    try (Connection conn = ds.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, restId);
        List<Post> results = new ArrayList<>();
        try (ResultSet rs = pstmt.executeQuery()) {
            while(rs.next()) {
                Post post = new Post();
                post.setPostId(rs.getInt("post_id"));
                post.setPublisher(rs.getInt("publisher"));
                post.setContent(rs.getString("content"));
                post.setVisibility(rs.getInt("visibility"));
                post.setRestaurantId(rs.getInt("restaurantid"));
                post.setPublisherNickname(rs.getString("nickname"));
                post.setRestaurantName(rs.getString("restaurant_name"));

                // 添加頭像
                byte[] profileImage = rs.getBytes("profileimage");
                post.setPublisherProfileImage(profileImage);

                // 加載照片
                loadPhotosForPostOnlyOne(conn, post);
                results.add(post);
            }
            return results;
        }
        
    } catch (SQLException e) {
        System.err.println("查詢貼文時發生錯誤，ID: " + restId + ", 錯誤訊息: " + e.getMessage());
        e.printStackTrace();
        return null;
    	}
	}

	@Override
	public String getUsernameByMemberId(Integer memberId) {
	    String sql = "SELECT username FROM member WHERE member_id = ?";
	
	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, memberId);
	      
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                String username = rs.getString("username");
	                System.out.println("查詢成功，找到 username: " + username);
	                return username;
	            } else {
	                System.out.println("查詢結果為空，沒有找到對應的 username");
	                return null;
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("=== SQL執行錯誤 ===");
	  
	        e.printStackTrace();
	        return null;
	    }
	}
	
	private void loadPhotosForPostOnlyOne(Connection conn, Post post) throws SQLException {
        try (PreparedStatement photoStmt = conn.prepareStatement(PHOTO_SQL)) {
            photoStmt.setInt(1, post.getPostId());
            ResultSet photoRs = photoStmt.executeQuery();
            if (photoRs.next()) {
                PostPhoto photo = new PostPhoto();
                photo.setPostPhotoId(photoRs.getInt("post_photo_id"));
                photo.setPostId(photoRs.getInt("post_id"));
                // 直接讀取為 byte[]
                byte[] photoBytes = photoRs.getBytes("photo_file");
                photo.setPhotoFile(photoBytes);
                photo.setCreatedTime(photoRs.getTimestamp("created_time"));
                post.addPhoto(photo);
            }
        }
    }




}