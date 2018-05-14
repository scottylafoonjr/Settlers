import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JOptionPane;


public class LocalGameBoard implements Serializable {
	
	/**
	 * 
	 */
	public boolean isTraining = false;
	public int longestRoadPlayer = -1;
	public boolean playedKnight = false;
	public int longestRoadLength = 4;
	public NeuralNet robot0 = null;
	public NeuralNet robot1 = null;
	public NeuralNet robot2 = null;
	public int largestArmySize = 0;
	public int largestArmyPlayer = -1;
	public boolean gameOver = false;
	public int gameWinner = -1;
	public int player = 0;
	private static final long serialVersionUID = 1L;
	public ArrayList<Path> allPaths = new ArrayList<Path>();
	public ArrayList<Hex> allHexes = new ArrayList<Hex>();
	public ArrayList<Joint> allJoints = new ArrayList<Joint>();
	public ArrayList<Port> allPorts = new ArrayList<Port>();
	public Robber rob;
	public ArrayList<HexType> hexInitArray = new ArrayList<HexType>();
	public ArrayList<Player> allPlayers = new ArrayList<Player>();
	int currentRoll = -1;
	int turn = 1;
	boolean cardPlayed = false;
	ArrayList<DevelopmentCardType> cardPile = new ArrayList<DevelopmentCardType>();
	public ArrayList<Integer> hexNumberInitArray = new ArrayList<Integer>();
	
	
	
public void reset(){
	currentRoll = -1;
	longestRoadPlayer = -1;
	playedKnight = false;
	longestRoadLength = 4;
	
	largestArmySize = 0;
	largestArmyPlayer = -1;
	 gameOver = false;
	 gameWinner = -1;
	 player = 0;
	
	 for(int i = 0; i < allHexes.size(); i++){
		 if(allHexes.get(i).hexType == HexType.SAND)//Put the robber in the sand
			 rob = new Robber(i);
	 }
	 
	 
	 	cardPile = null;
	 	cardPile = new ArrayList<DevelopmentCardType>();
	
		for(int i = 0; i < 14; i++)
			cardPile.add(DevelopmentCardType.KNIGHT);
			
			
			for(int i = 0; i < 5; i++)
			cardPile.add(DevelopmentCardType.VICTORY_POINT);
			
			for(int i = 0; i < 2; i++)
				cardPile.add(DevelopmentCardType.ROADS);
			
			for(int i = 0; i < 2; i++)
				cardPile.add(DevelopmentCardType.YEAR_OF_PLENTY);
			
			for(int i = 0; i < 2; i++)
				cardPile.add(DevelopmentCardType.MONOPOLY);
			
			for(int i = 0; i < 10; i++)//shuffle them nice and good like
			Collections.shuffle(cardPile);//shuffle the cards
	 
	 
	for(int i = 0; i < allPaths.size(); i++){
		allPaths.get(i).color = new Color(129,98,78);
	}
	
	for(int i = 0; i < allJoints.size(); i++){
		allJoints.get(i).city = false;
		allJoints.get(i).color = new Color(237,201,175);
	}
	
	
	for(int i = 0; i < allHexes.size(); i++){
		if(allHexes.get(i).number == -1)
			placeRobber(i);
	}
	
	for(int i = 0; i < allPlayers.size(); i++){
		allPlayers.get(i).cards = null;
		allPlayers.get(i).cards = new ArrayList<ResourceCardType>();
		for(DevelopmentCardType d: allPlayers.get(i).devCards.keySet()){
			allPlayers.get(i).devCards.put(d, 0);
		}
	}
	
	this.turn = 1;
	
}

	

public String toString(){
	String str = "";
	str = str + "allPaths size: "+allPaths.size()+" \n";
	str = str + "allHexes size: "+allHexes.size()+" \n";
	str = str + "allJoints size: "+allJoints.size()+" \n";
	
	return str;
}

public void nextTurn(){
	
	
	if(allPlayers.get(player).isRobot&&player == 0){
		while(robot0.executeNextMove(this));
	}
	else if(allPlayers.get(player).isRobot&&player == 1){
		while(robot1.executeNextMove(this));
	}
	else if(allPlayers.get(player).isRobot&&player == 2){
		while(robot2.executeNextMove(this));
	}
	else if(turn == 1 &&(!haveRoad()||!haveHouse())){
		return;
	}
	else if(turn == 2 &&(!have2Road()||!have2House())){
		return;
	}
	
	
	
	if(turn == 1)
		giveStartingCards();
	
	cardPlayed = false;
	
	 checkLongestRoad();
	 checkLargestArmy();
	 if(checkGameWinner()){
		 gameOver = true;
		 gameWinner = player;
		 return;
	 }
	 
	
	
	if(player > allPlayers.size()-2 && turn == 1){
		player = allPlayers.size() - 1;
		turn ++;}
	else if(player > allPlayers.size()-2 && turn != 2){
		player = 0;
		turn ++;}
	else if(turn == 2&& player == 0){
		turn ++;
	}
	else if(turn == 2)
		player --;
	else
		player ++;
	
	if(turn > 2){
	int min = 1;
	int max = 7;//pretty sure this is non inclusive.. could be wrong
	Random rand = new Random();
	
	currentRoll = rand.nextInt(max - min )+min + rand.nextInt(max - min )+min;
	
	if(currentRoll == 7){
		//handle robber situation
		
		for(int i = 0; i < allPlayers.size(); i++){
			if(allPlayers.get(i).cards.size() >= 7){
				Collections.shuffle(allPlayers.get(i).cards);
				
				int amount = allPlayers.get(i).cards.size()/2;
				
				for(int x = 0; x < amount;x++){
					allPlayers.get(i).cards.remove(0);
				}
				
				
			}
		}
		
			
	}
	
	for(int i = 0; i < allHexes.size(); i++){
		
	if(allHexes.get(i).number == currentRoll&& rob.location != i){
	
		for(Integer num : allHexes.get(i).joints){
			
			for(int y = 0; y < allPlayers.size(); y++){
				if(allJoints.get(num).color.equals(allPlayers.get(y).color)){
					if(allHexes.get(i).hexType == HexType.BRICK){
						allPlayers.get(y).cards.add(ResourceCardType.BRICKRC);
						if(allJoints.get(num).city)
						allPlayers.get(y).cards.add(ResourceCardType.BRICKRC);
					}
					else if(allHexes.get(i).hexType == HexType.LUMBER){
						allPlayers.get(y).cards.add(ResourceCardType.FORESTRC);
						if(allJoints.get(num).city)
						allPlayers.get(y).cards.add(ResourceCardType.FORESTRC);
					}
					else if(allHexes.get(i).hexType == HexType.SHEEP){
						allPlayers.get(y).cards.add(ResourceCardType.SHEEPRC);
						if(allJoints.get(num).city)
						allPlayers.get(y).cards.add(ResourceCardType.SHEEPRC);
					}
					else if(allHexes.get(i).hexType == HexType.STONE){
						allPlayers.get(y).cards.add(ResourceCardType.STONERC);
						if(allJoints.get(num).city)
						allPlayers.get(y).cards.add(ResourceCardType.STONERC);
					}
					else if(allHexes.get(i).hexType == HexType.WHEAT){
						allPlayers.get(y).cards.add(ResourceCardType.WHEATRC);
						if(allJoints.get(num).city)
						allPlayers.get(y).cards.add(ResourceCardType.WHEATRC);
					}
			}
			}
		}
	}
	}
	
	
	}
	
	
	
	
	
	if(allPlayers.get(player).isRobot){
		
	}
	
}



private void giveStartingCards() {
	// TODO Auto-generated method stub
	
	
	for(int i = 0; i < allJoints.size(); i++){
		if(allJoints.get(i).color.equals(allPlayers.get(player).color)){
			
			
			for(int x = 0; x < allHexes.size(); x++){
				
				if(allHexes.get(x).joints.contains(i)){
					if(allHexes.get(x).hexType == HexType.BRICK)
					allPlayers.get(player).cards.add(ResourceCardType.BRICKRC);
					
					if(allHexes.get(x).hexType == HexType.LUMBER)
						allPlayers.get(player).cards.add(ResourceCardType.FORESTRC);
					
					if(allHexes.get(x).hexType == HexType.SHEEP)
						allPlayers.get(player).cards.add(ResourceCardType.SHEEPRC);
					
					if(allHexes.get(x).hexType == HexType.STONE)
						allPlayers.get(player).cards.add(ResourceCardType.STONERC);
					
					if(allHexes.get(x).hexType == HexType.WHEAT)
						allPlayers.get(player).cards.add(ResourceCardType.WHEATRC);
				}
				
				
			}
			
			
			
			
		}
	}
	
	
	
}

public int getWinner() {
	// TODO Auto-generated method stub
	
	
		for(int i = 0; i < allPlayers.size(); i++){
		int total = 0;
		
		for(int x = 0; x < allJoints.size(); x++){
			
			if(allJoints.get(x).color.equals(allPlayers.get(i).color)){
				total ++;
			
				if(allJoints.get(x).city)
					total ++;
			
			}
		}
		
		if(longestRoadPlayer == player)
			total = total + 2;
		
		if(largestArmyPlayer == player)
			total = total + 2;
		
		total = total + allPlayers.get(i).devCards.get(DevelopmentCardType.VICTORY_POINT);
	
		
	
		if(total >= 10){
			
			return i;
		}
		
		}
	
	
	return -1;
}

public boolean checkGameWinner() {
	// TODO Auto-generated method stub
	
	
		
		int total = 0;
		
		for(int x = 0; x < allJoints.size(); x++){
			
			if(allJoints.get(x).color.equals(allPlayers.get(player).color)){
				total ++;
			
				if(allJoints.get(x).city)
					total ++;
			
			}
		}
		
		if(longestRoadPlayer == player)
			total = total + 2;
		
		if(largestArmyPlayer == player)
			total = total + 2;
		
		total = total + allPlayers.get(player).devCards.get(DevelopmentCardType.VICTORY_POINT);
	
		
	
		if(total >= 10){
			
			return true;
		}
		
		
	
	
	return false;
}


private void checkLargestArmy(){
	
	int amount = allPlayers.get(player).devCards.get(DevelopmentCardType.PLAYED_KNIGHT);
	
	if(amount > largestArmySize){
		largestArmyPlayer = player;
		largestArmySize = amount;
	}
	
	
}


private void checkLongestRoad() {
	// TODO Auto-generated method stub
	
	for(int z = 0; z < 2; z++){

	for(int i = 0; i < allPlayers.size(); i++){
		
		ArrayList<ArrayList<Integer>> allPoss = new ArrayList<ArrayList<Integer>>();
		
		for(int x = 0; x < allJoints.size(); x++){
			if(!allJoints.get(x).color.equals(new Color(237,201,175))){//potential for longest path
				
				for(int y= 0; y < allPaths.size(); y++){
					if(allPaths.get(y).color.equals(allPlayers.get(i).color)
						&&allPaths.get(y).joints.contains(x)){
						
						ArrayList<Integer> first = new ArrayList<Integer>();
						first.add(y);
						
						allPoss.add(first);
						
						
					}
				}
				
				
			}
			
		}
		
		//fadsgaD.println("player "+(player+1)+" poss size "+ allPoss.size());
		
		
		for(int t = 0; t < allPoss.size(); t++){
			
			int last = allPoss.get(t).get(allPoss.get(t).size()-1);
			
			
			
			for(int g = 0; g < allPaths.size(); g++){
				
				boolean pathAdded = false;//only add one path at a time
				
				for(Integer num1: allPaths.get(g).joints){
					for(Integer num2: allPaths.get(last).joints){
						
						if(arrayDoesntContain(allPoss.get(t), num1)){
						
						if(num1 == num2&&!pathAdded){
							if(allPaths.get(g).color.equals(allPlayers.get(i).color)){//path needs to be player color
								if(allJoints.get(num1).color.equals(allPlayers.get(i).color)
									||allJoints.get(num1).color.equals(new Color(237,201,175))){//shared joint is not cut off
									if(!allPoss.get(t).contains(g)){//path doesnt already contain the single path
										
										ArrayList<Integer> updated = new ArrayList<Integer>();
										
										for(int s = 0; s < allPoss.get(t).size(); s++){
											updated.add(allPoss.get(t).get(s));
										}
										
										updated.add(g);
										allPoss.add(updated);
										
										pathAdded = true;
										
									}
								}
							}
						}
					}
					}
				}
				
				
			}
		}
		
		
		
		
		int roadLength = 0;
		for(int w = 0; w < allPoss.size(); w++){
			if(allPoss.get(w).size()> roadLength)
				roadLength = allPoss.get(w).size();
		}
		
		 //Dethrown them if the get worst
		//If they retain the crown after getting worst, it will loop through again and give it to them
		
		if(roadLength < longestRoadLength && longestRoadPlayer == i){
			longestRoadLength = 4;			
			longestRoadPlayer = -1;
		}
		
		
		if(roadLength > longestRoadLength){
			longestRoadLength = roadLength;
			longestRoadPlayer = i;
			
		}
		
		
		
		
		
		
		
	//handle for each player	
		
	}
}

}










private boolean arrayDoesntContain(ArrayList<Integer> arrayList, Integer num1) {
	// TODO Auto-generated method stub
	
	int count = 0;
	
	for(int i = 0; i < allPaths.size(); i++){
		if(arrayList.contains(i)&&arrayList.indexOf(i)>= arrayList.size()-2){
			if(allPaths.get(i).joints.contains(num1)){
				count ++;
			}
		}
	}
	
	//fadsgaD.println("count "+count);
	
	if(count > 1)
		return false;
	
	
	return true;
}



private boolean have2Road(){
	int road = 0;
	
	
	
	for(int i = 0; i < allPaths.size();i++){
		if(allPaths.get(i).color.equals(allPlayers.get(player).color))
			road ++;
	}
	
	
	
	if(road == 2){
		return true;
	}
	
	
	return false;
	
}

private boolean haveRoad(){
	int road = 0;
	
	
	for(int i = 0; i < allPaths.size();i++){
		if(allPaths.get(i).color.equals(allPlayers.get(player).color))
			road ++;
	}
	
	
	
	
	if(road == 1){
		return true;
	}
	
	
	return false;
	
}

private boolean have2House(){
	
	int house = 0;
	

	
	for(int i = 0; i < allJoints.size();i++){
		if(allJoints.get(i).color.equals(allPlayers.get(player).color))
			house ++;
	}
	
	
	if(house == 2){
		return true;
	}
	
	
	return false;
}

private boolean haveHouse(){
	
	int house = 0;
	

	
	for(int i = 0; i < allJoints.size();i++){
		if(allJoints.get(i).color.equals(allPlayers.get(player).color))
			house ++;
	}
	
	
	if(house == 1){
		return true;
	}
	
	
	return false;
}




public Player getCurrentPlayer(){
	return allPlayers.get(player);
}

public void scrambleNumbers(){
	for(int i = 0; i < allHexes.size(); i++){
		if(allHexes.get(i).number != -1){
			int min = 2;
			int max = 13;//pretty sure this is non inclusive.. could be wrong
			Random rand = new Random();
			
			allHexes.get(i).number = rand.nextInt(max - min )+min;
			
			if(allHexes.get(i).number == 7)
				allHexes.get(i).number = 8;
		
		}
	}
}




public void scrambleHexes(){
	
	hexInitArray.add(HexType.BRICK);
	hexInitArray.add(HexType.BRICK);
	hexInitArray.add(HexType.BRICK);
	hexInitArray.add(HexType.STONE);
	hexInitArray.add(HexType.STONE);
	hexInitArray.add(HexType.STONE);
	hexInitArray.add(HexType.LUMBER);
	hexInitArray.add(HexType.LUMBER);
	hexInitArray.add(HexType.LUMBER);
	hexInitArray.add(HexType.LUMBER);
	hexInitArray.add(HexType.WHEAT);
	hexInitArray.add(HexType.WHEAT);
	hexInitArray.add(HexType.WHEAT);
	hexInitArray.add(HexType.WHEAT);
	hexInitArray.add(HexType.SHEEP);
	hexInitArray.add(HexType.SHEEP);
	hexInitArray.add(HexType.SHEEP);
	hexInitArray.add(HexType.SHEEP);
	
	
	Collections.shuffle(hexInitArray);//Scramble up the board pieces so that the Constructors of the Hex object can just pull the first object
	
	for(int i = 0; i < allHexes.size(); i++){
		if(allHexes.get(i).hexType!= HexType.SAND){
			allHexes.get(i).hexType = hexInitArray.remove(0);
		}
	}
	
}



public LocalGameBoard(){
	
	
	/*14 Soldiers/Knights  call them Knights from now on)
	5 Victory Points
	2 Road Building Cards
	2 Monopoly Cards
	2 Year of Plenty Cards
	*/
	
	for(int i = 0; i < 14; i++)
	cardPile.add(DevelopmentCardType.KNIGHT);
	
	
	for(int i = 0; i < 5; i++)
	cardPile.add(DevelopmentCardType.VICTORY_POINT);
	
	for(int i = 0; i < 2; i++)
		cardPile.add(DevelopmentCardType.ROADS);
	
	for(int i = 0; i < 2; i++)
		cardPile.add(DevelopmentCardType.YEAR_OF_PLENTY);
	
	for(int i = 0; i < 2; i++)
		cardPile.add(DevelopmentCardType.MONOPOLY);
	
	for(int i = 0; i < 10; i++)//shuffle them nice and good like
	Collections.shuffle(cardPile);//shuffle the cards
	
	hexNumberInitArray.add(12);
	hexNumberInitArray.add(11);
	hexNumberInitArray.add(11);
	hexNumberInitArray.add(10);
	hexNumberInitArray.add(10);
	hexNumberInitArray.add(9);
	hexNumberInitArray.add(9);
	hexNumberInitArray.add(8);
	hexNumberInitArray.add(8);
	hexNumberInitArray.add(6);
	hexNumberInitArray.add(6);
	hexNumberInitArray.add(5);
	hexNumberInitArray.add(5);
	hexNumberInitArray.add(4);
	hexNumberInitArray.add(4);
	hexNumberInitArray.add(3);
	hexNumberInitArray.add(3);
	hexNumberInitArray.add(2);
	
	for(int i = 0; i < 10; i++)//shuffle them nice and good like
	Collections.shuffle(hexNumberInitArray);//shuffle the cards
	
	
	
	allPorts.add(new Port(50,51,ResourceCardType.FORESTRC));
	allPorts.add(new Port(45,46,ResourceCardType.BRICKRC));
	allPorts.add(new Port(3,4,ResourceCardType.SHEEPRC));
	allPorts.add(new Port(7,17,ResourceCardType.STONERC));
	allPorts.add(new Port(28,38,ResourceCardType.WHEATRC));
	
	allPorts.add(new Port(47,48));
	allPorts.add(new Port(37,26));
	allPorts.add(new Port(15,14));
	allPorts.add(new Port(0,1));
	
	
	hexInitArray.add(HexType.SAND);
	hexInitArray.add(HexType.BRICK);
	hexInitArray.add(HexType.BRICK);
	hexInitArray.add(HexType.BRICK);
	hexInitArray.add(HexType.STONE);
	hexInitArray.add(HexType.STONE);
	hexInitArray.add(HexType.STONE);
	hexInitArray.add(HexType.LUMBER);
	hexInitArray.add(HexType.LUMBER);
	hexInitArray.add(HexType.LUMBER);
	hexInitArray.add(HexType.LUMBER);
	hexInitArray.add(HexType.WHEAT);
	hexInitArray.add(HexType.WHEAT);
	hexInitArray.add(HexType.WHEAT);
	hexInitArray.add(HexType.WHEAT);
	hexInitArray.add(HexType.SHEEP);
	hexInitArray.add(HexType.SHEEP);
	hexInitArray.add(HexType.SHEEP);
	hexInitArray.add(HexType.SHEEP);
	
	
	Collections.shuffle(hexInitArray);//Scramble up the board pieces so that the Constructors of the Hex object can just pull the first object
	
	//Patrick put all the constructors below this comment
	
	
    //Joints from left to right, top to bottom 
    // Row 1
    allJoints.add(new Joint((0.560546875 ),(0.1882716049382716 )));
    allJoints.add(new Joint((0.6061197916666666 ),(0.147119341563786 )));
    allJoints.add(new Joint((0.6477864583333334 ),(0.18209876543209877 )));
    allJoints.add(new Joint((0.6920572916666666 ),(0.1440329218106996 )));
    allJoints.add(new Joint((0.7350260416666666 ),(0.18209876543209877 )));
    allJoints.add(new Joint((0.7799479166666666 ),(0.14094650205761317 )));
    allJoints.add(new Joint((0.82421875 ),(0.18621399176954734 )));
    //Row 2
    allJoints.add(new Joint((0.5143229166666666 ),(0.31584362139917693 )));
    allJoints.add(new Joint((0.5611979166666666 ),(0.2716049382716049 )));
    allJoints.add(new Joint((0.6028645833333334 ),(0.3096707818930041 )));
    allJoints.add(new Joint((0.6471354166666666 ),(0.26954732510288065 )));
    allJoints.add(new Joint((0.69140625 ),(0.3117283950617284 )));
    allJoints.add(new Joint((0.7369791666666666 ),(0.26954732510288065 )));
    allJoints.add(new Joint((0.78125 ),(0.3117283950617284 )));
    allJoints.add(new Joint((0.8255208333333334 ),(0.2705761316872428 )));
    allJoints.add(new Joint((0.869140625 ),(0.30864197530864196 )));

    //Row 3
    allJoints.add(new Joint((0.4680989583333333 ),(0.4454732510288066 )));
    allJoints.add(new Joint((0.5143229166666666 ),(0.3991769547325103 )));
    allJoints.add(new Joint((0.5579427083333334 ),(0.4403292181069959 )));
    allJoints.add(new Joint((0.6028645833333334 ),(0.3991769547325103 )));
    allJoints.add(new Joint((0.6471354166666666 ),(0.4403292181069959 )));
    allJoints.add(new Joint((0.693359375 ),(0.4022633744855967 )));
    allJoints.add(new Joint((0.7389322916666666 ),(0.44238683127572015 )));
    allJoints.add(new Joint((0.7819010416666666 ),(0.4022633744855967 )));
    allJoints.add(new Joint((0.826171875 ),(0.43930041152263377 )));
    allJoints.add(new Joint((0.8723958333333334 ),(0.39814814814814814 )));
    allJoints.add(new Joint((0.9166666666666666 ),(0.43621399176954734 )));

    //Row 4
    allJoints.add(new Joint((0.470703125 ),(0.5318930041152263 )));
    allJoints.add(new Joint((0.5130208333333334 ),(0.5771604938271605 )));
    allJoints.add(new Joint((0.5592447916666666 ),(0.5318930041152263 )));
    allJoints.add(new Joint((0.603515625 ),(0.5740740740740741 )));
    allJoints.add(new Joint((0.6484375 ),(0.529835390946502 )));
    allJoints.add(new Joint((0.6953125 ),(0.573045267489712 )));
    allJoints.add(new Joint((0.7389322916666666 ),(0.5329218106995884 )));
    allJoints.add(new Joint((0.7838541666666666 ),(0.573045267489712 )));
    allJoints.add(new Joint((0.8294270833333334 ),(0.5277777777777778 )));
    allJoints.add(new Joint((0.8776041666666666 ),(0.5689300411522634 )));
    allJoints.add(new Joint((0.9205729166666666 ),(0.5246913580246914 )));

    //Row 5
    allJoints.add(new Joint((0.51171875 ),(0.6656378600823045 )));
    allJoints.add(new Joint((0.5579427083333334 ),(0.7098765432098766 )));
    allJoints.add(new Joint((0.6041666666666666 ),(0.6635802469135802 )));
    allJoints.add(new Joint((0.6497395833333334 ),(0.7057613168724279 )));
    allJoints.add(new Joint((0.6953125 ),(0.6646090534979424 )));
    allJoints.add(new Joint((0.7408854166666666 ),(0.7067901234567902 )));
    allJoints.add(new Joint((0.7884114583333334 ),(0.6635802469135802 )));
    allJoints.add(new Joint((0.833984375 ),(0.7057613168724279 )));
    allJoints.add(new Joint((0.8776041666666666 ),(0.6584362139917695 )));

    //Row 6
    allJoints.add(new Joint((0.5592447916666666 ),(0.8034979423868313 )));
    allJoints.add(new Joint((0.6048177083333334 ),(0.845679012345679 )));
    allJoints.add(new Joint((0.650390625 ),(0.8024691358024691 )));
    allJoints.add(new Joint((0.697265625 ),(0.8415637860082305 )));
    allJoints.add(new Joint((0.7434895833333334 ),(0.8024691358024691 )));
    allJoints.add(new Joint((0.7903645833333334 ),(0.8395061728395061 )));
    allJoints.add(new Joint((0.8346354166666666 ),(0.7962962962962963 )));
    
    

	
	
	//Tiles from left to right, top to bottom 
    //Row 1
     allHexes.add(new Hex((0.607421875 ),(0.2345679012345679 ),this));
     allHexes.add(new Hex((0.6946614583333334 ),(0.23353909465020575 ),this));
     allHexes.add(new Hex((0.7819010416666666 ),(0.23148148148148148 ),this));
     //Row 2
     allHexes.add(new Hex((0.5618489583333334 ),(0.36213991769547327 ),this));
     allHexes.add(new Hex((0.6497395833333334 ),(0.3611111111111111 ),this));
     allHexes.add(new Hex((0.7395833333333334 ),(0.3611111111111111 ),this));
     allHexes.add(new Hex((0.8274739583333334 ),(0.3631687242798354 ),this));
     //Row 3
     allHexes.add(new Hex((0.5149739583333334 ),(0.492798353909465 ),this));
     allHexes.add(new Hex((0.6041666666666666 ),(0.49176954732510286 ),this));
     allHexes.add(new Hex((0.6966145833333334 ),(0.49382716049382713 ),this));
     allHexes.add(new Hex((0.78515625 ),(0.49382716049382713 ),this));
     allHexes.add(new Hex((0.875 ),(0.4876543209876543 ),this));
     //Row 4
     allHexes.add(new Hex((0.5579427083333334 ),(0.6306584362139918 ),this));
     allHexes.add(new Hex((0.6516927083333334 ),(0.6193415637860082 ),this));
     allHexes.add(new Hex((0.7421875 ),(0.6244855967078189 ),this));
     allHexes.add(new Hex((0.8326822916666666 ),(0.6193415637860082 ),this));
     //Row 5
     allHexes.add(new Hex((0.6048177083333334 ),(0.7582304526748971 ),this));
     allHexes.add(new Hex((0.6966145833333334 ),(0.7551440329218106 ),this));
     allHexes.add(new Hex((0.7884114583333334 ),(0.757201646090535 ),this));
     


     
    
    //Horizontal paths, from left to right, top to bottom
    //Row 1 
    allPaths.add(new Path((0.5638020833333334 ),(0.19135802469135801 ) ,(0.6061197916666666 ) , (0.15020576131687244 )  ,this));
    allPaths.add(new Path((0.6067708333333334 ),(0.15020576131687244 ) ,(0.6484375 ) , (0.18724279835390947 )  ,this));
    allPaths.add(new Path((0.6484375 ),(0.18724279835390947 ) ,(0.6927083333333334 ) , (0.14814814814814814 )  ,this));
    allPaths.add(new Path((0.6927083333333334 ),(0.14814814814814814 ) ,(0.7356770833333334 ) , (0.1831275720164609 )  ,this));
    allPaths.add(new Path((0.7356770833333334 ),(0.1831275720164609 ) ,(0.7779947916666666 ) , (0.14609053497942387 )  ,this));
    allPaths.add(new Path((0.7779947916666666 ),(0.14609053497942387 ) ,(0.8235677083333334 ) , (0.18518518518518517 )  ,this));
    //Row 2
    allPaths.add(new Path((0.517578125 ),(0.31584362139917693 ) ,(0.5611979166666666 ) , (0.2736625514403292 )  ,this));
    allPaths.add(new Path((0.5611979166666666 ),(0.2726337448559671 ) ,(0.6067708333333334 ) , (0.31378600823045266 )  ,this));
    allPaths.add(new Path((0.6067708333333334 ),(0.31378600823045266 ) ,(0.650390625 ) , (0.2716049382716049 )  ,this));
    allPaths.add(new Path((0.650390625 ),(0.2705761316872428 ) ,(0.6940104166666666 ) , (0.31275720164609055 )  ,this));
    allPaths.add(new Path((0.6940104166666666 ),(0.3117283950617284 ) ,(0.73828125 ) , (0.26954732510288065 )  ,this));
    allPaths.add(new Path((0.73828125 ),(0.26954732510288065 ) ,(0.7825520833333334 ) , (0.3117283950617284 )  ,this));
    allPaths.add(new Path((0.7838541666666666 ),(0.3148148148148148 ) ,(0.8268229166666666 ) , (0.2726337448559671 )  ,this));
    allPaths.add(new Path((0.8307291666666666 ),(0.2726337448559671 ) ,(0.869140625 ) , (0.31069958847736623 )  ,this));

    //Row 3
    allPaths.add(new Path((0.4700520833333333 ),(0.4444444444444444 ) ,(0.515625 ) , (0.4022633744855967 )  ,this));
    allPaths.add(new Path((0.515625 ),(0.40329218106995884 ) ,(0.5592447916666666 ) , (0.44238683127572015 )  ,this));
    allPaths.add(new Path((0.5592447916666666 ),(0.44238683127572015 ) ,(0.60546875 ) , (0.4012345679012346 )  ,this));
    allPaths.add(new Path((0.60546875 ),(0.4012345679012346 ) ,(0.650390625 ) , (0.44238683127572015 )  ,this));
    allPaths.add(new Path((0.6497395833333334 ),(0.4444444444444444 ) ,(0.6953125 ) , (0.4002057613168724 )  ,this));
    allPaths.add(new Path((0.6953125 ),(0.4002057613168724 ) ,(0.7395833333333334 ) , (0.43930041152263377 )  ,this));
    allPaths.add(new Path((0.7395833333333334 ),(0.43930041152263377 ) ,(0.783203125 ) , (0.39609053497942387 )  ,this));
    allPaths.add(new Path((0.783203125 ),(0.39609053497942387 ) ,(0.8287760416666666 ) , (0.43930041152263377 )  ,this));
    allPaths.add(new Path((0.8287760416666666 ),(0.43930041152263377 ) ,(0.873046875 ) , (0.3940329218106996 )  ,this));
    allPaths.add(new Path((0.873046875 ),(0.3940329218106996 ) ,(0.9186197916666666 ) , (0.43930041152263377 )  ,this));
    //Row 4
    allPaths.add(new Path((0.4700520833333333 ),(0.5329218106995884 ) ,(0.5123697916666666 ) , (0.5771604938271605 )  ,this));
    allPaths.add(new Path((0.5123697916666666 ),(0.5771604938271605 ) ,(0.5592447916666666 ) , (0.5318930041152263 )  ,this));
    allPaths.add(new Path((0.5592447916666666 ),(0.5318930041152263 ) ,(0.6028645833333334 ) , (0.5761316872427984 )  ,this));
    allPaths.add(new Path((0.6041666666666666 ),(0.5740740740740741 ) ,(0.650390625 ) , (0.5318930041152263 )  ,this));
    allPaths.add(new Path((0.650390625 ),(0.5318930041152263 ) ,(0.6946614583333334 ) , (0.5740740740740741 )  ,this));
    allPaths.add(new Path((0.6946614583333334 ),(0.5740740740740741 ) ,(0.7408854166666666 ) , (0.5288065843621399 )  ,this));
    allPaths.add(new Path((0.7408854166666666 ),(0.5288065843621399 ) ,(0.7858072916666666 ) , (0.5699588477366255 )  ,this));
    allPaths.add(new Path((0.7858072916666666 ),(0.5699588477366255 ) ,(0.830078125 ) , (0.5277777777777778 )  ,this));
    allPaths.add(new Path((0.830078125 ),(0.5277777777777778 ) ,(0.8763020833333334 ) , (0.5689300411522634 )  ,this));
    allPaths.add(new Path((0.8763020833333334 ),(0.5689300411522634 ) ,(0.9186197916666666 ) , (0.5277777777777778 )  ,this));
    //Row 5
    allPaths.add(new Path((0.5130208333333334 ),(0.6676954732510288 ) ,(0.556640625 ) , (0.7098765432098766 )  ,this));
    allPaths.add(new Path((0.556640625 ),(0.7098765432098766 ) ,(0.603515625 ) , (0.6656378600823045 )  ,this));
    allPaths.add(new Path((0.603515625 ),(0.6656378600823045 ) ,(0.6490885416666666 ) , (0.7098765432098766 )  ,this));
    allPaths.add(new Path((0.6490885416666666 ),(0.7098765432098766 ) ,(0.6953125 ) , (0.6625514403292181 )  ,this));
    allPaths.add(new Path((0.6953125 ),(0.6625514403292181 ) ,(0.7421875 ) , (0.7067901234567902 )  ,this));
    allPaths.add(new Path((0.7421875 ),(0.7067901234567902 ) ,(0.7884114583333334 ) , (0.6635802469135802 )  ,this));
    allPaths.add(new Path((0.7884114583333334 ),(0.6635802469135802 ) ,(0.8346354166666666 ) , (0.7067901234567902 )  ,this));
    allPaths.add(new Path((0.8346354166666666 ),(0.7067901234567902 ) ,(0.8782552083333334 ) , (0.6594650205761317 )  ,this));
    //Row 6
    allPaths.add(new Path((0.5579427083333334 ),(0.8045267489711934 ) ,(0.6028645833333334 ) , (0.8477366255144033 )  ,this));
    allPaths.add(new Path((0.6028645833333334 ),(0.8477366255144033 ) ,(0.6510416666666666 ) , (0.8004115226337448 )  ,this));
    allPaths.add(new Path((0.6510416666666666 ),(0.8004115226337448 ) ,(0.6979166666666666 ) , (0.8467078189300411 )  ,this));
    allPaths.add(new Path((0.6979166666666666 ),(0.8467078189300411 ) ,(0.744140625 ) , (0.8024691358024691 )  ,this));
    allPaths.add(new Path((0.744140625 ),(0.8024691358024691 ) ,(0.7903645833333334 ) , (0.845679012345679 )  ,this));
    allPaths.add(new Path((0.7903645833333334 ),(0.845679012345679 ) ,(0.8359375 ) , (0.8004115226337448 )  ,this));
    
    //Vertical Paths, left to right, top to bottom
    //Row 1
    allPaths.add(new Path((0.5611979166666666 ),(0.18930041152263374 ) ,(0.5611979166666666 ) , (0.2736625514403292 )  ,this));
    allPaths.add(new Path((0.650390625 ),(0.18621399176954734 ) ,(0.6510416666666666 ) , (0.2705761316872428 )  ,this));
    allPaths.add(new Path((0.7369791666666666 ),(0.18415637860082304 ) ,(0.7369791666666666 ) , (0.26954732510288065 )  ,this));
    allPaths.add(new Path((0.8235677083333334 ),(0.18518518518518517 ) ,(0.8255208333333334 ) , (0.26440329218106995 )  ,this));
    //Row 2
    allPaths.add(new Path((0.5162760416666666 ),(0.31893004115226337 ) ,(0.5162760416666666 ) , (0.3991769547325103 )  ,this));
    allPaths.add(new Path((0.6061197916666666 ),(0.3117283950617284 ) ,(0.6061197916666666 ) , (0.3991769547325103 )  ,this));
    allPaths.add(new Path((0.6946614583333334 ),(0.31069958847736623 ) ,(0.6953125 ) , (0.3991769547325103 )  ,this));
    allPaths.add(new Path((0.78125 ),(0.30864197530864196 ) ,(0.7825520833333334 ) , (0.39300411522633744 )  ,this));
    allPaths.add(new Path((0.8697916666666666 ),(0.30761316872427985 ) ,(0.873046875 ) , (0.3950617283950617 )  ,this));
    //Row 3
    allPaths.add(new Path((0.4694010416666667 ),(0.44650205761316875 ) ,(0.4694010416666667 ) , (0.5339506172839507 )  ,this));
    allPaths.add(new Path((0.5598958333333334 ),(0.44238683127572015 ) ,(0.5592447916666666 ) , (0.5288065843621399 )  ,this));
    allPaths.add(new Path((0.650390625 ),(0.44135802469135804 ) ,(0.6510416666666666 ) , (0.5308641975308642 )  ,this));
    allPaths.add(new Path((0.7395833333333334 ),(0.4403292181069959 ) ,(0.7421875 ) , (0.5288065843621399 )  ,this));
    allPaths.add(new Path((0.8287760416666666 ),(0.43930041152263377 ) ,(0.8307291666666666 ) , (0.5267489711934157 )  ,this));
    allPaths.add(new Path((0.91796875 ),(0.43930041152263377 ) ,(0.9192708333333334 ) , (0.522633744855967 )  ,this));
    //Row 4
    allPaths.add(new Path((0.513671875 ),(0.5771604938271605 ) ,(0.5130208333333334 ) , (0.6656378600823045 )  ,this));
    allPaths.add(new Path((0.603515625 ),(0.5740740740740741 ) ,(0.6041666666666666 ) , (0.6646090534979424 )  ,this));
    allPaths.add(new Path((0.6959635416666666 ),(0.5740740740740741 ) ,(0.6966145833333334 ) , (0.6625514403292181 )  ,this));
    allPaths.add(new Path((0.787109375 ),(0.5699588477366255 ) ,(0.7877604166666666 ) , (0.661522633744856 )  ,this));
    allPaths.add(new Path((0.8756510416666666 ),(0.5689300411522634 ) ,(0.87890625 ) , (0.6563786008230452 )  ,this));
    //Row 5
    allPaths.add(new Path((0.5579427083333334 ),(0.7129629629629629 ) ,(0.5579427083333334 ) , (0.8004115226337448 )  ,this));
    allPaths.add(new Path((0.6516927083333334 ),(0.7088477366255144 ) ,(0.6510416666666666 ) , (0.801440329218107 )  ,this));
    allPaths.add(new Path((0.7421875 ),(0.7078189300411523 ) ,(0.7447916666666666 ) , (0.7993827160493827 )  ,this));
    allPaths.add(new Path((0.8333333333333334 ),(0.7078189300411523 ) ,(0.833984375 ) , (0.7973251028806584 )  ,this));
	
    for(int i = 0; i < allJoints.size();i++){
    	allJoints.get(i).setAdjacent(this);
    }
	

	 for(int i = 0; i < allHexes.size(); i++){
		 if(allHexes.get(i).hexType == HexType.SAND)//Put the robber in the sand
			 rob = new Robber(i);
	 }
	 
	 
}


public ArrayList<String> allPossibleActions(){
	
	ArrayList<String> allActions = new ArrayList<String>();
	

	
	for(int i = 0; i < allHexes.size(); i++){
		String s = getRobber(i);
		if(!s.equals("End Turn")){
			//fadsgaD.println("Trade "+i+" "+x);
			allActions.add(s);
		}
	}
	
	
	for(int i = 0; i < allJoints.size(); i++){
		
		if(getSettlementAction(i) != null){		
			allActions.add(getSettlementAction(i));
		}
		
		if(getCityAction(i) != null){
			allActions.add(getCityAction(i));
		}
		
	}
	
	
	for(int i = 0; i < allPaths.size(); i++){
		
		if(getPathAction(i) != null){		
			allActions.add(getPathAction(i));
		}
		
		
	}
	
	if(!getPlayDevCard(1).equals("End Turn"))
		allActions.add("Knight");
	
	if(!getPlayDevCard(2).equals("End Turn"))
		allActions.add("Plenty");
	
	if(!getPlayDevCard(3).equals("End Turn"))
		allActions.add("Monopoly");
	
	if(!getPlayDevCard(4).equals("End Turn"))
		allActions.add("Roads");
	
	

	if(!getBuyDevCard(true).equals("End Turn"))
		allActions.add("Buy");

		
	for(int i = 1; i < 6; i++){
		for(int x = 1; x < 6; x++){
			String s = getTradeWithBank(i,x);
			if(!s.equals("End Turn")){
				//fadsgaD.println("Trade "+i+" "+x);
				allActions.add("Trade "+i+" "+x);
			}
		}
	}		
	
	return allActions;
}



public String getAction(HashSet<Integer> selectedJoints, int playerCardSelect, int cardSelect, boolean buyCard, int devCardSelect) {
	
	
	if(this.gameOver)
		return "Game Over";
	
	
	if(allPlayers.get(player).isRobot)
		return "End Turn";
	
	
	String buttonString = "End Turn";
	
	if(selectedJoints.isEmpty()){
		buttonString = getTradeWithBank( playerCardSelect, cardSelect);
		if(!buttonString.equals("End Turn")){
			return buttonString;
		}
		
		
		buttonString = getBuyDevCard(buyCard);
		
		
		if(!buttonString.equals("End Turn")){
			return buttonString;
		}
		
		
		
		buttonString = getPlayDevCard(devCardSelect);
		
		if(!buttonString.equals("End Turn")){
			return buttonString;
		}
		
		
	}
	else{
		
		
	
	
		
		
	buttonString = getPlaceRobber(selectedJoints);	
		
	if(!buttonString.equals("End Turn")){
		return buttonString;
	}
	
	buttonString = getPlacePath(selectedJoints);
	
	if(!buttonString.equals("End Turn")){
		return buttonString;
	}
	
	
	
	if(selectedJoints.size() != 1){
		return "End Turn";
	}
	
	buttonString = getPlaceSettlementOrCity(selectedJoints);
	
			if(!buttonString.equals("End Turn")){
				return buttonString;
			}
	
	
	}
	
	
	return buttonString;
}


/*
public String getAction(HashSet<Integer> selectedJoints, int sheep, int wheat, int wood,
		int stone, int brick, int cardSelect) {
	
	
	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}
	
	
	
	
	
if(!selectedJoints.isEmpty()){
	
	for(int i = 0; i < allHexes.size(); i++){
		if(allHexes.get(i).joints.equals(selectedJoints)){//Need to add more conditions and actions
			return "Select Hex";//Can return after finding the right thing
			}
	}
	for(int i = 0; i < allPaths.size(); i++){
		if(allPaths.get(i).joints.equals(selectedJoints)){//Need to add more conditions and actions
			if(isGoodPathPlacement(i)){
			return "Place Path";//Can return after finding the right thing
			
		}else
		{
			return "End Turn";
			}
		}
	}
	
	if(selectedJoints.size()!=1){//Check and make sure only one left
		return "End Turn";
		}
	
	
	
	for(int i = 0; i < allJoints.size(); i++){//houses
		
		if(selectedJoints.contains(i)){//Need to add more conditions and actions
			if(isGoodHousePlacement(i)){
				return "Place House";
			}
			else{ 
				return "End Turn";}
			
}
		
			
			}
	
	
	return "End Turn";}
else{
	
if(sheepCount >= sheep&& woodCount >= wood&& wheatCount >= wheat && stoneCount >= stone
&&brickCount >= brick){
if(sheep + wood + wheat + stone + brick == 4){
	
	if(cardSelect == 1 ){
		return "Trade for Sheep";
	}
	else if(cardSelect == 2){
		return "Trade for Wheat";
	}
	else if(cardSelect == 3){
		return "Trade for Wood";
	}
	else if(cardSelect == 4){
		return "Trade for Stone";
	}
	else if(cardSelect == 5){
		return "Trade for Brick";
	}
	else{
		return "End Turn";
	}
	
	
	
}
else{
return "End Turn";	



}
}
else{
	
return "End Turn";
}
	
	
}	
}
*/

public void playMonopoly(int cardSelect){
	
	if(cardSelect == 0)
		return;
	
	if(allPlayers.get(player).devCards.get(DevelopmentCardType.MONOPOLY)>0&&!cardPlayed){
		int dec = allPlayers.get(player).devCards.get(DevelopmentCardType.MONOPOLY) -1;
		allPlayers.get(player).devCards.put(DevelopmentCardType.MONOPOLY, dec);
		
		cardPlayed = true;
	
          
         ResourceCardType res = null;
          
          if(cardSelect == 1){
        	 res = ResourceCardType.BRICKRC;
          }
          else if( cardSelect == 2){
        	  res = ResourceCardType.STONERC; 
          }
          else if( cardSelect == 3){
        	  res = ResourceCardType.WHEATRC; 
          }
          else if( cardSelect == 4){
        	  res = ResourceCardType.SHEEPRC; 
          }
          else if( cardSelect == 5){
        	  res = ResourceCardType.FORESTRC; 
          }
          
          if(res == null){
        	  System.exit(1);
          }
        	  
          for(int i = 0; i < allPlayers.size(); i++){
        	  if(i != player){
        		  while(allPlayers.get(i).cards.contains(res)){
        			  allPlayers.get(i).cards.remove(res);
        			  allPlayers.get(player).cards.add(res);
        		  }
        	  }
          }
		
		
	}
}


public void playRoads(){
	
	
	
	if(allPlayers.get(player).devCards.get(DevelopmentCardType.ROADS)>0&&!cardPlayed){
		
		cardPlayed = true;
		
		int dec = allPlayers.get(player).devCards.get(DevelopmentCardType.ROADS) -1;
		allPlayers.get(player).devCards.put(DevelopmentCardType.ROADS, dec);
	
	allPlayers.get(player).cards.add(ResourceCardType.FORESTRC);
	allPlayers.get(player).cards.add(ResourceCardType.FORESTRC);
	allPlayers.get(player).cards.add(ResourceCardType.BRICKRC);
	allPlayers.get(player).cards.add(ResourceCardType.BRICKRC);
	}
}



public void playPlenty(int card1, int card2){
	
	
	if(allPlayers.get(player).devCards.get(DevelopmentCardType.YEAR_OF_PLENTY)>0&&!cardPlayed){
		
		cardPlayed = true;
		
		int dec = allPlayers.get(player).devCards.get(DevelopmentCardType.YEAR_OF_PLENTY) -1;
		allPlayers.get(player).devCards.put(DevelopmentCardType.YEAR_OF_PLENTY, dec);
	
	if(card1 == 0){
		allPlayers.get(player).cards.add(ResourceCardType.BRICKRC);
	}
	else if(card1 == 1){
		allPlayers.get(player).cards.add(ResourceCardType.STONERC);
	}
	else if(card1 == 2){
		allPlayers.get(player).cards.add(ResourceCardType.WHEATRC);
	}
	else if(card1 == 3){
		allPlayers.get(player).cards.add(ResourceCardType.SHEEPRC);
	}
	else if(card1 == 4){
		allPlayers.get(player).cards.add(ResourceCardType.FORESTRC);
	}
	
	
	if(card2 == 0){
		allPlayers.get(player).cards.add(ResourceCardType.BRICKRC);
	}
	else if(card2 == 1){
		allPlayers.get(player).cards.add(ResourceCardType.STONERC);
	}
	else if(card2 == 2){
		allPlayers.get(player).cards.add(ResourceCardType.WHEATRC);
	}
	else if(card2 == 3){
		allPlayers.get(player).cards.add(ResourceCardType.SHEEPRC);
	}
	else if(card2 == 4){
		allPlayers.get(player).cards.add(ResourceCardType.FORESTRC);
	}
	
	}
}


public void playKnight(){
	if(allPlayers.get(player).devCards.get(DevelopmentCardType.KNIGHT)>0&&!cardPlayed){
		int dec = allPlayers.get(player).devCards.get(DevelopmentCardType.KNIGHT) -1;
		allPlayers.get(player).devCards.put(DevelopmentCardType.KNIGHT, dec);
		
		int inc =  allPlayers.get(player).devCards.get(DevelopmentCardType.PLAYED_KNIGHT) +1;
		allPlayers.get(player).devCards.put(DevelopmentCardType.PLAYED_KNIGHT, inc);
		
		cardPlayed = true;
		playedKnight = true;
		
	}
}

private String getPlayDevCard(int devCardSelect) {
	// TODO Auto-generated method stub
	
	if(cardPlayed)
		return "End Turn";
	
	if(devCardSelect == 1){
		if(allPlayers.get(player).devCards.get(DevelopmentCardType.KNIGHT)>0)
			return "Play Knight";
	}
	else if(devCardSelect == 3){
		if(allPlayers.get(player).devCards.get(DevelopmentCardType.MONOPOLY)>0)
			return "Play Monopoly";
	}
	else if(devCardSelect == 4){
		if(allPlayers.get(player).devCards.get(DevelopmentCardType.ROADS)>0)
			return "Play Roads";
	}
	else if(devCardSelect == 2){
		if(allPlayers.get(player).devCards.get(DevelopmentCardType.YEAR_OF_PLENTY)>0)
			return "Play Plenty";
		
	}

	
	
	
	return "End Turn";
}

public void buyDevCard(){
	
	if(allPlayers.get(player).cards.contains(ResourceCardType.SHEEPRC)
			&&allPlayers.get(player).cards.contains(ResourceCardType.STONERC)
			&&allPlayers.get(player).cards.contains(ResourceCardType.WHEATRC)
			&&!cardPile.isEmpty()){
	
		
	
		allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
		allPlayers.get(player).cards.remove(ResourceCardType.STONERC);
		allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
		
		DevelopmentCardType d = this.cardPile.remove(0);
		int inc = allPlayers.get(player).devCards.get(d) + 1;
		allPlayers.get(player).devCards.put(d, inc);
	}
	
	
	
	
}

private String getBuyDevCard(boolean buyCard) {
	// TODO Auto-generated method stub
	
	if(buyCard){
		if(allPlayers.get(player).cards.contains(ResourceCardType.SHEEPRC)
				&&allPlayers.get(player).cards.contains(ResourceCardType.STONERC)
				&&allPlayers.get(player).cards.contains(ResourceCardType.WHEATRC)
				&&!cardPile.isEmpty()){
		
		return "Buy Dev Card";
		}
	}
	

	return "End Turn";
}



/**
 * @deprecated Do not use this method! I altered the code to have more specific methods.
 * 		-Scott Lafoon
 * 
 * @param selectedJoints
 * @param sheep
 * @param wheat
 * @param wood
 * @param stone
 * @param brick
 * @param cardSelect
 */
public void doAction(HashSet<Integer> selectedJoints, int sheep, int wheat, int wood,
		int stone, int brick, int cardSelect) {
	
	
	if(selectedJoints.isEmpty()){
		//if(tradeWithBank( sheep, wood, wheat, stone, brick, cardSelect)){
			return;
		//}
	}
	else{
	if(placeRobber(selectedJoints)){
		return;
	}
	else if(placePath(selectedJoints)){
		return;
	}
	else if(selectedJoints.size() != 1){
		return;
	}
	//else if(placeSettlementOrCity(selectedJoints)){
		//return;
	//}
	}
	
	
}



private String getPlacePath(HashSet<Integer> selectedJoints) {
	// TODO Auto-generated method stub
	
	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}
	
	
	if(turn == 1){
		
		for(int i = 0; i < allPaths.size(); i++){
			if(allPaths.get(i).joints.equals(selectedJoints)){//Need to add more conditions and actions
				if(isGoodPathPlacement(i)&&!haveRoad()){
				return "Place Road";//Can return after finding the right thing
				
			}else
			{
				
				return "End Turn";}
			}
		}}
	else if(turn == 2){
		
		for(int i = 0; i < allPaths.size(); i++){
			if(allPaths.get(i).joints.equals(selectedJoints)){//Need to add more conditions and actions
				if(isGoodPathPlacement(i)&&!have2Road()){
				return "Place Road";//Can return after finding the right thing
				
			}else
			{
				return "End Turn";}
			}
		}
		
	}
	else{
		for(int i = 0; i < allPaths.size(); i++){
			if(allPaths.get(i).joints.equals(selectedJoints)){//Need to add more conditions and actions
				if(isGoodPathPlacement(i)&&woodCount >= 1 && brickCount >=1){

				return "Place Road";//Can return after finding the right thing
				
			}else
			{
				return "End Turn";}
			}
		}
	}
	
