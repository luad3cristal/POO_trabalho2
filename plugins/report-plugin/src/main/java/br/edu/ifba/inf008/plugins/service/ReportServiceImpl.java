package br.edu.ifba.inf008.plugins.service;

import br.edu.ifba.inf008.interfaces.service.ReportService;
import br.edu.ifba.inf008.plugins.dao.ReportDao;
import br.edu.ifba.inf008.plugins.dao.ReportDaoImpl;
import br.edu.ifba.inf008.plugins.DBConnection;

import java.sql.Connection;
import java.util.List;

public class ReportServiceImpl implements ReportService {
    private final ReportDao reportDao;

    public ReportServiceImpl() {
        try {
            Connection conn = DBConnection.getConnection();
            this.reportDao = new ReportDaoImpl(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getLoanReport() {
        return reportDao.getLoanReport();
    }

    @Override
    public List<String> getUserReport() {
        return reportDao.getUserReport();
    }

    @Override
    public List<String> getBookReport() {
        return reportDao.getBookReport();
    }
}
