package code;

import java.util.ArrayList;

public class MagicMain {
	public enum Item{
		Brot, Milch, Mehl
	}
	
	public static void main(String[] args, Class Item) {
		Item[] i = { Item.Milch, Item.Brot, Item.Brot};
		ArrayList<Item[]> transactions = new ArrayList<Item[]>();
		transactions.add(i);
		Apriori<Item> a = new Apriori<Item>(transactions);
		Item.v
		
	}

}
