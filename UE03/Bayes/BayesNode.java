import java.util.ArrayList;

public class BayesNode {
	// Name des Attributs, das in dem Knoten kodiert wird
	private String attributeName;
	//Ausprägungsklassen des Elternknotens (aka. die jeweiligen Algorithmen)
	private ArrayList<String> parentClasses;
	//Eigene Ausprägungen (bei Noise z.B. low, high, none
	private ArrayList<String> myClasses;
	//Die Wahrscheinlichkeitstabelle P(a|b) im Format [b][a]
	private float[][] probabilityTable;

	/**
	 * Erstellt einen neuen Knoten für einen naive Bayes
	 * @param name Name des Attributs, welches der Knoten kodiert
	 * @param pClasses Ausprägungsklassen des Elternknotens
	 * @param mClasses Eigene Ausprägungsklassen
	 */
	BayesNode(String name, ArrayList<String> pClasses,ArrayList<String> mClasses){
		attributeName= name;
		probabilityTable = new float[pClasses.size()][mClasses.size()];
		parentClasses=pClasses;
		myClasses=mClasses;
		
	}
	/**
	 * Fügt eine Neue Wahrscheinlichkeit P(a|b) in die Wahrscheinlichkeitstabelle ein.
	 * @param pClass b
	 * @param mClass a 
	 * @param prob P(a|b)
	 * @return 1 wenn erfolgreich
	 */
	public int addProb(String pClass, String mClass, float prob){
		if(parentClasses.indexOf(pClass)!=-1){
			if(myClasses.indexOf(mClass)!=-1){
				probabilityTable[parentClasses.indexOf(pClass)][myClasses.indexOf(mClass)] = prob;
				return 1; 
			}
		}
		return -1;
	}
	public float getProb(String form, String given){
		return probabilityTable[parentClasses.indexOf(given)][myClasses.indexOf(form)];
	}
	public ArrayList<String> getClasses(){
		return myClasses;
	}
	
	public String getName(){
		return attributeName;
	}
	
	/**
	 *  Methode zum "verbessern" eines Naive bayes indem die Niedrigste Wahrscheinlichket statt 0 1 wird.
	 */
	public void fixNode(){
		for(int x = 0; x< probabilityTable.length ; x++){
			for(int y = 0; y< probabilityTable[x].length ; y++){
				probabilityTable[x][y] += 1f;
			}
		}
	}
	
	public String toString(){
		String returner= "\n";
		returner += attributeName + "\n";
		returner +=  parentClasses.size() + " x " + myClasses.size() + " big\n";
		for(int x =0; x<parentClasses.size(); x++){
			for(int y=0; y<myClasses.size(); y++){
				returner += parentClasses.get(x) + " | " + myClasses.get(y) + ": " + probabilityTable[x][y];
				returner += "\n"; 
			}
		}
		return returner;
	}
	
}
