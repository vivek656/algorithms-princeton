/******************************************************************************
 *  Compilation:  javac NearestNeighborVisualizer.java
 *  Execution:    java NearestNeighborVisualizer input.txt
 *  Dependencies: PointSET.java KdTree.java
 *
 *  Read points from a file (specified as a command-line argument) and
 *  draw to standard draw. Highlight the closest point to the mouse.
 *
 *  The nearest neighbor according to the brute-force algorithm is drawn
 *  in red; the nearest neighbor using the kd-tree algorithm is drawn in blue.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class NearestNeighborVisualizer {

    public static void main(String[] args) {

        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        // the location (x, y) of the mouse
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        Point2D query = new Point2D(0.1171875, 0.990234375);

        // draw all of the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        kdtree.draw();

        // draw in red the nearest neighbor (using brute-force algorithm)
        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.RED);
        Point2D bruteNearest = brute.nearest(query);
        Point2D kdtreeNearest = kdtree.nearest(query);
        brute.nearest(query).draw();
        StdDraw.setPenRadius(0.02);

        // draw in blue the nearest neighbor (using kd-tree algorithm)
        StdDraw.setPenColor(StdDraw.BLUE);
        kdtree.nearest(query).draw();
        StdDraw.show();
        StdDraw.pause(40);

        if (!kdtreeNearest.equals(bruteNearest)) {
            StdOut.println("Failure point " + query);
            StdOut.println("Brute-force algorithm: " + bruteNearest);
            StdOut.println("kd-tree algorithm: " + kdtreeNearest);
            StdOut.println(kdtree.contains(bruteNearest));
        }

        // while (true) {
        //
        //     // the location (x, y) of the mouse
        //     double x = StdDraw.mouseX();
        //     double y = StdDraw.mouseY();
        //     Point2D query = new Point2D(x, y);
        //
        //     // draw all of the points
        //     StdDraw.clear();
        //     StdDraw.setPenColor(StdDraw.BLACK);
        //     StdDraw.setPenRadius(0.01);
        //     kdtree.draw();
        //
        //     // draw in red the nearest neighbor (using brute-force algorithm)
        //     StdDraw.setPenRadius(0.03);
        //     StdDraw.setPenColor(StdDraw.RED);
        //     Point2D bruteNearest = brute.nearest(query);
        //     Point2D kdtreeNearest = kdtree.nearest(query);
        //     brute.nearest(query).draw();
        //     StdDraw.setPenRadius(0.02);
        //
        //     // draw in blue the nearest neighbor (using kd-tree algorithm)
        //     StdDraw.setPenColor(StdDraw.BLUE);
        //     kdtree.nearest(query).draw();
        //     StdDraw.show();
        //     StdDraw.pause(40);
        //

        // }
    }
}
