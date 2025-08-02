package br.edu.ifba.inf008.plugins.dao;

import br.edu.ifba.inf008.interfaces.model.Book;
import java.util.List;

public interface BookDao {
    void insert(Book book);
    Book findById(int id);
    List<Book> findAll();
    void update(Book book);
    void delete(int id);
}