	return "End Turn";// if you weren't supposed to place a path
}


public String getPathAction(int index) {
	// TODO Auto-generated method stub
	
	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}
	
	
	if(turn == 1){
		
	
			//Need to add more conditions and actions
				if(isGoodPathPlacement(index)&&!haveRoad()){
				return "Path "+index;//Can return after finding the right thing
				
			}
			
		}
	else if(turn == 2){
		
		//add more conditions and actions
				if(isGoodPathPlacement(index)&&!have2Road()&&settlementDoesntHaveRoad(index)){
				return "Path "+index;//Can return after finding the right thing
				
			}
			
		
		
	}
	else{
		
		//Need to add more conditions and actions
				if(isGoodPathPlacement(index)&&woodCount >= 1 && brickCount >=1){
				
				return "Path "+index;//Can return after finding the right thing
				
			}
	else if(isGoodPathPlacement(index)&&(allPlayers.get(player).isRobot||isTraining)){

		if(brickCount >= 1){//trade for a wood
		
		if(sheepCount >= 4)//trade for a wood
			return "Path "+index;
		
		if(stoneCount >= 4)//trade for a wood
			return "Path "+index;
		
		if(wheatCount >= 4)//trade for a wood
			return "Path "+index;
		
		if(brickCount >= 5)
			return "Path "+index;
	
		}
		else if(woodCount >= 1){
			if(sheepCount >= 4)//trade for a brick
				return "Path "+index;
			
			if(stoneCount >= 4)//trade for a brick
				return "Path "+index;
			
			if(wheatCount >= 4)//trade for a brick
				return "Path "+index;
			
			if(woodCount >= 5)//trade for a brick
				return "Path "+index;
			
		}
					
				
	}
			
		
	}
	
	return null;// if you weren't supposed to place a path
}



