package jessey.service.impl;

import java.util.List;

import javax.naming.NamingException;

import jessey.dao.ReviewDao;
import jessey.dao.impl.ReviewDaoImpl;
import jessey.service.ReviewService;
import jessey.vo.Review;

public class ReviewServiceImpl implements ReviewService {
	private ReviewDao reviewDao;

	public ReviewServiceImpl() throws NamingException {
		reviewDao = new ReviewDaoImpl();
	}

	@Override
	public List<Review> preLoadReviewByRestIdService() {	
		List<Review> reviews = reviewDao.preLoadReviewsByRestaurantId(1);
		System.out.println("Service層獲取的評論數: " + (reviews != null ? reviews.size() : "null"));
		if (reviews != null && !reviews.isEmpty()) {
			System.out.println("第一篇評論內容: " + reviews.get(0).getComment());
		}
		return reviews;
	}

	@Override
	public boolean createReview(Review review) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public boolean updateReview(Review review) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Review getReviewById(int reviewId) {
		try {
			// 呼叫 DAO 層方法獲取
			Review review = reviewDao.getReviewById(reviewId);

			if (review != null) {
				System.out.println("Service層成功獲取評論，ID: " + reviewId);
				System.out.println("評論內容: " + review.getComment());
				System.out.println("評論者: " + review.getReviewerNickname());
				System.out.println("餐廳: " + review.getRestaurantName());

			} else {
				System.out.println("Service層找不到評論，ID: " + reviewId);
			}
			return review;
		} catch (Exception e) {
			System.err.println("Service層獲取評論時發生錯誤，ID: " + reviewId);
			e.printStackTrace();
			return null;
		}
	}

}
