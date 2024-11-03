package zoe.service.impl;

import java.sql.Timestamp;
import java.util.List;
import javax.naming.NamingException;
import zoe.service.CommentService;

import zoe.dao.CommentDao;
import zoe.dao.Impl.CommentDaoImpl;
import zoe.vo.Comment;

public class CommentServiceImpl implements CommentService {
    private CommentDao commentDao;

    public CommentServiceImpl() throws NamingException {
        commentDao = new CommentDaoImpl();
    }

    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        List<Comment> comments = commentDao.selectByPostId(postId);
        // 加入檢查
        System.out.println("Service層獲取的留言數量: " + (comments != null ? comments.size() : "null"));
        if (comments != null && !comments.isEmpty()) {
            System.out.println("第一則留言內容: " + comments.get(0).getContent());
        }
        return comments;
    }
    
    

    @Override
    public Comment createComment(Comment comment) {
        // 檢查 comment 物件是否為空
        if (comment == null) {
            throw new IllegalArgumentException("Comment 物件不可為空");
        }
        
        // 檢查必要欄位
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("留言內容不可為空");
        }
        
        if (comment.getPostId() == null) {
            throw new IllegalArgumentException("文章ID不可為空");
        }
        
        comment.setMessageTime(new Timestamp(System.currentTimeMillis()));
        
        // 儲存留言
        return commentDao.insert(comment);
    }
}