public boolean placePath(HashSet<Integer> selectedJoints) {
	// TODO Auto-generated method stub
	
	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}
	
	
	if(turn == 1){
		
		for(int i = 0; i < allPaths.size(); i++){
			if(allPaths.get(i).joints.equals(selectedJoints)){//Need to add more conditions and actions
				if(isGoodPathPlacement(i)&&!haveRoad()){
				allPaths.get(i).color = allPlayers.get(player).color;
				return true;//Can return after finding the right thing
				
			}
			}
		}}
	else if(turn == 2){
		
		for(int i = 0; i < allPaths.size(); i++){
			if(allPaths.get(i).joints.equals(selectedJoints)){//Need to add more conditions and actions
				if(isGoodPathPlacement(i)&&!have2Road()&&settlementDoesntHaveRoad(i)){
				allPaths.get(i).color = allPlayers.get(player).color;
				return true;//Can return after finding the right thing
				
			}
			}
		}
		
	}
	else{
		for(int i = 0; i < allPaths.size(); i++){
			if(allPaths.get(i).joints.equals(selectedJoints)){//Need to add more conditions and actions
				if(isGoodPathPlacement(i)&&woodCount >= 1 && brickCount >=1){
				allPaths.get(i).color = allPlayers.get(player).color;
				allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
				allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
				UserInterface.emptySelectedJoints();
				return true;//Can return after finding the right thing
				
			}
			}
		}
	}
	
	return false;// if you weren't supposed to place a path
}

