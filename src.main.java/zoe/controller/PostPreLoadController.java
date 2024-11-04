package zoe.controller;

<<<<<<< HEAD
import java.io.IOException;
import java.util.List;

=======
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.annotation.Resource;
>>>>>>> Zoe
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
<<<<<<< HEAD
=======
import javax.sql.DataSource;
>>>>>>> Zoe

import com.google.gson.Gson;
import zoe.service.PostService;
import zoe.service.impl.PostServiceImpl;
import zoe.vo.Post;

@WebServlet("/post/preLoad")
public class PostPreLoadController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PostService postService;
    private Gson gson;
<<<<<<< HEAD
    
=======

>>>>>>> Zoe
    @Override
    public void init() throws ServletException {
        try {
            postService = new PostServiceImpl();
<<<<<<< HEAD
        } catch (NamingException e) {
            e.printStackTrace();
        }
        gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException {
        
        // 設置回應格式
        resp.setContentType("application/json;charset=UTF-8");
        
        try {
            // 取得前10筆貼文
            List<Post> posts = postService.preLoadPosts();
            
            // 檢查是否有取得資料
            if (posts != null && !posts.isEmpty()) {
                // 將資料轉換為 JSON 並回傳
                String jsonResponse = gson.toJson(posts);
                resp.getWriter().write(jsonResponse);
            } else {
                // 若無資料，回傳空陣列
                resp.getWriter().write("[]");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            // 發生錯誤時回傳錯誤訊息
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException {
        doGet(req, resp);
    }
}
=======
            gson = new Gson();
        } catch (NamingException e) {
            throw new ServletException("初始化 PostService 失敗", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        // 設置回應格式與編碼
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        try {
            // 取得貼文列表
            List<Post> posts = postService.preLoadPostService();  // 修改為正確的方法名稱
            
            // 轉換為 JSON 並回傳
            String jsonResponse = gson.toJson(posts != null ? posts : List.of());
            resp.getWriter().write(jsonResponse);
            
        } catch (Exception e) {
            // 記錄錯誤
            getServletContext().log("取得貼文列表時發生錯誤", e);
            
            // 設置錯誤狀態和回傳錯誤訊息
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("取得貼文列表失敗")));
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

>>>>>>> Zoe
