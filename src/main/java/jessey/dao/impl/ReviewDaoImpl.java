package jessey.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import jessey.dao.ReviewDao;
import jessey.vo.Review;

public class ReviewDaoImpl implements ReviewDao {
	private DataSource ds;

	// 建構子，初始化資料源
	public ReviewDaoImpl() throws NamingException {
		try {
			Context context = new InitialContext();
			ds = (DataSource) context.lookup("java:comp/env/jdbc/server/foodhunter");
			System.out.println("資料源連接成功");
		} catch (NamingException e) {
			System.err.println("資料源連接失敗: " + e.getMessage());
			throw e;
		}
	}

	// 載入特定 restaurant_id 最近的 10 條評論
	@Override
	public List<Review> preLoadReviewsByRestaurantId(int restaurantId) {
		String sql = "SELECT r.review_id, r.reviewer, r.restaurant_id, r.rating, r.comments, r.review_date, "
				+ "r.thumbs_up, r.thumbs_down, r.price_range_max, r.price_range_min, r.service_charge, "
				+ "m.nickname AS reviewer_nickname, res.restaurant_name " + "FROM review r "
				+ "LEFT JOIN member m ON r.reviewer = m.member_id "
				+ "LEFT JOIN restaurant res ON r.restaurant_id = res.restaurant_id " + "WHERE r.restaurant_id = ? "
				+ "ORDER BY r.review_date DESC LIMIT 10";

		List<Review> results = new ArrayList<>();

		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, restaurantId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Review review = new Review();
					review.setReviewId(rs.getInt("review_id"));
					review.setReviewer(rs.getInt("reviewer"));
					review.setRestaurantId(rs.getInt("restaurant_id"));
					review.setComments(rs.getString("comments"));
					review.setReviewDate(rs.getTimestamp("review_date"));
					review.setThumbsUp(rs.getInt("thumbs_up"));
					review.setThumbsDown(rs.getInt("thumbs_down"));
					review.setPriceRangeMax(rs.getInt("price_range_max"));
					review.setPriceRangeMin(rs.getInt("price_range_min"));
					review.setServiceCharge(rs.getInt("service_charge"));

					review.setReviewerNickname(rs.getString("reviewer_nickname"));
					review.setRestaurantName(rs.getString("restaurant_name"));
					results.add(review);

					System.out.println(
							"讀取到評論: ID=" + review.getReviewId() + ", Content=" + review.getComments() + ", Reviewer="
									+ review.getReviewerNickname() + ", Restaurant=" + review.getRestaurantName());
				}
			}

			return results;

		} catch (SQLException e) {
			System.err.println("SQL執行錯誤: " + e.getMessage());
			return results;
		}
	}

	// 新增評論
	@Override
	public boolean addReview(Review review) {
		String sql = "INSERT INTO review (reviewer, restaurant_id, rating, comments, review_date, thumbs_up, thumbs_down, "
				+ "price_range_max, price_range_min, service_charge) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, review.getReviewer());
			pstmt.setInt(2, review.getRestaurantId());
			pstmt.setInt(3, review.getRating());
			pstmt.setString(4, review.getComments());
			pstmt.setTimestamp(5, review.getReviewDate());
			pstmt.setInt(6, review.getThumbsUp());
			pstmt.setInt(7, review.getThumbsDown());
			pstmt.setInt(8, review.getPriceRangeMax());
			pstmt.setInt(9, review.getPriceRangeMin());
			pstmt.setInt(10, review.getServiceCharge());

			int rowsAffected = pstmt.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("新增評論時發生錯誤: " + e.getMessage());
			return false;
		}
	}

	// 修改評論，只有評論者可以修改
	@Override
	public boolean updateReview(Review review) {
		String sql = "UPDATE review SET rating = ?, comments = ?, thumbs_up = ?, thumbs_down = ?, "
				+ "price_range_max = ?, price_range_min = ?, service_charge = ? "
				+ "WHERE review_id = ? AND reviewer = ?";

		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, review.getRating());
			pstmt.setString(2, review.getComments());
			pstmt.setInt(3, review.getThumbsUp());
			pstmt.setInt(4, review.getThumbsDown());
			pstmt.setInt(5, review.getPriceRangeMax());
			pstmt.setInt(6, review.getPriceRangeMin());
			pstmt.setInt(7, review.getServiceCharge());
			pstmt.setInt(8, review.getReviewId());
			pstmt.setInt(9, review.getReviewer());

			int rowsAffected = pstmt.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("修改評論時發生錯誤: " + e.getMessage());
			return false;
		}
	}

	// 按讚或倒讚評論
	@Override
	public boolean likeOrDislikeReview(int reviewId, boolean like) {
		String sql = like ? "UPDATE review SET thumbs_up = thumbs_up + 1 WHERE review_id = ?"
				: "UPDATE review SET thumbs_down = thumbs_down + 1 WHERE review_id = ?";

		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, reviewId);
			int rowsAffected = pstmt.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("按讚/倒讚評論時發生錯誤: " + e.getMessage());
			return false;
		}
	}

	// 複製評論文字
	@Override
	public String copyReviewComment(int reviewId) {
		String sql = "SELECT comments FROM review WHERE review_id = ?";

		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, reviewId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getString("comments");
				} else {
					return null;
				}
			}

		} catch (SQLException e) {
			System.err.println("複製評論文字時發生錯誤: " + e.getMessage());
			return null;
		}
	}

	// 根據指定排序方式顯示評論
	@Override
	public List<Review> getReviewsSorted(int restaurantId, String sortBy) {
		String sql = "SELECT r.review_id, r.reviewer, r.restaurant_id, r.rating, r.comments, r.review_date, "
				+ "r.thumbs_up, r.thumbs_down, r.price_range_max, r.price_range_min, r.service_charge, "
				+ "m.nickname AS reviewer_nickname, res.restaurant_name " + "FROM review r "
				+ "LEFT JOIN member m ON r.reviewer = m.member_id "
				+ "LEFT JOIN restaurant res ON r.restaurant_id = res.restaurant_id " + "WHERE r.restaurant_id = ? "
				+ "ORDER BY "
				+ (sortBy.equals("latest") ? "r.review_date DESC"
						: sortBy.equals("thumbsUp") ? "r.thumbs_up DESC"
								: sortBy.equals("rating") ? "r.rating DESC" : "r.review_date DESC");

		List<Review> results = new ArrayList<>();

		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, restaurantId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Review review = new Review();
					review.setReviewId(rs.getInt("review_id"));
					review.setReviewer(rs.getInt("reviewer"));
					review.setRestaurantId(rs.getInt("restaurant_id"));
					review.setComments(rs.getString("comment"));
					review.setReviewDate(rs.getTimestamp("review_date"));
					review.setThumbsUp(rs.getInt("thumbs_up"));
					review.setThumbsDown(rs.getInt("thumbs_down"));
					review.setPriceRangeMax(rs.getInt("price_range_max"));
					review.setPriceRangeMin(rs.getInt("price_range_min"));
					review.setServiceCharge(rs.getInt("service_charge"));

					review.setReviewerNickname(rs.getString("reviewer_nickname"));
					review.setRestaurantName(rs.getString("restaurant_name"));
					results.add(review);
				}
			}

			return results;

		} catch (SQLException e) {
			System.err.println("SQL執行錯誤: " + e.getMessage());
			return results;
		}
	}

	@Override
	public Review getReviewById(int reviewId) {
		String sql = "SELECT r.review_id, r.reviewer, r.restaurant_id, r.rating, r.comments, r.review_date, "
				+ "r.thumbs_up, r.thumbs_down, r.price_range_max, r.price_range_min, r.service_charge, "
				+ "m.nickname AS reviewer_nickname, res.restaurant_name " + "FROM review r "
				+ "LEFT JOIN member m ON r.reviewer = m.member_id "
				+ "LEFT JOIN restaurant res ON r.restaurant_id = res.restaurant_id " + "WHERE review_id = ? ";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			System.out.println("準備查詢評論，ID: " + reviewId);
			pstmt.setInt(1, reviewId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					Review review = new Review();
					review.setReviewId(rs.getInt("review_id"));
					review.setReviewer(rs.getInt("reviewer"));
					review.setRestaurantId(rs.getInt("restaurant_id"));
					review.setComments(rs.getString("comment"));
					review.setReviewDate(rs.getTimestamp("review_date"));
					review.setThumbsUp(rs.getInt("thumbs_up"));
					review.setThumbsDown(rs.getInt("thumbs_down"));
					review.setPriceRangeMax(rs.getInt("price_range_max"));
					review.setPriceRangeMin(rs.getInt("price_range_min"));
					review.setServiceCharge(rs.getInt("service_charge"));
					review.setReviewerNickname(rs.getString("reviewer_nickname"));
					review.setRestaurantName(rs.getString("restaurant_name"));

					System.out.println("成功查詢到評論，ID: " + reviewId);
					return review;
				} else {
					System.out.println("找不到貼文，ID: " + reviewId);
					return null;
				}
			}

		} catch (SQLException e) {
			System.err.println("查詢貼文時發生錯誤，ID: " + reviewId + ", 錯誤訊息: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}