package TIN;

import TIN.menu.MenuController;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

public class Encryptor {
    private String key;
    private static int keyLength = 16;
    private static int initVectorLength = 16;

    public Encryptor() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/TIN/resources/key"));
            key = reader.readLine();
            reader.close();
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
        }
    }

    public void encrypt(InputStream inputStream, OutputStream outputStream) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        CipherInputStream cis = new CipherInputStream(inputStream, cipher);

        byte[] buffer = new byte[128];
        int numBytes;

        while((numBytes = cis.read(buffer)) != -1) {
            outputStream.write(buffer, 0, numBytes);
        }

        outputStream.flush();
        outputStream.close();
        cis.close();
    }

    public void decrypt(InputStream inputStream, OutputStream outputStream) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        CipherOutputStream cos = new CipherOutputStream(outputStream, cipher);

        byte[] buffer = new byte[128];
        int numBytes;

        while((numBytes = inputStream.read(buffer)) != -1) {
            cos.write(buffer, 0, numBytes);
        }

        cos.flush();
        cos.close();
        inputStream.close();
    }
}
