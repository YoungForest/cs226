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

    private boolean contain(Point x1, Point y1, Point x2, Point y2) {
        if (x1.slopeTo(y1) == x2.slopeTo(y2)) {
            if (x1.compareTo(x2) == 0 || x1.slopeTo(x2) == x1.slopeTo(y1)) {
                return x1.compareTo(x2) <= 0 && y1.compareTo(y2) >= 0;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

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
        ArrayList<Point[]> lines = new ArrayList<>();
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
                            Point[] line = new Point[2];
                            line[0] = p;
                            line[1] = others.get(r - 1);
                            lines.add(line);
                        }
                        l = r;
                        slope = p.slopeTo(end);
                    }
                }
                if (others.size() - l >= 3) {
                    Point[] line = new Point[2];
                    line[0] = p;
                    line[1] = others.get(others.size() - 1);
                    lines.add(line);
                }
            }
        }
        ArrayList<LineSegment> ans = new ArrayList<>();
        lines.sort((a, b) -> {
            if (a[0].compareTo(b[0]) != 0) return a[0].compareTo(b[0]);
            else return a[1].compareTo(b[1]);
        });
        if (!lines.isEmpty()) {
            ArrayList<Point[]> unique = new ArrayList<>();
            unique.add(lines.get(0));
            for (int i = 1; i < lines.size(); ++i) {
                if (lines.get(i)[0] == lines.get(i-1)[0] && lines.get(i)[1] == lines.get(i-1)[1]) {
                    continue;
                } else {
                    unique.add(lines.get(i));
                }
            }
            lines = unique;
        }
        for (int i = 0; i < lines.size(); ++i) {
            boolean wasContain = false;
            for (int j = 0; j < lines.size(); ++j) {
                if (i == j) continue;
                if (contain(lines.get(j)[0], lines.get(j)[1], lines.get(i)[0], lines.get(i)[1])) {
                    wasContain = true;
                    break;
                }
            }
            if (!wasContain) {
                ans.add(new LineSegment(lines.get(i)[0], lines.get(i)[1]));
            }
        }
        lineSegments = ans.toArray(new LineSegment[0]);
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