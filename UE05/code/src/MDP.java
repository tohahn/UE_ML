
public class MDP {
	//The Rewards for each state.
	//NaN is a not accessible Field
	private float[][] rewards={
			{Float.NaN,Float.NaN,Float.NaN,Float.NaN,Float.NaN,Float.NaN},
			{Float.NaN,-0.01f,-0.01f,-0.01f,1f,Float.NaN},
			{Float.NaN,-0.01f,Float.NaN,-0.01f,-15f,Float.NaN},
			{Float.NaN,-0.01f,-0.01f,-0.01f,-0.01f,Float.NaN},
			{Float.NaN,Float.NaN,Float.NaN,Float.NaN,Float.NaN,Float.NaN}
	};
	//Hier werden Terminalzustände markiert
	private boolean[][] isTerminal={
			{false,false,false,false,false,false},
			{false,false,false,false,true,false},
			{false,false,false,false,true,false},
			{false,false,false,false,false,false},
			{false,false,false,false,false,false}
	};
	// Contains the plicy after valueIteration() 
	private Dir[][] policy;

	//Transition probabilities in the format: ahead,right,reverse,left
	private float[] transition={0.8f,0.07f,0.06f,0.07f};
	public MDP(){
		policy=  new Dir[rewards.length][rewards[0].length]; 
	}

	/**
	 * 
	 * @param discount The Discount used
	 * @param iter The Number of Iterations to Do. If iter=0 the Iteration will terminate if
	 * the maximum delta of the utilities is below err
	 * @param err The minimum delta of Utilities that will terminate the Iteration.
	 * If 0 only the specified #Iterations will terminate the loop
	 * @return
	 * A float[][] of Utilities.
	 */
	public float[][] valueIteration(float discount,int iter,float err){
		//create the utility matrix
		
		float[][] uOld = new float[rewards.length][rewards[0].length]; 
		//initialise Utilities with rewards?
		uOld= rewards.clone();
		double sigm = 0d;
		int i=0;
		do{
			sigm=0f;
			float[][] u = new float[rewards.length][rewards[0].length]; 
			for(int y =0; y<rewards.length;y++){
				for(int x=0; x<rewards[0].length; x++){
					//Do everything only if next State is accessible
					if(!Float.isNaN(rewards[y][x])){
						if(isTerminal[y][x]){
							u[y][x]= rewards[y][x];
							break;
						}
						//update Step
						u[y][x]= rewards[y][x]+ determineMax(uOld,x,y)*discount; 
						
						//calculate and remember Delta
						double diff = Math.abs(u[y][x]-uOld[y][x]);
						if(diff>sigm){
							sigm= diff;
						}
					}
				}
			}
			//Copy temp array
			uOld= u.clone();
			for(int f=0; f<uOld.length; f++){
				for(int index=0; i<uOld[f].length; i++){
					uOld[f][index]= u[f][index];
				}
				//System.out.println("cpy " +u[f][1] +" to: "+uOld[f][1]);
			}
			
			i++;
			
			//do while i is not jet == the number of iterations or iterations are not the 
			//termination criteria AND sigma(the delta of utilities) is smaller than
			//maximum allowed change
			System.out.print(".");
			//System.out.print(sigm);
		}while((iter==0||i<iter)&&sigm>= err);
		System.out.println("");
		return uOld;

	}
	//Directions for the MDP to go to
	public enum Dir {
	    UP, RIGHT, LEFT, DOWN,
	}
	/**
	 * Returns the maximum expected utility for u[y][x].
	 * To do so the method tries all possible directions to go to and returns max
	 */
	private float determineMax(float[][] u,int x, int y){
		
		
		//dont try on a wall
		if(!Float.isNaN(rewards[y][x])){
			float currentMax=-Float.MAX_VALUE;
			
			for(Dir d:Dir.values()){
				float sum=0f;
				int i=0;
				switch(d){
				//Determine degree of rotation for transitions
				case UP:
					i=0;
				break;
				case LEFT:
					i=1;
				break;
				case DOWN:	
					i=2;
				break;
				case RIGHT:
					i=3;
				break;
				}
				//rotate transitions to Fit direction
				sum+= tryForWall(u,y,x,Dir.UP)*transition[i%4];
				i++;
				sum+= tryForWall(u,y,x,Dir.RIGHT)*transition[i%4];
				i++;
				sum+= tryForWall(u,y,x,Dir.DOWN)*transition[i%4];
				i++;
				sum+= tryForWall(u,y,x,Dir.LEFT)*transition[i%4];
				
				if(sum>currentMax){
					currentMax=sum;
					policy[y][x]=d;
				}
			}
			
			return currentMax;
		}
		return Float.NaN;
	}
	
//	private float tryForWall(float[][]u,int y,int x,Dir dir){
//		
//		System.out.println("try: "+ x+" "+ y+" d: " +dir.toString()+ " -> "+ _tryForWall(u, y, x, dir));
//		return _tryForWall(u, y, x, dir);
//	}
	/**
	 * Tries to get the Reward of a step in a certain direction. Returns the Reward of the current step
	 * if a wall is hit.
	 * @param u the array to operate on
	 * @param y position coordinate
	 * @param x position coordnate
	 * @param dir the direction to go to
	 * @return Returns the utility of the state that is in the Direction dir.
	 * Returns the utility of the current state if there is a wall in that direction
	 */
	private float tryForWall(float[][]u,int y,int x,Dir dir){
		switch(dir){
		case UP:
			if(!Float.isNaN(u[y-1][x])){
				return u[y-1][x];
			}
		break;
		case RIGHT:
			if(!Float.isNaN(u[y][x+1])){
				return u[y][x+1];
			}
		break;
		case LEFT:
			if(!Float.isNaN(u[y][x-1])){
				return u[y][x-1];
			}
		break;
		case DOWN:
			if(!Float.isNaN(u[y+1][x])){
				return u[y+1][x];
			}
		break;
		}
		return u[y][x];
	}
	public Dir[][] getPolicy(){
		return policy;
	}
	public void overwriteRewards(int x, int y,float in){
		if(!Float.isNaN(in)){
			rewards[y][x]= in;
		}
		
	}
}
