/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.license;

import com.checkdesk.control.ApplicationController;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author marcelo.nicaretta
 */
public class LicenseController
{
    public static final int LICENSE_OK        = -1;
    public static final int LAST_DAYS         =  0;
    public static final int FILE_NOT_FOUND    =  1;
    public static final int FILE_CORRUPTED    =  2;
    public static final int INVALID_START     =  3;
    public static final int LICENSE_EXPIRED   =  4;
    
    private static LicenseController instance;

    public static LicenseController getInstance()
    {
        if (instance == null)
        {
            instance = new LicenseController();
        }
        
        return instance;
    }
    
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private Properties license;
    
    private LicenseController()
    {
    }
    
    public int validateLicense()
    {
        int result = LICENSE_OK;
        
        try
        {
            byte[] content = loadLicense();

            if (content == null)
            {
                result = FILE_CORRUPTED;
            }
            
            else
            {
                license = new Properties();
                license.load(new ByteArrayInputStream(content));

                if (license.isEmpty())
                {
                    result = FILE_NOT_FOUND;
                }
                
                else
                {
                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.HOUR, 0);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.SECOND, 0);
                    today.set(Calendar.MILLISECOND, 0);
                    
                    Date starts = df.parse(license.getProperty("starts"));
                    Date expires = df.parse(license.getProperty("expires"));
                    
                    if (starts.after(today.getTime()))
                    {
                        result = INVALID_START;
                    }
                    
                    else if (expires.before(today.getTime()))
                    {
                        result = LICENSE_EXPIRED;
                    }
                    
                    else
                    {
                        today.add(Calendar.DAY_OF_MONTH, 5);
                        
                        if (expires.before(today.getTime()))
                        {
                            result = LAST_DAYS;
                        }
                    }
                }
            }
        }
        
        catch (Exception e)
        {
            result = FILE_NOT_FOUND;
        }
        
        return result;
    }
    
    private byte[] loadLicense() throws Exception
    {
        byte[] result = null;
        
        File file = new File("config" + File.separator + "license");
        
        List<byte[]> list;
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file)))
        {
            list = (List<byte[]>) in.readObject();
        }
        
        if (list != null && list.size() == 2)
        {
            byte[] content = list.get(0);
            byte[] signature = list.get(1);
            
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");

            KeyFactory kf = KeyFactory.getInstance("DSA", "SUN");
            PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(LicenseGenerator.PUBLIC_KEY));
            
            sig.initVerify(publicKey);
            sig.update(content);
            
            if (sig.verify(signature))
            {
                result = content;
            }
        }
        
        return result;
    }
    
    public int daysToExpire()
    {
        int result = -1;
        
        if (license != null)
        {
            try
            {
                Calendar expires = Calendar.getInstance();
                expires.setTime(df.parse(license.getProperty("expires")));
            }
            
            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
        
        return result;
    }
}
