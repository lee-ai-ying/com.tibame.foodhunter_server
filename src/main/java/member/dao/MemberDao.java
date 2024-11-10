package member.dao;

import java.util.List;

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
int frienddel(Member member);
List<Member> selectByfriend(Member member);
List<Member> selectByfriend2(Member member);
List<Member> selectByRoomId(Member member);
List<Member> selectByRoomId2(Member member);
}
