package levelgenerator.core;

import java.util.Comparator;
import java.util.regex.Pattern;

import levelgenerator.map.LevelMap;
import fastVGDL.core.game.results.GameResults;

public class GameInfo{
	public GameResults results;;
	
//	public float score = 0;
//	public int timesteps = 0;
//	public int won = 0;
//	
//	public int actionCount = 0;
//	
//	public int interactions = 0;
//	public int sprites = 0;
	
//	public char[][] levelMap;
	public LevelMap levelMap;
	
	Pattern patternResult = Pattern.compile("Result \\(1->win; 0->lose\\):([-]?[0-1]+), Score:([-]?[0-9]+\\.[0-9]+), timesteps:([0-9]+)");

	public GameInfo(GameResults gr, LevelMap levelMap){
		
//		won = gr.won ? 1 : 0;
//		timesteps = gr.ticks;
//		actionCount = gr.actions;
//		interactions = gr.interactions;
//		sprites = gr.numberSprites;
		
		results = gr;
                    
                    this.levelMap = levelMap;
	}
	
//	public GameInfo(String dataFolder, LevelMap levelMap){
//		String gameDataPath = dataFolder + "gamedata.txt";
//		String actionFilePath = dataFolder + "actions.txt";
//		
//		this.levelMap = levelMap;
//		
//		BufferedReader br;
//		try {
//			br = new BufferedReader(new FileReader(gameDataPath));
//			String line;
//			while ((line = br.readLine()) != null) {
//				Matcher matcher = patternResult.matcher(line);
//				while (matcher.find()) {
//					won = Integer.parseInt(matcher.group(1));
//					score = Float.parseFloat(matcher.group(2));
//					timesteps = Integer.parseInt(matcher.group(3));
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			br = new BufferedReader(new FileReader(actionFilePath));
//			String line = br.readLine(); //<--Skip first line (seed)
//			while ((line = br.readLine()) != null) {
//				if (!line.contains("NIL")) actionCount++;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public String toString() {
		return results.toString();
//		return "Won: " + won + ", actions: " + actionCount + ", interactions: " + interactions + ", sprites: " + sprites;
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
    	
    	
//    	if (x.results.actions + x.results.numberSpritesMoved > y.results.actions + y.results.numberSpritesMoved){
//    		return -1;
//    	}
//    	if (x.results.actions + x.results.numberSpritesMoved < y.results.actions + y.results.numberSpritesMoved){
//    		return 1;
//    	}
    	
//    	if (x.results.numberSpritesMoved > y.results.numberSpritesMoved){
//    		return -1;
//    	}
//    	if (x.results.numberSpritesMoved < y.results.numberSpritesMoved){
//    		return 1;
//    	}
//    	
//    	
    	if (x.results.actions > y.results.actions){
    		return -1;
    	}
    	if (x.results.actions < y.results.actions){
    		return 1;
    	}
    	
    	if (x.results.numberSpritesMoved > y.results.numberSpritesMoved){
		return -1;
    	}
    	if (x.results.numberSpritesMoved < y.results.numberSpritesMoved){
    		return 1;
    	}
    	
    	
    	if (x.results.numSpritesHasInteracted > y.results.numSpritesHasInteracted){
    		return -1;
    	}
    	if (x.results.numSpritesHasInteracted < y.results.numSpritesHasInteracted){
    		return 1;
    	}
    	
    	
    	if (x.results.ticks > y.results.ticks){
    		return -1;
    	}
    	if (x.results.ticks < y.results.ticks){
    		return 1;
    	}
    	
//    	if (x.won > y.won){
//    		return -1;
//    	}
//        if (x.won < y.won){
//            return 1;
//        }
    	
//    	if (x.actionCount > y.actionCount){
//    		return -1;
//    	}
//    	if (x.actionCount < y.actionCount){
//    		return 1;
//    	}
    	
//    	if (x.interactions > y.interactions){
//    		return -1;
//    	}
//    	if (x.interactions < y.interactions){
//    		return 1;
//    	}
//    	
//    	if (x.timesteps > y.timesteps){
//    		return -1;
//    	}
//    	if (x.timesteps < y.timesteps){
//    		return 1;
//    	}
//    	
//    	if (x.score < y.score){
//    		return -1;
//    	}
//    	if (x.score > y.score){
//    		return 1;
//    	}
        return 0;
    }
}