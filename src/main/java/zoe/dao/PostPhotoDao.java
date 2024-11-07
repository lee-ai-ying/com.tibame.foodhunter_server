// PostPhotoDao.java
package zoe.dao;

import java.util.List;
import zoe.vo.PostPhoto;

public interface PostPhotoDao {
    /**
     * 新增貼文照片
     * @param photo 照片物件
     * @return 是否新增成功
     */
    boolean insertPostPhoto(PostPhoto photo);
    
    /**
     * 刪除指定貼文的所有照片
     * @param postId 貼文ID
     * @return 是否刪除成功
     */
    boolean deletePhotosByPostId(Long postId);
    
    /**
     * 取得指定貼文的所有照片
     * @param postId 貼文ID
     * @return 照片列表
     */
    List<PostPhoto> getPhotosByPostId(Integer postId);
}