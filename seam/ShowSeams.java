/******************************************************************************
 *  Compilation:  javac ShowSeams.java
 *  Execution:    java ShowSeams input.png
 *  Dependencies: SeamCarver.java SCUtility.java
 *
 *  Read image from file specified as command line argument. Show 3 images 
 *  original image as well as horizontal and vertical seams of that image.
 *  Each image hides the previous one - drag them to see all three.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class ShowSeams {

    private static void showHorizontalSeam(SeamCarver sc) {
        Picture picture = SCUtility.toEnergyPicture(sc);
        int[] horizontalSeam = sc.findHorizontalSeam();
        Picture overlay = SCUtility.seamOverlay(picture, true, horizontalSeam);
        overlay.show();
    }


    private static void showVerticalSeam(SeamCarver sc) {
        Picture picture = SCUtility.toEnergyPicture(sc);
        int[] verticalSeam = sc.findVerticalSeam();
        Picture overlay = SCUtility.seamOverlay(picture, false, verticalSeam);
        overlay.show();
    }

    public static void main(String[] args) {
        og(args);
    }

    public static void og(String[] args) {
        Picture picture = new Picture("chameleon.png");
        StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());
        picture.show();
        SeamCarver sc = new SeamCarver(picture);

        StdOut.printf("Displaying horizontal seam calculated.\n");
        showHorizontalSeam(sc);

        StdOut.printf("Displaying vertical seam calculated.\n");
        showVerticalSeam(sc);
    }

    public static void test() {
        Picture picture = new Picture("10x12.png");
        SeamCarver sc = new SeamCarver(picture);
        printEnergyMatrix(sc);
        printHorizontalSeam(sc);
        printVerticalSeam(sc);

        sc.removeVerticalSeam(sc.findVerticalSeam());

        Picture newPicture = sc.picture();
    }

    public static void printEnergyMatrix(SeamCarver sc) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sc.height(); i++) {
            for (int j = 0; j < sc.width(); j++) {
                sb.append(sc.energy(j, i)).append(" ");
            }
            sb.append("\n");
        }
        StdOut.println(sb);
    }

    public static void printHorizontalSeam(SeamCarver sc) {
        StdOut.printf("Displaying horizontal seam calculated.\n");
        StringBuilder sb = new StringBuilder();
        int[] horizontalSeam = sc.findHorizontalSeam();
        sb.append(Arrays.toString(horizontalSeam)).append("\n");

        for (int i = 0; i < sc.height(); i++) {
            for (int j = 0; j < sc.width(); j++) {
                sb.append(format(sc.energy(j, i)));
                if (horizontalSeam[j] == i) {
                    sb.append("*");
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        StdOut.println();
        StdOut.println(sb);
    }

    public static void printVerticalSeam(SeamCarver sc) {
        StdOut.printf("Displaying vertical seam calculated.\n");
        StringBuilder sb = new StringBuilder();
        int[] verticalSeam = sc.findVerticalSeam();
        sb.append(Arrays.toString(verticalSeam)).append("\n");

        for (int i = 0; i < sc.height(); i++) {
            for (int j = 0; j < sc.width(); j++) {
                sb.append(format(sc.energy(j, i)));
                if (verticalSeam[i] == j) {
                    sb.append("*");
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        StdOut.println();
        StdOut.println(sb);
    }


    public static String format(double d) {
        return String.format("%7.2f", d);
    }
}
