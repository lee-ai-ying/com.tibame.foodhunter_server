package zoe.controller;

import java.io.BufferedReader;
import java.io.IOException;

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

@WebServlet("/post/GetUsername")
public class GetUsername extends HttpServlet {
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
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        try {
            // 讀取請求內容
            BufferedReader reader = req.getReader();
            StringBuilder jsonBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
            
            System.out.println("收到的請求內容: " + jsonBody.toString());
            
            // 解析 JSON
            JsonObject jsonObject = gson.fromJson(jsonBody.toString(), JsonObject.class);
            int memberId = jsonObject.get("memberId").getAsInt();
            
            System.out.println("正在查詢 memberId: " + memberId);
            
            // 執行查詢
            String username = postService.getUsernameByMemberId(memberId);
            System.out.println("查詢結果 username: " + username);
            
            // 建立回應
            JsonObject response = new JsonObject();
            if (username != null) {
                response.addProperty("username", username);
                System.out.println("回傳 username: " + username);
                resp.getWriter().write(gson.toJson(response));
            } else {
                response.addProperty("error", "找不到對應的用戶名稱");
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(gson.toJson(response));
            }
            
        } catch (Exception e) {
            System.err.println("處理請求時發生錯誤: " + e.getMessage());
            e.printStackTrace();
            
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "取得用戶名稱失敗: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(errorResponse));
        }
    }
}