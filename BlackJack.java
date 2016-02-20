package blackjack;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BlackJack {
	private CardGame cardGame;
	private Player player, dealer;
	private int stake;  //current money we play for
	private boolean gameongoing;
	private boolean insurance; //is an insurance purchased?
	private boolean split; //is the deck splitted?
	private Card splitCard; //second card of the split
	private int splitStake; //stake at which the deck was splitted

	//Constructor
	public BlackJack (String n){
		cardGame=new CardGame();
		player = new Player(n, 500);
		dealer = new Player("Dealer", 10000);
		stake=0;
		gameongoing=false;
		insurance=false;
		split=false;
		splitCard=null;
		splitStake=0;

	}
	public CardGame getCardGame (){
		return cardGame;
	}
	public void setCardGame(CardGame cg){
		cardGame=cg;
	}
	public Player getPlayer(){
		return player;
	}
	public void setPlayer(Player p){
		player=p;
	}
	public Player getDealer(){
		return dealer;
	}
	public void setDealer(Player d){
		dealer=d;
	}
	public int getStake(){
		return stake;
	}
	public void setStake(int s){
		stake=s;
	}
	public int getSplitStake(){
		return splitStake;
	}
	public void setSplitStake(int s){
		splitStake=s;
	}
	public Card getSplitCard(){
		return splitCard;
	}
	public void setSplitCard(Card c){
		splitCard=c;
	}
	public boolean getGameongoing(){
		return gameongoing;
	}
	public void setGameongoing(boolean b){
		gameongoing=b;
	}
	public boolean getInsurance(){
		return insurance;
	}
	public void setInsurance(boolean b){
		insurance=b;
	}
	public boolean getSplit(){
		return split;
	}
	public void setSplit(boolean b){
		split=b;
	}
	//BlackJack-Methods
	public void newGame(){
		player.clear();
		dealer.clear();
		player.addCard(cardGame.dealACard());
		dealer.addCard(cardGame.dealACard());
		player.addCard(cardGame.dealACard());


		if((player.getCard(0).getRealValue()==player.getCard(1).getRealValue())&&(split==false)){//is a split possible?
			outputCardsPlayer(player, dealer);
			System.out.println("If you want to split your deck, press 's'.\n "
					+ "You will get a new second card and play the next round with the same stake with"
					+ " your current second card and another");
			InputStreamReader stdin2=new InputStreamReader(System.in);
			BufferedReader console2 = new BufferedReader(stdin2);
			String answer2=" ";
			try{
				answer2=console2.readLine();
			}catch(IOException ioex){
				System.out.println("Error");
				System.exit(1);
			}
			if (answer2.equals("s")){
				split=true;
				splitCard=player.getCard(1); //the other card and the stake are saved
				splitStake=stake;
				player.setMoney(player.getMoney()-stake/2); //player has to pay the bet for the second deck
				dealer.setMoney(dealer.getMoney()-stake/2);
				player.getCards().remove(1);
				player.addCard(cardGame.dealACard());  //player gets a new second card
				System.out.println("First deck is played");
				outputCardsPlayer(player, dealer);
				gameongoing=true;

			}
		}else if(split==true){
			player.getCards().remove(1); //if the deck was already splitted, we start with the second deck
			player.addCard(splitCard);
			stake=splitStake;
			split=false;
			System.out.println("Second deck is played");
			outputCardsPlayer(player, dealer);
			gameongoing=true;

		}
		if(dealer.getCard(0).getValue()==1&&((player.getMoney()-stake)>=0)){//based on the blackjack rules, you may get an insurance 
			//against dealer's blackjack if he has an Ace - we check also if the player has enough money to pay it
			outputCardsPlayer(player, dealer);
			System.out.println("Do you want an insurance in case of a dealer-BLACKJACK? It costs as much as the current stake and pays 2:1,\n"
					+ "but only if the dealer has a BLACKJACK. You will also loose no money in that case. \nAnswer 'yes' or 'no'");
			InputStreamReader stdin=new InputStreamReader(System.in);
			BufferedReader console = new BufferedReader(stdin);
			String answer=" ";
			try{
				answer=console.readLine();
			}catch(IOException ioex){
				System.out.println("Error");
				System.exit(1);
			}
			if (answer.equals("yes")){
				insurance=true;
				player.setMoney(player.getMoney()-stake);
				System.out.println("You got an insurance for "+stake+"$");
			}
		}
		outputCardsPlayer(player, dealer);
		gameongoing=true;

	}
	public void newCard(){
		player.addCard(cardGame.dealACard());
	}
	public void dealersTurn(){ //dealer only gets a new card if he has less then 17 Points
		while(dealer.currentPoints()<=16)
			dealer.addCard(cardGame.dealACard());
	}
	public boolean increaseStake(){//limited step size
		if(dealer.getMoney()>=25&&player.getMoney()>=25){
			dealer.setMoney(dealer.getMoney()-25);
			player.setMoney(player.getMoney()-25);
			stake+=50;
			return true;
		}
		else if (dealer.getMoney()<=25){ //dealer has not enough money
			System.out.println();
			System.out.println("WOW! You busted the bank!!!");
			System.exit(1);
		}
		return false;
	}

	//********************************
	//static methods
	private static void help(){
		System.out.println();
		System.out.println("Enter: ");
		System.out.println(" n = new card");
		System.out.println(" d = ready, dealer's turn");
		System.out.println(" + = higher stake for 50$");
		System.out.println(" r = new round");
		System.out.println(" 2 = double stake");
		System.out.println(" x = end game");
		System.out.println(" ? = help");
		System.out.println("Standard rules - first do your bet, then you get two cards and the dealer one card. \n"
				+ "Try to reach 21 as good as possible without busting. If you have more points than the dealer or "
				+ "the dealer gets over 21, you get a price as high as your stake.\nIf you have a BLACKJACK (Ace and King, Queen or Jack)"
				+ "or three times the seven, you get three dollar for each two dollar bet. Stalemate means no money is lost for you "
				+ "and the dealer.\nYou are only allowed to split your first 2 cards.\nYou may double your stake after getting your "
				+ "first 2 cards - you get only 1 card after that.");
		System.out.println();

	}

	private static void outputCardsPlayer(Player s, Player d){//prints out all cards
		System.out.println();
		System.out.println("You have: ");
		for (int i=0; i<s.getNumberCards(); i++){
			Card card=s.getCard(i);
			System.out.println(card.Card2String()+" ");
		}
		System.out.println(" (Points="+s.currentPoints()+")");
		System.out.println("The dealer has: ");
		for (int i=0; i<d.getNumberCards(); i++){
			Card card=d.getCard(i);
			System.out.println(card.Card2String()+" ");
		}
		System.out.println("(Points="+d.currentPoints()+")");
		System.out.println();

	}
	private static void accountData (Player s, Player d){//prints out current money
		System.out.println();
		System.out.println("$$$ "+s.getName()+": "+s.getMoney()+", bank: "+d.getMoney()+" $$$");
		System.out.println();
	}
	public static void evaluate(BlackJack blackjack){//evaluates the game situation based on Blackjack-rules 
		if(blackjack.getDealer().currentPoints()>21){//not more than 21 points!
			System.out.println("You won! Dealer ist over 21: "+blackjack.getDealer().currentPoints());
			blackjack.getPlayer().setMoney(blackjack.getPlayer().getMoney()+blackjack.stake);
		}else if((blackjack.getDealer().currentPoints()==blackjack.getPlayer().currentPoints())&&
				(blackjack.getDealer().getNumberCards()==2)&&(blackjack.getPlayer().getNumberCards()==2)){//both have a Blackjack
			System.out.println("Stand off!");
			if(blackjack.insurance){//insurance is active!
				blackjack.getPlayer().setMoney(blackjack.getPlayer().getMoney()+2*blackjack.stake);
				blackjack.getDealer().setMoney(blackjack.getDealer().getMoney()-blackjack.stake);
			}else{
			blackjack.getDealer().setMoney(blackjack.getDealer().getMoney()+blackjack.stake/2);
			blackjack.getPlayer().setMoney(blackjack.getPlayer().getMoney()+blackjack.stake/2);}
		}else if((blackjack.getDealer().currentPoints()==21)&&(blackjack.getDealer().getNumberCards()==2)){//dealer has Blackjack
			System.out.println("Already lost - dealer got 21 points with a BLACKJACK!");

			if(blackjack.insurance){//insurance active!
				blackjack.getPlayer().setMoney(blackjack.getPlayer().getMoney()+2*blackjack.stake);
				blackjack.getDealer().setMoney(blackjack.getDealer().getMoney()-blackjack.stake);
			}else{
				blackjack.getDealer().setMoney(blackjack.getDealer().getMoney()+blackjack.stake);//dealer is only payed 2:1
			}
			blackjack.setInsurance(false);
			blackjack.stake=0;

		}else if(blackjack.getDealer().getNumberCards()==5){//dealer wins if he reaches 5 cards
			System.out.println("You lost - dealer got 5 cards lower than 21");
			blackjack.getDealer().setMoney(blackjack.getDealer().getMoney()+blackjack.stake);

		}else if((blackjack.getDealer().currentPoints()==blackjack.getPlayer().currentPoints())&&
				(blackjack.getDealer().getNumberCards()!=2)&&(blackjack.getPlayer().getNumberCards()!=2)){//both have equally points
			System.out.println("Stand off!");
			blackjack.getDealer().setMoney(blackjack.getDealer().getMoney()+blackjack.stake/2);
			blackjack.getPlayer().setMoney(blackjack.getPlayer().getMoney()+blackjack.stake/2);


		}else if((blackjack.getPlayer().currentPoints()==21)&&(blackjack.getPlayer().getNumberCards()==2)){//Player has Blackjack - he's payed 3:2
			System.out.println("Already won - you got 21 points with a BLACKJACK!");
			blackjack.getDealer().setMoney(blackjack.getDealer().getMoney()-blackjack.stake/2);
			blackjack.getPlayer().setMoney(blackjack.getPlayer().getMoney()+3*(blackjack.stake/2));
			blackjack.stake=0;

		}else if (blackjack.getDealer().currentPoints()>blackjack.getPlayer().currentPoints()){
			System.out.println("You lost. "+blackjack.getDealer().currentPoints()+" to "+blackjack.getPlayer().currentPoints());
			blackjack.getDealer().setMoney(blackjack.getDealer().getMoney()+blackjack.stake);
		}else {
			System.out.println("You won! "+blackjack.getPlayer().currentPoints()+" to "+blackjack.getDealer().currentPoints());
			blackjack.getPlayer().setMoney(blackjack.getPlayer().getMoney()+blackjack.stake);
		}
	}
	public static void newCardCheck(BlackJack blackjack){//checks the players cards immediatly
		if((blackjack.getPlayer().currentPoints()==21)&&(blackjack.getPlayer().getCard(0).getValue()==7)&&
				(blackjack.getPlayer().getCard(1).getValue()==7)){//Triple seven detected
			System.out.println("TRIPLE SEVEN!");
			blackjack.getDealer().setMoney(blackjack.getDealer().getMoney()-blackjack.stake/2);
			blackjack.getPlayer().setMoney(blackjack.getPlayer().getMoney()-3*blackjack.stake/2);
			blackjack.stake=0;
			blackjack.gameongoing=false;
			accountData(blackjack.getPlayer(),blackjack.getDealer());
		}
		if(blackjack.getPlayer().currentPoints()>21){//more than 21 points detected
			outputCardsPlayer(blackjack.getPlayer(),blackjack.getDealer());
			System.out.println("You lost - you have more than 21 points: "+blackjack.getPlayer().currentPoints());
			blackjack.getDealer().setMoney(blackjack.getDealer().getMoney()+blackjack.stake);
			blackjack.stake=0;
			accountData(blackjack.getPlayer(),blackjack.getDealer());
			blackjack.gameongoing=false;
		}else{
			outputCardsPlayer(blackjack.getPlayer(),blackjack.getDealer());
		}
		
	}
	public static void main(String[] args) {
		System.out.println("--------------------------");
		System.out.println("Welcome to the table!");
		System.out.println("--------------------------");
		help();

		InputStreamReader stdin=new InputStreamReader(System.in);
		BufferedReader console = new BufferedReader(stdin);

		System.out.print("Enter your name: ");
		String name=" ";
		try{
			name=console.readLine();
		}catch(IOException ioex){
			System.out.println("Error");
			System.exit(1);
		}
		System.out.println();
		System.out.println("Hello "+name+", you have 500$. Do your bet(+) and start the game(r).");
		System.out.println();

		//Starting the round
		BlackJack blackjack = new BlackJack(name);
		accountData(blackjack.getPlayer(), blackjack.getDealer());
		boolean isReady=false;
		String input="";
		while(!isReady){//menu
			try{
				input=console.readLine();
			}catch(IOException ioex){
				System.out.println("Error");
			}
			if(input.equals("n")){
				//another card
				if(blackjack.getGameongoing()){
					blackjack.newCard();
					newCardCheck(blackjack);

				}
			}else if (input.equals("d")){
				//Game goes to dealer
				if((blackjack.getStake()>0&&(blackjack.getGameongoing()))){
					blackjack.dealersTurn();
					outputCardsPlayer(blackjack.getPlayer(),blackjack.getDealer());
					evaluate(blackjack);

					accountData(blackjack.getPlayer(),blackjack.getDealer());
					blackjack.stake=0;//stake reseted
					blackjack.gameongoing=false;
				}
			}else if (input.equals("+")){
				//increase stake
				if(blackjack.getGameongoing()||blackjack.split==true)//after split, stake is frozen
					System.out.println("The game ist already ongoing. No increasement possible");
				else{


					if ((!(blackjack.increaseStake())&&(blackjack.getStake()==0))|| //player has no money
							(blackjack.getPlayer().getMoney()<=0)&&(blackjack.getStake()==0)){
						System.out.println("You have not enough money!");
						System.out.println("GAME OVER!");
						System.exit(0);
					}else if (!(blackjack.increaseStake())){//player has no money, but can still win some
						System.out.println("You have not enough money!");}
					else
						System.out.println("Stake increased by 50$. The jackpot is "+blackjack.getStake()+"$");
				}accountData(blackjack.getPlayer(),blackjack.getDealer());
			}else if(input.equals("r")){
				//new round started after stakes were done
				if(((blackjack.getStake()>0)&&(!blackjack.getGameongoing()))||blackjack.split==true){
					blackjack.newGame();
					accountData(blackjack.getPlayer(),blackjack.getDealer());

				}
				else{
					System.out.print("You have to do a bet or the game is already ongoing!");
				}
			}else if(input.equals("2")){//stake is doubled, player gets exactly one card and the game is evaluated
				if(blackjack.getStake()>0&&(blackjack.getGameongoing())){
					blackjack.player.setMoney(blackjack.player.getMoney()-blackjack.getStake()/2);
					blackjack.dealer.setMoney(blackjack.dealer.getMoney()-blackjack.getStake()/2);
					blackjack.setStake(2*blackjack.getStake());
					blackjack.player.addCard(blackjack.cardGame.dealACard());
					newCardCheck(blackjack);
					if(blackjack.gameongoing==true){
					blackjack.dealersTurn();
					outputCardsPlayer(blackjack.getPlayer(),blackjack.getDealer());
					evaluate(blackjack);}
					accountData(blackjack.getPlayer(),blackjack.getDealer());
					blackjack.stake=0;
					blackjack.gameongoing=false;}
			}else if (input.equals("?")){
				help();
			}else if(input.equals("x")){
				isReady=true;
				break;
			}

		}

	}
}
