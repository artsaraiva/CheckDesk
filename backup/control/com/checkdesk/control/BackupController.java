/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author MNicaretta
 */
public class BackupController
{
    public static FileChooser.ExtensionFilter ZIP_FILTER = new FileChooser.ExtensionFilter("Zip", "*.zip");

    public static final int OPTION_DATABASE = 1;
    public static final int OPTION_APPLICATION = 2;

    private final Window window;
    private Properties databaseProperties;

    public BackupController(Window window)
    {
        this.window = window;
        loadProperties();
    }

    private void loadProperties()
    {
        while (databaseProperties == null)
        {
            Alert dialogInfo = new Alert(Alert.AlertType.INFORMATION);
            dialogInfo.getDialogPane().setPrefSize(400, 200);
            dialogInfo.setTitle("Arquivo de configuração");
            dialogInfo.setHeaderText("Arquivo de configuração não encontrado");
            dialogInfo.setContentText("Por favor, selecione o arquivo de configuração.");
            dialogInfo.showAndWait();

            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo de configuração", "*.properties"));

            databaseProperties = ConfigurationManager.loadProperties(chooser.showOpenDialog(window));
        }
    }

    public void backup(int option)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(ZIP_FILTER);
        fileChooser.setInitialFileName("backup_" + System.currentTimeMillis());

        File zip = fileChooser.showSaveDialog(window);

        if (zip != null)
        {
            try
            {
                try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip)))
                {
                    if ((option | OPTION_DATABASE) != 0)
                    {
                        putZipEntry("", getDatabaseBackup(), out);
                    }

                    if ((option | OPTION_APPLICATION) != 0)
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
                }
            }

            catch (IOException e)
            {
                e.printStackTrace(System.out);
            }
        }
    }

    private File getDatabaseBackup()
    {
        File backup = null;

        try
        {
            String name = databaseProperties.getProperty("db.name");
            String user = databaseProperties.getProperty("db.user");
            String password = databaseProperties.getProperty("db.password");

            backup = new File(System.getProperty("java.io.tmpdir") + File.separator + "backup.dump");

            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "pg_dump", "-F", "custom", "-U", user, "-d", name);
            builder.redirectOutput(backup);
            builder.environment().put("PGPASSWORD", password);

            Process p = builder.start();
            p.waitFor();
        }

        catch (IOException | InterruptedException e)
        {
            e.printStackTrace(System.out);
        }

        return backup;
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
                System.out.println("file zip: " + file.getAbsoluteFile());

                ZipEntry e = new ZipEntry(parent + file.getName());
                out.putNextEntry(e);

                byte[] data = Files.readAllBytes(file.toPath());
                out.write(data, 0, data.length);
                out.closeEntry();
            }

            catch (IOException e)
            {
                e.printStackTrace(System.out);
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

    private File getRootDirectory()
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));

        return directoryChooser.showDialog(window);
    }

    public void restore(File restoreFile)
    {
        byte[] buffer = new byte[1024];

        try
        {
            File folder = null;

            while (folder == null || !folder.isDirectory())
            {
                folder = getRootDirectory();
            }

            System.out.println("Unzip start");

            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(restoreFile)))
            {
                ZipEntry ze = zis.getNextEntry();

                while (ze != null)
                {

                    String fileName = ze.getName();
                    File newFile = new File(folder.getAbsoluteFile() + File.separator + fileName);

                    System.out.println("file unzip : " + newFile.getAbsoluteFile());

                    new File(newFile.getParent()).mkdirs();

                    try (FileOutputStream fos = new FileOutputStream(newFile))
                    {
                        int len;

                        while ((len = zis.read(buffer)) > 0)
                        {
                            fos.write(buffer, 0, len);
                        }
                    }

                    ze = zis.getNextEntry();
                }

                zis.closeEntry();
            }

            System.out.println("Unzip done");

            File[] backup = folder.listFiles((File dir, String name) -> name.equals("backup.dump"));

            if (backup.length == 1)
            {
                System.out.println("Backup start");

                try
                {
                    String name = databaseProperties.getProperty("db.name");
                    String user = databaseProperties.getProperty("db.user");
                    String password = databaseProperties.getProperty("db.password");

                    ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "pg_restore", "-c", "-F", "custom", "-U", user, "-d", name);

                    builder.redirectInput(backup[0]);
                    builder.environment().put("PGPASSWORD", password);
                    Process p = builder.start();
                    p.waitFor();

                    System.out.println("Backup done");
                }

                catch (IOException | InterruptedException e)
                {
                    e.printStackTrace(System.out);
                }
            }
        }

        catch (IOException e)
        {
            e.printStackTrace(System.out);
        }
    }
}
