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
    public List<Review> preLoadReviewsByRestaurantId(Integer restaurantId) {
        return reviewDao.preLoadReviewsByRestaurantId(restaurantId);
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
	        // 先檢查是否有reviewId
	        if (review.getReviewId() == null) {
	            System.out.println("未提供評論ID，無法更新評論");
	            return false;
	        }

	        // 獲取現有評論
	        Review existingReview = reviewDao.getReviewById(review.getReviewId());
	        
	        // 檢查評論是否存在
	        if (existingReview == null) {
	            System.out.println("找不到要更新的評論，ID: " + review.getReviewId());
	            return false;
	        }

	        // 驗證評論者是否為原作者
	        if (!existingReview.getReviewer().equals(review.getReviewer())) {
	            System.out.println("非原評論者，無權更新評論，ID: " + existingReview.getReviewId());
	            return false;
	        }

	        System.out.println("開始更新評論 - " + "ID: " + existingReview.getReviewId() 
	            + ", Reviewer: " + existingReview.getReviewer()
	            + ", 原評論內容: " + existingReview.getComments()
	            + ", 新評論內容: " + review.getComments());

	        // 建立更新用的評論物件，使用原評論的所有資料
	        Review updatedReview = new Review();
	        
	        // 從原評論複製所有欄位（保護不變動的資料）
	        updatedReview.setReviewId(existingReview.getReviewId());          // 保持原評論ID
	        updatedReview.setReviewer(existingReview.getReviewer());         // 保持原評論者
	        updatedReview.setRestaurantId(existingReview.getRestaurantId()); // 保持原餐廳ID
	        updatedReview.setReviewDate(existingReview.getReviewDate());     // 保持原評論日期
	        updatedReview.setRating(existingReview.getRating());            // 保持原評分
	        updatedReview.setThumbsUp(existingReview.getThumbsUp());        // 保持原按讚數
	        updatedReview.setThumbsDown(existingReview.getThumbsDown());    // 保持原倒讚數
	        updatedReview.setPriceRangeMax(existingReview.getPriceRangeMax()); // 保持原價格區間
	        updatedReview.setPriceRangeMin(existingReview.getPriceRangeMin());
	        updatedReview.setServiceCharge(existingReview.getServiceCharge()); // 保持原服務費
	        
	        // 只更新評論內容
	        updatedReview.setComments(review.getComments());                 // 允許更新評論內容

	        // 執行更新操作
	        Integer result = reviewDao.updateReview(updatedReview);
	        
	        if (result <= 0) {
	            throw new RuntimeException("更新失敗");
	        }
	        
	        System.out.println("成功更新評論，ID: " + existingReview.getReviewId());
	        
	        // 7. 重新獲取更新後的評論以確認
	        Review confirmUpdate = reviewDao.getReviewById(existingReview.getReviewId());
	        if (confirmUpdate != null) {
	            System.out.println("評論更新確認完成，新評論內容: " + confirmUpdate.getComments());
	            success = true;
	        }
	        
	        System.out.println("完成評論更新流程");
	        
	    } catch (Exception e) {
	        System.err.println("更新評論過程中發生錯誤: " + e.getMessage());
	        e.printStackTrace();
	    }
	    
	    return success;
	}

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
				System.out.println("評分: " + review.getRating());

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
