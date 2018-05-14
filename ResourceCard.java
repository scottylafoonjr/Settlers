import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ResourceCard {

	static BufferedImage brickResourceCard;
	static BufferedImage forestResourceCard;
	static BufferedImage sheepResourceCard;
	static BufferedImage stoneResourceCard;
	static BufferedImage wheatResourceCard;

public static void loadImages(){	
	
	try {
		brickResourceCard = ImageIO.read(new File("Extra/Brick.png"));
		forestResourceCard = ImageIO.read(new File("Extra/Lumber.png"));
		sheepResourceCard = ImageIO.read(new File("Extra/Sheep.png"));
		stoneResourceCard = ImageIO.read(new File("Extra/Stone.png"));
		wheatResourceCard = ImageIO.read(new File("Extra/Grain.png"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
