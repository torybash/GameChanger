package levelgenerator.core;

import gamechanger.core.GameChanger;
import gamechanger.parsing.LevelMapping;
import gamechanger.parsing.Parser;
import gamechanger.parsing.Sprite;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import levelgenerator.map.LevelMap;
import levelgenerator.map.Mapping;

import fastVGDL.core.game.Game;
import fastVGDL.core.game.GamePlayer;
import fastVGDL.core.game.results.GameResults;
import java.util.Arrays;

public class LevelGenerator {
	
	static Random r = new Random();
	
	public String gameTitle;
	
	public String gameFolder = "../gvgai/examples/gridphysics/";
	public String outputFolder = "levelgen/";
	
	public char groundChar = " ".charAt(0);
	
	boolean makeWall = true;
	
	PrintStream origOut;
	
	
	
	final int iterations = 1000;
	final int mutations = 100;
	final int mutationsSurvive = 20;
	
	public LevelGenerator(String gameTitle){
		this.gameTitle = gameTitle;
		origOut = System.out;
	}
	
    public LevelGenerator(String gameTitle, String gameFolder){
		this.gameTitle = gameTitle;
        this.gameFolder = gameFolder;
		origOut = System.out;
	}
	
    
    
    public void generateLevel(int width, int height, LevelMap initMap){
		System.out.println("Generating level for game: " + gameTitle);

		String gameDescPath = gameFolder + gameTitle + ".txt";
		ArrayList[] gameElements = Parser.readGameDescByPath(gameDescPath);
		ArrayList<Sprite> sprites = gameElements[0];
		ArrayList<LevelMapping> levelMappings = gameElements[2];
		ArrayList<Mapping> mappings = getMappings(sprites, levelMappings);
    	
		GameChanger.setSpriteClasses();
		GameChanger.setAvatar(sprites);
		
		LevelMap levelMap = initMap;
		if (levelMap.map == null){
			levelMap.map = makeLevel(mappings, width, height);
			levelMap.calculateSpriteMappings(mappings);
		}
		
		System.out.println(getLevelString(levelMap.map));
		
		LevelMap[] levelMaps = new LevelMap[mutations];
		
		//Initialise with mutated levels
		for (int m = 0; m < mutations; m++) {
			char[][] mutatedMap = mutateLevel(levelMap, mappings);
			levelMaps[m] = new LevelMap(mutatedMap);
			
			System.out.println(getLevelString(levelMaps[m].map));
//			int amountChangedSprites = 5, amountRemoves = 2, amountNewWall = 2, amountNewSprites = 2;
//			levelMaps[m] = mutateLevel(levelMap, mappings, amountRemoves, amountChangedSprites, amountNewWall, amountNewSprites);
		}
		
		
		
		
		GameInfo[] lastGameInfos = new GameInfo[mutations];
		for (int i = 0; i < iterations; i++) {
			
			//Mutating games
			if (i>0){ //Don't do in 1st iteration
				
				for (int m = 0; m < mutations; m++) {
					LevelMap newMap = new LevelMap();
					
					if (m > mutationsSurvive){
                                            if (r.nextFloat() > 0.25){
						int idx = r.nextInt(mutationsSurvive);
						newMap.map = mutateLevel(lastGameInfos[idx].levelMap, mappings);	
                                            }else if (r.nextFloat() > 0.65){
                                                int idx1 = r.nextInt(mutationsSurvive);
                                                int idx2 = r.nextInt(mutations);
                                                while (idx2 == idx1) idx2 = r.nextInt(mutations);
                                                newMap.map = crossOverLevel(lastGameInfos[idx1].levelMap, lastGameInfos[idx2].levelMap, mappings);
                                            }else{
                                                newMap.map = makeLevel(mappings, width, height);	//new level

                                            }
                                        }else{
                                            newMap.map = lastGameInfos[m].levelMap.map;
//                                            if (r.nextFloat() > 0.50){
//						newMap.map = mutateLevel(newMap, mappings);	
//                                            }
                                        }
					levelMaps[m] = newMap;
				}
				
//				for (int j = 0; j < mutationsSurvive; j++) { //for each mutation that survived last iteration, generate (=mutations/mutationsSurvive) new mutations
//					int amountNewMutations = mutations/mutationsSurvive;
//										
//					//Get game data
//					GameInfo gi = lastGameInfos[j];
//					
//					//Mutate game accordingly a number of times
////					for (int k = 0; k < amountNewMutations; k++) {
////						int amountChangedSprites = 0, amountRemoves = 0, amountMoves = 0, amountNewSprites = 0;
////						
////						char[][] newLevelMap = mutateLevel(surivedMutatedLevelMaps[j], mappings, amountRemoves, amountChangedSprites, amountMoves, amountNewSprites);
////						mutatedLevelMaps[j * amountNewMutations + k] = newLevelMap;
////					}
////					mutatedLevelMaps[mutations + j] = gi.levelMap;
//				}
			}

			
			//Play through all levels
			System.out.println("Finished mutating games ");
	        
	        Comparator<GameInfo> comparator = new GameInfoComparator();
	        PriorityQueue<GameInfo> gameInfos = new PriorityQueue<GameInfo>(mutations, comparator);
	        
	        for (int m = 0; m < mutations; m++) {
//	        	System.out.println("Playing level: \n" + getLevelString(levelMaps[m].map));
				GameInfo gi = playGameGetData(gameDescPath, levelMaps[m]);
				gameInfos.add(gi);
				
//				System.out.println("Got gameInfo: + " + m + ", won: " + gi.won + " , timesteps: " + gi.timesteps + " , score: " + gi.score + " , action count: " + gi.actionCount);
				;
			}
			
	        for (int m = 0; m < mutations; m++) {
	        	GameInfo gi = gameInfos.poll();
	        	lastGameInfos[m] = gi;
//	        	surivedMutatedLevelMaps[s] = gi.levelMap;
//	        	lastGameInfos[s] = gi;
	        }

		
	        System.out.println("---------------------------");
			System.out.println("Loop end - iteration: " + i);
			System.out.println("---------------------------");
			for (int j = 0; j < 3; j++) {
				System.out.println("Survived GameInfo: " + lastGameInfos[j]);
//				System.out.println("Survived GameInfo " + j + ", won: " + lastGameInfos[j].won + " , timesteps: " + lastGameInfos[j].timesteps + " , score: " + lastGameInfos[j].score + " , action count: " + lastGameInfos[j].actionCount);
				System.out.println(getLevelString(lastGameInfos[j].levelMap.map));
			}
			System.out.println("---------------------------");
			System.out.println("---------------------------");
			
		}
    }
    

		
	private char[][] crossOverLevel(LevelMap levelMap, LevelMap levelMap2, ArrayList<Mapping> mappings) {
		
//		System.out.println("CROSSOVER");
//		System.out.println(getLevelString(levelMap.map));
//		System.out.println("++++++++++");
//		System.out.println(getLevelString(levelMap2.map));
		
		char[][] newLevel = new char[levelMap.map.length][];
		for (int i = 0; i < newLevel.length; i++) newLevel[i] = new char[levelMap.map[i].length];
		
		for (int i = 0; i < levelMap.map.length; i++) {
			for (int j = 0; j < levelMap.map[i].length; j++) {
				double prob = r.nextDouble();
				if (prob > 0.5){
					newLevel[i][j] = levelMap.map[i][j];
				}else{
					newLevel[i][j] = levelMap2.map[i][j];
				}
			}
		}
		
//		System.out.println("==========");
//		System.out.println(getLevelString(newLevel));
		
		//Remove duplicates for singleton sprites
		removeSingletonDuplicates(mappings, newLevel);
		
//		System.out.println("--------->");
//		System.out.println(getLevelString(newLevel));
		
		return newLevel;
	}

