/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Board {

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private int[][] tiles;
    private int hamming = -1;
    private int manhattan = -1;
    private int dimension;
    private int blankPosition;

    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            this.tiles[i] = Arrays.copyOf(tiles[i], tiles[0].length);
        }
        this.dimension = tiles.length;
        initializeAll();
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
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return true;
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
        Queue<Board> q = new Queue<Board>();
        int[] blankPos = posAsArray(blankPosition);
        if (blankPos[0] != 0) {
            q.enqueue(exchnageAndGetNew(blankPos, new int[] { blankPos[0] - 1, blankPos[1] }));
        }
        if (blankPos[1] != dimension - 1) {
            q.enqueue(exchnageAndGetNew(blankPos, new int[] { blankPos[0], blankPos[1] + 1 }));

        }
        if (blankPos[0] != dimension - 1) {
            q.enqueue(exchnageAndGetNew(blankPos, new int[] { blankPos[0] + 1, blankPos[1] }));

        }
        if (blankPos[1] != 0) {
            q.enqueue(exchnageAndGetNew(blankPos, new int[] { blankPos[0], blankPos[1] - 1 }));
        }
        return q;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int random1 = randomNonBlankPosition(-1);
        int random2 = randomNonBlankPosition(random1);
        return exchnageAndGetNew(posAsArray(random1), posAsArray(random2));
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

    private void initializeAll() {
        int manhattanSum = 0;
        int hammingSum = 0;
        int[] blankPos = new int[2];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (isBlank(i, j)) {
                    blankPos[0] = i;
                    blankPos[1] = j;
                    continue;
                }
                int myplace = i * dimension + j + 1;
                if (tiles[i][j] != myplace) {
                    manhattanSum += dinstanceToOptimal(i, j);
                    hammingSum += 1;
                }
            }
        }
        manhattan = manhattanSum;
        hamming = hammingSum;
        blankPosition = blankPos[0] * dimension + blankPos[1];
    }

    private int randomNonBlankPosition(int except) {
        while (true) {
            int pos = StdRandom.uniformInt(0, dimension * dimension);
            if (pos != blankPosition && pos != except) {
                return pos;
            }
        }
    }

    private int[] posAsArray(int pos1) {
        return new int[] { (pos1) / dimension, dimension - pos1 % dimension };
    }

    private int[] neighboursPos() {
        int[] blankPos = posAsArray(blankPosition);
        int[] neighbours = new int[4];
        if (blankPos[0] != 0) {
            neighbours[0] = 1;
        }
        if (blankPos[1] != dimension - 1) {
            neighbours[1] = 1;
        }
        if (blankPos[0] != dimension - 1) {
            neighbours[2] = 1;
        }
        if (blankPos[1] != 0) {
            neighbours[3] = 1;
        }
        return neighbours;
    }

    private Board exchnageAndGetNew(int[] posi1, int[] posi2) {
        Board b = new Board(tiles);
        int temp = b.tiles[posi1[0]][posi1[1]];
        b.tiles[posi1[0]][posi1[1]] = b.tiles[posi2[0]][posi2[1]];
        b.tiles[posi2[0]][posi2[1]] = temp;
        return b;
    }

}
