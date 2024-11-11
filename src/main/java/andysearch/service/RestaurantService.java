package andysearch.service;

import java.util.List;
import andysearch.vo.Restaurant;

public interface RestaurantService {

	List<Restaurant> selectRest(String searchtext);
	List<Restaurant> preLoadRestService();
	Integer updateRestReviewAndScores(Restaurant restaurant);
	Restaurant selectRestById(Restaurant restaurant);
}
