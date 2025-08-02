package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.DatabaseService;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.model.Book;
import br.edu.ifba.inf008.plugins.service.BookServiceImpl;

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
        Button editButton = new Button("Editar Livro");
        Button deleteButton = new Button("Excluir Livro");
        
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
        listButton.setOnAction(e -> showBookList(contentArea));
        addButton.setOnAction(e -> showBookForm(contentArea));
        editButton.setOnAction(e -> showEditBookForm(contentArea));
        deleteButton.setOnAction(e -> showDeleteBookForm(contentArea));
        
        mainContainer.getChildren().addAll(title, buttonBox, scrollPane);
        return mainContainer;
    }
    
    private void showBookList(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label listTitle = new Label("Lista de Livros");
        listTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 0;");
        
        VBox bookList = new VBox(2);
        
        try {
            java.util.List<Book> books = bookService.readAll();
            for (Book book : books) {
                String copiesInfo = book.getCopiesAvailable() > 0 ? 
                    "Cópias disponíveis: " + book.getCopiesAvailable() : 
                    "Nenhuma cópia disponível";
                
                String bookInfo = String.format("%d. %s - %s\nISBN: %s - Ano de Publicação: %d - %s", 
                    book.getId(), 
                    book.getTitle(), 
                    book.getAuthor(), 
                    book.getIsbn(), 
                    book.getPublishedYear(),
                    copiesInfo);
                
                Label bookLabel = new Label(bookInfo);
                bookLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
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
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 0;");
        
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
    
    private void showEditBookForm(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label formTitle = new Label("Editar Livro");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 0;");
        
        VBox form = new VBox(10);
        form.setMaxWidth(400);
        
        TextField idField = new TextField();
        idField.setPromptText("ID do Livro");
        
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
        
        Button loadButton = new Button("Carregar");
        loadButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 15px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        
        Button saveButton = new Button("Salvar Alterações");
        saveButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        Label messageLabel = new Label();
        
        loadButton.setOnAction(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    messageLabel.setText("Por favor, informe o ID do livro.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                int id = Integer.parseInt(idField.getText().trim());
                Book book = bookService.read(id);
                
                if (book != null) {
                    titleField.setText(book.getTitle());
                    authorField.setText(book.getAuthor());
                    isbnField.setText(book.getIsbn());
                    yearField.setText(String.valueOf(book.getPublishedYear()));
                    copiesField.setText(String.valueOf(book.getCopiesAvailable()));
                    messageLabel.setText("Livro carregado com sucesso!");
                    messageLabel.setStyle("-fx-text-fill: green;");
                } else {
                    messageLabel.setText("Livro não encontrado.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                }
                
            } catch (NumberFormatException ex) {
                messageLabel.setText("ID deve ser um número válido.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                messageLabel.setText("Erro ao carregar livro: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        saveButton.setOnAction(e -> {
            try {
                if (idField.getText().trim().isEmpty() || titleField.getText().trim().isEmpty() || 
                    authorField.getText().trim().isEmpty() || isbnField.getText().trim().isEmpty() || 
                    yearField.getText().trim().isEmpty() || copiesField.getText().trim().isEmpty()) {
                    messageLabel.setText("Por favor, preencha todos os campos.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                Book book = new Book();
                book.setId(Integer.parseInt(idField.getText().trim()));
                book.setTitle(titleField.getText().trim());
                book.setAuthor(authorField.getText().trim());
                book.setIsbn(isbnField.getText().trim());
                book.setPublishedYear(Integer.parseInt(yearField.getText().trim()));
                book.setCopiesAvailable(Integer.parseInt(copiesField.getText().trim()));
                
                bookService.update(book);
                
                messageLabel.setText("Livro atualizado com sucesso!");
                messageLabel.setStyle("-fx-text-fill: green;");
                
            } catch (NumberFormatException ex) {
                messageLabel.setText("Erro: IDs, Ano e Cópias devem ser números válidos.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                messageLabel.setText("Erro ao atualizar livro: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        form.getChildren().addAll(
            new Label("ID do Livro:"), idField, loadButton,
            new Label("Título:"), titleField,
            new Label("Autor:"), authorField,
            new Label("ISBN:"), isbnField,
            new Label("Ano de Publicação:"), yearField,
            new Label("Cópias Disponíveis:"), copiesField,
            saveButton, messageLabel
        );
        
        contentArea.getChildren().addAll(formTitle, form);
    }
    
    private void showDeleteBookForm(VBox contentArea) {
        contentArea.getChildren().clear();
        
        Label formTitle = new Label("Excluir Livro");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 0;");
        
        VBox form = new VBox(10);
        form.setMaxWidth(400);
        
        TextField idField = new TextField();
        idField.setPromptText("ID do Livro");
        
        Label bookInfoLabel = new Label();
        bookInfoLabel.setStyle("-fx-font-size: 14px; -fx-wrap-text: true;");
        bookInfoLabel.setMaxWidth(Double.MAX_VALUE);
        
        Button loadButton = new Button("Carregar");
        loadButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 15px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        
        Button deleteButton = new Button("EXCLUIR");
        deleteButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-color: #F44336; -fx-text-fill: white;");
        deleteButton.setDisable(true);
        
        Label messageLabel = new Label();
        
        loadButton.setOnAction(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    messageLabel.setText("Por favor, informe o ID do livro.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                int id = Integer.parseInt(idField.getText().trim());
                Book book = bookService.read(id);
                
                if (book != null) {
                    bookInfoLabel.setText(String.format("ID: %d - %s - %s\nISBN: %s - Ano: %d", 
                        book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublishedYear()));
                    deleteButton.setDisable(false);
                    messageLabel.setText("Livro carregado. Confirme a exclusão.");
                    messageLabel.setStyle("-fx-text-fill: orange;");
                } else {
                    bookInfoLabel.setText("Livro não encontrado.");
                    deleteButton.setDisable(true);
                    messageLabel.setText("Livro não encontrado.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                }
                
            } catch (NumberFormatException ex) {
                messageLabel.setText("ID deve ser um número válido.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                messageLabel.setText("Erro ao carregar livro: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        deleteButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                bookService.delete(id);
                
                idField.clear();
                bookInfoLabel.setText("");
                deleteButton.setDisable(true);
                messageLabel.setText("Livro excluído com sucesso!");
                messageLabel.setStyle("-fx-text-fill: green;");
                
            } catch (Exception ex) {
                messageLabel.setText("Erro ao excluir livro: " + ex.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });
        
        form.getChildren().addAll(
            new Label("ID do Livro:"), idField, loadButton,
            new Label("Informações do Livro:"), bookInfoLabel,
            deleteButton, messageLabel
        );
        
        contentArea.getChildren().addAll(formTitle, form);
    }
}
