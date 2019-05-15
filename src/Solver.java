import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {

    private final List<Board> solution;
    private final boolean isSolvable;
    private final int moves;

    private class SearchNode implements Comparable<SearchNode> {
        SearchNode predecessor;
        int moves;
        int manhattanDistance;
        Board board;

        public SearchNode(SearchNode predecessor, Board board, int moves) {
            this.predecessor = predecessor;
            this.board = board;
            this.moves = moves;
            this.manhattanDistance = this.board.manhattan();
        }

        @Override
        public int compareTo(SearchNode that) {
            // TODO Auto-generated method stub
            if (this.manhattanDistance + this.moves > that.manhattanDistance + that.moves) {
                return 1;
            }
            if (this.manhattanDistance + this.moves < that.manhattanDistance + that.moves) {
                return -1;
            }
            return 0;
        }
    }

    public Solver(Board initial) { // find a solution to the initial board (using the A* algorithm)
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        Board twin = initial.twin();

        SearchNode init = new SearchNode(null, initial, 0);
        SearchNode tw = new SearchNode(null, twin, 0);

        MinPQ<SearchNode> mainQueue = new MinPQ<SearchNode>();
        mainQueue.insert(init);

        MinPQ<SearchNode> twinQueue = new MinPQ<SearchNode>();
        twinQueue.insert(tw);

        do {
            init = mainQueue.delMin();
            Iterable<Board> initNeighbor = init.board.neighbors();
            for (Board i : initNeighbor) {
                if (init.predecessor == null || !i.equals(init.predecessor.board)) {
                    SearchNode tmp = new SearchNode(init, i, init.moves + 1);
                    mainQueue.insert(tmp);
                }
            }

            tw = twinQueue.delMin();
            Iterable<Board> twNeighbor = tw.board.neighbors();
            for (Board i : twNeighbor) {
                if (tw.predecessor == null || !i.equals(tw.predecessor.board)) {
                    SearchNode tmp = new SearchNode(tw, i, tw.moves + 1);
                    twinQueue.insert(tmp);
                }
            }
        } while (!(init.board.isGoal() || tw.board.isGoal()));

        if (tw.board.isGoal()) {
            this.moves = -1;
            this.isSolvable = false;
            this.solution = null;
        }
        else {
            this.solution = new ArrayList<Board>();
            ArrayList<Board> temp = new ArrayList<Board>();
            this.isSolvable = true;
            this.moves = init.moves;
            while (init != null) {
                temp.add(init.board);
                init = init.predecessor;
            }
            for (int i = temp.size() - 1; i >= 0; i--) {
                solution.add(temp.get(i));
            }

        }
    }

    public boolean isSolvable() { // is the initial board solvable?
        return isSolvable;
    }

    public int moves() { // min number of moves to solve initial board; -1 if unsolvable
        if (!isSolvable()) {
            return -1;
        }
        return moves;
    }

    public Iterable<Board> solution() { // sequence of boards in a shortest solution; null if unsolvable
        return solution;
    }

    public static void main(String[] args) { // solve a slider puzzle (given below)

    }
}