package dataanalysis.core;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Utility;


public class GameData {
	public String gameTitle = "";
	public ArrayList<LevelPlay> levelsPlayed = new ArrayList<LevelPlay>();
	
	public int numberLevelsPlayed = 0;
	public LevelPlay currLP = null;
	
	public int seed = -1;
		
	public HashMap<String, Float> gameValues = new HashMap<String, Float>();
	public HashMap<String, Float>[] levelValues = new HashMap[10]; //values for each individual level (usually 5 levels exists)
	
	Pattern patternGame = Pattern.compile(" \\*\\* Playing game [a-zA-Z0-9/\\._;,]+/([a-zA-Z0-9_]+)\\.txt, level [a-zA-Z0-9/_\\.;,]+/[a-zA-Z0-9_]+_[a-z]*([0-9]*).txt \\(([0-9]+)/[0-9]+\\) \\*\\*");
	Pattern patternResult = Pattern.compile("Result \\(1->win; 0->lose\\):([-]?[0-1]+), Score:([-]?[0-9]+\\.[0-9]+), timesteps:([0-9]+)");
	Pattern patternResult2 = Pattern.compile("Num interactions:([0-9]+), avatar interactions:([0-9]+), num_sprites:([0-9]+), num_sprites_added:([0-9]+), num_sprites_killed:([0-9]+), num_walls: ([0-9]+)");
	
	public GameData(){
		for (Field field: DataTypes.class.getDeclaredFields()) {
			try {
				Object dataTypeObj = field.get(DataTypes.class);
				gameValues.put(dataTypeObj.toString(), -1f);
				for (int i = 0; i < levelValues.length; i++) {
					levelValues[i] = new HashMap<String,Float>();
					levelValues[i].put(dataTypeObj.toString(), -1f);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean parseLine(String line) {
				
		Matcher matcher = patternGame.matcher(line);
		while (matcher.find()) {

			gameTitle = matcher.group(1);
		    LevelPlay lp = new LevelPlay();
		    levelsPlayed.add(lp);
		    currLP = lp;
		    
			currLP.levelNumber = Integer.parseInt(matcher.group(2));
			currLP.levelNumberTry = Integer.parseInt(matcher.group(3));
			numberLevelsPlayed++;
			
			return true;
		}
		
		matcher = patternResult.matcher(line);
		while (matcher.find()) {
			currLP.won = Integer.parseInt(matcher.group(1));
			currLP.score = Float.parseFloat(matcher.group(2));
			currLP.timesteps = Integer.parseInt(matcher.group(3));
			
			return true;
		}
		
		
		matcher = patternResult2.matcher(line);
		while (matcher.find()) {
			currLP.numInteractions = Integer.parseInt(matcher.group(1));
			currLP.numAvatarInteractions = Integer.parseInt(matcher.group(2));
			currLP.numSprites = Integer.parseInt(matcher.group(3));
			currLP.numSpritesAdded = Integer.parseInt(matcher.group(4));
			currLP.numSpritesKilled = Integer.parseInt(matcher.group(5));
			currLP.numWalls = Integer.parseInt(matcher.group(6));
			
			return true;
		}
		
		return false;	
	}
	
	public void parseActionFileLine(String line){
		Pattern patternSeed = Pattern.compile("([-]?[0-9]+)");
		Pattern patternAction = Pattern.compile("([A-Z_]+)");
		
		Matcher matcher = patternSeed.matcher(line);
		while (matcher.find()) {
			seed = Integer.parseInt(matcher.group(1));
		}
		
		matcher = patternAction.matcher(line);
		while (matcher.find()) {
			currLP.actionSeq.add(line);
		}
	}
	
	
	public void calculateDataForLevel(int levelNr){
		float min = Float.POSITIVE_INFINITY;
		float max = Float.NEGATIVE_INFINITY;
		float ave = 0;
		float sd = 0;
		float se = 0;
		float sum = 0;
		float sumsq = 0;
		
		float[] scoreArray = new float[levelsPlayed.size()];
		int count = 0;
		
		for (int i = 0; i < levelsPlayed.size(); i++) {
			LevelPlay lp = levelsPlayed.get(i);
			if (lp.levelNumber == levelNr){
				float score = lp.score;
				if (score < min) min = score;
				if (score > max) max = score;
				sum += score;
				sumsq += score * score;
				scoreArray[i] = lp.score;
				count++;
			}
		}
		
		Arrays.sort(scoreArray);
		float median;
		if (scoreArray.length % 2 == 0)
		    median = ((float)scoreArray[scoreArray.length/2] + (float)scoreArray[scoreArray.length/2 - 1])/2;
		else
		    median = (float) scoreArray[scoreArray.length/2];
		
		ave = sum / (float)count;
		double num = sumsq - (count * ave * ave);
        if (num < 0) {
            // avoids tiny negative numbers possible through imprecision
            num = 0;
        }
        sd = (float) Math.sqrt(num / (count - 1));
        se =   sd / (float)Math.sqrt(count);
		
		levelValues[levelNr].put(DataTypes.MIN, min);
		levelValues[levelNr].put(DataTypes.MAX, max);
		levelValues[levelNr].put(DataTypes.AVE, ave);
		levelValues[levelNr].put(DataTypes.MEDI, median);
		levelValues[levelNr].put(DataTypes.SD, sd);
		levelValues[levelNr].put(DataTypes.SE, se);
	}

	public void calculateActionEntropy() {
		int amountOfActionTypes = Utility.getAmountOfActionsInGame(gameTitle);

//		System.out.println("Current game: "+gameTitle);
		
		for (LevelPlay lp : levelsPlayed) {
			ArrayList<String> acts = lp.actionSeq;
			HashMap<String, Integer> actions = new HashMap<String, Integer>();
			actions.put("ACTION_NIL", 0);actions.put("ACTION_LEFT", 0);actions.put("ACTION_RIGHT", 0);
			actions.put("ACTION_UP", 0);actions.put("ACTION_DOWN", 0);actions.put("ACTION_USE", 0);
			
			int amountOfActions = 0;
			for (String string : acts) {
				actions.put(string, actions.get(string) + 1);
				amountOfActions++;
			}
			
			float H = 0;
//			System.out.println("Probs for try nr: " + lp.levelNumberTry);
			for (String string : actions.keySet()) {
				float actionProb = actions.get(string) / (float)amountOfActions;
				H += actionProb == 0 ? 0 : actionProb * Utility.logOfBase(2, actionProb);
//				System.out.println("Probability of " + string + ": " + actionProb);
			}
//			System.out.println("Amount of actions:" + amountOfActions);
			lp.actionEntropy = -H;
//			System.out.println("Entropy for playthrough: " + lp.actionEntropy);
		}
//		System.out.println();
		
	}
	

}

