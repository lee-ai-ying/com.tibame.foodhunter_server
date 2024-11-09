package member.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import member.dao.MemberDao;
import member.dao.impl.MemberDaoImpl;
import member.service.MemberService;
import member.vo.Member;

public class MemberServiceImpl implements MemberService {
	private MemberDao memberDao;

	public MemberServiceImpl() throws Exception {
		memberDao = new MemberDaoImpl();
	}

	@Override
	public String register(Member member) {

		String username = member.getUsername();
		if (username == null || username.length() < 5 || username.length() > 50) {
			return "使用者名稱長度必須介於5-50";
		}

		String password = member.getPassword();
		if (password == null || password.length() < 6 || password.length() > 12) {
			return "密碼長度必須介於6-12";
		}

		String nickname = member.getNickname();
		if (nickname == null || nickname.length() < 1 || nickname.length() > 20) {
			return "暱稱名稱長度必須介於1-20";
		}

		if (memberDao.selectByUsername("username", username) != null) {
			return "此使用者名稱已被註冊";
		}

		String email = member.getEmail();
		if (memberDao.selectByUsername("email", email) != null) {
			return "此信箱名稱已被註冊";
		}
		String phone = member.getPhone();
		if (memberDao.selectByUsername("phone", phone) != null) {
			return "此號碼已被註冊";
		}
		if (phone.length() != 10) {
			return "請輸入正確的手機號碼";
		}

		int result = memberDao.insert(member);
		return result > 0 ? null : "註冊錯誤";
	}

	@Override
	public Member login(Member member) {
		String username = member.getUsername();
		if (username == null || username.isEmpty()) {
			return null;
		}

		String password = member.getPassword();
		if (password == null || password.isEmpty()) {
			return null;
		}

		return memberDao.selectByUsernameAndPassword(member);
	}

	@Override
	public Member getinfo(Member member) {

		return memberDao.selectByUserdata(member);
	}

	@Override
	public String save(Member member) {

		String password = member.getPassword();
		if (password == null || password.length() < 6 || password.length() > 12) {
			return "密碼長度必須介於6-12";
		}

		String nickname = member.getNickname();
		if (nickname == null || nickname.length() < 1 || nickname.length() > 20) {
			return "暱稱名稱長度必須介於1-20";
		}

		String username = member.getUsername();
		String email = member.getEmail();
		if (memberDao.selectByUsersave("email", email, username) != null) {
			return "此信箱名稱已被註冊";
		}
		String phone = member.getPhone();
		if (memberDao.selectByUsersave("phone", phone, username) != null) {
			return "此號碼已被註冊";
		}
		if (phone.length() != 10) {
			return "請輸入正確的手機號碼";
		}

		int result = memberDao.updateById(member);

		return result > 0 ? null : "編輯錯誤";
	}

	@Override
	public String image(Member member) {
		try {
			// 使用服務層方法來查詢圖片，捕捉異常並處理返回
			String base64Image = memberDao.selectByimage(member);
			if (base64Image != null && !base64Image.isEmpty()) {
				return base64Image;
			} else {
				System.out.println("未能找到用戶的圖片，將返回默認圖片");
				return null; // 或者返回默認圖片的 Base64 編碼
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null; // 若有錯誤，返回 null
		}
	}

	@Override
	public String saveimage(Member member) {
		// 检查是否提供了图片
		if (member.getProfileImageInputStream() != null) {
			try {
				// 调用 memberDao 中的 saveProfileImage 方法保存图片到数据库
				int rowsUpdated = memberDao.saveProfileImage(member);

				if (rowsUpdated > 0) {
					return null; // 成功保存图片
				} else {
					return "Error saving image to database"; // 如果更新失败
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "Error saving image: " + e.getMessage(); // 捕获异常并返回错误信息
			}
		} else {
			return "No image provided"; // 如果没有图片则返回错误信息
		}
	}

	@Override
	public String friendadd(Member member) {
		String username = member.getUsername();
		String friend = member.getFriend();

		if (memberDao.selectByUsername("username", friend) == null) {
			return "好友帳號錯誤";
		}
		int result = memberDao.friendadd(member);

		return result > 0 ? null : "好友帳號錯誤";
	}

	@Override
	public String frienddel(Member member) {
		String username = member.getUsername();
		String friend = member.getFriend();

		int result = memberDao.frienddel(member);

		return result > 0 ? null : "好友帳號錯誤";
	}

	@Override
	public List<Member> getFriends(Member member) {
		return memberDao.selectByfriend(member);

	}

	@Override
	public List<Member> getFriends2(Member member) {
		return memberDao.selectByfriend2(member);

	}

	@Override
	public List<Member> getRoomId(Member member) {
		return memberDao.selectByRoomId(member);

	}

	@Override
	public List<Member> getRoomId2(Member member) {
		return memberDao.selectByRoomId2(member);

	}

}