	public void generateLevel(int width, int height, char[][] initMap){	
//		
//		System.out.println("Generating level for game: " + gameTitle);
//
//		boolean addLastGameInfosToNewIterations = true;		
//		String gameDescPath = gameFolder + gameTitle + ".txt";
//		ArrayList[] gameElements = Parser.readGameOutput(gameDescPath);
//		ArrayList<Sprite> sprites = gameElements[0];
//		ArrayList<LevelMapping> levelMappings = gameElements[2];
//		ArrayList<Mapping> mappings = getMappings(sprites, levelMappings);
//		
//		GameChanger.setSpriteClasses();
//		GameChanger.setAvatar(sprites);
//		
//		char[][] levelMap = initMap;
//		if (levelMap == null) levelMap = makeLevel(mappings, width, height);
//		
//		char[][][] mutatedLevelMaps = new char[mutations + mutationsSurvive][][];
//		char[][][] surivedMutatedLevelMaps = new char[mutationsSurvive][][];
//		
//		//Initialise with mutated levels
//		for (int j = 0; j < mutations + mutationsSurvive; j++) {
//			int amountChangedSprites = 5, amountRemoves = 2, amountNewWall = 2, amountNewSprites = 2;
//			mutatedLevelMaps[j] = mutateLevel(levelMap, mappings, amountRemoves, amountChangedSprites, amountNewWall, amountNewSprites);
//		}
//
//
//		GameInfo[] lastGameInfos = new GameInfo[mutationsSurvive];
//		for (int i = 0; i < iterations; i++) {
//			
//			if (i>0){ //Don't check for survived games in 1st iteration
//				for (int j = 0; j < mutationsSurvive; j++) { //for each mutation that survived last iteration, generate (=mutations/mutationsSurvive) new mutations
//					int amountNewMutations = mutations/mutationsSurvive;
//										
//					//Get game data
//					GameInfo gi = lastGameInfos[j];
//					
//					//Mutate game accordingly a number of times
//					for (int k = 0; k < amountNewMutations; k++) {
//						int amountChangedSprites = 0, amountRemoves = 0, amountMoves = 0, amountNewSprites = 0;
//						
//						char[][] newLevelMap = mutateLevel(surivedMutatedLevelMaps[j], mappings, amountRemoves, amountChangedSprites, amountMoves, amountNewSprites);
//						mutatedLevelMaps[j * amountNewMutations + k] = newLevelMap;
//					}
//					mutatedLevelMaps[mutations + j] = gi.levelMap.map;
//				}
//			}
//
//			
//			//Play through all levels
//			System.out.println("Finished mutating games ");
//	        int mutatedGameCount = mutations;
//	        if (addLastGameInfosToNewIterations) mutatedGameCount += mutationsSurvive;
//	        
//	        Comparator<GameInfo> comparator = new GameInfoComparator();
//	        PriorityQueue<GameInfo> gameInfos = new PriorityQueue<GameInfo>(mutatedGameCount, comparator);
//	        
//			for (int j = 0; j < mutatedGameCount; j++) {	
//				GameInfo gi = playGameGetData(gameDescPath, mutatedLevelMaps[j]);
//				gameInfos.add(gi);
//				
//				System.out.println("Got gameInfo: + " + j + ", won: " + gi.won + " , timesteps: " + gi.timesteps + " , score: " + gi.score + " , action count: " + gi.actionCount);
//				System.out.println(getLevelString(gi.levelMap.map));
//			}
//			
//	        for (int s = 0; s < mutationsSurvive; s++) {
//	        	GameInfo gi = gameInfos.poll();
//	        	surivedMutatedLevelMaps[s] = gi.levelMap.map;
//	        	lastGameInfos[s] = gi;
//	        }
//
//		
//	        System.out.println("---------------------------");
//			System.out.println("Loop end - iteration: " + i);
//			System.out.println("---------------------------");
//			for (int j = 0; j < lastGameInfos.length; j++) {
//				System.out.println("Survived GameInfo " + j + ", won: " + lastGameInfos[j].won + " , timesteps: " + lastGameInfos[j].timesteps + " , score: " + lastGameInfos[j].score + " , action count: " + lastGameInfos[j].actionCount);
//				System.out.println(getLevelString(lastGameInfos[j].levelMap.map));
//			}
//			System.out.println("---------------------------");
//			System.out.println("---------------------------");
//			
//		}
	}
	
	
	private GameInfo playGameGetData(String gameDescPath, LevelMap map) {		
		//Save map as .txt-file as it is now
		String lvlString = getLevelString(map.map);
		String lvlPath = saveCurrentLevel(lvlString);
		
		String actionFilePath = outputFolder + "actions.txt";

		System.out.println("Playing game " + gameDescPath + " - level: ");
		System.out.println(getLevelString(map.map));
		//Play map as it is now, and store output in file
//        try {
//			System.setOut(new PrintStream(new FileOutputStream(outputFolder + "gamedata.txt")));
// 		} catch (FileNotFoundException e) {
// 			e.printStackTrace();
// 		}
        
                
             
       
        GameResults results = GamePlayer.playGame(gameDescPath, lvlPath, "fastVGDL.controllers.puzzleSolverPlus.Agent", false);
        
        GameInfo gi = new GameInfo(results, map);
        
        
//		ArcadeMachine.runOneGame(gameDescPath, lvlPath, false, "controllers.puzzleSolverPlus.Agent", actionFilePath, r.nextInt());
//		System.setOut(origOut);
		
		return  gi;
	}

