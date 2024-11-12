package ai_ying.dao.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import ai_ying.dao.GroupDao;
import ai_ying.vo.FcmToken;
import ai_ying.vo.Group;
import ai_ying.vo.GroupChat;
import ai_ying.vo.GroupMember;
import andysearch.vo.Restaurant;
import jessey.vo.Review;
import member.vo.Member;

public class GroupDaoImpl implements GroupDao {
	private DataSource ds;

	public GroupDaoImpl() throws NamingException {
		ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/server/foodhunter");
	}

	@Override // 取得參加揪團清單
	public List<Group> selectAllGroupsByMember(Member member) {
		String sql = "SELECT * FROM `group_member` AS GM LEFT JOIN `group` AS G ON G.group_id = GM.group_id LEFT JOIN `restaurant` AS R ON R.restaurant_id = G.location WHERE `member_id` = (SELECT `member_id` FROM `member` WHERE `username` = ?)";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, member.getUsername());
			try (ResultSet rs = pstmt.executeQuery();) {
				List<Group> groupList = new ArrayList<>();
				while (rs.next()) {
					Group group = new Group();
					group.setId(rs.getInt("group_id"));
					group.setName(rs.getString("name"));
					group.setLocation(rs.getInt("location"));
					group.setLocationName(rs.getString("restaurant_name"));
					group.setTime(rs.getDate("time"));
					group.setPriceMin(rs.getInt("price_min"));
					group.setPriceMax(rs.getInt("price_max"));
					group.setDescribe(rs.getString("describe"));
					groupList.add(group);
				}
				return groupList;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override // 建立揪團
	public int insertGroup(Group group) {
		String sql = "INSERT INTO `group`(`name`,`location`,`time`,`price_min`,`price_max`,`is_public`,`describe`) "
				+ "values(?,?,?,?,?,?,?)";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, group.getName());
			pstmt.setInt(2, group.getLocation());
			pstmt.setDate(3, group.getTime());
			pstmt.setInt(4, group.getPriceMin());
			pstmt.setInt(5, group.getPriceMax());
			pstmt.setInt(6, group.getIsPublic());
			pstmt.setString(7, group.getDescribe());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override // 搜尋揪團
	public List<Group> getGroupsByCondition(Group group) {
		String sql = "SELECT * FROM `group` WHERE `name` LIKE ? AND `location` IN (SELECT `restaurant_id` FROM `restaurant` WHERE `restaurant_name` LIKE ?) AND `time` = ? AND `price_min` >= ? AND `price_max` <= ? AND `is_public` = 0 AND `describe` LIKE ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, "%" + group.getName() + "%");
			pstmt.setString(2, "%" + group.getLocationName() + "%");
			pstmt.setDate(3, group.getTime());
			pstmt.setInt(4, group.getPriceMin());
			pstmt.setInt(5, group.getPriceMax());
			pstmt.setString(6, "%" + group.getDescribe() + "%");
			try (ResultSet rs = pstmt.executeQuery();) {
				List<Group> resultGroupList = new ArrayList<>();
				while (rs.next()) {
					group = new Group();
					group.setId(rs.getInt("group_id"));
					group.setName(rs.getString("name"));
					group.setLocation(rs.getInt("location"));
					group.setTime(rs.getDate("time"));
					group.setPriceMin(rs.getInt("price_min"));
					group.setPriceMax(rs.getInt("price_max"));
					group.setDescribe(rs.getString("describe"));
					resultGroupList.add(group);
				}
				return resultGroupList;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override // 參加揪團
	public int insertGroupMember(GroupMember groupMember) {
		String sql = "INSERT INTO `group_member`(`group_id`,`member_id`) values(?, (SELECT `member_id` FROM `member` WHERE `username` = ?))";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, groupMember.getGroupId());
			pstmt.setString(2, groupMember.getUsername());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public Group selectGroupById(Integer groupId) {
		String sql = "SELECT * FROM `group` WHERE group_id = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, groupId);
			try (ResultSet rs = pstmt.executeQuery();) {
				if (rs.next()) {
					Group group = new Group();
					group.setId(rs.getInt("group_id"));
					group.setName(rs.getString("name"));
					return group;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Member selectMemberByUsername(String username) {
		String sql = "SELECT * FROM `member` WHERE `username` = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery();) {
				if (rs.next()) {
					Member member = new Member();
					member.setUsername(rs.getString("username"));
					return member;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getIdAfterCreateGroup(Group group) {
		String sql = "SELECT `group_id` FROM `group` WHERE `name` = ? AND `location` = ? AND `time` = ? AND `price_min` = ? AND `price_max` = ? AND `describe` = ? ORDER BY `create_time` DESC LIMIT 1";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, group.getName());
			pstmt.setInt(2, group.getLocation());
			pstmt.setDate(3, group.getTime());
			pstmt.setInt(4, group.getPriceMin());
			pstmt.setInt(5, group.getPriceMax());
			pstmt.setString(6, group.getDescribe());
			try (ResultSet rs = pstmt.executeQuery();) {
				if (rs.next()) {
					return rs.getInt("group_id");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override // 傳送訊息
	public int insertGroupChat(GroupChat groupChat) {
		String sql = "INSERT INTO `group_chat`(`group_id`,`member_id`,`message`) values(?,(SELECT `member_id` FROM `member` WHERE `username` = ?),?)";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, groupChat.getGroupId());
			pstmt.setString(2, groupChat.getUsername());
			pstmt.setString(3, groupChat.getMessage());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override // 取得聊天紀錄
	public List<GroupChat> selectAllGroupChatByGroupId(Group group) {
		String sql = "SELECT `username`, `nickname`, `message`, `send_time` FROM `group_chat` AS GC LEFT JOIN `member` AS M ON GC.`member_id`=M.`member_id` WHERE `group_id` = ? ORDER BY `send_time` DESC;";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, group.getId());
			try (ResultSet rs = pstmt.executeQuery();) {
				List<GroupChat> result = new ArrayList<>();
				while (rs.next()) {
					GroupChat groupChat = new GroupChat();
					groupChat.setUsername(rs.getString("username"));
					groupChat.setMemberName(rs.getString("nickname"));
					groupChat.setMessage(rs.getString("message"));
					groupChat.setSendTime(rs.getTimestamp("send_time"));
					result.add(groupChat);
				}
				return result;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int insertFcmToken(FcmToken fcmToken) {
		String sql = "INSERT INTO `fcm_token`(`member_id`,`token`) values((SELECT `member_id` FROM `member` WHERE `username` = ?), ?)";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, fcmToken.getUsername());
			pstmt.setString(2, fcmToken.getFcmToken());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public Set<String> selectAllTokenByGroupId(Integer group_id) {
		String sql = "SELECT `token` FROM `fcm_token` AS F LEFT JOIN `group_member` AS GM ON F.`member_id` = GM.`member_id` WHERE `group_id` = ?;";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, group_id);
			try (ResultSet rs = pstmt.executeQuery();) {
				Set<String> tokens = Collections.synchronizedSet(new HashSet<>());
				while (rs.next()) {
					tokens.add(rs.getString("token"));
				}
				return tokens;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String selectTokenByUsername(String username) {
		String sql = "SELECT `token` FROM `fcm_token` WHERE `member_id` = (SELECT `member_id` FROM `member` WHERE `username` = ?)";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery();) {
				if (rs.next()) {
					return rs.getString("token");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override // 離開揪團
	public int deleteGroupMember(GroupMember groupMember) {
		String sql = "DELETE FROM `group_member` WHERE `group_id` = ? AND `member_id` = (SELECT `member_id` FROM `member` WHERE `username` = ?)";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, groupMember.getGroupId());
			pstmt.setString(2, groupMember.getUsername());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override // 取得揪團內頭像
	public List<Member> selectAvatarsByGroupId(Integer groupId) {
		String sql = "SELECT `username`, `profileimage` FROM `member` WHERE `member_id` IN (SELECT `member_id` FROM `group_member` WHERE `group_id` = ?);";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, groupId);
			try (ResultSet rs = pstmt.executeQuery();) {
				List<Member> result = new ArrayList<>();
				while (rs.next()) {
					Member member = new Member();
					member.setUsername(rs.getString("username"));
					byte[] imageData = null;
					InputStream inputStream = rs.getBinaryStream("profileimage");
					imageData = inputStream.readAllBytes();
					member.setProfileImageBase64(Base64.getEncoder().encodeToString(imageData));
					result.add(member);
				}
				return result;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Restaurant> selectAllRestaurant() {
		String sql = "SELECT `restaurant_id`, `restaurant_name` FROM `restaurant`;";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			try (ResultSet rs = pstmt.executeQuery();) {
				List<Restaurant> result = new ArrayList<>();
				while (rs.next()) {
					Restaurant restaurant = new Restaurant();
					restaurant.setRestaurantId(rs.getInt("restaurant_id"));
					restaurant.setRestaurantName(rs.getString("restaurant_name"));
					result.add(restaurant);
				}
				return result;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override // 使用id取得餐廳
	public Restaurant selectRestaurantById(int restaurantId) {
		String sql = "SELECT * FROM `restaurant` WHERE `restaurant_id` = ?;";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, restaurantId);
			try (ResultSet rs = pstmt.executeQuery();) {
				if (rs.next()) {
					Restaurant restaurant = new Restaurant();
					restaurant.setRestaurantId(rs.getInt("restaurant_id"));
					return restaurant;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override // 取得餐廳評論
	public Review selectRestaurantReview(Review review) {
		String sql = "SELECT * FROM `review` WHERE `restaurant_id` = ? AND `reviewer` = (SELECT `member_id` FROM `member` WHERE `username` = ?);";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, review.getRestaurantId());
			pstmt.setString(2, review.getReviewerNickname());
			try (ResultSet rs = pstmt.executeQuery();) {
				if (rs.next()) {
					review = new Review();
					review.setComments(rs.getString("comments"));
					review.setRating(rs.getInt("rating"));
					return review;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override // 新增餐廳評論
	public int insertReview(Review review) {
		String sql = "INSERT INTO `review` (`reviewer`,`restaurant_id`,`rating`,`comments`,`review_date`) values((SELECT `member_id` FROM `member` WHERE `username` = ?), ?, ?, ?, ?)";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			 pstmt.setString(1, review.getReviewerNickname());
			 pstmt.setInt(2, review.getRestaurantId());
			 pstmt.setInt(3, review.getRating());
			 pstmt.setString(4, review.getComments());
			 pstmt.setTimestamp(5, review.getReviewDate());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override // 更新餐廳評論
	public int updateReview(Review review) {
		String sql = "UPDATE `review` SET `rating` = ?, `comments` = ?, `review_date` = ? WHERE `reviewer`=(SELECT `member_id` FROM `member` WHERE `username` = ?) AND `restaurant_id`=?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, review.getRating());
			pstmt.setString(2, review.getComments());
			pstmt.setTimestamp(3, review.getReviewDate());
			pstmt.setString(4, review.getReviewerNickname());
			pstmt.setInt(5, review.getRestaurantId());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override // 計算total_review
	public int getTotalReviewSum(int restaurantId) {
		String sql = "SELECT COUNT(*) AS `total_reviews` FROM `review` WHERE `restaurant_id` = ?;";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1,restaurantId);
			try (ResultSet rs = pstmt.executeQuery();) {
				if (rs.next()) {
					return rs.getInt("total_reviews");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override // 計算total_scores
	public int getTotalScoresSum(int restaurantId) {
		String sql = "SELECT SUM(`rating`) AS `total_scores` FROM `review` WHERE `restaurant_id` = ?;";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1,restaurantId);
			try (ResultSet rs = pstmt.executeQuery();) {
				if (rs.next()) {
					return rs.getInt("total_scores");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int updateFcmToken(FcmToken fcmToken) {
		String sql = "UPDATE `fcm_token `SET `token` = ? WHERE `member_id` = (SELECT `member_id` FROM `member` WHERE `username` = ?);";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, fcmToken.getFcmToken());
			pstmt.setString(2, fcmToken.getUsername());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
