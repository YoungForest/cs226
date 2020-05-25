import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;

public class Solver {
    private boolean isSolve = false;
    private int distance = 0;
    private static final int INF = 0x3f3f3f3f;
    private List<Board> route;

    private class Node implements Comparable<Node> {
        int fScore;
        int gScore;
        Node comeFrom;
        Board data;

        public Node(Node source, Board b) {
            if (source == null)
                gScore = 0;
            else
                gScore = source.gScore + 1;
            fScore = gScore + b.manhattan();
            comeFrom = source;
            data = b;
        }

        @Override
        public int compareTo(Node that) {
            return this.fScore - that.fScore;
        }
    }

    Node searchBoard(MinPQ<Node> pq) {
        Node current = pq.delMin();
        if (current.data.isGoal()) {
            return current;
        }
        for (Board i : current.data.neighbors()) {
            if (current.comeFrom == null || !i.equals(current.comeFrom.data)) {
                pq.insert(new Node(current, i));
            }
        }
        return null;
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        int step = 0;
        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> twinPq = new MinPQ<>();
        pq.insert(new Node(null, initial));
        twinPq.insert(new Node(null, initial.twin()));
        while (!pq.isEmpty() && !twinPq.isEmpty()) {
            Node current = searchBoard(pq);
            if (current != null) {
                isSolve = true;
                distance = current.gScore;
                // construct answer
                route = new ArrayList<>();
                while (current != null) {
                    route.add(current.data);
                    current = current.comeFrom;
                }
                Collections.reverse(route);
                break;
            }
            if (searchBoard(twinPq) != null) {
                break;
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolve;
    }

    // min number of moves to solve initial board
    public int moves() {
        return distance;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return route;
    }

    // test client (see below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}