import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;

import javax.swing.*;
import java.awt.*;
import java.util.SortedSet;

/**
 * Created by tobias on 03.11.16.
 */
public class LinePlot extends JFrame {
    public LinePlot(Double[] values) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 760);

        DataTable data = new DataTable(Integer.class, Double.class);
        for (int x = 0; x < values.length; x++) {
            data.add(x, values[values.length - x - 1]);
        }
        XYPlot plot = new XYPlot(data);
        getContentPane().add(new InteractivePanel(plot));
        LineRenderer lines = new DefaultLineRenderer2D();
        plot.setLineRenderers(data, lines);
        Color color = new Color(0.0f, 0.3f, 1.0f);
        plot.getPointRenderers(data).get(0).setColor(color);
        plot.getLineRenderers(data).get(0).setColor(color);
    }
}
