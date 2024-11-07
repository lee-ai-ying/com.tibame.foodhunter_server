package ai_ying.service;

import java.util.List;

import ai_ying.vo.Group;
import ai_ying.vo.GroupChat;
import ai_ying.vo.GroupMember;
import member.vo.Member;

public interface GroupService {
	// 建立揪團
	String createGroup(Group group);

	// 搜尋揪團
	List<Group> searchGroups(Group group);

	// 取得參加揪團清單
	List<Group> getGroupList(Member member);

	// 參加揪團
	String joinGroup(GroupMember groupMember);

	// 取得gorupId
	int getGroupId(Group group);

	// 傳送訊息
	String sendMessage(GroupChat groupChat);

	// 取得聊天紀錄
	List<GroupChat> getGroupChatHistory(Group group);
}
