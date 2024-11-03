package zoe.controller;

import java.io.IOException;
import java.io.BufferedReader;
import java.sql.Timestamp;  // 改用 sql.Timestamp
import java.util.Date;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import zoe.service.impl.CommentServiceImpl;
import zoe.vo.Comment;

@WebServlet("/comment/create")
public class CreateCommentController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CommentServiceImpl commentService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        try {
            commentService = new CommentServiceImpl();
            gson = new Gson();
        } catch (NamingException e) {
            throw new ServletException("初始化 CommentService 失敗", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        try {
            BufferedReader reader = req.getReader();
            Comment comment = gson.fromJson(reader, Comment.class);

            if (comment == null) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "請求資料格式不正確");
                return;
            }

            if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "留言內容不能為空");
                return;
            }

            if (comment.getPostId() == null) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "貼文ID不能為空");
                return;
            }

            if (comment.getMemberId() == null) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "會員ID不能為空");
                return;
            }

            // 設定留言時間 - 改用 Timestamp
            comment.setMessageTime(new Timestamp(System.currentTimeMillis()));

            Comment savedComment = commentService.createComment(comment);
            String jsonResponse = gson.toJson(savedComment);
            resp.getWriter().write(jsonResponse);

        } catch (Exception e) {
            getServletContext().log("新增留言時發生錯誤", e);
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "新增留言失敗");
        }
    }

    private void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.getWriter().write(gson.toJson(new ErrorResponse(message)));
    }

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