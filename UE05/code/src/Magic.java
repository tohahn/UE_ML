import java.util.Arrays;

public class Magic {

	public static void main(String[] args){
		MDP mdp = new MDP();
		//Aufgabe1
		System.out.println("Aufgabe 1: Utilities");
		float[][]ut = mdp.valueIteration(1f,100,0.0f);
		for(float[] f:ut){
			for(float ft: f){
				System.out.printf("%1f \t", ft);
			}
			System.out.println("");
		}
		System.out.println("");
		
		//Aufgabe 2
		System.out.println("Aufgabe 2: Policies");
		MDP.Dir[][] p= mdp.getPolicy();
		for(MDP.Dir[] line :p){
			for(MDP.Dir dir: line){
				System.out.print(dir+"\t");
			}
			System.out.println("");
		}
		System.out.println("");


		//Aufgabe 3
		System.out.println("Aufgabe3");
		MDP mdp2 = new MDP();
		mdp2.valueIteration(1f,100,0.0f);
		MDP.Dir[][] p2= mdp2.getPolicy();
		MDP.Dir[][] p3;
		
		float i =1f;
		do{
			i = i-0.01f; 
			MDP mdp3 = new MDP();
			mdp3.valueIteration(i, 100, 0f);
			p3= mdp3.getPolicy();
		}
		while(Arrays.deepToString(p2).equals(Arrays.deepToString(p3)));
		System.out.println("Policy changed at: " +i + "  To:");
		for(MDP.Dir[] line :p3){
			for(MDP.Dir dir: line){
				System.out.print(dir+"\t");
			}
			System.out.println("");
		}
		System.out.println("From: ");
		//print last step that is simmilar to first
		MDP mdp3 = new MDP();
		mdp3.valueIteration(i+0.01f, 100, 0f);
		p3= mdp3.getPolicy();
		for(MDP.Dir[] line :p3){
			for(MDP.Dir dir: line){
				System.out.print(dir+"\t");
			}
			System.out.println("");
		}
		System.out.println("");
		
		//Aufgabe 4
		System.out.println("Reward für Fluss auf -300");
		mdp = new MDP();
		mdp.overwriteRewards(4,2,-300f);
		ut = mdp.valueIteration(1f,100,0.0f);
		for(float[] f:ut){
			for(float ft: f){
				System.out.printf("%1f \t", ft);
			}
			System.out.println("");
		}
		System.out.println("");
		p= mdp.getPolicy();
		for(MDP.Dir[] line :p){
			for(MDP.Dir dir: line){
				System.out.print(dir+"\t");
			}
			System.out.println("");
		}
		System.out.println("");
		
		System.out.println(" Reward für Fluss auf -2");
		 mdp = new MDP();
		mdp.overwriteRewards(4,2,-2f);
		ut = mdp.valueIteration(1f,100,0.0f);
		for(float[] f:ut){
			for(float ft: f){
				System.out.printf("%1f \t", ft);
			}
			System.out.println("");
		}
		System.out.println("");
		p= mdp.getPolicy();
		for(MDP.Dir[] line :p){
			for(MDP.Dir dir: line){
				System.out.print(dir+"\t");
			}
			System.out.println("");
		}
		System.out.println("");
		
		//Aufgabe 6
		System.out.println(" Reward für Fluss auf -1000, Abbruch bei delta < 0.02");
		mdp = new MDP();
		mdp.overwriteRewards(4,2,-1000f);
		ut = mdp.valueIteration(1f,0,0.02f);
		for(float[] f:ut){
			for(float ft: f){
				System.out.printf("%1f \t", ft);
			}
			System.out.println("");
		}
		System.out.println("");
		p= mdp.getPolicy();
		for(MDP.Dir[] line :p){
			for(MDP.Dir dir: line){
				System.out.print(dir+"\t");
			}
			System.out.println("");
		}
		System.out.println("");
		System.out.println(" Reward für Fluss auf -1000, Abbruch bei delta < 0.002");
		mdp = new MDP();
		mdp.overwriteRewards(4,2,-1000f);
		ut = mdp.valueIteration(1f,0,0.002f);
		p= mdp.getPolicy();
		for(MDP.Dir[] line :p){
			for(MDP.Dir dir: line){
				System.out.print(dir+"\t");
			}
			System.out.println("");
		}
		System.out.println("");
	}
}