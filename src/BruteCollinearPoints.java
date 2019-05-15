import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final ArrayList<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
        if (points == null) {
            throw new IllegalArgumentException();
        }

        validatePoints(points);

        this.segments = new ArrayList<LineSegment>();

        Point[] pts = points.clone();
        Arrays.sort(pts);

        int n = pts.length;

        for (int i = 0; i < n - 3; i++) {
            Point p = pts[i];
            for (int j = i + 1; j < n - 2; j++) {
                Point q = pts[j];
                for (int k = j + 1; k < n - 1; k++) {
                    Point r = pts[k];
                    if (p.slopeTo(q) == p.slopeTo(r)) {
                        for (int l = k + 1; l < n; l++) {
                            Point s = pts[l];
                            if (p.slopeTo(q) == p.slopeTo(s)) {
                                segments.add(new LineSegment(p, s));
                            }
                        }
                    }
                }
            }
        }
    }

    private void validatePoints(Point[] points) {
        int n = points.length;
        
        for (int i=0; i<n-1; i++) {
            validatePoint(points[i]);
            for (int j=i+1; j<n; j++) {
                validatePoint(points[j]);
                if( points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private void validatePoint(Point point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }
    }
    
    public int numberOfSegments() { // the number of line segments
        return this.segments.size();
    }

    public LineSegment[] segments() { // the line segments
        return this.segments.toArray(new LineSegment[segments.size()]);
    }
}