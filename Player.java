package blackjack;

import java.util.Vector;

public class Player {
	private String playerName;
	private int money; 
	private Vector<Card> cards; //all cards the player currently has
	
	public Player(String n, int m){
		playerName =n;
		money = m;
		cards= new Vector<Card>();
	}
public String getName(){
	return playerName;
}
public void setName (String n){
	playerName=n;
}
public int getMoney(){
	return money;
}
public void setMoney (int m){
	money=m;
}
public Vector<Card> getCards(){
	return cards;
}
public void setCards(Vector<Card> c){
	cards=c;
}
public void clear(){
	cards.removeAllElements();
}
public void addCard(Card k){
	cards.addElement(k);  //Player gets a new random card
}
public int getNumberCards(){
	return cards.size();
}

//Player-methods
public Card getCard(int p){
	if((p>=0)&&(p<cards.size()))
		return cards.elementAt(p);
	else
		return null;
}
public int currentPoints(){ //Evaluation of the value the player's deck has based on the Blackjack-rules
	int value=0, number=getNumberCards();
	boolean isAce=false;
	
	Card card;
	int cardValue;
	//go through cards
	for(int i=0; i<number; i++){
		card = getCard(i);
		cardValue=card.getValue();
		
		//Rating of cards
		if(cardValue>10) cardValue=10;
		if(cardValue==1) isAce=true;
		
		value+=cardValue;
	}
	if(isAce&&(value+10<=21))
		value=value+10;
	return value;
}
}
