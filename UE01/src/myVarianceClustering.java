import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tobias on 29.10.16.
 */
public class myVarianceClustering {
    public double distance(Point2D instance, Point2D centroid) {
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

    public Map<Point2D, List<Point2D>> cluster(List<Point2D> instances, int number) {
        List<Point2D> centroids = getInitialCentroids(instances, number);
        final Map<Point2D, List<Point2D>> clusters = new HashMap<>();
        centroids.forEach(c -> {
           clusters.put(c, new ArrayList<>());
        });
        double oldError = Double.MAX_VALUE, newError = 0;

        while (oldError != newError) {
            oldError = newError;
            centroids.forEach(c -> {
                clusters.remove(c);
                clusters.put(c, new ArrayList<>());
            });
            instances.forEach(p -> clusters.get(assign(p, centroids)).add(p));

            Map<Point2D, List<Point2D>> temp = new HashMap<>();
            temp.putAll(clusters);
            for (Map.Entry<Point2D, List<Point2D>> e : temp.entrySet()) {
                Point2D newCentroid = centroid(e.getKey(), e.getValue());
                if (!newCentroid.equals(e.getKey())) {
                    List<Point2D> tempList = clusters.get(e.getKey());
                    clusters.remove(e.getKey());
                    centroids.remove(e.getKey());
                    clusters.put(newCentroid, tempList);
                    centroids.add(newCentroid);
                }
            }
            newError = 0;
            for (Point2D c : centroids) {
               for (Point2D p : clusters.get(c)) {
                   newError += distance(c,p);
               }
            }
        }
        return clusters;
    }

    private List<Point2D> getInitialCentroids(List<Point2D> instances, int number) {
        Map<Integer, List<Point2D>> assign = new HashMap<>();

        for (int i = 0; i < number; i++) {
            assign.put(i, new ArrayList<>());
        }
        for (int i = 0; i < instances.size(); i++) {
            assign.get(i%number).add(instances.get(i));
        }

        return assign.values().stream().map(l -> centroid(null, l)).collect(Collectors.toList());
    }
}
