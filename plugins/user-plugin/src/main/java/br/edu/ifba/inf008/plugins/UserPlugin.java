package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.DatabaseService;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.users.service.UserServiceImpl;
import br.edu.ifba.inf008.interfaces.model.User;

import javafx.scene.control.MenuItem;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;


public class UserPlugin implements IPlugin {
    private DatabaseService<User> userService = new UserServiceImpl();

    @Override
    public boolean init() {
        IUIController uiController = ICore.getInstance().getUIController();

        MenuItem menuItem = uiController.createMenuItem("Usuários", "Listar Usuários");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Exemplo: listar usuários no console
                userService.readAll().forEach(u -> System.out.println(u.getName()));
            }
        });

        uiController.createTab("Usuários", new Rectangle(200, 200, Color.LIGHTSTEELBLUE));

        return true;
    }
}
