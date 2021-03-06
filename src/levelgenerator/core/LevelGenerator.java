package levelgenerator.core;

import fastVGDL.core.game.GamePlayer;
import fastVGDL.core.game.results.GameResults;
import gamechanger.core.GameChanger;
import gamechanger.parsing.LevelMapping;
import gamechanger.parsing.Parser;
import gamechanger.parsing.Sprite;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Random;

import levelgenerator.map.LevelMap;
import levelgenerator.map.Mapping;

public class LevelGenerator {
	
	static Random r = new Random();
	
	public String gameTitle;
	
	public String gameFolder = "../gvgai/examples/gridphysics/";
		
	public char groundChar = " ".charAt(0);
	
	public boolean makeWall = true;
	
	PrintStream origOut;
	
	
	
	final int iterations = 100;
	final int mutations = 100;
	final int mutationsSurvive = 50;
	
	public LevelGenerator(String gameTitle){
		this.gameTitle = gameTitle;
		origOut = System.out;
	}
	
    public LevelGenerator(String gameTitle, String gameFolder){
		this.gameTitle = gameTitle;
        this.gameFolder = gameFolder;
		origOut = System.out;
	}
	
    
    
    public GameInfo generateLevel(int width, int height, LevelMap initMap){
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
		
//		System.out.println(getLevelString(levelMap.map));
		
		LevelMap[] levelMaps = new LevelMap[mutations];
		
		//Initialise with mutated levels
		for (int m = 0; m < mutations; m++) {
			char[][] mutatedMap = mutateLevel(levelMap, mappings);
			levelMaps[m] = new LevelMap(mutatedMap);
			
//			System.out.println(getLevelString(levelMaps[m].map));
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
						float rnd = r.nextFloat();
                                            if (rnd < 0.66){
						int idx = r.nextInt(mutationsSurvive);
						newMap.map = mutateLevel(lastGameInfos[idx].levelMap, mappings);	
                                            }else if (rnd < 0.90){
                                                int idx1 = r.nextInt(mutationsSurvive);
                                                int idx2 = r.nextInt(mutationsSurvive);
                                                while (idx2 == idx1) idx2 = r.nextInt(mutations);
                                                newMap.map = crossOverLevel(lastGameInfos[idx1].levelMap, lastGameInfos[idx2].levelMap, mappings);
                                                newMap.map = mutateLevel(lastGameInfos[idx1].levelMap, mappings);	
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
			System.out.println("Finished mutating levels -- playing through");
	        
	        Comparator<GameInfo> comparator = new GameInfoComparator();
	        PriorityQueue<GameInfo> gameInfos = new PriorityQueue<GameInfo>(mutations, comparator);
	        
	        for (int m = 0; m < mutations; m++) {
//	        	System.out.println("Playing level: \n" + getLevelString(levelMaps[m].map));
				GameInfo gi = playGameGetData(gameDescPath, levelMaps[m]);
				gameInfos.add(gi);
				
				if (m % (mutations/10) == 0) System.out.println("played through " + m + " mutated levels");
				
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
			System.out.println("Time: " + new Timestamp(new Date().getTime()));
			System.out.println("---------------------------");
			for (int j = 0; j < 3; j++) {
				System.out.println("Survived GameInfo: " + lastGameInfos[j]);
//				System.out.println("Survived GameInfo " + j + ", won: " + lastGameInfos[j].won + " , timesteps: " + lastGameInfos[j].timesteps + " , score: " + lastGameInfos[j].score + " , action count: " + lastGameInfos[j].actionCount);
				System.out.println(getLevelString(lastGameInfos[j].levelMap.map));
			}
			System.out.println("---------------------------");
			System.out.println("---------------------------");
			
			boolean isInteresting = true;
			if (lastGameInfos[0].results.actions <= 1) isInteresting = false;
			for (int j = 0; j < lastGameInfos.length; j++) {
				if (lastGameInfos[j].results.numberSpritesMoved > 0){
					isInteresting = true;
					break;
				}else{
					isInteresting = false;
				}
			}
			if (!isInteresting) return null;
			
			if (i > 10 && (lastGameInfos[0].results.actions == lastGameInfos[mutations-1].results.actions || lastGameInfos[0].results.actions < 25)){
				System.out.println("Returning early beca" +
						"+use best act count == worst act count || act count too low");
				return null;
			}
		}
		
		return lastGameInfos[0];
    }
    

		
	private char[][] crossOverLevel(LevelMap levelMap, LevelMap levelMap2, ArrayList<Mapping> mappings) {
		
//		System.out.println("CROSSOVER");
//		System.out.println(getLevelString(levelMap.map));
//		System.out.println("++++++++++");
//		System.out.println(getLevelString(levelMap2.map));
		
		boolean passedAnAvatar = false;
		
		char[][] newLevel = new char[levelMap.map.length][];
		for (int i = 0; i < newLevel.length; i++) newLevel[i] = new char[levelMap.map[i].length];
		
		for (int i = 0; i < levelMap.map.length; i++) {
			for (int j = 0; j < levelMap.map[i].length; j++) {
				double prob = r.nextDouble();
				
				if (passedAnAvatar && (levelMap.map[i][j] == "A".charAt(0) || levelMap2.map[i][j] == "A".charAt(0))){ //set avatar here not matter what
					newLevel[i][j] = "A".charAt(0);
				}else{
					
				
				
					if (prob > 0.5){
						newLevel[i][j] = levelMap.map[i][j];
										
						if (levelMap2.map[i][j] == "A".charAt(0)) passedAnAvatar = true;
					}else{
						newLevel[i][j] = levelMap2.map[i][j];
						
						if (levelMap.map[i][j] == "A".charAt(0)) passedAnAvatar = true;
					}
				
				}
			}
		}
		
//		System.out.println("==========");
//		System.out.println(getLevelString(newLevel));
		
		//Remove duplicates for singleton sprites
		removeSingletonDuplicates(mappings, newLevel);
		
//		System.out.println("---------> (after removeSingletonDuplicates)");
//		System.out.println(getLevelString(newLevel));
		
		return newLevel;
	}
	
	
	private GameInfo playGameGetData(String gameDescPath, LevelMap map) {		
		//Save map as .txt-file as it is now
		String lvlString = getLevelString(map.map);
		String lvlPath = saveCurrentLevel(lvlString);
		
//		String actionFilePath = gameFolder + "actions.txt";

//		System.out.println("Playing game " + gameDescPath + " - level: ");
//		System.out.println(getLevelString(map.map));
		//Play map as it is now, and store output in file
//        try {
//			System.setOut(new PrintStream(new FileOutputStream(outputFolder + "gamedata.txt")));
// 		} catch (FileNotFoundException e) {
// 			e.printStackTrace();
// 		}
        
                
             
       
        GameResults results = GamePlayer.playGame(gameDescPath, lvlPath, "fastVGDL.controllers.puzzleSolverPlusLowMem.Agent", false);
        
        GameInfo gi = new GameInfo(results, map);
        
        
//        System.out.println("Finished playing..");
//        System.out.println(results);
        
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
		return mutateLevel(levelMap.map, mappings, r.nextInt(2), r.nextInt(2), r.nextInt(2), r.nextInt(2));
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
		String path = gameFolder + gameTitle + "_genlvl.txt";
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
	
	
	public static String getLevelString(char[][] levelMap){
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
	
	
	

	
	
}
