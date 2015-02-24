package gamegenerator.test;

import gamegenerator.core.GameGenerator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameGenMain {
	

	public static void main(String[] args) {
//		evolveGameFromExisting("../gvgai/examples/gridphysics/", "boulderdash");
		
		
		 String evolvedFolder = "game_generator/evolved_games/";

		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd;MM,HH;mm");
        String outputFolderPath = evolvedFolder + dateFormat.format(new Date()) + "/";
        File outputFolder = new File(outputFolderPath);
        outputFolder.mkdirs();
        
		for (int i = 0; i < 50; i++) {
			GameGenerator.evolveGameFromScratch("coolgame" + i, outputFolderPath);
		}
		

	}
}
