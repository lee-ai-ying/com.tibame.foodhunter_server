package jessey.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jessey.service.ReviewService;
import jessey.service.impl.ReviewServiceImpl;
import jessey.vo.Review;

@WebServlet("/review/update")
public class RevUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ReviewService reviewService;
	private Gson gson;

	@Override
	public void init() throws ServletException {
		try {
			reviewService = new ReviewServiceImpl();
			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		} catch (NamingException e) {
			throw new ServletException("初始化 ReviewService 失敗", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		
		// 設置請求和回應的編碼
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json; charset=UTF-8");

		try {
			System.out.println("開始處理更新評論請求");
			
			// 檢查從 JSON 接收到的資料
			BufferedReader reader = req.getReader();
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			System.out.println("接收到的原始 JSON: " + sb.toString());

			// 從 JSON 讀取貼文資料
			// 將 JSON 字串轉換為 Review 對象
			Review review = gson.fromJson(sb.toString(), Review.class);

			// 驗證必要欄位
			if (review.getReviewId() == null || review.getReviewId() <= 0) {
				throw new ServletException("無效的評論ID");
			}

			System.out.println("接收到的評論資料: " + "\nID: " + review.getReviewId() + "\nReviewer: " + review.getReviewer()
					+ "\nComments: " + review.getComments());
			// 執行更新操作
			boolean success = reviewService.updateReview(review);

			if (success) {
				SuccessResponse response = new SuccessResponse("評論更新成功", review.getReviewId());
				String jsonResponse = gson.toJson(response);
				System.out.println("更新成功，回應: " + jsonResponse);
				resp.getWriter().write(jsonResponse);
			} else {
				throw new ServletException("評論更新失敗");
			}

		} catch (Exception e) {
			System.err.println("更新評論時發生錯誤: " + e.getMessage());
			e.printStackTrace();

			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			ErrorResponse errorResponse = new ErrorResponse("更新評論失敗: " + e.getMessage());
			resp.getWriter().write(gson.toJson(errorResponse));
		}

	}

	private static class SuccessResponse {
		private final String message;
		private final Integer reviewId;

		public SuccessResponse(String message, Integer reviewId) {

			this.message = message;
			this.reviewId = reviewId;
			}

		}

		private static class ErrorResponse {
			private final String error;

			public ErrorResponse(String error) {
				this.error = error;
			}
		}
}
