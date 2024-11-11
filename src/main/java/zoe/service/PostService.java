package zoe.service;

import java.util.List;

import andysearch.vo.Restaurant;
import zoe.vo.Post;

/**
 * 貼文服務介面
 * 提供貼文的基本操作功能
 */
public interface PostService {
    
	
	
	    /**
	     * 預載入貼文列表
	     * @return 貼文列表
	     */
	    List<Post> preLoadPostService();
	    
	    /**
	     * 新增貼文
	     * @param post 要新增的貼文
	     * @return 新增成功返回 true，失敗返回 false
	     */
	    boolean createPost(Post post);
	    
	    /**
	     * 刪除貼文
	     * @param postId 要刪除的貼文 ID
	     * @return 刪除成功返回 true，失敗返回 false
	     */
	    boolean deletePost(Long postId);
	    
	    /**
	     * 根據 ID 獲取貼文
	     * @param postId 貼文 ID
	     * @return 貼文物件，如果不存在返回 null
	     */
	    Post getPostById(Long postId);
	    
	    /**
	     * 更新貼文
	     * @param post 要更新的貼文
	     * @return 更新成功返回 true，失敗返回 false
	     */
	    boolean updatePost(Post post);
	    
	    /**
	     * 獲取所有貼文
	     * @return 所有貼文列表
	     */
	    List<Post> getAllPosts();
	    
	    /**
	     * 根據用戶 ID 獲取該用戶的所有貼文
	     * @param userId 用戶 ID
	     * @return 該用戶的所有貼文列表
	     */
	    List<Post> getPostsByUserId(Long userId);
	    String getUsernameByMemberId(Integer memberId);
	    
	    List<Post> getPostByRestId(Restaurant restaurant);
	}

