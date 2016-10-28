import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by tobias on 24.10.16.
 */
public class Magic {
    private static myClustering means = new myClustering();

    public static void main(String[] args) throws IOException {
        String name = "/home/tobias/repos/tohahn/UE_ML/UE01/data/mars.csv";
        BufferedReader csv = new BufferedReader(new FileReader(name));
        csv.readLine();

        String line;
        ArrayList<Point> points = new ArrayList<>();
        while ((line = csv.readLine()) != null) {
            points.add(new Point(Integer.parseInt(line.split(",")[0]), Integer.parseInt(line.split(",")[1])));
        }
        ArrayList<Point> centroids = new ArrayList<>();
        centroids.add(new Point(10,10));
        centroids.add(new Point(20,20));
        centroids.add(new Point(30,30));

        Map<Point, java.util.List<Point>> result3 = means.cluster(points, centroids);
        result3.forEach((p, l) -> {
            try {
                printCentroid(String.valueOf((int) p.getX()), p, l);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void printCentroid(String name, Point centroid, List<Point> points) throws IOException {
        BufferedWriter csv = new BufferedWriter(new FileWriter("/home/tobias/repos/tohahn/UE_ML/UE01/results/" + name + "_p" + ".csv"));
        csv.write(points.stream().map(p -> (int) p.getX() + "," + (int) p.getY() + "\n").reduce("", (a,b) -> a+b));
        csv.flush();

        csv = new BufferedWriter(new FileWriter("/home/tobias/repos/tohahn/UE_ML/UE01/results/" + name + ".csv"));
        csv.write((int) centroid.getX() + "," + (int) centroid.getY() + "\n");
        csv.flush();
    }
}
