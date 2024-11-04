package zoe.controller;

import java.io.IOException;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import zoe.service.Impl.CommentServiceImpl;
import zoe.vo.Comment;

@WebServlet("/comment/byPost")
public class CommentByPostController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CommentServiceImpl commentService;  // 改用實作類型
    private Gson gson;

    @Override
    public void init() throws ServletException {
        try {
            commentService = new CommentServiceImpl();  // 直接使用實作類
            gson = new Gson();
        } catch (NamingException e) {
            throw new ServletException("初始化 CommentService 失敗", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 設置回應格式與編碼
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // 從請求參數中獲取貼文ID
            String postIdStr = req.getParameter("postId");
            if (postIdStr == null || postIdStr.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ErrorResponse("缺少貼文ID參數")));
                return;
            }

            // 轉換貼文ID為整數
            Integer postId;
            try {
                postId = Integer.parseInt(postIdStr);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ErrorResponse("無效的貼文ID格式")));
                return;
            }

            // 取得留言列表
            List<Comment> comments = commentService.getCommentsByPostId(postId);

            // 轉換為 JSON 並回傳
            String jsonResponse = gson.toJson(comments != null ? comments : List.of());
            resp.getWriter().write(jsonResponse);

        } catch (Exception e) {
            // 記錄錯誤
            getServletContext().log("取得留言列表時發生錯誤", e);

            // 設置錯誤狀態和回傳錯誤訊息
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("取得留言列表失敗")));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
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