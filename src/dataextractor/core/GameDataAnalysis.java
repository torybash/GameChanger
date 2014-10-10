package dataextractor.core;

import java.util.ArrayList;
import java.util.Arrays;

import core.game.Game;

public class GameDataAnalysis {
	public static void main(String[] args) {
		GameDataAnalysis gda = new GameDataAnalysis();
		
//		gda.analyzeDifference(new String[]{"gamedata/mcts200ticks_all", "gamedata/dontdie200ticks_all", "gamedata/ga200ticks_all", 
//				"gamedata/randomonestep200ticks_all", "gamedata/onestep200ticks_all", "gamedata/random200ticks_all"});
		
//		gda.analyzeDifference(new String[]{"gamedata/mcts200ticks_gengames", "gamedata/dontdie200ticks_gengames", "gamedata/ga200ticks_gengames", "gamedata/randomonestep200ticks_gengames", 
//				"gamedata/onestep200ticks_gengames", "gamedata/random200ticks_gengames"});
		
//		gda.analyzeDifference(new String[]{"gamedata/mcts_example_1_all/", "gamedata/random_example_1_all/"});
		
//		gda.analyzeMutationDifference(new String[]{"gamedata/mcts200ticks_mutation_", "gamedata/dontdie200ticks_mutation_", "gamedata/ga200ticks_mutation_", 
//				"gamedata/randomonestep200ticks_mutation_", "gamedata/onestep200ticks_mutation_", "gamedata/random200ticks_mutation_"}, 20);

//		gda.analyzeMutationAverages(new String[]{"gamedata/mcts200ticks_mutation_", "gamedata/dontdie200ticks_mutation_", "gamedata/ga200ticks_mutation_", 
//				"gamedata/randomonestep200ticks_mutation_", "gamedata/onestep200ticks_mutation_", "gamedata/random200ticks_mutation_"}, 20);

		
		gda.analyzeBestGamesDifference(new String[]{"gamedata/randomOneStep800ticks6acttime_gengames", "gamedata/random800ticks6acttime_gengames",
				"gamedata/doNothing800ticks6acttime_gengames"});
		
		
	}
	
