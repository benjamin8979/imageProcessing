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

    // convert the pixel value to an array containing the RGBA values of that pixel
    public static int[] pixelToRGBA(int pixel) {
        Color pixelColor = new Color(pixel);
        int[] rgba = {pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha()};
        return rgba;
    }

    // convert array of RGBA values to int containing hex value of that pixel
    public static int RGBAToPixel(int[] rgba) {
        if (rgba.length == 4) {
            Color pixelColor = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
            return pixelColor.getRGB();
        }
        else {
            System.out.println("Incorrect number of elements in rgba array. Requires 4 values.");
            return -1;
        }
    }

    // view pixel data of top left corner of image
    public static void viewImageData(int[][] pixelData) {
        if (pixelData.length > 3 && pixelData[0].length > 3) {
            int[][] rawPixelData = new int[3][3];
            for (int i = 0; i < pixelData.length; i++) {
                for (int j = 0; j < pixelData[0].length; j++) {
                    rawPixelData[i][j] = pixelData[i][j];
                }
            }
            System.out.println("Raw pixel Data:");
            System.out.println(Arrays.deepToString(rawPixelData));
            int[][][] extractedPixelData = new int[3][3][4];
            for (int i = 0; i < pixelData.length; i++) {
                for (int j = 0; j < pixelData[0].length; j++) {
                    extractedPixelData[i][j] = pixelToRGBA(pixelData[i][j]);
                }
            }
            System.out.println("Extracted RGBA values from pixel data:");
            System.out.println(Arrays.deepToString(extractedPixelData));
        }
        else {
            System.out.println("Image is not big enough to display a 3x3 sample of the top left corner.");
        }
    }
}