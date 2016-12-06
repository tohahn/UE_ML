import java.util.ArrayList;
import java.util.HashSet;

public class Bayes {
// Name des Wurzelknotens
String name;
// Ausprägungsklassen des Wurzelknotens
ArrayList<String>  classes = new ArrayList<>();
//Basiswahrscheinlichkeiten der Ausprägungen
float[] probs;
// Die Kinderknoten
HashSet<BayesNode> nodes = new HashSet<>();

/**
 * Erzeugt einen neuen Naive Bayes Baum
 * @param name: Name des Wurzelknotens
 * @param classes: Seine Ausprägungen
 */ 
	public Bayes(String name, HashSet<String> classes){
		this.classes.addAll(classes);
		probs = new float[classes.size()];
		for(int i = 0 ; i<probs.length;i++){
			probs[i]=0f;
		}
		this.name =name;
	}
	/**
	 * Query an den Naive Bayes baum, gegeben ein komplettes Datenset.
	 * @param data: Die Ausprägungen der einzelnen Attribute. Seperiert durch Komma,
	 * ein Array Eintrag pro Datensatz. Die Ausprägungen müssen mit denen der Nodes übereinstimmen.
	 * @param names: Die Namen der Attribute. Die Reihenfolge muss die selbe sein, welche in data verwendet wird. Außerdem müssen
	 * die Namen mit den Namen der Kinderknoten übereinstimmen.
	 * @return Ein String Array mit einem Eintrag pro Ausprägungsklasse der Wurzel pro Datensatz sowie
	 * den Namen der Attribute im ersten Eintrag.
	 */
	public String[] classify(ArrayList<String[]> data,String[] names){
		//Namen im String platzieren
		String returner = "";
		for(String n: names){
			returner += "(" +n + ") " ;
		}
		returner+="\n";
		
		//für Jeden Datensatz
		for(String[] line :data){
			//schreibe zuerst die Attributausprägungen in den Stirng wie sie im Datensatz vorliegen
			String dataAttributes = "";
			for(String s : line){
				dataAttributes += s;
				dataAttributes += ", ";
			}
			dataAttributes = dataAttributes.substring(0, dataAttributes.lastIndexOf(','));
			
			//Nun füge die Wahrscheinlichkeit hinzu
			float max = 0f;
			String current = "None";
			for(String c:classes){
				//für jede Klasse des Wurzelknotens
				float prob=getProb(c);
				//basiswahrscheinlichkeit 
				
				for(int i = 0; i<line.length; i++){
					//für jeden Eintrag im Datensatz
					prob *= this.getNode(names[i]).getProb(line[i], c); 
					//Wahrscheinlichkeiten für P_Attributname(c|Attributausprägung) aufmultiplizieren
				}
				
				//if(prob>max){
					current= c;
					max = prob;
				//}
					// als Ergebnis als Zeile in der Ausgabe vermerken
				returner+= dataAttributes+ " -> " + current + " with " + max;
				returner+= "\n";
			}
			
			returner+= "\n";
			
		}
		return returner.split("\n");
	}
	public void addNode(String name, ArrayList<String> forms){
		nodes.add(new BayesNode(name,classes,forms));
		System.out.println("New Node " +name+ " added.");
	}
	/**
	 * Fügt dem Naive Bayes Netzwerk eine neue Basiswahrscheinlichkeit hinzu.
	 * @param className: Name der Klasse des Wurzelknotens
	 * @param prob: Wahrscheinlichkeit der Klasse
	 */
	public void addProb(String className, float prob){
		for(int i =0; i<classes.size();i++){
			if(classes.get(i).equals(className)){
				probs[i]=prob;
				return;
			}
		}
	}
	/**
	 * hole Basiswahrscheinlichkeit
	 * @param clss: zu dieser Klasse
	 * @return
	 */
	private float getProb(String clss){
		for(int i =0; i<classes.size();i++){
			if(classes.get(i).equals(clss)){
				return probs[i];
			}
		}
		return 0f;
	}
	public ArrayList<String> getClasses(){
		return classes;
	}
	public BayesNode getNode(String name){
		 for(BayesNode n:nodes){
			 if(n.getName().equals(name)){
				 return n;
			 }
		 }
		 return null;
	}
	public HashSet<BayesNode> getNodes(){
		 return nodes;
	}
	public String toString(){
		String returner= "";
		returner += "\n-----------\n";
		returner += "Root: ";
		returner += name + "\n";
		returner += "Base probabilities:" + "\n";
		int i = 0;
		for(String s: classes){
			returner += s + ": " ;
			returner += probs[i] + ", ";
		}
		returner += "\n";
		returner += "Nodes:\n";
		for(BayesNode n: nodes){
			returner += n.toString();
		}
		return returner + "\n-----------";
	}
	/**
	 *  Methode zum "verbessern" eines Naive bayes indem die Niedrigste Wahrscheinlichket statt 0 1 wird.
	 */
	public void fixBayes(){
		for(int i =0; i<probs.length; i++){
			probs[i]=probs[i]+1f;
		}
		for(BayesNode n: nodes){
			n.fixNode();
		}
	}
}
