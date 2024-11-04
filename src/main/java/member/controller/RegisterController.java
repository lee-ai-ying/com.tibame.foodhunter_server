package member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import member.service.impl.MemberServiceImpl;
import member.service.MemberService;
import member.vo.Member;
@WebServlet("/member/register")
public class RegisterController  extends HttpServlet{
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
	req.setCharacterEncoding("UTF-8");
	
	Gson gson =new Gson();
	Member member = gson.fromJson(req.getReader(),Member.class);
	String errMsg = service.register(member);
	JsonObject respBody = new JsonObject();
	respBody.addProperty("result",errMsg == null);
	respBody.addProperty("errMsg", errMsg);
	resp.setCharacterEncoding("UTF-8");
	resp.getWriter().write(respBody.toString());
	
}
}
