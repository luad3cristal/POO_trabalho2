package br.edu.ifba.inf008.plugins.books.service;

import br.edu.ifba.inf008.interfaces.service.BookService;
import br.edu.ifba.inf008.interfaces.model.Book;
import br.edu.ifba.inf008.plugins.DBConnection;
import br.edu.ifba.inf008.plugins.books.dao.BookDao;
import br.edu.ifba.inf008.plugins.books.dao.BookDaoImpl;

import java.sql.Connection;
import java.util.List;

public class BookServiceImpl implements BookService {
    private final BookDao bookDao;

    public BookServiceImpl() {
        try {
            Connection conn = DBConnection.getConnection();
            this.bookDao = new BookDaoImpl(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Book book) {
        bookDao.insert(book);
    }

    @Override
    public Book read(int id) {
        return bookDao.findById(id);
    }

    @Override
    public List<Book> readAll() {
        return bookDao.findAll();
    }

    @Override
    public void update(Book book) {
        bookDao.update(book);
    }

    @Override
    public void delete(int id) {
        bookDao.delete(id);
    }
}
