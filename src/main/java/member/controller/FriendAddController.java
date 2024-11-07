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
@WebServlet("/member/friendAdd")
public class FriendAddController extends HttpServlet{
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
		boolean Add = false;
		Gson gson =new Gson();
		Member member = gson.fromJson(req.getReader(),Member.class);
		String errMsg = service.friendadd(member);
		JsonObject respBody = new JsonObject();
		respBody.addProperty("result",errMsg == null);
		respBody.addProperty("errMsg", errMsg);
		if(errMsg == null) {
			Add = true;
		respBody.addProperty("Add", Add);

		}
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(respBody.toString());
		
	}
	}


