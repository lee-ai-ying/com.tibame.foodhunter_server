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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try {
            // 從請求體讀取 JSON
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = req.getReader().readLine()) != null) {
                sb.append(line);
            }
            
            // 解析請求中的 restaurantId
            RequestBody requestBody = gson.fromJson(sb.toString(), RequestBody.class);
            Integer restaurantId = requestBody.getRestaurantId();

            // 取得特定餐廳的評論列表
            List<Review> reviews = reviewService.preLoadReviewsByRestaurantId(restaurantId);
            
            // 轉換為 JSON 並回傳
            String jsonResponse = gson.toJson(reviews);
            resp.getWriter().write(jsonResponse);
            
        } catch (Exception e) {
            getServletContext().log("取得評論列表時發生錯誤", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("取得評論列表失敗")));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        resp.getWriter().write(gson.toJson(new ErrorResponse("請使用 POST 方法")));
    }

    // 請求體類
    private static class RequestBody {
        private Integer restaurantId;
        
        public Integer getRestaurantId() {
            return restaurantId;
        }
    }

    // 錯誤回應類
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