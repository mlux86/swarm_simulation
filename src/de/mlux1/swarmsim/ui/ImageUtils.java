package de.mlux1.swarmsim.ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author mlux
 *         Date: 27.08.11
 *         <p/>
 *         Utility class for manipulating images.
 */
public final class ImageUtils
{

    /* utility class - constructor not visible */
    private ImageUtils()
    {
    }

    public static BufferedImage loadImage(String fileName) throws IOException
    {
        InputStream in = ClassLoader.getSystemResourceAsStream(fileName);
        return ImageIO.read(in);
    }

    /**
     * Transforms a certain color in an image to transparency.
     * From: http://www.javalobby.org/articles/ultimate-image/#8
     *
     * @param image The image to apply the transformation.
     * @param color The color that should be transparent.
     * @return The resulting image.
     */
    public static Image transformColorToTransparency(final BufferedImage image, final Color color)
    {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = result.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(image, null, 0, 0);
        g.dispose();
        for (int i = 0; i < result.getHeight(); i++)
        {
            for (int j = 0; j < result.getWidth(); j++)
            {
                if (result.getRGB(j, i) == color.getRGB())
                {
                    result.setRGB(j, i, 0x8F1C1C);
                }
            }
        }
        return result;
    }

    /**
     * Resize an image to the specified width and height.
     * The resulting image will not be interpolated.
     *
     * @param img       The image to resize.
     * @param newWidth  The new width.
     * @param newHeight The new height.
     * @return The resized image.
     */
    public static BufferedImage resize(BufferedImage img, int newWidth, int newHeight)
    {
        BufferedImage result = new BufferedImage(newWidth, newHeight, img.getType());
        Graphics2D g = result.createGraphics();
        g.drawImage(img, 0, 0, newWidth, newHeight, 0, 0, img.getWidth(), img.getHeight(), null);
        g.dispose();
        return result;
    }


}
