package datanalysis.core;

import java.util.ArrayList;

public class LevelPlay{
	public int levelNumber = -1;
	public int levelNumberTry = -1;
	public int won = -1;
	public float score = Float.MIN_VALUE;
	public int timesteps = -1;
	public ArrayList<String> actionSeq = new ArrayList<String>();
	public float actionEntropy = -1;
	
	public LevelPlay(){
		
	}
}
