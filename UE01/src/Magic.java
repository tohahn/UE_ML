import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by tobias on 24.10.16.
 */
public class Magic {
    private static myClustering means = new myClustering();
    private static String[] colors = {"blue", "green", "red", "yellow", "black"};
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
    }

    private static void printResult(String name, Map<Point, List<Point>> result) throws IOException {
        BufferedWriter tex = new BufferedWriter(new FileWriter("/home/tobias/repos/tohahn/UE_ML/UE01/results/" + name + ".tex"));

        tex.write("\\documentclass{article}\n\\usepackage{tikz,pgfplots}\n\\begin{document}\n\\begin{tikzpicture}\n\\begin{axis}[scatter/classes={");
        int i = 0;
        for (Point p : result.keySet()) {
            if (i > 0) tex.write(",");
            tex.write(p.x+"_"+p.y+"c={mark=x,draw=" + colors[i%colors.length] + "},");
            tex.write(p.x+"_"+p.y+"p={mark=o,draw=" + colors[i++%colors.length] + "}");
        }
        tex.write("}]\n\\addplot[scatter,only marks,scatter src=explicit symbolic]table[meta=label] {\nx y label\n");
        for (Map.Entry<Point, List<Point>> e : result.entrySet()) {
            tex.write(e.getKey().x + " " + e.getKey().y + " " + e.getKey().x+"_"+e.getKey().y + "c\n");
            e.getValue().forEach(cP -> {
                try {
                    tex.write(cP.x + " " + cP.y + " " + e.getKey().x+"_"+e.getKey().y + "p\n");
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
}
