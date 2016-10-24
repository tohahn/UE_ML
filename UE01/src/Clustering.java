import java.awt.Point;
import java.util.List;
import java.util.Map;

public interface Clustering {

	/**
	 * Berechnet den Abstand zwischen einem beliebigen Punkt und einem Centroid.
	 * 
	 * @param instance
	 *            der Punkt
	 * @param centroid
	 *            der Centroid
	 * @return Abstand zwischen Punkt und Centroid
	 */
	public double distance(Point instance, Point centroid);

	/**
	 * Berechnet den Centroid eines Clusters aus Punkten. Falls dem Centroid
	 * keine Punkte zugeordnet sind bleibt der Centroid unverändert.
	 * 
	 * @param oldCentroid
	 *            der alte Centroid
	 * @param instances
	 *            die Punkte
	 * @return der Centroid
	 */
	public Point centroid(Point oldCentroid, List<Point> instances);

	/**
	 * Liefert den nächsten Centroid zu einem Punkt
	 * 
	 * @param instance
	 *            der Punkt
	 * @param centroids
	 *            die möglichen Centroide
	 * @return der nächste Centroid am Punkt
	 */
	public Point assign(Point instance, List<Point> centroids);

	/**
	 * Implementierung des Clustering-Algorithmus (bspw. k-Means). Weisst allen
	 * Punkten einen Centroid zu. Das Ergnis ist eine Liste der endgültigen
	 * Centroide, sowie deren zugehörige Punkte.
	 * 
	 * @param instances
	 *            die Punkte
	 * @param centroids
	 *            die Centroide
	 * @return Centroide und deren zugewiesene Punkte
	 */
	public Map<Point, List<Point>> cluster(List<Point> instances,
			List<Point> centroids);

}
