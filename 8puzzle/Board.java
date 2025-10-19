/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

public class Board {

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private int[][] tiles;
    private int hamming = -1;
    private int manhattan = -1;
    private int dimension;

    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            this.tiles[i] = Arrays.copyOf(tiles[i], tiles[0].length);
        }
        this.dimension = tiles.length;
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension());
        for (int[] arr : tiles) {
            sb.append("\r\n");
            for (int i : arr) {
                sb.append(i).append(" ");
            }
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return this.dimension;
    }

    // number of tiles out of place
    public int hamming() {
        if (hamming != -1) return hamming;
        int outOfPlace = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (isBlank(i, j)) continue;
                int myplace = i * dimension + j + 1;
                if (tiles[i][j] != myplace) {
                    outOfPlace++;
                }
            }
        }
        hamming = outOfPlace;
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattan != -1) return manhattan;
        int sum = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (isBlank(i, j)) continue;
                int myplace = i * dimension + j + 1;
                if (tiles[i][j] != myplace) {
                    sum += dinstanceToOptimal(i, j);
                }
            }
        }
        manhattan = sum;
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {

    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null || y.getClass() != getClass()) return false;
        Board other = (Board) y;
        if (other.dimension != dimension) return false;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] != other.tiles[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

    }

    private boolean isBlank(int i, int j) {
        return tiles[i][j] == 0;
    }


    // unit testing (not graded)
    public static void main(String[] args) {

    }

    private int dinstanceToOptimal(int i, int j) {
        int value = tiles[i][j];
        int optimalVertical = (int) Math.ceil((double) value / dimension);
        int optimalHorizontal = value % dimension;
        return (optimalVertical - (i + 1)) + (optimalHorizontal - (j + 1));
    }


}
