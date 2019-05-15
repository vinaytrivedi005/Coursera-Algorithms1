import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

    private SET<Point2D> points;

    public PointSET() { // construct an empty set of points
        this.points = new SET<>();
    }

    public boolean isEmpty() { // is the set empty?
        return points.isEmpty();
    }

    public int size() { // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        points.add(p);
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    public void draw() { // draw all points to standard draw

    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> pts = new ArrayList<>();
        for (Point2D p : this.points) {
            if (rect.contains(p)) {
                pts.add(p);
            }
        }
        return pts;
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException();
        }

        Point2D nearest = null;
        double minDistSquared = Double.POSITIVE_INFINITY;
        double distSquared;
        for (Point2D pt : this.points) {
            distSquared = pt.distanceSquaredTo(p);
            if (distSquared < minDistSquared) {
                minDistSquared = distSquared;
                nearest = pt;
            }
        }

        return nearest;
    }

    public static void main(String[] args) { // unit testing of the methods (optional)

    }
}