package andysearch.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import andysearch.dao.RestaurantDao;
import andysearch.dao.impl.RestaurantDaoImpl;
import andysearch.service.RestaurantService;
import andysearch.vo.Restaurant;

public class RestaurantServiceImpl implements RestaurantService{
	
	private RestaurantDao restaurantDao;
	
	public RestaurantServiceImpl() throws NamingException {
		restaurantDao = new RestaurantDaoImpl();
	}

	

	@Override
	public List<Restaurant> selectRest(String searchtext) {
		String[] keywords = searchtext.split(" ");
		Integer price = null;
		List<String> labels = new ArrayList<>();
		
		for (String keyword: keywords) {
			if (isNumeric(keyword)) {
				price = Integer.parseInt(keyword);
			} else {
				labels.add(keyword);
			}
		}
		
		
		return restaurantDao.selectByKeywordsAndPrice(labels, price);	
	}

	@Override
	public List<Restaurant> preLoadRestService() {
		
		return restaurantDao.preLoadRestaurant();
	}

	
	// 判斷是否為數字
	private boolean isNumeric(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
}