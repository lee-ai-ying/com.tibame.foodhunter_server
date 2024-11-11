package sharon.dao;

import java.util.List;

import sharon.vo.Group;

public interface GroupDao {
    List<Group> getAllGroupsByMemberId(int memberId);
}