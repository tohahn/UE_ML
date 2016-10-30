import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tobias on 24.10.16.
 */
public class myClustering implements Clustering {

    @Override
    public double distance(Point instance, Point centroid) {
        return instance.distance(centroid); //just uses the distance function built into point
    }

    @Override
    public Point centroid(Point oldCentroid, java.util.List<Point> instances) {
        if (instances == null || instances.isEmpty()) { //if there are no points, return old centroid
            return oldCentroid;
        }

        Point newCentroid = new Point();

        //this is a collector function that adds x and y from all points. in the end we divide through the number of point sto get mean
        instances.stream().reduce(newCentroid, (a,p) -> { a.setLocation(a.getLocation().getX() + p.getX(), a.getLocation().getY() + p.getY()); return a; });
        newCentroid.setLocation(newCentroid.getX() / instances.size(), newCentroid.getY() / instances.size());


        return newCentroid;
    }

    @Override
    public Point assign(Point instance, List<Point> centroids) {
        // finds the centroid with the least distance with a java 8 expression
        return centroids.stream().min((p1, p2) -> Double.compare(p1.distance(instance), p2.distance(instance))).get();
    }

    @Override
    public Map<Point, List<Point>> cluster(List<Point> instances, List<Point> centroids) {
        // if we get bad input, return null
        if (instances == null || instances.isEmpty() || centroids == null || centroids.isEmpty()) {
            return null;
        }

        //assign - this is the assign step for the first time
        final Map<Point, List<Point>> clusters = new HashMap<>();
        centroids.forEach(p -> clusters.put(p, new ArrayList<>()));
        doClustering(instances, centroids, clusters);   //here we call the recursive clustering funciton

        return clusters;
    }

    public Map<Point, List<Point>> clusterOne(List<Point> instances, List<Point> centroids) {
        if (instances == null || instances.isEmpty() || centroids == null || centroids.isEmpty()) {
            return null;
        }

        //assign
        final Map<Point, List<Point>> clusters = new HashMap<>();
        centroids.forEach(p -> clusters.put(p, new ArrayList<>()));

        //do one step of clustering
        for (Point cP : instances) {
            Point newCentroid = assign(cP, centroids);
            Point oldCentroid = null;
            //gets the old centroid
            for (Map.Entry<Point, List<Point>> e : clusters.entrySet()) {
                if (e.getValue().contains(cP)) {
                    oldCentroid = e.getKey();
                }
            }
            //if the cnetroids change, change in datastructure
            if (!newCentroid.equals(oldCentroid)) {
                clusters.get(newCentroid).add(cP);
                recalculateCentroid(newCentroid, centroids, clusters);
                if (oldCentroid != null) {
                    clusters.get(oldCentroid).remove(cP);
                    recalculateCentroid(oldCentroid, centroids, clusters);
                }
                break;
            }
        }

        return clusters;
    }

    private void doClustering(List<Point> instances, List<Point> centroids, Map<Point, List<Point>> clusters) {
       for (Point cP : instances) {
           Point newCentroid = assign(cP, centroids);
           Point oldCentroid = null;
           //gets the old centroid
           for (Map.Entry<Point, List<Point>> e : clusters.entrySet()) {
               if (e.getValue().contains(cP)) {
                   oldCentroid = e.getKey();
               }
           }
           //if centroid changed, change in data structrue
           if (!newCentroid.equals(oldCentroid)) {
               clusters.get(newCentroid).add(cP);
               recalculateCentroid(newCentroid, centroids, clusters);
               if (oldCentroid != null) {
                   clusters.get(oldCentroid).remove(cP);
                   recalculateCentroid(oldCentroid, centroids, clusters);
               }
           }
       }
    }

    private void recalculateCentroid(Point oldCentroid, List<Point> centroids, Map<Point, List<Point>> clusters) {
        //recalculates the centroids - changes data structure accordingly
        Point newCentroid = centroid(oldCentroid, clusters.get(oldCentroid));
        centroids.remove(oldCentroid);
        centroids.add(newCentroid);
        clusters.put(newCentroid, clusters.get(oldCentroid));
        if (oldCentroid.hashCode() != newCentroid.hashCode()) {
            clusters.remove(oldCentroid);
        }
    }
}
