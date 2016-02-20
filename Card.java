package blackjack;

public class Card {
	//Rating of cards and definition of colors
	public final static int DIAMONDS=0,HEARTS=1,SPADES=2, CLUBS=3;
	public final static int JACK=11, QUEEN=12, KING=13, ACE=1;
	private final int color, value;

	//Constructor
	public Card(int c, int v){
		color=c;
		value=v;
	}
	public int getColor(){
		return color;
	}
	public int getValue(){
		return value;
	}
	//Card-methods
	public String Color2String(){
		switch(color){
		case DIAMONDS:
			return "Diamonds";
		case HEARTS:
			return "Hearts";
		case SPADES:
			return "Spades";
		case CLUBS:
			return "Clubs";
		}
		System.out.println("Color wrong!:"+color);
		return"-1";
	}
	public String Value2String(){
		if ((value>=2)&&(value<=10))
			return ""+value;
		switch(value){
		case 1:
			return "A";
		case 11:
			return "J";
		case 12:
			return "Q";
		case 13:
			return "K";
		}
		return "-1";


	}
	public String Card2String(){
		return Color2String()+"-"+Value2String();
	}
	public int getRealValue(){  //value based on Blackjack-rules
		if ((value>=2)&&(value<=10))
			return value;
		switch(value){
		case 1:
			return 1;
		case 11:
			return 10;
		case 12:
			return 10;
		case 13:
			return 10;
		}
		return -1;

	}

}
