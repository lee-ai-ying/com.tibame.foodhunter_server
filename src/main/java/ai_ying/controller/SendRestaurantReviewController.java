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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import ai_ying.Common;
import ai_ying.service.GroupService;
import ai_ying.service.impl.GroupServiceImpl;
import ai_ying.vo.GroupChat;
import jessey.vo.Review;

@WebServlet("/group/review/send")
public class SendRestaurantReviewController extends HttpServlet{
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
		Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd hh:mm:ss").create();
		Review review = gson.fromJson(req.getReader(), Review.class);
		JsonObject result = new JsonObject();
		result.addProperty("result", gson.toJson(service.sendRestaurantReview(review)));
		PrintWriter out = resp.getWriter();
		out.println(result.toString());
	}
}
