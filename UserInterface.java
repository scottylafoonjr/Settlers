import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class UserInterface extends JPanel{
	
	static boolean first = false;
	static int screenHeight = 800;
	static int screenWidth = 1200;
	//final static int screenWidth = screenHeight;
	static boolean continueGame = true;
	static boolean buyCard = false;
    Font font2 = new Font("Jokerman", Font.PLAIN, 20);
    Font font4 = new Font("Jokerman", Font.PLAIN, 50);
    Font font5 = new Font("Jokerman", Font.PLAIN, 16);
	private static final int UPDATE_RATE = 12;
	static JFrame frame = new JFrame();
	static String buttonAction = "End Turn";
	static BufferedImage brickImage;
	static BufferedImage forestImage;
	static BufferedImage sandImage;
	static BufferedImage sheepImage;
	static BufferedImage stoneImage;
	static BufferedImage wheatImage;
	static BufferedImage robberImage;
	static BufferedImage portImage;
	static BufferedImage brickResourceCard;
	static BufferedImage forestResourceCard;
	static BufferedImage sheepResourceCard;
	static BufferedImage stoneResourceCard;
	static BufferedImage wheatResourceCard;
	static BufferedImage gameboardBackground;
	static BufferedImage KnightDevCard;
	static BufferedImage VictoryPointDevCard;
	static BufferedImage RoadDevCard;
	static BufferedImage MonopolyDevCard;
	static BufferedImage YearOfPlentyDevCard;
	static BufferedImage costCard;
	static int playerCardSelect = 0;
	static int cardSelect = 0;
	static HashSet<Integer> selectedJoints = new HashSet<Integer>();
	static HashSet<DevelopmentCardType> selectedCards = new HashSet<DevelopmentCardType>();
	static LocalGameBoard gameBoard;
	static public boolean seeMap = true;
	static public boolean seeBank = false;
	static int devCardSelect = 0;
	static boolean gameOver = false;
	

	
	public UserInterface() {
	
			
		
		try {
			//brickImage = ImageIO.read(new File("brick_hex.png"));
			brickImage = ImageIO.read(getClass().getResourceAsStream("Extra/brick_hex.png"));
			forestImage = ImageIO.read(getClass().getResourceAsStream("Extra/forest_hex.png"));
			sandImage = ImageIO.read(getClass().getResourceAsStream("Extra/sand_hex.png"));
			sheepImage = ImageIO.read(getClass().getResourceAsStream("Extra/sheep_hex.png"));
			stoneImage = ImageIO.read(getClass().getResourceAsStream("Extra/stone_hex.png"));
			wheatImage = ImageIO.read(getClass().getResourceAsStream("Extra/wheat_hex.png"));
			robberImage = ImageIO.read(getClass().getResourceAsStream("Extra/robber.png"));
			brickResourceCard = ImageIO.read(getClass().getResourceAsStream("Extra/Brick.png"));
			forestResourceCard = ImageIO.read(getClass().getResourceAsStream("Extra/Lumber.png"));
			sheepResourceCard = ImageIO.read(getClass().getResourceAsStream("Extra/Sheep.png"));
			stoneResourceCard = ImageIO.read(getClass().getResourceAsStream("Extra/Stone.png"));
			wheatResourceCard = ImageIO.read(getClass().getResourceAsStream("Extra/Wheat.png"));
			//gameboardBackground = ImageIO.read(new File("Extra/background.png"));
			gameboardBackground = ImageIO.read(getClass().getResourceAsStream("Extra/background.png"));
			KnightDevCard = ImageIO.read(getClass().getResourceAsStream("Extra/Knight.png"));
			VictoryPointDevCard = ImageIO.read(getClass().getResourceAsStream("Extra/University.png"));
			RoadDevCard = ImageIO.read(getClass().getResourceAsStream("Extra/Roads.png"));
			MonopolyDevCard = ImageIO.read(getClass().getResourceAsStream("Extra/Monopoly.png"));
			YearOfPlentyDevCard = ImageIO.read(getClass().getResourceAsStream("Extra/YearOfPlenty.png"));
			costCard = ImageIO.read(getClass().getResourceAsStream("Extra/cost_card.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
		
		
		
		
		

	      Thread gameThread = new Thread() {
	         public void run() {
	            while (continueGame) { 
	            
	            
	            	
	               repaint(); // Callback paintComponent()
	               // Delay for timing control and give other threads a chance
	               try {
	                  Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
	               } catch (InterruptedException ex) { }
	            }
	         }
	      };
	      gameThread.start();  // Callback run()
	   }
	
	//paint 
	
	 public UserInterface(LocalGameBoard game) {
		// TODO Auto-generated constructor stub
		 gameBoard = game;
	}

	@Override
	   public void paintComponent(Graphics g) {
	      super.paintComponent(g);    // Paint background
	      
	      Graphics2D g2 = (Graphics2D) g;
	      
	      JPanel totalGUI = new JPanel();
	      totalGUI.setLayout(null);
	      
	      
	      if(gameBoard == null){
	    	  System.exit(1);
	      }
	  
	      // Draw the box
	      g2.setColor(gameBoard.getCurrentPlayer().color);
	      g2.fillRect(0, 0, screenWidth, screenHeight);
	      
	      g2.setColor(Color.black);
	       
	      
	      

	if(seeMap){
		   g2.drawImage(gameboardBackground,(int)(screenWidth*.38), (int)(screenHeight*.05), (int)(screenWidth*.62), (int)(screenHeight*.88),this);
		   //Port image
	      
	      
	      for(int i = 0; i < gameBoard.allHexes.size(); i++){
	    if(gameBoard.allHexes.get(i).wasCreated){
	      //g2.setColor(gameBoard.allHexes.get(i).color); set up the colors
	      //g2.fillPolygon(gameBoard.allHexes.get(i).xLocs, gameBoard.allHexes.get(i).yLocs, 6);
	      g2.drawImage(gameBoard.allHexes.get(i).getImage()
	    		  , (int)((gameBoard.allHexes.get(i).centerX*screenWidth) - ((int)(.05*screenWidth)))
	    		  , (int)((gameBoard.allHexes.get(i).centerY*screenHeight) - ((int)(.095*screenHeight)))
	    		  , (int)(.1*screenWidth)
	    		  , (int)(.195*screenHeight)
	    		  , this);
	      
	      if(gameBoard.rob != null){
	      if(i == gameBoard.rob.location){
	    	  g2.drawImage(robberImage
		    		  , (int)((gameBoard.allHexes.get(i).centerX*screenWidth) - ((int)(.05*screenWidth)))
		    		  , (int)((gameBoard.allHexes.get(i).centerY*screenHeight) - ((int)(.1*screenHeight)))
		    		  , (int)(.1*screenWidth)
		    		  , (int)(.195*screenHeight)
		    		  , this);
	      }}
	      
	      g2.setColor(Color.cyan);
	      Font font = new Font("Jokerman", Font.PLAIN, 35);
	      g2.setFont(font);
	      if(gameBoard.allHexes.get(i).number != -1){
		  g2.drawString(""+gameBoard.allHexes.get(i).number
	    		  , (int)((gameBoard.allHexes.get(i).centerX*screenWidth))
	    		  , (int)((gameBoard.allHexes.get(i).centerY*screenHeight))
	    		);}
	      
	      
	      
	    }}
	      
	     g2.setStroke(new BasicStroke(20));
	      
	      for(int i = 0; i < gameBoard.allPaths.size();i++){
	    	  g2.setColor(gameBoard.allPaths.get(i).color);
	    	  g2.drawLine((int)(gameBoard.allPaths.get(i).xLoc1*screenWidth),
	    			  (int)(gameBoard.allPaths.get(i).yLoc1*screenHeight)
	    			  ,(int)( gameBoard.allPaths.get(i).xLoc2* screenWidth)
	    			  ,(int)( gameBoard.allPaths.get(i).yLoc2* screenHeight));
	      }
	      
	      for(int i =0; i < gameBoard.allJoints.size();i++){
	     	  g2.setColor(gameBoard.allJoints.get(i).color);
	     	  
	     if(selectedJoints.contains(i)){
	    	 
	    	 if(!gameBoard.allJoints.get(i).city){
	    	  g2.fillOval((int)(gameBoard.allJoints.get(i).xLoc*screenWidth) - (int)(gameBoard.allJoints.get(i).getSize()*1.5)/2
	    			  ,(int)(gameBoard.allJoints.get(i).yLoc*screenHeight)- (int)(gameBoard.allJoints.get(i).getSize()*1.5)/2
	    			  , (int)(gameBoard.allJoints.get(i).getSize()*1.5)
	    			  ,(int)(gameBoard.allJoints.get(i).getSize()*1.5)
	    			  );}
	    	 else{
	    		 g2.fillRect((int)(gameBoard.allJoints.get(i).xLoc*screenWidth) - (int)(gameBoard.allJoints.get(i).getSize()*1.5)/2
		    			  ,(int)(gameBoard.allJoints.get(i).yLoc*screenHeight)- (int)(gameBoard.allJoints.get(i).getSize()*1.5)/2
		    			  , (int)(gameBoard.allJoints.get(i).getSize()*1.5)
		    			  ,(int)(gameBoard.allJoints.get(i).getSize()*1.5)
		    			  );
	
	    	 }	 
	     
	     }
	     else{
	    	if(!gameBoard.allJoints.get(i).city){ 
	    	  g2.fillOval((int)(gameBoard.allJoints.get(i).xLoc*screenWidth) - gameBoard.allJoints.get(i).getSize()/2
	    			  ,(int)(gameBoard.allJoints.get(i).yLoc*screenHeight)- gameBoard.allJoints.get(i).getSize()/2
	    			  , gameBoard.allJoints.get(i).getSize() 
	    			  ,gameBoard.allJoints.get(i).getSize());
	     }else{
	    	 g2.fillRect((int)(gameBoard.allJoints.get(i).xLoc*screenWidth) - gameBoard.allJoints.get(i).getSize()/2
	    			  ,(int)(gameBoard.allJoints.get(i).yLoc*screenHeight)- gameBoard.allJoints.get(i).getSize()/2
	    			  , gameBoard.allJoints.get(i).getSize() 
	    			  ,gameBoard.allJoints.get(i).getSize());
	    	 
	    	 
	     } }
	      }
	      
	      
	}
	else if(seeBank){//Graphics for bank
	
		g2.setFont(font2);
		g2.setColor(Color.black);
		g2.drawString("Trade Away", (int)(screenWidth*.45), (int)(screenHeight*.15));
		
		g2.drawString("Recieve", (int)(screenWidth*.75), (int)(screenHeight*.15));
		
		g2.setFont(font4);
		g2.setColor(Color.cyan);
		
		
		g2.drawImage(this.costCard, (int)(screenWidth*.575), (int)(screenHeight*.6), 
				(int)(screenWidth*.2),(int)(screenHeight*.3), this);
		
		
		g2.drawImage(this.sheepResourceCard, (int)(screenWidth*.5), (int)(screenHeight*.2), 
				(int)(screenWidth*.05),(int)(screenHeight*.1), this);
		if(playerCardSelect == 1)
		g2.drawString(">>>>>", (int)(screenWidth*.45), (int)(screenHeight*.25));
		
	
		
		g2.drawImage(this.wheatResourceCard, (int)(screenWidth*.5), (int)(screenHeight*.3), 
				(int)(screenWidth*.05),(int)(screenHeight*.1), this);
		if(playerCardSelect == 2)
		g2.drawString(">>>>>", (int)(screenWidth*.45), (int)(screenHeight*.35));
		
		
		g2.drawImage(this.forestResourceCard, (int)(screenWidth*.5), (int)(screenHeight*.4), 
				(int)(screenWidth*.05),(int)(screenHeight*.1), this);
		if(playerCardSelect == 3)
		g2.drawString(">>>>>", (int)(screenWidth*.45), (int)(screenHeight*.45));
		
		
		g2.drawImage(this.stoneResourceCard, (int)(screenWidth*.5), (int)(screenHeight*.5), 
				(int)(screenWidth*.05),(int)(screenHeight*.1), this);
		if(playerCardSelect == 4)
		g2.drawString(">>>>>", (int)(screenWidth*.45), (int)(screenHeight*.55));

		
		g2.drawImage(this.brickResourceCard, (int)(screenWidth*.5), (int)(screenHeight*.6), 
				(int)(screenWidth*.05),(int)(screenHeight*.1), this);
		if(playerCardSelect == 5)
		g2.drawString(">>>>>", (int)(screenWidth*.45), (int)(screenHeight*.65));
		
		
		
		
		g2.drawImage(this.sheepResourceCard, (int)(screenWidth*.8), (int)(screenHeight*.2), 
				(int)(screenWidth*.05),(int)(screenHeight*.1), this);
		if(cardSelect == 1)
		g2.drawString("<<<<<", (int)(screenWidth*.9), (int)(screenHeight*.25));
		
		g2.drawImage(this.wheatResourceCard, (int)(screenWidth*.8), (int)(screenHeight*.3), 
				(int)(screenWidth*.05),(int)(screenHeight*.1), this);
		
		if(cardSelect == 2)
		g2.drawString("<<<<<", (int)(screenWidth*.9), (int)(screenHeight*.35));
		
		g2.drawImage(this.forestResourceCard, (int)(screenWidth*.8), (int)(screenHeight*.4), 
				(int)(screenWidth*.05),(int)(screenHeight*.1), this);
		
		if(cardSelect == 3)
		g2.drawString("<<<<<", (int)(screenWidth*.9), (int)(screenHeight*.45));
		
		g2.drawImage(this.stoneResourceCard, (int)(screenWidth*.8), (int)(screenHeight*.5), 
				(int)(screenWidth*.05),(int)(screenHeight*.1), this);
	
		if(cardSelect == 4)
		g2.drawString("<<<<<", (int)(screenWidth*.9), (int)(screenHeight*.55));
		
		g2.drawImage(this.brickResourceCard, (int)(screenWidth*.8), (int)(screenHeight*.6), 
				(int)(screenWidth*.05),(int)(screenHeight*.1), this);
		
		if(cardSelect == 5)
		g2.drawString("<<<<<", (int)(screenWidth*.9), (int)(screenHeight*.65));
		
		
		
	}//graphics for bank
	else{//graphics for dev cards
		
		String s ="";
		g2.setFont(font2);
		g2.setColor(Color.red);
		
		g2.drawImage(this.VictoryPointDevCard, (int)(screenWidth*.4), (int)(screenHeight*.1), 
				(int)(screenWidth*.15),(int)(screenHeight*.3), this);
		s = ""+gameBoard.allPlayers.get(gameBoard.player).devCards.get(DevelopmentCardType.VICTORY_POINT);
		g2.drawString(s, (int)(screenWidth*.5), (int)(screenHeight*.2));
		
	
		g2.drawImage(this.KnightDevCard, (int)(screenWidth*.55), (int)(screenHeight*.1), 
				(int)(screenWidth*.15),(int)(screenHeight*.3), this);
		s = ""+gameBoard.allPlayers.get(gameBoard.player).devCards.get(DevelopmentCardType.KNIGHT);
		g2.drawString(s, (int)(screenWidth*.65), (int)(screenHeight*.25));
		
		g2.drawImage(this.YearOfPlentyDevCard, (int)(screenWidth*.7), (int)(screenHeight*.1), 
				(int)(screenWidth*.15),(int)(screenHeight*.3), this);
		s = ""+gameBoard.allPlayers.get(gameBoard.player).devCards.get(DevelopmentCardType.YEAR_OF_PLENTY);
		g2.drawString(s, (int)(screenWidth*.8), (int)(screenHeight*.25));
		
		g2.drawImage(this.MonopolyDevCard, (int)(screenWidth*.85), (int)(screenHeight*.1), 
				(int)(screenWidth*.15),(int)(screenHeight*.3), this);
		s = ""+gameBoard.allPlayers.get(gameBoard.player).devCards.get(DevelopmentCardType.MONOPOLY);
		g2.drawString(s, (int)(screenWidth*.865), (int)(screenHeight*.25));
		
		
		
		g2.drawImage(this.RoadDevCard, (int)(screenWidth*.4), (int)(screenHeight*.4), 
				(int)(screenWidth*.15),(int)(screenHeight*.3), this);
		s = ""+gameBoard.allPlayers.get(gameBoard.player).devCards.get(DevelopmentCardType.ROADS);
		g2.drawString(s, (int)(screenWidth*.45), (int)(screenHeight*.5));
		
		
		g2.setColor(Color.black);
		g2.fillRect((int)(screenWidth*.85), (int)(screenHeight*.4), 
				(int)(screenWidth*.15),(int)(screenHeight*.3));
		
		if(buyCard){
			g2.setColor(Color.green);
		}
		else
			g2.setColor(Color.red);
		
		
		g2.fillRect((int)(screenWidth*.865), (int)(screenHeight*.415), 
				(int)(screenWidth*.11),(int)(screenHeight*.265));
		
		g2.setColor(Color.black);
		g2.setFont(this.font5);
		g2.drawString("Buy Card", (int)(screenWidth*.88), (int)(screenHeight*.5515));
		
		
		

		
	for(int x = 0; x < gameBoard.allPlayers.size(); x++){
		
		g2.setColor(gameBoard.allPlayers.get(x).color);
		g2.fillRect((int)(screenWidth*.4+(int)(x*.15*screenWidth)), (int)(screenHeight*.7), 
				(int)(screenWidth*.15),(int)(screenHeight*.3));
		
		g2.setColor(Color.black);
		g2.setFont(font5);
		
		if(gameBoard.largestArmyPlayer == x)
		g2.drawString("Largest Army"
				, (int)(screenWidth*.4)+(int)(x*.15*screenWidth), (int)(screenHeight*.75));
		
		if(gameBoard.longestRoadPlayer == x)
		g2.drawString("Longest Road"
				, (int)(screenWidth*.4)+(int)(x*.15*screenWidth), (int)(screenHeight*.8));
		
		
		
		g2.drawString("Played Knights: "+gameBoard.allPlayers.get(x).devCards.get(DevelopmentCardType.PLAYED_KNIGHT)
				, (int)(screenWidth*.4)+(int)(x*.15*screenWidth), (int)(screenHeight*.85));
		
		int total = 0;
		for(DevelopmentCardType d :gameBoard.allPlayers.get(x).devCards.keySet()){
			total += gameBoard.allPlayers.get(x).devCards.get(d);
		}
		
		g2.drawString("Total Cards: "+total, (int)(screenWidth*.4)+(int)(x*.15*screenWidth), (int)(screenHeight*.9));
		
	}
		
		
		
		
	}//graphics for dev cards
	      
	  
	      
	      g2.setColor(Color.cyan);
	      g2.fill3DRect(0, (int)(screenHeight*.8), (int)(screenWidth*.2),(int)(screenHeight*.2) , true);
	      g2.setColor(Color.black);
	  
	      g2.setFont(font2);
	      g2.drawString(buttonAction, (int)(screenWidth*.02), (int)(screenHeight*.88));
	    
	      
	      g2.setColor(Color.cyan);
	      g2.fill3DRect((int)(screenWidth*.2), (int)(screenHeight*.8), (int)(screenWidth*.2),(int)(screenHeight*.2) , true);
	      g2.setColor(Color.black);
	      
	      if(seeMap)
	      g2.drawString("Bank", (int)(screenWidth*.22), (int)(screenHeight*.88));
	      else if(seeBank)
	      g2.drawString("Development Cards", (int)(screenWidth*.22), (int)(screenHeight*.88));
	      else
	      g2.drawString("Map", (int)(screenWidth*.22), (int)(screenHeight*.88));
	      
	      if(gameOver)
		  g2.drawString(gameBoard.getCurrentPlayer().username+" WINS!!!!", (int)(screenWidth*.5), (int)(screenHeight * .05));
	      else if(gameBoard.currentRoll > 0)
	      g2.drawString(gameBoard.getCurrentPlayer().username+" rolls a "+gameBoard.currentRoll%13, (int)(screenWidth*.5), (int)(screenHeight * .05));
	      else
	      g2.drawString(gameBoard.getCurrentPlayer().username+" place a settlement and road", (int)(screenWidth*.5), (int)(screenHeight * .05));
	      
	      
	 
	      
   double yScreen = .02;
	      
	      for(int i = 0; i < gameBoard.allPlayers.size(); i++){
	    	  
	    	  g2.setColor(gameBoard.allPlayers.get(i).color);
	    	  g2.fillRect((int)(screenWidth*.02), (int)(screenHeight*yScreen- screenHeight*.03), (int)(int)(screenWidth*.38),(int)(screenHeight*.2) );
	    	  
	    	  Font font3 = new Font("Jokerman", Font.PLAIN, 15);
	    	  g2.setColor(Color.black);
		      g2.setFont(font2);
	    	  g2.drawString(gameBoard.allPlayers.get(i).username,(int)(screenWidth*.02), (int)(screenHeight*yScreen));
	    	  
	    	  //new code
	    	  

		    	  
		    	  //Where we would put development cards and resource cards
		    	  if(gameBoard.allPlayers.get(i).cards.size() < 9){
		    		  for (int j = 0; j < gameBoard.allPlayers.get(i).cards.size(); ++j){
		    			  if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.SHEEPRC){
		    		  			g2.drawImage(sheepResourceCard, (int)(screenWidth*(.02)+(j*.04*screenWidth)), (int)(screenHeight*(yScreen + .01)), 
		    		  					(int)(screenWidth*.08), (int)(screenHeight*.17), this);
		    			  }
		    			  else if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.BRICKRC){
		    				  g2.drawImage(brickResourceCard, (int)(screenWidth*(.02) +(j*.04*screenWidth)), (int)(screenHeight*(yScreen + .01)), 
		    						  (int)(screenWidth*.08), (int)(screenHeight*.17), this);
		    			  }
		    			  else if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.FORESTRC){
		    				  g2.drawImage(forestResourceCard, (int)(screenWidth*(.02) +(j*.04*screenWidth)), (int)(screenHeight*(yScreen + .01)), 
		    						  (int)(screenWidth*.08), (int)(screenHeight*.17), this);
		    			  }
		    			  else if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.STONERC){
		    				  g2.drawImage(stoneResourceCard, (int)(screenWidth*(.02) +(j*.04*screenWidth)), (int)(screenHeight*(yScreen + .01)), 
		    						  (int)(screenWidth*.08), (int)(screenHeight*.17), this);
		    			  }
		    			  else if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.WHEATRC){
		    				  g2.drawImage(wheatResourceCard, (int)(screenWidth*(.02) +(j*.04*screenWidth)), (int)(screenHeight*(yScreen + .01)), 
		    						  (int)(screenWidth*.08), (int)(screenHeight*.17), this);
		    			  }
		    		  }
		    	  }
		    	  else{
		    		  for (int j = 0; j < gameBoard.allPlayers.get(i).cards.size(); ++j){
							  if(j < 20){
								  if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.SHEEPRC){
						  			g2.drawImage(sheepResourceCard, (int)(screenWidth*(.02)+(j*.02*screenWidth)), (int)(screenHeight*(yScreen + .01)), 
						  					(int)(screenWidth*.04), (int)(screenHeight*.085), this);
								  }
								
								  else if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.BRICKRC){
									  g2.drawImage(brickResourceCard, (int)(screenWidth*(.02) +(j*.02*screenWidth)), (int)(screenHeight*(yScreen + .01)), 
											  (int)(screenWidth*.04), (int)(screenHeight*.085), this);
								  }
								  else if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.FORESTRC){
									  g2.drawImage(forestResourceCard, (int)(screenWidth*(.02) +(j*.02*screenWidth)), (int)(screenHeight*(yScreen + .01)), 
											  (int)(screenWidth*.04), (int)(screenHeight*.085), this);
								  }
								  else if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.STONERC){
									  g2.drawImage(stoneResourceCard, (int)(screenWidth*(.02) +(j*.02*screenWidth)), (int)(screenHeight*(yScreen + .01)), 
											  (int)(screenWidth*.04), (int)(screenHeight*.085), this);
								  }
								  else if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.WHEATRC){
									  g2.drawImage(wheatResourceCard, (int)(screenWidth*(.02) +(j*.02*screenWidth)), (int)(screenHeight*(yScreen + .01)), 
											  (int)(screenWidth*.04), (int)(screenHeight*.085), this);
								  }
							  }
							  else{
			    			  	  if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.SHEEPRC){
							  			g2.drawImage(sheepResourceCard, (int)(screenWidth*(.02)+((j-20)*.02*screenWidth)), (int)(screenHeight*(yScreen + .1)), 
							  					(int)(screenWidth*.04), (int)(screenHeight*.085), this);
								  }
								  else if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.BRICKRC){
									  g2.drawImage(brickResourceCard, (int)(screenWidth*(.02) +((j-20)*.02*screenWidth)), (int)(screenHeight*(yScreen + .1)), 
											  (int)(screenWidth*.04), (int)(screenHeight*.085), this);
								  }
								  else if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.FORESTRC){
									  g2.drawImage(forestResourceCard, (int)(screenWidth*(.02) +((j-20)*.02*screenWidth)), (int)(screenHeight*(yScreen + .1)), 
											  (int)(screenWidth*.04), (int)(screenHeight*.085), this);
								  }
								  else if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.STONERC){
									  g2.drawImage(stoneResourceCard, (int)(screenWidth*(.02) +((j-20)*.02*screenWidth)), (int)(screenHeight*(yScreen + .1)), 
											  (int)(screenWidth*.04), (int)(screenHeight*.085), this);
								  }
								  else if(gameBoard.allPlayers.get(i).cards.get(j) == ResourceCardType.WHEATRC){
									  g2.drawImage(wheatResourceCard, (int)(screenWidth*(.02) +((j-20)*.02*screenWidth)), (int)(screenHeight*(yScreen + .1)), 
											  (int)(screenWidth*.04), (int)(screenHeight*.085), this);
								  }
							  
							  }
		    		  	}
		    	  }
	    	  
	    	
  		  
	    	  
	    	  yScreen = yScreen + .2;
	      }
	   }
	

	   public void runLocalInterface() {//will probably need to change to a normal method that takes a gameBoard
		  
		  
		   
		      javax.swing.SwingUtilities.invokeLater(new Runnable() {
		         public void run() {
		        	 
		        	 JPanel totalGUI = new JPanel();
		        	 totalGUI.setLayout(null);
		        	 
		        	 JButton button = new JButton();
		 
		        	 button.setSize(screenWidth, screenHeight); 
		        	 button.setLocation(0, 0);
		        	 button.setOpaque(false);
		        	 button.setContentAreaFilled(false);
		        	 button.setBorderPainted(false);
		        	 button.setVisible(true);
		        	 totalGUI.add(button);
		        	 
		        
		        	 
		 button.addMouseListener(new MouseAdapter() {
			      			@Override
			      			public void mouseClicked(MouseEvent mC) { 
			      		
			      				int xClick = mC.getX();
			      				int yClick = mC.getY();
			      				
			      				
			 
			      				
			      			if(seeMap){	
			      				
			      				if(isJointSelect((1.0*xClick)/screenWidth,(1.0*yClick)/screenHeight)){
			      			
			      				}
			      				else if(xClick > 0&& xClick < (int)(screenWidth*.2)&& yClick > (int)(screenHeight*.8)){
			      				
			      					
			      					
			      					if(buttonAction.equals("Place Settlement")){
			      						gameBoard.placeSettlement(selectedJoints);
			      					}
			      					else if(buttonAction.equals("Place City")){
			      						System.out.println("inside here");
			      						gameBoard.placeCity(selectedJoints);
			      					}
			      					else if(buttonAction.equals("Place Road")){
			      						gameBoard.placePath(selectedJoints);
			      					}
			      					else if(buttonAction.equals("Move Robber")){
			      						gameBoard.placeRobber(selectedJoints);
			      					}
			      					else{
			      						gameBoard.nextTurn();
			      						seeMap = true;
			      						seeBank = false;
			      					}
			      					
			      					
			      					emptyDevCard();
			      					emptyBank();
			      					emptySelectedJoints();
					         
			      					
			      				}
			      				else if(xClick > (int)(screenWidth*.2)&& xClick < (int)(screenWidth*.4)&& yClick > (int)(screenHeight*.8)){
			      					
			      					
			      					
			      					UserInterface.emptySelectedJoints();
			      					seeBank = true;
			      					seeMap = false;
			      				}
			      				else{
			      					
			      				}
			      				
			      	
			      	
			      		
	
			      			}
			      			else if(seeBank){
			      				
			      				double x = (1.0*xClick)/screenWidth;
			      				double y = (1.0*yClick)/screenHeight;
			      				
			      			//	g2.drawString("-", (int)(screenWidth*.45), (int)(screenHeight*.25));
			      			//	g2.drawString("+", (int)(screenWidth*.55), (int)(screenHeight*.27));
			      			//	g2.drawString("X "+sheepAmount, (int)(screenWidth*.6), (int)(screenHeight*.25));
			      				
			      				
			      				
			      			if(xClick > 0&& xClick < (int)(screenWidth*.2)&& yClick > (int)(screenHeight*.8)){
			 
			      				
			      				if(buttonAction.equals("Trade With Bank")){
		      						gameBoard.tradeWithBank(playerCardSelect, cardSelect);
		      					}
			      				else{
			      					gameBoard.nextTurn();
			      					
		      						seeMap = true;
		      						seeBank = false;
			      				}
			      				
			      				emptyDevCard();
		      					emptyBank();
		      					emptySelectedJoints();
				         
			      			}
			      			else if(xClick > (int)(screenWidth*.2)&& xClick < (int)(screenWidth*.4)&& yClick > (int)(screenHeight*.8)){
			      				emptyDevCard();
		      					emptyBank();
		      					emptySelectedJoints();
				         
		      					seeBank = false;
		      					seeMap = false;
		      				}
			      				
			      				if(x>.4&&x<.6&&y>.2&&y<.3){
			      					playerCardSelect = 1;
			      				}
			      				if(x>.4&&x<.6&&y>.3&&y<.4){
			      					playerCardSelect = 2;
			      				}
			      				if(x>.4&&x<.6&&y>.4&&y<.5){
			      					playerCardSelect = 3;
			      				}
			      				if(x>.4&&x<.6&&y>.5&&y<.6){
			      					playerCardSelect = 4;
			      				}
			      				if(x>.4&&x<.6&&y>.6&&y<.7){
			      					playerCardSelect = 5;
			      				}
			      				
			      				
			      	
			      				
			      				
			      				if(x>.8&&x<.9&&y>.2&&y<.3){
			      					cardSelect = 1;
			      				}
			      				if(x>.8&&x<.9&&y>.3&&y<.4){
			      					cardSelect = 2;
			      				}
			      				if(x>.8&&x<.9&&y>.4&&y<.5){
			      					cardSelect = 3;
			      				
			      				}
			      				if(x>.8&&x<.9&&y>.5&&y<.6){
			      					cardSelect = 4;
			      					
			      				}
			      				if(x>.8&&x<.9&&y>.6&&y<.7){
			      					cardSelect = 5;	
			      				}
			      				
	      				
			      			}else{//dev card button
			      				
			      				
			      				double x = (1.0*xClick)/screenWidth;
			      				double y = (1.0*yClick)/screenHeight;
			      				
			      				
			      			if(xClick > 0&& xClick < (int)(screenWidth*.2)&& yClick > (int)(screenHeight*.8)){
			 
			      				
			      				if(buttonAction.equals("Buy Dev Card")){
		      						gameBoard.buyDevCard();
		      					}
			      				else if(buttonAction.equals("Play Knight")){
			      					gameBoard.playKnight();
			      				}
			      				else if(buttonAction.equals("Play Monopoly")){
			      					Object[] options = {"Cancel",
			      		                    "Brick",
			      		                    "Stone",
			      		                    "Wheat",
			      		                    "Sheep",
			      		                    "Wood",
			      		                    };
			      					int n = JOptionPane.showOptionDialog(frame,
			      							"Pick a resource to monopolize"
			      									,
			      									"Monopoly Card",
			      									JOptionPane.YES_NO_CANCEL_OPTION,
			      									JOptionPane.QUESTION_MESSAGE,
			      									null,
			      									options,
			      									options[2]);
			      					
			      					gameBoard.playMonopoly(n);
			      					
			      					
			      				}
			      				else if(buttonAction.equals("Play Plenty")){
			      					Object[] options = {
			      		                    "Brick",
			      		                    "Stone",
			      		                    "Wheat",
			      		                    "Sheep",
			      		                    "Wood",
			      		                    };
			      					int n = JOptionPane.showOptionDialog(frame,
			      							"Pick a resource"
			      									,
			      									"Plenty Card",
			      									JOptionPane.YES_NO_CANCEL_OPTION,
			      									JOptionPane.QUESTION_MESSAGE,
			      									null,
			      									options,
			      									options[2]);
			      					
			      					int p = JOptionPane.showOptionDialog(frame,
			      							"Pick a resource"
			      									,
			      									"Plenty Card",
			      									JOptionPane.YES_NO_CANCEL_OPTION,
			      									JOptionPane.QUESTION_MESSAGE,
			      									null,
			      									options,
			      									options[2]);
			      					
			      					gameBoard.playPlenty(n, p);
			      				}
			      				else if(buttonAction.equals("Play Roads")){
			      					gameBoard.playRoads();
			      				}
			      				else{
			      					gameBoard.nextTurn();

		      						seeMap = true;
		      						seeBank = false;
			      				}
			      					
			      					
			      			}
			      			else if(		
			      					(xClick > (int)(screenWidth*.865)&& yClick> (int)(screenHeight*.415)&&
			      							xClick< screenWidth
			      							&&yClick< (int)(screenHeight*.415)+(int)(screenHeight*.265))){
			      				buyCard = !buyCard;
			      				devCardSelect = 0;
			      			}
			      			else if(xClick > (int)(screenWidth*.55)&&yClick > (int)(screenHeight*.1)&&
			      					xClick < (int)(screenWidth*.55)+(int)(screenWidth*.15)&& yClick <(int)(screenHeight*.1)+(int)(screenHeight*.3)){
			      			devCardSelect = 1;	
			      			buyCard = false;
			      			}
			      			else if(xClick > (int)(screenWidth*.70)&&yClick > (int)(screenHeight*.1)&&
			      					xClick < (int)(screenWidth*.70)+(int)(screenWidth*.15)&& yClick <(int)(screenHeight*.1)+(int)(screenHeight*.3)){
			      			devCardSelect = 2;	
			      			buyCard = false;
			      			}
			      			else if(xClick > (int)(screenWidth*.85)&&yClick > (int)(screenHeight*.1)&&
			      					xClick < (int)(screenWidth*.85)+(int)(screenWidth*.15)&& yClick <(int)(screenHeight*.1)+(int)(screenHeight*.3)){
			      			devCardSelect = 3;	
			      			buyCard = false;
			      			}
			      			else if(xClick > (int)(screenWidth*.4)&&yClick > (int)(screenHeight*.4)&&
			      					xClick < (int)(screenWidth*.4)+(int)(screenWidth*.15)&& yClick <(int)(screenHeight*.4)+(int)(screenHeight*.3)){
			      			devCardSelect = 4;	
			      			buyCard = false;
			      			}
			      			
			      			else if(xClick > (int)(screenWidth*.2)&& xClick < (int)(screenWidth*.4)&& yClick > (int)(screenHeight*.8)){
			      				emptyDevCard();
		      					emptyBank();
		      					emptySelectedJoints();
				         
		      					
		      				
		      					seeBank = false;
		      					seeMap = true;
		      				}
			      				
			      				
			      				
			      				
			      			}//dev card button
			      			
			      			

			      				
			      			
			      		
			      			buttonAction = gameBoard.getAction(selectedJoints, playerCardSelect, cardSelect, buyCard, devCardSelect);	
			      			
			      			
			      			if(buttonAction.equals("Game Over"))
			      				gameOver = true;
			      			
			      			
			      			}

					
						
								
								
								
							
							
		 
		 });
		        	 
		 
		 
		 
		 
		        	JPanel panel = new UserInterface();
		        	panel.setSize(screenWidth,screenHeight);
		        	panel.setLocation(0, 0);
		        	panel.setVisible(true);
			    	totalGUI.add(panel);
		        	 
		        	//totalGUI.add(hello);
		      
		        	frame = new JFrame("Settlers of Farmville");
		        	frame.setSize(screenWidth, screenHeight);
		            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		            frame.setContentPane(totalGUI);
		           // frame.pack();
		            frame.setVisible(true);
		        
		            
	
		
		
	

		
		
	
	
		
		         }
		      });
		
		}

		



	public static void emptySelectedJoints() {
		// TODO Auto-generated method stub
		selectedJoints = new HashSet<Integer>();
	}
	
	private static void emptyDevCard() {
		// TODO Auto-generated method stub
		buyCard = false;
		devCardSelect = 0;
	}


	public static void emptyBank() {
		// TODO Auto-generated method stub
		playerCardSelect = 0;
		cardSelect = 0;
	}



	
	public boolean isJointSelect(double x, double y) {
		// TODO Auto-generated method stub
		
		for(int i = 0; i < gameBoard.allJoints.size(); i++){//Add all the joints corresponding to this hex
			double dist = Math.sqrt((x - gameBoard.allJoints.get(i).xLoc)*(x - gameBoard.allJoints.get(i).xLoc)
					+(y - gameBoard.allJoints.get(i).yLoc)*(y - gameBoard.allJoints.get(i).yLoc));
			

			if(dist < .02){//theoretically should only happen once per location
				
				System.out.println(i);
				
				if(!selectedJoints.contains(i))
					selectedJoints.add(i);//Adds the UserInteface arraylist joint position 
				else{
							selectedJoints.remove(i);
				}
				return true;
			}
		
		
		
		
	}
		
		return false;
		
		}

	public void updateGame(LocalGameBoard game) {
		// TODO Auto-generated method stub
		gameBoard = game;
	}
		
		
		
		
		
		
		
	}

