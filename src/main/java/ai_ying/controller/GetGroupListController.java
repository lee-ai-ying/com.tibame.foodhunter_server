package ai_ying.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ai_ying.service.GroupService;
import ai_ying.service.impl.GroupServiceImpl;
import member.vo.Member;

@WebServlet("/group/getList")
public class GetGroupListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GroupService service;

	@Override
	public void init() throws ServletException {
		try {
			service = new GroupServiceImpl();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private final static String CONTENT_TYPE = "application/json; charset=UTF-8";
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Gson gson = new Gson();
		Member member = gson.fromJson(req.getReader(), Member.class);
		JsonObject result = new JsonObject();
		result.addProperty("result", gson.toJson(service.getGroupList(member)));
		resp.setContentType(CONTENT_TYPE);
		PrintWriter out = resp.getWriter();
		out.println(result.toString());
	}

}
