package zoe.controller;

import java.io.IOException;
import java.util.ArrayList;
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

import zoe.service.PostService;
import zoe.service.Impl.PostServiceImpl;
import zoe.vo.Post;
import zoe.vo.PostPhoto;

@WebServlet("/post/create")

public class PostCreateController extends HttpServlet {
    private PostService postService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        try {
            postService = new PostServiceImpl();
            gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        } catch (NamingException e) {
            throw new ServletException("初始化 PostService 失敗", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
            
        // 設置請求和回應的編碼
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");  // 加入 charset=UTF-8
        
        try {
            System.out.println("開始處理新增貼文請求");
            
            // 從 JSON 讀取貼文資料
            Post post = gson.fromJson(req.getReader(), Post.class);
            
            // 處理 Base64 照片資料
            if (post.getPhotos() != null && !post.getPhotos().isEmpty()) {
                List<PostPhoto> processedPhotos = new ArrayList<>();
                
                for (PostPhoto photo : post.getPhotos()) {
                    String base64Data = photo.getImgBase64Str();
                    if (base64Data != null && !base64Data.isEmpty()) {
                        System.out.println("處理 Base64 編碼的照片");
                        
                        if (base64Data.contains(",")) {
                            base64Data = base64Data.split(",")[1];
                        }
                        
                        try {
                            byte[] photoData = Base64.getDecoder().decode(base64Data);
                            PostPhoto processedPhoto = new PostPhoto();
                            processedPhoto.setPhotoFile(photoData);
                            processedPhotos.add(processedPhoto);
                            
                            System.out.println("成功處理照片，大小: " + photoData.length + " bytes");
                        } catch (IllegalArgumentException e) {
                            System.err.println("Base64 解碼失敗: " + e.getMessage());
                            throw new ServletException("照片格式無效");
                        }
                    }
                }
                
                post.setPhotos(processedPhotos);
            }
            
            // 執行新增操作
            boolean success = postService.createPost(post);
            
            if (success) {
                SuccessResponse response = new SuccessResponse("貼文新增成功", post.getPostId());
                resp.getWriter().write(gson.toJson(response));
            } else {
                throw new ServletException("貼文新增失敗");
            }
            
        } catch (Exception e) {
            System.err.println("新增貼文時發生錯誤: " + e.getMessage());
            e.printStackTrace();
            
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ErrorResponse errorResponse = new ErrorResponse("新增貼文失敗: " + e.getMessage());
            resp.getWriter().write(gson.toJson(errorResponse));
        }
    
    }
    
    private static class SuccessResponse {
        private final String message;
        private final Integer postId;

        public SuccessResponse(String message, Integer postId) {
            this.message = message;
            this.postId = postId;
        }
    }
    
    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}