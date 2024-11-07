package member.controller;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import member.service.MemberService;
import member.service.impl.MemberServiceImpl;
import member.vo.Member;
@WebServlet("/member/image")
public class ImageController extends HttpServlet {
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
        Gson gson = new Gson();

        // 解析前端发送的 JSON 请求
        Member member = gson.fromJson(req.getReader(), Member.class);

        // 从服务层获取图片的 Base64 字符串
        String base64Image = service.image(member); // 获取 Base64 字符串

        // 将图片 Base64 字符串设置到 Member 对象中
        // 如果您要返回 Member 对象的话，可以把 Base64 字符串设置到 member 的一个属性中
        member.setProfileImageBase64(base64Image);

        // 将带有图片的 Member 对象转换为 JSON 字符串
        String jsonStr = gson.toJson(member);

        // 设置返回的字符编码
        resp.setCharacterEncoding("UTF-8");

        // 将 JSON 字符串写入响应
        Writer writer = resp.getWriter();
        writer.write(jsonStr);
        writer.flush();
        writer.close();

        System.out.println(jsonStr);
    }
}
