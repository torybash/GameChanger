package dataextractor.core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GameData {
	public String gameTitle = "";
	public ArrayList<LevelPlay> levelsPlayed = new ArrayList<LevelPlay>();
	
	public int numberLevelsPlayed = 0;
	public LevelPlay currLP = null;
	
	public int seed = -1;
	
	public int n = -1;
	
	public HashMap<String, Float> gameValues = new HashMap<String, Float>();
	
	public HashMap<String, Float>[] levelValues = new HashMap[5];
	



	public GameData(){
		gameValues.put("min", -1f);
		gameValues.put("max", -1f);
		gameValues.put("ave", -1f);
		gameValues.put("sd", -1f);
		gameValues.put("se", -1f);
		gameValues.put("sum", -1f);
		gameValues.put("sumsq", -1f);
		gameValues.put("wrate", -1f);
		gameValues.put("avtic", -1f);
		gameValues.put("sdtic", -1f);
		
		for (int i = 0; i < levelValues.length; i++) {
			levelValues[i] = new HashMap<String,Float>();
			levelValues[i].put("min", -1f);
			levelValues[i].put("max", -1f);
			levelValues[i].put("ave", -1f);
			levelValues[i].put("sd", -1f);
			levelValues[i].put("se", -1f);
			levelValues[i].put("sum", -1f);
			levelValues[i].put("sumsq", -1f);
			levelValues[i].put("wrate", -1f);
			levelValues[i].put("avtic", -1f);
			levelValues[i].put("sdtic", -1f);
		}
	}

	public void parseLine(String line) {
		
		
//		Pattern patternGame = Pattern.compile(" \\*\\* Playing game examples/gridphysics/([a-zA-Z0-9_]+)\\.txt, level examples/gridphysics/[a-zA-Z]+_lvl([0-9]*).txt \\(([0-9]+)/[0-9]+\\) \\*\\*");
		Pattern patternGame = Pattern.compile(" \\*\\* Playing game [a-zA-Z0-9/\\.]+/([a-zA-Z0-9_]+)\\.txt, level [a-zA-Z0-9/\\.]+/[a-zA-Z0-9_]+_lvl([0-9]*).txt \\(([0-9]+)/[0-9]+\\) \\*\\*");
		Pattern patternResult = Pattern.compile("Result \\(1->win; 0->lose\\):([-]?[0-1]+), Score:([-]?[0-9]+\\.[0-9]+), timesteps:([0-9]+)");
		Pattern patternEndResult = Pattern.compile(" ([a-z]+)[ ]+= ([-]?[0-9]+[\\.]?[0-9]?+)[ ]*");
		
		Matcher matcher = patternGame.matcher(line);
		while (matcher.find()) {

			gameTitle = matcher.group(1);
		    LevelPlay lp = new LevelPlay();
		    levelsPlayed.add(lp);
		    currLP = lp;
		    
			currLP.levelNumber = Integer.parseInt(matcher.group(2));
			currLP.levelNumberTry = Integer.parseInt(matcher.group(3));
			numberLevelsPlayed++;
		}
		
		matcher = patternResult.matcher(line);
		while (matcher.find()) {
			currLP.won = Integer.parseInt(matcher.group(1));
			currLP.score = Float.parseFloat(matcher.group(2));
			currLP.timesteps = Integer.parseInt(matcher.group(3));
		}
		
		matcher = patternEndResult.matcher(line);
		while (matcher.find()) {
			String name = matcher.group(1);
			
			if (name.equals("n")){
				int val = Integer.parseInt(matcher.group(2));
				n = val;
			}else{
				float val = Float.parseFloat(matcher.group(2));
				gameValues.put(name, val);
			}
		}
		
				
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
			currLP.actionSeq += line + ",";
		}
		
	}
	
	public void calculateDataForGame(){
		float winrate = 0;
		float avetick = 0;
		float stdtick = 0;
		
		float ticksum = 0;
		float ticksumsq = 0;
		for (int i = 0; i < levelsPlayed.size(); i++) {
			LevelPlay lp = levelsPlayed.get(i);
			winrate += lp.won;
			ticksum += lp.timesteps;
			ticksumsq += lp.timesteps * lp.timesteps;
		}
		winrate /= levelsPlayed.size();
		
		//game ticks
		avetick = ticksum / (float)levelsPlayed.size();
		double num = ticksumsq - (levelsPlayed.size() * avetick * avetick);
        if (num < 0) {
            // avoids tiny negative numbers possible through imprecision
            num = 0;
        }
        stdtick = (float) Math.sqrt(num / (levelsPlayed.size() - 1));
        
        gameValues.put("wrate", winrate);
        gameValues.put("avtic", avetick);
        gameValues.put("sdtic", stdtick);
	}
	
	
	public void calculateDataForLevel(int level){
		
		float min = Float.POSITIVE_INFINITY;
		float max = Float.NEGATIVE_INFINITY;
		float ave = 0;
		float sd = 0;
		float se = 0;
		float sum = 0;
		float sumsq = 0;
		
		int count = 0;
		
		for (LevelPlay lp: levelsPlayed) {
			if (lp.levelNumber == level){
				
				float score = lp.score;
				if (score < min) min = score;
				if (score > max) max = score;
				sum += score;
				sumsq += score * score;
				
				
				
				count++;
			}
		}
		
		ave = sum / (float)count;
		double num = sumsq - (count * ave * ave);
        if (num < 0) {
            // avoids tiny negative numbers possible through imprecision
            num = 0;
        }
        // System.out.println("Num = " + num);
        sd = (float) Math.sqrt(num / (count - 1));
        se =   sd / (float)Math.sqrt(count);
		
		levelValues[level].put("min", min);
		levelValues[level].put("max", max);
		levelValues[level].put("ave", ave);
		levelValues[level].put("sd", sd);
		levelValues[level].put("se", se);
		levelValues[level].put("sum", sum);
		levelValues[level].put("sumsq", sumsq);
	}
}

class LevelPlay{
	public int levelNumber = -1;
	public int levelNumberTry = -1;
	public int won = -1;
	public float score = Float.MIN_VALUE;
	public int timesteps = -1;
	public String actionSeq = "";
	
	public LevelPlay(){
		
	}
}
