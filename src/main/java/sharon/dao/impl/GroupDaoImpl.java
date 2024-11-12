package sharon.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import sharon.dao.GroupDao;
import sharon.vo.Group;

public class GroupDaoImpl implements GroupDao {
    private DataSource ds;

    public GroupDaoImpl() throws NamingException {
        ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/server/foodhunter");
    }

    @Override
    public List<Group> getAllGroupsByMemberId(int memberId) {
        String sql = "SELECT g.group_id, g.name, g.is_public, g.time, " +
                    "r.restaurant_name, r.address, gm.member_id " +
                    "FROM `group` g " +
                    "JOIN group_member gm ON g.group_id = gm.group_id " +
                    "LEFT JOIN restaurant r ON g.location = r.restaurant_id " +
                    "WHERE gm.member_id = ?";

        List<Group> groups = new ArrayList<>();

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Group group = new Group();
                    group.setGroupId(rs.getInt("group_id"));
                    group.setGroupName(rs.getString("name"));
                    group.setRestaurantName(rs.getString("restaurant_name"));
                    group.setRestaurantAddress(rs.getString("address"));
                    group.setIsPublic(rs.getInt("is_public"));
                    group.setGroupDate(rs.getDate("time"));
                    group.setMemberId(rs.getInt("member_id"));
                    groups.add(group);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }
}