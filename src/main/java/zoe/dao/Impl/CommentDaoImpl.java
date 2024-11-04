package zoe.dao.Impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import zoe.dao.CommentDao;
import zoe.vo.Comment;

public class CommentDaoImpl implements CommentDao {
    private DataSource ds;
    
    public CommentDaoImpl() throws NamingException {
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
    public List<Comment> selectByPostId(Integer postId) {
        String sql = "SELECT m.*, mb.nickname as member_nickname " +
                    "FROM message m " +
                    "LEFT JOIN member mb ON m.member = mb.member_id " +
                    "WHERE m.post_id = ? " +
                    "ORDER BY m.message_time DESC";

        List<Comment> results = new ArrayList<>();
        
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            System.out.println("執行SQL查詢: " + sql + " [postId=" + postId + "]");
            
            pstmt.setInt(1, postId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setMessageId(rs.getInt("message_id"));
                    comment.setPostId(rs.getInt("post_id"));
                    comment.setMemberId(rs.getInt("member"));
                    comment.setContent(rs.getString("content"));
                    comment.setMessageTime(rs.getTimestamp("message_time"));
                    comment.setMemberNickname(rs.getString("member_nickname"));
                    
                    results.add(comment);
                    System.out.println("讀取到留言: ID=" + comment.getMessageId() +
                                     ", Content=" + comment.getContent() +
                                     ", Member=" + comment.getMemberNickname());
                }
            }
            
            System.out.println("總共讀取到 " + results.size() + " 筆留言");
            return results;
            
        } catch (SQLException e) {
            System.err.println("SQL執行錯誤: " + e.getMessage());
            e.printStackTrace();
            return results;
        }
    }
    
    @Override
    public Comment insert(Comment comment) {
        String sql = "INSERT INTO message (post_id, member, content, message_time) " +
                    "VALUES (?, ?, ?, ?)";
                    
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            System.out.println("執行SQL新增: " + sql);
            
            pstmt.setInt(1, comment.getPostId());
            pstmt.setInt(2, comment.getMemberId());
            pstmt.setString(3, comment.getContent());
            pstmt.setTimestamp(4, new Timestamp(comment.getMessageTime().getTime()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("新增留言失敗，沒有新增任何資料");
            }
            
            // 獲取自動生成的主鍵
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comment.setMessageId(generatedKeys.getInt(1));
                    System.out.println("新增留言成功: ID=" + comment.getMessageId());
                } else {
                    throw new SQLException("新增留言失敗，無法獲取ID");
                }
            }
            
            return comment;
            
        } catch (SQLException e) {
            System.err.println("SQL執行錯誤: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("新增留言失敗", e);
        }
    }
}