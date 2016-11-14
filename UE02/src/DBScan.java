import java.awt.geom.Point2D;
import java.util.*;

/**
 * Created by tobias on 07.11.16.
 */
public class DBScan {
    public List<List<Point2D>> clustering(int minPts, double epsilon, List<Point2D> points) {
        List<Point2D> objects = new ArrayList<>();
        List<Point2D> noise = new ArrayList<>();
        List<List<Point2D>> clusters = new ArrayList<>();
        objects.addAll(points);

        while (!objects.isEmpty()) {
            Point2D currentPoint = objects.get(0);
            List<Point2D> cluster = getCluster(points, objects, currentPoint, minPts, epsilon);
            if (cluster == null) {
                noise.add(currentPoint);
            } else {
                clusters.add(cluster);
            }
        }
        clusters.add(noise);

        return clusters;
    }

    private List<Point2D> getCluster(List<Point2D> points, List<Point2D> objects, Point2D currentPoint, int minPts, double epsilon) {
        Set<Point2D> seeds = getSeeds(points, objects, currentPoint, minPts, epsilon);
        List<Point2D> cluster = new ArrayList<>();

        if (seeds == null) {
            objects.remove(currentPoint);
            return null;
        } else {
            cluster.add(currentPoint);
            objects.remove(currentPoint);

            while (!seeds.isEmpty()) {
                Iterator<Point2D> iter = seeds.iterator();
                currentPoint = iter.next();
                iter.remove();
                objects.remove(currentPoint);
                cluster.add(currentPoint);

                Set<Point2D> newSeeds = getSeeds(points,objects,currentPoint,minPts,epsilon);
                if (newSeeds != null) {
                    seeds.addAll(newSeeds);
                }

                seeds.remove(currentPoint);
            }

            return cluster;
        }
    }

    public Point2D centroid(Point2D oldCentroid, java.util.List<Point2D> instances) {
        if (instances == null || instances.isEmpty()) { //if we have no points, return old centroid
            return oldCentroid;
        }

        Point2D newCentroid = new Point2D.Float();

        //java 8 expression to calculate new centroid
        instances.stream().reduce(newCentroid, (a,p) -> { a.setLocation(a.getX() + p.getX(), a.getY() + p.getY()); return a; });
        newCentroid.setLocation(newCentroid.getX() / instances.size(), newCentroid.getY() / instances.size());


        return newCentroid;
    }

    private Set<Point2D> getSeeds(List<Point2D> points, List<Point2D> objects, Point2D currentPoint, int minPts, double epsilon) {
        List<Point2D> neighbors = new ArrayList<>();
        Set<Point2D> seeds = new HashSet<>();

        points.forEach(p -> {
            if (!p.equals(currentPoint)) {
                if (p.distance(currentPoint) < epsilon) {
                    neighbors.add(p);
                }
            }
        });
        if (neighbors.size() >= minPts) {
            neighbors.forEach(p -> {
                if (objects.contains(p)) {
                    seeds.add(p);
                }
            });
        } else {
            return null;
        }

        return seeds;
    }

    public double calcSilhoutte(Map<Point2D, List<Point2D>> clustering) {
        //calculates the silhoutte factor
        List<Point2D> centroids = new ArrayList<>();
        List<Point2D> instances = new ArrayList<>();

        centroids.addAll(clustering.keySet());
        clustering.values().forEach(l -> instances.addAll(l));

        double sil = 0, dA, dB;
        for (Point2D p : instances) {
            //gets the two nearest centroids
            Point2D tempC = assign(p, centroids);
            centroids.remove(tempC);
            Point2D tempC2 = assign(p, centroids);
            centroids.add(tempC);

            //calculates distance to them
            dA = tempC.distance(p);
            dB = tempC2.distance(p);

            //adds silhoutte factor to counter
            sil += (dB - dA) / Math.max(dA, dB);
        }
        //average the factor
        sil /= instances.size();

        return sil;
    }

    public Point2D assign(Point2D instance, List<Point2D> centroids) {
        //gets centroid with least distance
        return centroids.stream().min((p1, p2) -> Double.compare(p1.distance(instance), p2.distance(instance))).get();
    }
}
