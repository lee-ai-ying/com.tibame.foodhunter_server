package ai_ying.service;

import java.util.List;

import ai_ying.vo.Group;
import member.vo.Member;

public interface GroupService {
	// 建立揪團
	String createGroup(Group group);

	// 搜尋揪團
	List<Group> searchGroups(Group group);

	// 取得參加揪團清單
	List<Group> getGroupList(Member member);

	// 參加揪團
	String joinGroup(Integer groupId, Integer memberId);

	// 取得gorupId
	int getGroupId(Group group);
}
