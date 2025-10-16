/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.LinkedBag;

import java.util.Comparator;

public class BruteCollinearPoints {
    private static final Comparator<Point[]> SLOPE_COLLINEAR_COMPARE = (a, b) -> {
        double slope1 = a[1].slopeTo(a[0]);
        double slope2 = b[1].slopeTo(b[0]);
        if (slope1 != slope2) return Double.compare(slope1, slope2);
        if (isCollinear(a, b)) {
            return 0;
        }
        else return a[1].compareTo(b[0]);
    };

    private Point[] points;
    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException(
                "argument to BruteCollinearPoints constructor is null");
        this.points = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException(
                    "argument to Fast constructor contains null point");
            this.points[i] = points[i];
        }
        sort();
    }

    private static boolean isCollinear(Point[] line1, Point[] line2) {
        Point p1 = line1[0];
        Point p2 = line1[1];
        Point p3 = line2[0];
        Point p4 = line2[1];
        if (p1.slopeTo(p2) != p3.slopeTo(p4)) return false;
        if (p1.compareTo(p3) == 0 || p1.compareTo(p4) == 0
                || p2.compareTo(p4) == 0 || p2.compareTo(p3) == 0
        ) return true;
        Comparator<Point> slopeorder = p1.slopeOrder();
        if (slopeorder.compare(p2, p3) != 0) return false;
        if (slopeorder.compare(p2, p4) != 0) return false;
        return true;
    }

    // the number of line segments
    public int numberOfSegments() {
        if (lineSegments == null) {
            performCheck();
        }
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        if (lineSegments == null) {
            performCheck();
        }
        return copySegments();
    }

    private void performCheck() {
        LinkedBag<Point[]> lines = new LinkedBag<>();

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (equals(points[i], points[j])) continue;
                double slope = points[i].slopeTo(points[j]);

                for (int k = j + 1; k < points.length; k++) {
                    if (equals(points[j], points[i]) || equals(points[k], points[j])) continue;
                    if (slope != points[i].slopeTo(points[k])) continue;

                    for (int m = k + 1; m < points.length; m++) {
                        if (equals(points[k], points[m]) || equals(points[i], points[m])
                                || equals(points[j], points[m])) continue;
                        if (slope == points[i].slopeTo(points[m])) {
                            lines.add(new Point[] { points[i], points[m] });
                        }
                    }
                }
            }
        }
        int k = 0;
        Point[][] segments = new Point[lines.size()][2];
        for (Point[] line : lines) {
            segments[k++] = line;
        }
        removeDuplicatesInLineSegmentsAndSave(segments);
    }

    private void sort() {
        sortT(Point::compareTo, points);
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                throw new IllegalArgumentException("dupliacte point");
            }
        }
    }

    private <T> void sortT(Comparator<T> comparator, T[] list) {
        int shellInt = 1;
        while (shellInt < list.length) {
            shellInt = shellInt * 3 + 1;
        }
        while (shellInt > 0) {
            for (int i = shellInt; i < list.length; i++) {
                int comp = comparator.compare(list[i], list[i - shellInt]);
                if (comp < 0) {
                    swap(list, i, i - shellInt);
                    int j = i - shellInt;
                    while (j > 0 && comparator.compare(list[j], list[j - 1]) < 0) {
                        swap(list, j, j - 1);
                        j = j - 1;
                    }
                }
            }
            shellInt = (shellInt - 1) / 3;
        }
    }

    private boolean equals(Point p1, Point p2) {
        return p1.compareTo(p2) == 0;
    }

    private <T> void swap(T[] ps, int i, int j) {
        T temp = ps[i];
        ps[i] = ps[j];
        ps[j] = temp;
    }

    private Point[] mergeIntoOneBigLine(Point[] l1, Point[] l2) {
        Point start = l1[0].compareTo(l2[0]) < 0 ? l1[0] : l2[0];
        Point end = l1[1].compareTo(l2[1]) > 0 ? l1[1] : l2[1];
        return new Point[] { start, end };
    }

    private void transformToOrder(Point[][] segments) {
        for (int i = 0; i < segments.length; i++) {
            if (segments[i][0].compareTo(segments[i][1]) == 0) continue;
            if (segments[i][0].compareTo(segments[i][1]) > 0) {
                Point start = segments[i][0];
                segments[i][0] = segments[i][1];
                segments[i][1] = start;
            }
        }
    }

    private void removeDuplicatesInLineSegmentsAndSave(Point[][] segments) {
        if (segments.length < 2) {
            lineSegments = new LineSegment[segments.length];
            for (int i = 0; i < segments.length; i++) {
                lineSegments[i] = new LineSegment(segments[i][0], segments[i][1]);
            }
            return;
        }
        transformToOrder(segments);
        sortT((a, b) -> a[0].compareTo(b[0]) + a[1].compareTo(b[1]), segments);
        sortT(SLOPE_COLLINEAR_COMPARE, segments);
        int numSegments = 0;
        for (int i = 0; i < segments.length; i++) {
            if (segments[i] == null) continue;
            numSegments++;
            for (int j = i + 1; j < segments.length; j++) {
                if (segments[j] == null) continue;
                if (isCollinear(segments[i], segments[j])) {
                    segments[i] = mergeIntoOneBigLine(segments[i], segments[j]);
                    segments[j] = null;
                }
            }
        }
        lineSegments = new LineSegment[numSegments];
        int k = 0;
        for (int i = 0; i < segments.length; i++) {
            if (segments[i] == null) continue;
            lineSegments[k++] = new LineSegment(segments[i][0], segments[i][1]);
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