	private ArrayList<Mapping> getMappings(ArrayList<Sprite> sprites, ArrayList<LevelMapping> levelMappings) {
		ArrayList<Mapping> result = new ArrayList<Mapping>();

		boolean hasAvatar = false;
		for (LevelMapping lm : levelMappings) {
			boolean isSingleton = false;
			
			//Check if mapping reference a singleton
			for (Sprite sprite : sprites) {
				for (String lmRef : lm.references) {
					if (lmRef.equals(sprite.identifier)){
						if (sprite.parameters.containsKey("singleton") && sprite.parameters.get("singleton").equalsIgnoreCase("true")) isSingleton = true;
					}
				}
			}
			
			Mapping m = new Mapping(lm.charID, isSingleton);
			result.add(m);
			
			if (lm.charID == "A".charAt(0)) hasAvatar = true;
		}
		
		Mapping mw = new Mapping("w".charAt(0), false);
		result.add(mw);
		
		if (!hasAvatar){
			Mapping ma = new Mapping("A".charAt(0), true);
			result.add(ma);
		}

		return result;
	}

	private char[][] mutateLevel(LevelMap levelMap, ArrayList<Mapping> mappings) {
		return mutateLevel(levelMap.map, mappings, 1, 1, 1, 1);
	}
	
	private char[][] mutateLevel(char[][] levelMap, ArrayList<Mapping> mappings, int amountRemoves, int amountChanges, int amountMoves, int amountNewSprites) {
		char[][] newLevelMap = new char[levelMap.length][];
		for (int i = 0; i < newLevelMap.length; i++) {
			newLevelMap[i] = levelMap[i].clone();
		}
		
		
		//Remove sprites (change to blank)
		int amountOfSpriteTilesWithWalls = amountOfSpriteTiles(newLevelMap, true);
		amountRemoves = amountOfSpriteTilesWithWalls < amountRemoves ? amountOfSpriteTilesWithWalls : amountRemoves;
		for (int i = 0; i < amountRemoves; i++) {
			int x = -1, y = -1;
			while (x == -1 || newLevelMap[x][y] == "A".charAt(0)  || newLevelMap[x][y] == groundChar){
				
				x = r.nextInt(newLevelMap.length - 2) + 1;
				y = r.nextInt(newLevelMap[0].length - 2) + 1;
			}
			newLevelMap[x][y] = groundChar;
		}
		
		//Change sprites
		int amountOfSpriteTiles = amountOfSpriteTiles(newLevelMap, false);
		amountChanges = amountOfSpriteTiles < amountChanges ? amountOfSpriteTiles : amountChanges;
		for (int i = 0; i < amountChanges; i++) {
			int x = -1, y = -1;
			while (x == -1 || newLevelMap[x][y] == "A".charAt(0) || newLevelMap[x][y] == groundChar){
				
				x = r.nextInt(newLevelMap.length - 2) + 1;
				y = r.nextInt(newLevelMap[0].length - 2) + 1;
			}

			Mapping lm = null;
			int mappingIdx = range(0, mappings.size()*2);	//50% chance for wall
			if (mappingIdx >= mappings.size()){
				newLevelMap[x][y] = "w".charAt(0);
			}else{
				while (lm == null || lm.charID == "A".charAt(0) || lm.charID == groundChar){
					mappingIdx = r.nextInt(mappings.size());
					lm = mappings.get(mappingIdx);
				}
				newLevelMap[x][y] = lm.charID;
			}
		}
		
		//Remove duplicates for singleton sprites
		removeSingletonDuplicates(mappings, newLevelMap);
		
		
		//Move sprites
		int amountOfFreeTiles = amountOfFreeTiles(newLevelMap);
		amountMoves = amountOfFreeTiles > 0 ? amountMoves : 0;
		for (int i = 0; i < amountMoves; i++) {
			//Find sprite to move
			char charToMove;
			int x = -1, y = -1;
			while (x == -1 || newLevelMap[x][y] == groundChar || newLevelMap[x][y] == "A".charAt(0)){
				x = r.nextInt(newLevelMap.length - 2) + 1;
				y = r.nextInt(newLevelMap[0].length - 2) + 1;
			}
			charToMove = newLevelMap[x][y];
			newLevelMap[x][y] = groundChar;
			
			//Find new place for sprite
			int oldx = x, oldy = y;
			while ((oldx == x && oldy == y) || newLevelMap[x][y] != groundChar){
				x = r.nextInt(newLevelMap.length - 2) + 1;
				y = r.nextInt(newLevelMap[0].length - 2) + 1;
			}
			newLevelMap[x][y] = charToMove;
		}
		
		//Add new sprites
		amountNewSprites = amountOfFreeTiles < amountNewSprites ? amountOfFreeTiles : amountNewSprites;
		for (int i = 0; i < amountNewSprites; i++) {
			int x = -1, y = -1;
			while (x == -1 || newLevelMap[x][y] != groundChar){
				
				x = r.nextInt(newLevelMap.length - 2) + 1;
				y = r.nextInt(newLevelMap[0].length - 2) + 1;
			}
			
			Mapping lm = null;
			int mappingIdx = range(0, mappings.size()*2);	//50% chance for wall
			if (mappingIdx >= mappings.size()){
				newLevelMap[x][y] = "w".charAt(0);
			}else{
				while (lm == null || lm.charID == "A".charAt(0) || lm.charID == groundChar){
					
					mappingIdx = r.nextInt(mappings.size());
					lm = mappings.get(mappingIdx);
				}
				newLevelMap[x][y] = lm.charID;
			}

		}
		//Remove duplicates for singleton sprites
		removeSingletonDuplicates(mappings, newLevelMap);
		


		
		return newLevelMap;
	}
	
