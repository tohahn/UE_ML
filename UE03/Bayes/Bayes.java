import java.util.ArrayList;
import java.util.HashSet;

public class Bayes {
String name;
ArrayList<String>  classes = new ArrayList<>();
float[] probs;
HashSet<BayesNode> nodes = new HashSet<>();


	public Bayes(String name, HashSet<String> classes){
		this.classes.addAll(classes);
		probs = new float[classes.size()];
		for(int i = 0 ; i<probs.length;i++){
			probs[i]=0f;
		}
		this.name =name;
	}
	
	public String[] classify(ArrayList<String[]> data,String[] names){
		String returner = "";
		for(String n: names){
			returner += "(" +n + ") " ;
		}
		returner+="\n";
		for(String[] line :data){
			for(String s : line){
			returner += s;
			returner += ", ";
			}
			returner = returner.substring(0, returner.lastIndexOf(','));
			
			float max = 0f;
			String best = "None";
			for(String c:classes){
				float prob=getProb(c);
				for(int i = 0; i<line.length; i++){
					prob *= this.getNode(names[i]).getProb(line[i], c); 
				}
				if(prob>max){
					best= c;
					max = prob;
				}
			}
			returner+= " -> " + best + " with " + max;
			returner+= "\n";
		}
		return returner.split("\n");
	}
	public void addNode(String name, ArrayList<String> forms){
		nodes.add(new BayesNode(name,classes,forms));
		System.out.println("New Node " +name+ " added.");
	}
	
	public void addProb(String className, float prob){
		for(int i =0; i<classes.size();i++){
			if(classes.get(i).equals(className)){
				probs[i]=prob;
				return;
			}
		}
	}
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
		returner += "Root ";
		returner += name + "\n";
		int i = 0;
		for(String s: classes){
			returner += s + ", " ;
			returner += probs[i] + " ; ";
		}
		returner += "\n";
		returner += "Nodes:\n";
		for(BayesNode n: nodes){
			returner += n.toString();
		}
		return returner + "\n-----------";
	}
	public void fixBayes(){
		for(int i =0; i<probs.length; i++){
			probs[i]=probs[i]+1f;
		}
		for(BayesNode n: nodes){
			n.fixNode();
		}
	}
}
