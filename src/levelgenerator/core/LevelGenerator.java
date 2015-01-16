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

import core.ArcadeMachine;

public class LevelGenerator {
	
	static Random r = new Random();
	
	public String gameTitle;
	
	public String gameFolder = "../gvgai/examples/gridphysics/";
	public String outputFolder = "levelgen/";
	
	public char groundChar = " ".charAt(0);
	
	boolean makeWall = true;
	
	PrintStream origOut;
	
	public static void main(String[] args) {
		LevelGenerator lg = new LevelGenerator("realsokoban");	
		lg.groundChar = ".".charAt(0);
		lg.makeWall = true;
		lg.generateLevel(8, 7, null);
		
//		lg.generateLevel(8, 7, lg.getCharMap(	"wwwwwwww\n"+
//												"w.w....w\n"+
//												"w.w.w*.w\n"+
//												"ww.*A..w\n"+
//												"w...Owww\n"+
//												"wO*...Ow\n"+
//												"wwwwwwww"));
	}
	
	public LevelGenerator(String gameTitle){
		this.gameTitle = gameTitle;
		origOut = System.out;
	}
	

		
	
	public void generateLevel(int width, int height, char[][] initMap){	
		System.out.println("Generating level for game: " + gameTitle);
		
		
		boolean addLastGameInfosToNewIterations = true;
		
		String gameDescPath = gameFolder + gameTitle + ".txt";
		
		int iterations = 100;
		int mutations = 1000;
		int mutationsSurvive = 4;
				
		ArrayList[] gameElements = Parser.readGameOutput(gameDescPath);
		
		ArrayList<Sprite> sprites = gameElements[0];
		ArrayList<LevelMapping> levelMappings = gameElements[2];
		
		ArrayList<Mapping> mappings = getMappings(sprites, levelMappings);
		
		GameChanger.setAvatar(sprites);
		
		char[][] levelMap = initMap;
		if (levelMap == null){
			levelMap = makeLevel(mappings, width, height);
		}
		
		char[][][] mutatedLevelMaps = new char[mutations + mutationsSurvive][][];
		char[][][] surivedMutatedLevelMaps = new char[mutationsSurvive][][];
		
		//Initialize with slightly mutated levels
		for (int j = 0; j < mutations + mutationsSurvive; j++) {
			int amountChangedSprites = 1, amountRemoves = 0, amountNewWall = 0, amountNewSprites = 0;
			mutatedLevelMaps[j] = mutateLevel(levelMap, mappings, amountRemoves, amountChangedSprites, amountNewWall, amountNewSprites);
		}


		GameInfo bestGameInfo = null;
		GameInfo[] lastGameInfos = new GameInfo[mutationsSurvive];
		for (int i = 0; i < iterations; i++) {
			
			
			if (i>0){ //Dont check for survived games in 1st iteration
				for (int j = 0; j < mutationsSurvive; j++) { //for each mutation that survived last iteration, generate (=mutations/mutationsSurvive) new mutations
					int amountNewMutations = mutations/mutationsSurvive;
										
					//Get game data
					GameInfo gi = lastGameInfos[j];
					
					//Mutate game accordingly a number of times
					for (int k = 0; k < amountNewMutations; k++) {
						int amountChangedSprites = 0, amountRemoves = 0, amountMoves = 0, amountNewSprites = 0;
//						if (gi.won < 0){
//							amountRemoves += 1;
//							amountChangedSprites += 1;
//						}else{
//							if (gi.actionCount < 100){
//								amountRemoves += 1;
//								amountMoves += 1;
//								amountNewSprites += 1;
//							}
//							amountChangedSprites += 1;
//						}
						amountRemoves += 1;
//						amountChangedSprites += 1;
						amountNewSprites += 1;
//						int paramToChange = range(0,4);
//						if (paramToChange==0) amountRemoves += 1;
//						else if (paramToChange==1) amountChangedSprites += 1;
//						else if (paramToChange==2) amountMoves += 1;
//						else if (paramToChange==3) amountNewSprites += 1;
						
						while (r.nextDouble() < 0.5){
							amountRemoves += 1;
//							amountChangedSprites += 1;
							amountNewSprites += 1;
							int paramToChange = range(0,6);
							if (paramToChange==0) amountRemoves += 1;
							else if (paramToChange==1) amountChangedSprites += 1;
							else if (paramToChange==2) amountMoves += 1;
							else if (paramToChange==3) amountNewSprites += 1;
						}
						
						char[][] newLevelMap = mutateLevel(surivedMutatedLevelMaps[j], mappings, amountRemoves, amountChangedSprites, amountMoves, amountNewSprites);
						mutatedLevelMaps[j * amountNewMutations + k] = newLevelMap;
					}
					mutatedLevelMaps[mutations + j] = gi.levelMap;
				}
			}

			System.out.println("Finished mutating games ");
			//Only let some games survive
//			ArrayList<GameInfo> gameInfos = new ArrayList<GameInfo>();
	        
	        
	        int mutatedGameCount = mutations;
	        if (addLastGameInfosToNewIterations){
	        	mutatedGameCount += mutationsSurvive;
//	        	mutatedGameCount += 1; //only take the best one mutation from last iteration
	        }
	        
	        Comparator<GameInfo> comparator = new GameInfoComparator();
	        PriorityQueue<GameInfo> gameInfos = new PriorityQueue<GameInfo>(mutatedGameCount, comparator);

	        
			for (int j = 0; j < mutatedGameCount; j++) {	
				//Save map as .txt-file as it is now
				String lvlString = getLevelString(mutatedLevelMaps[j]);
				String lvlPath = saveCurrentLevel(lvlString);
				
				String actionFilePath = outputFolder + "actions.txt";

				//Play map as it is now, and store output in file
		        try {
					System.setOut(new PrintStream(new FileOutputStream(outputFolder + "gamedata.txt")));
		 		} catch (FileNotFoundException e) {
		 			e.printStackTrace();
		 		}
				ArcadeMachine.runOneGame(gameDescPath, lvlPath, false, "controllers.puzzleSolverPlus.Agent", actionFilePath, r.nextInt());
				System.setOut(origOut);
				
				//Get game data
				GameInfo gi = new GameInfo(outputFolder, mutatedLevelMaps[j]);
				gameInfos.add(gi);
				
				System.out.println("Got gameInfo: + " + j + ", won: " + gi.won + " , timesteps: " + gi.timesteps + " , score: " + gi.score + " , action count: " + gi.actionCount);
				System.out.println(getLevelString(gi.levelMap));
			}
			
			GameInfo[] gameInfoList = null;
	        for (int k = 0; k < mutationsSurvive; k++) {
	        	GameInfo gi = null;
//	        	if (k < mutationsSurvive/2){
		        	gi = gameInfos.remove();
//	        	}else{
//	        		if (gameInfoList == null) gameInfoList = gameInfos.toArray(new GameInfo[mutatedGameCount -  mutationsSurvive/2]);
//	        		int idx = range(0, mutatedGameCount -  mutationsSurvive/2 - 1);
//	        		gi = gameInfoList[idx];
//	        	}
	        	surivedMutatedLevelMaps[k] = gi.levelMap;
	        	lastGameInfos[k] = gi;
	        	if (k==0) bestGameInfo = gi;
	        }

			
//			System.out.println("GameInfo, won: " + gi.won + " , timesteps: " + gi.timesteps + " , score: " + gi.score);
//			System.out.println("Mutating game with parameters, amountRemoves: " +  amountRemoves + ", amountChangedSprites: " +amountChangedSprites + ", amountNewWall: " + amountNewWall + ", amountNewSprites: " +amountNewSprites);
//			levelMap = mutateLevel(levelMap, mappings, amountRemoves, amountChangedSprites, amountNewWall, amountNewSprites);
//			
			
	        System.out.println("---------------------------");
			System.out.println("Loop end - iteration: " + i);
			System.out.println("---------------------------");
			for (int j = 0; j < lastGameInfos.length; j++) {
				System.out.println("Survived GameInfo " + j + ", won: " + lastGameInfos[j].won + " , timesteps: " + lastGameInfos[j].timesteps + " , score: " + lastGameInfos[j].score + " , action count: " + lastGameInfos[j].actionCount);
				System.out.println(getLevelString(lastGameInfos[j].levelMap));
			}
			System.out.println("---------------------------");
			System.out.println("---------------------------");
			
		}
		
		
		String lvlString = getLevelString(bestGameInfo.levelMap);
		saveCurrentLevel(lvlString);
		
	}
	
	
	private ArrayList<Mapping> getMappings(ArrayList<Sprite> sprites, ArrayList<LevelMapping> levelMappings) {
		ArrayList<Mapping> result = new ArrayList<Mapping>();

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
		}
		
