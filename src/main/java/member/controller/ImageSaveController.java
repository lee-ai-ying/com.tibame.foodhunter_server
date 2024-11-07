package member.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import member.service.MemberService;
import member.service.impl.MemberServiceImpl;
import member.vo.Member;

@WebServlet("/member/imageSave")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
                 maxFileSize = 5 * 1024 * 1024, // 5 MB
                 maxRequestSize = 10 * 1024 * 1024 // 10 MB
)
public class ImageSaveController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MemberService service;

    @Override
    public void init() throws ServletException {
        try {
            service = new MemberServiceImpl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        // 打印请求的基本信息
        System.out.println("Received image save request");

        // 读取请求体中的 JSON 数据
        Gson gson = new Gson();
        JsonObject jsonRequest = gson.fromJson(req.getReader(), JsonObject.class);

        // 获取 Base64 编码的图片
        String base64Image = jsonRequest.get("profileimage").getAsString();
        String username = jsonRequest.get("username").getAsString();

        System.out.println("Base64 image: " + base64Image);

        if (base64Image != null && !base64Image.isEmpty()) {
            // 去掉 Base64 编码的前缀（如果有）
            if (base64Image.startsWith("data:image/")) {
                base64Image = base64Image.split(",")[1]; // 去除前缀部分
            }

            // 将 Base64 字符串转回 InputStream
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            System.out.println("Decoded image size: " + imageBytes.length);
            InputStream imageInputStream = new ByteArrayInputStream(imageBytes);

            // 创建 Member 对象并设置图片流
            Member member = new Member();
            member.setUsername(username);
            member.setProfileImageInputStream(imageInputStream);

            // 保存图片
            String errMsg = service.saveimage(member);  // 保存图片

            // 发送响应
            JsonObject respBody = new JsonObject();
            respBody.addProperty("result", errMsg == null);
            boolean imageSave = errMsg == null;
            respBody.addProperty("imageSave", imageSave);
            respBody.addProperty("errMsg", errMsg);

            // 打印响应内容，确保发送的是正确的 JSON
            System.out.println("Response: " + respBody.toString());

            // 设置响应的内容类型和编码
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");

            // 写入响应
            resp.getWriter().write(respBody.toString());
        } else {
            // 如果没有提供图片
            JsonObject respBody = new JsonObject();
            respBody.addProperty("result", false);
            respBody.addProperty("imageSave", false);
            respBody.addProperty("errMsg", "No image provided");

            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.getWriter().write(respBody.toString());
        }
    }
}
