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


@WebServlet("/PreLoadRest")
public class PreLoadRest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private RestaurantService service;


	@Override
		public void init() throws ServletException {
			try {
				service = new RestaurantServiceImpl();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	
		List<Restaurant> restaurants = service.preLoadRestService();
		
		JsonArray restaurantArray = new JsonArray();
		if (restaurants != null) {
			for(Restaurant rest : restaurants) {
				JsonObject respBody = new JsonObject();
				respBody.addProperty("restaurant_id", rest.getRestaurantId());
				respBody.addProperty("name", rest.getRestaurantName());
				respBody.addProperty("address", rest.getAddress());
				respBody.addProperty("latitude", rest.getLatitude());
				respBody.addProperty("longitude", rest.getLongitude());
				restaurantArray.add(respBody);
			}
	
		} else {
            JsonObject notFound = new JsonObject();
            notFound.addProperty("NotFind", "查無此餐廳");
            restaurantArray.add(notFound); //
		}
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(restaurantArray.toString()); 
        System.out.println(restaurantArray);
	}

}