private boolean settlementDoesntHaveRoad(int i) {
	// TODO Auto-generated method stub
	
	int jointIndex = -1;
	
	for(Integer num: allPaths.get(i).joints){
		if(allJoints.get(num).color.equals(allPlayers.get(player)))//city road is gonna be attatched to
			jointIndex = num;
	}
	
	for(int x = 0; x < allPaths.size(); x++){
		if(allPaths.get(x).color.equals(allPlayers.get(player).color)&&allPaths.get(x).joints.contains(jointIndex))
			return false;
	}
	
	return true;
}

public String getRobber(int index) {
	// TODO Auto-generated method stub
	
	
		//Need to add more conditions and actions
			if(currentRoll == 7||playedKnight){
			return "Robber "+index;//Can return after finding the right thing
			}
	
	
	
	return "End Turn";//If it wasn't supposed to place robber
}


public boolean placeRobber(int index) {
	// TODO Auto-generated method stub
	
	
		//Need to add more conditions and actions
			if(currentRoll == 7||playedKnight){
			stealACard(index);	
			currentRoll = 20;
			//method to steal one resource card from wherever they place the robber
			rob.move(index);
			playedKnight = false;
			return true;//Can return after finding the right thing
			}
	
	
	
	return false;//If it wasn't supposed to place robber
}



