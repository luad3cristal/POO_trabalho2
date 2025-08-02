package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.DatabaseService;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.model.Loan;
import br.edu.ifba.inf008.plugins.loans.service.LoanServiceImpl;

import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class LoanPlugin implements IPlugin {
    private DatabaseService<Loan> loanService = new LoanServiceImpl();

    @Override
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Empréstimos", "Listar Empréstimos");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                loanService.readAll().forEach(l -> System.out.println("Empréstimo: " + l.getId()));
            }
        });

        uiController.createTab("Empréstimos", new Rectangle(200, 200, Color.LIGHTBLUE));

        return true;
    }
}
