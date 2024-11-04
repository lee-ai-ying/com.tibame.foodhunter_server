package zoe.dao;

import java.util.List;
<<<<<<< HEAD


import zoe.vo.Post;

public interface PostDao {
	

	// TODO 以post內文搜尋
	List<Post> selectByPostcontent(Post post);

	// TODO 預先載入10篇貼文
	List<Post> preLoadPost();

}
=======
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
}
>>>>>>> Zoe
