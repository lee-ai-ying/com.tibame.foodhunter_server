package zoe.controller;

import java.io.IOException;
import java.io.BufferedReader;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import zoe.service.PostService;
import zoe.service.Impl.PostServiceImpl;
import zoe.vo.Post;

@WebServlet("/post/get")
public class PostGetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PostService postService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        try {
            postService = new PostServiceImpl();
            gson = new Gson();
        } catch (NamingException e) {
            throw new ServletException("初始化 PostService 失敗", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 設置回應格式與編碼
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // 讀取 JSON 請求內容
            StringBuilder jsonBuilder = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            // 解析 JSON
            JsonObject jsonObject = gson.fromJson(jsonBuilder.toString(), JsonObject.class);
            
            // 檢查是否有 postId
            if (!jsonObject.has("postId")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "貼文ID不能為空", null)));
                return;
            }

            try {
                // 解析 postId
                Long postId = jsonObject.get("postId").getAsLong();
                Post post = postService.getPostById(postId);

                if (post != null) {
                    // 回傳成功取得的貼文
                    resp.getWriter().write(gson.toJson(new ApiResponse(true, "成功取得貼文", post)));
                } else {
                    // 找不到貼文
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write(gson.toJson(new ApiResponse(false, "找不到指定的貼文", null)));
                }

            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ApiResponse(false, "無效的貼文ID格式", null)));
            }

        } catch (Exception e) {
            // 記錄錯誤
            getServletContext().log("取得貼文時發生錯誤", e);

            // 設置錯誤狀態和回傳錯誤訊息
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ApiResponse(false, "取得貼文時發生系統錯誤: " + e.getMessage(), null)));
        }
    }

    // API 回應的通用格式
    private static class ApiResponse {
        private final boolean success;
        private final String message;
        private final Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }
}