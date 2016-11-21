import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Magic2 {
	 private static ArrayList<String[]> readData(String name) throws IOException {
	        //reads our data from a csv file
	        BufferedReader csv = new BufferedReader(new FileReader(name));
	        csv.readLine();

	        String line;
	        ArrayList<String[]> cases = new ArrayList<>();
	        while ((line = csv.readLine()) != null) {
	        	cases.add(line.split(","));
	        	
	        }
	        for(String[] sArr: cases){
	        	for(int i = 0 ; i<sArr.length;i++){
	        		sArr[i]=sArr[i].replaceAll("\\s", "");
	        	}
        	}
	        csv.close();
	        return cases;
	    }
	 private static String[] readNames(String name) throws IOException {
	        //reads our data from a csv file
	        BufferedReader csv = new BufferedReader(new FileReader(name));
	        String line;
	        ArrayList<String[]> names = new ArrayList<>();
	        if ((line = csv.readLine()) != null) {
	        	names.add(line.split(","));
	        }
	        for(String[] sArr: names){
	        	for(int i = 0 ; i<sArr.length;i++){
	        		sArr[i]=sArr[i].replaceAll("\\s", "");
	        	}
        	}
	        csv.close();
	        
	        return names.get(0);
	    }
	 private static Bayes loadBayes(ArrayList<String[]> data,String[] names){
		 HashSet<String> h = new HashSet<>();
		 for(String[] sArr : data){
			h.add(sArr[sArr.length-1]); 
		 }
		 Bayes b = new Bayes(names[names.length-1],h);
		 ArrayList<String> forms = new ArrayList<>();
		 //!!! Letzte Spalte auslassen !!!
		 for(int i = 0 ; i<data.get(0).length-1; i++){
			 for(String[] sArr : data){
				 if(forms.indexOf(sArr[i])==-1){
					 
					 forms.add(sArr[i]);
				 }
			 }
			 
			 b.addNode(names[i], (ArrayList<String>) forms.clone());
			 forms.clear();
		 }
		 BayesTrainer bT= new BayesTrainer(b);
		 bT.trainRoot(data, names);
		 bT.trainNodes(data, names);
		 return b;
	 }
	 public static void main(String[] args) throws IOException{
		 String file = "trainingset.csv";
		 String test = "queryset.csv";
		 Bayes b = loadBayes(readData(file),readNames(file));
		 System.out.println(b);
		 String [] result = b.classify(readData(test),readNames(test));
		 for(String s: result){
			 System.out.println(s);
		 }
		 b.fixBayes();
		 result = b.classify(readData(test),readNames(test));
		 for(String s: result){
			 System.out.println(s);
		 }
	 }
}
