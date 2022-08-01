import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

class imageProcessing {
    public static void main(String[] args) {

    }

    // Convert image from input file or link to 2D array of pixel rgba values
    public static int[][] imgToArr(String inputImage) {
        try {
            BufferedImage image = null;
            if (inputImage.substring(0,4).toLowerCase().equals("http")) {
                URL imageURL = new URL(inputImage);
                image = ImageIO.read(imageURL);
                if (image == null) {
                    System.err.println("Failed to get image from provided URL.");
                }
            }
            else {
                image = ImageIO.read(new File(inputImage));
            }
            int rows = image.getHeight();
            int cols = image.getWidth();
            int[][] pixelData = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    pixelData[i][j] = image.getRGB(j, i);
                }
            }
            return pixelData;
        }
        catch (Exception e) {
            System.err.println("Failed to load image: " + e.getLocalizedMessage());
            return null;
        }
    }

    // Convert 2D array of pixel rgba values into image file
    public static void arrToImg(int[][] pixelData, String filename) {
        try {
            int rows = pixelData.length;
            int cols = pixelData[0].length;
            BufferedImage result = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    result.setRGB(j, i, pixelData[i][j]);
                }
            }
            File output = new File(filename);
            ImageIO.write(result, "jpg", output);
        }
        catch (Exception e) {
            System.err.println("Failed to save image: " + e.getLocalizedMessage());
        }
    }
}