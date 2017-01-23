package code;

import java.util.*;

public class MagicMain {
	public enum Item{
		Brot, Milch, Mehl
	}
	private static HashSet<Item> one = new HashSet<>(); 
	private static HashSet<Item> two = new HashSet<>(); 
	private static HashSet<Item> three= new HashSet<>(); 

	
	public static void main(String[] args) {
		ArrayList<HashSet<Item>> transactions = new ArrayList<>();
		one.add(Item.Brot);
		two.add(Item.Brot);
		three.add(Item.Brot);
		
		one.add(Item.Milch);
		two.add(Item.Milch);
		
		two.add(Item.Mehl);
		three.add(Item.Mehl);
		
		transactions.add(one);
		transactions.add(two);
		transactions.add(three);
		Apriori<Item> a = new Apriori<Item>(transactions);
		
		for(Map.Entry<HashSet<Item>,Float > e: a.getRules(0.00f, 0.00f).entrySet()){
			for(Item i : e.getKey()){
				System.out.print(i.name()+ "\t");
			}
			System.out.println(e.getValue());
		}
	}

}
