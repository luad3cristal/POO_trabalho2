package br.edu.ifba.inf008.plugins.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDaoImpl implements ReportDao {
    private final Connection conn;

    public ReportDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<String> getLoanReport() {
        List<String> report = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                "SELECT l.loan_id, u.name AS user, b.title AS book, l.loan_date, l.return_date " +
                "FROM loans l JOIN users u ON l.user_id = u.user_id " +
                "JOIN books b ON l.book_id = b.book_id"
            );
            while (rs.next()) {
                report.add(
                    "Empréstimo #" + rs.getInt("loan_id") +
                    " | Usuário: " + rs.getString("user") +
                    " | Livro: " + rs.getString("book") +
                    " | Data: " + rs.getDate("loan_date") +
                    " | Devolução: " + rs.getDate("return_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }

    @Override
    public List<String> getUserReport() {
        List<String> report = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT user_id, name, email FROM users");
            while (rs.next()) {
                report.add(
                    "Usuário #" + rs.getInt("user_id") +
                    " | Nome: " + rs.getString("name") +
                    " | Email: " + rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }

    @Override
    public List<String> getBookReport() {
        List<String> report = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT book_id, title, author, isbn FROM books");
            while (rs.next()) {
                report.add(
                    "Livro #" + rs.getInt("book_id") +
                    " | Título: " + rs.getString("title") +
                    " | Autor: " + rs.getString("author") +
                    " | ISBN: " + rs.getString("isbn")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }
}
