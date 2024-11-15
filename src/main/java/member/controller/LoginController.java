package member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import member.service.MemberService;
import member.service.impl.MemberServiceImpl;
import member.vo.Member;

@WebServlet("/member/login")
public class LoginController extends HttpServlet {
	private static final long sarialVersionUID = 1L;
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
		Member member = gson.fromJson(req.getReader(), Member.class);
		member = service.login(member);
		System.out.println(member);
		JsonObject respBody = new JsonObject();
		boolean logged = false;
		if (member != null) {
			System.out.println("member is not null");

			logged = true;
			respBody.addProperty("username", member.getUsername());
			respBody.addProperty("nickname", member.getNickname());
			respBody.addProperty("logged", logged);
			if (req.getSession(false) != null) {
				System.out.println("session is not null and change");
				req.changeSessionId(); // ←變更Session ID
			} // ↓此屬性物件即用來區分是否登入中
			req.getSession().setAttribute("member", member);
		} else {
			respBody.addProperty("errMsg", "使用者名稱或密碼錯誤");
		}
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(respBody.toString());

	}
}
