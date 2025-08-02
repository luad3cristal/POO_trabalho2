package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.DatabaseService;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.model.Book;
import br.edu.ifba.inf008.plugins.books.service.BookServiceImpl;

import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class BookPlugin implements IPlugin {
    private DatabaseService<Book> bookService = new BookServiceImpl();

    @Override
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Livros", "Gerenciar Livros");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                uiController.createTab("Livros", createBookView());
            }
        });

        return true;
    }

    private VBox createBookView() {
        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #F0FFF0;"); // Honeydew background
        
        // Title
        Label title = new Label("Gerenciamento de Livros");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Buttons
        HBox buttonBox = new HBox(10);
        Button listButton = new Button("Listar Livros");
        Button addButton = new Button("Cadastrar Livro");
        
        listButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        addButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        
        buttonBox.getChildren().addAll(listButton, addButton);
        
        // Content area
        VBox contentArea = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        
        // Button actions
        listButton.setOnAction(e -> showBookList(contentArea));
        addButton.setOnAction(e -> showBookForm(contentArea));
        
        mainContainer.getChildren().addAll(title, buttonBox, scrollPane);
        return mainContainer;
    }
    
    private void showBookList(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label listTitle = new Label("Lista de Livros");
        listTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
        
        VBox bookList = new VBox(5);
        
        try {
            java.util.List<Book> books = bookService.readAll();
            for (Book book : books) {
                String bookInfo = String.format("%d. %s - %s - %s - %d - %d", 
                    book.getId(), 
                    book.getTitle(), 
                    book.getAuthor(), 
                    book.getIsbn(), 
                    book.getPublishedYear(),
                    book.getCopiesAvailable());
                
                Label bookLabel = new Label(bookInfo);
                bookLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1px;");
                bookList.getChildren().add(bookLabel);
            }
        } catch (Exception e) {
            Label errorLabel = new Label("Erro ao carregar livros: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            bookList.getChildren().add(errorLabel);
        }
        
        contentArea.getChildren().addAll(listTitle, bookList);
    }
    
    private void showBookForm(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label formTitle = new Label("Cadastrar Novo Livro");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
        
        VBox form = new VBox(10);
        form.setMaxWidth(400);
        
        TextField titleField = new TextField();
        titleField.setPromptText("Título");
        
        TextField authorField = new TextField();
        authorField.setPromptText("Autor");
        
        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");
        
        TextField yearField = new TextField();
        yearField.setPromptText("Ano de Publicação");
        
        TextField copiesField = new TextField();
        copiesField.setPromptText("Cópias Disponíveis");
        
        Button saveButton = new Button("Salvar");
        saveButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        Label messageLabel = new Label();
        
        saveButton.setOnAction(e -> {
            try {
                if (titleField.getText().trim().isEmpty() || authorField.getText().trim().isEmpty() || 
                    isbnField.getText().trim().isEmpty() || yearField.getText().trim().isEmpty() || 
                    copiesField.getText().trim().isEmpty()) {
                    messageLabel.setText("Por favor, preencha todos os campos.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                Book newBook = new Book();
                newBook.setTitle(titleField.getText().trim());
                newBook.setAuthor(authorField.getText().trim());
                newBook.setIsbn(isbnField.getText().trim());
                newBook.setPublishedYear(Integer.parseInt(yearField.getText().trim()));
                newBook.setCopiesAvailable(Integer.parseInt(copiesField.getText().trim()));
                
                bookService.create(newBook);
                
                titleField.clear();
                authorField.clear();
                isbnField.clear();
                yearField.clear();
                copiesField.clear();
                messageLabel.setText("Livro cadastrado com sucesso!");
                messageLabel.setStyle("-fx-text-fill: green;");
                
            } catch (NumberFormatException ex) {
                messageLabel.setText("Erro: Ano e Cópias devem ser números válidos.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                messageLabel.setText("Erro ao cadastrar livro: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        form.getChildren().addAll(
            new Label("Título:"), titleField,
            new Label("Autor:"), authorField,
            new Label("ISBN:"), isbnField,
            new Label("Ano de Publicação:"), yearField,
            new Label("Cópias Disponíveis:"), copiesField,
            saveButton, messageLabel
        );
        
        contentArea.getChildren().addAll(formTitle, form);
    }
}
