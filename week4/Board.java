import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Board {
    private final int[][] tiles;
    private final int rows;
    private final int cols;
    private final int n;
    private int hammingCache;
    private int manhattenCache;
    private String stringCache;
    private List<Board> neigh;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException("tiles is null");
        this.rows = tiles.length;
        if (this.rows == 0)
            throw new IllegalArgumentException("tiles rows == 0");
        this.cols = tiles[0].length;
        if (this.rows != this.cols)
            throw new IllegalArgumentException("tiles rows != cols");
        this.n = this.rows;
        this.tiles = new int[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < rows; ++j) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
        hammingCache = 0;
        manhattenCache = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] == 0)
                    continue;
                if (tiles[i][j] != i * n + j + 1)
                    ++hammingCache;
                int targetI = (tiles[i][j] - 1) / n;
                int targetJ = (tiles[i][j] - 1) % n;
                manhattenCache += Math.abs(targetI - i) + Math.abs(targetJ - j);
            }
        }
        StringBuilder ans = new StringBuilder();
        ans.append(n);
        ans.append('\n');
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                ans.append(tiles[i][j]);
                ans.append(' ');
            }
            ans.append('\n');
        }
        stringCache = ans.toString();
    }

    // string representation of this board
    public String toString() {
        return stringCache;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingCache;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattenCache;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        return stringCache.equals(y.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (neigh == null) {
            int zeroI = 0;
            int zeroJ = 0;
            outerloop: for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (tiles[i][j] == 0) {
                        zeroI = i;
                        zeroJ = j;
                        break outerloop;
                    }
                }
            }
            int[][] exchangeTiles = new int[n][n];
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    exchangeTiles[i][j] = tiles[i][j];
                }
            }
            int[][] moveTile = { { 0, 1 }, { 1, 0 }, { -1, 0 }, { 0, -1 } };
            neigh = new ArrayList<Board>();
            for (int[] d : moveTile) {
                int ni = zeroI + d[0];
                int nj = zeroJ + d[1];
                if (ni >= 0 && ni < n && nj >= 0 && nj < n) {
                    int tmp = exchangeTiles[zeroI][zeroJ];
                    exchangeTiles[zeroI][zeroJ] = exchangeTiles[ni][nj];
                    exchangeTiles[ni][nj] = tmp;
                    neigh.add(new Board(exchangeTiles));
                    tmp = exchangeTiles[zeroI][zeroJ];
                    exchangeTiles[zeroI][zeroJ] = exchangeTiles[ni][nj];
                    exchangeTiles[ni][nj] = tmp;
                }
            }
        }
        return neigh;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] exchangeTiles = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                exchangeTiles[i][j] = tiles[i][j];
            }
        }
        if (exchangeTiles[0][0] != 0 && exchangeTiles[0][1] != 0) {
            int tmp = exchangeTiles[0][0];
            exchangeTiles[0][0] = exchangeTiles[0][1];
            exchangeTiles[0][1] = tmp;
        } else {
            int tmp = exchangeTiles[n - 1][0];
            exchangeTiles[n - 1][0] = exchangeTiles[n - 1][1];
            exchangeTiles[n - 1][1] = tmp;
        }
        return new Board(exchangeTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = { { 1, 2, 3 }, { 4, 6, 5 }, { 7, 8, 0 } };
        Board b = new Board(tiles);
        System.out.println(b);
        System.out.println("Hamming: ");
        System.out.println(b.hamming());
        System.out.println("Manhattan: ");
        System.out.println(b.manhattan());
        System.out.println(b.twin());
        System.out.println(b.equals(b));
        for (Board i : b.neighbors()) {
            System.out.println(i);
        }
    }
}