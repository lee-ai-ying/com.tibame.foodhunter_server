package andysearch.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import andysearch.dao.RestaurantDao;
import andysearch.vo.Restaurant;

public class RestaurantDaoImpl implements RestaurantDao{
	private DataSource ds;

	public RestaurantDaoImpl() throws NamingException {
		ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/server/restaurant");
	}
	@Override
	public List<Restaurant> selectByRestaurantNameOrLabel(Restaurant restaurant) {
	
		String sql = "select * from restaurant where restaurant_name like ? or restaurant_label like ?";
	
		try (
			Connection conn = ds.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)
		){
			List<Restaurant> results = new ArrayList<>();
			pstmt.setString(1, "%" + restaurant.getRestaurantName() + "%");
			pstmt.setString(2, "%" + restaurant.getRestaurantLabel() + "%");
			try (ResultSet rs = pstmt.executeQuery()){
				while (rs.next()) {
					Restaurant rest = new Restaurant();
					rest.setRestaurantId(rs.getInt("restaurant_id"));
					rest.setRestaurantName(rs.getString("restaurant_name"));
					rest.setAddress(rs.getString("address"));
					rest.setTotalScores(rs.getInt("total_scores"));
					rest.setTotalReview(rs.getInt("total_review"));
					rest.setLatitude(rs.getDouble("latitude"));
					rest.setLongitude(rs.getDouble("longitude"));
					rest.setOpeningHours(rs.getString("opening_hours"));
					rest.setHomePhone(rs.getString("home_phone"));
					rest.setPriceRangeMax(rs.getInt("price_range_max"));
					rest.setPriceRangeMin(rs.getInt("price_range_min"));
					rest.setEmail(rs.getString("email"));
					results.add(rest);
				}
				return results;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	@Override
	public List<Restaurant> preLoadRestaurant() {
		String sql = "select * from restaurant limit 10";
	
		try (
			Connection conn = ds.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)
		){
			List<Restaurant> results = new ArrayList<>();
			try (ResultSet rs = pstmt.executeQuery()){
				while (rs.next()) {
					Restaurant rest = new Restaurant();
					rest.setRestaurantId(rs.getInt("restaurant_id"));
					rest.setRestaurantName(rs.getString("restaurant_name"));
					rest.setAddress(rs.getString("address"));
					rest.setTotalScores(rs.getInt("total_scores"));
					rest.setTotalReview(rs.getInt("total_review"));
					rest.setLatitude(rs.getDouble("latitude"));
					rest.setLongitude(rs.getDouble("longitude"));
					rest.setOpeningHours(rs.getString("opening_hours"));
					rest.setHomePhone(rs.getString("home_phone"));
					rest.setPriceRangeMax(rs.getInt("price_range_max"));
					rest.setPriceRangeMin(rs.getInt("price_range_min"));
					rest.setEmail(rs.getString("email"));
					results.add(rest);
				}
				return results;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
}

