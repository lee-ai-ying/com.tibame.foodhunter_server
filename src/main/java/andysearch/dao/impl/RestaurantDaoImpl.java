package andysearch.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import andysearch.dao.RestaurantDao;
import andysearch.vo.Restaurant;

public class RestaurantDaoImpl implements RestaurantDao{
	private DataSource ds;

	public RestaurantDaoImpl() throws NamingException {
		ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/server/foodhunter");
	}
	@Override
	public List<Restaurant> selectByKeywordsAndPrice(List<String> labels, Integer price) {
	
		StringBuilder sql = new StringBuilder("select * from restaurant where");
		Pattern cityDistrictPattern = Pattern.compile(".*市.*區");
		List<String> conditions = new ArrayList<>();
		for (String label: labels) {
	        Matcher matcher = cityDistrictPattern.matcher(label);
	        if (matcher.matches()) {
	            conditions.add("(address LIKE ?)");
	        } else {
	        	conditions.add("(restaurant_name like ? OR restaurant_label like ?)");
	        }
		}
		
		if (price != null) {
			conditions.add("(price_range_min <= ? AND price_range_max >= ?)");
		}
		sql.append(String.join("and", conditions));
		
		try ( 
			Connection conn = ds.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString())
		){
			int index = 1;
			for (String label: labels) {
				String keyword = "%" + label + "%";
	            Matcher matcher = cityDistrictPattern.matcher(label);
	            if (matcher.matches()) {
	                pstmt.setString(index++, keyword); // address
	            } else {
					pstmt.setString(index++, keyword); // name
					pstmt.setString(index++, keyword); // label
	            }
			
			}
			if (price != null) {
				pstmt.setInt(index++, price);
				pstmt.setInt(index++, price);
			}
			List<Restaurant> results = new ArrayList<>();
			try (ResultSet rs = pstmt.executeQuery()){
				while (rs.next()) {
					Restaurant rest = new Restaurant();
					rest.setRestaurantId(rs.getInt("restaurant_id"));
					rest.setRestaurantName(rs.getString("restaurant_name"));
					rest.setAddress(rs.getString("address"));
					rest.setRestaurantLabel(rs.getString("restaurant_label"));
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
	
	

	@Override
	public List<Restaurant> preLoadRestaurant() {
		String sql = "select * from restaurant where restaurant_id >= (SELECT FLOOR( MAX(restaurant_id) * RAND()) FROM restaurant )"
				+ "ORDER BY restaurant_id LIMIT 10";
	
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

