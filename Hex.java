import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;


public class Hex implements Serializable{
	
/**
	 * 
	 */
private static final long serialVersionUID = 1L;
double centerX;
double centerY;
boolean wasCreated = true;
int number;
Color color = Color.red;
HexType hexType;
HashSet<Integer> joints = new HashSet<Integer>();



public Hex(){wasCreated = false;}
	
	public Hex(	double centerX, double centerY, LocalGameBoard gameBoard){
		hexType = gameBoard.hexInitArray.remove(0);
		this.centerX = centerX;
		this.centerY = centerY;
		
		for(int i = 0; i < gameBoard.allJoints.size(); i++){//Add all the joints corresponding to this hex
			double dist = Math.sqrt((centerX - gameBoard.allJoints.get(i).xLoc)*(centerX - gameBoard.allJoints.get(i).xLoc)
					+(centerY - gameBoard.allJoints.get(i).yLoc)*(centerY - gameBoard.allJoints.get(i).yLoc));
			
			if(dist < .1){
				joints.add(i);//Adds the arraylist position of the joint(So we don't need to deal with serialization of every object)
			}
			
		
		}
		
	if(hexType != HexType.SAND){
		int min = 2;
		int max = 12;//pretty sure this is non inclusive.. could be wrong
		Random rand = new Random();
		
		number = rand.nextInt(max - min)+min ;
		
		if(number == 7)
			number = 8;//Add to the 8 party woop woop
	
	}
	else{
		number = -1;
	}
		
	
	if(number != -1){
		number = gameBoard.hexNumberInitArray.remove(0);
	}
		
	
	}

	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		if(hexType == HexType.BRICK)
			return UserInterface.brickImage;
		else if(hexType == HexType.LUMBER)
			return UserInterface.forestImage;
		else if(hexType == HexType.SAND)
			return UserInterface.sandImage;
		else if(hexType == HexType.SHEEP)
			return UserInterface.sheepImage;
		else if(hexType == HexType.STONE)
			return UserInterface.stoneImage;
		else if(hexType == HexType.WHEAT)
			return UserInterface.wheatImage;
		else 
			return null;
		
		
	}
	

}
