package dataanalysis.core;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class LevelPlay{
	public int levelNumber = -1;
	public int levelNumberTry = -1;
	public int won = -1;
	public float score = Float.MIN_VALUE;
	public int timesteps = -1;
	public ArrayList<String> actionSeq = new ArrayList<String>();
	public float actionEntropy = -1;
	
	public int numInteractions = -1;
	public int numAvatarInteractions = -1;
	public int numSprites = -1;
	public int numSpritesAdded = -1;
	public int numSpritesKilled = -1;
	public int numWalls = -1;
	
	Pattern patternResult2 = Pattern.compile("Num interactions:([0-9]+), avatar interactions:([0-9]+), num_sprites:([0-9]+), num_sprites_added:([0-9]+), num_sprites_killed:([0-9]+), num_walls: ([0-9]+)");

	
	public LevelPlay(){
		
	}
}
