package card;

import java.util.*;

public class discardPile {
	LinkedList<card> cardpile;
	public discardPile(){
		cardpile=new LinkedList();
	}
	public void addcard(card discardCard){
		cardpile.addFirst(discardCard);
	}
	public card getFist(){
		return cardpile.getFirst();
	}
}
