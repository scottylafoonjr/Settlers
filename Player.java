

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class Player {
	
	boolean isRobot = false;
	Color color;
	String username;
    ArrayList<ResourceCardType> cards = new ArrayList<ResourceCardType>();
    HashMap<DevelopmentCardType, Integer> devCards = new HashMap<DevelopmentCardType, Integer>();
    
    

 public Player(Color color, String username){
	 this.color = color;
	 this.username = username;
	 
	 
	 devCards.put(DevelopmentCardType.KNIGHT, 0);
	 devCards.put(DevelopmentCardType.MONOPOLY, 0);
	 devCards.put(DevelopmentCardType.PLAYED_KNIGHT, 0);
	 devCards.put(DevelopmentCardType.ROADS, 0);
	 devCards.put(DevelopmentCardType.VICTORY_POINT, 0);
	 devCards.put(DevelopmentCardType.YEAR_OF_PLENTY, 0);
	 
	 
 }
 
 public Player(Color color, String username, boolean isRobot){
	 this.color = color;
	 this.username = username;
	 this.isRobot = isRobot;
	 
	 devCards.put(DevelopmentCardType.KNIGHT, 0);
	 devCards.put(DevelopmentCardType.MONOPOLY, 0);
	 devCards.put(DevelopmentCardType.PLAYED_KNIGHT, 0);
	 devCards.put(DevelopmentCardType.ROADS, 0);
	 devCards.put(DevelopmentCardType.VICTORY_POINT, 0);
	 devCards.put(DevelopmentCardType.YEAR_OF_PLENTY, 0);
	 
	 
 }
    
    
    
    
    
    
    
    
    
    

/*
    @SuppressWarnings("resource")
	private void run(String serverAddress, String portNumber) throws IOException {

     try{
    	 

        System.out.println("inside run");
        Socket socket = new Socket(serverAddress, Integer.parseInt(portNumber));
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
       
       // out.writeObject((Object)"hello this is the player speaking");
        
     }
     catch(java.net.SocketException e){
    	JOptionPane.showMessageDialog(frame, "Couldn't connect to that IP adress and Port number");
    	System.exit(0);
     }



              System.out.println("before while loop");
             
             while(true){
            	 
            	 System.out.println("after while loop");
				try {
					System.out.println("inside try");
					game.allPaths =  (ArrayList<Path>) in.readObject();
					game.allHexes = (ArrayList<Hex>) in.readObject();
					game.allJoints = (ArrayList<Joint>) in.readObject();
					UserInterface.init(game);
					System.out.println("after read object");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	  
         
            	  
            }
              
              
    }
    
    


 
    public static void start() throws Exception {
    	
        String serverAddress = JOptionPane.showInputDialog(
        		frame,
                "Enter IP Address of the Server:",
                "Settlers of Farmville",
                JOptionPane.QUESTION_MESSAGE);
        
        String portNumber = JOptionPane.showInputDialog(
        		frame,
                "Enter Port Number:",
                "Settlers of Farmville!",
                JOptionPane.QUESTION_MESSAGE);
        
        String username = JOptionPane.showInputDialog(
        		frame,
                "Choose a unique username",
                "Settlers of Farmville!",
                JOptionPane.PLAIN_MESSAGE);
        
        System.out.println(username);
        System.out.println(serverAddress);
        System.out.println(Integer.parseInt(portNumber));
    	
    	
       Player player = new Player();
        player.run(serverAddress, portNumber);
    }*/
}