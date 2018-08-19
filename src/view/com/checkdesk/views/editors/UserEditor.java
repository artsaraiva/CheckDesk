/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.editors;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.util.Item;
import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.User;
import com.checkdesk.views.util.EditorCallback;
import static com.sun.corba.se.impl.util.Utility.printStackTrace;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author MNicaretta
 */
public class UserEditor
        extends DefaultEditor<User>
{

    public UserEditor(EditorCallback<User> callback)
    {
        super(callback);

        initComponents();

        setTitle("Editor de Usuários");
        setHeaderText("Editor de Usuários");

        setSource(callback.getSource());
    }

    private void setSource(User value)
    {
        this.source = value;

        nameField.setText(value.getName());
        loginField.setText(value.getLogin());
        emailField.setText(value.getEmail());
        phoneField.setText(value.getPhone());
        passwordField.setText(value.getPassword());
        typeField.setValue(UserUtilities.getType(value.getType()));
    }

    @Override
    protected void obtainInput()
    {
        source.setName(nameField.getText());
        source.setLogin(loginField.getText());
        source.setEmail(emailField.getText());
        source.setPhone(phoneField.getText());
        source.setPassword(ApplicationController.hash(passwordField.getText()));
        source.setType(typeField.getValue().getValue());
    }

    private void initComponents()
    {
        setWidth(500);
        setHeight(400);

        gridPane.setVgap(10);
        gridPane.setHgap(10);

        int count = 0;

        HBox.setHgrow(typeField, Priority.ALWAYS);
        loginField.setMinWidth(500);
        typeField.setItems(UserUtilities.getItems());

        gridPane.addRow(count++, nameLabel, nameField);
        gridPane.addRow(count++, loginLabel, loginField);
        gridPane.addRow(count++, emailLabel, emailField);
        gridPane.addRow(count++, phoneLabel, phoneField);
        gridPane.addRow(count++, passwordLabel, passwordField);
        gridPane.addRow(count++, typeLabel, typeField);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(col1, col2);

        getDialogPane().setContent(gridPane);
    }

    private GridPane gridPane = new GridPane();

    private Label nameLabel = new Label("Nome");
    private TextField nameField = new TextField();

    private Label loginLabel = new Label("Login");
    private TextField loginField = new TextField();

    private Label emailLabel = new Label("E-mail");
    private TextField emailField = new TextField();

    private Label phoneLabel = new Label("Fone");
    private TextField phoneField = new TextField();

    private Label passwordLabel = new Label("Senha");
    private PasswordField passwordField = new PasswordField();

    private Label typeLabel = new Label("Tipo:");
    private ComboBox<Item> typeField = new ComboBox();
}
