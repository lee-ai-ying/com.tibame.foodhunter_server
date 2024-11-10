package ai_ying.service.impl;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import ai_ying.dao.GroupDao;
import ai_ying.dao.impl.GroupDaoImpl;
import ai_ying.service.GroupService;
import ai_ying.vo.FcmToken;
import ai_ying.vo.Group;
import ai_ying.vo.GroupChat;
import ai_ying.vo.GroupMember;
import andysearch.vo.Restaurant;
import jessey.vo.Review;
import member.vo.Member;

public class GroupServiceImpl implements GroupService {
	private GroupDao groupDao;

	public GroupServiceImpl() throws NamingException {
		groupDao = new GroupDaoImpl();
	}

	@Override // 建立揪團
	public String createGroup(Group group) {
		String name = group.getName();
		if (name == null || name.isBlank()) {
			return "必須輸入揪團名稱";
		}
		Integer location = group.getLocation();
		if (location == null) {
			return "必須選擇揪團地點";
		}
		String time = group.getTime().toString();
		if (time == null || time.isBlank()) {
			return "必須選擇揪團時間";
		}
		int result = groupDao.insertGroup(group);
		return result > 0 ? null : "建立揪團失敗";
	}

	@Override // 搜尋揪團
	public List<Group> searchGroups(Group group) {
		String name = group.getName();
		if (name == null) {
			group.setName("");
		}
		String locationName = group.getLocationName();
		if (locationName == null) {
			group.setLocationName("");
		}
		Date time = group.getTime();
		if (time == null) {
			group.setTime(new Date(System.currentTimeMillis()));
		}
		String describe = group.getDescribe();
		if (describe == null) {
			group.setDescribe("");
		}
		// System.out.println("service: "+group.toString());
		return groupDao.getGroupsByCondition(group);
	}

	@Override // 取得參加揪團清單
	public List<Group> getGroupList(Member member) {
		if (groupDao.selectMemberByUsername(member.getUsername()) == null) {
			return null;
		}
		return groupDao.selectAllGroupsByMember(member);
	}

	@Override
	public String joinGroup(GroupMember groupMember) {
		if (groupDao.selectGroupById(groupMember.getGroupId()) == null) {
			return "該揪團不存在";
		}
		if (groupDao.selectMemberByUsername(groupMember.getUsername()) == null) {
			return "該會員不存在";
		}
		int result = groupDao.insertGroupMember(groupMember);
		return result > 0 ? null : "加入揪團失敗";
	}

	@Override
	public int getGroupId(Group group) {
		return groupDao.getIdAfterCreateGroup(group);
	}

	@Override
	public String sendMessage(GroupChat groupChat) {
		if (groupDao.selectGroupById(groupChat.getGroupId()) == null) {
			return "該揪團不存在";
		}
		if (groupDao.selectMemberByUsername(groupChat.getUsername()) == null) {
			return "該會員不存在";
		}
		int result = groupDao.insertGroupChat(groupChat);
		return result > 0 ? null : "傳送訊息失敗";
	}

	@Override
	public List<GroupChat> getGroupChatHistory(Group group) {
		return groupDao.selectAllGroupChatByGroupId(group);
	}

	@Override
	public String registerFcm(FcmToken fcmToken) {
		if (groupDao.selectMemberByUsername(fcmToken.getUsername()) == null) {
			return "該會員不存在";
		}
		if (groupDao.selectTokenByUsername(fcmToken.getUsername()) != null) {
			return "該token已註冊";
		}
		int result = groupDao.insertFcmToken(fcmToken);
		return result > 0 ? null : "註冊fcm失敗";
	}

	@Override
	public Set<String> getTokens(Integer groupId) {
		return groupDao.selectAllTokenByGroupId(groupId);
	}

	@Override
	public String leaveGroup(GroupMember groupMember) {
		if (groupDao.selectGroupById(groupMember.getGroupId()) == null) {
			return "該揪團不存在";
		}
		if (groupDao.selectMemberByUsername(groupMember.getUsername()) == null) {
			return "該會員不存在";
		}
		int result = groupDao.deleteGroupMember(groupMember);
		return result > 0 ? null : "離開揪團失敗";
	}

	@Override
	// 取得頭像清單
	public List<Member> getAvatars(Group group) {
		if (groupDao.selectGroupById(group.getId()) == null) {
			return null;
		}
		return groupDao.selectAvatarsByGroupId(group.getId());
	}

	@Override
	public List<Restaurant> getRestaurantList() {
		return groupDao.selectAllRestaurant();
	}

	@Override // 取得餐廳評論
	public Review getRestaurantReview(Review review) {
		if (groupDao.selectRestaurantById(review.getRestaurantId()) == null) {
			return null;
		}
		if (groupDao.selectMemberByUsername(review.getReviewerNickname()) == null) {
			return null;
		}
		return groupDao.selectRestaurantReview(review);
	}

	@Override // 新增或更新評論
	public String sendRestaurantReview(Review review) {
		if (groupDao.selectRestaurantById(review.getRestaurantId()) == null) {
			return "該餐廳不存在";
		}
		if (groupDao.selectMemberByUsername(review.getReviewerNickname()) == null) {
			System.out.println(review.getReviewerNickname());
			return "該會員不存在";
		}
		if (getRestaurantReview(review)==null) {
			int result = groupDao.insertReview(review);
			return result > 0 ? null : "新增評論失敗";
		}
		else {
			int result = groupDao.updateReview(review);
			return result > 0 ? null : "更新評論失敗";
		}
	}
}
