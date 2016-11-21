import java.util.ArrayList;

public class BayesTrainer {
	private Bayes bayes;
	BayesTrainer(Bayes b){
		bayes = b;
	}
	public void trainRoot(ArrayList<String[]> data,String[] names){

		for(String s:bayes.getClasses()){
			float counter = 0f;
			for(String[] sArr : data){
				if(sArr[sArr.length-1].equals(s)){
					counter++;
				}

			}
			bayes.addProb(s, counter/data.size());
		}
	}
	public void trainNodes(ArrayList<String[]> data,String[] names){
		for(BayesNode n : bayes.getNodes()){
			//index of the Node Attribute
			int i=0;
			while(names[i]!=n.getName()){
				i++;
			}
			//count #forms
			for(String parentClass : bayes.getClasses()){
				//per parent CLass
				for(String form:n.getClasses()){
					//per form of node classes
					float nrForms = 0f;
					float nrSimilarClass=0f;
					for(String[] sArr : data){
						
						if(sArr[sArr.length-1].equals(parentClass)){
							nrSimilarClass++;
							if(sArr[i].equals(form)){
								nrForms++;
							}
						}

					}
					n.addProb(parentClass, form, nrForms/nrSimilarClass);
				}
			}
			
		}
	}

}
