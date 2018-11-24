/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.backup;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author arthu
 */
public class Teste
{
    public static void main(String[] args)
    {
        String path = System.getProperty("java.io.tmpdir") + "backup.dump";
        
        String user = "postgres";
        String password = "postgres";
        String url = "postgresql://" + user + ":" + password + "@localhost:5432/checkdesk";
        
        try
        {
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "pg_dump", url, ">", "C:\\Users\\arthu\\Desktop\\test3.dump");
//            builder.environment().put("PGPASSWORD", password);
            Process p = builder.start();
            p.waitFor();
            
            File f = new File("C:\\Users\\arthu\\Desktop" + File.separator
                                + "backup_" + System.currentTimeMillis() + ".zip");
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
            ZipEntry e = new ZipEntry("test3.dump");
            out.putNextEntry(e);
            
            byte[] data = Files.readAllBytes(new File("C:\\Users\\arthu\\Desktop\\test3.dump").toPath());
            out.write(data, 0, data.length);
            out.closeEntry();

            out.close();
        }
        
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
