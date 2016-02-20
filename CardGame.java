package blackjack;

public class CardGame {
	//52 cards (2-10,J,Q,K,A for 4 colors)
	private Card[] pile;
	private int cardsInGame;
	
	//Constructor, creates 52 cards an mixes them
	public CardGame(){
		pile = new Card[52];
		int counter=0;
		for(int f=0;f<4;f++){
			for(int w=1; w<14;w++){
				pile[counter]=new Card(f,w);
				counter++;
			}
		}
		mix();
	}
	//CardGame-Methods
	public void mix(){
		Card temp;
		for(int i=51;i>0;i--){
			int luck=(int)(Math.random()*(i+1));
			temp=pile[i];
			pile[i]=pile[luck];
			pile[luck]=temp;
		}
		cardsInGame=52;
	}
	public int cardsNumber(){
		return cardsInGame;
	}
	public Card dealACard(){
		if(cardsInGame==0)
			mix();
		cardsInGame--;
		return pile[cardsInGame];
	}

}
