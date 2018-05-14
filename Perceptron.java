import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class Perceptron implements Serializable{
	
	ArrayList<Double> settlementBias = new ArrayList<Double>();
	//ArrayList<Double> cityBias = new ArrayList<Double>();
	ArrayList<Double> pathBias = new ArrayList<Double>();
	double update = .1;
	double isGood = 0;



	
	public Perceptron(){
		isGood = 0;
	}
	
	public Perceptron(Perceptron copy){
		for(int i = 0; i < copy.settlementBias.size(); i++){
			this.settlementBias.add(copy.settlementBias.get(i));
		}
	
		for(int i = 0; i < copy.pathBias.size(); i++){
			this.pathBias.add(copy.pathBias.get(i));
		}
		

		
	}

	public Perceptron(LocalGameBoard temp) {
		
		Random ran = new Random();
	
		
		for(int i = 0; i < temp.allJoints.size(); i++){ //settlements
			settlementBias.add((ran.nextDouble()*5));
		}
		
	
		
	
		
		
		for(int i = 0; i < temp.allPaths.size(); i++){ //paths
			pathBias.add((ran.nextDouble()*2) - 1);
		}
		
	
		
		

		
		
		
		
		
		
		
		
		
		
	}

	public double getValue(LocalGameBoard game) {
		// TODO Auto-generated method stub
		
		
		return isGood;
		
		/*
		double total = 0;
		
		for(int i = 0; i < game.allJoints.size(); i++){ //settlements
			if(game.allJoints.get(i).color.equals(new Color(237,201,175))){
				total += settlementBias.get(i);
			}
			else if(game.allJoints.get(i).color.equals(game.allPlayers.get(game.player).color)){ //if it belongs to the player
				total += settlementBias.get(i);
			}
			else{
				total -= settlementBias.get(i);
			}
		}
		

		
		for(int i = 0; i < game.allPaths.size(); i++){ //paths
			if(game.allPaths.get(i).color.equals(new Color(129,98,78))){
				total += pathBias.get(i);
			}
			else if(game.allPaths.get(i).color.equals(game.allPlayers.get(game.player).color)){ //if it belongs to the player
				total += pathBias.get(i);
			}
			else{
				total -= pathBias.get(i);
			}
		}
		
		


	
		return total;*/
	}
	
	



	public void setLoseValue(LocalGameBoard game){
		
		
		for(int i = 0; i < game.allJoints.size(); i++){ //settlements
			if(game.allJoints.get(i).color.equals(new Color(237,201,175))){
				settlementBias.set(i,settlementBias.get(i)-update);
				
			}
			else if(game.allJoints.get(i).color.equals(game.allPlayers.get(game.player).color)){ //if it belongs to the player
				settlementBias.set(i,settlementBias.get(i)-update);
				
			}
			else{
				settlementBias.set(i,settlementBias.get(i)+update);
				
			}
		}
		

		
		for(int i = 0; i < game.allPaths.size(); i++){ //paths
			if(game.allPaths.get(i).color.equals(new Color(129,98,78))){
				pathBias.set(i,pathBias.get(i)-update);
				
			}
			else if(game.allPaths.get(i).color.equals(game.allPlayers.get(game.player).color)){ //if it belongs to the player
				pathBias.set(i,pathBias.get(i)-update);
				
			}
			else{
				pathBias.set(i,pathBias.get(i)+update);
				
			}
		}
		
		
		
		
	}
	
	
	


	public void setWinValue(LocalGameBoard game) {
		// TODO Auto-generated method stub
		
		isGood += (120*1.0 - game.turn*1.0)/100.0;
		
		
		/*
		for(int i = 0; i < game.allJoints.size(); i++){ //settlements
			if(game.allJoints.get(i).color.equals(new Color(237,201,175))){
				settlementBias.set(i,settlementBias.get(i)+update);
				
			}
			else if(game.allJoints.get(i).color.equals(game.allPlayers.get(game.player).color)){ //if it belongs to the player
				settlementBias.set(i,settlementBias.get(i)+update);
			
			}
			else{
				settlementBias.set(i,settlementBias.get(i)-update);
				
			}
		}
		

		
		for(int i = 0; i < game.allPaths.size(); i++){ //paths
			if(game.allPaths.get(i).color.equals(new Color(129,98,78))){
				pathBias.set(i,pathBias.get(i)+update);
				
			}
			else if(game.allPaths.get(i).color.equals(game.allPlayers.get(game.player).color)){ //if it belongs to the player
				pathBias.set(i,pathBias.get(i)+update);
			
			}
			else{
				pathBias.set(i,pathBias.get(i)-update);
				
			}
		}
		
		
		
		
		*/
		
	}



}
