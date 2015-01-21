package gamechanger.core;

import gamechanger.parsing.Parser;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import levelgenerator.core.LevelGenerator;




public class Test {

    public static void main(String[] args) {
		
        String[] games = new String[]{"aliens", "boulderdash", "butterflies", "chase", "frogs",
                "missilecommand", "portals", "sokoban", "survivezombies", "zelda",
                "camelRace", "digdug", "firestorms", "infection", "firecaster",
                "overload", "pacman", "seaquest", "whackamole", "eggomania"};
        
//        String[] games = new String[]{"aliens", "boulderdash", "butterflies", "chase", "frogs",
//                "missilecommand", "portals", "sokoban", "survivezombies", "zelda",
//                "camelRace", "digdug", "firestorms", "infection", "firecaster",
//                "overload", "pacman", "seaquest", "whackamole", "eggomania",
//                "bait", "boloadventures", "bombuzal", "brainman", "chipschallenge",
//                "modality", "painter", "realsokoban", "thecitadel", "zenpuzzle"};
		
//        games = new String[]{"seaquest"};
        
        
        //1. Make a lot mutated games
//        int mutationAmount = 10;
//		for (int i = 0; i < games.length; i++) {
//			String gamePath = "../gvgai/examples/gridphysics/" + games[i] + ".txt";
//			
//			for (int j = 0; j < mutationAmount; j++) {
//				String new_desc = GameChanger.changeGame(gamePath, false, true, false);
//				String path = "mutatedgames/" + games[i] + "_mutation_" + j + ".txt";
//				
//				PrintWriter writer = null;
//				try {
//					writer = new PrintWriter(path, "UTF-8");
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				writer.flush();
//				writer.print(new_desc);
//				writer.close();
//			}
//		}

		
        //2. Makes a lot of completely new games
		int newGameAmount = 5000;
		for (int i = 0; i < newGameAmount; i++) {
			String new_desc = GameChanger.makeGame();
			String level_desc = GameChanger.makeLevel(new_desc);
			String gamePath = "generatedgames/gen_game_" + i + ".txt";
			String levelPath = "generatedgames/gen_game_" + i + "_lvl0.txt";
			
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(gamePath, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			writer.flush();
			writer.print(new_desc);
			writer.close();
			
			writer = null;
			try {
				writer = new PrintWriter(levelPath, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			writer.flush();
			writer.print(level_desc);
			writer.close();
		}
	
        
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
//        while(!gameIsGood){
//            String new_desc = GameChanger.makePuzzleGame();
//            System.out.println(new_desc);
//            
//            
//            
//            LevelGenerator lg = new LevelGenerator(new_desc);
//            lg.generateLevel(8, 8, null);
//            
//        }

    }
        
        

 
}
