import java.awt.Color;
import java.awt.Frame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;




public class Host {
	
	
	public static void main(String[] args){
		try {
			Host h = new Host();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	static Frame f = new Frame();
    static int portNumber = 5000;
    static HashSet<ObjectOutputStream> players = new HashSet<ObjectOutputStream>();
    static LocalGameBoard game;

 
    public Host() throws Exception {
       String alert = "";
       ServerSocket server = null;
       
       System.out.println("Generating game board");
       
       game = new LocalGameBoard();// create a new GameBoard object);
       Player firstPlayer = new Player(Color.red, "host");
       game.allPlayers.add(firstPlayer);
       
       System.out.println("Initializing interface");
       //UserInterface ui = new UserInterface(game);
       //ui.runLocalInterface();
      

        try {
            server = new ServerSocket(portNumber,
                    1000,//amount of stuff sent may need to change
                    InetAddress.getLocalHost());
            
           
           String s = InetAddress.getLocalHost().toString();
          // String a = InetAddress.getByName("76.5.75.248 ").toString();//possibility?
          // System.out.println(a);
           System.out.println(s);
           alert = "Share this with other players: ";
           for(int i = 0; i < s.length(); i++){
        	   char c = s.charAt(i);
        	   
        	   if(c == '0'||c == '1'||c == '2'||c == '3'||c == '4'||c == '5'||c == '6'||c == '7'||c == '8'||c == '9'
        			   ||c == '.'){
        		   alert = alert + c;
        		   }
           }
           
           alert = alert +"\n";
           alert = alert + "Port Number: "+portNumber;
           
          System.out.println(alert);
          
           
           f.dispose();
               
        } catch (IOException e) {
        	portNumber = portNumber + 1;//increment the portNumber
        	System.exit(1);
        	new Host();//If the port is full increment and try another
        }
        try {
            while (true) {
                new Handler(server.accept()).start();
            }
        } finally {
            server.close();
        }
    }


    private static class Handler extends Thread {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

   
        public Handler(Socket socket) {
            this.socket = socket;
        }


        public void run() {
            try {
            	
            	System.out.println("inside Host run");
            	
            	out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());


                players.add(out);
                System.out.println("Players size "+players.size());

 
                while (true) {
                	System.out.println("inside while loop");
                    Object obj = null;
					try {
						System.out.println("inside try");
						System.out.println(game);
						out.writeObject(game.allPaths);
						out.writeObject(game.allHexes);
						out.writeObject(game.allJoints);
						obj = in.readObject();
						System.out.println(obj);
					} catch (ClassNotFoundException e) {
						System.out.println(e);
					}
                    if (obj == null) {
                        return;
                    }
                    for (ObjectOutputStream oos : players) {
                    	System.out.println("game sent to player");
                        oos.writeObject(game);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    players.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}