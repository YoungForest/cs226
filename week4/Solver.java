import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;

public class Solver {
    boolean isSolve = false;
    int distance = 0;
    static final int INF = 0x3f3f3f3f;
    List<Board> route;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        int step = 0;
        Map<Board, Integer> fScore = new HashMap<>();
        Map<Board, Integer> gScore = new HashMap<>();
        Map<Board, Board> cameFrom = new HashMap<>();
        Set<Board> seen = new HashSet<>();
        MinPQ<Board> pq = new MinPQ<>((a, b) -> {
            return fScore.get(a) - fScore.get(b);
        });
        gScore.put(initial, 0);
        fScore.put(initial, initial.manhattan());
        pq.insert(initial);
        assert(seen.add(initial));
        cameFrom.put(initial, null);
        while(!pq.isEmpty()) {
            Board current = pq.delMin();
            if (current.isGoal()) {
                isSolve = true;
                distance = gScore.get(current);
                // construct answer
                route = new ArrayList<>();
                while (current != null) {
                    route.add(current);
                    current = cameFrom.get(current);
                }
                Collections.reverse(route);
                break;
            }
            for (Board i : current.neighbors()) {
                int tentative_gScore = gScore.get(current) + 1;
                if (tentative_gScore < gScore.getOrDefault(i, INF)) {
                    cameFrom.put(i, current);
                    gScore.put(i, tentative_gScore);
                    fScore.put(i, tentative_gScore + i.manhattan());
                    if (!seen.contains(i)) {
                        pq.insert(i);
                        assert(seen.add(i));
                    }
                }
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
        return new Iterable<Board>() { 
            @Override
            public Iterator<Board> iterator() 
            { 
                return route.iterator();
            } 
        }; 
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