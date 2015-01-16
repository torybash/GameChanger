package dataanalysis.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import dataanalysis.controller.Controller;
import dataanalysis.fitness.FitnessCalculator;
import dataanalysis.fitness.GameFitness;
import dataanalysis.tools.ExtractGameData;
import dataanalysis.tools.GameDataCalculator;

public class GameDataAnalysis {
	
	


	static double[] manualWeights = new double[]{-0.4153384388856199, 0.32268699980710247, 0.21596321809594157, 0.06460745649175328, -0.0040225842971198245, 0.7554545968139259, -0.45903570728671733, 0.845139862853348, -1.0, -0.1168660110256575, 0.9324833361722796, 0.1027779947879512, 0.7028153739118276, -0.013611602864438813};










	public void analyzeAllGamesAverage(Controller[] controllers, boolean readActionFiles) {
		int n = controllers.length;		
		//Extract data from data folders
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractGameDatas(controllers, readActionFiles);
		//Don't accept bad games
		ArrayList<GameData[]> acceptedGameDatas = GameDataCalculator.getAcceptedGames(gameDatas);
		//Generate average gamedatas:
		GameData[] aveGameDatas = GameDataCalculator.getAverageGameDatas(acceptedGameDatas, controllers[0].dataFolder.split("_")[1] + " average");		
		//Compare average gamedatas:
		printGameDatas(aveGameDatas, controllers);
	}
	
	public void analyzeAllMutationsAverage(Controller[] controllers, int numberMutations, boolean readActionFiles) {
		int n = controllers.length;
		//Extract data from data folders
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractMutatedGameDatas(controllers, numberMutations, readActionFiles);
		//Don't accept bad games
		ArrayList<GameData[]> acceptedGameDatas = GameDataCalculator.getAcceptedGames(gameDatas);
		//Generate average gamedatas:
		GameData[] aveGameDatas = GameDataCalculator.getAverageGameDatas(acceptedGameDatas, "all mutations average");
		//Compare average gamedatas:
		printGameDatas(aveGameDatas, controllers);
	}
	
	public void analyzeEachMutationAverage(String[] dataFolders, int numberMutations) {
		//TODO
	}
	
		
	public void analyzeGameDifference(Controller[] controllers, boolean readActionFiles, boolean acceptAll){
		int n = controllers.length;
		//Extract data from data folders
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractGameDatas(controllers, readActionFiles);		
		if (!acceptAll) gameDatas = GameDataCalculator.getAcceptedGames(gameDatas);

		//Print game idxs:
//		String line = "{";
//		for (GameData[] gds : gameDatas) {
//			line += gds[0].gameTitle.split("_")[2].split(" ")[0] + ", ";
//		}
//		line += "}";
//		System.out.println(line);
		
		ArrayList<GameData[]> gameAverages = GameDataCalculator.getAverageForEachGame(gameDatas);
		for (GameData[] aveGameDatas : gameAverages) {
			printGameDatas(aveGameDatas, controllers);
		}
	}
	

	public void analyzeMutationDifference(Controller[] controllers, int numberMutations, boolean readActionFiles, boolean acceptAll){
		int n = controllers.length;
		//Extract data from data folders
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractMutatedGameDatas(controllers, numberMutations, readActionFiles);
		if (!acceptAll) gameDatas = GameDataCalculator.getAcceptedGames(gameDatas);
		
		ArrayList<GameData[]> gameAverages = GameDataCalculator.getAverageForEachGame(gameDatas);
		
		for (GameData[] aveGameDatas : gameAverages) {
			printGameDatas(aveGameDatas, controllers);
		}
	}

	private void printGameDatas(GameData[] gds, Controller[] controllers) {
		int n = gds.length;
		
		LinkedHashMap<String, Boolean> valShow = new LinkedHashMap<String, Boolean>();
		valShow.put(DataTypes.AVE, 		true);
		valShow.put(DataTypes.SD, 		true);
		valShow.put(DataTypes.NAVE, 	false);
		valShow.put(DataTypes.ZNAVE, 	false);
		valShow.put(DataTypes.MMAVE, 	true);
		valShow.put(DataTypes.MMSD, 	false);
		valShow.put(DataTypes.MMSE, 	true);
		valShow.put(DataTypes.WRATE, 	true);
		valShow.put(DataTypes.WRSD, 	false);
		valShow.put(DataTypes.WRSE, 	true);
		valShow.put(DataTypes.MEDI, 	false);
		valShow.put(DataTypes.QUAR1, 	false);
		valShow.put(DataTypes.QUAR3, 	false);
		valShow.put(DataTypes.ERRLO, 	false);
		valShow.put(DataTypes.ERRHI, 	false);
		valShow.put(DataTypes.MAX, 		false);
		valShow.put(DataTypes.MIN, 		false);
		valShow.put(DataTypes.AVTIC, 	true);
		valShow.put(DataTypes.SDTIC, 	false);
		valShow.put(DataTypes.ACTEN, 	true);
		
		
		int nrValuesToShow = 1;
		for (String stringKey : valShow.keySet()) {
			if (valShow.get(stringKey)) nrValuesToShow++;
		}
		
		String infoLine = "Agent:\t";
		String[] info = new String[nrValuesToShow]; info[0] = "Agent"; int cnt = 1;
		for (String s : valShow.keySet()) {
			if (valShow.get(s)){
				infoLine += s + ":\t";
				info[cnt] = s; cnt++;
			}
		}
		String[] valLines = new String[n];
		String[][] values = new String[n][nrValuesToShow];
		for (int c = 0; c < n; c++) {
			cnt = 1;
			valLines[c] = "";
			valLines[c] += controllers[c].name + "\t";
			HashMap<String, Float> gameVals = gds[c].gameValues;
			for (String s : valShow.keySet()) {
				if (valShow.get(s)){
					valLines[c] += gameVals.get(s) + "\t";
					values[c][cnt] = ""+gameVals.get(s);
				}
			}
		}
		
		System.out.println(gds[0].gameTitle);
		System.out.println(infoLine);
		for (int c = 0; c < n; c++) System.out.println(valLines[c]);
		System.out.println();
	}


}


