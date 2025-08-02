package br.edu.ifba.inf008.interfaces.service;

import java.util.List;

public interface ReportService {
    List<String> getLoanReport();
    List<String> getUserReport();
    List<String> getBookReport();
}
