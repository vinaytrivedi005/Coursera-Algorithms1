import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] points) { // finds all line segments containing 4 or more points
        if (points == null) {
            throw new IllegalArgumentException();
        }

        validatePoints(points);

        this.segments = new ArrayList<LineSegment>();

        Point[] pointsCloned = points.clone();
        Arrays.sort(pointsCloned);
        int n = points.length;

        for (int i = 0; i < n - 3; i++) {

            Point[] pts = Arrays.copyOfRange(pointsCloned, i, points.length);
            Arrays.sort(pts, pts[0].slopeOrder());

            int startIndex = 1;
            int endIndex = 2;

            for (; endIndex < pts.length; endIndex++) {
                while (endIndex < pts.length && pts[0].slopeTo(pts[startIndex]) == pts[0].slopeTo(pts[endIndex])) {
                    endIndex++;
                }
                if (endIndex - startIndex >= 3) {
                    segments.add(new LineSegment(pts[0], pts[endIndex - 1]));
                }
                startIndex = endIndex;
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
