package gamechanger.dataextractor;
import java.lang.reflect.Method;
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
	
	float min = -1;
	float max = -1;
	float ave = -1;
	float sd = -1;
	float se = -1;
	float sum = -1;
	float sumsq = -1;
	
	public int n = -1;
	
	public HashMap<String, Float> gameValues = new HashMap<String, Float>();
	


	public GameData(){
		gameValues.put("min", min);
		gameValues.put("max", max);
		gameValues.put("ave", ave);
		gameValues.put("sd", sd);
		gameValues.put("se", se);
		gameValues.put("sum", sum);
		gameValues.put("sumsq", sumsq);
	}

	public void parseLine(String line) {
		
//		System.out.println(line);
		
		Pattern patternGame = Pattern.compile(" \\*\\* Playing game examples/gridphysics/([a-zA-Z]+)\\.txt, level examples/gridphysics/[a-zA-Z]+_lvl([0-9]*).txt \\(([0-9]+)/[0-9]+\\) \\*\\*");
		Pattern patternResult = Pattern.compile("Result \\(1->win; 0->lose\\):([0-1]+), Score:([-]?[0-9]+\\.[0-9]+), timesteps:([0-9]+)");
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
