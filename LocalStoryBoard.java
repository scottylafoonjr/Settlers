import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class LocalStoryBoard {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		

		
		
		
		LocalGameBoard game = new LocalGameBoard();//Randomly generated game board
		
		
		
		Object[] options = {
                  1,2,3,4
                  };
			Component frame = null;
			int n = JOptionPane.showOptionDialog(frame,
					"How many Players"
							,
							"Settlers of Farmville",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							options,
							options[2]);
			
		n = n + 1;	
		
		
		String player1 = "";
		
		while(player1==null||player1.equals(""))
		player1 = JOptionPane.showInputDialog("Player 1 username: (Type 'quit' to quit)");
		
		if(player1.equals("quit"))
			System.exit(0);
		
		
		if(n == 1){
			
			Player p1 = new Player(Color.red, "Optimus Prime",true);
			Player p2 = new Player(Color.green, "Bender",true);
			Player p3 = new Player(Color.blue, "Terminator",true);
			Player p4 = new Player(Color.yellow, player1);
			
			game.allPlayers.add(p1);
			game.allPlayers.add(p2);
			game.allPlayers.add(p3);
			game.allPlayers.add(p4);
			
			
			try {
				game.loadGame();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		else{
		
		String player2 = "";
		String player3 = "";
		String player4 = "";
		
		while(player2==null||player2.equals(""))
			player2 = JOptionPane.showInputDialog("Player 2 username: (Type 'quit' to quit)");
			
			if(player2.equals("quit"))
				System.exit(0);
			
			
		if(n > 2){
			while(player3==null||player3.equals(""))
				player3= JOptionPane.showInputDialog("Player 3 username: (Type 'quit' to quit)");
				
				if(player3.equals("quit"))
					System.exit(0);
		}	
			
		if(n > 3){
			while(player4==null||player4.equals(""))
				player4 = JOptionPane.showInputDialog("Player 2 username: (Type 'quit' to quit)");
				
				if(player4.equals("quit"))
					System.exit(0);
		}
		
		Player p1 = new Player(Color.red, player1);
		Player p2 = new Player(Color.green, player2);
		Player p3 = new Player(Color.blue, player3);
		Player p4 = new Player(Color.yellow, player4);
		
		game.allPlayers.add(p1);
		game.allPlayers.add(p2);
		
		
		if(n > 2)
			game.allPlayers.add(p3);
		
		if(n > 3)
			game.allPlayers.add(p4);
		

	}
		
		
		/*
		game.allPlayers.get(0).devCards.put(DevelopmentCardType.MONOPOLY, 2);
		game.allPlayers.get(0).devCards.put(DevelopmentCardType.ROADS, 3);
		game.allPlayers.get(0).devCards.put(DevelopmentCardType.PLAYED_KNIGHT, 4);
		game.allPlayers.get(0).devCards.put(DevelopmentCardType.YEAR_OF_PLENTY, 5);
		game.allPlayers.get(0).devCards.put(DevelopmentCardType.VICTORY_POINT, 0);
	
		
		
		//game.allPlayers.add(p3);
		//game.allPlayers.add(p4);
		
		//init everything and do what you do
		
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		
		
		
		game.allPlayers.get(1).cards.add(ResourceCardType.WHEATRC);
		game.allPlayers.get(1).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(1).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(1).cards.add(ResourceCardType.SHEEPRC);
		game.allPlayers.get(1).cards.add(ResourceCardType.STONERC);
		game.allPlayers.get(1).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(1).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(1).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(1).cards.add(ResourceCardType.BRICKRC);
		
		
		
		
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.SHEEPRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.STONERC);
		game.allPlayers.get(0).cards.add(ResourceCardType.WHEATRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.SHEEPRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.STONERC);
		game.allPlayers.get(0).cards.add(ResourceCardType.WHEATRC);
		game.allPlayers.get(0).cards.add(ResourceCardType.WHEATRC);
		
		game.allPlayers.get(1).cards.add(ResourceCardType.WHEATRC);
		game.allPlayers.get(1).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(1).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(1).cards.add(ResourceCardType.STONERC);
		game.allPlayers.get(1).cards.add(ResourceCardType.SHEEPRC);
		
		
		
		game.allPlayers.get(2).cards.add(ResourceCardType.SHEEPRC);
		game.allPlayers.get(2).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(2).cards.add(ResourceCardType.STONERC);
		game.allPlayers.get(2).cards.add(ResourceCardType.WHEATRC);
		game.allPlayers.get(2).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(2).cards.add(ResourceCardType.SHEEPRC);
		game.allPlayers.get(2).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(2).cards.add(ResourceCardType.STONERC);
		game.allPlayers.get(2).cards.add(ResourceCardType.WHEATRC);
		game.allPlayers.get(2).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(2).cards.add(ResourceCardType.WHEATRC);
		game.allPlayers.get(2).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(2).cards.add(ResourceCardType.WHEATRC);
		game.allPlayers.get(2).cards.add(ResourceCardType.FORESTRC);

		
		game.allPlayers.get(3).cards.add(ResourceCardType.FORESTRC);
		game.allPlayers.get(3).cards.add(ResourceCardType.BRICKRC);
		game.allPlayers.get(3).cards.add(ResourceCardType.SHEEPRC);
		game.allPlayers.get(3).cards.add(ResourceCardType.WHEATRC);
		game.allPlayers.get(3).cards.add(ResourceCardType.STONERC);
		*/
		
		UserInterface ui = new UserInterface();	
		ui.SetUserInterface(game);
		ui.runLocalInterface();
		
		
		
		
		
	
		
		
		
		
		

	}

}
