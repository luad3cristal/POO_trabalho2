package br.edu.ifba.inf008.plugins.service;

import br.edu.ifba.inf008.interfaces.service.LoanService;
import br.edu.ifba.inf008.interfaces.model.Loan;
import br.edu.ifba.inf008.plugins.DBConnection;
import br.edu.ifba.inf008.plugins.dao.LoanDao;
import br.edu.ifba.inf008.plugins.dao.LoanDaoImpl;

import java.sql.Connection;
import java.util.List;

public class LoanServiceImpl implements LoanService {
    private final LoanDao loanDao;

    public LoanServiceImpl() {
        try {
            Connection conn = DBConnection.getConnection();
            this.loanDao = new LoanDaoImpl(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Loan loan) {
        loanDao.insert(loan);
    }

    @Override
    public Loan read(int id) {
        return loanDao.findById(id);
    }

    @Override
    public List<Loan> readAll() {
        return loanDao.findAll();
    }

    @Override
    public void update(Loan loan) {
        loanDao.update(loan);
    }

    @Override
    public void delete(int id) {
        loanDao.delete(id);
    }
    
    public String getUserNameById(int userId) {
        return loanDao.getUserNameById(userId);
    }
    
    public String getBookTitleById(int bookId) {
        return loanDao.getBookTitleById(bookId);
    }
    
    public Integer getUserIdByName(String userName) {
        return loanDao.getUserIdByName(userName);
    }
    
    public Integer getBookIdByTitle(String bookTitle) {
        return loanDao.getBookIdByTitle(bookTitle);
    }
}
