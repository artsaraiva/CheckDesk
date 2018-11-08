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
import com.checkdesk.views.parts.MaskField;
import com.checkdesk.views.util.EditorCallback;
import com.checkdesk.views.util.Validation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

        setValidations();
    }

    @Override
    protected void obtainInput()
    {
        source.setName(nameField.getText());
        source.setLogin(loginField.getText());
        source.setEmail(emailField.getText());
        source.setPhone(phoneField.getText());

        String password = passwordField.getText();

        if (!password.equals(source.getPassword()))
        {
            password = ApplicationController.hash(password);
        }

        source.setPassword(password);
        source.setType(typeField.getValue().getValue());
    }

    private void setValidations()
    {
        addValidation(nameField);
        addValidation(loginField, new Validation()
        {
            private String error;

            @Override
            public boolean validate()
            {
                boolean result = false;

                String login = loginField.getText();

                if (login == null || login.isEmpty())
                {
                    error = "Esse campo deve ser preenchido";
                }

                else
                {
                    try
                    {
                        result = UserUtilities.isUniqueLogin(source, login.toLowerCase());

                        error = result ? "" : "O login informado já está sendo usado";
                    }

                    catch (Exception e)
                    {
                        ApplicationController.logException(e);
                    }
                }

                return result;
            }

            @Override
            public String getError()
            {
                return error;
            }

        });

        addValidation(emailField, new Validation()
        {
            private String error;

            @Override
            public boolean validate()
            {
                boolean result = false;

                String email = emailField.getText();

                if (email == null || email.isEmpty())
                {
                    error = "Esse campo deve ser preenchido";
                }

                else
                {
                    Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
                    Matcher matcher = pattern.matcher(email);

                    if (!matcher.find() || !matcher.group().equals(email))
                    {
                        error = "O e-mail informado está inválido";
                    }

                    else
                    {
                        try
                        {
                            result = UserUtilities.isUniqueEmail(source, email.toLowerCase());

                            error = result ? "" : "O e-mail informado já está sendo usado";
                        }

                        catch (Exception e)
                        {
                            ApplicationController.logException(e);
                        }
                    }
                }

                return result;
            }

            @Override
            public String getError()
            {
                return error;
            }
        });
        addValidation(passwordField);
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
    private MaskField phoneField = new MaskField(MaskField.MASK_PHONE);

    private Label passwordLabel = new Label("Senha");
    private PasswordField passwordField = new PasswordField();

    private Label typeLabel = new Label("Tipo:");
    private ComboBox<Item> typeField = new ComboBox();
}
