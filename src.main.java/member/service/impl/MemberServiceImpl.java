package member.service.impl;

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
		if(username == null ||username.length()<5 ||username.length()>50) {
		return "使用者名稱長度必須介於5-50";
		}
		
		String password = member.getPassword();
		if(password == null ||password.length()<6 ||password.length()>12) {
			return "密碼長度必須介於6-12";
		}
		
		
		String nickname = member.getNickname();
		if(nickname == null ||nickname.length()<1 ||nickname.length()>20) {
			return "暱稱名稱長度必須介於1-20";
		}		
			
		if(memberDao.selectByUsername("username",username) != null) {
		return "此使用者名稱已被註冊";
		}
		
		String email = member.getEmail();
		if(memberDao.selectByUsername("email",email) != null) {
			return "此信箱名稱已被註冊";
			}
		String phone = member.getPhone();
		if(memberDao.selectByUsername("phone",phone) != null) {
			return "此號碼已被註冊";
			}
		if(phone.length() != 10) {
			return "請輸入正確的手機號碼";
		}
		
		int result = memberDao.insert(member);
		return result > 0 ? null:"註冊錯誤";
}
	@Override
	public Member login(Member member) {
		String username = member.getUsername();
		if(username == null || username.isEmpty()) {
		return null;
		}
		
		String password = member.getPassword();
		if(password == null ||password.isEmpty()) {
			return null;
		}
		
		
		
		return memberDao.selectByUsernameAndPassword(member);
	}
	@Override
	public String save(Member member) {
		
		String password = member.getPassword();
		if(password == null ||password.length()<6 ||password.length()>12) {
			return "密碼長度必須介於6-12";
		}
		
		
		String nickname = member.getNickname();
		if(nickname == null ||nickname.length()<1 ||nickname.length()>20) {
			return "暱稱名稱長度必須介於1-20";
		}		
			
		
		String email = member.getEmail();
		if(memberDao.selectByUsername("email",email) != null) {
			return "此信箱名稱已被註冊";
			}
		String phone = member.getPhone();
		if(memberDao.selectByUsername("phone",phone) != null) {
			return "此號碼已被註冊";
			}
		if(phone.length() != 10) {
			return "請輸入正確的手機號碼";
		}
		
		
		
		
		
		int result = memberDao.updateById(member);
		
		return result>0 ? null :"編輯錯誤";
	}
}
