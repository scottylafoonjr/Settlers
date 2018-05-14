import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;


public class Simulator {
	
	
	static LocalGameBoard game = new LocalGameBoard();//Randomly generated game board

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	game.isTraining = true;
	//int learnerWins = 0;	
	int beforeWins = 0;

	//n.getBestMove(game);
	
	UserInterface ui = new UserInterface(game);
	//ui.SetUserInterface(game);
	

	RandomBot ran0 = new RandomBot();
	RandomBot ran1 = new RandomBot();
	RandomBot ran2 = new RandomBot();
	RandomBot ran3 = new RandomBot();
	//game = initGame;
	
	//ui.runLocalInterface();	
	
	game.robot0 = new NeuralNet(game, true);
	game.robot1 = new NeuralNet(game, true);
	game.robot2 = new NeuralNet(game, true);

	
	Player p1 = new Player(Color.red, "Random Bot");
	Player p2 = new Player(Color.blue, "Random Bot");
	Player p3 = new Player(Color.yellow, "Random Bot");
	Player p4 = new Player(Color.green, "Random Bot");
	
	Player robot0 = new Player(Color.red, "AI", true); //Set robot0 in gameboard
	Player robot1 = new Player(Color.blue, "AI 2", true); //set robot 1
	Player robot2 = new Player(Color.yellow, "AI 3", true); //set robot2

	Random rand = new Random();
	
	game.allPlayers.add(p1);
	game.allPlayers.add(p2);
	game.allPlayers.add(p3);
	game.allPlayers.add(p4);
	
