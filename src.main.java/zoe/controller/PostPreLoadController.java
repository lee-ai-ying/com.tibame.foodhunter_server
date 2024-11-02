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
import zoe.service.PostService;
import zoe.service.impl.PostServiceImpl;
import zoe.vo.Post;

@WebServlet("/post/preLoad")
public class PostPreLoadController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PostService postService;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        try {
            postService = new PostServiceImpl();
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