public boolean placeRobber(HashSet<Integer> selectedJoints) {
	// TODO Auto-generated method stub
	
	for(int i = 0; i < allHexes.size(); i++){
		if(allHexes.get(i).joints.equals(selectedJoints)){//Need to add more conditions and actions
			if(currentRoll == 7||playedKnight){
			stealACard(i);	
			//method to steal one resource card from wherever they place the robber
			currentRoll = 20;
			rob.move(i);
			playedKnight = false;
			return true;//Can return after finding the right thing
			}}
	}
	
	
	return false;//If it wasn't supposed to place robber
}

private String getPlaceRobber(HashSet<Integer> selectedJoints) {
	// TODO Auto-generated method stub
	
	for(int i = 0; i < allHexes.size(); i++){
		if(allHexes.get(i).joints.equals(selectedJoints)){//Need to add more conditions and actions
			if(currentRoll == 7||playedKnight){
			return "Move Robber";//Can return after finding the right thing
			}}
	}
	
	
	return "End Turn";//If it wasn't supposed to place robber
}


private String getPlaceSettlementOrCity(HashSet<Integer> selectedJoints) {
	// TODO Auto-generated method stub

	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}	
	
	
	
if(turn == 1){
	
	for(int i = 0; i < allJoints.size(); i++){//houses
		
		if(selectedJoints.contains(i)){//Need to add more conditions and actions
			if(isGoodHousePlacement(i)&&!haveHouse()){
			return "Place Settlement";
			}

			else{ 
				return "Next Turn";}
			
			}
			}
	
}	
else if(turn == 2){
	for(int i = 0; i < allJoints.size(); i++){//houses
		
		if(selectedJoints.contains(i)){//Need to add more conditions and actions
			if(isGoodHousePlacement(i)&&!have2House()){
			return "Place Settlement";
			}

			else{ 
				return "End Turn";}
			}
		
			
			}
	
}
else{
	
	for(int i = 0; i < allJoints.size(); i++){//houses
		
		if(selectedJoints.contains(i)){//Need to add more conditions and actions
			if(isGoodHousePlacement(i)&&
			brickCount >= 1&& woodCount >= 1 && wheatCount >= 1 && sheepCount >=1){
					return "Place Settlement";
			}
			else if(isGoodCityPlacement(i)&& wheatCount >= 2 && stoneCount >= 3){
					return "Place City";
			}
			else{ 
				return "End Turn";}
			
			}
		
			
			}
}
	
	return "End Turn";
}


