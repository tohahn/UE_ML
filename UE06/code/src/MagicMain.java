

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
	/**
	 * WEKA gets the following results:
	 * 1. Wein=True 2 ==> Cola=True 2    <conf:(1)> lift:(1.8) lev:(0.1) [0] conv:(0.89)
 	 * 2. Wein=True 2 ==> Saft=True 2    <conf:(1)> lift:(2.25) lev:(0.12) [1] conv:(1.11)
 	 * 3. Bier=True Saft=True 2 ==> Cola=True 2    <conf:(1)> lift:(1.8) lev:(0.1) [0] conv:(0.89)
 	 * 4. Saft=True Wein=True 2 ==> Cola=True 2    <conf:(1)> lift:(1.8) lev:(0.1) [0] conv:(0.89)
 	 * 5. Cola=True Wein=True 2 ==> Saft=True 2    <conf:(1)> lift:(2.25) lev:(0.12) [1] conv:(1.11)
 	 * 6. Wein=True 2 ==> Cola=True Saft=True 2    <conf:(1)> lift:(3) lev:(0.15) [1] conv:(1.33)
 	 * 7. Bier=True 4 ==> Cola=True 3    <conf:(0.75)> lift:(1.35) lev:(0.09) [0] conv:(0.89)
 	 * 8. Saft=True 4 ==> Cola=True 3    <conf:(0.75)> lift:(1.35) lev:(0.09) [0] conv:(0.89)
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<HashSet<Item>> transactions = new ArrayList<>();
		for(Item[] transaction : transactionsArr){
			transactions.add(getSet(transaction));
		}
		
		Apriori<Item> a = new Apriori<Item>(transactions);
		
//		System.out.println("support test one :" + a.support(one));
//		System.out.println("support test two :" + a.support(two));
//		System.out.println("support test three :" + a.support(three));
		for(Map.Entry<HashSet<Item>,HashSet<HashSet<Item>>> e: a.getRules(0.75f, 0.2f).entrySet()){
			for(Item i : e.getKey()){
				System.out.print(i.name()+ "\t");
			}
			System.out.print("--->\t");
			System.out.println(e.getValue());
		}
	}

}
