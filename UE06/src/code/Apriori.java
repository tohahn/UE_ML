package code;

import java.util.ArrayList;
import java.util.HashMap;

public class Apriori<E extends Enum> {
	// alle Transactions aufgeteilt in untermengen mit der jeweiligen Anzahl an Elementen.
	private ArrayList<ArrayList<E[]>> orderedTransa;
	
	//# aller Transactions
	private float nrTransa;
	
	public Apriori(ArrayList<E[]> transactions){
		nrTransa= transactions.size();
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
	 * 
	 */
	public HashMap<E[],Float> getRules(){
		do{
			
		}
		while(true);
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
