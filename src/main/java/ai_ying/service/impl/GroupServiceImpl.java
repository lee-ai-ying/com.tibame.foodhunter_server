package ai_ying.service.impl;

import java.sql.Date;
import java.util.List;

import javax.naming.NamingException;

import ai_ying.dao.GroupDao;
import ai_ying.dao.impl.GroupDaoImpl;
import ai_ying.service.GroupService;
import ai_ying.vo.Group;
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
		String location = group.getLocation();
		if (location == null || location.isBlank()) {
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
		String location = group.getLocation();
		if (location == null) {
			group.setLocation("");
		}
		Date time= group.getTime();
		if (time == null) {
			group.setTime(new Date(System.currentTimeMillis()));
		}
		String describe = group.getDescribe();
		if (describe == null) {
			group.setDescribe("");
		}

		//System.out.println("service: "+group.toString());
		return groupDao.getGroupsByCondition(group);
	}

	@Override // 取得參加揪團清單
	public List<Group> getGroupList(Member member) {
		Integer memberId = member.getId();
		if (groupDao.selectMemberById(memberId)==null) {
			return null;
		}
		return groupDao.selectAllGroupsByMember(member);
	}

	@Override
	public String joinGroup(Integer groupId, Integer memberId) {
		if (groupDao.selectGroupById(groupId)==null) {
			return "該揪團不存在";
		}
		if (groupDao.selectMemberById(memberId)==null) {
			return "該會員不存在";
		}
		int result = groupDao.insertGroupMember(groupId,memberId);
		return result > 0 ? null : "加入揪團失敗";
	}

	@Override
	public int getGroupId(Group group) {
		return groupDao.getIdAfterCreateGroup(group);
	}
}
