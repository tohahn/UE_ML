package code;

import java.util.ArrayList;

public class MagicMain {
	public enum Items{
		Brot, Milch, Mehl
	}
	
	public static void main(String[] args) {
		Items[] i = { Items.Milch, Items.Brot, Items.Brot};
		ArrayList<Items[]> transactions = new ArrayList<Items[]>();
		transactions.add(i);
		//Apriori<Items> a = new Apriori<Items>(transactions);
		for( Items item : i[0].values()){
			System.out.println(item.name());
		}
	}

}
