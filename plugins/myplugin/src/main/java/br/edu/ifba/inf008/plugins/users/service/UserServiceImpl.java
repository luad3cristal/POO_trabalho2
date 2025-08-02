package br.edu.ifba.inf008.plugins.users.service;

import br.edu.ifba.inf008.interfaces.service.UserService;
import br.edu.ifba.inf008.interfaces.model.User;
import br.edu.ifba.inf008.plugins.DBConnection;
import br.edu.ifba.inf008.plugins.users.dao.UserDao;
import br.edu.ifba.inf008.plugins.users.dao.UserDaoImpl;

import java.sql.Connection;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl() {
        try {
            Connection conn = DBConnection.getConnection();
            this.userDao = new UserDaoImpl(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(User user) {
        userDao.insert(user);
    }

    @Override
    public User read(int id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> readAll() {
        return userDao.findAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void delete(int id) {
        userDao.delete(id);
    }
}
