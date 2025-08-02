package br.edu.ifba.inf008.plugins.reports.dao;

import java.util.List;

public interface ReportDao {
    List<String> getLoanReport();
    List<String> getUserReport();
    List<String> getBookReport();
}
