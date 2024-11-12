package member.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ai_ying.Common;
import member.service.MemberService;
import member.service.impl.MemberServiceImpl;
import member.vo.Member;
@WebServlet("/member/sendMessage")

public class SendMessageController  extends HttpServlet{
	private static final long sarialVersionUID = 1L;
	private MemberService service;
	@Override
		public void init() throws ServletException {
			Common.initializeFirebaseApp(getServletContext());
			try {
				service = new MemberServiceImpl();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	@Override
	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
//		req.setCharacterEncoding("UTF-8");
//		boolean send = false;
//		Gson gson =new Gson();
//		Member member = gson.fromJson(req.getReader(),Member.class);
//		String errMsg = service.sendMessage(member);
//		JsonObject respBody = new JsonObject();
//		respBody.addProperty("result",errMsg == null);
//		respBody.addProperty("errMsg", errMsg);
//		if(errMsg == null) {
//			send = true;
//		respBody.addProperty("send", send);
//
//		}
//		resp.setCharacterEncoding("UTF-8");
//		resp.getWriter().write(respBody.toString());
		boolean send = false;
		Gson gson = new Gson();
		Member member = gson.fromJson(req.getReader(), Member.class);
		String msg = member.getMessage();
		try {			
			String errMsg = service.sendMessage(member);
			JsonObject respBody = new JsonObject();
			respBody.addProperty("result",errMsg == null);
			respBody.addProperty("errMsg", errMsg);
			if(errMsg == null) {
				send = true;
				respBody.addProperty("send", send);
				Notification notification = Notification.builder().setTitle(service.getinfo(member).getNickname()).setBody(msg).build();
				Message message = Message.builder().setNotification(notification).putData("data", msg)
						.setToken(service.getTokenByMember(member)).build();
				FirebaseMessaging.getInstance().send(message);
			}
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(respBody.toString());
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
	}
	}


