package member.service;

import java.util.List;

import member.vo.Member;

public interface MemberService {
	String register(Member member);
	Member login(Member member);
	Member getinfo(Member member);
	String save(Member member);
	String image(Member member);
	String saveimage(Member member);
	String friendadd(Member member);
	String frienddel(Member member);
	List<Member> getFriends(Member member);
	List<Member> getFriends2(Member member);
	List<Member> getRoomId(Member member);
	List<Member> getRoomId2(Member member);
}
