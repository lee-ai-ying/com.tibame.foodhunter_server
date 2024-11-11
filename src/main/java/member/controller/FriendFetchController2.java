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
import member.vo.FriendInfo;
import member.vo.Member;

@WebServlet("/member/friendFetch2")
public class FriendFetchController2 extends HttpServlet {
	
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
        String username = member.getUsername().trim();

        // 創建 Member 對象並設置用戶名
        member.setUsername(username);

        // 從服務層獲取該用戶的好友列表
        List<Member> friendsList = service.getFriends2(member);
        System.out.println(friendsList);
        // 如果沒有好友，返回空列表
        if (friendsList == null || friendsList.isEmpty()) {
            resp.getWriter().write(gson.toJson(new ArrayList<FriendInfo>()));
            System.out.println("沒有好友");
            return;
        }

        // 用來存放好友信息的列表
        List<FriendInfo> friendInfoList = new ArrayList<>();

        // 遍歷好友列表，獲取每個好友的資料
        for (Member friend : friendsList) {
        	// 假設 service.image 返回 Base64 編碼的圖片字符串
            String base64Image = service.image(friend);  // 獲取好友的圖片
            if (base64Image != null && !base64Image.isEmpty()) {
                System.out.println("圖片已成功加載: " + base64Image);
            } else {
                System.out.println("圖片加載失敗或沒有圖片");
            }
            // 創建 FriendInfo 並填充數據
            FriendInfo friendInfo = new FriendInfo(friend.getId(),friend.getUsername(),friend.getNickname(),base64Image);
            System.out.println("抓到好友");
            // 添加到列表中
            friendInfoList.add(friendInfo);
        }

        // 將好友信息轉換為 JSON 字符串
        String jsonStr = gson.toJson(friendInfoList);

        // 將 JSON 字符串寫入響應
        resp.getWriter().write(jsonStr);
    }
}