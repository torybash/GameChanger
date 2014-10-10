package dataextractor.core;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;


public class ExtractGameData {

//	public static void main(String[] args) {
//		ExtractGameData rg = new ExtractGameData();
//		
//		
//	}
	
	
	
	
//	String datafile = "gamedata/dontdietest2-3.txt";
//	String dataFolder = "gamedata/randomtest1-all/";
//	
//	boolean actionFiles = true;
	
	public ExtractGameData(){
		


//		WriteGameData(gds);
	}
	
	
	
	public static ArrayList<GameData> extractData(String data, boolean actionFiles){
		ArrayList<GameData> gds = new ArrayList<GameData>();
		
		String gameDataFile = data  + "gamedata.txt";;
			
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(gameDataFile));
			String line;
			
		
			int gameNumber = 0;
			GameData currGD = null;
			int lastLevelNr = -1;
			int lastTryNr = -1;
			while ((line = br.readLine()) != null) {
				if (gds.size() <= gameNumber){
					GameData gd = new GameData();
					gds.add(gd);
					currGD = gd;
				}
				
				if (line.equals(" *********")){		//Done with game
					currGD.calculateDataForGame();
					currGD.calculateDataForLevel(lastLevelNr);
					gameNumber++;
				}else{
					currGD.parseLine(line);
					int levelNr = currGD.currLP.levelNumber;
					int tryNr = currGD.currLP.levelNumberTry;
					if (levelNr != lastLevelNr && lastLevelNr >= 0){
						currGD.calculateDataForLevel(lastLevelNr);
					}
					if (levelNr != lastLevelNr || tryNr != lastTryNr){
						lastLevelNr = levelNr;
						lastTryNr = tryNr;
						if (actionFiles)readActionFile(data, currGD, gameNumber, levelNr, tryNr-1);
					}
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return gds;
	}
	
	
	
	
	
	private static void readActionFile(String dataFolder, GameData gd, int gameNr, int levelNr, int tryNr){
				
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
			e.printStackTrace();
		}
	}
		
	void writeGameData(ArrayList<GameData> gds){
		
		try {
			System.setOut(new PrintStream(new FileOutputStream("out.txt")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (GameData gd : gds) {
			
		
			System.out.println(gd.gameTitle);
//			System.out.println(gd.seed);
			System.out.println("Win:\tScore:\tTicks:\tLevel:\tTry:\tActions:");
			for (LevelPlay lp: gd.levelsPlayed) {
				System.out.println(lp.won + "\t" + lp.score + "\t" + lp.timesteps + "\t" + lp.levelNumber + "\t" + lp.levelNumberTry + "\t" + lp.actionSeq);
				
			}
			
//			System.out.println("");
			
//			for (String key : gd.gameValues.keySet()) {
//				float val = gd.gameValues.get(key);
//				System.out.println(key + "\t" + val);
//			}
//			System.out.println("n\t" + gd.n);
			System.out.println();
		}
	}
}
