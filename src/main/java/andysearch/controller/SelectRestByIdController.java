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

/**
 * Servlet implementation class SelectRestByIdController
 */
@WebServlet("/SelectRestByIdController")
public class SelectRestByIdController extends HttpServlet {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Gson gson = new Gson();
		request.setCharacterEncoding("UTF-8");
		Restaurant restaurant = gson.fromJson(request.getReader(), Restaurant.class);
		Restaurant resp_restaurant = service.selectRestById(restaurant);
		JsonObject respBody = new JsonObject();
		try {
		    if (resp_restaurant != null) {  
		            respBody.addProperty("restaurant_id", resp_restaurant.getRestaurantId());
		            respBody.addProperty("name", resp_restaurant.getRestaurantName());
		            respBody.addProperty("address", resp_restaurant.getAddress());
		            respBody.addProperty("total_scores", resp_restaurant.getTotalScores());
		            respBody.addProperty("total_review", resp_restaurant.getTotalReview());
		            respBody.addProperty("opening_hours", resp_restaurant.getOpeningHours());
		            respBody.addProperty("home_phone", resp_restaurant.getHomePhone());
		            respBody.addProperty("latitude", resp_restaurant.getLatitude());
		            respBody.addProperty("longitude", resp_restaurant.getLongitude());
		            respBody.addProperty("email", resp_restaurant.getEmail());
		        }
		    else {
		        JsonObject notFound = new JsonObject();
		        notFound.addProperty("NotFind", "查無此餐廳");
		        response.setContentType("application/json");
		        response.setCharacterEncoding("UTF-8");
		        response.getWriter().write(notFound.toString()); 
		        
		    }
		} catch (Exception e) {
//	        JsonObject notFound = new JsonObject();
//	        notFound.addProperty("NotFind", "查無此餐廳");
//	        response.setContentType("application/json");
//	        response.setCharacterEncoding("UTF-8");
//	        response.getWriter().write(notFound.toString()); 
		    // 當捕捉到例外時，restaurantArray 將保持為空的 JsonArray
		    e.printStackTrace(); // 可選擇保留或移除這行，用於記錄錯誤
		}
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(respBody.toString()); 
   
	}

}