	private void analyzeMutationAverages(String[] dataFolders, int numberMutations) {
		int n = dataFolders.length;
		
		ArrayList<GameData>[] gameDatas = new ArrayList[n*numberMutations];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < numberMutations; j++) {
				gameDatas[j + i *numberMutations] = ExtractGameData.extractData(dataFolders[i] + j +"/", false);
			}
		}
		
		
		ArrayList<GameData>[] aveGameDatas = new ArrayList[n];
		for (int k = 0; k < n; k++) {
			aveGameDatas[k] = new ArrayList<GameData>();
		}

		for (int i = 0; i < gameDatas[0].size(); i++) { //for each game orignal (aliens, boulderdash ..)
			GameData[] aveGds = new GameData[n];
			
			//Deny bad mutations
			boolean[] deniedMutations = new boolean[numberMutations];
			int amountMutationsDenied = 0;
			
			
			for (int k = 0; k < numberMutations; k++) { //for each mutation
				boolean allScoresEqual = true; 
				boolean hasDisqualified = false;
				float lastScore = -999999f;
				for (int j = 0; j < n; j++) { //for each controller

					for (LevelPlay lp : gameDatas[k + j * numberMutations].get(i).levelsPlayed) {
						if (lp.won < 0){
							hasDisqualified = true;
							break;
						}
						
						if (lastScore != -999999f && lp.score != lastScore){
							allScoresEqual = false;
						}
						lastScore = lp.score;
					}
				}
				if (allScoresEqual || hasDisqualified){
					deniedMutations[k] = true;
					amountMutationsDenied++;
				}
			}
			
			//Create aveGds values
			for (int j = 0; j < n; j++) {	//for each controller
				int count = numberMutations * gameDatas[j + 0 * numberMutations].get(i).levelsPlayed.size();
				
				float min = Float.POSITIVE_INFINITY;
				float max = Float.NEGATIVE_INFINITY;
				float sum = 0;
				float sumsq = 0;
				float ave = 0;
				float sd = 0;
				float se = 0;
				float sumtic = 0;
				float sumsqtic = 0;
				float avtic = 0;
				float sdtic = 0;
				float setic = 0;
				float wrate = 0;
				
				for (int k = 0; k < numberMutations; k++) { //for each mutation
					if (deniedMutations[k]) continue;
					
					GameData gd = gameDatas[k + j *numberMutations].get(i);
					
					if (deniedMutations[k]) continue;
					for (LevelPlay lp : gd.levelsPlayed) {
						sum += lp.score;
						sumsq += lp.score * lp.score;
						sumtic += lp.timesteps;
						sumsqtic += lp.timesteps * lp.timesteps;
						wrate += lp.won;
						if (lp.score > max) max = lp.score;
						if (lp.score < min) min = lp.score;
					}
				}
				
				count -= amountMutationsDenied * gameDatas[j + 0 * numberMutations].get(i).levelsPlayed.size();
				
				wrate /= (float)count;
				ave = sum / (float)count;
				avtic = sumtic / (float)count;
				
				double num = sumsq - (count * ave * ave);
		        if (num < 0) {
		            // avoids tiny negative numbers possible through imprecision
		            num = 0;
		        }
		        // System.out.println("Num = " + num);
		        sd = (float) Math.sqrt(num / (count - 1));
		        se = sd / (float)Math.sqrt(count);
		        
				num = sumsqtic - (count * avtic * avtic);
		        if (num < 0) {
		            // avoids tiny negative numbers possible through imprecision
		            num = 0;
		        }
		        // System.out.println("Num = " + num);
		        sdtic = (float) Math.sqrt(num / (count - 1));
		        setic = sdtic / (float)Math.sqrt(count);
		        
		        
				aveGds[j] = new GameData();
				aveGds[j].gameTitle = gameDatas[0].get(i).gameTitle + " -- mutations: " + (numberMutations-amountMutationsDenied);
				
				aveGds[j].gameValues.put("ave", ave); 
				aveGds[j].gameValues.put("max", max); 
				aveGds[j].gameValues.put("min", min); 
				aveGds[j].gameValues.put("sd", sd); 
				aveGds[j].gameValues.put("se", se); 
				aveGds[j].gameValues.put("wrate", wrate); 
				aveGds[j].gameValues.put("avtic", avtic); 
				aveGds[j].gameValues.put("sdtic", sdtic); 
		        
		        aveGameDatas[j].add(aveGds[j]);
			}
			
			
			

		}
		
		
		
		for (int i = 0; i < aveGameDatas[0].size(); i++) {  //For each game
			GameData[] gds = new GameData[n];			
			for (int j = 0; j < n; j++) {				//For each controller
				gds[j] = aveGameDatas[j].get(i);
			}

			compareGameDatas(gds, dataFolders);
		}
		
	}

	private boolean hasSameAverages(GameData[] gds) {
		float lastAve = 0;
		for (int i = 0; i < gds.length; i++) {
			float ave = gds[i].gameValues.get("ave");
			if (i > 0 && ave != lastAve) return false;
			lastAve = ave;
		}
		return true;
	}

	private boolean hasDisqualified(GameData[] gds) {
		for (int i = 0; i < gds.length; i++) {
			GameData gd = gds[i];
			if (gd.gameValues.get("min") <= -1000) return true;
		}
		return false;
	}

	private void analyzeBestGamesDifference(String[] dataFolders) {
		int n = dataFolders.length;
		ArrayList<GameData>[] gameDatas = new ArrayList[n];
		ArrayList<GameData>[] acceptedGameDatas = new ArrayList[n];
		
		for (int i = 0; i < n; i++) {
			gameDatas[i] = ExtractGameData.extractData(dataFolders[i] + "/", false);
			acceptedGameDatas[i] = new ArrayList<GameData>();
		}

		for (int i = 0; i < gameDatas[0].size(); i++) {  //For each game
			GameData[] gds = new GameData[n];			
			for (int j = 0; j < n; j++) {				//For each controller
				gds[j] = gameDatas[j].get(i);
			}
			
			boolean isAccepted = true;
			
			float lastAve = Float.MAX_VALUE;
			for (int j = 0; j < n; j++) {				//For each controller
				GameData gd = gds[j];
				if (gd.gameValues.get("min") <= -1000) isAccepted = false;
				if (j == 0 && gd.gameValues.get("sd") == 0) isAccepted = false;
				float ave = gd.gameValues.get("ave");
				if (ave > lastAve) isAccepted = false;
				lastAve = ave;
			}
			if (isAccepted){
				for (int j = 0; j < n; j++) {				//For each controller
					acceptedGameDatas[j].add(gds[j]);
				}
			}
		}
		
		
		for (int i = 0; i < acceptedGameDatas[0].size(); i++) {  //For each game
			GameData[] gds = new GameData[n];			
			for (int j = 0; j < n; j++) {				//For each controller
				gds[j] = acceptedGameDatas[j].get(i);
			}

			compareGameDatas(gds, dataFolders);
		}
	}
	
	
	
	
	
	public void analyzeDifference(String[] dataFolders){
		int n = dataFolders.length;
		ArrayList<GameData>[] gameDatas = new ArrayList[n];
		
		for (int i = 0; i < n; i++) {
			gameDatas[i] = ExtractGameData.extractData(dataFolders[i] + "/", false);
		}
		
		for (int i = 0; i < gameDatas[0].size(); i++) {  //For each game
			GameData[] gds = new GameData[n];			
			for (int j = 0; j < n; j++) {				//For each controller
				gds[j] = gameDatas[j].get(i);
			}
			compareGameDatas(gds, dataFolders);
		}
	}
	
	

	
	public void analyzeMutationDifference(String[] dataFolders, int numberMutations){
		
		int n = dataFolders.length;
		
		ArrayList<GameData>[] gameDatas = new ArrayList[n*numberMutations];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < numberMutations; j++) {
				gameDatas[j + i *numberMutations] = ExtractGameData.extractData(dataFolders[i] + j +"/", false);
			}
		}
		

		for (int i = 0; i < gameDatas[0].size(); i++) { //for each game orignal (aliens, boulderdash ..)
			
			for (int j = 0; j < numberMutations; j++) {
				GameData[] gds = new GameData[n];
				for (int k = 0; k < n; k++) {
					gds[k] = gameDatas[j + k *numberMutations].get(i);
				}
				compareGameDatas(gds, dataFolders);
			}
			
			
		}
	}

	private void compareGameDatas(GameData[] gds, String[] dataFolders) {
		int n = gds.length;
		System.out.println(gds[0].gameTitle);
		
		float[] averages = new float[n];
		float[] maxs = new float[n];
		float[] mins = new float[n];
		float[] stds = new float[n];
		float[] winrates = new float[n];
		float[] aveticks = new float[n];
		float[] stdticks = new float[n];

		for (int k = 0; k < n; k++) {
			GameData gd = gds[k];
			averages[k] = gd.gameValues.get("ave");
			maxs[k] = gd.gameValues.get("max");
			mins[k] = gd.gameValues.get("min");
			stds[k] = gd.gameValues.get("sd");
			winrates[k] = gd.gameValues.get("wrate");
			aveticks[k] = gd.gameValues.get("avtic");
			stdticks[k] = gd.gameValues.get("sdtic");
		}
		
		System.out.println("Agent:\tMean:\tMax:\tMin:\tStd:\tWinrate:\tAve tick:\tStd tick:\tRel. diff. from " + dataFolders[0].split("/")[1].split("\\d")[0] + ":");
		for (int k = 0; k < n; k++) {
			String agentName = dataFolders[k].split("/")[1].split("\\d")[0];
//			float denominator = (Math.abs(averages[j])+Math.abs(averages[0]))/2f;
			float denominator = Math.max(Math.abs(averages[k]), Math.abs(averages[0]));
			float advantage = averages[0]-averages[k] == 0 ? 0 : (averages[0]-averages[k]) / denominator;
			String dataString = agentName + "\t" + averages[k] + "\t" + maxs[k] + "\t" + mins[k] + "\t" + stds[k] + "\t" + winrates[k] + "\t" + aveticks[k] + "\t" + stdticks[k];
			if (k>0) dataString += "\t" + advantage;
			
			System.out.println(dataString);
//			System.out.println(Arrays.toString(wins[k]));
//			System.out.println(Arrays.toString(ticks[k]));

		}
		System.out.println();
	}
	

}


