import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tobias on 03.11.16.
 */
public class Magic {
    public static void main(String[] args) throws IOException {
        kDistance d = new kDistance();
        List<Point2D> points = readData("/home/tobias/repos/tohahn/UE_ML/UE02/data/fahrrad.csv");

//        for (int i = 3; i < 4; i++) {
//            LinePlot plot = new LinePlot(d.makeDiagramData(i, points));
//            plot.setTitle("k="+i);
//            plot.setVisible(true);
//        }
//        Double[] epsi = d.makeDiagramData(3, points);
//        System.out.println(epsi[epsi.length - 28]);
//        System.out.println(epsi[epsi.length - 29]);
//        System.out.print(epsi[epsi.length - 30]);
        DBScan scan = new DBScan();
        List<List<Point2D>> clusters = scan.clustering(4, 0.012, points);
        ScatterPlot plot = new ScatterPlot(clusters);
        plot.setVisible(true);

        Map<Point2D, List<Point2D>> cost = new HashMap<>();
        for (int i = 0; i < clusters.size() - 1; i++) {
            cost.put(scan.centroid(null, clusters.get(i)), clusters.get(i));
        }

        System.out.printf("KENNZAHLEN\nAnzahl Cluster: %d\nKosten für el Jefe: %f\nAnzahl der Noise-Punkte: %d\nSilhouttenkoeffizient: %f\n", clusters.size()-1, calculateCost(clusters.size() - 1, cost), clusters.get(clusters.size()-1).size(), scan.calcSilhoutte(cost));
    }

    private static double calculateCost(int num, Map<Point2D, List<Point2D>> clustering) {
        //calculates cost in gummi coins
        float error = 0;
        for (Point2D c : clustering.keySet()) {
            for (Point2D p : clustering.get(c)) {
                error += c.distance(p);
            }
        }
        return 38.63767077587896/12 * num + error/2;
    }

    private static List<Point2D> readData(String name) throws IOException {
        //reads our data from a csv file
        BufferedReader csv = new BufferedReader(new FileReader(name));
        csv.readLine();

        String line;
        ArrayList<Point2D> points = new ArrayList<>();
        while ((line = csv.readLine()) != null) {
            points.add(new Point2D.Double(Double.parseDouble(line.split(",")[1]), Double.parseDouble(line.split(",")[2])));
        }
        return points;
    }
}
