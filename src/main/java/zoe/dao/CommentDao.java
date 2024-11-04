package zoe.dao;

import java.util.List;
import zoe.vo.Comment;

public interface CommentDao {
    // 根據貼文ID查詢留言
    List<Comment> selectByPostId(Integer postId);
    
    Comment insert(Comment comment);
}
