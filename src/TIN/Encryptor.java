package TIN;

import TIN.menu.MenuController;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Arrays;

public class Encryptor {
    private byte[] key;
    private byte[] iv;
    private String cipherType;
    private String algorithmType;

    public Encryptor() {
        try {
//            BufferedReader reader = new BufferedReader(
//                    new FileReader("src/TIN/resources/key"));
//            key = reader.readLine().getBytes();
//            iv = reader.readLine().getBytes();
            algorithmType = "AES";
            cipherType = "AES/CBC/PKCS5Padding";

            File keyFile = new File("src/TIN/resources/key.bin");
            byte[] keyAndIv = new byte[(int) keyFile.length()];
            InputStream is = new FileInputStream(keyFile);
            int readBytes = is.read(keyAndIv);

            if (readBytes != 32)
                throw new Exception("Wrong number of bytes in key file!");

            key = Arrays.copyOfRange(keyAndIv, 0, 16);
            iv = Arrays.copyOfRange(keyAndIv, 16, 32);

            is.close();

        } catch (FileNotFoundException e) {
            MenuController.showAlertDialog(
                    "Error opening key file",
                    e.getMessage()
            );
        } catch (IOException e) {
            MenuController.showAlertDialog(
                    "Error reading key file",
                    e.getMessage()
            );
        } catch (Exception e) {
            MenuController.showAlertDialog(
                    "Unknown error in Encryptor contructor",
                    e.getMessage()
            );
        }
    }

//    public void encrypt(InputStream inputStream, OutputStream outputStream) throws Exception {
    public byte[] encrypt(byte[] messageToEncrypt) throws Exception {
//        SecretKeySpec keySpec = new SecretKeySpec(key, algorithmType);
//
//        Cipher cipher = Cipher.getInstance(cipherType);
//        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
//
//        CipherInputStream cis = new CipherInputStream(inputStream, cipher);
//
//        byte[] buffer = new byte[128];
//        int numBytes;
//
//        while ((numBytes = cis.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, numBytes);
//        }
//
//        outputStream.flush();
//        outputStream.close();
//        cis.close();
        return encryptOrDecrypt(Cipher.ENCRYPT_MODE, messageToEncrypt);
    }

//    public void decrypt(InputStream inputStream, OutputStream outputStream) throws Exception {
    public byte[] decrypt(byte[] messageToDecrypt) throws Exception {
//        SecretKeySpec keySpec = new SecretKeySpec(key, algorithmType);
//
//        Cipher cipher = Cipher.getInstance(cipherType);
//        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
//
//        CipherOutputStream cos = new CipherOutputStream(outputStream, cipher);
//
//        byte[] buffer = new byte[128];
//        int numBytes;
//
//        while ((numBytes = inputStream.read(buffer)) != -1) {
//            cos.write(buffer, 0, numBytes);
//        }
//
//        cos.flush();
//        cos.close();
//        inputStream.close();
        return encryptOrDecrypt(Cipher.DECRYPT_MODE, messageToDecrypt);
    }

    private byte[] encryptOrDecrypt(int mode, byte[] message) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key, algorithmType);
        Cipher cipher = Cipher.getInstance(cipherType);
        cipher.init(mode, keySpec, new IvParameterSpec(iv));
        return cipher.doFinal(message);
    }
}
