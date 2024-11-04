package zoe.service;

import java.util.List;
import zoe.vo.Comment;

public interface CommentService {
    // 只保留根據貼文ID查詢留言的方法
    List<Comment> getCommentsByPostId(Integer postId);
    Comment createComment(Comment comment);
}