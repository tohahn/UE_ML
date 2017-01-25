

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
		for(int i =1; i<= sizeOfLongestTransaction; i++){
			
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
	public HashMap<HashSet<E>,HashSet<E>> getRules(float min_confidence, float min_support){
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
			HashMap<HashSet<E>,HashSet<E>> returner = new HashMap<>();
			for(HashSet<E> entry : lALL.keySet()){
			 returner.put(entry, null);
			}
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
			setPrint("survivors: ", ln.keySet(),"");
			for(Entry<HashSet<E>,Float> entry : ln.entrySet()){
				lALL.put(entry.getKey(), entry.getValue());
			}

		}
		while(!ln.isEmpty());
		//now generate association rules
		HashMap<HashSet<E>,HashSet<E>> assRules = new HashMap<>();
		//also needed are the Conclusions 
		HashSet<HashSet<E>> hK = new HashSet<>();
		for(Entry<HashSet<E>,Float> entry: lALL.entrySet()){
			for(E item : entry.getKey()){
				
				HashSet<E> aImplies = new HashSet<>();
				HashSet<E> b = new HashSet<>();
				aImplies.addAll(entry.getKey());
				aImplies.remove(item);
				b.add(item);
				if(confidence(aImplies,b)>min_confidence){
					assRules.put(aImplies, b);
					hK.add(b);
				}
			}
		}
		do{
			HashSet<HashSet<E>> hKMinusOne = new HashSet<>();
			hKMinusOne.addAll(hK);
			hK= aprioriGen(hKMinusOne);
			setPrint("new apriori-gen:", hK,"");
			HashSet<HashSet<E>> toBeRemoved = new HashSet<>();
			for(HashSet<E> conclusion : hK){
				for(Entry<HashSet<E>,Float> entry: lALL.entrySet()){
					HashSet<E> aImplies = new HashSet<>();
					aImplies.addAll(entry.getKey());
					aImplies.removeAll(conclusion);
					
					float conf = confidence(aImplies,conclusion);
					
					System.out.println("Confidence" + aImplies + "-->"+ conclusion);
					System.out.println("is" + conf);
					
					if(conf>min_confidence){
						assRules.put(aImplies, conclusion);
					}
					else{
						toBeRemoved.add(conclusion);
					}
				}
			}
			for(HashSet<E> entry : toBeRemoved){
				hK.remove(entry);
			}
			setPrint("survivors: ", hK,"");
		}
		while(!hK.isEmpty());
		return assRules;
	}
	private void setPrint(String pre, Set<HashSet<E>> s, String suff){
		System.out.print(pre);
		for(HashSet<E> transaction: s){
			System.out.print(transaction);
		}
		System.out.println(suff);
	}
	private void setPrint(String pre, HashSet<E> s, String suff){
		System.out.print(pre);
		System.out.print(s);
		System.out.println(suff);
	}

	private HashSet<HashSet<E>> aprioriGen(Set<HashSet<E>> c){
		HashSet <HashSet<E>>candidates = new HashSet<>();
		//step one of apriori-gen:
		//join-step
		if(c.isEmpty()){
			return candidates;
		}
		int	currentHypothesisLength= c.iterator().next().size();
		for(HashSet<E> transaction1: c){
			for(HashSet<E> transaction2: c){
				HashSet<E> joined = copyJoin(transaction1,transaction2);
				
				if(joined.size() == currentHypothesisLength + 1){
					candidates.add(joined);
				}
			}
		}
		setPrint("[ \t\t after aprioriGen step join"  + " :" ,candidates,"]");
		
		//step two of apriori-gen:
		//prune-step
		//TODO nothing gets pruned!
		for(HashSet<E> transaction: candidates){
			for(E item: transaction){
				HashSet<E> current = new HashSet<>();
				current.addAll( transaction);
				current.remove(item);
				if(!c.containsAll(current)){
					candidates.remove(current.add(item));
				}
				
			}
		}
		//convert to HashSet<E[]>
//		HashSet<E[]> returner = new HashSet<>();
//		for(HashSet<E> transaction : candidates){
//			returner.add((E[])transaction.toArray());
//		}
		setPrint("[ \t\tafter aprioriGen step prune"  + " :" ,candidates,"]");
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
	private float confidence(HashSet<E> x, HashSet<E> y){
		
		HashSet<E> xy = new HashSet<>();
		xy.addAll(x);
		xy.addAll(y);
		return support(xy)/support(x);
	}
//	private float support(E[] e){
//		int denominator = 0;
//		for( int i = e.length; i<orderedTransa.size(); i++ ){
//			for(E[] transa : orderedTransa.get(i)){
//				denominator += containsAll(e,transa)? 1 : 0;
//			}
//		}
//		return denominator/nrTransa;
//	}
	
	public float support(HashSet<E> e){
		float denominator = 0;
		for( int i = e.size(); i<=sizeOfLongestTransaction; i++ ){
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
