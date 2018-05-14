import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class NeuralNet implements Serializable{
	
	double update = .5;
	ArrayList<Perceptron> settlement = new ArrayList<Perceptron>();
	ArrayList<Perceptron> city = new ArrayList<Perceptron>();
	ArrayList<Perceptron> path = new ArrayList<Perceptron>();
	ArrayList<Perceptron> trade = new ArrayList<Perceptron>();
	ArrayList<Perceptron> robber = new ArrayList<Perceptron>();
	Perceptron buyDevCard = new Perceptron();
	Perceptron playKnight = new Perceptron();
	Perceptron playPlenty = new Perceptron();
	Perceptron playMonopoly = new Perceptron();
	Perceptron playRoads = new Perceptron();
	Perceptron doNothing = new Perceptron();
	
	boolean isLearning = false;
	NeuralNet loseNeuralNet;
	
	
	public String toString(){
		String s ="";
		
		
		for(int i = 0; i < settlement.size(); i++){
			s += "settlement "+i+" value: "+settlement.get(i).isGood+"\n";
		}
		
		
		return s;
	}
	
	
	public NeuralNet(NeuralNet copy){//copy constructor
	
		for(int i = 0; i < copy.settlement.size(); i++){
			this.settlement.add(new Perceptron(copy.settlement.get(i)));
		}
		
		for(int i = 0; i < copy.city.size(); i++){
			this.city.add(new Perceptron(copy.city.get(i)));
		}
		
		for(int i = 0; i < copy.path.size(); i++){
			this.path.add(new Perceptron(copy.path.get(i)));
		}
		
		for(int i = 0; i < copy.trade.size(); i++){
			this.trade.add(new Perceptron(copy.trade.get(i)));
		}
		
		for(int i = 0; i < copy.robber.size(); i++){
			this.robber.add(new Perceptron(copy.robber.get(i)));
		}
		
		this.buyDevCard = (new Perceptron(copy.buyDevCard));
		this.playKnight = (new Perceptron(copy.playKnight));
		this.playPlenty = (new Perceptron(copy.playPlenty));
		this.playMonopoly = (new Perceptron(copy.playMonopoly));
		this.playRoads = (new Perceptron(copy.playRoads));
		this.doNothing = (new Perceptron(copy.doNothing));
		

		
		
		
	}
	
	
	
	public NeuralNet(LocalGameBoard temp, boolean isLearning){
		
		this.isLearning = isLearning;
		
		for(int i = 0; i < temp.allJoints.size(); i++){//Initializing each settlement perceptron
			settlement.add(new Perceptron(temp));
		}
		
		for(int i = 0; i < temp.allHexes.size(); i++){
			robber.add(new Perceptron(temp));
		}
		
		
		for(int i = 0; i < temp.allJoints.size(); i++){ //cities
			city.add(new Perceptron(temp));
		}
		
		
		
		for(int i = 0; i < temp.allPaths.size(); i++){ //paths
			path.add(new Perceptron(temp));
		}
		
		
		for(int x = 0; x < 6; x++){//trade 1-5 ignore 0
		for(int i = 0; i < 6; i++){ //trade 1-5 ignore 0
			trade.add(new Perceptron(temp));
		}
		}
		
		
		buyDevCard = new Perceptron(temp);
		playKnight = new Perceptron(temp);
		playPlenty = new Perceptron(temp);
		playMonopoly = new Perceptron(temp);
		playRoads = new Perceptron(temp);
		doNothing = new Perceptron(temp);
		
	
		loseNeuralNet = this;
	}
	
	
	public boolean executeNextMove(LocalGameBoard game){
		
		//ArrayList<String> allActions = new ArrayList<String>();
		//allActions = game.allPossibleActions();
		
	
		
		//String input = allActions.get(random);
		String input = getBestMove(game);
/*		
	if(isLearning){
		loseNeuralNet.updateLoseNeuralNet(input, game);
		}
	*/	
		String[] split = input.split(" ");
		if(split[0].equals("Settlement")){
			game.placeSettlement(Integer.parseInt(split[1]));
			return true;
		}
		else if(split[0].equals("Path")){
			game.placePath(Integer.parseInt(split[1]));
			return true;
		}
		else if(split[0].equals("City")){
			game.placeCity(Integer.parseInt(split[1]));
			return true;
		}
		else if(split[0].equals("Trade")){			
			game.tradeWithBank(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
			return true;
		}
		else if(split[0].equals("Buy")){			
			game.buyDevCard();
			return true;
		}
		else if(split[0].equals("Knight")){			
			game.playKnight();
			return true;
		}
		else if(split[0].equals("Plenty")){			
			if(game.turn < 20)
			game.playPlenty(0, 4);
			else
			game.playPlenty(1, 2);
			
			return true;
		}
		else if(split[0].equals("Monopoly")){	
			Random ran = new Random();
			game.playMonopoly(ran.nextInt(5));
			return true;
		}
		else if(split[0].equals("Roads")){			
			game.playRoads();
			return true;
		}
		else if(split[0].equals("Robber")){
			game.placeRobber(Integer.parseInt(split[1]));
			return true;
		}
		
		
		return false;
	}
	
	
	public void updateWinNeuralNet(ArrayList<String> allMoves, LocalGameBoard game) {
		// TODO Auto-generated method stub
		
		for(int i = 0; i < allMoves.size(); i++){
	
		String[] split = allMoves.get(i).split(" ");
		if(split[0].equals("Settlement")){
			settlement.get(Integer.parseInt(split[1])).setWinValue(game);
		}
		else if(split[0].equals("Path")){
			path.get(Integer.parseInt(split[1])).setWinValue(game);
		}
		else if(split[0].equals("Robber")){
			robber.get(Integer.parseInt(split[1])).setWinValue(game);
		}
		else if(split[0].equals("City")){
			city.get(Integer.parseInt(split[1])).setWinValue(game);
		}
		else if(split[0].equals("Trade")){
			trade.get(((Integer.parseInt(split[1]))*5)+Integer.parseInt(split[2])).setWinValue(game);
		}
		else if(split[0].equals("Buy")){			
			buyDevCard.setWinValue(game);
		}
		else if(split[0].equals("Knight")){			
			playKnight.setWinValue(game);
		}
		else if(split[0].equals("Plenty")){			
			playPlenty.setWinValue(game);
		}
		else if(split[0].equals("Monopoly")){			
			playMonopoly.setWinValue(game);
		}
		else if(split[0].equals("Roads")){			
			playRoads.setWinValue(game);
		}
		else if(split[0].equals("Pass")){			
			doNothing.setWinValue(game);
		}
		
		
		
	}
	}
	
	private void updateMissedWinNeuralNet(String input, LocalGameBoard game) {
		// TODO Auto-generated method stub
		
	
		String[] split = input.split(" ");
		if(split[0].equals("Settlement")){
			settlement.get(Integer.parseInt(split[1])).setLoseValue(game);
		}
		else if(split[0].equals("Path")){
			path.get(Integer.parseInt(split[1])).setLoseValue(game);
		}
		else if(split[0].equals("Robber")){
			robber.get(Integer.parseInt(split[1])).setLoseValue(game);

		}
		else if(split[0].equals("City")){
			city.get(Integer.parseInt(split[1])).setLoseValue(game);
		}
		else if(split[0].equals("Trade")){
			trade.get(((Integer.parseInt(split[1]))*5)+Integer.parseInt(split[2])).setLoseValue(game);
		}
		else if(split[0].equals("Buy")){			
			buyDevCard.setLoseValue(game);
		}
		else if(split[0].equals("Knight")){			
			playKnight.setLoseValue(game);
		
		}
		else if(split[0].equals("Plenty")){			
			playPlenty.setLoseValue(game);

		}
		else if(split[0].equals("Monopoly")){			
			playMonopoly.setLoseValue(game);

		}
		else if(split[0].equals("Roads")){			
			playRoads.setLoseValue(game);

		}
		else if(split[0].equals("Pass")){			
			doNothing.setLoseValue(game);
		}
	}

	
	
	private void updateMissedLoseNeuralNet(String input, LocalGameBoard game) {
		// TODO Auto-generated method stub
		
	
		String[] split = input.split(" ");
		if(split[0].equals("Settlement")){
			
			settlement.get(Integer.parseInt(split[1])).setWinValue(game);


		}
		else if(split[0].equals("Path")){
			path.get(Integer.parseInt(split[1])).setWinValue(game);


		}
		else if(split[0].equals("Robber")){
			robber.get(Integer.parseInt(split[1])).setWinValue(game);


		}
		else if(split[0].equals("City")){
			city.get(Integer.parseInt(split[1])).setWinValue(game);


		}
		else if(split[0].equals("Trade")){
			trade.get(((Integer.parseInt(split[1]))*5)+Integer.parseInt(split[2])).setWinValue(game);
		}
		else if(split[0].equals("Buy")){			
			buyDevCard.setWinValue(game);
		}
		else if(split[0].equals("Knight")){			
			playKnight.setWinValue(game);
		
		}
		else if(split[0].equals("Plenty")){			
			playPlenty.setWinValue(game);

		}
		else if(split[0].equals("Monopoly")){			
			playMonopoly.setWinValue(game);

		}
		else if(split[0].equals("Roads")){			
			playRoads.setWinValue(game);
		}
		else if(split[0].equals("Pass")){			
			doNothing.setWinValue(game);
		}
		
		
		
		
	}
	
	
	
	private void updateLoseNeuralNet(String input, LocalGameBoard game) {
		// TODO Auto-generated method stub
		
	
		String[] split = input.split(" ");
		if(split[0].equals("Settlement")){
			settlement.get(Integer.parseInt(split[1])).setLoseValue(game);
		}
		else if(split[0].equals("Path")){
			path.get(Integer.parseInt(split[1])).setLoseValue(game);
		}
		else if(split[0].equals("Robber")){
			robber.get(Integer.parseInt(split[1])).setLoseValue(game);
		}
		else if(split[0].equals("City")){
			city.get(Integer.parseInt(split[1])).setLoseValue(game);
		}
		else if(split[0].equals("Trade")){
			trade.get(((Integer.parseInt(split[1]))*5)+Integer.parseInt(split[2])).setLoseValue(game);
		}
		else if(split[0].equals("Buy")){			
			buyDevCard.setLoseValue(game);
		}
		else if(split[0].equals("Knight")){			
			playKnight.setLoseValue(game);
		
		}
		else if(split[0].equals("Plenty")){			
			playPlenty.setLoseValue(game);

		}
		else if(split[0].equals("Monopoly")){			
			playMonopoly.setLoseValue(game);

		}
		else if(split[0].equals("Roads")){			
			playRoads.setLoseValue(game);

		}
		else if(split[0].equals("Pass")){			
			doNothing.setLoseValue(game);
		}
	}


	public String getBestMove(LocalGameBoard game){
		
		String response = "nothing";
		double currentHigh = -999999999;
		ArrayList<String> allActions = new ArrayList<String>();
		int currentIndex = -1;
		allActions = game.allPossibleActions();
		boolean nothingElse = true;
		
		for(int i = 0; i < allActions.size(); i ++){	
			String input = allActions.get(i);
			String[] split = input.split(" ");
			if(split[0].equals("Settlement")){
				
				double result = settlement.get(Integer.parseInt(split[1])).getValue(game);
				if(result > currentHigh){
					nothingElse = false;
					currentHigh = result;
					currentIndex = i;
					response = "Settlement "+Integer.parseInt(split[1]);
				}

			}
			else if(split[0].equals("Path")){
				double result = path.get(Integer.parseInt(split[1])).getValue(game);
				if(result > currentHigh){
					nothingElse = false;
					currentHigh = result;
					currentIndex = i;
					response = "Path "+Integer.parseInt(split[1]);
				}

			}
			else if(split[0].equals("City")){
				double result = city.get(Integer.parseInt(split[1])).getValue(game);
				if(result > currentHigh){
					nothingElse = false;
					currentHigh = result;
					currentIndex = i;
					response = "City "+Integer.parseInt(split[1]);
				}

			}
			else if(nothingElse&&split[0].equals("Trade")&&(game.getAmountOfRoads()>10||game.player == 2)){
				double result = trade.get(((Integer.parseInt(split[1]))*5)+Integer.parseInt(split[2])).getValue(game);
				if(result > currentHigh){
					currentHigh = result;
					currentIndex = i;
					response = "Trade "+Integer.parseInt(split[1])+" "+Integer.parseInt(split[2]);
				}

			}
			else if(split[0].equals("Buy")&&(game.player != 0)&&(game.turn > 25||game.player != 1)){	//buy as many dev cards as possible		
				double result = buyDevCard.getValue(game);
				if(result > currentHigh){
					nothingElse = false;
					currentHigh = result;
					currentIndex = i;
					response = "Buy";
				}
			}
			else if(split[0].equals("Knight")){	 //play knights asap
				currentIndex = i;
					return "Knight";
			}
			else if(split[0].equals("Plenty")){			
				double result = playPlenty.getValue(game);
				if(result > currentHigh){
					nothingElse = false;
					currentHigh = result;
					currentIndex = i;
					response = "Plenty";
				}
			}
			else if(split[0].equals("Monopoly")){			
				double result = playMonopoly.getValue(game);
				if(result > currentHigh){
					nothingElse = false;
					currentHigh = result;
					currentIndex = i;
					response = "Monopoly";
				}
			}
			else if(split[0].equals("Roads")){			
				double result = playRoads.getValue(game);
				if(result > currentHigh){
					nothingElse = false;
					currentHigh = result;
					currentIndex = i;
					response = "Roads";
				}
			}
			else if(split[0].equals("Robber")){			
				double result = robber.get(Integer.parseInt(split[1])).getValue(game);
				if(result > currentHigh){
					nothingElse = false;
					currentHigh = result;
					currentIndex = i;
					response = "Robber "+Integer.parseInt(split[1]);
				}
			}
			
		}


		
	/*	
		if(currentIndex > -1){
			allActions.remove(currentIndex);
		}
		
		
		
		for(int i = 0; i < allActions.size(); i++){
			loseNeuralNet.updateMissedLoseNeuralNet(allActions.get(i), game);
		}
		*/
		
		//update perceptrons with response

		
		return response;
	}
	
	
	

}
