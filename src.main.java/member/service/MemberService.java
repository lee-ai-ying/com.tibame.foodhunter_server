package member.service;

import member.vo.Member;

public interface MemberService {
	String register(Member member);
	Member login(Member member);
	
	String save(Member member);
}
