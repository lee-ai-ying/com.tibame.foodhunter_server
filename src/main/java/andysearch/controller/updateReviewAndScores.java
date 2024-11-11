package andysearch.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import andysearch.service.RestaurantService;
import andysearch.service.impl.RestaurantServiceImpl;
import andysearch.vo.Restaurant;

@WebServlet("/updateReviewAndScores")
public class updateReviewAndScores extends HttpServlet {
       
	private static final long serialVersionUID = 1L;
	private RestaurantService service;
       
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		try {
			service = new RestaurantServiceImpl();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
  
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Gson gson = new Gson();
		req.setCharacterEncoding("UTF-8");
		Restaurant restaurant = gson.fromJson(req.getReader(), Restaurant.class);
		Integer result = service.updateRestReviewAndScores(restaurant);
		JsonObject respBody = new JsonObject();
		respBody.addProperty("result", result);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(respBody.toString()); 
        System.out.println(respBody);
	
	}

}
