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
                "SELECT l.loan_id, u.name AS user_name, b.title AS book_title, b.author AS book_author, " +
                "l.loan_date, l.return_date " +
                "FROM loans l " +
                "JOIN users u ON l.user_id = u.user_id " +
                "JOIN books b ON l.book_id = b.book_id " +
                "ORDER BY l.loan_date DESC"
            );
            while (rs.next()) {
                String returnInfo = rs.getDate("return_date") != null ? 
                    "Devolvido em: " + rs.getDate("return_date") : 
                    "Livro ainda não devolvido";
                
                report.add(String.format(
                    "ID do Empréstimo: %d | Título: %s | Autor: %s \nUsuário: %s | Data Empréstimo: %s | %s \n\n",
                    rs.getInt("loan_id"),
                    rs.getString("book_title"),
                    rs.getString("book_author"),
                    rs.getString("user_name"),
                    rs.getDate("loan_date"),
                    returnInfo
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            report.add("Erro ao gerar relatório: " + e.getMessage());
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
