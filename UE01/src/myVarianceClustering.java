import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * Created by tobias on 29.10.16.
 */
public class myVarianceClustering {
    public double distance(Point2D.Float instance, Point2D.Float centroid) {
        return instance.distance(centroid);
    }

    public Point2D centroid(Point2D oldCentroid, java.util.List<Point2D> instances) {
        if (instances == null || instances.isEmpty()) {
            return oldCentroid;
        }

        Point2D newCentroid = new Point2D.Float();

        instances.stream().reduce(newCentroid, (a,p) -> { a.setLocation(a.getX() + p.getX(), a.getY() + p.getY()); return a; });
        newCentroid.setLocation(newCentroid.getX() / instances.size(), newCentroid.getY() / instances.size());


        return newCentroid;
    }

    public Point2D assign(Point2D instance, List<Point2D> centroids) {
        return centroids.stream().min((p1, p2) -> Double.compare(p1.distance(instance), p2.distance(instance))).get();
    }

    public Map<Point2D, List<Point2D>> cluster(List<Point2D> instances, List<Point2D> centroids) {
        return null;
    }
}
