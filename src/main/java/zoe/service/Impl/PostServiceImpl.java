package zoe.service.Impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import zoe.dao.PostDao;
import zoe.dao.Impl.PostDaoImpl;
import zoe.vo.Post;
import zoe.service.PostService;

public class PostServiceImpl implements PostService {
    private PostDao postDao;

    public PostServiceImpl() throws NamingException {
        postDao = new PostDaoImpl();
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

    // 為了符合 PostService 介面，需要實現其他方法，但目前先返回預設值
    @Override
    public boolean createPost(Post post) {
        throw new UnsupportedOperationException("Not implemented yet");
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
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Post> getAllPosts() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        throw new UnsupportedOperationException("Not implemented yet");

    }
}