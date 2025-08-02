package br.edu.ifba.inf008.plugins.dao;

import br.edu.ifba.inf008.interfaces.model.Loan;
import java.util.List;

public interface LoanDao {
    void insert(Loan loan);
    Loan findById(int id);
    List<Loan> findAll();
    void update(Loan loan);
    void delete(int id);
}
