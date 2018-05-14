import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DevelopmentCard {
		
		static BufferedImage KnightDevCard;
		static BufferedImage VictoryPointDevCard;
		static BufferedImage RoadDevCard;
		static BufferedImage MonopolyDevCard;
		static BufferedImage YearOfPlentyDevCard;
		static boolean initialized = false;
		boolean isPlayed = false;
		DevelopmentCardType type;
		
public DevelopmentCard(DevelopmentCardType type){
	
	if(!initialized)
		initImages();
	
	this.type = type;
}

public BufferedImage getImage(boolean belongToPlayer){
	
	
	if(type == DevelopmentCardType.KNIGHT &&belongToPlayer)
		return KnightDevCard;
	else if(type == DevelopmentCardType.MONOPOLY &&belongToPlayer)
		return MonopolyDevCard;
	else if(type == DevelopmentCardType.ROADS&&belongToPlayer)
		return RoadDevCard;
	else if(type == DevelopmentCardType.VICTORY_POINT&&belongToPlayer)
		return VictoryPointDevCard;
	else if(type == DevelopmentCardType.YEAR_OF_PLENTY&&belongToPlayer)
		return YearOfPlentyDevCard;
	
	
	
	return null;
}
		
		
public static void initImages(){		
		
		try {
			KnightDevCard = ImageIO.read(new File("Extra/Knight.png"));
			VictoryPointDevCard = ImageIO.read(new File("Extra/University.png"));
			RoadDevCard = ImageIO.read(new File("Extra/Roads.png"));
			MonopolyDevCard = ImageIO.read(new File("Extra/Monopoly.png"));
			YearOfPlentyDevCard = ImageIO.read(new File("Extra/YearOfPlenty.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}

}
