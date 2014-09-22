package gamechanger.dataextractor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ExtractGameData {

	public static void main(String[] args) {
		ExtractGameData rg = new ExtractGameData();
		
		
	}
	
//	String datafile = "gamedata/dontdietest2-2.txt";
	String dataFolder = "gamedata/mtcstest1/";
	
	public ExtractGameData(){
		
		ArrayList<GameData> gds = new ArrayList<GameData>();
		
		String dataFile = dataFolder + "gamedata.txt";
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(dataFile));
			String line;
			
		
			int gameNumber = 0;
			GameData lastGD = null;
			GameData currGD = null;
			int lastLevelNr = -1;
			int lastTryNr = -1;
			while ((line = br.readLine()) != null) {
				
				if (gds.size() <= gameNumber){
					GameData gd = new GameData();
					gds.add(gd);
					currGD = gd;
				}
				
				if (line.equals(" *********")){
//					System.out.println("Game " + gameNumber + " parsed");
					
					gameNumber++;
				}else{
					currGD.parseLine(line);
					int levelNr = currGD.currLP.levelNumber;
					int tryNr = currGD.currLP.levelNumberTry;
//					System.out.println(levelNr +" " + tryNr + " gd:" + currGD);
					if (levelNr != lastLevelNr || tryNr != lastTryNr){
						lastLevelNr = levelNr;
						lastTryNr = tryNr;
						readActionFile(currGD, gameNumber, levelNr, tryNr-1);
					}
				}
				lastGD = currGD;
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		WriteGameData(gds);


	}
	
	void readActionFile(GameData gd, int gameNr, int levelNr, int tryNr){
		
		//actions_game_1_level_0_0
		
		String actionFile = dataFolder + "actions_game_" + gameNr + "_level_" + levelNr + "_" + tryNr + ".txt";
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(actionFile));
			String line;
			
			while ((line = br.readLine()) != null) {
				gd.parseActionFileLine(line);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	void WriteGameData(ArrayList<GameData> gds){
		for (GameData gd : gds) {
			
		
			System.out.println(gd.gameTitle);
			System.out.println(gd.seed);
			System.out.println("Win:\tScore:\tTicks:\tLevel:\tTry:\tActions:");
			for (LevelPlay lp: gd.levelsPlayed) {
				System.out.println(lp.won + "\t" + lp.score + "\t" + lp.timesteps + "\t" + lp.levelNumber + "\t" + lp.levelNumberTry + "\t" + lp.actionSeq);
				
			}
			
			System.out.println("");
			
			for (String key : gd.gameValues.keySet()) {
				float val = gd.gameValues.get(key);
				System.out.println(key + "\t" + val);
			}
			System.out.println("n\t" + gd.n);
			System.out.println();
		}
	}
}
