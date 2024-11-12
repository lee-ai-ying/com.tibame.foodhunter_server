package jessey.vo;

import java.sql.Timestamp;

public class Review {

	private Integer reviewId;
	private Integer reviewer;
	private Integer restaurantId;
	private Integer rating;
	private String comments;
	private Timestamp reviewDate;
	private Integer thumbsUp;
	private Integer thumbsDown;
	private Integer priceRangeMax;
	private Integer priceRangeMin;
	private Integer serviceCharge;

	private String reviewerNickname;
	private String restaurantName;

	public Review() {
	};

	public Review(Integer reviewId, Integer reviewer, Integer restaurantId, Integer rating , String comments, Timestamp reviewDate, Integer thumbsUp,
			Integer thumbsDown, Integer priceRangeMax, Integer priceRangeMin, Integer serviceCharge) {
		this.reviewId = reviewId;
		this.reviewer = reviewer;
		this.restaurantId = restaurantId;
		this.rating = rating;
		this.comments = comments;
		this.reviewDate = reviewDate;
		this.thumbsUp = thumbsUp;
		this.thumbsDown = thumbsDown;
		this.priceRangeMax = priceRangeMax;
		this.priceRangeMin = priceRangeMin;
		this.serviceCharge = serviceCharge;
	}

	public Integer getReviewId() {
		return reviewId;
	}

	public void setReviewId(Integer reviewId) {
		this.reviewId = reviewId;
	}

	public Integer getReviewer() {
		return reviewer;
	}

	public void setReviewer(Integer reviewer) {
		this.reviewer = reviewer;
	}

	public Integer getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Timestamp getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Timestamp reviewDate) {
		this.reviewDate = reviewDate;
	}

	public Integer getThumbsUp() {
		return thumbsUp;
	}

	public void setThumbsUp(Integer thumbsUp) {
		this.thumbsUp = thumbsUp;
	}

	public Integer getThumbsDown() {
		return thumbsDown;
	}

	public void setThumbsDown(Integer thumbsDown) {
		this.thumbsDown = thumbsDown;
	}

	public Integer getPriceRangeMax() {
		return priceRangeMax;
	}

	public void setPriceRangeMax(Integer priceRangeMax) {
		this.priceRangeMax = priceRangeMax;
	}

	public Integer getPriceRangeMin() {
		return priceRangeMin;
	}

	public void setPriceRangeMin(Integer priceRangeMin) {
		this.priceRangeMin = priceRangeMin;
	}

	public Integer getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(Integer serviceCharge) {
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