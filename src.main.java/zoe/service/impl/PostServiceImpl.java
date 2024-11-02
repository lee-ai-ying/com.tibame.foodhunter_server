package zoe.service.impl;

import java.util.List;
import javax.naming.NamingException;
import zoe.dao.PostDao;
import zoe.dao.impl.PostDaoImpl; // 加入 PostDaoImpl 的 import
import zoe.vo.Post; // 加入 Post 類別的 import
import zoe.service.PostService; // 加入 PostService 介面的 import

public class PostServiceImpl implements PostService {
    
    private PostDao postDao;
    
    public PostServiceImpl() throws NamingException {
              postDao = new PostDaoImpl();
    }
    
    @Override
    public List<Post> preLoadPosts() {
        return postDao.preLoadPost();
    }
}