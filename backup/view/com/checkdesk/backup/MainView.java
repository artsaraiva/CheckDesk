/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.backup;

import com.checkdesk.control.BackupController;
import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author arthu
 */
public class MainView
        extends Application
{
    private Stage stage;
    private File restoreFile = null;
    private BackupController controller;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;

        initComponents();

        stage.setTitle("Backup/Restaurar");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        resize();

        controller = new BackupController(stage);
    }

    private void resize()
    {
        hbox.setPrefSize(pane.getWidth(), pane.getHeight());
        vboxBackup.setPrefSize(pane.getWidth() / 2, pane.getHeight());
        vboxRestore.setPrefSize(pane.getWidth() / 2, pane.getHeight());
    }

    private void selectRestoreFile()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(BackupController.ZIP_FILTER);

        restoreFile = fileChooser.showOpenDialog(stage);
        restoreField.setText(restoreFile != null ? restoreFile.getAbsolutePath() : null);
    }

    private void doBackup()
    {
        int option = 0;

        if (dataBaseCheck.isSelected())
        {
            option &= BackupController.OPTION_DATABASE;
        }

        if (applicationCheck.isSelected())
        {
            option &= BackupController.OPTION_APPLICATION;
        }

        controller.backup(option);
    }

    private void doRestore()
    {
        controller.restore(restoreFile);
    }

    private void initComponents()
    {
        pane.setPrefSize(400, 300);
        pane.setStyle("-fx-background-color: #eeeeee");

        hbox.autosize();
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10));

        backupButton.setDisable(true);
        restoreButton.setDisable(true);

        restoreField.setEditable(false);
        restoreField.setPromptText("Clique para selecionar um arquivo");

        restoreField.setCursor(Cursor.HAND);
        restoreButton.setCursor(Cursor.HAND);
        backupButton.setCursor(Cursor.HAND);

        vboxBackup.setSpacing(15);
        vboxRestore.setSpacing(15);

        vboxBackup.setAlignment(Pos.CENTER);
        vboxRestore.setAlignment(Pos.CENTER);

        vboxBackup.getChildren().addAll(dataBaseCheck, applicationCheck, backupButton);
        vboxRestore.getChildren().addAll(restoreField, restoreButton);

        hbox.getChildren().addAll(vboxBackup, new Separator(Orientation.VERTICAL), vboxRestore);
        pane.getChildren().add(hbox);

        restoreField.setOnMouseClicked((event) ->
        {
            selectRestoreFile();
            restoreButton.setDisable(restoreFile == null);
        });

        applicationCheck.setOnAction((event) ->
        {
            backupButton.setDisable(!applicationCheck.isSelected() && !dataBaseCheck.isSelected());
        });

        dataBaseCheck.setOnAction((event) ->
        {
            backupButton.setDisable(!applicationCheck.isSelected() && !dataBaseCheck.isSelected());
        });

        backupButton.setOnAction((event) ->
        {
            doBackup();
        });

        restoreButton.setOnAction((event) ->
        {
            doRestore();
        });
    }

    private final AnchorPane pane = new AnchorPane();
    private final Scene scene = new Scene(pane);

    private final TextField restoreField = new TextField();

    private final CheckBox applicationCheck = new CheckBox("Aplicação");
    private final CheckBox dataBaseCheck = new CheckBox("Banco de dados");

    private final Button backupButton = new Button("Backup");
    private final Button restoreButton = new Button("Restaurar");

    private final HBox hbox = new HBox();
    private final VBox vboxBackup = new VBox();
    private final VBox vboxRestore = new VBox();
}
