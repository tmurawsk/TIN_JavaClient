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
    static int maxImageSize = 786486;

    public static byte[] generateEmptyByteBuffer(int size) {
        return new byte[size];
    }

    public static byte[] getBytesFromImage(Image image, String extension) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferedImage, extension, out);
            byte[] buffer = out.toByteArray();
            out.close();
            return buffer;
        } catch (IOException e) {
            MenuController.showAlertDialog(
                    "Error converting image to byte array",
                    e.getMessage()
            );
            return null;
        }
    }

    public static Image getImageFromBytes(byte[] buffer) {
        return buffer == null ? null : new Image(new ByteArrayInputStream(buffer));
    }
}
