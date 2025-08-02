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
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class BookPlugin implements IPlugin {
    private DatabaseService<Book> bookService = new BookServiceImpl();

    @Override
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Livros", "Listar Livros");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                bookService.readAll().forEach(b -> System.out.println(b.getTitle()));
            }
        });

        uiController.createTab("Livros", new Rectangle(200, 200, Color.LIGHTGREEN));

        return true;
    }
}
