package levelgenerator.test;

import gamechanger.core.GameChanger;
import gamechanger.writer.Writer;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import levelgenerator.core.GameInfo;
import levelgenerator.core.LevelGenerator;
import levelgenerator.map.LevelMap;

public class Main {

	final static String generatedPuzzleGamesFolder = "rnd_gen_puzzle_games/";
	final static String evolvedPuzzleGamesFolder = "evolPuzzleGames/";
	final static String evolvedDesLevelsFolder = "evolPuzzleGames/des_games_levels/";
    
//    Survived GameInfo 0, won: 1 , timesteps: 76 , score: 0.0 , action count: 76
//wwwwwwww
//wM1Mw 0w
//w0M M1Mw
//w0MwwMMw
//wKwM1 1w
//w1GMAMMw
//wwwwwwww
	
	
//	lg.generateLevel(8, 7, lg.getCharMap(	"wwwwwwww\n"+
//											"w.w....w\n"+
//											"w.w.w*.w\n"+
//											"ww.*A..w\n"+
//											"w...Owww\n"+
//											"wO*...Ow\n"+
//											"wwwwwwww"));

	
	public static void main(String[] args) {
		
		String[] games = new String[]{"bait","brainman","thecitadel","realsokoban","modality"};
		
		
		//1. Generate a level for a gvg_ai game
//		LevelGenerator lg = new LevelGenerator("bait");	
//		lg.groundChar = " ".charAt(0);
//		lg.makeWall = true;
//		lg.generateLevel(8, 7, new LevelMap());
		
		
		//2. Generate a level for a series of gvg_ai games
		for (int g = 0; g < games.length; g++) {
			LevelGenerator lg = new LevelGenerator(games[g]);	
			lg.groundChar = " ".charAt(0);
			lg.makeWall = true;
			if (games[g].equals("realsokoban")) lg.groundChar = ".".charAt(0);
			if (games[g].equals("modality")) lg.makeWall = false;
			
			GameInfo result = null;
			while (result == null){
				result = lg.generateLevel(8, 8, new LevelMap());
			}
		
			String lvlDesc = LevelGenerator.getLevelString(result.levelMap.map);
			
			Writer.storeString(lvlDesc, evolvedDesLevelsFolder, games[g] + "_genlvl0");
			Writer.storeString(result.toString(), evolvedDesLevelsFolder, games[g] + "_genlvl_results");
		}


		
		//3. Generate a level for another games
//		LevelGenerator lg = new LevelGenerator("genpuzzle", generatedPuzzleGamesFolder);	
//		lg.groundChar = " ".charAt(0);
//		lg.makeWall = true;
//		lg.generateLevel(8, 7, new LevelMap());
		
		
		
		//4. Generate a game, then some levels, then test if best level has: actions>1, pushes>0.
		// If yes, the generated level is evolved from, getting better levels
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd;MM,HH;mm");
//		boolean gameIsGood = false;
//        int cnt = 0;
//        String outputFolderPath = evolvedPuzzleGamesFolder + dateFormat.format(new Date()) + "/";
//        File outputFolder = new File(outputFolderPath);
//        outputFolder.mkdirs();
//        while(!gameIsGood){
//        	
//        	//Make game description - assume that the resulting game can have puzzle game play features
//            String new_desc = GameChanger.makePuzzleGame();
//            System.out.println("Made new puzzle game description - evolving levels (time: " + new Timestamp(new Date().getTime()) + ")");
//            
//            Writer.storeString(new_desc, generatedPuzzleGamesFolder, "genpuzzler");
//            
//            LevelGenerator lg = new LevelGenerator("genpuzzler", generatedPuzzleGamesFolder);
//            GameInfo genLvlInfo = lg.generateLevel(8, 8, new LevelMap(null));
//            
//            if (genLvlInfo != null){
//            	Writer.storeString(new_desc, outputFolderPath, "genpuzzler" + cnt);
//            	Writer.storeString(LevelGenerator.getLevelString(genLvlInfo.levelMap.map), outputFolderPath, "genpuzzler" + cnt + "_lvl0");
//            	Writer.storeString(genLvlInfo.results.toString(), outputFolderPath, "genpuzzler" + cnt + "_results");
//            	cnt++;
//            }
//        }
		
	}
}
