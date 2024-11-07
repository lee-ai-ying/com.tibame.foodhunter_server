package zoe.dao;

import java.util.List;
import zoe.vo.Post;

public interface PostDao {
    /**
     * 以post內文搜尋
     */
    List<Post> selectByPostcontent(Post post);
    
    /**
     * 預先載入10篇貼文
     */
    List<Post> preLoadPost();
    
    /**
     * 根據ID刪除貼文
     */
    boolean deletePost(Long postId);
    
    /**
     * 根據ID獲取貼文
     */
    Post getPostById(Long postId);
    
    Integer insertPost(Post post);
    /**
     * 更新貼文
     * @param post 包含要更新的貼文資訊，必須包含postId
     * @return 更新成功返回1，失敗返回0
     */
    Integer updatePost(Post post);
}