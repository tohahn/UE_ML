import java.util.ArrayList;

public class BayesTrainer {
	private Bayes bayes;
	BayesTrainer(Bayes b){
		bayes = b;
	}
	/**
	 * Zählt das Vorkommen jede Klasse des Wurzelknotens im Bayes und errechnet daraus die Basiswahrscheinlichkeiten
	 * @param data: Die Trainingsdaten mit der Ausprägung des Wurzelknotens an der letzten Stelle
	 * @param names: hier Ungenuntzt
	 */
	public void trainRoot(ArrayList<String[]> data,String[] names){

		for(String s:bayes.getClasses()){
			//für jede bekannte Ausprägung
			float counter = 0f;
			//zähle das Vorkommen der verschiedenen Ausprägungen
			for(String[] sArr : data){
				if(sArr[sArr.length-1].equals(s)){
					counter++;
				}

			}
			//Teile die Vorgekkommenen durch die Gesammtanzahl
			bayes.addProb(s, counter/data.size());
		}
	}
	/**
	 * Trainiert die Wahrscheinlichkeiten aller Wurzelknotens
	 * @param data
	 * @param names
	 */
	public void trainNodes(ArrayList<String[]> data,String[] names){
		for(BayesNode n : bayes.getNodes()){
			//für alle Blattknoten bzw. Attribute
			int i=0;
			while(names[i]!=n.getName()){
				i++;
				//Wie lautet der Index des aktuellen Attributs im Datensatz?
			}
			//Berechne Die Anzahl der vorkommenden Einträge pro Wurzelknoten (Klassifikationsergebnis)
			for(String parentClass : bayes.getClasses()){
				//für alle Ausprägungen des Wurzelknotens
				for(String form:n.getClasses()){
					//für jede einzelne Ausprägung des gerade betrachteten Blattknotens
					float nrForms = 0f;
					float nrSimilarClass=0f;
					for(String[] sArr : data){
						
						if(sArr[sArr.length-1].equals(parentClass)){
							//Wenn die Datenzeile zur gleichen Ausprägung des Wurzelknotens gehört
							nrSimilarClass++;
							if(sArr[i].equals(form)){
								//Wenn die Datenzeile zusätzlich das zur Zeit betrachtete Blattknotenattribut hat 
								nrForms++;
							}
						}

					}
					/*Anzahl der vorkommenden Ausprägungen/ Anzahl aller ausprägungen welche zur 
					*selben WurzelknotenKlasse (aka. zum selben Klassifikator) gehören
					*/
					n.addProb(parentClass, form, nrForms/nrSimilarClass);
				}
			}
			
		}
	}

}
