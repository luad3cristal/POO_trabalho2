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
import javafx.scene.shape.Rectangle;

public class ReportPlugin implements IPlugin {
    private final ReportService reportService = new ReportServiceImpl();

    @Override
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Relatórios", "Ver Relatórios");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                sb.append("=== Relatório de Empréstimos ===\n");
                reportService.getLoanReport().forEach(l -> sb.append(l).append("\n"));
                sb.append("\n=== Relatório de Usuários ===\n");
                reportService.getUserReport().forEach(u -> sb.append(u).append("\n"));
                sb.append("\n=== Relatório de Livros ===\n");
                reportService.getBookReport().forEach(b -> sb.append(b).append("\n"));

                TextArea reportArea = new TextArea(sb.toString());
                uiController.createTab("Relatórios", reportArea);
            }
        });

        // Tab inicial (opcional)
        uiController.createTab("Relatórios", new Rectangle(200, 200, Color.LIGHTYELLOW));

        return true;
    }
}
