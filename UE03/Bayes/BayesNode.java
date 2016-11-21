import java.util.ArrayList;

public class BayesNode {
	private String attributeName;
	private ArrayList<String> parentClasses;
	private ArrayList<String> myClasses;
	private float[][] probabilityTable;

	BayesNode(String name, ArrayList<String> pClasses,ArrayList<String> mClasses){
		attributeName= name;
		probabilityTable = new float[pClasses.size()][mClasses.size()];
		parentClasses=pClasses;
		myClasses=mClasses;
		
	}

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