	private void removeSingletonDuplicates(ArrayList<Mapping> mappings, char[][] levelMap){
		for (Mapping mapping : mappings){
			if (mapping.isSingleton){
				boolean foundSprite = false;
				for (int i = 0; i < levelMap.length; i++) {
					for (int j = 0; j < levelMap[0].length; j++) {
						if (levelMap[i][j] == mapping.charID){
							if (!foundSprite){
								foundSprite = true;
							}else{
								levelMap[i][j] = groundChar;
							}
						}
					}
				}
			}
		}
	}

	private int amountOfSpriteTiles(char[][] levelMap, boolean withWalls) {
		int result = 0;
		for (int i = 0; i < levelMap.length; i++) {
			for (int j = 0; j < levelMap[0].length; j++) {
				if (withWalls){
					if (levelMap[i][j] != groundChar && levelMap[i][j] != "A".charAt(0)) result++;
				}else{
					if (levelMap[i][j] != groundChar && levelMap[i][j] != "w".charAt(0) && levelMap[i][j] != "A".charAt(0)) result++;
				}
			}
		}
		return result;
	}

	int amountOfFreeTiles(char[][] levelMap){
		int result = 0;
		for (int i = 0; i < levelMap.length; i++) {
			for (int j = 0; j < levelMap[0].length; j++) {
				if (levelMap[i][j] == groundChar) result++;
			}
		}
		return result;
	}
	
