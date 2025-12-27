/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private static final int[] IDXES = { 0, -1, 1 };
    private int[][] picture;
    private double[][] energy;
    private int currentWidth;
    private int currentHeight;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        throwIfNull(picture);
        this.currentWidth = picture.width();
        this.currentHeight = picture.height();
        initailizeEnergyAndValues(picture);
    }

    // current picture
    public Picture picture() {
        Picture res = new Picture(this.currentWidth, this.currentHeight);
        for (int col = 0; col < this.currentWidth; col++) {
            for (int row = 0; row < this.currentHeight; row++) {
                res.setRGB(col, row, this.picture[col][row]);
            }
        }
        return res;
    }

    // width of current picture
    public int width() {
        return currentWidth;
    }

    // height of current picture
    public int height() {
        return currentHeight;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (outsideBorder(x, y)) {
            throw new IllegalArgumentException();
        }
        return this.energy[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findSeam(0);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(1);
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, 0);
        for (int col = 0; col < seam.length; col++) {
            int row = seam[col];
            System.arraycopy(picture[col], row + 1, picture[col], row, currentHeight - row - 1);
        }
        currentHeight--;
        setEnergies();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, 1);
        for (int row = 0; row < seam.length; row++) {
            for (int col = seam[row]; col < currentWidth - 1; col++) {
                picture[col][row] = picture[col + 1][row];
            }
        }
        currentWidth--;
        setEnergies();
    }

    private void validateSeam(int[] seam, int axis) {
        throwIfNull(seam);
        int length = axisLength(axis);
        int minorLength = axisLength(1 - axis);
        if (seam.length != length) {
            throw new IllegalArgumentException("invalid seam length");
        }
        if (minorLength == 1) {
            throw new IllegalArgumentException("invalid picture length , cannot be carved more");
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= minorLength) {
                throw new IllegalArgumentException("Invalid seam coordinate: " + seam[i]);
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("Invalid seams items differ more by 1: ");
            }
        }
    }

    private int[] findSeam(int axis) {
        int[] next = new int[currentWidth * currentHeight];
        double[][] weight = new double[currentWidth][currentHeight];
        int[] last = { currentHeight, currentWidth };
        double lowestStart = Double.POSITIVE_INFINITY;
        int startMinor = 0;
        int majourLength = axisLength(axis);
        int minorLength = axisLength(1 - axis);
        for (int major = majourLength - 1; major >= 0; major--) {
            for (int minor = minorLength - 1; minor >= 0; minor--) {
                if (major == majourLength - 1) {
                    set(weight, major, minor, axis, energy(major, minor, axis));
                    continue;
                }
                double lowestWeight = Double.POSITIVE_INFINITY;
                int nextIdx = 0;
                for (int i : IDXES) {
                    double myWeight = get(weight, major + 1, minor + i, axis);
                    if (myWeight < lowestWeight) {
                        nextIdx = i;
                        lowestWeight = myWeight;
                    }
                }
                set(weight, major, minor, axis, lowestWeight + energy(major, minor, axis));
                set(next, major, minor, axis, new int[] {
                        column(major + 1, minor + nextIdx, axis),
                        row(major + 1, minor + nextIdx, axis)
                });

                if (major == 0) {
                    if (get(weight, major, minor, axis) <= lowestStart) {
                        lowestStart = get(weight, major, minor, axis);
                        startMinor = minor;
                    }
                }
            }
        }

        int[] minors = new int[majourLength];
        int currentMinor = startMinor;
        for (int major = 0; major < majourLength; major++) {
            minors[major] = currentMinor;
            currentMinor = minor(get(next, major, currentMinor, axis, last), axis);
        }
        return minors;
    }


    private int getSqaureGradient(int x, int y) {
        int r = (x >> 16 & 0xFF) - (y >> 16 & 0xFF);
        int g = (x >> 8 & 0xFF) - (y >> 8 & 0xFF);
        int b = (x & 0xFF) - (y & 0xFF);
        return r * r + g * g + b * b;
    }


    private int index(int col, int row) {
        if (col < 0 || col >= currentWidth || row < 0 || row >= currentHeight) {
            return -1;
        }
        return (currentHeight * col) + row;
    }

    private void initailizeEnergyAndValues(Picture ogPicture) {
        picture = new int[currentWidth][currentHeight];
        energy = new double[currentWidth][currentHeight];
        for (int col = 0; col < currentWidth; col++) {
            for (int row = 0; row < currentHeight; row++) {
                picture[col][row] = ogPicture.getRGB(col, row);
            }
        }
        setEnergies();
    }

    private double getEnergy(int col, int row) {
        if (atOurOutsideBorder(col, row)) {
            return 1000;
        }
        int horizontalGrad = getSqaureGradient(picture[col - 1][row], picture[col + 1][row]);
        int verticalGrad = getSqaureGradient(picture[col][row + 1], picture[col][row - 1]);
        return Math.sqrt(horizontalGrad + verticalGrad);
    }


    private void set(int[] next, int majour, int minor, int axis, int[] rc) {
        int indexi = index(column(majour, minor, axis), row(majour, minor, axis));
        int to = index(rc[0], rc[1]);
        if (indexi < 0 || to < 0) return;
        next[indexi] = to;
    }

    private void set(double[][] weight, int majour, int minor, int axis, double value) {
        weight[column(majour, minor, axis)][row(majour, minor, axis)] = value;
    }

    private double get(double[][] weight, int majour, int minor, int axis) {
        int column = column(majour, minor, axis);
        int row = row(majour, minor, axis);
        int index = index(column, row);
        if (index < 0) return Double.POSITIVE_INFINITY;
        if (weight[column][row] < 0) return Double.POSITIVE_INFINITY;
        return weight[column][row];
    }

    private int[] get(int[] next, int majour, int minor, int axis, int[] orElse) {
        int index = index(column(majour, minor, axis), row(majour, minor, axis));
        if (index == -1) {
            return orElse;
        }
        return toInts(next[index]);
    }

    // axis 0 major col minor row
    // axis 1 majour row minor col
    private int column(int majour, int minor, int axis) {
        if (axis == 0) return majour;
        else return minor;
    }

    private int row(int majour, int minor, int axis) {
        if (axis == 0) return minor;
        else return majour;
    }

    private void setEnergies() {
        for (int col = 0; col < currentWidth; col++) {
            for (int row = 0; row < currentHeight; row++) {
                energy[col][row] = getEnergy(col, row);
            }
        }
    }

    private int axisLength(int axis) {
        if (axis == 0) return currentWidth;
        else return currentHeight;
    }

    private int minor(int[] ints, int axis) {
        return ints[1 - axis];
    }

    private boolean atOurOutsideBorder(int col, int row) {
        return (col <= 0 || col >= currentWidth - 1 || row <= 0 || row >= currentHeight - 1);
    }

    private boolean outsideBorder(int col, int row) {
        return (col < 0 || col >= currentWidth || row < 0 || row >= currentHeight);
    }

    private double energy(int majour, int minor, int axis) {
        return energy(column(majour, minor, axis), row(majour, minor, axis));
    }

    private int[] toInts(int index) {
        return new int[] { index / currentHeight, index % currentHeight };
    }

    private void throwIfNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException();
        }
    }

}
