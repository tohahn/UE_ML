
public class MDP {
	//Die Rewards für die jeweiligen Schritte
	//NaN steht für ein nicht betretbares Feld
	private float[][] rewards={
			{Float.NaN,Float.NaN,Float.NaN,Float.NaN,Float.NaN,Float.NaN},
			{Float.NaN,-0.01f,-0.01f,-0.01f,1f,Float.NaN},
			{Float.NaN,-0.01f,Float.NaN,-0.01f,-15f,Float.NaN},
			{Float.NaN,-0.01f,-0.01f,-0.01f,-0.01f,Float.NaN},
			{Float.NaN,Float.NaN,Float.NaN,Float.NaN,Float.NaN,Float.NaN}
	};
	private boolean[][] isTerminal={
			{false,false,false,false,false,false},
			{false,false,false,false,true,false},
			{false,false,false,false,true,false},
			{false,false,false,false,false,false},
			{false,false,false,false,false,false}
	};
	private Dir[][] policy;
//	private float[][] rewards={
//			{Float.NaN,Float.NaN,Float.NaN,Float.NaN,Float.NaN,Float.NaN},
//			{Float.NaN,-0.04f,-0.04f,-0.04f,1f,Float.NaN},
//			{Float.NaN,-0.04f,Float.NaN,-0.04f,-1f,Float.NaN},
//			{Float.NaN,-0.04f,-0.04f,-0.04f,-0.04f,Float.NaN},
//			{Float.NaN,Float.NaN,Float.NaN,Float.NaN,Float.NaN,Float.NaN}
//	};
	//ahead,right,reverse,left
	private float[] transition={0.8f,0.07f,0.06f,0.07f};
	public MDP(){
		policy=  new Dir[rewards.length][rewards[0].length]; 
	}

	public float[][] valueIteration(float discount,int iter,float err){
		//create the utility matrix
		
		float[][] uOld = new float[rewards.length][rewards[0].length]; 
		//initialise Utilities with rewards?
		//uOld= rewards.clone();
		double sigm = 0d;
		int i=0;
		do{
			sigm=0f;
			float[][] u = new float[rewards.length][rewards[0].length]; 
			for(int y =0; y<rewards.length;y++){
				for(int x=0; x<rewards[0].length; x++){
					if(!Float.isNaN(rewards[y][x])){
						if(isTerminal[y][x]){
							u[y][x]= rewards[y][x];
							break;
						}
						u[y][x]= rewards[y][x]+ determineMax(uOld,x,y)*discount; 
						
						double diff = Math.abs(u[y][x]-uOld[y][x]);
						if(diff>sigm){
							sigm= diff;
						}
					}
				}
			}
			uOld= u.clone();
//			for(int f=0; f<uOld.length; f++){
//				for(int index=0; i<uOld[f].length; i++){
//					uOld[f][index]= u[f][index];
//				}
//				//System.out.println("cpy " +u[f][1] +" to: "+uOld[f][1]);
//			}
			
			i++;
			
		}while(i<iter&&sigm>= (err*((1-discount))/discount) );
		//System.out.println("sigm " + sigm);
		return uOld;

	}
	public enum Dir {
	    UP, RIGHT, LEFT, DOWN,
	}
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
	/**
	 * Tries to get the Reward of a step in a certain direction. Returns the Reward of the current step
	 * if a wall is hit.
	 * @param u
	 * @param y
	 * @param x
	 * @param dir
	 * @return
	 */
//	private float tryForWall(float[][]u,int y,int x,Dir dir){
//		
//		System.out.println("try: "+ x+" "+ y+" d: " +dir.toString()+ " -> "+ _tryForWall(u, y, x, dir));
//		return _tryForWall(u, y, x, dir);
//	}
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
}
