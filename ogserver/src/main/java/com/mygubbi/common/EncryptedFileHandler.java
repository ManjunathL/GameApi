package com.mygubbi.common;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Created by test on 18-05-2016.
 */
public class EncryptedFileHandler
{
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public void encrypt(String key, File inputFile, File outputFile)
    {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public void decrypt(String key, File inputFile, File outputFile)
    {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    public String decrypt(String key, File inputFile)
    {
        return new String(doCrypto(Cipher.DECRYPT_MODE, key, inputFile));
    }

    public String decrypt(String key, String inputFile)
    {
        return decrypt(key, new File(inputFile));
    }

    private byte[] doCrypto(int cipherMode, String key, File inputFile)
    {
        FileInputStream inputStream = null;
        try
        {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            return cipher.doFinal(inputBytes);

        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex)
        {
            throw new RuntimeException("Error encrypting/decrypting file", ex);
        }
        finally
        {
            try
            {
                if (inputStream != null) inputStream.close();
            }
            catch (IOException e)
            {
                //Nothing to do
            }
        }
    }

    private void doCrypto(int cipherMode, String key, File inputFile, File outputFile)
    {
        try
        {
            byte[] outputBytes = doCrypto(cipherMode, key, inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);
            outputStream.close();
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Error writing to output file", ex);
        }
    }
}
