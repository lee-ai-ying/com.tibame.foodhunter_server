package sharon.service.impl;

import java.util.List;

import javax.naming.NamingException;
import sharon.dao.GroupDao;
import sharon.dao.impl.GroupDaoImpl;
import sharon.service.GroupService;
import sharon.vo.Group;

public class GroupServiceImpl implements GroupService {
    private GroupDao groupDao;

    public GroupServiceImpl() throws NamingException {
        groupDao = new GroupDaoImpl();
    }

    @Override
    public List<Group> getAllGroupsByMemberId(int memberId) {
        return groupDao.getAllGroupsByMemberId(memberId);
    }
}