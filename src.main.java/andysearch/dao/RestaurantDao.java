package andysearch.dao;

import java.util.List;

import andysearch.vo.Restaurant;

public interface RestaurantDao {

	// TODO 以全名/名稱(不一定全名)搜尋
	List<Restaurant> selectByKeywordsAndPrice(List<String> labels, Integer price);
		
	// TODO 預先載入10家餐廳
	List<Restaurant> preLoadRestaurant();
	
	
}
