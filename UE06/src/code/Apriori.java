package code;

import java.util.*;
import java.util.Map.Entry;

public class Apriori<E extends Enum<E>> {
	// all transactions subdevided into lists by transaction size
	private ArrayList<ArrayList<E[]>> orderedTransa;
	
	//# of all Transactions
	private float nrTransa;
	
	private E[] values;
	
	public Apriori(ArrayList<E[]> transactions){
		// total number of transactions
		nrTransa= transactions.size();
		
		// all possible values of the transaction
		values = transactions.get(0)[0].getDeclaringClass().getEnumConstants();
		
		for(int i =0 ; i< transactions.size(); i++ ){
				
			E[] e = transactions.get(i);
				// create an ArrayList for each transaction length
				if(orderedTransa.get(e.length)==null){
					orderedTransa.add(e.length, new ArrayList<E[]>());
				}
			//fill the ArrayLists with the transactions of corresponding length	
			orderedTransa.get(e.length).add(e);
		}
	}
	
	/**
	 * Returns a Map of item-sets to their respective confidence
	 */
	public HashMap<E[],Float> getRules(float min_confidence, float min_support){
		HashMap<E[],Float> c1 = new HashMap<>();
		for( E item: values){
			E[] hypothese =(E[]) new Object[1];
			
			c1.put(hypothese , 0.0f);
		}
		HashMap<E[],Float> l1 = new HashMap<>();
		//Set<Entry<E,Float>> c1Set = c1.entrySet();
		for(Entry<E[],Float> entry : c1.entrySet()){
			float  support= support(entry.getKey());
			if(support>=min_support){
				l1.put(entry.getKey(), support);
			}
		}
		HashMap<E[],Float> lALL = new HashMap<>();
		if(l1.isEmpty()){
			return lALL;
		}
		
		//still work to do, start iterations
		@SuppressWarnings("unchecked")
		HashMap<E[],Float> ln = (HashMap<E[],Float>) l1.clone();
		HashMap<E[],Float> cn = new HashMap<E[],Float>();
		do{
			cn = aprioriGen(ln);
			ln = new HashMap<E[],Float>();
			for(Entry<E[],Float> entry : cn.entrySet()){
				float support = support(entry.getKey());
				if(support>=min_support){
					ln.put(entry.getKey(), support);
				}
			}
			for(Entry<E[],Float> entry : ln.entrySet()){
				lALL.put(entry.getKey(), entry.getValue());
			}

		}
		while(false);

		return null;
	}
	private HashMap<E[],Float> aprioriGen(HashMap<E[],Float> c){
		return null;
	}
	private float confidence(E[] x, E[] y){
		@SuppressWarnings("unchecked")
		E[] xy = (E[])new Object[x.length+y.length];
		System.arraycopy(x, 0, xy, 0, x.length);
		System.arraycopy(y, 0, xy, x.length-1, y.length);
		return support(xy)/support(x);
	}
	private float support(E[] e){
		int denominator = 0;
		for( int i = e.length; i<orderedTransa.size(); i++ ){
			for(E[] transa : orderedTransa.get(i)){
				denominator += containsAll(e,transa)? 1 : 0;
			}
		}
		return denominator/nrTransa;
	}
	/**
	 * returns true if all a are containded in b
	 * @return
	 */
	private boolean containsAll(E[] a, E[] b){
		for(E element: a){
			if(!contains(b, element)){
				return false;
			}
		}
		return true;
	}
	/**
	 * by: http://stackoverflow.com/questions/1128723/how-can-i-test-if-an-array-contains-a-certain-value
	 * @param array
	 * @param v
	 * @return
	 */
	private static <T> boolean contains(final T[] array, final T v) {
	    if (v == null) {
	        for (final T e : array)
	            if (e == null)
	                return true;
	    } else {
	        for (final T e : array)
	            if (e == v || v.equals(e))
	                return true;
	    }

	    return false;
	}
}
