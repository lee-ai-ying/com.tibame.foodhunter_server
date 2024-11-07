package jessey.vo;

import java.sql.Timestamp;

public class Review {

	private int reviewId;
	private int reviewer;
	private int restaurantId;
	private int rating;
	private String comment;
	private Timestamp reviewDate;
	private int thumbsUp;
	private int thumbsDown;
	private int priceRangeMax;
	private int priceRangeMin;
	private int serviceCharge;

	private String reviewerNickname;
	private String restaurantName;

	public Review() {
	};

	public Review(int reviewId, int reviewer, int restaurantId, String comment, Timestamp reviewDate, int thumbsUp,
			int thumbsDown, Integer priceRangeMax, Integer priceRangeMin, String email, Integer serviceCharge) {
		this.reviewId = reviewId;
		this.reviewer = reviewer;
		this.restaurantId = restaurantId;
		this.comment = comment;
		this.reviewDate = reviewDate;
		this.thumbsUp = thumbsUp;
		this.thumbsDown = thumbsDown;
		this.priceRangeMax = priceRangeMax;
		this.priceRangeMin = priceRangeMin;
		this.serviceCharge = serviceCharge;
	}

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public int getReviewer() {
		return reviewer;
	}

	public void setReviewer(int reviewer) {
		this.reviewer = reviewer;
	}

	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Timestamp reviewDate) {
		this.reviewDate = reviewDate;
	}

	public int getThumbsUp() {
		return thumbsUp;
	}

	public void setThumbsUp(int thumbsUp) {
		this.thumbsUp = thumbsUp;
	}

	public int getThumbsDown() {
		return thumbsDown;
	}

	public void setThumbsDown(int thumbsDown) {
		this.thumbsDown = thumbsDown;
	}

	public int getPriceRangeMax() {
		return priceRangeMax;
	}

	public void setPriceRangeMax(int priceRangeMax) {
		this.priceRangeMax = priceRangeMax;
	}

	public int getPriceRangeMin() {
		return priceRangeMin;
	}

	public void setPriceRangeMin(int priceRangeMin) {
		this.priceRangeMin = priceRangeMin;
	}

	public int getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(int serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getReviewerNickname() {
		return reviewerNickname;
	}

	public void setReviewerNickname(String reviewerNickname) {
		this.reviewerNickname = reviewerNickname;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
}