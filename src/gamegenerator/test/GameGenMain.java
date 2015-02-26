package gamegenerator.test;

import gamegenerator.core.GameGenerator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameGenMain {
	

	public static void main(String[] args) {
		 String evolvedFolder = "game_generator/evolved_games/";
		 String mutEvolvedFolder = "game_generator/mut_evolved_games/";


		
		 //1. From designed evolution
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd;MM,HH;mm");
        String outputFolderPath = mutEvolvedFolder + dateFormat.format(new Date()) + "/";
	    for (int i = 0; i < 50; i++) {
		    GameGenerator.evolveGameFromExisting("../gvgai/examples/gridphysics/", "boulderdash", outputFolderPath, i);
		}
		
		

		//2. From scratch evolution
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd;MM,HH;mm");
//        String outputFolderPath = evolvedFolder + dateFormat.format(new Date()) + "/";
//	    File outputFolder = new File(outputFolderPath);
//	    outputFolder.mkdirs();
//		for (int i = 0; i < 50; i++) {
//			GameGenerator.evolveGameFromScratch("coolgame" + i, outputFolderPath);
//		}
		

	}
}
