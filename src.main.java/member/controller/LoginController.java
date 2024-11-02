package member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import member.service.MemberService;
import member.service.impl.MemberServiceImpl;
import member.vo.Member;
@WebServlet("/member/login")
public class LoginController  extends HttpServlet {
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
		protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
			Gson gson =new Gson();
			Member member = gson.fromJson(req.getReader(),Member.class);
			member =service.login(member);
			
			JsonObject respBody = new JsonObject();
			if(member != null) {
			respBody.addProperty("nickname",member.getNickname());
			if (req.getSession(false) != null) {
				req.changeSessionId(); // ←變更Session ID
				} // ↓此屬性物件即用來區分是否登入中
				req.getSession().setAttribute("member", member);
			}else {
				respBody.addProperty("errMsg","使用者名稱或密碼錯誤");
			}
			resp.getWriter().write(respBody.toString());
		}
}
