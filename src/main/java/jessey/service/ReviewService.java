package jessey.service;

import java.util.List;

import jessey.vo.Review;
import zoe.vo.Post;

public interface ReviewService {

//	    根據餐廳ID預載評論列表

	List<Review> preLoadReviewsByRestaurantId(Integer restaurantId);

//     * 新增評論
//     * @param review 要新增的評論
//     * @return 新增成功返回 true，失敗返回 false    
	boolean createReview(Review review);

//     * 根據 Id 獲取評論
//     * @param reviewId 評論 ID
//     * @return 評論物件，如果不存在返回 null


//     * 更新評論
//     * @param post 要更新的貼文
//     * @return 更新成功返回 true，失敗返回 false
	boolean updateReview(Review review);


//     * 根據 評論ID 獲取評論
//     * @param reviewId 評論ID
//     * @return評論物件，如果不存在返回 null
	Review getReviewById(int reviewId);


	
	
	// 根據內容搜尋評論
//	 List<Review> searchReviewsByComment(String comment);
}
