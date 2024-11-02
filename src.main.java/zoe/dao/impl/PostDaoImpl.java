package zoe.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import zoe.dao.PostDao;
import zoe.vo.Post;

public  class PostDaoImpl implements PostDao {

	private DataSource ds;
	public PostDaoImpl() throws NamingException {
		ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/server/post");
	}
	@Override
	public List<Post> selectByPostcontent(Post post) {
	    String sql = "SELECT post_id, post_tag, publisher, content, message_time, post_time, " +
	                 "visibility, restaurant_id, like_count " +
	                 "FROM post " +
	                 "WHERE content LIKE ? OR post_tag LIKE ?";
	    
	    try (Connection conn = ds.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        List<Post> results = new ArrayList<>();
	        pstmt.setString(1, "%" + post.getContent() + "%");
	        pstmt.setString(2, "%" + post.getPostTag() + "%");
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Post npost = new Post();
	                
	                // 設置所有基本欄位
	                npost.setPostId(rs.getInt("post_id"));
	                npost.setPostTag(rs.getString("post_tag"));
	                npost.setPublisher(rs.getInt("publisher"));
	                npost.setContent(rs.getString("content"));
	                npost.setMessageTime(rs.getTimestamp("message_time"));
	                npost.setPostTime(rs.getTimestamp("post_time"));
	                npost.setVisibility(rs.getInt("visibility"));
	                npost.setRestaurantId(rs.getInt("restaurant_id"));
	                npost.setLikeCount(rs.getInt("like_count"));
	                
	                              
	                // 將新建立的 post 物件加入結果列表
	                results.add(npost);  
	            }
	            return results;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	@Override
	
	public List<Post> preLoadPost(Post post) {
	   String sql = "select * from post limit 10";
	   
	   try (Connection conn = ds.getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(sql)) {
	       
	       List<Post> results = new ArrayList<>();
	       
	       try (ResultSet rs = pstmt.executeQuery()) {
	           while (rs.next()) {
	               Post npost = new Post();
	               
	               // 設置所有欄位
	               npost.setPostId(rs.getInt("post_id"));
	               npost.setPostTag(rs.getString("post_tag"));
	               npost.setPublisher(rs.getInt("publisher")); 
	               npost.setContent(rs.getString("content"));
	               npost.setMessageTime(rs.getTimestamp("message_time"));
	               npost.setPostTime(rs.getTimestamp("post_time"));
	               npost.setVisibility(rs.getInt("visibility"));
	               npost.setRestaurantId(rs.getInt("restaurant_id"));
	               npost.setLikeCount(rs.getInt("like_count"));
	               
	               // 將新建立的 post 物件加入結果列表
	               results.add(npost);
	           }
	           return results;
	       }
	   } catch (SQLException e) {
	       e.printStackTrace();
	   }
	   return null;
	}
}