public String getCityAction(int index){
	
	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}	
	
	
	
	if(isGoodCityPlacement(index)&& wheatCount >= 2 && stoneCount >= 3){	
		return "City "+index;
	}
	else if(isGoodCityPlacement(index)&&(allPlayers.get(player).isRobot||isTraining)){

		
	}
	
	return null;
}


public String getSettlementAction(int index) {
	// TODO Auto-generated method stub

	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}	
	
	
	
if(turn == 1){
	

			if(isGoodHousePlacement(index)&&!haveHouse()){
				return "Settlement "+index;
			}

		
			
			
			
	
}	
else if(turn == 2){
	
		
	
			if(isGoodHousePlacement(index)&&!have2House()){
			return "Settlement "+index;
			}

		
			
			
		
			
			
	
}
else{
	

			if(isGoodHousePlacement(index)&&
			brickCount >= 1&& woodCount >= 1 && wheatCount >= 1 && sheepCount >=1){
					return "Settlement "+index;
			}
			else if(isGoodHousePlacement(index)&&(allPlayers.get(player).isRobot||isTraining)){

				if(brickCount ==0&& woodCount >= 1 && wheatCount >= 1 && sheepCount >=1){
					
					if(woodCount >= 5)
						return "Settlement "+index;
					
					if(wheatCount >= 5)
						return "Settlement "+index;
					
					if(sheepCount >= 5)
						return "Settlement "+index;	
						
				
					if(stoneCount >= 4)
						return "Settlement "+index;
				
				}
				if(brickCount >=1&& woodCount == 0 && wheatCount >= 1 && sheepCount >=1){
					if(brickCount >= 5)
						return "Settlement "+index;
					
					if(wheatCount >= 5)
						return "Settlement "+index;
					
					if(sheepCount >= 5)
						return "Settlement "+index;	
						
				
					if(stoneCount >= 4)
						return "Settlement "+index;
			}
				
				
				if(brickCount >=1&& woodCount >= 1 && wheatCount == 0 && sheepCount >=1){
					if(woodCount >= 5)
						return "Settlement "+index;
					
					if(brickCount >= 5)
						return "Settlement "+index;
					
					if(sheepCount >= 5)
						return "Settlement "+index;	
						
				
					if(stoneCount >= 4)
						return "Settlement "+index;
			}
				
				
				if(brickCount >=1&& woodCount >= 1 && wheatCount >= 1 && sheepCount ==0){
					if(woodCount >= 5)
						return "Settlement "+index;
					
					if(wheatCount >= 5)
						return "Settlement "+index;
					
					if(brickCount >= 5)
						return "Settlement "+index;	
						
					if(stoneCount >= 4)
						return "Settlement "+index;
			}
				
				
			}
			
			
			}
return null;
		
			
		
	

}



public boolean placeCity(HashSet<Integer> selectedJoints){
	
	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}	
	
	
	for(int i = 0; i < allJoints.size(); i++){
	if(selectedJoints.contains(i)){
	if(isGoodCityPlacement(i)&& wheatCount >= 2 && stoneCount >= 3){
		allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
		allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
		allPlayers.get(player).cards.remove(ResourceCardType.STONERC);
		allPlayers.get(player).cards.remove(ResourceCardType.STONERC);
		allPlayers.get(player).cards.remove(ResourceCardType.STONERC);
		allJoints.get(i).city = true;
		
		
		return true;
	}
	}}
	return false;
}


public boolean placeSettlement(int index) {
	// TODO Auto-generated method stub

	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}	
	
	
	
if(turn == 1){
	
	//houses
		
		
			if(isGoodHousePlacement(index)&&!haveHouse()){
			allJoints.get(index).color = allPlayers.get(player).color;
			}

		
			
			return true;
			
	
}	
else if(turn == 2){

		
		//Need to add more conditions and actions
			if(isGoodHousePlacement(index)&&!have2House()){
			allJoints.get(index).color = allPlayers.get(player).color;
			}

			else{ 
				}
			
			return true;
		
			
			
	
}
else{
	

		
	
			if(isGoodHousePlacement(index)&&
			brickCount >= 1&& woodCount >= 1 && wheatCount >= 1 && sheepCount >=1){
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(index).color = allPlayers.get(player).color;
			}
	else if(isGoodHousePlacement(index)&&(allPlayers.get(player).isRobot||isTraining)){

		if(brickCount ==0&& woodCount >= 1 && wheatCount >= 1 && sheepCount >=1){
			
			if(woodCount >= 5){
				tradeWithBank(3,5);
				allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
				allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
				allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
				allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
				allJoints.get(index).color = allPlayers.get(player).color;
				return true;}
			
			if(wheatCount >= 5){
				tradeWithBank(2,5);
				allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
				allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
				allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
				allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
				allJoints.get(index).color = allPlayers.get(player).color;
				return true;
			}
			if(sheepCount >= 5){
				tradeWithBank(1,5);
				allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
				allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
				allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
				allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
				allJoints.get(index).color = allPlayers.get(player).color;
				return true;
			}
		
			if(stoneCount >= 4){
				tradeWithBank(4,5);
				allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
				allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
				allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
				allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
				allJoints.get(index).color = allPlayers.get(player).color;
				return true;
			}
		}
		if(brickCount >=1&& woodCount == 0 && wheatCount >= 1 && sheepCount >=1){
			if(brickCount >= 5)
				{tradeWithBank(5,3);
				allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
				allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
				allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
				allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
				allJoints.get(index).color = allPlayers.get(player).color;
				return true;}
			
			if(wheatCount >= 5)
			{tradeWithBank(2,3);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(index).color = allPlayers.get(player).color;
			return true;}
			
			if(sheepCount >= 5)
			{tradeWithBank(1,3);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(index).color = allPlayers.get(player).color;
			return true;}
				
		
			if(stoneCount >= 4)
			{tradeWithBank(4,3);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(index).color = allPlayers.get(player).color;
			return true;}
	}
		
		
		if(brickCount >=1&& woodCount >= 1 && wheatCount == 0 && sheepCount >=1){
			if(woodCount >= 5)
			{tradeWithBank(3,2);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(index).color = allPlayers.get(player).color;
			return true;}
			
			if(brickCount >= 5)
			{tradeWithBank(5,2);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(index).color = allPlayers.get(player).color;
			return true;}
			
			if(sheepCount >= 5)
			{tradeWithBank(1,2);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(index).color = allPlayers.get(player).color;
			return true;}
				
		
			if(stoneCount >= 4)
			{tradeWithBank(4,2);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(index).color = allPlayers.get(player).color;
			return true;}
	}
		
		
		if(brickCount >=1&& woodCount >= 1 && wheatCount >= 1 && sheepCount ==0){
			if(woodCount >= 5)
			{tradeWithBank(3,1);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(index).color = allPlayers.get(player).color;
			return true;}
			
			if(wheatCount >= 5)
			{tradeWithBank(2,1);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(index).color = allPlayers.get(player).color;
			return true;}
			
			if(brickCount >= 5)
			{tradeWithBank(5,1);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(index).color = allPlayers.get(player).color;
			return true;}
				
			if(stoneCount >= 4)
			{tradeWithBank(4,1);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(index).color = allPlayers.get(player).color;
			return true;}
	}
		
		
				
			}
			
			
			return true;
		
			
			
}
}




public boolean placeSettlement(HashSet<Integer> selectedJoints) {
	// TODO Auto-generated method stub

	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}	
	
	
	
if(turn == 1){
	
	for(int i = 0; i < allJoints.size(); i++){//houses
		
		if(selectedJoints.contains(i)){//Need to add more conditions and actions
			if(isGoodHousePlacement(i)&&!haveHouse()){
			allJoints.get(i).color = allPlayers.get(player).color;
			}

		
			
			return true;}
			}
	
}	
else if(turn == 2){
	for(int i = 0; i < allJoints.size(); i++){//houses
		
		if(selectedJoints.contains(i)){//Need to add more conditions and actions
			if(isGoodHousePlacement(i)&&!have2House()){
			allJoints.get(i).color = allPlayers.get(player).color;
			}

			else{ 
				}
			
			return true;}
		
			
			}
	
}
else{
	
	for(int i = 0; i < allJoints.size(); i++){//houses
		
		if(selectedJoints.contains(i)){//Need to add more conditions and actions
			if(isGoodHousePlacement(i)&&
			brickCount >= 1&& woodCount >= 1 && wheatCount >= 1 && sheepCount >=1){
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
			allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
			allJoints.get(i).color = allPlayers.get(player).color;
			}
			
			
			return true;}
		
			
			}
}
	
	return false;
}

private void stealACard(int i) {
	// TODO Auto-generated method stub
	
	
	for(Integer num : allHexes.get(i).joints){
		for(int y = 0; y < allPlayers.size(); y++){
			if(allJoints.get(num).color.equals(allPlayers.get(y).color)&& y != player){
				if(allPlayers.get(y).cards.size() > 0){
					Collections.shuffle(allPlayers.get(player).cards);
					allPlayers.get(player).cards.add(allPlayers.get(y).cards.remove(0));
					return;
				}
					
			}
		}
	}
	
	
}

private boolean isGoodCityPlacement(int i) {
	// TODO Auto-generated method stub
	if(allJoints.get(i).color.equals(allPlayers.get(player).color)&&
			!allJoints.get(i).city)
		return true;
	
	return false;
}

private boolean isGoodHousePlacement(int i) {
	// TODO Auto-generated method stub
	

	
	if(!allJoints.get(i).color.equals(new Color(237,201,175))) //blank circle
		return false;
	
	for(int x = 0; x < allJoints.get(i).adjacent.size(); x++){ //away from everything else
		if(!allJoints.get(i).adjacent.get(x).color.equals(new Color(237,201,175)))
			return false;
	}
	
	if(turn < 3)// Good for the beginners
	return true;
	
	
	for(int x = 0; x < allPaths.size(); x++){
		if(allPaths.get(x).joints.contains(i)&&allPaths.get(x).color.equals(allPlayers.get(player).color))
				return true;
	}
	
	return false;
}


private boolean isGoodPathPlacement(int i){
	
	int totalCount = 0;
	
	for(int x = 0; x < allPaths.size(); x++){
		if(allPaths.get(x).color.equals(allPlayers.get(player).color))
			totalCount ++;
	}
	
	if(totalCount > 14)//Have already used all 15 pieces
		return false;
	
	
	if(!allPaths.get(i).color.equals(new Color(129,98,78)))//If path isn't empty
		return false;
	
	
	
	for(Integer num: allPaths.get(i).joints){
		
		if(allJoints.get(num).color.equals(allPlayers.get(player).color)){ //if one of the joints is equal
			if(turn > 2)
			return true;
			else{
			
				for(int x = 0; x < allPaths.size(); x++){
					
					if(allPaths.get(x).joints.contains(num)&&!allPaths.get(x).color.equals(new Color(129,98,78)))
						return false;
				}
				
				return true;
			}
		}
	}
	
	if(turn < 3)
		return false;
	
	int first = -1;
	int second = -1;
	
	for(Integer num: allPaths.get(i).joints){
		
		if(first == -1)
			first = num;
		else 
			second = num;
	
	}
	
	
	for(int x = 0; x < allPaths.size(); x++){
		if((allPaths.get(x).joints.contains(first)
			&& allJoints.get(first).color.equals(new Color(237,201,175))	)
				||(allPaths.get(x).joints.contains(second)
					&& allJoints.get(second).color.equals(new Color(237,201,175)))){
			if(allPaths.get(x).color.equals(allPlayers.get(player).color))
				return true;
		}
		
	}

	
	
	
	
	
	return false;
	
}



