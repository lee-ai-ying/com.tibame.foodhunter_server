package jessey.controller;

import java.io.IOException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import andysearch.vo.Restaurant;
import jessey.service.ReviewService;
import jessey.service.impl.ReviewServiceImpl;
import jessey.vo.Review;

@WebServlet("/review/preLoadController")
public class RevPreLoadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ReviewService reviewService;
	private Gson gson;

	@Override
	public void init() throws ServletException {
		try {
			reviewService = new ReviewServiceImpl();
			gson = new Gson();

		} catch (NamingException e) {
			throw new ServletException("初始化 ReviewService 失敗", e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Gson gson = new Gson();

		resp.setContentType("application/json");
		req.setCharacterEncoding("UTF-8");

		try {
			// 取得評論列表
			List<Review> reviews = reviewService.preLoadReviewByRestIdService();
			// 轉換為 JSON 並回傳
			String jsonResponse = gson.toJson(reviews != null ? reviews : List.of());
			resp.getWriter().write(jsonResponse);
		} catch (Exception e) {
			// 記錄錯誤
			getServletContext().log("取得評論列表時發生錯誤", e);

			// 設置錯誤狀態和回傳錯誤訊息
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write(gson.toJson(new ErrorResponse("取得評論列表失敗")));
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	// 內部類別用於錯誤回應
	private static class ErrorResponse {
		private final String error;

		public ErrorResponse(String error) {
			this.error = error;
		}

		public String getError() {
			return error;
		}
	}
}