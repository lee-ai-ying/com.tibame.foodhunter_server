package member.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import member.dao.MemberDao;
import member.vo.Member;

public class MemberDaoImpl implements MemberDao {
	private DataSource ds;

	public MemberDaoImpl() throws NamingException {
		ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/server/foodhunter");
	}

	@Override
	public int insert(Member member) {
		String sql = "insert into member(username,password,nickname,email,phone,registrationdate,gender,birthday) "
				+ "values(?,?,?,?,?,CURDATE(),?,?)";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, member.getUsername());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getNickname());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getPhone());
			pstmt.setString(6, member.getGender());
			pstmt.setTimestamp(7, member.getBirthday());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public Member selectByUsername(String table,String username) {
		String sql = "select * from member where  "+table+" = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					Member member = new Member();
					member.setId(rs.getInt("member_id"));
					member.setUsername(rs.getString("username"));
					member.setPassword(rs.getString("password"));
					member.setNickname(rs.getString("nickname"));
					member.setEmail(rs.getString("email"));
					member.setPhone(rs.getString("phone"));
					member.setRegistrationdate(rs.getTimestamp("registrationdate"));
					member.setGender(rs.getString("gender"));
					member.setBirthday(rs.getTimestamp("birthday"));
					return member;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Member selectByUsernameAndPassword(Member member) {
		String sql = "select * from MEMBER where  USERNAME = ? and PASSWORD = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, member.getUsername());
			pstmt.setString(2, member.getPassword());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					member = new Member();
					member.setId(rs.getInt("ID"));
					member.setUsername(rs.getString("USERNAME"));
					member.setPassword(rs.getString("PASSWORD"));
					member.setNickname(rs.getString("NICKNAME"));
					member.setPass(rs.getBoolean("PASS"));
					member.setCreator(rs.getString("CREATOR"));

					member.setUpdater(rs.getString("UPDATER"));
					return member;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateById(Member member) {
		String sql = "update MEMBER "+"set"+" PASSWORD = ?,"+" NICKNAME = ?"+" where ID = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setString(1, member.getPassword());
			pstmt.setString(2, member.getNickname());
			pstmt.setInt(3, member.getId());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
