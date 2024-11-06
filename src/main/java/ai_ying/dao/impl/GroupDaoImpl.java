package ai_ying.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import ai_ying.dao.GroupDao;
import ai_ying.vo.Group;
import member.vo.Member;

public class GroupDaoImpl implements GroupDao {
	private DataSource ds;

	public GroupDaoImpl() throws NamingException {
		ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/server/foodhunter");
	}

	@Override // 取得參加揪團清單
	public List<Group> selectAllGroupsByMember(Member member) {
		String sql = "SELECT * FROM `group_member` AS GM LEFT JOIN `group` AS G ON G.group_id = GM.group_id WHERE member_id = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, member.getId());
			try (ResultSet rs = pstmt.executeQuery();) {
				List<Group> groupList = new ArrayList<>();
				while (rs.next()) {
					Group group = new Group();
					group.setId(rs.getInt("group_id"));
					group.setName(rs.getString("name"));
					group.setLocation(rs.getString("location"));
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
			pstmt.setString(2, group.getLocation());
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
		String sql = "SELECT * FROM `group` WHERE `name` LIKE ? AND `location` LIKE ? AND `time` = ? AND `price_min` >= ? AND `price_max` <= ? AND `is_public` = 0 AND `describe` LIKE ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, "%" + group.getName() + "%");
			pstmt.setString(2, "%" + group.getLocation() + "%");
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
					group.setLocation(rs.getString("location"));
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
	public int insertGroupMember(Integer groupId, Integer memberId) {
		String sql = "INSERT INTO `group_member`(`group_id`,`member_id`) values(?,?)";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, groupId);
			pstmt.setInt(2, memberId);
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
	public Member selectMemberById(Integer memberId) {
		String sql = "SELECT * FROM `member` WHERE member_id = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, memberId);
			try (ResultSet rs = pstmt.executeQuery();) {
				if (rs.next()) {
					Member member= new Member();
					member.setId(rs.getInt("member_id"));
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
			pstmt.setString(2, group.getLocation());
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

}
