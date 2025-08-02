package br.edu.ifba.inf008.plugins.dao;

import br.edu.ifba.inf008.interfaces.model.Loan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDaoImpl implements LoanDao {
    private final Connection conn;

    public LoanDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Loan loan) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO loans (user_id, book_id, loan_date, return_date) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, loan.getUserId());
            stmt.setInt(2, loan.getLivroId());
            stmt.setDate(3, new java.sql.Date(loan.getLoanDate().getTime()));
            if (loan.getReturnDate() != null) {
                stmt.setDate(4, new java.sql.Date(loan.getReturnDate().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loan findById(int id) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM loans WHERE loan_id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Loan(
                    rs.getInt("loan_id"),
                    rs.getInt("user_id"),
                    rs.getInt("book_id"),
                    rs.getDate("loan_date"),
                    rs.getDate("return_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Loan> findAll() {
        List<Loan> loans = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM loans");
            while (rs.next()) {
                loans.add(new Loan(
                    rs.getInt("loan_id"),
                    rs.getInt("user_id"),
                    rs.getInt("book_id"),
                    rs.getDate("loan_date"),
                    rs.getDate("return_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    @Override
    public void update(Loan loan) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE loans SET user_id=?, book_id=?, loan_date=?, return_date=? WHERE loan_id=?")) {
            stmt.setInt(1, loan.getUserId());
            stmt.setInt(2, loan.getLivroId());
            stmt.setDate(3, new java.sql.Date(loan.getLoanDate().getTime()));
            if (loan.getReturnDate() != null) {
                stmt.setDate(4, new java.sql.Date(loan.getReturnDate().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setInt(5, loan.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM loans WHERE loan_id=?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getUserNameById(int userId) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT name FROM users WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Usuário ID: " + userId;
    }
    
    public String getBookTitleById(int bookId) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT title FROM books WHERE book_id = ?")) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("title");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Livro ID: " + bookId;
    }
    
    public Integer getUserIdByName(String userName) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT user_id FROM users WHERE name LIKE ?")) {
            stmt.setString(1, "%" + userName + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Integer getBookIdByTitle(String bookTitle) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT book_id FROM books WHERE title LIKE ?")) {
            stmt.setString(1, "%" + bookTitle + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("book_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
