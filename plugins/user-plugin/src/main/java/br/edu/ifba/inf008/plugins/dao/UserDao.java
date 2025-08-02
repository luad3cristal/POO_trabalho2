package br.edu.ifba.inf008.plugins.dao;

import br.edu.ifba.inf008.interfaces.model.User;
import java.util.List;

public interface UserDao {
    void insert(User user);
    User findById(int id);
    List<User> findAll();
    void update(User user);
    void delete(int id);
}
