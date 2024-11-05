package ai_ying.controller;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import ai_ying.service.GroupService;
import ai_ying.service.impl.GroupServiceImpl;
import ai_ying.vo.Group;

@WebServlet("/group/create")
public class CreateGroupController extends HttpServlet {
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
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		Group group = gson.fromJson(req.getReader(), Group.class);
		
		String errMsg = service.createGroup(group);
		JsonObject respBody = new JsonObject();
		respBody.addProperty("result", errMsg == null);
		respBody.addProperty("errMsg", errMsg);
		
		resp.getWriter().write(respBody.toString());
	}

}
