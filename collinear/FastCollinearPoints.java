/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

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

    private static boolean isCollinear(Point[] line1, Point[] line2) {
        Point p1 = line1[0];
        Point p2 = line1[1];
        Point p3 = line2[0];
        Point p4 = line2[1];
        Comparator<Point> slopeorder = p1.slopeOrder();
        if (p1.slopeTo(p2) != p3.slopeTo(p4)) return false;
        if (slopeorder.compare(p2, p4) != 0) return false;
        return true;
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
        Point[] points = new Point[sortedPoints.length];
        Point[] aux = new Point[sortedPoints.length];
        Point[][] listAndAux = new Point[2][sortedPoints.length];
        for (int i = 0; i < points.length; i++) {
            points[i] = sortedPoints[i];
            aux[i] = points[i];
        }
        listAndAux[0] = points;
        listAndAux[1] = aux;
        Point[][] seenPoints = new Point[points.length][];
        int seen = 0;
        while (start < points.length) {
            if (start + 3 >= points.length) break;
            sortAfterWithSlope(start, listAndAux);
            points = listAndAux[0];
            aux = listAndAux[1];
            int len = 2;
            double checkSlope = points[start].slopeTo(points[start + 1]);
            for (int i = start + 2; i < points.length; i++) {
                if (points[start].slopeTo(points[i]) == checkSlope) {
                    len++;
                }
                else {
                    if (len >= 4) {
                        seen = mergeSeen(seenPoints, seen,
                                         new Point[] { points[start], points[i - 1] });

                    }
                    checkSlope = points[start].slopeTo(points[i]);
                    len = 2;
                }
                if (i == points.length - 1 && points[start].slopeTo(points[i]) == checkSlope) {
                    if (len >= 4) {
                        seen = mergeSeen(seenPoints, seen,
                                         new Point[] { points[start], points[i] });
                    }
                }
                if (seen >= seenPoints.length - 2) {
                    Point[][] temp = new Point[seenPoints.length * 2][2];
                    for (int j = 0; j < seenPoints.length; j++) {
                        temp[j] = seenPoints[j];
                    }
                    seenPoints = temp;
                }
            }

            start++;
            if (start < points.length) {
                for (int i = 0; i < sortedPoints.length; i++) {
                    points[i] = sortedPoints[i];
                    aux[i] = points[i];
                }
            }
        }

        lineSegments = new LineSegment[seen];
        for (int i = 0; i < seen; i++) {
            lineSegments[i] = new LineSegment(seenPoints[i][0], seenPoints[i][1]);
        }
    }

    private int mergeSeen(
            Point[][] segments, int length, Point[] seen
    ) {
        if (length == 0) {
            segments[length] = seen;
            return 1;
        }
        Point[] maxEnd = segments[length - 1];
        double maxComp = maxEnd[1].compareTo(seen[1]);

        if (maxComp < 0) {
            segments[length] = seen;
            return length + 1;
        }


        double seenSlope = seen[0].slopeTo(seen[1]);
        int low = 0;
        int high = length - 1;
        int replaceIndex = -1;

        while (low < high) {
            int mid = low + (high - low) / 2;
            double maxCompMid = seen[1].compareTo(segments[mid][1]);
            if (maxCompMid > 0) {
                low = mid + 1;
            }
            else if (maxCompMid < 0) {
                high = mid;
            }
            else {
                double midSlopeDiff = seenSlope - segments[mid][0].slopeTo(segments[mid][1]);
                if (midSlopeDiff < 0) {
                    high = mid;
                }
                else if (midSlopeDiff > 0) {
                    low = mid + 1;
                }
                else {
                    replaceIndex = mid;
                    break;
                }
            }
        }
        if (replaceIndex == -1 && low == high && isCollinear(segments[low], seen)) {
            replaceIndex = low;
        }
        if (replaceIndex != -1) {
            segments[replaceIndex] = mergeIntoOneBigLine(segments[replaceIndex], seen);
            return length;
        }


        int insertAt;
        double maxCompMid = seen[1].compareTo(segments[low][1]);
        if (maxCompMid > 0) {
            insertAt = low + 1;
        }
        else if (maxCompMid < 0) {
            insertAt = low;
        }
        else {
            double midSlopeDiff = seenSlope - segments[low][0].slopeTo(segments[low][1]);
            if (midSlopeDiff < 0) {
                insertAt = low;
            }
            else {
                insertAt = low + 1;
            }
        }
        for (int i = length - 1; i >= insertAt; i--) {
            segments[i + 1] = segments[i];
        }
        segments[insertAt] = seen;
        return length + 1;
    }


    private void sortPoints() {
        Point[][] listAndAux = new Point[][] { sortedPoints, new Point[sortedPoints.length] };
        sortT(listAndAux, 0, Point::compareTo);
        sortedPoints = listAndAux[0];
        for (int i = 1; i < sortedPoints.length; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i - 1]) == 0) {
                throw new IllegalArgumentException("duplicate points");
            }
        }
    }

    private <T> void sortT(T[][] listAndAux, int start, Comparator<T> comparator) {
        T[] current = listAndAux[0];
        T[] auxilary = listAndAux[1];
        T[] temp;
        int starLength = 2;
        //
        while (starLength / 2 <= current.length) {
            for (int i = start; i < current.length; i += starLength) {
                int low = i;
                int middle = i + starLength / 2;
                int right = middle;
                int end = (middle + starLength / 2) >= current.length ? current.length :
                          middle + starLength / 2;
                for (int j = low; j < end; j++) {
                    if (low >= middle) {
                        auxilary[j] = current[right];
                        right++;
                        continue;
                    }
                    else if (right >= end) {
                        auxilary[j] = current[low];
                        low++;
                        continue;
                    }
                    int c = comparator.compare(current[low], current[right]);
                    if (c <= 0) {
                        auxilary[j] = current[low];
                        low++;
                    }
                    else {
                        auxilary[j] = current[right];
                        right++;
                    }
                }
            }
            starLength *= 2;
            temp = current;
            current = auxilary;
            auxilary = temp;
        }
        // 0-32
        listAndAux[0] = current;
        listAndAux[1] = auxilary;
    }

    private void sortAfterWithSlope(int start, Point[][] listAndAux) {
        Comparator<Point> slopeOrder = listAndAux[0][start].slopeOrder();
        sortT(listAndAux, start + 1, slopeOrder);
    }


    private Point[] mergeIntoOneBigLine(Point[] l1, Point[] l2) {
        Point start = l1[0].compareTo(l2[0]) < 0 ? l1[0] : l2[0];
        Point end = l1[1].compareTo(l2[1]) > 0 ? l1[1] : l2[1];
        return new Point[] { start, end };
    }

    private LineSegment[] copySegments() {
        LineSegment[] copy = new LineSegment[lineSegments.length];
        for (int i = 0; i < lineSegments.length; i++) {
            copy[i] = lineSegments[i];
        }
        return copy;
    }
}