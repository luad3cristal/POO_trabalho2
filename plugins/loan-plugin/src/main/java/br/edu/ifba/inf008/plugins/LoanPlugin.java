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
    private LoanServiceImpl loanServiceImpl = new LoanServiceImpl();

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
        Button returnButton = new Button("Registrar Devolução");
        Button deleteButton = new Button("Excluir Empréstimo");
        
        listButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        addButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        returnButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        deleteButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        
        buttonBox.getChildren().addAll(listButton, addButton, returnButton, deleteButton);
        
        // Content area
        VBox contentArea = new VBox(10);
        contentArea.setPadding(new Insets(15));
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        
        // Button actions - go directly to forms
        listButton.setOnAction(e -> showLoanList(contentArea));
        addButton.setOnAction(e -> showLoanForm(contentArea));
        returnButton.setOnAction(e -> showReturnForm(contentArea));
        deleteButton.setOnAction(e -> showDeleteLoanForm(contentArea));
        
        mainContainer.getChildren().addAll(title, buttonBox, scrollPane);
        return mainContainer;
    }
    
    private void showLoanList(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label listTitle = new Label("Lista de Empréstimos");
        listTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 0;");
        
        VBox loanList = new VBox(2);
        
        try {
            java.util.List<Loan> loans = loanService.readAll();
            for (Loan loan : loans) {
                String userName = loanServiceImpl.getUserNameById(loan.getUserId());
                String bookTitle = loanServiceImpl.getBookTitleById(loan.getLivroId());

                String loanInfo = String.format("%d. Usuário: %s - Livro: %s \nData de Empréstimo: %s | Data de Devolução: %s",
                    loan.getId(),
                    userName,
                    bookTitle,
                    loan.getLoanDate() != null ? loan.getLoanDate().toString() : "N/A",
                    loan.getReturnDate() != null ? loan.getReturnDate().toString() : "Não devolvido");
                
                Label loanLabel = new Label(loanInfo);
                loanLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
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
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 0;");
        
        VBox form = new VBox(10);
        form.setMaxWidth(400);
        
        // Opção de cadastro
        Label optionLabel = new Label("Escolha o método de cadastro:");
        optionLabel.setStyle("-fx-font-weight: bold;");
        
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton idOption = new RadioButton("Cadastrar por ID");
        RadioButton nameOption = new RadioButton("Cadastrar por Nome");
        idOption.setToggleGroup(toggleGroup);
        nameOption.setToggleGroup(toggleGroup);
        idOption.setSelected(true);
        
        HBox optionBox = new HBox(10);
        optionBox.getChildren().addAll(idOption, nameOption);
        
        // Campos para ID
        VBox idFields = new VBox(10);
        TextField userIdField = new TextField();
        userIdField.setPromptText("ID do Usuário");
        TextField bookIdField = new TextField();
        bookIdField.setPromptText("ID do Livro");
        idFields.getChildren().addAll(
            new Label("ID do Usuário:"), userIdField,
            new Label("ID do Livro:"), bookIdField
        );
        
        // Campos para Nome
        VBox nameFields = new VBox(10);
        TextField userNameField = new TextField();
        userNameField.setPromptText("Nome do Usuário");
        TextField bookNameField = new TextField();
        bookNameField.setPromptText("Título do Livro");
        nameFields.getChildren().addAll(
            new Label("Nome do Usuário:"), userNameField,
            new Label("Título do Livro:"), bookNameField
        );
        nameFields.setVisible(false);
        nameFields.setManaged(false);
        
        // Data
        DatePicker loanDatePicker = new DatePicker();
        loanDatePicker.setPromptText("Data do Empréstimo");
        loanDatePicker.setValue(java.time.LocalDate.now());
        
        DatePicker returnDatePicker = new DatePicker();
        returnDatePicker.setPromptText("Data de Devolução (opcional)");
        
        Button saveButton = new Button("Salvar");
        saveButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        Label messageLabel = new Label();
        
        idOption.setOnAction(e -> {
            idFields.setVisible(true);
            idFields.setManaged(true);
            nameFields.setVisible(false);
            nameFields.setManaged(false);
        });
        
        nameOption.setOnAction(e -> {
            idFields.setVisible(false);
            idFields.setManaged(false);
            nameFields.setVisible(true);
            nameFields.setManaged(true);
        });
        
        saveButton.setOnAction(e -> {
            try {
                if (loanDatePicker.getValue() == null) {
                    messageLabel.setText("Por favor, selecione a data do empréstimo.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                Integer userId = null;
                Integer bookId = null;
                
                if (idOption.isSelected()) {
                    // Cadastro por ID
                    if (userIdField.getText().trim().isEmpty() || bookIdField.getText().trim().isEmpty()) {
                        messageLabel.setText("Por favor, preencha os IDs do usuário e livro.");
                        messageLabel.setStyle("-fx-text-fill: red;");
                        return;
                    }
                    userId = Integer.parseInt(userIdField.getText().trim());
                    bookId = Integer.parseInt(bookIdField.getText().trim());
                } else {
                    // Cadastro por Nome
                    if (userNameField.getText().trim().isEmpty() || bookNameField.getText().trim().isEmpty()) {
                        messageLabel.setText("Por favor, preencha o nome do usuário e título do livro.");
                        messageLabel.setStyle("-fx-text-fill: red;");
                        return;
                    }
                    userId = loanServiceImpl.getUserIdByName(userNameField.getText().trim());
                    bookId = loanServiceImpl.getBookIdByTitle(bookNameField.getText().trim());
                    
                    if (userId == null) {
                        messageLabel.setText("Usuário não encontrado com o nome: " + userNameField.getText());
                        messageLabel.setStyle("-fx-text-fill: red;");
                        return;
                    }
                    if (bookId == null) {
                        messageLabel.setText("Livro não encontrado com o título: " + bookNameField.getText());
                        messageLabel.setStyle("-fx-text-fill: red;");
                        return;
                    }
                }
                
                Loan newLoan = new Loan();
                newLoan.setUserId(userId);
                newLoan.setLivroId(bookId);
                newLoan.setLoanDate(java.sql.Date.valueOf(loanDatePicker.getValue()));
                
                if (returnDatePicker.getValue() != null) {
                    newLoan.setReturnDate(java.sql.Date.valueOf(returnDatePicker.getValue()));
                }
                
                loanService.create(newLoan);
                
                // Limpar campos
                userIdField.clear();
                bookIdField.clear();
                userNameField.clear();
                bookNameField.clear();
                loanDatePicker.setValue(java.time.LocalDate.now());
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
            optionLabel, optionBox,
            idFields, nameFields,
            new Label("Data do Empréstimo:"), loanDatePicker,
            new Label("Data de Devolução (opcional):"), returnDatePicker,
            saveButton, messageLabel
        );
        
        contentArea.getChildren().addAll(formTitle, form);
    }
    
    private void showReturnForm(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label formTitle = new Label("Registrar Devolução");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 0;");
        
        VBox form = new VBox(10);
        form.setMaxWidth(400);
        
        TextField loanIdField = new TextField();
        loanIdField.setPromptText("ID do Empréstimo");
        
        Label loanInfoLabel = new Label();
        loanInfoLabel.setStyle("-fx-font-size: 14px; -fx-wrap-text: true;");
        loanInfoLabel.setMaxWidth(Double.MAX_VALUE);
        
        DatePicker returnDatePicker = new DatePicker();
        returnDatePicker.setPromptText("Data de Devolução");
        returnDatePicker.setValue(java.time.LocalDate.now());
        
        Button loadButton = new Button("Carregar Empréstimo");
        loadButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 15px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        
        Button returnButton = new Button("Registrar Devolução");
        returnButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        returnButton.setDisable(true);
        
        Label messageLabel = new Label();
        
        loadButton.setOnAction(e -> {
            try {
                if (loanIdField.getText().trim().isEmpty()) {
                    messageLabel.setText("Por favor, informe o ID do empréstimo.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                int id = Integer.parseInt(loanIdField.getText().trim());
                Loan loan = loanService.read(id);
                
                if (loan != null) {
                    if (loan.getReturnDate() != null) {
                        loanInfoLabel.setText("Este empréstimo já foi devolvido em: " + loan.getReturnDate());
                        returnButton.setDisable(true);
                        messageLabel.setText("Empréstimo já devolvido.");
                        messageLabel.setStyle("-fx-text-fill: orange;");
                    } else {
                        String userName = loanServiceImpl.getUserNameById(loan.getUserId());
                        String bookTitle = loanServiceImpl.getBookTitleById(loan.getLivroId());
                        loanInfoLabel.setText(String.format("ID: %d - Usuário: %s - Livro: %s\nData do Empréstimo: %s", 
                            loan.getId(), userName, bookTitle, loan.getLoanDate()));
                        returnButton.setDisable(false);
                        messageLabel.setText("Empréstimo carregado. Pronto para devolução.");
                        messageLabel.setStyle("-fx-text-fill: green;");
                    }
                } else {
                    loanInfoLabel.setText("Empréstimo não encontrado.");
                    returnButton.setDisable(true);
                    messageLabel.setText("Empréstimo não encontrado.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                }
                
            } catch (NumberFormatException ex) {
                messageLabel.setText("ID deve ser um número válido.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                messageLabel.setText("Erro ao carregar empréstimo: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        returnButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(loanIdField.getText().trim());
                Loan loan = loanService.read(id);
                
                if (loan != null && loan.getReturnDate() == null) {
                    loan.setReturnDate(java.sql.Date.valueOf(returnDatePicker.getValue()));
                    loanService.update(loan);
                    
                    loanIdField.clear();
                    loanInfoLabel.setText("");
                    returnButton.setDisable(true);
                    messageLabel.setText("Devolução registrada com sucesso!");
                    messageLabel.setStyle("-fx-text-fill: green;");
                } else {
                    messageLabel.setText("Erro: Empréstimo não encontrado ou já devolvido.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                }
                
            } catch (Exception ex) {
                messageLabel.setText("Erro ao registrar devolução: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        form.getChildren().addAll(
            new Label("ID do Empréstimo:"), loanIdField, loadButton,
            new Label("Informações do Empréstimo:"), loanInfoLabel,
            new Label("Data de Devolução:"), returnDatePicker,
            returnButton, messageLabel
        );
        
        contentArea.getChildren().addAll(formTitle, form);
    }
    
    private void showDeleteLoanForm(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label formTitle = new Label("Excluir Empréstimo");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 0;");
        
        VBox form = new VBox(10);
        form.setMaxWidth(400);
        
        TextField idField = new TextField();
        idField.setPromptText("ID do Empréstimo");
        
        Label loanInfoLabel = new Label();
        loanInfoLabel.setStyle("-fx-font-size: 14px; -fx-wrap-text: true;");
        loanInfoLabel.setMaxWidth(Double.MAX_VALUE);
        
        Button loadButton = new Button("Carregar");
        loadButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 15px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        
        Button deleteButton = new Button("EXCLUIR");
        deleteButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #F44336; -fx-text-fill: white;");
        deleteButton.setDisable(true);
        
        Label messageLabel = new Label();
        
        loadButton.setOnAction(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    messageLabel.setText("Por favor, informe o ID do empréstimo.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                int id = Integer.parseInt(idField.getText().trim());
                Loan loan = loanService.read(id);
                
                if (loan != null) {
                    String userName = loanServiceImpl.getUserNameById(loan.getUserId());
                    String bookTitle = loanServiceImpl.getBookTitleById(loan.getLivroId());
                    loanInfoLabel.setText(String.format("ID: %d  - Usuário: %s - Livro: %s \nEmpréstimo: %s - Devolução: %s", 
                        loan.getId(), userName, bookTitle, 
                        loan.getLoanDate(), loan.getReturnDate() != null ? loan.getReturnDate().toString() : "Pendente"));
                    deleteButton.setDisable(false);
                    messageLabel.setText("Empréstimo carregado. Confirme a exclusão.");
                    messageLabel.setStyle("-fx-text-fill: orange;");
                } else {
                    loanInfoLabel.setText("Empréstimo não encontrado.");
                    deleteButton.setDisable(true);
                    messageLabel.setText("Empréstimo não encontrado.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                }
                
            } catch (NumberFormatException ex) {
                messageLabel.setText("ID deve ser um número válido.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                messageLabel.setText("Erro ao carregar empréstimo: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        deleteButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                loanService.delete(id);
                
                idField.clear();
                loanInfoLabel.setText("");
                deleteButton.setDisable(true);
                messageLabel.setText("Empréstimo excluído com sucesso!");
                messageLabel.setStyle("-fx-text-fill: green;");
                
            } catch (Exception ex) {
                messageLabel.setText("Erro ao excluir empréstimo: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        form.getChildren().addAll(
            new Label("ID do Empréstimo:"), idField, loadButton,
            new Label("Informações do Empréstimo:"), loanInfoLabel,
            deleteButton, messageLabel
        );
        
        contentArea.getChildren().addAll(formTitle, form);
    }
}
