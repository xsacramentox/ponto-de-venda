package com.github.fgabrielbraga.controller;

import com.github.fgabrielbraga.controller.util.AlertBox;
import com.github.fgabrielbraga.controller.util.Helper;
import com.github.fgabrielbraga.controller.util.Validator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.github.fgabrielbraga.model.User;
import com.github.fgabrielbraga.model.dao.UserDAO;
import com.github.fgabrielbraga.view.MenuPrincipal;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField fieldLoginUser;

    @FXML
    private PasswordField fieldLoginPassword;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnCancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fieldLoginUser.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                fieldLoginPassword.requestFocus();
        });

        fieldLoginPassword.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                enter();
        });

        btnSubmit.setOnMouseClicked(click -> {
            enter();
        });

        btnCancel.setOnMouseClicked(click -> {
            closeWindow();
        });

        Helper.addTextLimiter(fieldLoginUser, 40);
        Helper.addTextLimiter(fieldLoginPassword, 20);
    }

    private void enter() {
        String username = fieldLoginUser.getText();
        String password = fieldLoginPassword.getText();
        if (Validator.validateFields(username, password)) {
            UserDAO dao = new UserDAO();
            createAdminUserIfNecessary(dao);
            List<User> users = dao.selectUserByNameAndPassword(username, password);
            if (!users.isEmpty() || users == null) {
                User user = users.get(0);
                Access.checkFullAccess(user.getPermission());
                Access.setOperator(user);
                closeWindow();
                (new MenuPrincipal()).start(new Stage());
            } else {
                AlertBox.incorrectUserOrPassword();
            }
        } else {
            AlertBox.fillAllFields();
        }
    }

    private void createAdminUserIfNecessary(UserDAO dao) {
        
        User user = new User("admin", "admin", Access.ADMINISTRATIVE_ACCESS);
        if(dao.selectUserByName(user.getName()).isEmpty()) {
            dao.insert(user);
        }
    }
    
    private void closeWindow() {
        ((Stage) rootPane.getScene().getWindow()).close();
    }
}
