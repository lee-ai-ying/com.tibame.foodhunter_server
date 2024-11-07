package member.dao;

import member.vo.Member;


public interface MemberDao {
int insert(Member member);
int updateById(Member member);
Member selectByUsername(String table,String username);
Member selectByUsersave(String table,String data,String username);
Member selectByUsernameAndPassword(Member member);
Member selectByUserdata(Member member);
String selectByimage(Member member);
int saveProfileImage(Member member);
int friendadd(Member member);
}
