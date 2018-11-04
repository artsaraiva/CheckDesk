/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.control.ResourceLocator;
import com.checkdesk.control.ServerConnection;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.views.parts.Prompts;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author arthu
 */
public class LoginView
        extends Application
{
    private Stage stage;

    public static void main(String[] args)
    {
        try
        {
            //Inicialization
            ApplicationController.getInstance();
            launch(args);
        }
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;

        initComponents();

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        resize();
    }

    private void resize()
    {
        vbox.setLayoutY((pane.getHeight() - vbox.getHeight()) / 2);
        vbox.setLayoutX((pane.getWidth() - vbox.getWidth()) / 2);
    }

    private void validateLogin()
    {
        boolean keepTrying = true;
        
        while (keepTrying)
        {
            keepTrying = ServerConnection.getInstance() == null;
            
            if (keepTrying)
            {
                String settings = ServerConnection.getConnectionSettings();
                
                String noConnection = "A conexão ao servidor não está configurada.";
                String connectionRefused = "Não foi possível connectar ao servidor: " + settings;
                
                keepTrying = Prompts.confirm("Erro de conexão", (settings != null ? connectionRefused : noConnection) + "\n" +
                                             "Deseja mudar as configurações de conexão?" );
                
                if (keepTrying)
                {
                    Prompts.editConnectionSettings();
                }
                
                else
                {
                    keepTrying = Prompts.confirm("Erro de conexão", "Tentar novamente?");
                }
            }
        }
        
        if (ApplicationController.getInstance().login(loginField.getText(), passwordField.getText()) != null)
        {
            try
            {
                new MainView().start(new Stage());
                stage.close();
            }
            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }

        else
        {
            Prompts.info("Login Inválido", "Usuário/E-mail e senha não estão corretos. \nTente outra vez !");

            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    private void initComponents()
    {
        loginField.setPromptText("Usuário ou E-mail");
        passwordField.setPromptText("Senha");

        pane.setPrefSize(400, 300);
        icon.setFitHeight(200);
        icon.setFitWidth(200);
        loginField.setPrefWidth(200);
        passwordField.setPrefWidth(200);
        loginButton.setPrefWidth(200);

        icon.setImage(new Image(ResourceLocator.getInstance().getImageResource("login.png")));

        vbox.autosize();
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(icon, loginField, passwordField, loginButton);

        pane.getChildren().add(vbox);
        pane.setStyle("-fx-background-color: #eeeeee");

        loginButton.setCursor(Cursor.HAND);

        loginButton.setOnAction((ActionEvent event) ->
        {
            validateLogin();
        });

        pane.setOnKeyPressed((KeyEvent event) ->
        {
            if (event.getCode().equals(KeyCode.ENTER))
            {
                validateLogin();
            }
            else if (event.getCode().equals(KeyCode.ESCAPE))
            {
                System.exit(0);
            }
        });
    }

    private AnchorPane pane = new AnchorPane();
    private Scene scene = new Scene(pane);

    private ImageView icon = new ImageView();
    private TextField loginField = new TextField();
    private PasswordField passwordField = new PasswordField();

    private Button loginButton = new Button("Entrar");

    private VBox vbox = new VBox();
}
