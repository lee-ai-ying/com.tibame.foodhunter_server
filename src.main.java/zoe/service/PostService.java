package zoe.service;

import java.util.List;
import zoe.vo.Post;

public interface PostService {
    List<Post> preLoadPosts();
}