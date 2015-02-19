package dataanalysis.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.rmi.CORBA.Util;

import util.Utility;

import dataanalysis.controller.Controller;
import dataanalysis.controller.Controller.ControllerType;
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
        
        
        
    public void analyzeGoodToBad(Controller[] ctrlGameDatas1, Controller[] ctrlGameDatas2) {
        ArrayList<GameData[]> gameDatas1 = ExtractGameData.extractGameDatas(ctrlGameDatas1, false);
        ArrayList<GameData[]> gameDatas2 = ExtractGameData.extractGameDatas(ctrlGameDatas2, false);
        
        ArrayList<GameData[]> acceptedGameDatas1 = GameDataCalculator.getAcceptedGames(gameDatas1);
        ArrayList<GameData[]> acceptedGameDatas2 = GameDataCalculator.getAcceptedGames(gameDatas2);
        
        GameDataCalculator.getGoodToBadValues(acceptedGameDatas1, acceptedGameDatas2);
        
        
        //Compare average gamedatas:
//        printGameDatas(aveGameDatas, controllers);
    }
        

	public static void printGameDatas(GameData[] gds, Controller[] controllers) {
		int n = gds.length;
		
//		String seperator = ","; //"\t" 
		String seperator = "\t";
		
		LinkedHashMap<String, Boolean> valShow = new LinkedHashMap<String, Boolean>();
		valShow.put(DataTypes.AVE, 		true);
		valShow.put(DataTypes.SD, 		true);
		valShow.put(DataTypes.SE, 		true);
		valShow.put(DataTypes.NAVE, 	false);
		valShow.put(DataTypes.ZNAVE, 	false);
		valShow.put(DataTypes.MMAVE, 	true);
		valShow.put(DataTypes.MMSD, 	true);
		valShow.put(DataTypes.MMSE, 	true);
		valShow.put(DataTypes.WRATE, 	true);
		valShow.put(DataTypes.WRSD, 	false);
		valShow.put(DataTypes.WRSE, 	true);
		valShow.put(DataTypes.MEDI, 	true);
		valShow.put(DataTypes.QUAR1, 	true);
		valShow.put(DataTypes.QUAR3, 	true);
		valShow.put(DataTypes.ERRLO, 	false);
		valShow.put(DataTypes.ERRHI, 	false);
		valShow.put(DataTypes.MAX, 		true);
		valShow.put(DataTypes.MIN, 		true);
		valShow.put(DataTypes.AVTIC, 	true);
		valShow.put(DataTypes.SDTIC, 	true);
		valShow.put(DataTypes.SETIC, 	true);
		valShow.put(DataTypes.ACTEN, 	true);
		valShow.put(DataTypes.ACTSE, 	true);
		
		valShow.put(DataTypes.NINT, 	false);
		valShow.put(DataTypes.NAINT, 	false);
		valShow.put(DataTypes.NSPR, 	false);
		valShow.put(DataTypes.NSPRA, 	false);
		valShow.put(DataTypes.NSPRK, 	false);
		valShow.put(DataTypes.NWALS, 	false);

		
		int nrValuesToShow = 1;
		for (String stringKey : valShow.keySet()) {
			if (valShow.get(stringKey)) nrValuesToShow++;
		}
		
		String infoLine = "Agent:" + seperator; 
		String[] info = new String[nrValuesToShow]; info[0] = "Agent"; int cnt = 1;
		for (String s : valShow.keySet()) {
			if (valShow.get(s)){
				infoLine += s + ":" + seperator;
				info[cnt] = s; cnt++;
			}
		}
		String[] valLines = new String[n];
		String[][] values = new String[n][nrValuesToShow];
		for (int c = 0; c < n; c++) {
			cnt = 1;
			valLines[c] = "";
			valLines[c] += controllers[c].name + seperator;
			HashMap<String, Float> gameVals = gds[c].gameValues;
			for (String s : valShow.keySet()) {
				if (valShow.get(s)){
					valLines[c] += gameVals.get(s) + seperator;
					values[c][cnt] = ""+gameVals.get(s);
				}
			}
		}
		
		System.out.println(gds[0].gameTitle);
		System.out.println(infoLine);
		for (int c = 0; c < n; c++) System.out.println(valLines[c]);
		System.out.println();
	}

	
	
	public void printWellFormedGamesIdx(Controller[] controllers) {
		int n = controllers.length;
		//Extract data from data folders
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractGameDatas(controllers, false);

		ArrayList<GameData[]> gameAverages = GameDataCalculator.getAverageForEachGame(gameDatas);

//		for (GameData[] gds : gameAverages) {
//			printGameDatas(gds, controllers);
//		}
		
		ArrayList<GameData[]> wellFormedGameAverages = new ArrayList<GameData[]>();
		
		String result = "{";
		boolean first = false;
		for (GameData[] gds : gameAverages) {
			GameData gd = gds[0];

			if (gd.gameValues.get(DataTypes.AVE) < 0) continue;
			if (gd.gameValues.get(DataTypes.AVE) > 100) continue;
			if (gd.gameValues.get(DataTypes.MIN) < -50) continue;
			if (gd.gameValues.get(DataTypes.MAX) > 500) continue;
			if ((gd.gameValues.get(DataTypes.MAX) - gd.gameValues.get(DataTypes.MIN)) > 50) continue;
			if (gd.gameValues.get(DataTypes.WRATE) == 0) continue;
			if (gd.gameValues.get(DataTypes.WRATE) == 1) continue;
			if (gd.gameValues.get(DataTypes.SDTIC) == 0) continue;
			if (gd.gameValues.get(DataTypes.SD) < 1) continue;
						
			if (first) result += ", ";
			result += gd.gameTitle.split("_")[2].split(" ")[0];
			first = true;
			
			wellFormedGameAverages.add(gds);
			
		}
		result += "}";
		
		
		
		System.out.println(result);
		
		for (GameData[] gds : wellFormedGameAverages) {
			printGameDatas(gds, controllers);
		}
	}

	public void countGamesHaveCondition(Controller[] controllers, boolean readActionFiles) {
		int n = controllers.length;		
		//Extract data from data folders
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractGameDatas(controllers, readActionFiles);
		//Don't accept bad games
		gameDatas = GameDataCalculator.getAcceptedGames(gameDatas);

		ArrayList<GameData[]> gameAverages = GameDataCalculator.getAverageForEachGame(gameDatas);

		
		for (int c1 = 0; c1 < controllers.length; c1++) {
			for (int c2 = 0; c2 < controllers.length; c2++) {
				if (c1 >= c2) continue;
				for (int t = 0; t < DataTypes.class.getFields().length; t++) {
					try {
						countGamesHaveCondition(controllers, gameAverages, (String) DataTypes.class.getFields()[t].get(new String()), c1, c2);
					} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}


	public int countGamesHaveCondition(Controller[] controllers, ArrayList<GameData[]> gameAverages, String dataTyp, int ctrl1Id, int ctrl2Id) {
		int count = 0;
		for (GameData[] gds : gameAverages) { // fore game
			float ctrl1Val = gds[ctrl1Id].gameValues.get(dataTyp);
			float ctrl2Val = gds[ctrl2Id].gameValues.get(dataTyp);
			if (ctrl1Val >= ctrl2Val) count++;
		}
//		System.out.println(count + " games (out of " + gameAverages.size() + ") fulfil the condition: " + dataTyp + " " + controllers[ctrl1Id].name + ">" + controllers[ctrl2Id].name);
		
		return count;
	}
	
	public float getAveragedRelativeDifference(Controller[] controllers, ArrayList<GameData[]> gameAverages, String dataTyp, int ctrl1Id, int ctrl2Id) {
		float fitness = 0;
		for (GameData[] gds : gameAverages) { // fore game
			float ctrl1Val = gds[ctrl1Id].gameValues.get(dataTyp);
			float ctrl2Val = gds[ctrl2Id].gameValues.get(dataTyp);


			fitness += Utility.relDiff(ctrl1Val, ctrl2Val);
		}
		
		fitness /= gameAverages.size();
//		System.out.println(count + " games (out of " + gameAverages.size() + ") fulfil the condition: " + dataTyp + " " + controllers[ctrl1Id].name + ">" + controllers[ctrl2Id].name);
		
		return fitness;
	}


	public void makeFeatureTypeCountCSV(Controller[] designedDataControllers, Controller[] mutatedDataControllers, Controller[] generatedDataControllers) {
		String result = "datatype,count1,aveRelDiff1,count2,aveRelDiff2,count3,aveRelDiff3\n";
		
		ArrayList<GameData[]> designedGameDatas = ExtractGameData.extractGameDatas(designedDataControllers, false);
		designedGameDatas = GameDataCalculator.getAcceptedGames(designedGameDatas);
		ArrayList<GameData[]> mutatedGameDatas = ExtractGameData.extractMutatedGameDatas(mutatedDataControllers, 10, false);
		mutatedGameDatas = GameDataCalculator.getAcceptedGames(mutatedGameDatas);
		ArrayList<GameData[]> generatedGameDatas = ExtractGameData.extractGameDatas(generatedDataControllers, false);
		generatedGameDatas = GameDataCalculator.getAcceptedGames(generatedGameDatas);

		ArrayList<GameData[]> designedGameAverages = GameDataCalculator.getAverageForEachGame(designedGameDatas);
		ArrayList<GameData[]> mutatedGameAverages = GameDataCalculator.getAverageForEachGame(mutatedGameDatas);
		ArrayList<GameData[]> generatedGameAverages = GameDataCalculator.getAverageForEachGame(generatedGameDatas);
		
		
		String[] datTyps = new String[]{DataTypes.AVE, DataTypes.WRATE, DataTypes.MEDI, DataTypes.QUAR1, DataTypes.QUAR3, DataTypes.MIN, DataTypes.MAX};
		
		int[] ctrl1s = new int[]{0};
		int[] ctrl2s = new int[]{4,5,6};
		
		int totDesCount = designedGameAverages.size();
		int totMutCount = mutatedGameAverages.size();
		int totGenCount = generatedGameAverages.size();
		
		for (int t = 0; t < datTyps.length; t++) {
			for (int c1 = 0; c1 < ctrl1s.length; c1++) {
				for (int c2 = 0; c2 < ctrl2s.length; c2++) {
					int desCount = countGamesHaveCondition(designedDataControllers, designedGameAverages, datTyps[t], ctrl1s[c1], ctrl2s[c2]);
					int mutCount = countGamesHaveCondition(mutatedDataControllers, mutatedGameAverages, datTyps[t], ctrl1s[c1], ctrl2s[c2]);
					int genCount = countGamesHaveCondition(generatedDataControllers, generatedGameAverages, datTyps[t], ctrl1s[c1], ctrl2s[c2]);
					
					float desAveRelDiff = getAveragedRelativeDifference(designedDataControllers, designedGameAverages,  datTyps[t], ctrl1s[c1], ctrl2s[c2]);
					float mutAveRelDiff = getAveragedRelativeDifference(mutatedDataControllers, mutatedGameAverages,  datTyps[t], ctrl1s[c1], ctrl2s[c2]);
					float genAveRelDiff = getAveragedRelativeDifference(generatedDataControllers, generatedGameAverages,  datTyps[t], ctrl1s[c1], ctrl2s[c2]);
					
					result += "("+designedDataControllers[ctrl1s[c1]].name + ">=" + designedDataControllers[ctrl2s[c2]].name + ")." + datTyps[t] + ",";
					
					result += desCount + " (of " + totDesCount + "),";
					result += String.format("%.3f", desAveRelDiff) + ",";
//					result += String.format("%.1f", 100*desCount/(float)totDesCount) + ",";
					result += mutCount + " (of " + totMutCount + "),";
					result += String.format("%.3f", mutAveRelDiff) + ",";
//					result += String.format("%.1f", 100*mutCount/(float)totMutCount) + ",";
					result += genCount + " (of " + totGenCount + "),";
					result += String.format("%.3f", genAveRelDiff);
//					result += String.format("%.1f", 100*genCount/(float)totGenCount) + "";
					result += "\n";
				}
			}
		}
		
		
		
		System.out.println(result);
	}




}


