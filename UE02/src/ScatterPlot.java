import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Created by tobias on 03.11.16.
 */
public class ScatterPlot extends JFrame {
    private static Color[] colors = new Color[] {
            Color.BLACK,
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.ORANGE,
            Color.PINK,
            Color.RED,
            Color.YELLOW
    };

    public ScatterPlot(List<List<Point2D>> points) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 760);
        DataTable[] data = new DataTable[points.size()];
        DataSeries[] series = new DataSeries[points.size()];
        XYPlot XYPlot = new XYPlot();
        for (int x = 0; x < points.size(); x++) {
            data[x] = new DataTable(Double.class, Double.class);
            for (int y = 0; y < points.get(x).size(); y++) {
                data[x].add(points.get(x).get(y).getX(), points.get(x).get(y).getY());
            }
            series[x] = new DataSeries(x < points.size() - 1 ? "Cluster " + x : "Noise", data[x], 0, 1);

            XYPlot.add(series[x]);
            PointRenderer renderer = new DefaultPointRenderer2D();
//            renderer.setShape(new Rectangle2D.Double(-2.5,-2.5,5,5));
            renderer.setColor(colors[x % colors.length]);
            XYPlot.setPointRenderers(series[x], renderer);
        }
        getContentPane().add(new InteractivePanel(XYPlot));
        XYPlot.setLegendVisible(true);
    }
}
