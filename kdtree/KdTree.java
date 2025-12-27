/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

import java.util.LinkedList;

public class KdTree {
    private static class Node {
        private Point2D point;
        private Node left;
        private Node right;
        private RectHV rect;
        private int orientaion;

        private boolean goLeft(Point2D p) {
            if (orientaion == 0) {
                return p.x() < point.x();
            }
            else {
                return p.y() < point.y();
            }
        }

        private RectHV rectForLeftChild() {
            if (orientaion == 0) {
                return new RectHV(
                        rect.xmin(), rect.ymin(), point.x(), rect.ymax()
                );
            }
            else {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
            }
        }

        private RectHV rectForRightChild() {
            if (orientaion == 0) {
                return new RectHV(
                        point.x(), rect.ymin(), rect.xmax(), rect.ymax()
                );
            }
            else {
                return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
            }
        }
    }

    private int size = 0;
    private Node root;

    public KdTree() {
        root = null;
    }                              // construct an empty set of points

    public boolean isEmpty() {
        return size == 0;
    }                    // is the set empty?

    public int size() {
        return size;
    }                // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        if (root == null) {
            root = new Node();
            root.point = p;
            root.orientaion = 0;
            root.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            size++;
            return;
        }
        if (contains(p)) return;
        Node current = root;
        Node newNode = new Node();
        newNode.point = p;
        while (current != null) {
            boolean goLeft = current.goLeft(p);
            if (current.left == null && goLeft) {
                current.left = newNode;
                newNode.orientaion = 1 - current.orientaion;
                newNode.rect = current.rectForLeftChild();
                break;
            }
            else if (current.right == null && !goLeft) {
                current.right = newNode;
                newNode.orientaion = 1 - current.orientaion;
                newNode.rect = current.rectForRightChild();
                break;
            }
            if (goLeft) current = current.left;
            else current = current.right;
        }
        size++;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        Node current = root;
        while (current != null) {
            if (current.point.equals(p)) return true;
            if (current.goLeft(p)) current = current.left;
            else current = current.right;
        }
        return false;
    }

    public void draw() {
        if (root == null) return;
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node current = stack.pop();
            if (current != null) {
                current.point.draw();
                stack.push(current.left);
                stack.push(current.right);
            }
        }
    }                  // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Point cannot be null");
        if (root == null) return new LinkedList<>();
        LinkedList<Point2D> points = new LinkedList<>();
        Node current = root;
        Stack<Node> stack = new Stack<>();
        stack.push(current);
        while (!stack.isEmpty()) {
            current = stack.pop();
            if (rect.contains(current.point)) points.add(current.point);
            if (current.left != null && current.left.rect.intersects(rect))
                stack.push(current.left);
            if (current.right != null && current.right.rect.intersects(rect))
                stack.push(current.right);
        }
        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        if (root == null) return null;
        Point2D nearest = root.point;
        Node current = root;
        Queue<Node> queue = new Queue<>();
        queue.enqueue(current);
        double lowestDist = p.distanceSquaredTo(nearest);
        while (!queue.isEmpty()) {
            current = queue.dequeue();
            if (current.point.equals(p)) return current.point;
            if (lowestDist > current.point.distanceSquaredTo(p)) {
                nearest = current.point;
                lowestDist = current.point.distanceSquaredTo(p);
            }
            boolean goLeft = current.goLeft(p);
            Node left = null, right = null;
            if (current.left != null && current.left.rect.distanceSquaredTo(p) < lowestDist) {
                left = current.left;
            }
            if (current.right != null && current.right.rect.distanceSquaredTo(p) < lowestDist) {
                right = current.right;
            }
            if (goLeft) {
                if (left != null) {
                    queue.enqueue(left);
                }
                if (right != null) queue.enqueue(right);
            }
            else {
                if (right != null) queue.enqueue(right);
                if (left != null) queue.enqueue(left);
            }
        }
        return nearest;
    }

    public static void main(String[] args) {

    }               // unit testing of the methods (optional)
}
