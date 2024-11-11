package sharon.service;

import java.util.List;

import sharon.vo.Group;


public interface GroupService {
    List<Group> getAllGroupsByMemberId(int memberId);
}