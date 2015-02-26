package dataanalysis.tools;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dataanalysis.controller.Controller;
import dataanalysis.core.GameData;


public class ExtractGameData {
	
	
	private static ArrayList<GameData> extractData(String data, boolean actionFiles){
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
//					currGD.calculateDataForGame();
					if (actionFiles) currGD.calculateActionEntropy();
					currGD.calculateDataForLevel(lastLevelNr);
					gameNumber++;
				}else{
					boolean succes = currGD.parseLine(line);
					
					if (succes){
						int levelNr = currGD.currLP.levelNumber;
						int tryNr = currGD.currLP.levelNumberTry;
						if (levelNr != lastLevelNr && lastLevelNr >= 0){
							currGD.calculateDataForLevel(lastLevelNr);
						}
						if (levelNr != lastLevelNr || tryNr != lastTryNr){
							lastLevelNr = levelNr;
							lastTryNr = tryNr;
							if (actionFiles){
//								System.out.println("-----reading actions for game " + gameNumber + ", level: " + levelNr + " try: " + tryNr);
//								System.out.println(currGD.gameTitle.split("_")[2]);
								if (currGD.gameTitle.contains("gen")){
									readActionFile(data, currGD, Integer.parseInt(currGD.gameTitle.split("_")[2]), levelNr, tryNr-1);
								}else{
									readActionFile(data, currGD, gameNumber, levelNr, tryNr-1);

								}
							}
							
						}
					}
				}
			}
			br.close();
		} catch (IOException e) {
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
		
	public static ArrayList<GameData[]> extractGameDatas(Controller[] controllers, boolean readActionFiles) {
		int n = controllers.length;
		ArrayList<GameData>[] gameDatas = new ArrayList[n];
		for (int c = 0; c < n; c++) {
//			System.out.println("-------EXTRACTING FOR CONTROLLER: " + controllers[c].name);
			gameDatas[c] = extractData(controllers[c].dataFolder +"/", readActionFiles);
		}
		ArrayList<GameData[]> result = new ArrayList<GameData[]>();
		for (int g = 0; g < gameDatas[0].size(); g++) {
			GameData[] list = new GameData[n];
			for (int c = 0; c < n; c++) {
				list[c] = gameDatas[c].get(g);
			}
			result.add(list);
		}
		return result;
	}
	
	public static ArrayList<GameData[]> extractMutatedGameDatas(Controller[] controllers, int numberMutations, boolean readActionFiles) {
		ArrayList<GameData>[][] datas = new ArrayList[numberMutations][];
		ArrayList<GameData[]> result = new ArrayList<GameData[]>();
		for (int m = 0; m < numberMutations; m++) { //for each mutation
			ArrayList<GameData>[] gameDatas = new ArrayList[controllers.length];
			for (int c = 0; c < controllers.length; c++) {	//for each controller
//				System.out.println("-------EXTRACTING FOR CONTROLLER: " + controllers[c].name + " - MUTATION: " + m);
				gameDatas[c] = extractData(controllers[c].dataFolder + m +"/", readActionFiles);
			}
			datas[m] = gameDatas;
		}
		
		for (int m = 0; m < numberMutations; m++) {
			for (int g = 0; g < datas[0][0].size(); g++) {
				GameData[] list = new GameData[controllers.length];
				for (int c = 0; c < controllers.length; c++) { //for each controller
					list[c] = datas[m][c].get(g);
				}
				result.add(list);
			}
		}
		return result;
	}
}