	private String saveCurrentLevel(String lvlString) {
		String[] lines = lvlString.split("\\n");
		PrintWriter writer;
		String path = outputFolder + gameTitle + "_genlvl.txt";
		try {
			writer = new PrintWriter(path, "UTF-8");
			for (int i = 0; i < lines.length; i++) {
				writer.println(lines[i]);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return path;
	}



	char[][] makeLevel(ArrayList<Mapping> mappings, int width, int height) {
		ArrayList<Mapping> newMappings = (ArrayList<Mapping>) mappings.clone();
		
		String result = "";
		
		char[][] levelMap = new char[width][];
		for (int i = 0; i < width; i++) {
			levelMap[i] = new char[height];
			for (int j = 0; j < height; j++) {
				levelMap[i][j] = groundChar;
			}
		}
		
		//Make wall at edges
		if (makeWall){
			for (int i = 0; i < width; i++) {
				levelMap[i][0] = "w".charAt(0);
				levelMap[i][height-1] = "w".charAt(0);
			}
			for (int i = 0; i < height; i++) {
				levelMap[0][i] = "w".charAt(0);
				levelMap[width-1][i] = "w".charAt(0);
			}
		}
		
		//Checkif mappings contain avatar - otherwise create one
		boolean haveAvatarMapping = false;
		for (Mapping mapping : newMappings) {
			if (mapping.charID == "A".charAt(0)) haveAvatarMapping = true;
		}
		if (!haveAvatarMapping){
			Mapping avatarMapping = new Mapping("A".charAt(0), false);
			newMappings.add(avatarMapping);
		}
		
		Mapping wallMapping = new Mapping("w".charAt(0), false);
		newMappings.add(wallMapping);
		
		for (Mapping mapping : newMappings) {
			int amount = range(1, 50);
			amount = 1 + (amount * amount * amount * amount)/1000000; // 1 - 8
			int amountPut = 0;
			if (mapping.isAvatar) amount = 1;
			if (mapping.isWall) amount = range(1, 10);
			if (mapping.isSingleton) amount = 1;
			
//			System.out.println("mapping.isAvatar: " + mapping.isAvatar);
			
			int amountFreeTiles = amountOfFreeTiles(levelMap);
			boolean foundRoom = false;
			int x = -1, y = -1;
			while (amount > amountPut && amountPut < amountFreeTiles){
				x = r.nextInt(width);
				y = r.nextInt(height);
				if (levelMap[x][y] == groundChar){
					levelMap[x][y] = mapping.charID;
					amountPut++;
				}
			}
			
		}
		return levelMap;
	}
	
	
	String getLevelString(char[][] levelMap){
		String result = "";
		for (int j = 0; j < levelMap[0].length; j++) {
			for (int i = 0; i < levelMap.length; i++) {
				result += levelMap[i][j];
			}
			result += "\n";
		}
		return result;
	}
	
	char[][] getCharMap(String levelString){
		
		String[] lines = levelString.split("\\n");
		
		
		char[][] result = new char[lines[0].length()][];
		for (int i = 0; i < result.length; i++) {
			result[i] = new char[lines.length];
		}
		
		for (int j = 0; j < lines.length; j++) {
			String line = lines[j];
			for (int i = 0; i < line.length(); i++) {
				result[i][j] = line.charAt(i);
			}
		}
		return result;
	}
	

	/**both value included
	 */
	public static int range(int from, int to){
		if (from > to) throw new IllegalArgumentException("To value can't be smaller than from");		
		int result = r.nextInt(to-from+1) + from;
		return result;
	}
	
	
	
	class GameInfo{
		public GameResults results;;
		
		public float score = 0;
		public int timesteps = 0;
		public int won = 0;
		
		public int actionCount = 0;
		
		public int interactions = 0;
		public int sprites = 0;
		
//		public char[][] levelMap;
		public LevelMap levelMap;
		
		Pattern patternResult = Pattern.compile("Result \\(1->win; 0->lose\\):([-]?[0-1]+), Score:([-]?[0-9]+\\.[0-9]+), timesteps:([0-9]+)");

		public GameInfo(GameResults gr, LevelMap levelMap){
			
//			won = gr.won ? 1 : 0;
//			timesteps = gr.ticks;
//			actionCount = gr.actions;
//			interactions = gr.interactions;
//			sprites = gr.numberSprites;
			
			results = gr;
                        
                        this.levelMap = levelMap;
		}
		
		public GameInfo(String dataFolder, LevelMap levelMap){
			String gameDataPath = dataFolder + "gamedata.txt";
			String actionFilePath = dataFolder + "actions.txt";
			
			this.levelMap = levelMap;
			
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(gameDataPath));
				String line;
				while ((line = br.readLine()) != null) {
					Matcher matcher = patternResult.matcher(line);
					while (matcher.find()) {
						won = Integer.parseInt(matcher.group(1));
						score = Float.parseFloat(matcher.group(2));
						timesteps = Integer.parseInt(matcher.group(3));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				br = new BufferedReader(new FileReader(actionFilePath));
				String line = br.readLine(); //<--Skip first line (seed)
				while ((line = br.readLine()) != null) {
					if (!line.contains("NIL")) actionCount++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public String toString() {
			return results.toString();
//			return "Won: " + won + ", actions: " + actionCount + ", interactions: " + interactions + ", sprites: " + sprites;
		}
	}
	
    class GameInfoComparator implements Comparator<GameInfo>
	{
	    @Override
	    public int compare(GameInfo x, GameInfo y)
	    {
	    	if (x.results.won && !y.results.won){
	    		return -1;
	    	}
	    	if (!x.results.won && y.results.won){
	    		return 1;
	    	}
	    	
	    	if (x.results.actions > y.results.actions){
	    		return -1;
	    	}
	    	if (x.results.actions < y.results.actions){
	    		return 1;
	    	}
	    	
	    	if (x.results.numSpritesHasInteracted > y.results.numSpritesHasInteracted){
	    		return -1;
	    	}
	    	if (x.results.numSpritesHasInteracted < y.results.numSpritesHasInteracted){
	    		return 1;
	    	}
	    	
	    	
//	    	if (x.won > y.won){
//	    		return -1;
//	    	}
//	        if (x.won < y.won){
//	            return 1;
//	        }
	    	
//	    	if (x.actionCount > y.actionCount){
//	    		return -1;
//	    	}
//	    	if (x.actionCount < y.actionCount){
//	    		return 1;
//	    	}
	    	
//	    	if (x.interactions > y.interactions){
//	    		return -1;
//	    	}
//	    	if (x.interactions < y.interactions){
//	    		return 1;
//	    	}
//	    	
//	    	if (x.timesteps > y.timesteps){
//	    		return -1;
//	    	}
//	    	if (x.timesteps < y.timesteps){
//	    		return 1;
//	    	}
//	    	
//	    	if (x.score < y.score){
//	    		return -1;
//	    	}
//	    	if (x.score > y.score){
//	    		return 1;
//	    	}
	        return 0;
	    }
	}
	
	
}
