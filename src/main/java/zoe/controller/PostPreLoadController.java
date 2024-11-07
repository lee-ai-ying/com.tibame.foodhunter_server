package zoe.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import zoe.service.PostService;
import zoe.service.Impl.PostServiceImpl;
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
            
            // 建立自訂的 Gson，處理 byte[] 轉 Base64
            gson = new GsonBuilder()
                .registerTypeAdapter(byte[].class, new JsonSerializer<byte[]>() {
                    @Override
                    public JsonElement serialize(byte[] bytes, Type type, JsonSerializationContext context) {
                        if (bytes == null) {
                            return JsonNull.INSTANCE;
                        }
                        
                        try {
                            // 直接將 byte[] 轉換為 Base64
                            String base64Image = Base64.getEncoder().encodeToString(bytes);
                            // 加上 data URL prefix
                            return new JsonPrimitive("data:image/jpeg;base64," + base64Image);
                        } catch (Exception e) {
                            // 錯誤處理：記錄錯誤但不中斷處理
                            e.printStackTrace();
                            return JsonNull.INSTANCE;
                        }
                    }
                })
                // 如果有日期時間欄位，可以加入自訂格式
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
                
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
            System.out.println("開始執行 preLoadPostService");
            
            // 取得貼文列表（包含照片）
            List<Post> posts = postService.preLoadPostService();
            
            System.out.println("取得貼文數量: " + (posts != null ? posts.size() : 0));
            if (posts != null && !posts.isEmpty()) {
                System.out.println("第一篇貼文照片數量: " + 
                    (posts.get(0).getPhotos() != null ? posts.get(0).getPhotos().size() : 0));
            }

            // 轉換為 JSON 並回傳
            String jsonResponse = gson.toJson(posts != null ? posts : List.of());
//            System.out.println("JSON 轉換完成，準備回傳");
            
            resp.getWriter().write(jsonResponse);
//            System.out.println("回應已送出");

        } catch (Exception e) {
            // 記錄詳細錯誤訊息
            System.err.println("取得貼文列表時發生錯誤: " + e.getMessage());
            e.printStackTrace();
            getServletContext().log("取得貼文列表時發生錯誤", e);
            
            // 設置錯誤狀態和回傳錯誤訊息
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ErrorResponse errorResponse = new ErrorResponse("取得貼文列表失敗: " + e.getMessage());
            resp.getWriter().write(gson.toJson(errorResponse));
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