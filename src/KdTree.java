import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree
    }

    public KdTree() { // construct an empty set of points
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() { // is the set empty?
        return size == 0;
    }

    public int size() { // number of points in the set
        return this.size;
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (!contains(p)) {
            addNode(p);
            size++;
        }
    }

    private void addNode(Point2D p) {

        if (root == null) {
            root = new Node();
            root.p = p;
            root.rect = new RectHV(0, 0, 1, 1);
        } else {
            Node n = root;
            boolean h = true;
            while (true) {
                if (h) {
                    if (n.p.x() > p.x() && n.lb == null) {
                        n.lb = new Node();
                        n.lb.p = p;
                        n.lb.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.p.x(), n.rect.ymax());
                        break;
                    } else if (n.p.x() > p.x() && n.lb != null) {
                        n = n.lb;
                    } else if (n.p.x() <= p.x() && n.rt == null) {
                        n.rt = new Node();
                        n.rt.p = p;
                        n.rt.rect = new RectHV(n.p.x(), n.rect.ymin(), n.rect.xmax(), n.rect.ymax());
                        break;
                    } else if (n.p.x() <= p.x() && n.rt != null) {
                        n = n.rt;
                    }
                    h = false;
                } else {
                    if (n.p.y() > p.y() && n.lb == null) {
                        n.lb = new Node();
                        n.lb.p = p;
                        n.lb.rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.p.y());
                        break;
                    } else if (n.p.y() > p.y() && n.lb != null) {
                        n = n.lb;
                    } else if (n.p.y() <= p.y() && n.rt == null) {
                        n.rt = new Node();
                        n.rt.p = p;
                        n.rt.rect = new RectHV(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.rect.ymax());
                        break;
                    } else if (n.p.y() <= p.y() && n.rt != null) {
                        n = n.rt;
                    }
                    h = true;
                }
            }
        }
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (root == null) {
            return false;
        }
        Node n = root;
        boolean h = true;
        while (n != null) {
            if (n.p.equals(p)) {
                return true;
            }

            if (h) {
                if (n.p.x() > p.x()) {
                    n = n.lb;
                } else {
                    n = n.rt;
                }
                h = false;
            } else {
                if (n.p.y() > p.y()) {
                    n = n.lb;
                } else {
                    n = n.rt;
                }
                h = true;
            }
        }

        return false;
    }

    public void draw() { // draw all points to standard draw

        if (root == null) {
            return;
        }

        Queue<Node> nodes = new Queue<KdTree.Node>();
        nodes.enqueue(root);
        Queue<Boolean> hvs = new Queue<Boolean>();
        hvs.enqueue(false);
        // StdDraw.setPenRadius(0.01);
        // StdDraw.rectangle(0.5, 0.5, 0.5, 0.5);
        while (!nodes.isEmpty()) {
            Node n = nodes.dequeue();
            Boolean h = hvs.dequeue();

            if (n.lb != null) {
                nodes.enqueue(n.lb);
                if (h) {
                    hvs.enqueue(false);
                } else {
                    hvs.enqueue(true);
                }
            }

            if (n.rt != null) {
                nodes.enqueue(n.rt);
                if (h) {
                    hvs.enqueue(false);
                } else {
                    hvs.enqueue(true);
                }
            }

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(n.p.x(), n.p.y());

            StdDraw.setPenRadius();

            if (h) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
            }

            else {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
            }

        }
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        if (root == null) {
            return null;
        }

        Queue<Node> nodes = new Queue<KdTree.Node>();
        nodes.enqueue(root);
        List<Point2D> pts = new ArrayList<Point2D>();
        while (!nodes.isEmpty()) {
            Node n = nodes.dequeue();
            if (rect.contains(n.p)) {
                pts.add(n.p);
            }

            if (rect.intersects(n.rect)) {
                if (n.lb != null && rect.intersects(n.lb.rect)) {
                    nodes.enqueue(n.lb);
                }
                if (n.rt != null && rect.intersects(n.rt.rect)) {
                    nodes.enqueue(n.rt);
                }
            }
        }

        return pts;
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (root == null) {
            return null;
        }

        Queue<Node> nodes = new Queue<KdTree.Node>();
        nodes.enqueue(root);

        Point2D nearest = null;

        while (!nodes.isEmpty()) {

            Node n = nodes.dequeue();
            if (nearest == null) {
                nearest = n.p;
            } else {
                if (p.distanceSquaredTo(nearest) > p.distanceSquaredTo(n.p)) {
                    nearest = n.p;
                }
            }
            if (n.lb != null && n.lb.rect.distanceSquaredTo(p) <= p.distanceSquaredTo(nearest)) {
                nodes.enqueue(n.lb);
            }

            if (n.rt != null && n.rt.rect.distanceSquaredTo(p) <= p.distanceSquaredTo(nearest)) {
                nodes.enqueue(n.rt);
            }
        }

        return nearest;
    }

    public static void main(String[] args) { // unit testing of the methods (optional)
        KdTree kdTree = new KdTree();
        /*
         * 
         * insert (0.7, 0.2) insert (0.5, 0.4) insert (0.2, 0.3) insert (0.4, 0.7)
         * insert (0.9, 0.6)
         */
        Point2D p1 = new Point2D(0.7, 0.2);
        Point2D p2 = new Point2D(0.5, 0.4);
        Point2D p3 = new Point2D(0.2, 0.3);
        Point2D p4 = new Point2D(0.4, 0.7);
        Point2D p5 = new Point2D(0.9, 0.6);

        kdTree.insert(p1);
        kdTree.insert(p2);
        kdTree.insert(p3);
        kdTree.insert(p4);
        kdTree.insert(p5);

        // System.out.println(kdTree.size());

        Point2D testPoint1 = new Point2D(0.235, 0.643);
        System.out.println("nearest: " + kdTree.nearest(testPoint1));

//        RectHV testRect1 = new RectHV(0.15, 0.15, 0.75, 0.45);
//        Iterable<Point2D> pts = kdTree.range(testRect1);

//        for (Point2D p : pts) {
//            System.out.println("in range: " + p);
//        }
//        kdTree.draw();
    }
}
