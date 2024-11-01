package andysearch.service.impl;

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

	// TODO 以全名/名稱(不一定全名)搜尋
		
	// TODO 以關鍵字搜尋
	// TODO 以距離搜尋
	// TODO 以時間搜尋
	// TODO 以價格搜尋
	

	@Override
	public List<Restaurant>  selectRest(Restaurant restaurant) {
		String restname = restaurant.getRestaurantName();

		if (restname == null || restname.isEmpty()) {
			return null;
		}
		
		return restaurantDao.selectByRestaurantNameOrLabel(restaurant);	
	}

	@Override
	public List<Restaurant> preLoadRestService() {
		
		return restaurantDao.preLoadRestaurant();
	}

}
