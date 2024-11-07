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


@WebServlet("/SelectRestController")
public class SelectRestController extends HttpServlet {
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
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Gson gson = new Gson();
		req.setCharacterEncoding("UTF-8");
		Restaurant restaurant = gson.fromJson(req.getReader(), Restaurant.class);
		
		List<Restaurant> restaurants = service.selectRest(restaurant.getSearchText());
		
		JsonArray restaurantArray = new JsonArray();
		if (restaurants == null || restaurants.isEmpty()) {
            JsonObject notFound = new JsonObject();
            notFound.addProperty("NotFind", "查無此餐廳");
            restaurantArray.add(notFound); //

		} else {
			for(Restaurant rest : restaurants) {
				JsonObject respBody = new JsonObject();
	            respBody.addProperty("restaurant_id", rest.getRestaurantId());
	            respBody.addProperty("name", rest.getRestaurantName());
	            respBody.addProperty("address", rest.getAddress());
	            respBody.addProperty("opening_hours", rest.getOpeningHours());
	            respBody.addProperty("home_phone", rest.getHomePhone());
	            respBody.addProperty("latitude", rest.getLatitude());
	            respBody.addProperty("longitude", rest.getLongitude());
				restaurantArray.add(respBody);
			}
		}
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(restaurantArray.toString()); 
        System.out.println(restaurantArray);
	}
	
}
