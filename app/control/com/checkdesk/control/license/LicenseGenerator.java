/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.license;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author marcelo.nicaretta
 */
class LicenseGenerator
{
    public static void main(String[] args)
    {
        try
        {
//            generateKeys();
            signLicense();
        }
        
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
    }
    
    private static void signLicense() throws Exception
    {
        Scanner scanner = new Scanner(System.in);
        
        File file = null;
        
        while (file == null)
        {
            System.out.println("Caminho do arquivo: ");
            file = new File(scanner.nextLine());
        }
        
        Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
        
        KeyFactory kf = KeyFactory.getInstance("DSA", "SUN");
        PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(PRIVATE_KEY));
        
        sig.initSign(privateKey);
        
        byte[] content = Files.readAllBytes(file.toPath());
        
        sig.update(content);
        
        byte[] realSig = sig.sign();
        
        try (ObjectOutputStream signed = new ObjectOutputStream(new FileOutputStream(file)))
        {
            signed.writeObject(Arrays.asList(content, realSig));
        }
    }
    
    private static void generateKeys() throws Exception
    {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);

        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey priv = pair.getPrivate();
        PublicKey pub = pair.getPublic();

        System.out.println("Privada: " + Arrays.toString(priv.getEncoded()));
        System.out.println("Pública: " + Arrays.toString(pub.getEncoded()));
    }
    
    static final byte[] PUBLIC_KEY = new byte[]
    {
          48, -126,    1,  -73,   48, -126,    1,   44,    6,    7,
          42, -122,   72,  -50,   56,    4,    1,   48, -126,    1,
          31,    2, -127, -127,    0,   -3,  127,   83, -127,   29,
         117,   18,   41,   82,  -33,   74, -100,   46,  -20,  -28,
         -25,  -10,   17,  -73,   82,   60,  -17,   68,    0,  -61,
          30,   63, -128,  -74,   81,   38,  105,   69,   93,   64,
          34,   81,   -5,   89,   61, -115,   88,   -6,  -65,  -59,
         -11,  -70,   48,  -10,  -53, -101,   85,  108,  -41, -127,
          59, -128,   29,   52,  111,  -14,  102,   96,  -73,  107,
        -103,   80,  -91,  -92,  -97,  -97,  -24,    4,  123,   16,
          34,  -62,   79,  -69,  -87,  -41,   -2,  -73,  -58,   27,
          -8,   59,   87,  -25,  -58,  -88,  -90,   21,   15,    4,
          -5, -125,  -10,  -45,  -59,   30,  -61,    2,   53,   84,
          19,   90,   22, -111,   50,  -10,  117,  -13,  -82,   43,
          97,  -41,   42,  -17,  -14,   34,    3,   25,  -99,  -47,
          72,    1,  -57,    2,   21,    0, -105,   96,   80, -113,
          21,   35,   11,  -52,  -78, -110,  -71, -126,  -94,  -21,
        -124,   11,  -16,   88,   28,  -11,    2, -127, -127,    0,
          -9,  -31,  -96, -123,  -42, -101,   61,  -34,  -53,  -68,
         -85,   92,   54,  -72,   87,  -71,  121, -108,  -81,  -69,
          -6,   58,  -22, -126,   -7,   87,   76,   11,   61,    7,
        -126,  103,   81,   89,   87, -114,  -70,  -44,   89,   79,
         -26,  113,    7,   16, -127, -128,  -76,   73,   22,  113,
          35,  -24,   76,   40,   22,   19,  -73,  -49,    9,   50,
        -116,  -56,  -90,  -31,   60,   22,  122, -117,   84,  124,
        -115,   40,  -32,  -93,  -82,   30,   43,  -77,  -90,  117,
        -111,  110,  -93,  127,   11,   -6,   33,   53,   98,  -15,
          -5,   98,  122,    1,   36,   59,  -52,  -92,  -15,  -66,
         -88,   81, -112, -119,  -88, -125,  -33,  -31,   90,  -27,
         -97,    6, -110, -117,  102,   94, -128,  123,   85,   37,
         100,    1,   76,   59,   -2,  -49,   73,   42,    3, -127,
        -124,    0,    2, -127, -128,  122,  104,  -57,    4,  -20,
         -35,   62,   24,   50,  101,  -44,  102,  -46,   20,  -63,
         -49,  115,  -25,   82,  -84,   13,  124,  117,  -36,  -41,
         -53,  -72,  -97,  -11,   82,  -61,   64,  -99,   92,   63,
         -13,  -71,   -1, -117, -110,  112,  -64,   57,    1,   68,
          12,   16,   50, -115,  -25,  -91,   39,   80,  121,  -75,
         -49,   76,  -20,  110,  -49,  -77,  -40,  120,    8,   -1,
          31,   60,   45,  -52,   -4, -116,  -28,  -33,   23,   19,
          37,  -79,   52,   94,  -72,   80,   29,   14,  111,   72,
          78,   64,  -37,  -40,   60,  -30,   96,   13,  116,  -62,
          84,  102,  -69, -109,   60, -116, -104,   44,  -56,   48,
         117,  -99,  106,   -2,   50,  -74,  -26, -125,  105,   62,
         -80,  -51,   35, -123,   85, -118,   93,   30,   44,   49,
         -87,   42,   47
    };
    
    static final byte[] PRIVATE_KEY = new byte[]
    {
          48, -126,    1,   75,    2,    1,    0,   48, -126,    1,
          44,    6,    7,   42, -122,   72,  -50,   56,    4,    1,
          48, -126,    1,   31,    2, -127, -127,    0,   -3,  127,
          83, -127,   29,  117,   18,   41,   82,  -33,   74, -100,
          46,  -20,  -28,  -25,  -10,   17,  -73,   82,   60,  -17,
          68,    0,  -61,   30,   63, -128,  -74,   81,   38,  105,
          69,   93,   64,   34,   81,   -5,   89,   61, -115,   88,
          -6,  -65,  -59,  -11,  -70,   48,  -10,  -53, -101,   85,
         108,  -41, -127,   59, -128,   29,   52,  111,  -14,  102,
         96,   -73,  107, -103,   80,  -91,  -92,  -97,  -97,  -24,
           4,  123,   16,   34,  -62,   79,  -69,  -87,  -41,   -2,
         -73,  -58,   27,   -8,   59,   87,  -25,  -58,  -88,  -90,
          21,   15,    4,   -5, -125,  -10,  -45,  -59,   30,  -61,
           2,   53,   84,   19,   90,   22, -111,   50,  -10,  117,
         -13,  -82,   43,   97,  -41,   42,  -17,  -14,   34,    3,
          25,  -99,  -47,   72,    1,  -57,    2,   21,    0, -105,
          96,   80, -113,   21,   35,   11,  -52,  -78, -110,  -71,
        -126,  -94,  -21, -124,   11,  -16,   88,   28,  -11,    2,
        -127, -127,    0,   -9,  -31,  -96, -123,  -42, -101,   61,
         -34,  -53,  -68,  -85,   92,   54,  -72,   87,  -71,  121,
        -108,  -81,  -69,   -6,   58,  -22, -126,   -7,   87,   76,
          11,   61,    7, -126,  103,   81,   89,   87, -114,  -70,
         -44,   89,   79,  -26,  113,    7,   16, -127, -128,  -76,
          73,   22,  113,   35,  -24,   76,   40,   22,   19,  -73,
         -49,    9,   50, -116,  -56,  -90,  -31,   60,   22,  122,
        -117,   84,  124, -115,   40,  -32,  -93,  -82,   30,   43,
         -77,  -90,  117, -111,  110,  -93,  127,   11,   -6,   33,
          53,   98,  -15,   -5,   98,  122,    1,   36,   59,  -52,
         -92,  -15,  -66,  -88,   81, -112, -119,  -88, -125,  -33,
         -31,   90,  -27,  -97,    6, -110, -117,  102,   94, -128,
         123,   85,   37,  100,    1,   76,   59,   -2,  -49,   73,
          42,    4,   22,    2,   20,   54,   11,   10, -102,   -1,
          33,   85,  115,   62,  -48,    6, -127,  118,  126,  -34,
         108,   10,  -55,   38,  -12
    };
}
