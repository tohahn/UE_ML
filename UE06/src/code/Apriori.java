package code;

import java.util.*;
import java.util.Map.Entry;

public class Apriori<E extends Enum<E>> {
	// all transactions subdevided into lists by transaction size
	private HashMap<Integer,ArrayList<HashSet<E>>> orderedTransa;
	
	// needed to know the highes key for the map above
	int sizeOfLongestTransaction =0;
	
	//# of all Transactions
	private float nrTransa;
	
	private E[] values;
	
	public Apriori(ArrayList<HashSet<E>> transactions){
		// total number of transactions
		nrTransa= transactions.size();
		
		// all possible values of the transaction
		values = transactions.get(0).iterator().next().getDeclaringClass().getEnumConstants();
		
		orderedTransa = new HashMap<>();
		for(int i =0 ; i< transactions.size(); i++ ){
			
			HashSet<E> e = transactions.get(i);
			if(e.size()>sizeOfLongestTransaction){
				sizeOfLongestTransaction= e.size();
			}
				// fill HashMap with transactions sorted by their length
			if(orderedTransa.containsKey(e.size())){
				orderedTransa.get(e.size()).add(e);
				System.out.print("a" + e.size());
			}
			else{
				System.out.print("b"+ e.size());
				ArrayList<HashSet<E>> listOfTransactions = new ArrayList<>();
				listOfTransactions.add(e);
				orderedTransa.put(e.size(), listOfTransactions);
			}
			
		}

		System.out.println("Transactions :");
		for(int i =1; i< sizeOfLongestTransaction; i++){
			
			if(orderedTransa.containsKey(i)){
				
				for(HashSet<E> transaction: orderedTransa.get(i)){
					setPrint("Transaction size: "+ i, transaction,"");
				}
			}
			
		}
	}
	
	
	/**
	 * Returns a Map of item-sets to their respective confidence
	 */
	public HashMap<HashSet<E>,Float> getRules(float min_confidence, float min_support){
		HashSet<HashSet<E>> c1 = new HashSet<>();
		for( E item: values){
			HashSet<E> hypothesis = new HashSet<>();
			hypothesis.add(item);
			c1.add(hypothesis);
		}
		HashMap<HashSet<E>,Float> l1 = new HashMap<>();
		//HashSet<Entry<E,Float>> c1Set = c1.entrySet();
		for(HashSet<E> entry : c1){
			float  support= support(entry);
			if(support>=min_support){
				l1.put(entry, support);
			}
		}
		HashMap<HashSet<E>,Float> lALL = new HashMap<>();
		if(l1.isEmpty()){
			return lALL;
		}
		
		//still work to do, start iterations
		@SuppressWarnings("unchecked")
		HashMap<HashSet<E>,Float> ln = new HashMap<>();
		ln.putAll(l1);
		HashSet<HashSet<E>> cn = new HashSet<>();
		do{
			
			cn = aprioriGen(ln.keySet());
			setPrint("new apriori-gen:", cn,"");
			ln = new HashMap<HashSet<E>,Float>();
			for(HashSet<E> entry : cn){
				float support = support(entry);
				if(support>=min_support){
					ln.put(entry, support);
				}
			}
			setPrint("Survivers: ", ln.keySet(),"");
			for(Entry<HashSet<E>,Float> entry : ln.entrySet()){
				lALL.put(entry.getKey(), entry.getValue());
			}

		}
		while(!ln.isEmpty());

		return lALL;
	}
	private void setPrint(String pre, Set<HashSet<E>> s, String suff){
		System.out.println(pre);
		for(HashSet<E> transaction: s){
			for(E item : transaction){
				System.out.print(item.name()+ " ");
			}
		}
		System.out.println(suff);
	}
	private void setPrint(String pre, HashSet<E> s, String suff){
		System.out.println(pre);
			for(E item : s){
				System.out.print(item.name()+ " ");
			}
		System.out.println(suff);
	}
//	private HashSet<E[]> aprioriGen(HashSet<E[]> c){
//		HashSet <HashSet<E>>candidates = new HashSet<>();
//		//step one of apriori-gen:
//		//join-step
//		int currentHypothesisLength;
//		{
//			Object[] someHypothesis = c.toArray();
//			currentHypothesisLength= someHypothesis.length;
//		}
//		for(E[] transaction1: c){
//			for(E[] transaction2: c){
//				HashSet joined = copyJoin(transaction1, transaction2);
//				if(joined.size() == currentHypothesisLength + 2){
//					candidates.add(joined);
//				}
//			}
//		}
//		//step two of apriori-gen:
//		//prune-step
//		for(HashSet<E> transaction: candidates){
//			for(E item: transaction){
//				transaction.remove(item);
//				if(c.contains(transaction)){
//					transaction.add(item);
//				}
//				else{
//					candidates.remove(transaction.add(item));
//				}
//			}
//		}
//		//convert to HashSet<E[]>
//		HashSet<E[]> returner = new HashSet<>();
//		for(HashSet<E> transaction : candidates){
//			returner.add((E[])transaction.toArray());
//		}
//		return returner;
//	}
	private HashSet<HashSet<E>> aprioriGen(Set<HashSet<E>> c){
		HashSet <HashSet<E>>candidates = new HashSet<>();
		//step one of apriori-gen:
		//join-step
		int currentHypothesisLength;
		{
			currentHypothesisLength= c.iterator().next().size();
		}
		for(HashSet<E> transaction1: c){
			for(HashSet<E> transaction2: c){
				HashSet<E> joined = copyJoin(transaction1,transaction2);
				
				if(joined.size() == currentHypothesisLength + 1){
					candidates.add(joined);
				}
			}
		}
		setPrint("after aprioriGen step join"  + " :" ,candidates,"");
		
		//step two of apriori-gen:
		//prune-step
		for(HashSet<E> transaction: candidates){
			for(E item: transaction){
				HashSet<E> current = new HashSet<>();
				current = (HashSet<E>) transaction.clone();
				current.remove(item);
				if(!c.contains(transaction)){
					candidates.remove(current.add(item));
				}
				
			}
		}
		//convert to HashSet<E[]>
//		HashSet<E[]> returner = new HashSet<>();
//		for(HashSet<E> transaction : candidates){
//			returner.add((E[])transaction.toArray());
//		}
		setPrint("after aprioriGen step prune"  + " :" ,candidates,"");
		return candidates;
	}
	private HashSet<E> copyJoin(E[] a, E[] b){
		HashSet<E> returner = new HashSet<>();
		for(E e : a ){
			returner.add(e);
		}
		for(E e : b ){
			returner.add(e);
		}
		return returner;
		
	}
	private HashSet<E> copyJoin(HashSet<E> a, HashSet<E> b){
		HashSet<E> returner = new HashSet<>();
		for(E e : a ){
			returner.add(e);
		}
		for(E e : b ){
			returner.add(e);
		}
		return returner;
		
	}
	private E[] copyWithout(E[] in, E without){
		E[] returner= (E[]) new Object[in.length-1];
		int j=0;
		for( int i = 0; i< in.length; i++){
			if(!in[i].equals(without)){
				returner[j]= in[i];
				j++;
			}
		}
		return returner;
	}
	/**
	 * Contains for an Array and a HashSet.
	 * The Array can be shorter than the HashSet.
	 * @param a
	 * @param eSet
	 * @return
	 */
//	private boolean contains(E[] a,HashSet<E[]> eSet ){
//		for(E[] b: eSet ){
//			if(!containsAll(a,b)){
//				return false;
//			}
//		}
//		return true;
//	}
//	private float confidence(E[] x, E[] y){
//		@SuppressWarnings("unchecked")
//		E[] xy = (E[])new Object[x.length+y.length];
//		System.arraycopy(x, 0, xy, 0, x.length);
//		System.arraycopy(y, 0, xy, x.length-1, y.length);
//		return support(xy)/support(x);
//	}
//	private float support(E[] e){
//		int denominator = 0;
//		for( int i = e.length; i<orderedTransa.size(); i++ ){
//			for(E[] transa : orderedTransa.get(i)){
//				denominator += containsAll(e,transa)? 1 : 0;
//			}
//		}
//		return denominator/nrTransa;
//	}
	
	private float support(HashSet<E> e){
		float denominator = 0;
		for( int i = e.size(); i<sizeOfLongestTransaction; i++ ){
			if(orderedTransa.containsKey(i)){
				
				for(Set<E> transa : orderedTransa.get(i)){
					denominator += transa.containsAll(e)? 1.0f : 0;
					if(transa.containsAll(e)){
						//System.out.println("Wuhuu!!!!!!!!");
					}
				}
				
			}
		}
		if(denominator>0){
			System.out.println(denominator/nrTransa);
		}
		return denominator/nrTransa;
	}
//	/**
//	 * returns true if all a are containded in b
//	 * @return
//	 */
//	private boolean containsAll(E[] a, E[] b){
//		for(E element: a){
//			if(!contains(b, element)){
//				return false;
//			}
//		}
//		return true;
//	}
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
