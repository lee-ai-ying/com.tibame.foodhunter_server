package jessey.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import jessey.dao.ReviewDao;
import jessey.dao.impl.ReviewDaoImpl;
import jessey.service.ReviewService;
import jessey.vo.Review;
import zoe.vo.Post;

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
			System.out.println("第一篇評論內容: " + reviews.get(0).getComments());
		}
		return reviews;
	}

	@Override
	public boolean createReview(Review review) {
		boolean success = false;
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public boolean updateReview(Review review) {
		boolean success = false;
		try {
			System.out.println("開始更新評論 - " + ",ID: " + review.getReviewId() + ",Reviewer: " + review.getReviewer()
					+ ",Comments: " + review.getComments());
			Review existingReview = reviewDao.getReviewById(review.getReviewId());

			if (existingReview == null) {
				System.out.println("找不到要更新的評論，ID: " + review.getReviewId());
				return false;
			}

			// 複製不應該被更新的欄位
			review.setReviewer(existingReview.getReviewer()); // 保持原發布者
			review.setRestaurantId(existingReview.getRestaurantId()); // 保持原餐廳ID
			review.setReviewDate(existingReview.getReviewDate()); // 保持原發布時間
			review.setRating(existingReview.getRating()); // 保持原星星數

			if (review.getThumbsUp() == null) {
				review.setThumbsUp(existingReview.getThumbsUp()); // 保持原按讚數
			}

			if (review.getThumbsDown() == null) {
				review.setThumbsDown(existingReview.getThumbsDown()); // 保持原倒讚數
			}
			review.setPriceRangeMax(existingReview.getPriceRangeMax()); // 保持原價格區間、服務費
			review.setPriceRangeMin(existingReview.getPriceRangeMin());
			review.setServiceCharge(existingReview.getServiceCharge());

			// 1. 更新評論主體
			Integer result = reviewDao.updateReview(review);
			if (result <= 0) {
				throw new RuntimeException("更新失敗");
			}

			System.out.println("成功更新評論基本資訊，ID: " + review.getReviewId());

			// 2. 重新獲取更新後的評論
			Review updatedReview = reviewDao.getReviewById(review.getReviewId());
			System.out.println("評論更新完成 ");

			success = true;
			System.out.println("完成評論更新流程");

		} catch (Exception e) {
			System.err.println("更新評論過程中發生錯誤: " + e.getMessage());
			e.printStackTrace();
		}

		return success;
	}// throw new UnsupportedOperationException("Not implemented yet");

	@Override
	public Review getReviewById(int reviewId) {
		try {
			// 呼叫 DAO 層方法獲取
			Review review = reviewDao.getReviewById(reviewId);

			if (review != null) {
				System.out.println("Service層成功獲取評論，ID: " + reviewId);
				System.out.println("評論內容: " + review.getComments());
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
