import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by tobias on 24.10.16.
 */
public class Magic {
    private static myClustering means = new myClustering();
    private static myVarianceClustering meansBike = new myVarianceClustering();
    private static String[] colors = {"blue", "green", "red", "yellow", "black", "purple"};
    private static String[] label = {"a", "b", "c", "d", "e", "f", "g"};

    public static void main(String[] args) throws IOException {
        List<Point> points = readData("/home/tobias/repos/tohahn/UE_ML/UE01/data/mars.csv");
        List<Point> centroids = new ArrayList<Point>();
        centroids.add(new Point(10,10));
        centroids.add(new Point(50,50));
        printResult("mars/result2_2", means.cluster(points, centroids));

        centroids.add(new Point(80,50));
        printResult("mars/result2_3", means.cluster(points, centroids));

        centroids.clear();
        centroids.add(new Point(10,10));
        centroids.add(new Point(20,20));
        centroids.add(new Point(30,30));
        printResult("mars/result3", means.cluster(points, centroids));

        points = readData("/home/tobias/repos/tohahn/UE_ML/UE01/data/mars_ordered.csv");
        centroids.clear();
        centroids.add(new Point(40,50));
        centroids.add(new Point(100,50));
        printResult("mars/result4", means.clusterOne(points, centroids));

        List<Point2D> points_bike = readDataBike("/home/tobias/repos/tohahn/UE_ML/UE01/data/fahrrad.csv");
        printResultBike("bike/results1", meansBike.cluster(points_bike, 6));

        ArrayList<Map<Point2D, List<Point2D>>> results = new ArrayList<>();
        Map<Integer, Double> cost = new HashMap<>();
        Map<Integer, Double> sil = new HashMap<>();
        double min = Double.MAX_VALUE;
        int index = -1;

        Map<Point2D, List<Point2D>> sils = new HashMap<>();
        Point2D silsC = new Point2D.Float(0,0);
        sils.put(silsC, new ArrayList<>());
        for (int i = 0; i < 19; i++) {
            results.add(meansBike.cluster(points_bike, i+2));
            cost.put(i, calculateCost(i, results.get(i)));
            sil.put(i, calcSilhoutte(results.get(i)));

            if (cost.get(i) < min) {
                min = cost.get(i);
                index = i;
            }

            sils.get(silsC).add(new Point2D.Float(sil.get(i).floatValue(), cost.get(i).floatValue()));
        }

        printResultBike("bike/result2", results.get(index));
        printResultBike("bike/result4", sils);
    }

    private static double calculateCost(int num, Map<Point2D, List<Point2D>> clustering) {
        float error = 0;
        for (Point2D c : clustering.keySet()) {
            for (Point2D p : clustering.get(c)) {
                error += c.distance(p);
            }
        }
        return 38.63767077587896/12 * num + error/2;
    }

    private static double calcSilhoutte(Map<Point2D, List<Point2D>> clustering) {
        List<Point2D> centroids = new ArrayList<>();
        List<Point2D> instances = new ArrayList<>();

        centroids.addAll(clustering.keySet());
        clustering.values().forEach(l -> instances.addAll(l));

        double sil = 0, dA, dB;
        for (Point2D p : instances) {
            Point2D tempC = assign(p, centroids);
            centroids.remove(tempC);
            Point2D tempC2 = assign(p, centroids);
            centroids.add(tempC);

            dA = tempC.distance(p);
            dB = tempC2.distance(p);

            sil += (dB - dA) / Math.max(dA, dB);
        }
        sil /= instances.size();

        return sil;
    }

    private static Point2D assign(Point2D instance, List<Point2D> centroids) {
        return centroids.stream().min((p1, p2) -> Double.compare(p1.distance(instance), p2.distance(instance))).get();
    }

    private static void printResult(String name, Map<Point, List<Point>> result) throws IOException {
        BufferedWriter tex = new BufferedWriter(new FileWriter("/home/tobias/repos/tohahn/UE_ML/UE01/results/" + name + ".tex"));

        tex.write("\\documentclass{article}\n\\usepackage{tikz,pgfplots}\n\\begin{document}\n\\begin{tikzpicture}\n\\begin{axis}[scatter/classes={");
        int i = 0;
        for (Point p : result.keySet()) {
            if (i > 0) tex.write(",");
            tex.write(p.getX()+"_"+p.getY()+"c={mark=x,draw=" + colors[i%colors.length] + "},");
            tex.write(p.getX()+"_"+p.getY()+"p={mark=o,draw=" + colors[i++%colors.length] + "}");
        }
        tex.write("}]\n\\addplot[scatter,only marks,scatter src=explicit symbolic]table[meta=label] {\nx y label\n");
        for (Map.Entry<Point, List<Point>> e : result.entrySet()) {
            tex.write(e.getKey().x + " " + e.getKey().y + " " + e.getKey().x+"_"+e.getKey().y + "c\n");
            e.getValue().forEach(cP -> {
                try {
                    tex.write(cP.getX() + " " + cP.getY() + " " + e.getKey().x+"_"+e.getKey().y + "p\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        }
        tex.write("};\n\\end{axis}\n\\end{tikzpicture}\n\\end{document}");
        tex.flush();
        tex.close();
    }

    private static void printResultBike(String name, Map<Point2D, List<Point2D>> result) throws IOException {
        BufferedWriter tex = new BufferedWriter(new FileWriter("/home/tobias/repos/tohahn/UE_ML/UE01/results/" + name + ".tex"));

        tex.write("\\documentclass{article}\n\\usepackage{tikz,pgfplots}\n\\begin{document}\n\\begin{tikzpicture}\n\\begin{axis}[scatter/classes={");
        int i = 0;
        for (Point2D p : result.keySet()) {
            if (i > 0) tex.write(",");
            tex.write(p.getX()+"_"+p.getY()+"c={mark=x,draw=" + colors[i%colors.length] + "},");
            tex.write(p.getX()+"_"+p.getY()+"p={mark=o,draw=" + colors[i++%colors.length] + "}");
        }
        tex.write("}]\n\\addplot[scatter,only marks,scatter src=explicit symbolic]table[meta=label] {\nx y label\n");
        for (Map.Entry<Point2D, List<Point2D>> e : result.entrySet()) {
            tex.write(e.getKey().getX() + " " + e.getKey().getY() + " " + e.getKey().getX()+"_"+e.getKey().getY() + "c\n");
            e.getValue().forEach(cP -> {
                try {
                    tex.write(cP.getX() + " " + cP.getY() + " " + e.getKey().getX()+"_"+e.getKey().getY() + "p\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        }
        tex.write("};\n\\end{axis}\n\\end{tikzpicture}\n\\end{document}");
        tex.flush();
        tex.close();
    }

    private static List<Point> readData(String name) throws IOException {
        BufferedReader csv = new BufferedReader(new FileReader(name));
        csv.readLine();

        String line;
        ArrayList<Point> points = new ArrayList<>();
        while ((line = csv.readLine()) != null) {
            points.add(new Point(Integer.parseInt(line.split(",")[0]), Integer.parseInt(line.split(",")[1])));
        }
        return points;
    }

    private static List<Point2D> readDataBike(String name) throws IOException {
        BufferedReader csv = new BufferedReader(new FileReader(name));
        csv.readLine();

        String line;
        ArrayList<Point2D> points = new ArrayList<>();
        while ((line = csv.readLine()) != null) {
            points.add(new Point2D.Float(Float.parseFloat(line.split(",")[1]), Float.parseFloat(line.split(",")[2])));
        }

        return points;
    }
}
