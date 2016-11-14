import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Created by tobias on 03.11.16.
 */

public class kDistance {
    public Double[] makeDiagramData(int k, final List<Point2D> points) {
        final Map<Point2D, SortedMap<Double, Point2D>> distances = new HashMap<>();
        final Set<List<Point2D>> hasDistance = new HashSet<>();
        final SortedSet<Double> epsilons = new TreeSet<>();

        points.forEach(p -> epsilons.add(getEpsilon(k, p, distances, hasDistance, points)));

        return epsilons.toArray(new Double[epsilons.size()]);
    }

    private Double getEpsilon(int k, Point2D initP, Map<Point2D, SortedMap<Double, Point2D>> distances, Set<List<Point2D>> hasDistance, List<Point2D> points) {
        points.forEach(p -> {
            if (!p.equals(initP)) {
                if (!hasDistance.contains(Arrays.asList(initP, p))) {
                    if (!distances.containsKey(initP)) {
                        distances.put(initP, new TreeMap<>());
                    }
                    if (!distances.containsKey(p)) {
                        distances.put(p, new TreeMap<>());
                    }
                    distances.get(initP).put(initP.distance(p), p);
                    distances.get(p).put(initP.distance(p), initP);
                    hasDistance.add(Arrays.asList(initP, p));
                }
            }
        });

        return (Double) distances.get(initP).keySet().toArray()[k-1];
    }
}
