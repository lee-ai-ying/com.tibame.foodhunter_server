package andysearch.service;

import java.util.List;
import andysearch.vo.Restaurant;

public interface RestaurantService {

	List<Restaurant> selectRest(Restaurant restaurant);
	List<Restaurant> preLoadRestService();
}
