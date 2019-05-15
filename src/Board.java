import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] board;
    private final int n;

    public Board(int[][] blocks) { // construct a board from an n-by-n array of blocks
        this.n = blocks.length;
        this.board = new int[blocks.length][blocks.length];
        
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                board[i][j] = blocks[i][j];
            }
        }
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() { // board dimension n
        return n;
    }

    public int hamming() { // number of blocks out of place

        int hamming = 0;

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] != 0 && this.board[i][j] != valueAtBlock(i, j)) {
                    hamming++;
                }
            }
        }

        return hamming;
    }

    public int manhattan() { // sum of Manhattan distances between blocks and goal

        int manhattan = 0;

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] != 0 && this.board[i][j] != valueAtBlock(i, j)) {
                    int p = (this.board[i][j] % n) == 0 ? (this.board[i][j] / this.n) - 1 : this.board[i][j] / this.n;
                    int q = (this.board[i][j] % n) == 0 ? this.n - 1 : (this.board[i][j] % n) - 1;
                    manhattan = manhattan + Math.abs(i - p) + Math.abs(j - q);
                }
            }
        }

        return manhattan;
    }

    public boolean isGoal() { // is this board the goal board?

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.board[i][j] != 0 && this.board[i][j] != valueAtBlock(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board twin() { // a board that is obtained by exchanging any pair of blocks

        int p = 0, q = 0, r = 0, s = 0;
        boolean found = false;

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] != 0) {
                    p = i;
                    q = j;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        found = false;

        for (int i = p; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {

                if (i == p && j <= q) {
                    continue;
                }

                if (this.board[i][j] != 0) {
                    r = i;
                    s = j;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        int[][] temp = this.cloneBoard();

        int tmp = temp[p][q];
        temp[p][q] = temp[r][s];
        temp[r][s] = tmp;

        return new Board(temp);
    }

    public boolean equals(Object y) { // does this board equal y?

        if (y == null) {
            return false;
        }

        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) y;

        if (this == that) {
            return true;
        }

        if (this.n != that.n) {
            return false;
        }

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j] != that.board[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() { // all neighboring boards

        List<Board> neighbors = new ArrayList<>();

        int p = 0, q = 0;

        while (p < this.n && this.board[p][q] != 0) {
            if (q < this.n - 1) {
                q++;
            } else if (p < this.n) {
                p++;
                q = 0;
            }
        }

        if (p - 1 >= 0) {
            int[][] temp = cloneBoard();
            temp[p][q] = temp[p - 1][q];
            temp[p - 1][q] = 0;
            neighbors.add(new Board(temp));
        }
        if (p + 1 < this.n) {
            int[][] temp = cloneBoard();
            temp[p][q] = temp[p + 1][q];
            temp[p + 1][q] = 0;
            neighbors.add(new Board(temp));
        }
        if (q - 1 >= 0) {
            int[][] temp = cloneBoard();
            temp[p][q] = temp[p][q - 1];
            temp[p][q - 1] = 0;
            neighbors.add(new Board(temp));
        }
        if (q + 1 < this.n) {
            int[][] temp = cloneBoard();
            temp[p][q] = temp[p][q + 1];
            temp[p][q + 1] = 0;
            neighbors.add(new Board(temp));
        }

        return neighbors;
    }

    public String toString() { // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private int[][] cloneBoard() {
        int[][] temp = new int[this.n][this.n];

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                temp[i][j] = this.board[i][j];
            }
        }

        return temp;
    }

    private int valueAtBlock(int row, int column) {
        return (this.n * row) + column + 1;
    }

    public static void main(String[] args) { // unit tests (not graded)
        // int[][] blocks = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        int[][] blocks = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        Board b = new Board(blocks);
//        System.out.println("Board: \n" + b);
//        System.out.println("dimension: " + b.dimension());
//        System.out.println("hamming: " + b.hamming());
        System.out.println("manhattan: " + b.manhattan());
//        System.out.println("twin: \n" + b.twin());
//        System.out.println("Goal? " + b.isGoal());
//        System.out.println("equal: " + b.equals(new Board(blocks)));
//        System.out.println("neighbors: ");
//        Iterable<Board> it = b.neighbors();
//        for (Board i : it) {
//            System.out.println(i);
//        }
    }
}