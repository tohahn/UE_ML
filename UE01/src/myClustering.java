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

        instances.stream().reduce(newCentroid, (a,p) -> { a.setLocation(a.getLocation().getX() + p.getX(), a.getLocation().getY() + p.getY()); return a; });
        newCentroid.setLocation(newCentroid.getX() / instances.size(), newCentroid.getY() / instances.size());


        return newCentroid;
    }

    @Override
    public Point assign(Point instance, List<Point> centroids) {
        return centroids.stream().min((p1, p2) -> Double.compare(p1.distance(instance), p2.distance(instance))).get();
    }

    @Override
    public Map<Point, List<Point>> cluster(List<Point> instances, List<Point> centroids) {
        if (instances == null || instances.isEmpty() || centroids == null || centroids.isEmpty()) {
            return null;
        }

        Map<Point, List<Point>> retMap;
        double oldError = Double.MAX_VALUE, newError = 0d;

        do {
            oldError = newError;
            //assign
            final Map<Point, List<Point>> clusters = new HashMap<>();
            centroids.forEach(p -> clusters.put(p, new ArrayList<>()));
            List<Point> finalCentroids = centroids;
            instances.forEach(p -> clusters.get(assign(p, finalCentroids)).add(p));
            retMap = clusters;

            //centroid and error calculation
            centroids = new ArrayList<>();
            newError = 0;
            for (Map.Entry<Point, List<Point>> e : clusters.entrySet()) {
                final Point cur = centroid(e.getKey(), e.getValue());
                if (!(e.getValue() == null || e.getValue().isEmpty())) {
                    centroids.add(cur);
                    newError += e.getValue().stream().map(a -> cur.distance(a)).reduce(Double::sum).get();
                }
            }
        } while (newError != oldError);

        return retMap;
    }
}
