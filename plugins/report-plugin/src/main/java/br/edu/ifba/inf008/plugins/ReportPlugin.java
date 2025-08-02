package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.service.ReportService;
import br.edu.ifba.inf008.plugins.reports.service.ReportServiceImpl;

import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

public class ReportPlugin implements IPlugin {
    private final ReportService reportService = new ReportServiceImpl();

    @Override
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Relatórios", "Livros Emprestados");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                uiController.createTab("Relatórios", createReportView());
            }
        });

        return true;
    }

    private VBox createReportView() {
        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #FFFACD;"); // LemonChiffon background
        
        // Title
        Label title = new Label("Relatório de Livros Emprestados");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Report content
        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setPrefHeight(500);
        reportArea.setStyle("-fx-font-family: monospace; -fx-font-size: 14px;");
        
        // Generate report
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE LIVROS EMPRESTADOS ===\n\n");
        
        try {
            java.util.List<String> loanReport = reportService.getLoanReport();
            if (loanReport.isEmpty()) {
                sb.append("Nenhum empréstimo encontrado.\n");
            } else {
                for (String loan : loanReport) {
                    sb.append(loan).append("\n");
                }
            }
        } catch (Exception e) {
            sb.append("Erro ao gerar relatório: ").append(e.getMessage()).append("\n");
        }
        
        sb.append("\n=== FIM DO RELATÓRIO ===");
        reportArea.setText(sb.toString());
        
        mainContainer.getChildren().addAll(title, reportArea);
        return mainContainer;
    }
}
