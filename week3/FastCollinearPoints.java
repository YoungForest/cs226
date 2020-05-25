import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        final int n = points.length;
        for (int i = 0; i < n; ++i) {
            if (points[i] == null) {
                throw new IllegalArgumentException("The " + i + " st point is null");
            }
        }
        ArrayList<LineSegment> lines = new ArrayList<>();
        Arrays.sort(points);
        for (int i = 0; i < n - 1; ++i) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("The " + i + " st point is repeated");
            }
        }
        for (int i = 0; i + 3 < n; ++i) {
            ArrayList<Point> others = new ArrayList<>();
            for (int j = i + 1; j < n; ++j) {
                others.add(points[j]);
            }
            final Point p = points[i];
            Collections.sort(others, (a, b) -> {
                if (p.slopeTo(a) < p.slopeTo(b)) {
                    return -1;
                } else if (p.slopeTo(a) <= p.slopeTo(b)) {
                    return 0;
                } else {
                    return 1;
                }
            });
            if (others.size() > 0) {
                double slope = p.slopeTo(others.get(0));
                int l = 0;
                for (int r = 1; r < others.size(); ++r) {
                    final Point end = others.get(r);
                    if (slope == p.slopeTo(end)) {
                    } else {
                        if (r - l >= 3) {
                            lines.add(new LineSegment(p, others.get(r - 1)));
                        }
                        l = r;
                        slope = p.slopeTo(end);
                    }
                }
                if (others.size() - l >= 3) {
                    lines.add(new LineSegment(p, others.get(others.size() - 1)));
                }
            }
        }
        lineSegments = lines.toArray(new LineSegment[0]);
    } // finds all line segments containing 4 or more points

    public int numberOfSegments() {
        return lineSegments.length;
    } // the number of line segments

    public LineSegment[] segments() {
        return lineSegments;
    } // the line segments

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}