	//UserInterface ui = new UserInterface(game);
	//ui.runInterface();

for(int round = 0; round < 30; round ++){	
	
	
	
//Start of Segment
		game.allPlayers.set(0, p1);
		game.allPlayers.set(1, p2);
		game.allPlayers.set(2, p3);
		game.allPlayers.set(3, p4);
		game.reset();
	while(true){//training robot0	
		ui.buttonAction = game.turn+" Training AI 1 #: "+round;
		System.out.println(game.turn+" Training AI 1 #: "+round);
		
		
		if(game.player == 0){
			ran0.executeNextMove(game);
		}
		else if(game.player == 1){
			ran1.executeNextMove(game);
		}
		else if(game.player == 2){
			ran2.executeNextMove(game);
		}
		else if(game.player == 3){
			ran3.executeNextMove(game);
		}
			if(game.checkGameWinner()){
				
				if(game.getWinner() == 0){
					game.robot0.updateWinNeuralNet(ran0.allMoves, game);
				}
				else if(game.getWinner() == 1){
					game.robot0.updateWinNeuralNet(ran1.allMoves, game);
				}
				else if(game.getWinner() == 2){
					game.robot0.updateWinNeuralNet(ran2.allMoves, game);
				}	
				else if(game.getWinner() == 3){
					game.robot0.updateWinNeuralNet(ran3.allMoves, game);
			}
				
				ran0.allMoves = null;
				ran0.allMoves = new ArrayList<String>();
				
				ran1.allMoves = null;
				ran1.allMoves = new ArrayList<String>();
				
				ran2.allMoves = null;
				ran2.allMoves = new ArrayList<String>();
				
				ran3.allMoves = null;
				ran3.allMoves = new ArrayList<String>();
			
				
				break;}			
		

			
			
			
		
		game.nextTurn();
		
		
	
		
		
	}
		
	
		
		
	
		
		
	


game.allPlayers.set(0, robot0);

//End of Segment



//Start of Segment

		game.reset();
	while(true){//train robot1	
		ui.buttonAction = game.turn+" Training AI 2 #: "+round;
		System.out.println(game.turn+" Training AI 2 #: "+round);

		
		if(game.player == 1){
			ran1.executeNextMove(game);
		}
		else if(game.player == 2){
			ran2.executeNextMove(game);
		}
		else if(game.player == 3){
			ran3.executeNextMove(game);
		}
			if(game.checkGameWinner()){
				
				if(game.getWinner() == 1){
					game.robot1.updateWinNeuralNet(ran1.allMoves, game);
					ran1.allMoves = null;
					ran1.allMoves = new ArrayList<String>();
					
					ran2.allMoves = null;
					ran2.allMoves = new ArrayList<String>();
					
					ran3.allMoves = null;
					ran3.allMoves = new ArrayList<String>();
				
					
					break;
				}
				else if(game.getWinner() == 2){
					game.robot1.updateWinNeuralNet(ran2.allMoves, game);
					ran1.allMoves = null;
					ran1.allMoves = new ArrayList<String>();
					
					ran2.allMoves = null;
					ran2.allMoves = new ArrayList<String>();
					
					ran3.allMoves = null;
					ran3.allMoves = new ArrayList<String>();
				
					
					break;
				}	
				else if(game.getWinner() == 3){
					game.robot1.updateWinNeuralNet(ran3.allMoves, game);
					ran1.allMoves = null;
					ran1.allMoves = new ArrayList<String>();
					
					ran2.allMoves = null;
					ran2.allMoves = new ArrayList<String>();
					
					ran3.allMoves = null;
					ran3.allMoves = new ArrayList<String>();
				
					
					break;
			}
				else{
					ran1.allMoves = null;
					ran1.allMoves = new ArrayList<String>();
					
					ran2.allMoves = null;
					ran2.allMoves = new ArrayList<String>();
					
					ran3.allMoves = null;
					ran3.allMoves = new ArrayList<String>();
				
					
					game.reset();
				}
				
		
		}			
		


		
		game.nextTurn();

	}
		
	
		
		
	
		
		
	


game.allPlayers.set(1, robot1);


//End of Segment

//Start of Segment

		game.reset();
	while(true){	//training robot2
		ui.buttonAction = game.turn+" Training AI 3 #:"+round;
		System.out.println(game.turn+" Training AI 3 #:"+round);

		

		if(game.player == 2){
			ran2.executeNextMove(game);
		}
		else if(game.player == 3){
			ran3.executeNextMove(game);
		}
	if(game.checkGameWinner()){
			
			
			if(game.getWinner() == 0){
				game.reset();
				
				ran2.allMoves = null;
				ran2.allMoves = new ArrayList<String>();
				
				ran3.allMoves = null;
				ran3.allMoves = new ArrayList<String>();
				
			
				
			}	
			else if(game.getWinner() == 2){
				
				game.robot2.updateWinNeuralNet(ran2.allMoves, game);
				
				ran2.allMoves = null;
				ran2.allMoves = new ArrayList<String>();
				
				ran3.allMoves = null;
				ran3.allMoves = new ArrayList<String>();
				
				break;
				
			}
			else if(game.getWinner() == 1){
				
				game.reset();
				
				ran2.allMoves = null;
				ran2.allMoves = new ArrayList<String>();
				
				ran3.allMoves = null;
				ran3.allMoves = new ArrayList<String>();
				
			}
			else if(game.getWinner() == 3){
				game.robot2.updateWinNeuralNet(ran3.allMoves, game);
				
				
				ran2.allMoves = null;
				ran2.allMoves = new ArrayList<String>();
				
				ran3.allMoves = null;
				ran3.allMoves = new ArrayList<String>();
				
				break;
		
		}}		
		

		
		game.nextTurn();
	
	}
		
	
		
		
	
		
		
	


game.allPlayers.set(2, robot2);
game.allPlayers.set(1, p2);
//End of Segment

//Start of Segment

		game.reset();
	while(true){//train robot1	
		ui.buttonAction = game.turn+" Training AI 2 #:"+round;
		System.out.println(game.turn+" Training AI 2 #:"+round);

		

		if(game.player == 1){
			ran1.executeNextMove(game);
		}
		else if(game.player == 3){
			ran3.executeNextMove(game);
		}
		if(game.checkGameWinner()){
			
			
			if(game.getWinner() == 0){
				game.reset();
				
				ran1.allMoves = null;
				ran1.allMoves = new ArrayList<String>();
				
				ran3.allMoves = null;
				ran3.allMoves = new ArrayList<String>();
				
			
				
			}	
			else if(game.getWinner() == 1){
				
				game.robot1.updateWinNeuralNet(ran0.allMoves, game);
				
				ran1.allMoves = null;
				ran1.allMoves = new ArrayList<String>();
				
				ran3.allMoves = null;
				ran3.allMoves = new ArrayList<String>();
				
				break;
				
			}
			else if(game.getWinner() == 2){
				
				game.reset();
				
				ran1.allMoves = null;
				ran1.allMoves = new ArrayList<String>();
				
				ran3.allMoves = null;
				ran3.allMoves = new ArrayList<String>();
				
			}
			else if(game.getWinner() == 3){
				game.robot0.updateWinNeuralNet(ran3.allMoves, game);
				
				
				ran1.allMoves = null;
				ran1.allMoves = new ArrayList<String>();
				
				ran3.allMoves = null;
				ran3.allMoves = new ArrayList<String>();
				
				break;
		
		}}		
		

		
		game.nextTurn();

	}
		
	
		
		
	
		
		
	
//End of Segment
	
	//Start of Segment
	
	game.allPlayers.set(1, robot1);
	game.allPlayers.set(0, p1);

		game.reset();
		while(true){//train robot0	
			ui.buttonAction = game.turn+" Training AI 1 #:"+round;
			System.out.println(game.turn+" Training AI 1 #:"+round);

			

			if(game.player == 0){
				ran0.executeNextMove(game);
			}
			else if(game.player == 3){
				ran3.executeNextMove(game);
			}
			if(game.checkGameWinner()){
				
				
				if(game.getWinner() == 1){
					game.reset();
					
					ran0.allMoves = null;
					ran0.allMoves = new ArrayList<String>();
					
					ran3.allMoves = null;
					ran3.allMoves = new ArrayList<String>();
					
				
					
				}	
				else if(game.getWinner() == 0){
					
					game.robot0.updateWinNeuralNet(ran0.allMoves, game);
					
					ran0.allMoves = null;
					ran0.allMoves = new ArrayList<String>();
					
					ran3.allMoves = null;
					ran3.allMoves = new ArrayList<String>();
					
					break;
					
				}
				else if(game.getWinner() == 2){
					
					game.reset();
					
					ran0.allMoves = null;
					ran0.allMoves = new ArrayList<String>();
					
					ran3.allMoves = null;
					ran3.allMoves = new ArrayList<String>();
					
				}
				else if(game.getWinner() == 3){
					game.robot0.updateWinNeuralNet(ran3.allMoves, game);
					
					
					ran0.allMoves = null;
					ran0.allMoves = new ArrayList<String>();
					
					ran3.allMoves = null;
					ran3.allMoves = new ArrayList<String>();
					
					break;
			
			}}		
			

			
			game.nextTurn();
		
		}
			
		
			
			
		
			
			
		
	//End of Segment



}


game.allPlayers.set(0, robot0);
game.allPlayers.set(1, robot1);
game.allPlayers.set(2, robot2);


for(int gameNum= 1; gameNum < 201; gameNum ++){
	

	game.reset();
	
	
while(true){	
	
	//ArrayList<String> allActions = new ArrayList<String>();
	

	

	//System.out.println("Game number: "+gameNum);
	if(gameNum > 1)
	ui.buttonAction = "#:"+gameNum+ "-"+((beforeWins*100.0)/((gameNum-1)*1.0))+"%";
	
	if(gameNum > 1)
	System.out.println("#:"+gameNum+ "-"+((beforeWins*100.0)/((gameNum-1)*1.0))+"%");
		
	//if(game.player == 0){
		//game.nextTurn();
	//}
	//if(game.player == 2){
		//ran2.executeNextMove(game);
	//}
	if(game.player == 3){
		ran3.executeNextMove(game);
	}
	

		
			
		
			
			if(game.getWinner() == 0){
			System.out.println("Game winner "+game.getWinner());
			beforeWins++;
			break;
			}
			else if(game.getWinner() == 1){
			beforeWins++;
			System.out.println("Game winner "+game.getWinner());
			break;
				}
			else if(game.getWinner() == 2){
				System.out.println("Game winner "+game.getWinner());
				beforeWins ++;
				break;
				}
			else if(game.getWinner() == 3){
				System.out.println("Game winner "+game.getWinner());
				break;
				}
		
			
				
	

	
	game.nextTurn();
	
}
	

	
	

	
	
}
	
	
	game.saveGameBoard();
	
	System.out.println("Game successfully saved!");




	}

}
