/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.view;

import com.checkdesk.control.ResourceLocator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

/**
 *
 * @author arthu
 */
public class LoginView
    extends Application
{
    private Stage stage;

    public static void main( String[] args )
    {
        launch( args );
    }

    @Override
    public void start( Stage stage ) throws Exception
    {
        this.stage = stage;

        initComponents();

        stage.setTitle( "Login" );
        stage.setScene( scene );
        stage.setResizable( false );
        stage.show();

        resize();
    }

    private void resize()
    {
        vbox.setLayoutY( ( pane.getHeight() - vbox.getHeight() ) / 2  );
        vbox.setLayoutX( ( pane.getWidth() - vbox.getWidth() ) / 2 );
    }

    private void initComponents()
    {
        emailField.setPromptText( "Usuário ou E-mail" );
        passwordField.setPromptText( "Senha" );

        pane.setPrefSize( 400, 300 );
        icon.setFitHeight( 200 );
        icon.setFitWidth( 200 );
        emailField.setPrefWidth( 200 );
        passwordField.setPrefWidth( 200 );
        loginButton.setPrefWidth( 200 );

        icon.setImage( new Image( ResourceLocator.getInstance().getImageResource( "login.jpg" ) ) );

        vbox.autosize();
        vbox.setSpacing( 5 );
        vbox.setAlignment( Pos.CENTER );
        vbox.getChildren().addAll( icon, emailField, passwordField, loginButton );

        pane.getChildren().add( vbox );
        pane.setStyle( "-fx-background-color: #ffffff" );

        loginButton.setCursor( Cursor.HAND );

        loginButton.setOnAction( new EventHandler<ActionEvent>()
        {
            @Override
            public void handle( ActionEvent event )
            {
                //To Do criar método de validação de login 
            }

        } );

        pane.setOnKeyPressed( new EventHandler<KeyEvent>()
        {

            @Override
            public void handle( KeyEvent event )
            {
                if ( event.getCode().equals( KeyCode.ENTER ) )
                {
                    //TO-DO Validate Login
                }
                else if ( event.getCode().equals( KeyCode.ESCAPE ) )
                {
                    System.exit( 0 );
                }
            }

        } );
    }

    private AnchorPane pane = new AnchorPane();
    private Scene scene = new Scene( pane );

    private ImageView icon = new ImageView();
    private TextField emailField = new TextField();
    private PasswordField passwordField = new PasswordField();

    private Button loginButton = new Button( "Entrar" );

    private VBox vbox = new VBox();
}