public void tradeWithBank(int playerCardSelect ,int cardSelect){
	
	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	

	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}
	
	int sheep = 0;
	int wood = 0;
	int wheat = 0;
	int stone = 0;
	int brick = 0;
	
	if(playerCardSelect == 1){
		sheep = 4;
	}
	else if(playerCardSelect == 2){
		wheat = 4;
	}
	else if(playerCardSelect == 3){
		wood = 4;
	}
	else if(playerCardSelect == 4){
		stone = 4;
	}
	else if(playerCardSelect == 5){
		brick = 4;
	}
	else{
		return; 
	}
	
	for(int i = 0; i < allPorts.size(); i++){
		
		if(allJoints.get(allPorts.get(i).joint1).color.equals(allPlayers.get(player).color)||
				allJoints.get(allPorts.get(i).joint2).color.equals(allPlayers.get(player).color)){
			
			if(playerCardSelect == 1 && allPorts.get(i).type == null){
				sheep = 3;
			}
			else if(playerCardSelect == 2&& allPorts.get(i).type == null){
				wheat = 3;
			}
			else if(playerCardSelect == 3&& allPorts.get(i).type == null){
				wood = 3;
			}
			else if(playerCardSelect == 4&& allPorts.get(i).type == null){
				stone = 3;
			}
			else if(playerCardSelect == 5&& allPorts.get(i).type == null){
				brick = 3;
			}
			
			
			
		}	
		
		
	}
	
	
	
	for(int i = 0; i < allPorts.size(); i++){
		
		if(allJoints.get(allPorts.get(i).joint1).color.equals(allPlayers.get(player).color)||
				allJoints.get(allPorts.get(i).joint2).color.equals(allPlayers.get(player).color)){
			
			if(playerCardSelect == 1 && allPorts.get(i).type == ResourceCardType.SHEEPRC){
				sheep = 2;
			}
			else if(playerCardSelect == 2&& allPorts.get(i).type == ResourceCardType.WHEATRC){
				wheat = 2;
			}
			else if(playerCardSelect == 3&& allPorts.get(i).type == ResourceCardType.FORESTRC){
				wood = 2;
			}
			else if(playerCardSelect == 4&& allPorts.get(i).type == ResourceCardType.STONERC){
				stone = 2;
			}
			else if(playerCardSelect == 5&& allPorts.get(i).type == ResourceCardType.BRICKRC){
				brick = 2;
			}
			
			
			
		}	
		
		
	}
	
	
	
	if(sheepCount >= sheep&& woodCount >= wood&& wheatCount >= wheat && stoneCount >= stone
			&&brickCount >= brick){
			
				
				if(cardSelect == 1 ){
					for(int i = 0; i < sheep; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
					}
					for(int i = 0; i < wheat; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
					}
					for(int i = 0; i < wood; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
					}
					for(int i = 0; i < stone; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.STONERC);
					}
					for(int i = 0; i < brick; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
					}
					UserInterface.emptyBank();
					allPlayers.get(player).cards.add(ResourceCardType.SHEEPRC);
				}
				else if(cardSelect == 2){
					for(int i = 0; i < sheep; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
					}
					for(int i = 0; i < wheat; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
					}
					for(int i = 0; i < wood; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
					}
					for(int i = 0; i < stone; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.STONERC);
					}
					for(int i = 0; i < brick; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
					}
					UserInterface.emptyBank();
					allPlayers.get(player).cards.add(ResourceCardType.WHEATRC);	
				}
				else if(cardSelect == 3){
					for(int i = 0; i < sheep; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
					}
					for(int i = 0; i < wheat; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
					}
					for(int i = 0; i < wood; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
					}
					for(int i = 0; i < stone; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.STONERC);
					}
					for(int i = 0; i < brick; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
					}
					UserInterface.emptyBank();
					allPlayers.get(player).cards.add(ResourceCardType.FORESTRC);
				}
				else if(cardSelect == 4){
					for(int i = 0; i < sheep; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
					}
					for(int i = 0; i < wheat; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
					}
					for(int i = 0; i < wood; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
					}
					for(int i = 0; i < stone; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.STONERC);
					}
					for(int i = 0; i < brick; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
					}
					UserInterface.emptyBank();
					
					allPlayers.get(player).cards.add(ResourceCardType.STONERC);
				}
				else if(cardSelect == 5){
					for(int i = 0; i < sheep; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.SHEEPRC);
					}
					for(int i = 0; i < wheat; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
					}
					for(int i = 0; i < wood; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
					}
					for(int i = 0; i < stone; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.STONERC);
					}
					for(int i = 0; i < brick; i++){
						allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
					}
					
					UserInterface.emptyBank();
					
					allPlayers.get(player).cards.add(ResourceCardType.BRICKRC);
				}
				else{
					
					return;
				}
				
				
	
			return;
			}
	
	return ;
}






public String getTradeWithBank(int playerCardSelect ,int cardSelect){
	

	
	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}
	
	int sheep = 0;
	int wood = 0;
	int wheat = 0;
	int stone = 0;
	int brick = 0;
	
	if(playerCardSelect == 1){
		sheep = 4;
	}
	else if(playerCardSelect == 2){
		wheat = 4;
	}
	else if(playerCardSelect == 3){
		wood = 4;
	}
	else if(playerCardSelect == 4){
		stone = 4;
	}
	else if(playerCardSelect == 5){
		brick = 4;
	}
	
	
	for(int i = 0; i < allPorts.size(); i++){
		
		if(allJoints.get(allPorts.get(i).joint1).color.equals(allPlayers.get(player).color)||
				allJoints.get(allPorts.get(i).joint2).color.equals(allPlayers.get(player).color)){
			
			if(playerCardSelect == 1 && allPorts.get(i).type == null){
				sheep = 3;
			}
			else if(playerCardSelect == 2&& allPorts.get(i).type == null){
				wheat = 3;
			}
			else if(playerCardSelect == 3&& allPorts.get(i).type == null){
				wood = 3;
			}
			else if(playerCardSelect == 4&& allPorts.get(i).type == null){
				stone = 3;
			}
			else if(playerCardSelect == 5&& allPorts.get(i).type == null){
				brick = 3;
			}
			
			
			
		}	
		
		
	}
	
	
	
	for(int i = 0; i < allPorts.size(); i++){
		
		if(allJoints.get(allPorts.get(i).joint1).color.equals(allPlayers.get(player).color)||
				allJoints.get(allPorts.get(i).joint2).color.equals(allPlayers.get(player).color)){
			
			if(playerCardSelect == 1 && allPorts.get(i).type == ResourceCardType.SHEEPRC){
				sheep = 2;
			}
			else if(playerCardSelect == 2&& allPorts.get(i).type == ResourceCardType.WHEATRC){
				wheat = 2;
			}
			else if(playerCardSelect == 3&& allPorts.get(i).type == ResourceCardType.FORESTRC){
				wood = 2;
			}
			else if(playerCardSelect == 4&& allPorts.get(i).type == ResourceCardType.STONERC){
				stone = 2;
			}
			else if(playerCardSelect == 5&& allPorts.get(i).type == ResourceCardType.BRICKRC){
				brick = 2;
			}
			
			
			
		}	
		
		
	}
	
	
	if(sheepCount >= sheep&& woodCount >= wood&& wheatCount >= wheat && stoneCount >= stone
			&&brickCount >= brick){
			
				
				if(cardSelect == 1 ){
					return "Trade With Bank";
				}
				else if(cardSelect == 2){
					return "Trade With Bank";
				}
				else if(cardSelect == 3){
					return "Trade With Bank";
				}
				else if(cardSelect == 4){
					return "Trade With Bank";
				}
				else if(cardSelect == 5){
					return "Trade With Bank";
				}
				else{
					return "End Turn";
				}
				
				
				
			
			
			
			}
	
	return "End Turn";
}

public boolean placePath(int index) {
	// TODO Auto-generated method stub
	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}
	
	
	if(turn == 1){
		
		
			//Need to add more conditions and actions
				if(isGoodPathPlacement(index)&&!haveRoad()){
				allPaths.get(index).color = allPlayers.get(player).color;
				return true;//Can return after finding the right thing
				
			}
			
		}
	else if(turn == 2){
		
		
			//Need to add more conditions and actions
				if(isGoodPathPlacement(index)&&!have2Road()&&settlementDoesntHaveRoad(index)){
				allPaths.get(index).color = allPlayers.get(player).color;
				return true;//Can return after finding the right thing
				
			}
			
		
		
	}
	else{
		
			//Need to add more conditions and actions
				if(isGoodPathPlacement(index)&&woodCount >= 1 && brickCount >=1){
				allPaths.get(index).color = allPlayers.get(player).color;
				allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
				allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
				UserInterface.emptySelectedJoints();
				return true;//Can return after finding the right thing
				
			}
	else if(isGoodPathPlacement(index)&&(allPlayers.get(player).isRobot||isTraining)){
		if(brickCount >= 1){//trade for a wood
			
		if(sheepCount >= 4)//trade for a wood
			{
			tradeWithBank(1,3);
			allPaths.get(index).color = allPlayers.get(player).color;
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			return true;
			}
		
		if(stoneCount >= 4)//trade for a wood
		{
			tradeWithBank(4,3);
			allPaths.get(index).color = allPlayers.get(player).color;
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			return true;
			}
		
		if(wheatCount >= 4)//trade for a wood
		{
			tradeWithBank(2,3);
			allPaths.get(index).color = allPlayers.get(player).color;
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			return true;
			}
		
		if(brickCount >= 5)
		{
			tradeWithBank(5,3);
			allPaths.get(index).color = allPlayers.get(player).color;
			allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
			allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
			return true;
			}
	
		}
		else if(woodCount >= 1){
			if(sheepCount >= 4)//trade for a brick
			{
				tradeWithBank(1,5);
				allPaths.get(index).color = allPlayers.get(player).color;
				allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
				allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
				return true;
				}
			
			if(stoneCount >= 4)//trade for a brick
			{
				tradeWithBank(4,5);
				allPaths.get(index).color = allPlayers.get(player).color;
				allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
				allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
				return true;
				}
			
			if(wheatCount >= 4)//trade for a brick
			{
				tradeWithBank(2,5);
				allPaths.get(index).color = allPlayers.get(player).color;
				allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
				allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
				return true;
				}
			
			if(woodCount >= 5)//trade for a brick
			{
				tradeWithBank(3,5);
				allPaths.get(index).color = allPlayers.get(player).color;
				allPlayers.get(player).cards.remove(ResourceCardType.FORESTRC);
				allPlayers.get(player).cards.remove(ResourceCardType.BRICKRC);
				return true;
				}
			
		}
				
					
					
				}
			
		
	}
	
	return false;// if you weren't supposed to place a path
	
}

