package andysearch.dao;

import java.util.List;

import andysearch.vo.Restaurant;

public interface RestaurantDao {

	//  以關鍵字搜尋
	List<Restaurant> selectByKeywordsAndPrice(List<String> labels, Integer price);
		
	//  預先載入10家餐廳
	List<Restaurant> preLoadRestaurant();
	
	Integer updateRestReviewAndScores(Integer restaurant_id, Integer total_review, Integer total_scores);
}
