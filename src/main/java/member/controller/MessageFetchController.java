package member.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import member.service.MemberService;
import member.service.impl.MemberServiceImpl;
import member.vo.Member;
import member.vo.Message;

@WebServlet("/member/messageFetch")
public class MessageFetchController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private MemberService service;

    @Override
    public void init() throws ServletException {
        try {
            // 初始化 MemberService
            service = new MemberServiceImpl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 設置字符編碼
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        // 從請求中獲取用戶名
        Member member = gson.fromJson(req.getReader(), Member.class);

        

        // 从服务层获取两个用户之间的消息记录
        List<Member> messageList = service.getMessage(member);
        System.out.println("Messages: " + messageList);


        // 如果沒有好友，返回空列表
        if (messageList == null || messageList.isEmpty()) {
            resp.getWriter().write(gson.toJson(new ArrayList<Message>()));
            System.out.println("沒有好友");
            return;
        }

        // 用來存放好友信息的列表
        List<Message> MessageList = new ArrayList<>();

        // 遍歷好友列表，獲取每個好友的資料
        for (Member message : messageList) {
        
            
            // 創建 FriendInfo 並填充數據
        	Message Message = new Message(message.getMessage_id(),message.getReceiver_id(),message.getMessage(),message.getMessage_time());
            System.out.println("抓到好友");
            // 添加到列表中
            MessageList.add(Message);
        }

        // 將好友信息轉換為 JSON 字符串
        String jsonStr = gson.toJson(MessageList);

        // 將 JSON 字符串寫入響應
        resp.getWriter().write(jsonStr);
    }
}


