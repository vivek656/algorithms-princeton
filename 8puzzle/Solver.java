/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;
import java.util.Iterator;

public class Solver {

    private Board myBoard;
    private boolean isSolvable;
    private Board[] movesBoard;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        this.myBoard = initial;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable) return -1;
        return movesBoard.length;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable) return null;
        Stack<Board> stack = new Stack<>();
        for (Board b : movesBoard) {
            stack.push(b);
        }
        return stack;
    }

    // test client (see below)
    public static void main(String[] args) {

    }

    private void solve() {
        Board twin = myBoard.twin();
        MinPQ<BoardWithMoves> q1 = new MinPQ<>();
        MinPQ<BoardWithMoves> q2 = new MinPQ<>();
        q1.insert(new BoardWithMoves(myBoard, 0, null));
        q2.insert(new BoardWithMoves(twin, 0, null));
        BoardWithMoves previousQ1 = null;
        BoardWithMoves previousQ2 = null;
        while (!q1.isEmpty()) {
            BoardWithMoves q1Board = q1.delMin();
            BoardWithMoves q2Board = q2.delMin();
            if (q1Board.board.manhattan() == 0) return;
            if (q2Board.board.manhattan() == 0) return;

            Iterator<Board> q1Neighbours = q1Board.board.neighbors().iterator();
            Iterator<Board> q2Neighbours = q2Board.board.neighbors().iterator();

            while (q1Neighbours.hasNext()) {
                Board next = q1Neighbours.next();
                if (previousQ1 == null || (!next.equals(previousQ1.board))) {
                    q1.insert(new BoardWithMoves(q1Neighbours.next(), q1Board.moves + 1, q1Board));
                }
            }
            while (q2Neighbours.hasNext()) {
                Board next = q2Neighbours.next();
                if (previousQ2 == null || (!next.equals(previousQ2.board))) {
                    q2.insert(new BoardWithMoves(q2Neighbours.next(), q2Board.moves + 1, q2Board));
                }
            }
            previousQ1 = q1Board;
            previousQ2 = q2Board;
        }
    }

    class BoardWithMoves implements Comparator<BoardWithMoves> {
        private Board board;
        private int moves;
        private BoardWithMoves parent;

        BoardWithMoves(Board board, int moves, BoardWithMoves parent) {
            this.board = board;
            this.moves = moves;
            this.parent = parent;
        }

        public int compare(BoardWithMoves o1, BoardWithMoves o2) {
            if (o1 == o2) return 0;
            int manhattanO1 = o1.board.manhattan();
            int manhattan02 = o2.board.manhattan();
            return (manhattanO1 + o1.moves) - (manhattan02 + o2.moves);
        }
    }

    private void notSolvableFill() {
        isSolvable = false;
    }

    private void solvableFill(BoardWithMoves current) {
        isSolvable = true;
        int numMoves = current.moves;
        Board[] movess = new Board[numMoves];
        BoardWithMoves bbs = current;
        int k = 0;
        while (bbs != null) {
            movess[k] = bbs.board;
            k++;
            bbs = bbs.parent;
        }
        this.movesBoard = movess;
    }
}
