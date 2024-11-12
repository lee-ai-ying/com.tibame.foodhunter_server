package zoe.service.Impl;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.naming.NamingException;

import andysearch.vo.Restaurant;
import zoe.dao.PostDao;
import zoe.dao.PostPhotoDao;
import zoe.dao.Impl.PostDaoImpl;
import zoe.dao.Impl.PostPhotoDaoImpl;
import zoe.service.PostService;
import zoe.vo.Post;
import zoe.vo.PostPhoto;

public class PostServiceImpl implements PostService {
    private PostDao postDao;
    private PostPhotoDao postPhotoDao;

    public PostServiceImpl() throws NamingException {
        try {
            postDao = new PostDaoImpl();
            postPhotoDao = new PostPhotoDaoImpl();
            System.out.println("PostService 初始化完成");
        } catch (NamingException e) {
            System.err.println("PostService 初始化失敗: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean createPost(Post post) {
        boolean success = false;
        PostPhotoDao photoDao = null;  // 在方法內創建實例
        
        try {
            System.out.println("開始新增貼文流程 - " + 
                             "Tag: " + post.getPostTag() + 
                             ", Publisher: " + post.getPublisher() + 
                             ", Content: " + post.getContent());
            
            // 初始化 PostPhotoDao
            photoDao = new PostPhotoDaoImpl();
            
            // 設置時間和預設值
            post.setPostTime(new Timestamp(System.currentTimeMillis()));
         
            post.setLikeCount(0);   // 預設按讚數為 0
            
            // 1. 新增貼文主體
            Integer postId = postDao.insertPost(post);
            if (postId == null) {
                throw new SQLException("新增貼文失敗");
            }
            
            post.setPostId(postId);
            System.out.println("成功新增貼文，ID: " + postId);
            
            // 2. 如果有照片，新增照片
            if (post.getPhotos() != null && !post.getPhotos().isEmpty()) {
                for (PostPhoto photo : post.getPhotos()) {
                    photo.setPostId(postId);
                    photo.setCreatedTime(new Timestamp(System.currentTimeMillis()));
                    
                    boolean photoInserted = photoDao.insertPostPhoto(photo);  // 使用本地實例
                    if (!photoInserted) {
                        throw new SQLException("新增照片失敗");
                    }
                }
                System.out.println("成功新增 " + post.getPhotos().size() + " 張照片");
            }
            
            success = true;
            System.out.println("完成貼文新增流程");
            
        } catch (SQLException | NamingException e) {
            System.err.println("新增貼文過程中發生錯誤: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
    
    
    @Override
    public List<Post> preLoadPostService() {
        List<Post> posts = postDao.preLoadPost();
        // 加入檢查
        System.out.println("Service層獲取的貼文數量: " + (posts != null ? posts.size() : "null"));
        if (posts != null && !posts.isEmpty()) {
            System.out.println("第一篇貼文內容: " + posts.get(0).getContent());
        }
        return posts;
    }

    @Override
    public boolean deletePost(Long postId) {
        try {
            // 檢查貼文是否存在
            Post existingPost = postDao.getPostById(postId);
            if (existingPost == null) {
                System.out.println("找不到要刪除的貼文，ID: " + postId);
                return false;
            }

            // 執行刪除操作
            boolean result = postDao.deletePost(postId);
            if (result) {
                System.out.println("成功刪除貼文，ID: " + postId);
            } else {
                System.out.println("刪除貼文失敗，ID: " + postId);
            }
            return result;

        } catch (Exception e) {
            System.err.println("刪除貼文時發生錯誤，ID: " + postId + ", 錯誤訊息: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Post getPostById(Long postId) {
        try {
            // 呼叫 DAO 層方法獲取貼文
            Post post = postDao.getPostById(postId);
            
            if (post != null) {
                System.out.println("Service層成功獲取貼文，ID: " + postId);
                System.out.println("貼文內容: " + post.getContent());
                System.out.println("發布者: " + post.getPublisherNickname());
                System.out.println("餐廳: " + post.getRestaurantName());
            } else {
                System.out.println("Service層找不到貼文，ID: " + postId);
            }
            return post;
            
        } catch (Exception e) {
            System.err.println("Service層獲取貼文時發生錯誤，ID: " + postId);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updatePost(Post post) {
        boolean success = false;

        try {
            System.out.println("開始更新貼文流程 - " +
                    "ID: " + post.getPostId() +
                    ", Tag: " + post.getPostTag() +
                    ", Content: " + post.getContent());

            // 檢查貼文是否存在
            Post existingPost = postDao.getPostById(Long.valueOf(post.getPostId()));
            if (existingPost == null) {
                System.out.println("找不到要更新的貼文，ID: " + post.getPostId());
                return false;
            }

            // 複製不應該被更新的欄位
            post.setPublisher(existingPost.getPublisher()); // 保持原始發布者
            post.setPostTime(existingPost.getPostTime()); // 保持原始發布時間
            if (post.getLikeCount() == null) {
                post.setLikeCount(existingPost.getLikeCount()); // 保持原始按讚數
            }
            // 保持原始照片
            post.setPhotos(existingPost.getPhotos());
            
            // 1. 更新貼文主體
            Integer result = postDao.updatePost(post);
            if (result <= 0) {
                throw new SQLException("更新貼文失敗");
            }

            System.out.println("成功更新貼文基本資訊，ID: " + post.getPostId());

            // 2. 照片保持不變，不進行更新
            System.out.println("保持原有照片不變，照片數: " + 
                (post.getPhotos() != null ? post.getPhotos().size() : 0));

            // 3. 重新獲取更新後的貼文（包含照片）
            Post updatedPost = postDao.getPostById(Long.valueOf(post.getPostId()));
            if (updatedPost != null) {
                System.out.println("貼文更新完成，當前照片數: " +
                        (updatedPost.getPhotos() != null ? updatedPost.getPhotos().size() : 0));
            }

            success = true;
            System.out.println("完成貼文更新流程");

        } catch (SQLException e) {
            System.err.println("更新貼文過程中發生SQL錯誤: " + e.getMessage());
            e.printStackTrace();
        }

        return success;
    }
    @Override
    public List<Post> getAllPosts() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        throw new UnsupportedOperationException("Not implemented yet");

    }

	@Override
	public List<Post> getPostByRestId(Restaurant restaurant) {
		Integer restId = restaurant.getRestaurantId();
		
		return postDao.selectPostByRestId(restId);
	}

	@Override
	public String getUsernameByMemberId(Integer memberId) {
	    if (memberId == null || memberId <= 0) {
	        System.out.println("無效的 memberId: " + memberId);
	        return null;
	    }
	    
	    try {
	        String username = postDao.getUsernameByMemberId(memberId);
	        System.out.println("查詢結果 - memberId: " + memberId + ", username: " + username);
	        return username;
	    } catch (Exception e) {
	        System.err.println("查詢 username 時發生錯誤: " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    }
	}

}