package TIN;

import TIN.menu.MenuController;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Converter {
    public static int maxImageSize = 786486;

    public static byte[] generateEmptyByteBuffer(int size) {
        return new byte[size];
    }

    public static byte[] getBytesFromImage(Image image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer;

        try {
            ImageIO.write(bufferedImage, "bmp", out);
            buffer = out.toByteArray();
            out.close();
        } catch (IOException e) {
            MenuController.showAlertDialog(
                    "Error converting image to byte array",
                    e.getMessage()
            );
            return null;
        }
        return buffer;
    }

    public static Image getImageFromBytes(byte[] buffer) {
        return new Image(new ByteArrayInputStream(buffer));
    }
}
