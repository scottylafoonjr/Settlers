import java.util.ArrayList;
import java.util.Random;


public class RandomBot {
	
	ArrayList<String> allMoves = new ArrayList<String>();
	
public void executeNextMove(LocalGameBoard game){
	boolean nextMove = true;
	
	while(nextMove){
	
		
		nextMove = false;
	
		
		ArrayList<String> allActions = new ArrayList<String>();
		allActions = game.allPossibleActions();
		
		Random rand = new Random();
		
		String input = "nothing";
		
		if(allActions.size() > 0){
		int random = rand.nextInt(allActions.size());
		input = allActions.get(random);}
		
	
		
		
		

		
		String[] split = input.split(" ");
		if(split[0].equals("Settlement")){
			game.placeSettlement(Integer.parseInt(split[1]));
			allMoves.add(input);
			nextMove = true;
		}
		else if(split[0].equals("Path")){
			game.placePath(Integer.parseInt(split[1]));
			allMoves.add(input);
			nextMove = true;
		}
		else if(split[0].equals("City")){
			game.placeCity(Integer.parseInt(split[1]));
			allMoves.add(input);
			nextMove = true;
		}
		else if(split[0].equals("Trade")&&(game.getAmountOfRoads()>10||game.player == 2)){			
			game.tradeWithBank(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
			allMoves.add(input);
			nextMove = true;
		}
		else if(split[0].equals("Buy")&&(game.player != 0)&&(game.turn > 25||game.player != 1)){			
			game.buyDevCard();
			allMoves.add(input);
			nextMove = true;
		}
		else if(split[0].equals("Knight")){			
			game.playKnight();
			allMoves.add(input);
			nextMove = true;
		}
		else if(split[0].equals("Plenty")){			
			if(game.turn < 20)
			game.playPlenty(0, 4);
			else
			game.playPlenty(1, 2);
			
			allMoves.add(input);
			nextMove = true;
		}
		else if(split[0].equals("Monopoly")){	
			Random ran = new Random();
			game.playMonopoly(ran.nextInt(5));
			
			allMoves.add(input);
			nextMove = true;
		}
		else if(split[0].equals("Roads")){			
			game.playRoads();
			
			allMoves.add(input);
			nextMove = true;
		}
		else if(split[0].equals("Robber")){
			game.placeRobber(Integer.parseInt(split[1]));
			
			allMoves.add(input);
			nextMove = true;
		}
		
		
		
		
		
		
}
	}
	
	

	
	

}
