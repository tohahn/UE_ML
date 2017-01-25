

import java.util.*;

public class MagicMain {
	public enum Item{
		Brot, Bier, Schokolade,Cola,Chips,Wasser,Saft,Wein,Schinken
	}
	private static ArrayList<HashSet<Item>> transactions = new ArrayList<>();
	private static Item[][] transactionsArr = {{
		Item.Brot,Item.Bier
	},{
		Item.Schokolade,Item.Cola,Item.Chips	
	},{
		Item.Wasser
	},{
		Item.Saft,Item.Cola,Item.Bier,Item.Wein
	},{
		Item.Cola,Item.Bier
	},{
		Item.Saft,Item.Wasser
	},{
		Item.Saft,Item.Cola,Item.Wein
	},{
		Item.Saft,Item.Cola,Item.Bier
	},{
		Item.Schokolade,Item.Schinken,Item.Brot
	}};
	private static HashSet<Item> two = new HashSet<>(); 
	private static HashSet<Item> three= new HashSet<>(); 

	private static HashSet<Item> getSet(Item[] items){
		 HashSet<Item> returner = new HashSet<>();
		 for(Item i: items){
			 returner.add(i);
		 }
		 return returner;
		 
	}
	public static void main(String[] args) {
		ArrayList<HashSet<Item>> transactions = new ArrayList<>();
		for(Item[] transaction : transactionsArr){
			transactions.add(getSet(transaction));
		}
		
		Apriori<Item> a = new Apriori<Item>(transactions);
		
//		System.out.println("support test one :" + a.support(one));
//		System.out.println("support test two :" + a.support(two));
//		System.out.println("support test three :" + a.support(three));
		for(Map.Entry<HashSet<Item>,HashSet<Item>> e: a.getRules(0.75f, 0.2f).entrySet()){
			for(Item i : e.getKey()){
				System.out.print(i.name()+ "\t");
			}
			System.out.print("--->\t");
			System.out.println(e.getValue());
		}
	}

}
