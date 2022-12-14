import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import javax.imageio.ImageIO;

class imageProcessing {
    public static void main(String[] args) {
        // Example saving image as 2D array
        int[][] pixelData = imgToArr("./apple.jpg");

        // Alter image using methods below and resave as new file
        int[][] altered_img = colorFilter(invertImage(shrinkVertically(stretchHorizontally
        (negativeColor(trimBorders(pixelData, 20))))), -50, 80, 40);
        arrToImg(altered_img, "./altered.jpg");
    }

    // Return pixel data of image with color filter changing the rgb values
    public static int[][] colorFilter(int[][] pixelData, int redChange, int greenChange, int blueChange) {
        int[][] filtered = new int[pixelData.length][pixelData[0].length];
        for (int i = 0; i < filtered.length; i++) {
            for (int j = 0; j < filtered[0].length; j++) {
                int[] rgba = pixelToRGBA(pixelData[i][j]);
                rgba[0] += redChange;
                rgba[1] += greenChange;
                rgba[2] += blueChange;
                for (int k = 0; k < rgba.length; k++) {
                    if (rgba[k] < 0) {
                        rgba[k] = 0;
                    }
                    else if (rgba[k] > 255) {
                        rgba[k] = 255;
                    }
                }
                filtered[i][j] = RGBAToPixel(rgba);
            }
        }
        return filtered;
    }

    // Return pixel data of image flipped horizontally and vertically
    public static int[][] invertImage(int[][] pixelData) {
        int[][] inverted = new int[pixelData.length][pixelData[0].length];
        for (int i = 0; i < inverted.length; i++) {
            for (int j = 0; j < inverted[0].length; j++) {
                inverted[i][j] = pixelData[inverted.length - 1 - i][inverted[0].length - 1 - j];
            }
        }
        return inverted;
    }

    // Return pixel data of image shrunken by half in height
    public static int[][] shrinkVertically(int[][] pixelData) {
        int[][] shrunken = new int[pixelData.length/2][pixelData[0].length];
        for (int i = 0; i < shrunken.length; i++) {
            for (int j = 0; j < shrunken[0].length; j++) {
                shrunken[i][j] = pixelData[i*2][j];
            }
        }
        return shrunken;
    }

    // Return pixel data of image doubled in width
    public static int[][] stretchHorizontally(int[][] pixelData) {
        int[][] stretched = new int[pixelData.length][pixelData[0].length * 2];
        for (int i = 0; i < stretched.length; i++) {
            for (int j = 0; j < stretched[0].length; j++) {
                stretched[i][j] = pixelData[i][j/2];
            }
        }
        return stretched;
    }

    // Return negative version of input image pixel data
    public static int[][] negativeColor(int[][] pixelData) {
        int[][] negative = new int[pixelData.length][pixelData[0].length];
        for (int i = 0; i < negative.length; i++) {
            for (int j = 0; j < negative[0].length; j++) {
                negative[i][j] = 255 - pixelData[i][j];
            }
        }
        return negative;
    }

    // Trim pixelCount pixels from the all four borders of the image and return the resulting 2D array of pixels
    public static int[][] trimBorders(int[][] pixelData, int pixelCount) {
        if (pixelData.length > pixelCount * 2 && pixelData[0].length > pixelCount * 2) {
            int[][] trimmedPixelData = new int[pixelData.length - pixelCount * 2][pixelData[0].length - pixelCount * 2];
            for (int i  = 0; i < trimmedPixelData.length; i++) {
                for (int j = 0; j < trimmedPixelData[0].length; j++) {
                    trimmedPixelData[i][j] = pixelData[i + pixelCount][j + pixelCount];
                }
            }
            return trimmedPixelData;
        }
        else {
            System.out.println("Image does not contain enough pixels to trim by that amount");
            return pixelData;
        }
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

    // Convert the pixel value to an array containing the RGBA values of that pixel
    public static int[] pixelToRGBA(int pixel) {
        Color pixelColor = new Color(pixel);
        int[] rgba = {pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha()};
        return rgba;
    }

    // Convert array of RGBA values to int containing hex value of that pixel
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

    // View pixel data of top left corner of image
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