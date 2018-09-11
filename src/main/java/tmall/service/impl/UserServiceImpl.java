package tmall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tmall.dao.UserMapper;
import tmall.entity.User;
import tmall.entity.UserExample;
import tmall.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> selectUserList() {
        UserExample userExample = new UserExample();
        userExample.setOrderByClause("id DESC");

        return userMapper.selectByExample(userExample);
    }

    @Override
    public void addUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public User selectUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateUserById(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public User selectUserByNameAndPassword(String name, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(name).andPasswordEqualTo(password);

        List<User> users = userMapper.selectByExample(userExample);
        if (!users.isEmpty())
            return users.get(0);
        return null;
    }

    @Override
    public boolean isExist(String name) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(name);

        List<User> user = userMapper.selectByExample(userExample);
        return !user.isEmpty();
    }
}
