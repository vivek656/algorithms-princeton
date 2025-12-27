import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PointSET {

    private TreeSet<Point2D> points;

    public PointSET() {
        points = new TreeSet<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        points.add(p);
    }          // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        return points.contains(p);
    }       // does the set contain point p?

    public void draw() {
        points.forEach(Point2D::draw);
    }                  // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Point cannot be null");
        return points.stream().filter(rect::contains).collect(Collectors.toList());
    }          // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        if (points.isEmpty()) return null;
        return points.stream().min(Comparator.comparingDouble(p::distanceSquaredTo)).get();
    }

    public static void main(String[] args) {

    }               // unit testing of the methods (optional)
}
