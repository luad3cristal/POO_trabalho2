package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.DatabaseService;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.model.Loan;
import br.edu.ifba.inf008.plugins.service.LoanServiceImpl;

import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class LoanPlugin implements IPlugin {
    private DatabaseService<Loan> loanService = new LoanServiceImpl();

    @Override
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Empréstimos", "Gerenciar Empréstimos");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                uiController.createTab("Empréstimos", createLoanView());
            }
        });

        return true;
    }

    private VBox createLoanView() {
        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #F0F8FF;"); // Alice Blue background
        
        // Title
        Label title = new Label("Gerenciamento de Empréstimos");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Buttons
        HBox buttonBox = new HBox(10);
        Button listButton = new Button("Listar Empréstimos");
        Button addButton = new Button("Cadastrar Empréstimo");
        
        listButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        addButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        
        buttonBox.getChildren().addAll(listButton, addButton);
        
        // Content area
        VBox contentArea = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        
        // Button actions
        listButton.setOnAction(e -> showLoanList(contentArea));
        addButton.setOnAction(e -> showLoanForm(contentArea));
        
        mainContainer.getChildren().addAll(title, buttonBox, scrollPane);
        return mainContainer;
    }
    
    private void showLoanList(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label listTitle = new Label("Lista de Empréstimos");
        listTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
        
        VBox loanList = new VBox(5);
        
        try {
            java.util.List<Loan> loans = loanService.readAll();
            for (Loan loan : loans) {
                String loanInfo = String.format("%d. %d - %d - %s - %s", 
                    loan.getId(), 
                    loan.getUserId(), 
                    loan.getLivroId(), 
                    loan.getLoanDate() != null ? loan.getLoanDate().toString() : "N/A",
                    loan.getReturnDate() != null ? loan.getReturnDate().toString() : "Não devolvido");
                
                Label loanLabel = new Label(loanInfo);
                loanLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1px;");
                loanList.getChildren().add(loanLabel);
            }
        } catch (Exception e) {
            Label errorLabel = new Label("Erro ao carregar empréstimos: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            loanList.getChildren().add(errorLabel);
        }
        
        contentArea.getChildren().addAll(listTitle, loanList);
    }
    
    private void showLoanForm(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label formTitle = new Label("Cadastrar Novo Empréstimo");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
        
        VBox form = new VBox(10);
        form.setMaxWidth(400);
        
        TextField userIdField = new TextField();
        userIdField.setPromptText("ID do Usuário");
        
        TextField bookIdField = new TextField();
        bookIdField.setPromptText("ID do Livro");
        
        DatePicker loanDatePicker = new DatePicker();
        loanDatePicker.setPromptText("Data do Empréstimo");
        
        DatePicker returnDatePicker = new DatePicker();
        returnDatePicker.setPromptText("Data de Devolução (opcional)");
        
        Button saveButton = new Button("Salvar");
        saveButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        Label messageLabel = new Label();
        
        saveButton.setOnAction(e -> {
            try {
                if (userIdField.getText().trim().isEmpty() || bookIdField.getText().trim().isEmpty() || 
                    loanDatePicker.getValue() == null) {
                    messageLabel.setText("Por favor, preencha os campos obrigatórios.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                Loan newLoan = new Loan();
                newLoan.setUserId(Integer.parseInt(userIdField.getText().trim()));
                newLoan.setLivroId(Integer.parseInt(bookIdField.getText().trim()));
                newLoan.setLoanDate(java.sql.Date.valueOf(loanDatePicker.getValue()));
                
                if (returnDatePicker.getValue() != null) {
                    newLoan.setReturnDate(java.sql.Date.valueOf(returnDatePicker.getValue()));
                }
                
                loanService.create(newLoan);
                
                userIdField.clear();
                bookIdField.clear();
                loanDatePicker.setValue(null);
                returnDatePicker.setValue(null);
                messageLabel.setText("Empréstimo cadastrado com sucesso!");
                messageLabel.setStyle("-fx-text-fill: green;");
                
            } catch (NumberFormatException ex) {
                messageLabel.setText("Erro: IDs devem ser números válidos.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                messageLabel.setText("Erro ao cadastrar empréstimo: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        form.getChildren().addAll(
            new Label("ID do Usuário:"), userIdField,
            new Label("ID do Livro:"), bookIdField,
            new Label("Data do Empréstimo:"), loanDatePicker,
            new Label("Data de Devolução (opcional):"), returnDatePicker,
            saveButton, messageLabel
        );
        
        contentArea.getChildren().addAll(formTitle, form);
    }
}
