package ai_ying.dao;

import java.util.List;
import java.util.Set;

import ai_ying.vo.FcmToken;
import ai_ying.vo.Group;
import ai_ying.vo.GroupChat;
import ai_ying.vo.GroupMember;
import andysearch.vo.Restaurant;
import jessey.vo.Review;
import member.vo.Member;

public interface GroupDao {
	// 建立揪團
	int insertGroup(Group group);

	// 搜尋揪團
	List<Group> getGroupsByCondition(Group group);

	// 取得參加揪團清單
	List<Group> selectAllGroupsByMember(Member member);

	// 參加揪團
	int insertGroupMember(GroupMember groupMember);

	// 取得揪團資料
	Group selectGroupById(Integer groupId);

	// 取得會員資料
	Member selectMemberByUsername(String username);

	// 建立揪團後取得揪團id
	int getIdAfterCreateGroup(Group group);

	// 傳送訊息
	int insertGroupChat(GroupChat groupChat);

	// 取得聊天紀錄
	List<GroupChat> selectAllGroupChatByGroupId(Group group);

	// 註冊FCM token
	int insertFcmToken(FcmToken fcmToken);

	// 取得特定group的token清單
	Set<String> selectAllTokenByGroupId(Integer groupId);

	// 取得特定token
	String selectTokenByUsername(String username);

	// 離開揪團
	int deleteGroupMember(GroupMember groupMember);
	
	// 取得揪團內頭像
	List<Member> selectAvatarsByGroupId(Integer groupId);
	
	// 取得餐廳清單
	List<Restaurant> selectAllRestaurant();

	// 使用id取得餐廳
	Restaurant selectRestaurantById(int restaurantId);

	// 取得餐廳評論
	Review selectRestaurantReview(Review review);

	// 新增餐廳評論
	int insertReview(Review review);

	// 更新餐廳評論
	int updateReview(Review review);
	
	// 計算total_review
	int getTotalReviewSum(int restaurantId);

	// 計算total_scores
	int getTotalScoresSum(int restaurantId);

	int updateFcmToken(FcmToken fcmToken);
}