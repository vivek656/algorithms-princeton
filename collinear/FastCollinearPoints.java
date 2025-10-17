/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

    private Point[] sortedPoints;
    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException(
                "argument to BruteCollinearPoints constructor is null");
        this.sortedPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException(
                    "argument to Fast constructor contains null point");
            this.sortedPoints[i] = points[i];
        }
        sortPoints();
    }

    // the number of line segments
    public int numberOfSegments() {
        if (lineSegments == null) {
            generateSegments();
        }
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        if (lineSegments == null) {
            generateSegments();
        }
        return copySegments();
    }

    private void generateSegments() {
        int start = 0;
        Point[] points = Arrays.copyOf(this.sortedPoints, this.sortedPoints.length);
        ArrayList<LineSegment> seenPoints = new ArrayList<LineSegment>();
        while (start < points.length) {
            Point me = sortedPoints[start];
            Comparator<Point> so = me.slopeOrder();
            Comparator<Point> slopeOrder = (a, b) -> {
                int sslope = so.compare(a, b);
                if (sslope != 0.0) return sslope;
                else return a.compareTo(b);
            };
            Arrays.sort(points, slopeOrder);
            int len = 2;
            double checkSlope = Double.NEGATIVE_INFINITY;

            Point low = me;
            Point high = null;
            for (int i = 0; i < points.length; i++) {
                if (me.slopeTo(points[i]) == checkSlope) {
                    len++;
                    high = points[i];
                }
                else {
                    if (len >= 4 && me.compareTo(low) <= 0) {
                        seenPoints.add(new LineSegment(me, high));
                    }
                    checkSlope = me.slopeTo(points[i]);
                    low = points[i];
                    len = 2;
                }
                if (i == points.length - 1 && me.slopeTo(points[i]) == checkSlope
                        && me.compareTo(low) <= 0) {
                    if (len >= 4) {
                        seenPoints.add(new LineSegment(me, high));
                    }
                }
            }
            start++;
        }
        lineSegments = seenPoints.toArray(new LineSegment[0]);
    }

    private void sortPoints() {
        Arrays.sort(sortedPoints, Point::compareTo);
        for (int i = 1; i < sortedPoints.length; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i - 1]) == 0) {
                throw new IllegalArgumentException("duplicate points");
            }
        }
    }

    private LineSegment[] copySegments() {
        LineSegment[] copy = new LineSegment[lineSegments.length];
        for (int i = 0; i < lineSegments.length; i++) {
            copy[i] = lineSegments[i];
        }
        return copy;
    }
}