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
import javafx.geometry.Insets;

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
        Button editButton = new Button("Editar Usuário");
        Button deleteButton = new Button("Excluir Usuário");
        
        listButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        addButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        editButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        deleteButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        
        buttonBox.getChildren().addAll(listButton, addButton, editButton, deleteButton);
        
        // Content area
        VBox contentArea = new VBox(10);
        contentArea.setPadding(new Insets(15));
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        
        // Button actions - go directly to forms
        listButton.setOnAction(e -> showUserList(contentArea));
        addButton.setOnAction(e -> showUserForm(contentArea));
        editButton.setOnAction(e -> showEditUserForm(contentArea));
        deleteButton.setOnAction(e -> showDeleteUserForm(contentArea));
        
        mainContainer.getChildren().addAll(title, buttonBox, scrollPane);
        return mainContainer;
    }
    
    private void showUserList(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label listTitle = new Label("Lista de Usuários");
        listTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 0;");

        VBox userList = new VBox(2);

        try {
            java.util.List<User> users = userService.readAll();
            for (User user : users) {
                String registrationInfo = user.getRegisteredAt() != null ? 
                    "Data e hora de registro: " + user.getRegisteredAt().toString() : 
                    "Data e hora de registro: N/A";
                
                String userInfo = String.format("%d. %s - %s - %s", 
                    user.getId(), 
                    user.getName(), 
                    user.getEmail(), 
                    registrationInfo);
                
                Label userLabel = new Label(userInfo);
                userLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
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
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 0;");
        
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
    
    private void showEditUserForm(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label formTitle = new Label("Editar Usuário");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 0;");
        
        VBox form = new VBox(10);
        form.setMaxWidth(400);
        
        TextField idField = new TextField();
        idField.setPromptText("ID do Usuário");
        
        TextField nameField = new TextField();
        nameField.setPromptText("Nome");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        
        Button loadButton = new Button("Carregar");
        loadButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 15px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        
        Button saveButton = new Button("Salvar Alterações");
        saveButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        Label messageLabel = new Label();
        
        loadButton.setOnAction(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    messageLabel.setText("Por favor, informe o ID do usuário.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                int id = Integer.parseInt(idField.getText().trim());
                User user = userService.read(id);
                
                if (user != null) {
                    nameField.setText(user.getName());
                    emailField.setText(user.getEmail());
                    messageLabel.setText("Usuário carregado com sucesso!");
                    messageLabel.setStyle("-fx-text-fill: green;");
                } else {
                    messageLabel.setText("Usuário não encontrado.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                }
                
            } catch (NumberFormatException ex) {
                messageLabel.setText("ID deve ser um número válido.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                messageLabel.setText("Erro ao carregar usuário: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        saveButton.setOnAction(e -> {
            try {
                if (idField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty() || 
                    emailField.getText().trim().isEmpty()) {
                    messageLabel.setText("Por favor, preencha todos os campos.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                User user = new User();
                user.setId(Integer.parseInt(idField.getText().trim()));
                user.setName(nameField.getText().trim());
                user.setEmail(emailField.getText().trim());
                user.setRegisteredAt(new java.util.Date());
                
                userService.update(user);
                
                messageLabel.setText("Usuário atualizado com sucesso!");
                messageLabel.setStyle("-fx-text-fill: green;");
                
            } catch (NumberFormatException ex) {
                messageLabel.setText("ID deve ser um número válido.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                messageLabel.setText("Erro ao atualizar usuário: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        form.getChildren().addAll(
            new Label("ID do Usuário:"), idField, loadButton,
            new Label("Nome:"), nameField,
            new Label("Email:"), emailField,
            saveButton, messageLabel
        );
        
        contentArea.getChildren().addAll(formTitle, form);
    }
    
    private void showDeleteUserForm(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label formTitle = new Label("Excluir Usuário");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 0;");
        
        VBox form = new VBox(10);
        form.setMaxWidth(400);
        
        TextField idField = new TextField();
        idField.setPromptText("ID do Usuário");
        
        Label userInfoLabel = new Label();
        userInfoLabel.setStyle("-fx-font-size: 14px; -fx-wrap-text: true;");
        userInfoLabel.setMaxWidth(Double.MAX_VALUE);
        
        Button loadButton = new Button("Carregar");
        loadButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 15px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        
        Button deleteButton = new Button("EXCLUIR");
        deleteButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #F44336; -fx-text-fill: white;");
        deleteButton.setDisable(true);
        
        Label messageLabel = new Label();
        
        loadButton.setOnAction(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    messageLabel.setText("Por favor, informe o ID do usuário.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                int id = Integer.parseInt(idField.getText().trim());
                User user = userService.read(id);
                
                if (user != null) {
                    userInfoLabel.setText(String.format("ID: %d - Nome: %s - Email: %s", 
                        user.getId(), user.getName(), user.getEmail()));
                    deleteButton.setDisable(false);
                    messageLabel.setText("Usuário carregado. Confirme a exclusão.");
                    messageLabel.setStyle("-fx-text-fill: orange;");
                } else {
                    userInfoLabel.setText("Usuário não encontrado.");
                    deleteButton.setDisable(true);
                    messageLabel.setText("Usuário não encontrado.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                }
                
            } catch (NumberFormatException ex) {
                messageLabel.setText("ID deve ser um número válido.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                messageLabel.setText("Erro ao carregar usuário: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        deleteButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                userService.delete(id);
                
                idField.clear();
                userInfoLabel.setText("");
                deleteButton.setDisable(true);
                messageLabel.setText("Usuário excluído com sucesso!");
                messageLabel.setStyle("-fx-text-fill: green;");
                
            } catch (Exception ex) {
                messageLabel.setText("Erro ao excluir usuário: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        form.getChildren().addAll(
            new Label("ID do Usuário:"), idField, loadButton,
            new Label("Informações do Usuário:"), userInfoLabel,
            deleteButton, messageLabel
        );
        
        contentArea.getChildren().addAll(formTitle, form);
    }
}
