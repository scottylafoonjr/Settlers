import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;


public class Joint implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double xLoc;
	public double yLoc;
	Color color = new Color(237,201,175);
	public double size = .03;
	ArrayList<Joint> adjacent = new ArrayList<Joint>();
	boolean city = false;
	
	public void setAdjacent(LocalGameBoard localGameBoard){
		
		int search = -1;
		
		for(int i = 0; i < localGameBoard.allJoints.size(); i++){
			if(localGameBoard.allJoints.get(i).equals(this))
				search = i;
		}
		

		
			for(int i = 0; i < localGameBoard.allPaths.size(); i++){
				if(localGameBoard.allPaths.get(i).joints.contains(search)){
					 for (Integer num : localGameBoard.allPaths.get(i).joints) {
					        if (!localGameBoard.allJoints.get(num).equals(this)) 
					          adjacent.add(localGameBoard.allJoints.get(num));
					      } 
					
				}
			}
		
		
		/*//How to debug the adjacent stuff
		 * 
		 * System.out.println("adjacent "+adjacent.size());
		 * 
		if(search == 0){
			size = .05;
			for(int i = 0; i < adjacent.size(); i++){
				adjacent.get(i).size = .05;
			}
		}
		*/
	}
	
	public Joint(double xLoc, double yLoc ){
		this.xLoc = xLoc;
		this.yLoc = yLoc;
	}
	
	public int getSize(){
		return (int)(size*UserInterface.screenWidth);
	}

}
