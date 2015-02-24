package gamechanger.test;

import gamechanger.core.GameChanger;
import gamechanger.writer.Writer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import levelgenerator.core.LevelGenerator;
import levelgenerator.map.LevelMap;




public class Test {

	final static String generatedGamesFolder = "rnd_gen_games/";

	
	static String[] games = new String[]{"aliens", "boulderdash", "butterflies", "chase", "frogs",
            "missilecommand", "portals", "sokoban", "survivezombies", "zelda",
            "camelRace", "digdug", "firestorms", "infection", "firecaster",
            "overload", "pacman", "seaquest", "whackamole", "eggomania"};
	
	
    public static void main(String[] args) {
		

  
        
//        games = new String[]{"aliens", "boulderdash", "butterflies", "chase", "frogs",
//                "missilecommand", "portals", "sokoban", "survivezombies", "zelda",
//                "camelRace", "digdug", "firestorms", "infection", "firecaster",
//                "overload", "pacman", "seaquest", "whackamole", "eggomania",
//                "bait", "boloadventures", "bombuzal", "brainman", "chipschallenge",
//                "modality", "painter", "realsokoban", "thecitadel", "zenpuzzle"};
		
//        games = new String[]{"seaquest"};
        
        games = new String[]{"aliens", "astroblast", "boulderdash", "centipede", "crackpots",
        		"digdug", "eggomania", "frogs", "missilecommand", "pacman", "seaquest", 
        		"solarfox", "zelda"};
        
        //1. Make a lot mutated games
//        int mutationAmount = 10;
//		for (int i = 0; i < games.length; i++) {
//			String gamePath = "../gvgai/examples/gridphysics/" + games[i] + ".txt";
//			
//			for (int j = 0; j < mutationAmount; j++) {
//				String new_desc = GameChanger.changeGameByPath(gamePath, false, true, false);
//				String path = "mutatedgames/" + games[i] + "_mutation_" + j + ".txt";
//				
//				PrintWriter writer = null;
//				try { writer = new PrintWriter(path, "UTF-8"); } catch (IOException e) { e.printStackTrace();}
//									
//				writer.flush();
//				writer.print(new_desc);
//				writer.close();
//			}
//		}

		
        //2. Makes a lot of completely new games
//		int newGameAmount = 400, levelsPerGame = 1;
//		for (int i = 0; i < newGameAmount; i++) {
//			String new_desc = GameChanger.makeGame();
//			String[] level_descs = new String[levelsPerGame];
//			for (int j = 0; j < level_descs.length; j++) {
//				level_descs[j] = GameChanger.makeLevel(new_desc);
//			}
//			String gamePath = generatedGamesFolder + "gen_game_" + i + ".txt";
//			String[] levelPaths = new String[levelsPerGame];
//			for (int j = 0; j < levelPaths.length; j++) {
//				levelPaths[j] = generatedGamesFolder + "gen_game_" + i + "_lvl" + j + ".txt";
//			}
//			
//			PrintWriter writer = null;
//			try {
//				writer = new PrintWriter(gamePath, "UTF-8");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			writer.flush();
//			writer.print(new_desc);
//			writer.close();
//			
//			writer = null;
//			for (int j = 0; j < levelsPerGame; j++) {
//				try {
//					writer = new PrintWriter(levelPaths[j], "UTF-8");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				writer.flush();
//				writer.print(level_descs[j]);
//				writer.close();
//			}
//
//		}
	
        
        //3. Generate levels for designed games
//        int amountGenLvls = 5;
//		for (int i = 0; i < games.length; i++) {
//			String gamePath = "../gvgai/examples/gridphysics/" + games[i] + ".txt";
//			
//			String gameDesc = Parser.getGameDescFromPath(gamePath);
//			
//			
//			for (int j = 0; j < amountGenLvls; j++) {
//				String level_desc = GameChanger.makeLevel(gameDesc);
//				
//				String path = "levelgen/" + games[i] + "_genlvl" + j + ".txt";
//				
//				PrintWriter writer = null;
//				try {
//					writer = new PrintWriter(path, "UTF-8");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				writer.flush();
//				writer.print(level_desc);
//				writer.close();
//			}
//		}
        
            
        //4. Make a puzzle game, and evolve a level
//        boolean gameIsGood = false;
//        int cnt = 0;
//        while(!gameIsGood){
//        	
//        	//Make game description - assume that the resulting game can have puzzle game play features
//            String new_desc = GameChanger.makePuzzleGame();
//            System.out.println("Made new puzzle game description:\n" + new_desc);
//            
//            Writer.storeString(new_desc, generatedPuzzleGamesFolder, "genpuzzler");
//            
//            LevelGenerator lg = new LevelGenerator("genpuzzler", generatedPuzzleGamesFolder);
//            String genLvlDesc = lg.generateLevel(8, 8, new LevelMap(null));
//            
//            if (genLvlDesc.length() > 0){
//            	Writer.storeString(new_desc, evolvedPuzzleGamesFolder, "genpuzzler" + cnt);
//            	Writer.storeString(genLvlDesc, evolvedPuzzleGamesFolder, "genpuzzler" + cnt + "_lvl0");
//            	cnt++;
//            }
//        }

    }
        
        

 
}
