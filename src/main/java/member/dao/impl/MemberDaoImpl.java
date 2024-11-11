package member.dao.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
	public Member selectByUsername(String table, String username) {
		String sql = "select * from member where  " + table + " = ?";
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
	public Member selectByUsersave(String table, String data, String username) {
		String sql = "select * from member where  " + table + " = ?" + " and username != ?";
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
		String sql = "update member set password = ?," + " nickname = ?," + " email = ?," + " phone = ?"
				+ " where username = ?";
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
	public int updateByPassword(Member member) {
		String sql = "update member set password = ? where email = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, member.getPassword());
			pstmt.setString(2, member.getEmail());
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
	public Member selectByEmail(Member member) {
		String sql = "select * from member where  email = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, member.getEmail());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					member.setEmail(rs.getString("email"));
					member.setPassword(rs.getString("password"));				
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
		String sql = "select profileimage from member where username = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, member.getUsername());
			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {
					byte[] imageData = null;
					InputStream inputStream = rs.getBinaryStream("profileimage");
					imageData = inputStream.readAllBytes(); // 将输入流转换为 byte[]
					System.out.println(Base64.getEncoder().encodeToString(imageData));
					// 将图片的 byte[] 转换为 Base64 字符串
					return Base64.getEncoder().encodeToString(imageData);

				} else {
					System.out.println("圖片數據為 null，無法加載圖片");
					return null; // 如果圖片數據為 null，返回 null
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
		String sql = "insert into friend(friend_id1,friend_id2,status) values((select member_id from member where  username = ?),(select member_id from member where username = ?),1)";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, member.getUsername());
			pstmt.setString(2, member.getFriend());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int frienddel(Member member) {
		String sql = "delete from friend where friend_id1 = (select member_id from member where  username = ?) and friend_id2 = (select member_id from member where username = ?)";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, member.getUsername());
			pstmt.setString(2, member.getFriend());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public List<Member> selectByfriend(Member member) {
		String sql = "select member_id,username,nickname from friend join member on member_id = friend_id2 where friend_id1 = (select member_id from member where username = ?)";
		List<Member> members = new ArrayList<>();
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, member.getUsername()); // 設置 username 作為參數

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					// 從結果集中提取資料
					Member friend = new Member();
					friend.setId(rs.getInt("member_id"));
					friend.setUsername(rs.getString("username"));
					friend.setNickname(rs.getString("nickname"));
					// 將 Member 添加到列表中
					members.add(friend);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return members.isEmpty() ? null : members; // 如果沒找到資料則返回 null，否則返回列表
	}

	@Override
	public List<Member> selectByfriend2(Member member) {
		String sql = "select member_id,username,nickname FROM friend join member on member_id = friend_id1 where friend_id2 =(select member_id from member where username = ?) and member_id not in(select member_id from friend join member on member_id = friend_id2 where friend_id1 = (select member_id from member where username = ?));";
		List<Member> members = new ArrayList<>();
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, member.getUsername()); // 設置 username 作為參數
			pstmt.setString(2, member.getUsername());

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					// 從結果集中提取資料
					Member friend = new Member();
					friend.setId(rs.getInt("member_id"));
					friend.setUsername(rs.getString("username"));
					friend.setNickname(rs.getString("nickname"));
					// 將 Member 添加到列表中
					members.add(friend);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return members.isEmpty() ? null : members; // 如果沒找到資料則返回 null，否則返回列表
	}

	@Override
	public List<Member> selectByRoomId(Member member) {
		String sql = "select concat(friend_id1,\"_\",friend_id2) roomid ,username,nickname from friend join member on member_id = friend_id2 where friend_id1 = (select member_id from member where username = ?)";
		List<Member> members = new ArrayList<>();
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, member.getUsername()); // 設置 username 作為參數

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					// 從結果集中提取資料
					Member roomId = new Member();
					roomId.setRoomid(rs.getString("roomid"));
					roomId.setUsername(rs.getString("username"));
					roomId.setNickname(rs.getString("nickname"));
					// 將 Member 添加到列表中
					members.add(roomId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return members.isEmpty() ? null : members; // 如果沒找到資料則返回 null，否則返回列表
	}

	public List<Member> selectByRoomId2(Member member) {
		String sql = "select distinct concat(friend_id2,\"_\",friend_id1) roomid ,username,nickname from (friend join member on member_id = friend_id1) join chat on friend_id1 = message_id and friend_id2 = receiver_id where  friend_id2 =(select member_id from member where username = ?) and member_id not in(select member_id from friend join member on member_id = friend_id2 where friend_id1 = (select member_id from member where username = ?))";
		List<Member> members = new ArrayList<>();
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, member.getUsername()); // 設置 username 作為參數
			pstmt.setString(2, member.getUsername());
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					// 從結果集中提取資料
					Member roomId = new Member();
					roomId.setRoomid(rs.getString("roomid"));
					roomId.setUsername(rs.getString("username"));
					roomId.setNickname(rs.getString("nickname"));
					// 將 Member 添加到列表中
					members.add(roomId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return members.isEmpty() ? null : members; // 如果沒找到資料則返回 null，否則返回列表
	}

	@Override
	public int insertmessage(Member member) {
		String sql = "insert into chat(message_id,receiver_id,message,message_time) value ((select member_id from member where username = ?),(select member_id from member where username = ?),?,NOW())";
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, member.getMessage_id());
			pstmt.setString(2, member.getReceiver_id());
			pstmt.setString(3, member.getMessage());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;

	}

	@Override
	public List<Member> selectByMessage(Member member) {
		String sql = "select * FROM chat where (message_id = (select member_id from member where username = ?) and receiver_id = (select member_id from member where username = ?)) or (message_id = (select member_id from member where username = ?) and receiver_id = (select member_id from member where username = ?)) order by chat_id desc ";
		List<Member> members = new ArrayList<>();
		try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, member.getMessage_id());
			pstmt.setString(2, member.getReceiver_id());
			pstmt.setString(3, member.getReceiver_id());
			pstmt.setString(4, member.getMessage_id());
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					// 從結果集中提取資料
					Member message = new Member();
					message.setMessage_id(rs.getString("message_id"));
					message.setReceiver_id(rs.getString("receiver_id"));
					message.setMessage(rs.getString("message"));
					message.setMessage_time(rs.getTimestamp("message_time"));
					// 將 Member 添加到列表中
					members.add(message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return members.isEmpty() ? null : members; // 如果沒找到資料則返回 null，否則返回列表
	}

}