		return result;
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
			while (x == -1 || newLevelMap[x][y] == "A".charAt(0)){
				
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
				while (lm == null || lm.charID == "A".charAt(0)){
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
				while (lm == null || lm.charID == "A".charAt(0)){
					
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
			
			System.out.println("mapping.isAvatar: " + mapping.isAvatar);
			
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
		public float score = 0;
		public int timesteps = 0;
		public int won = 0;
		
		public int actionCount = 0;
		
		public char[][] levelMap;
		
		Pattern patternResult = Pattern.compile("Result \\(1->win; 0->lose\\):([-]?[0-1]+), Score:([-]?[0-9]+\\.[0-9]+), timesteps:([0-9]+)");

		public GameInfo(String dataFolder, char[][] levelMap){
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
	}
	
    class GameInfoComparator implements Comparator<GameInfo>
	{
	    @Override
	    public int compare(GameInfo x, GameInfo y)
	    {
	    	if (x.won > y.won){
	    		return -1;
	    	}
	        if (x.won < y.won){
	            return 1;
	        }
	    	
	    	if (x.actionCount > y.actionCount){
	    		return -1;
	    	}
	    	if (x.actionCount < y.actionCount){
	    		return 1;
	    	}
	    	
	    	if (x.timesteps > y.timesteps){
	    		return -1;
	    	}
	    	if (x.timesteps < y.timesteps){
	    		return 1;
	    	}
	    	
	    	if (x.score < y.score){
	    		return -1;
	    	}
	    	if (x.score > y.score){
	    		return 1;
	    	}
	        return 0;
	    }
	}
	
	
}