/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.backup;

import com.checkdesk.control.ConfigurationManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author arthu
 */
public class MainView
        extends Application
{

    private static FileChooser.ExtensionFilter ZIP_FILTER = new FileChooser.ExtensionFilter("Zip", "*.zip");

    private Stage stage;
    private File restoreFile = null;

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
        fileChooser.getExtensionFilters().add(ZIP_FILTER);

        restoreFile = fileChooser.showOpenDialog(stage);
        restoreField.setText(restoreFile != null ? restoreFile.getAbsolutePath() : null);
    }

    private void doBackup()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(ZIP_FILTER);
        fileChooser.setInitialFileName("backup_" + System.currentTimeMillis());

        File zip = fileChooser.showSaveDialog(stage);

        if (zip != null)
        {
            try
            {
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));

                if (dataBaseCheck.isSelected())
                {
                    putZipEntry("", getDatabaseBackup(), out);
                }

                if (applicationCheck.isSelected())
                {
                    File directory = null;

                    while (directory == null || !directory.isDirectory())
                    {
                        directory = getRootDirectory();
                    }

                    for (File f : directory.listFiles())
                    {
                        putZipEntry("", f, out);
                    }
                }

                out.close();
            }

            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private File getDatabaseBackup()
    {
        File backup = null;
        try
        {
            Properties properties = ConfigurationManager.loadProperties(null);

            while (properties == null)
            {
                Alert dialogInfo = new Alert(Alert.AlertType.INFORMATION);
                dialogInfo.getDialogPane().setPrefSize(400, 200);
                dialogInfo.setTitle("Arquivo de configuração");
                dialogInfo.setHeaderText("Arquivo de configuração não encontrado");
                dialogInfo.setContentText("Por favor, selecione o arquivo de configuração.");
                dialogInfo.showAndWait();

                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo de configuração", "*.properties"));

                properties = ConfigurationManager.loadProperties(chooser.showOpenDialog(stage));
            }

            String name = properties.getProperty("db.name");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            backup = new File(System.getProperty("java.io.tmpdir") + File.separator + "backup.dump");

            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "pg_dump", "-F", "custom", "-U", user, "-d", name);
            builder.redirectOutput(backup);
            builder.environment().put("PGPASSWORD", password);

            Process p = builder.start();
            p.waitFor();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return backup;
    }

    private File getRootDirectory()
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));

        return directoryChooser.showDialog(stage);
    }

    private void putZipEntry(String parent, File file, ZipOutputStream out)
    {
        if (!parent.isEmpty())
        {
            parent = parent + File.separator;
        }

        if (file.isFile())
        {
            try
            {
                System.out.println("file zip : " + file.getAbsoluteFile());

                ZipEntry e = new ZipEntry(parent + file.getName());
                out.putNextEntry(e);

                byte[] data = Files.readAllBytes(file.toPath());
                out.write(data, 0, data.length);
                out.closeEntry();
            }

            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        else
        {
            if (file.isDirectory())
            {
                for (File f : file.listFiles())
                {
                    putZipEntry(parent + file.getName(), f, out);
                }
            }
        }
    }

    private void doRestore()
    {
        byte[] buffer = new byte[1024];

        try
        {
            File folder = null;

            while (folder == null || !folder.isDirectory())
            {
                folder = getRootDirectory();
            }

            ZipInputStream zis = new ZipInputStream(new FileInputStream(restoreFile));
            ZipEntry ze = zis.getNextEntry();

            while (ze != null)
            {

                String fileName = ze.getName();
                File newFile = new File(folder.getAbsoluteFile() + File.separator + fileName);

                System.out.println("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0)
                {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            System.out.println("Done");

            File[] backup = folder.listFiles(new FilenameFilter()
            {
                @Override
                public boolean accept(File dir, String name)
                {
                    return name.equals("backup.dump");
                }
            });

            if (backup.length == 1)
            {
                System.out.println("Backup start");

                try
                {
                    Properties properties = ConfigurationManager.loadProperties(null);

                    while (properties == null)
                    {
                        Alert dialogInfo = new Alert(Alert.AlertType.INFORMATION);
                        dialogInfo.getDialogPane().setPrefSize(400, 200);
                        dialogInfo.setTitle("Arquivo de configuração");
                        dialogInfo.setHeaderText("Arquivo de configuração não encontrado");
                        dialogInfo.setContentText("Por favor, selecione o arquivo de configuração.");
                        dialogInfo.showAndWait();

                        FileChooser chooser = new FileChooser();
                        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo de configuração", "*.properties"));

                        properties = ConfigurationManager.loadProperties(chooser.showOpenDialog(stage));
                    }

                    String name = properties.getProperty("db.name");
                    String user = properties.getProperty("db.user");
                    String password = properties.getProperty("db.password");

                    ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "pg_restore", "-c", "-F", "custom", "-U", user, "-d", name);

                    builder.redirectInput(backup[0]);
                    builder.environment().put("PGPASSWORD", password);
                    Process p = builder.start();
                    p.waitFor();

                    System.out.println("Backup done");
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

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

    private AnchorPane pane = new AnchorPane();
    private Scene scene = new Scene(pane);

    private TextField restoreField = new TextField();

    private CheckBox applicationCheck = new CheckBox("Aplicação");
    private CheckBox dataBaseCheck = new CheckBox("Banco de dados");

    private Button backupButton = new Button("Backup");
    private Button restoreButton = new Button("Restaurar");

    private HBox hbox = new HBox();
    private VBox vboxBackup = new VBox();
    private VBox vboxRestore = new VBox();
}
