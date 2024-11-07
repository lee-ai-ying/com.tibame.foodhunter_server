package jessey.dao;

import java.util.List;

import jessey.vo.Review;

public interface ReviewDao {
	
	
    // 載入特定 restaurant_id 最近的 10 條評論
    List<Review> preLoadReviewsByRestaurantId(int restaurantId);

    // 新增評論
    boolean addReview(Review review);

    // 修改評論（只有評論者本人能修改）
    boolean updateReview(Review review);

    // 按讚或倒讚評論
    boolean likeOrDislikeReview(int reviewId, boolean like);

    // 複製評論文字
    String copyReviewComment(int reviewId);

    // 根據指定的排序方式顯示評論，支持 "最新發布"、"最多讚"、"最高分"
    List<Review> getReviewsSorted(int restaurantId, String sortBy);
    
    Review getReviewById(int reviewId);
}