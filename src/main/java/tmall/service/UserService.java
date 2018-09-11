package tmall.service;

import tmall.entity.User;

import java.util.List;

public interface UserService {
    List<User> selectUserList();

    void addUser(User user);

    void deleteUserById(Integer id);

    User selectUserById(Integer id);

    void updateUserById(User user);

    User selectUserByNameAndPassword(String name, String password);

    boolean isExist(String name);
}
