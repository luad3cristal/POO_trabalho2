package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.DatabaseService;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.service.UserServiceImpl;
import br.edu.ifba.inf008.interfaces.model.User;

import javafx.scene.control.MenuItem;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


public class UserPlugin implements IPlugin {
    private DatabaseService<User> userService = new UserServiceImpl();

    @Override
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Usuários", "Gerenciar Usuários");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                uiController.createTab("Usuários", createUserView());
            }
        });

        return true;
    }

    private VBox createUserView() {
        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #E6E6FA;"); // Lavender background
        
        // Title
        Label title = new Label("Gerenciamento de Usuários");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Buttons
        HBox buttonBox = new HBox(10);
        Button listButton = new Button("Listar Usuários");
        Button addButton = new Button("Cadastrar Usuário");
        
        listButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        addButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        
        buttonBox.getChildren().addAll(listButton, addButton);
        
        // Content area
        VBox contentArea = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        
        // Button actions
        listButton.setOnAction(e -> showUserList(contentArea));
        addButton.setOnAction(e -> showUserForm(contentArea));
        
        mainContainer.getChildren().addAll(title, buttonBox, scrollPane);
        return mainContainer;
    }
    
    private void showUserList(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label listTitle = new Label("Lista de Usuários");
        listTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
        
        VBox userList = new VBox(5);
        
        try {
            java.util.List<User> users = userService.readAll();
            for (User user : users) {
                String userInfo = String.format("%d. %s - %s - %s", 
                    user.getId(), 
                    user.getName(), 
                    user.getEmail(), 
                    user.getRegisteredAt() != null ? user.getRegisteredAt().toString() : "N/A");
                
                Label userLabel = new Label(userInfo);
                userLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1px;");
                userList.getChildren().add(userLabel);
            }
        } catch (Exception e) {
            Label errorLabel = new Label("Erro ao carregar usuários: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            userList.getChildren().add(errorLabel);
        }
        
        contentArea.getChildren().addAll(listTitle, userList);
    }
    
    private void showUserForm(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label formTitle = new Label("Cadastrar Novo Usuário");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
        
        VBox form = new VBox(10);
        form.setMaxWidth(400);
        
        TextField nameField = new TextField();
        nameField.setPromptText("Nome");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        
        Button saveButton = new Button("Salvar");
        saveButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        Label messageLabel = new Label();
        
        saveButton.setOnAction(e -> {
            try {
                if (nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
                    messageLabel.setText("Por favor, preencha todos os campos.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                User newUser = new User();
                newUser.setName(nameField.getText().trim());
                newUser.setEmail(emailField.getText().trim());
                newUser.setRegisteredAt(new java.util.Date());
                
                userService.create(newUser);
                
                nameField.clear();
                emailField.clear();
                messageLabel.setText("Usuário cadastrado com sucesso!");
                messageLabel.setStyle("-fx-text-fill: green;");
                
            } catch (Exception ex) {
                messageLabel.setText("Erro ao cadastrar usuário: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        form.getChildren().addAll(
            new Label("Nome:"), nameField,
            new Label("Email:"), emailField,
            saveButton, messageLabel
        );
        
        contentArea.getChildren().addAll(formTitle, form);
    }
}
