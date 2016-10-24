import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by tobias on 24.10.16.
 */
public class myClustering implements Clustering {

    @Override
    public double distance(Point instance, Point centroid) {
        return instance.distance(centroid);
    }

    @Override
    public Point centroid(Point oldCentroid, java.util.List<Point> instances) {
        if (instances == null || instances.isEmpty()) {
            return oldCentroid;
        }

        Point newCentroid = new Point();
        double nx = 0d, ny = 0d;
        instances.forEach(p -> {nx += p.getX(); ny += p.getY();});
        nx /= instances.size(); ny /= instances.size();
        newCentroid.setLocation(nx, ny);

        return newCentroid;
    }

    @Override
    public Point assign(Point instance, List<Point> centroids) {
        return null;
    }

    @Override
    public Map<Point, List<Point>> cluster(List<Point> instances, List<Point> centroids) {
        return null;
    }
}
