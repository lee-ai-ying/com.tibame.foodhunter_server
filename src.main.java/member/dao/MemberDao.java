package member.dao;

import member.vo.Member;


public interface MemberDao {
int insert(Member member);
int updateById(Member member);
Member selectByUsername(String table,String username);
Member selectByUsernameAndPassword(Member member);
}
