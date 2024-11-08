package ai_ying.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ai_ying.Common;
import ai_ying.service.GroupService;
import ai_ying.service.impl.GroupServiceImpl;
import ai_ying.vo.GroupChat;

@WebServlet("/group/chat/send")
public class SendMessageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GroupService service;

	@Override
	public void init() throws ServletException {
		Common.initializeFirebaseApp(getServletContext());
		try {
			service = new GroupServiceImpl();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		Gson gson = new Gson();
//		GroupChat groupChat = gson.fromJson(req.getReader(), GroupChat.class);
//		JsonObject result = new JsonObject();
//		result.addProperty("result", gson.toJson(service.sendMessage(groupChat)));
//		PrintWriter out = resp.getWriter();
//		out.println(result.toString());
//		GroupChat groupChat = gson.fromJson(req.getReader(), GroupChat.class);
//		JsonObject result = new JsonObject();
//		result.addProperty("result", gson.toJson(service.sendMessage(groupChat)));
//		PrintWriter out = resp.getWriter();
//		out.println(result.toString());
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);
		String title = jsonObject.get("title").getAsString();
		String body = jsonObject.get("body").getAsString();
		String data = jsonObject.get("data").getAsString();
		GroupChat groupChat = gson.fromJson(data, GroupChat.class);
		Notification notification = Notification.builder().setTitle(title).setBody(body).build();
		MulticastMessage message = MulticastMessage.builder().setNotification(notification).putData("data", "")
				.addAllTokens(service.getTokens(groupChat.getGroupId())).build();
		try {
			// BatchResponse batchResponse =
			FirebaseMessaging.getInstance().sendEachForMulticast(message);
			// System.out.println(batchResponse.getSuccessCount() + " messages were sent successfully");
			JsonObject result = new JsonObject();
			result.addProperty("result", gson.toJson(service.sendMessage(groupChat)));
			PrintWriter out = resp.getWriter();
			out.println(result.toString());
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
	}
}
