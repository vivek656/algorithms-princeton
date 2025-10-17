/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        test.testDraw(args);
    }

    void testBrute() {
        Point[] points = new Point[] {
                new Point(4847, 24341),
                new Point(15166, 14301),
                new Point(15166, 14301),
                };
        BruteCollinearPoints bcollinear = new BruteCollinearPoints(points);
        StdOut.println("brute collinear points:");
        for (LineSegment segment : bcollinear.segments()) {
            //   StdOut.println(segment);
        }
    }

    private void testDraw(String[] args) {

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
        BruteCollinearPoints bcollinear = new BruteCollinearPoints(points);
        StdOut.println("brute collinear points:" + bcollinear.numberOfSegments());
        for (LineSegment segment : bcollinear.segments()) {
            StdOut.println(segment);
        }

        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println("fast collinear points:" + collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            //   StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private static boolean isLess(Point p1, Point p2) {
        return p1.compareTo(p2) < 0;
    }

}
