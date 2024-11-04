package member.controller;

import java.io.IOException;
import java.io.Writer;

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

@WebServlet("/member/getInfo")
public class GetInfoController extends HttpServlet {
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
        Member member = gson.fromJson(req.getReader(), Member.class);
		member = service.getinfo(member);         
		String jsonStr = gson.toJson(member);
        resp.setCharacterEncoding("UTF-8");
        Writer writer = resp.getWriter();
        writer.write(jsonStr);
        System.out.println(jsonStr);
    }
}