package zoe.dao;

import java.util.List;


import zoe.vo.Post;

public interface PostDao {
	

	// TODO 以post內文搜尋
	List<Post> selectByPostcontent(Post post);

	// TODO 預先載入10篇貼文
	List<Post> preLoadPost();

}