public void trade4Cards() {
	// TODO Auto-generated method stub
	
	int sheep = 0;
	int wheat = 0;
	int wood = 0;
	int stone = 0;
	int brick = 0;
	

	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			wood ++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brick ++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheep ++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheat ++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stone ++;
		
	}
	
	
	if(sheep > 3){
		if(wheat == 0)
			tradeWithBank(1,2);
		if(wood == 0)
			tradeWithBank(1,3);
		if(stone == 0)
			tradeWithBank(1,4);
		if(brick == 0)
			tradeWithBank(1,5);
			}
	
	if(wheat > 3){
		if(sheep == 0)
			tradeWithBank(2,1);
		if(wood == 0)
			tradeWithBank(2,3);
		if(stone == 0)
			tradeWithBank(2,4);
		if(brick == 0)
			tradeWithBank(2,5);

			}
	
	
	if(wood > 3){
		if(wheat == 0)
			tradeWithBank(3,2);
		if(sheep == 0)
			tradeWithBank(3,1);
		if(stone == 0)
			tradeWithBank(3,4);
		if(brick == 0)
			tradeWithBank(3,5);

			}
	
	if(stone > 3){
		if(wheat == 0)
			tradeWithBank(4,2);
		if(wood == 0)
			tradeWithBank(4,3);
		if(sheep == 0)
			tradeWithBank(4,1);
		if(brick == 0)
			tradeWithBank(4,5);
	
			}
	
	if(brick > 3){
		if(wheat == 0)
			tradeWithBank(5,2);
		if(wood == 0)
			tradeWithBank(5,3);
		if(stone == 0)
			tradeWithBank(5,4);
		if(sheep == 0)
			tradeWithBank(5,1);
		
			}
	
	
	
	if(sheep > 3){
		if(wheat == 1)
			tradeWithBank(1,2);
		if(wood == 1)
			tradeWithBank(1,3);
		if(stone == 1)
			tradeWithBank(1,4);
		if(brick == 1)
			tradeWithBank(1,5);
			}
	
	if(wheat > 3){
		if(sheep == 1)
			tradeWithBank(2,1);
		if(wood == 1)
			tradeWithBank(2,3);
		if(stone == 1)
			tradeWithBank(2,4);
		if(brick == 1)
			tradeWithBank(2,5);

			}
	
	
	if(wood > 3){
		if(wheat == 1)
			tradeWithBank(3,2);
		if(sheep == 1)
			tradeWithBank(3,1);
		if(stone == 1)
			tradeWithBank(3,4);
		if(brick == 1)
			tradeWithBank(3,5);

			}
	
	if(stone > 3){
		if(wheat == 1)
			tradeWithBank(4,2);
		if(wood == 1)
			tradeWithBank(4,3);
		if(sheep == 1)
			tradeWithBank(4,1);
		if(brick == 1)
			tradeWithBank(4,5);
	
			}
	
	if(brick > 3){
		if(wheat == 1)
			tradeWithBank(5,2);
		if(wood == 1)
			tradeWithBank(5,3);
		if(stone == 1)
			tradeWithBank(5,4);
		if(sheep == 1)
			tradeWithBank(5,1);
		
			}
	
}

public boolean placeCity(int index) {
	// TODO Auto-generated method stub
	
	int sheepCount = 0;
	int wheatCount = 0;
	int woodCount = 0;
	int stoneCount = 0;
	int brickCount = 0;
	
	
	for(int i = 0; i < allPlayers.get(player).cards.size(); i++){
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.SHEEPRC)
			sheepCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.BRICKRC)
			brickCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.FORESTRC)
			woodCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.STONERC)
			stoneCount++;
		
		if(allPlayers.get(player).cards.get(i) == ResourceCardType.WHEATRC)
			wheatCount++;
		
	}	
	
	
	
	
	if(isGoodCityPlacement(index)&& wheatCount >= 2 && stoneCount >= 3){
		allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
		allPlayers.get(player).cards.remove(ResourceCardType.WHEATRC);
		allPlayers.get(player).cards.remove(ResourceCardType.STONERC);
		allPlayers.get(player).cards.remove(ResourceCardType.STONERC);
		allPlayers.get(player).cards.remove(ResourceCardType.STONERC);
		allJoints.get(index).city = true;
		
		
		return true;
	}
	else if(isGoodCityPlacement(index)&&(allPlayers.get(player).isRobot||isTraining)){

		
		
	}
	
	return false;
	
}



public void saveGameBoard() {
	// TODO Auto-generated method stub
	reset();
	
	 try (
		      OutputStream file = new FileOutputStream("Extra/singlePlayer");
		      OutputStream buffer = new BufferedOutputStream(file);
		      ObjectOutput output = new ObjectOutputStream(buffer);
		    ){
		 
			for(int i = 0; i < allJoints.size(); i++){
				output.writeObject(allJoints.get(i));
			}
			for(int i = 0; i < allHexes.size(); i++){
				output.writeObject(allHexes.get(i));
			}
			for(int i = 0; i < allPaths.size(); i++){
				output.writeObject(allPaths.get(i));
			}
			//read in first neural net
			for(int i = 0; i < robot0.city.size(); i ++){
				output.writeObject(robot0.city.get(i));
			}
			for(int i = 0; i < robot0.path.size(); i ++){
				output.writeObject(robot0.path.get(i));
			}
			for(int i = 0; i < robot0.settlement.size(); i ++){
				output.writeObject(robot0.settlement.get(i));
			}
			for(int i = 0; i < robot0.trade.size(); i ++){
				output.writeObject(robot0.trade.get(i));
			}
			for(int i = 0; i < robot0.robber.size(); i ++){
				output.writeObject(robot0.robber.get(i));
			}
			output.writeObject(robot0.buyDevCard);
			output.writeObject(robot0.playKnight);
			output.writeObject(robot0.playMonopoly);
			output.writeObject(robot0.playPlenty);
			output.writeObject(robot0.playRoads);
			
			
			
			for(int i = 0; i < robot1.city.size(); i ++){
				output.writeObject(robot1.city.get(i));
			}
			for(int i = 0; i < robot1.path.size(); i ++){
				output.writeObject(robot1.path.get(i));
			}
			for(int i = 0; i < robot1.settlement.size(); i ++){
				output.writeObject(robot1.settlement.get(i));
			}
			for(int i = 0; i < robot1.trade.size(); i ++){
				output.writeObject(robot1.trade.get(i));
			}
			for(int i = 0; i < robot1.robber.size(); i ++){
				output.writeObject(robot1.robber.get(i));
			}
			output.writeObject(robot1.buyDevCard);
			output.writeObject(robot1.playKnight);
			output.writeObject(robot1.playMonopoly);
			output.writeObject(robot1.playPlenty);
			output.writeObject(robot1.playRoads);
			
			
			for(int i = 0; i < robot2.city.size(); i ++){
				output.writeObject(robot2.city.get(i));
			}
			for(int i = 0; i < robot2.path.size(); i ++){
				output.writeObject(robot2.path.get(i));
			}
			for(int i = 0; i < robot2.settlement.size(); i ++){
				output.writeObject(robot2.settlement.get(i));
			}
			for(int i = 0; i < robot2.trade.size(); i ++){
				output.writeObject(robot2.trade.get(i));
			}
			for(int i = 0; i < robot2.robber.size(); i ++){
				output.writeObject(robot2.robber.get(i));
			}
			output.writeObject(robot2.buyDevCard);
			output.writeObject(robot2.playKnight);
			output.writeObject(robot2.playMonopoly);
			output.writeObject(robot2.playPlenty);
			output.writeObject(robot2.playRoads);
		
		    }  
		    catch(IOException ex){
		      System.exit(1);
		    }


	
	
	
}



public void loadGame() throws ClassNotFoundException {
	// TODO Auto-generated method stub
	
	 try (
		     // InputStream file = new FileInputStream("Extra/singlePlayer");
			 
			  InputStream file = getClass().getResourceAsStream("Extra/singlePlayer");
		      InputStream buffer = new BufferedInputStream(file);
		      ObjectInput input = new ObjectInputStream(buffer);
		    ){
		 
			for(int i = 0; i < allJoints.size(); i++){
				allJoints.set(i, (Joint)input.readObject());
			}
			for(int i = 0; i < allHexes.size(); i++){
				allHexes.set(i, (Hex)input.readObject());
			}
			for(int i = 0; i < allPaths.size(); i++){
				allPaths.set(i, (Path)input.readObject());
			}
			//read in first neural net
			robot0 = new NeuralNet(this, false);
			
			for(int i = 0; i < robot0.city.size(); i ++){
				robot0.city.set(i, (Perceptron)input.readObject());
			}
			for(int i = 0; i < robot0.path.size(); i ++){
				robot0.path.set(i, (Perceptron)input.readObject());
			}
			for(int i = 0; i < robot0.settlement.size(); i ++){
				robot0.settlement.set(i, (Perceptron)input.readObject());
			}
			for(int i = 0; i < robot0.trade.size(); i ++){
				robot0.trade.set(i, (Perceptron)input.readObject());
			}
			for(int i = 0; i < robot0.robber.size(); i ++){
				robot0.robber.set(i, (Perceptron)input.readObject());
			}
			
			robot0.buyDevCard = (Perceptron) input.readObject();
			robot0.playKnight = (Perceptron) input.readObject();
			robot0.playMonopoly = (Perceptron) input.readObject();
			robot0.playPlenty = (Perceptron) input.readObject();
			robot0.playRoads = (Perceptron) input.readObject();
			
			robot1 = new NeuralNet(this, false);
			
			for(int i = 0; i < robot1.city.size(); i ++){
				robot1.city.set(i, (Perceptron)input.readObject());
			}
			for(int i = 0; i < robot1.path.size(); i ++){
				robot1.path.set(i, (Perceptron)input.readObject());
			}
			for(int i = 0; i < robot1.settlement.size(); i ++){
				robot1.settlement.set(i, (Perceptron)input.readObject());
			}
			for(int i = 0; i < robot1.trade.size(); i ++){
				robot1.trade.set(i, (Perceptron)input.readObject());
			}
			for(int i = 0; i < robot1.robber.size(); i ++){
				robot1.robber.set(i, (Perceptron)input.readObject());
			}
			
			robot1.buyDevCard = (Perceptron) input.readObject();
			robot1.playKnight = (Perceptron) input.readObject();
			robot1.playMonopoly = (Perceptron) input.readObject();
			robot1.playPlenty = (Perceptron) input.readObject();
			robot1.playRoads = (Perceptron) input.readObject();
			
			
			robot2 = new NeuralNet(this, false);
			
			for(int i = 0; i < robot2.city.size(); i ++){
				robot2.city.set(i, (Perceptron)input.readObject());
			}
			for(int i = 0; i < robot2.path.size(); i ++){
				robot2.path.set(i, (Perceptron)input.readObject());
			}
			for(int i = 0; i < robot2.settlement.size(); i ++){
				robot2.settlement.set(i, (Perceptron)input.readObject());
			}
			for(int i = 0; i < robot2.trade.size(); i ++){
				robot2.trade.set(i, (Perceptron)input.readObject());
			}
			for(int i = 0; i < robot2.robber.size(); i ++){
				robot2.robber.set(i, (Perceptron)input.readObject());
			}
			
			robot2.buyDevCard = (Perceptron) input.readObject();
			robot2.playKnight = (Perceptron) input.readObject();
			robot2.playMonopoly = (Perceptron) input.readObject();
			robot2.playPlenty = (Perceptron) input.readObject();
			robot2.playRoads = (Perceptron) input.readObject();
			

		
		    }  
		    catch(IOException ex){
		      System.exit(1);
		    }


	reset();
	
	
}



public int getAmountOfRoads() {
	// TODO Auto-generated method stub
	
	Color user = allPlayers.get(player).color;
	
	int roads = 0;
	
	for(int i = 0; i < allPaths.size(); i++){
		if(allPaths.get(i).color.equals(user))
			roads ++;
	}
	
	
	return roads;
}


	
	
}





