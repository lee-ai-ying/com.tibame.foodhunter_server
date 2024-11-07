package member.dao.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

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
	public Member selectByUsersave(String table,String data,String username) {
		String sql = "select * from member where  "+table+" = ?"+" and username != ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, data);
			pstmt.setString(2, username);
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
		String sql = "select * from member where  username = ? and password = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, member.getUsername());
			pstmt.setString(2, member.getPassword());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
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
	public int updateById(Member member) {
		String sql = "update member set password = ?,"+" nickname = ?,"+" email = ?,"+" phone = ?"+" where username = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setString(1, member.getPassword());
			pstmt.setString(2, member.getNickname());
			pstmt.setString(3, member.getEmail());
			pstmt.setString(4, member.getPhone());
			pstmt.setString(5, member.getUsername());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public Member selectByUserdata(Member member) {
		String sql = "select * from member where  username = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, member.getUsername());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
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
	public String selectByimage(Member member) {
	    String sql = "select * from member where username = ?";
	    try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, member.getUsername());
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                byte[] imageData = null;
	                InputStream inputStream = rs.getBinaryStream("profileimage");
	                imageData = inputStream.readAllBytes();  // 将输入流转换为 byte[]
	                
	                // 将图片的 byte[] 转换为 Base64 字符串
	                return Base64.getEncoder().encodeToString(imageData);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	@Override
	public int saveProfileImage(Member member) {
	    String sql = "UPDATE member SET profileimage = ? WHERE username = ?";
	    
	    try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        // 设置图片的 InputStream 为 BLOB 类型
	        pstmt.setBinaryStream(1, member.getProfileImageInputStream());
	        
	        // 设置用户名，指定要更新的会员
	        pstmt.setString(2, member.getUsername());
	        
	        // 执行更新操作，返回更新的行数
	        return pstmt.executeUpdate(); // 返回更新的行数，成功时大于0
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // 如果保存失败，捕获异常并返回-1
	        return -1;
	    }
	}

	@Override
	public int friendadd(Member member) {
		String sql = "insert into friend(friend_id1,friend_id2,status) "
				+ "values((select member_id from member where  username = ?),(select member_id from member where username = ?),1)";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, member.getUsername());
			pstmt.setString(2, member.getFriend());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
