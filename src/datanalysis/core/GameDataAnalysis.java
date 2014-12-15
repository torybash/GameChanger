package datanalysis.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import util.Utility;
import datanalysis.core.FitnessCalculator.FitnessType;

public class GameDataAnalysis {

	private final int LOWEST_TICKS_ALLOWED = 10;
	private final int MAX_ABS_SCORE = 10000;
	
	public static void main(String[] args) {
		GameDataAnalysis gda = new GameDataAnalysis();
		
		//*********************************
		//**********2000 TICKS TEST********
		//*********************************
		//Example games show data (200 ticks test)
//		gda.analyzeGameDifference(new String[]{"gamedata/dontdie2000ticks_all", "gamedata/mcts2000ticks_all", "gamedata/ga2000ticks_all", 
//				"gamedata/randomonestep2000ticks_all", "gamedata/onestep2000ticks_all", "gamedata/random2000ticks_all", "gamedata/doNothing2000ticks_all"});
//		gda.analyzeAllGamesAverage(new String[]{"gamedata/dontdie2000ticks_all", "gamedata/mcts2000ticks_all", "gamedata/ga2000ticks_all", 
//				"gamedata/randomonestep2000ticks_all", "gamedata/onestep2000ticks_all", "gamedata/random2000ticks_all", "gamedata/doNothing2000ticks_all"}, false);
//		gda.analyzeFitness(new String[]{"gamedata/dontdie2000ticks_all", "gamedata/mcts2000ticks_all", "gamedata/ga2000ticks_all", 
//				"gamedata/randomonestep2000ticks_all", "gamedata/onestep2000ticks_all", "gamedata/random2000ticks_all", "gamedata/doNothing2000ticks_all"});

		
		//*********************************
		//**********200 TICKS TEST*********
		//*********************************
//		gda.analyzeGameDifference(new String[]{"gamedata/dontdie200ticks_all", "gamedata/mcts200ticks_all", "gamedata/ga200ticks_all", 
//				"gamedata/randomonestep200ticks_all", "gamedata/onestep200ticks_all", "gamedata/random200ticks_all", "gamedata/doNothing200ticks_all"});
//		gda.analyzeGameDifference(new String[]{"gamedata/dontdie200ticks_all2", "gamedata/mcts200ticks_all2", "gamedata/ga200ticks_all2", "gamedata/MCTSish200ticks_all2", 
//				"gamedata/randomonestep200ticks_all2", "gamedata/onestep200ticks_all2", "gamedata/random200ticks_all2", "gamedata/doNothing200ticks_all2"});
		//Mutated games show data (200 ticks test)
//		gda.analyzeMutationDifference(new String[]{"gamedata/dontdie200ticks_mutation_", "gamedata/mcts200ticks_mutation_", "gamedata/ga200ticks_mutation_", 
//				"gamedata/randomonestep200ticks_mutation_", "gamedata/onestep200ticks_mutation_", "gamedata/random200ticks_mutation_", "gamedata/doNothing200ticks_mutation_"}, 20);
		//Generated games show data (200 ticks test)
//		gda.analyzeGameDifference(new String[]{"gamedata/dontdie200ticks_gengames", "gamedata/mcts200ticks_gengames", "gamedata/ga200ticks_gengames", 
//				"gamedata/randomonestep200ticks_gengames", "gamedata/onestep200ticks_gengames", "gamedata/random200ticks_gengames", "gamedata/doNothing200ticks_gengames"});		

		//Example games average show data (200 ticks test)
//		gda.analyzeAllGamesAverage(new String[]{"gamedata/dontdie200ticks_all", "gamedata/mcts200ticks_all", "gamedata/ga200ticks_all", 
//				"gamedata/randomonestep200ticks_all", "gamedata/onestep200ticks_all", "gamedata/random200ticks_all", "gamedata/doNothing200ticks_all"}, false);
//		gda.analyzeAllGamesAverage(new String[]{"gamedata/dontdie200ticks_all2", "gamedata/mcts200ticks_all2", "gamedata/ga200ticks_all2", 
//				"gamedata/randomonestep200ticks_all2", "gamedata/onestep200ticks_all2", "gamedata/random200ticks_all2", "gamedata/doNothing200ticks_all2"}, false);
		//Mutated games average show data (200 ticks test)
//		gda.analyzeEachMutationAverage(new String[]{"gamedata/dontdie200ticks_mutation_", "gamedata/mcts200ticks_mutation_", "gamedata/ga200ticks_mutation_", 
//				"gamedata/randomonestep200ticks_mutation_", "gamedata/onestep200ticks_mutation_", "gamedata/random200ticks_mutation_", "gamedata/doNothing200ticks_mutation_"}, 20);
		//Generated games average show data (200 ticks test)
//		gda.analyzeAllMutationsAverage(new String[]{"gamedata/dontdie200ticks_mutation_", "gamedata/mcts200ticks_mutation_", "gamedata/ga200ticks_mutation_", 
//				"gamedata/randomonestep200ticks_mutation_", "gamedata/onestep200ticks_mutation_", "gamedata/random200ticks_mutation_", "gamedata/doNothing200ticks_mutation_"}, 20);
//		gda.analyzeAllGamesAverage(new String[]{"gamedata/dontdie200ticks_gengames", "gamedata/mcts200ticks_gengames", "gamedata/ga200ticks_gengames", "gamedata/randomonestep200ticks_gengames", 
//				"gamedata/onestep200ticks_gengames", "gamedata/random200ticks_gengames", "gamedata/doNothing200ticks_gengames"}, false);

		
//		gda.findStrangeGames(new String[]{"gamedata/mcts200ticks_gengames", "gamedata/dontdie200ticks_gengames", "gamedata/ga200ticks_gengames", "gamedata/randomonestep200ticks_gengames", 
//				"gamedata/onestep200ticks_gengames", "gamedata/random200ticks_gengames"}, new boolean[]{true,false,false,false,false});
		
//		gda.findStrangeMutatedGames(new String[]{"gamedata/mcts200ticks_mutation_", "gamedata/dontdie200ticks_mutation_", "gamedata/ga200ticks_mutation_", 
//				"gamedata/randomonestep200ticks_mutation_", "gamedata/onestep200ticks_mutation_", "gamedata/random200ticks_mutation_"}, 20, new boolean[]{true,true,false,false,false});
		
		
		
//		gda.analyzeFitness(new String[]{"gamedata/dontdie200ticks_all", "gamedata/mcts200ticks_all", "gamedata/ga200ticks_all", 
//				"gamedata/randomonestep200ticks_all", "gamedata/onestep200ticks_all", "gamedata/random200ticks_all", "gamedata/doNothing200ticks_all"});
//		gda.analyzeFitness(new String[]{"gamedata/dontdie200ticks_all2", "gamedata/mcts200ticks_all2", "gamedata/ga200ticks_all2", 
//				"gamedata/randomonestep200ticks_all2", "gamedata/onestep200ticks_all2", "gamedata/random200ticks_all2", "gamedata/doNothing200ticks_all2"});
		gda.analyzeMutationsFitness(new String[]{"gamedata/dontdie200ticks_mutation_", "gamedata/mcts200ticks_mutation_", "gamedata/ga200ticks_mutation_", 
				"gamedata/randomonestep200ticks_mutation_", "gamedata/onestep200ticks_mutation_", "gamedata/random200ticks_mutation_", "gamedata/doNothing200ticks_mutation_"}, 20);
//		gda.analyzeFitness(new String[]{"gamedata/dontdie200ticks_gengames", "gamedata/mcts200ticks_gengames", "gamedata/ga200ticks_gengames", 
//				"gamedata/randomonestep200ticks_gengames", "gamedata/onestep200ticks_gengames", "gamedata/random200ticks_gengames", "gamedata/doNothing200ticks_gengames"});

		
//		gda.evolveFitnessWeights(new String[]{"gamedata/dontdie200ticks_all", "gamedata/mcts200ticks_all", "gamedata/ga200ticks_all", 
//				"gamedata/randomonestep200ticks_all", "gamedata/onestep200ticks_all", "gamedata/random200ticks_all", "gamedata/doNothing200ticks_all"},
//				new String[]{"gamedata/dontdie200ticks_gengames", "gamedata/mcts200ticks_gengames", "gamedata/ga200ticks_gengames", 
//				"gamedata/randomonestep200ticks_gengames", "gamedata/onestep200ticks_gengames", "gamedata/random200ticks_gengames", "gamedata/doNothing200ticks_gengames"});
		
		//*********************************
		//**********2 GAMES TEST***********
		//*********************************
//		gda.analyzeGameDifference(new String[]{"gamedata/2gamestest/dontDie2games_examples", "gamedata/2gamestest/sampleMCTS2games_examples", "gamedata/2gamestest/sampleGA2games_examples", 
//				"gamedata/2gamestest/randomOneStep2games_examples", "gamedata/2gamestest/sampleonesteplookahead2games_examples", "gamedata/2gamestest/sampleRandom2games_examples"});

//		gda.analyzeEachMutationAverage(new String[]{"gamedata/2gamestest/dontDie2games_mutation_", "gamedata/2gamestest/sampleMCTS2games_mutation_",  "gamedata/2gamestest/sampleGA2games_mutation_", 
//				"gamedata/2gamestest/randomOneStep2games_mutation_", "gamedata/2gamestest/sampleonesteplookahead2games_mutation_", "gamedata/2gamestest/sampleRandom2games_mutation_"}, 50);

//		gda.analyzeFitness(new String[]{"gamedata/2gamestest/dontDie2games_examples", "gamedata/2gamestest/sampleMCTS2games_examples", "gamedata/2gamestest/sampleGA2games_examples", 
//				"gamedata/2gamestest/randomOneStep2games_examples", "gamedata/2gamestest/sampleonesteplookahead2games_examples", "gamedata/2gamestest/sampleRandom2games_examples"});

		
		
		//**************************************
		//**********BEST GEN GAMES TEST*********
		//**************************************
//		gda.analyzeBestGamesDifference(new String[]{"gamedata/randomOneStep800ticks6acttime_gengames", "gamedata/random800ticks6acttime_gengames",
//				"gamedata/doNothing800ticks6acttime_gengames"});
		
//		gda.analyzeBestGamesDifference(new String[]{"gamedata/randomOneStep800ticksSimple_gengames", "gamedata/random800ticksSimple_gengames",
//		"gamedata/doNothing800ticksSimple_gengames"});
		

		
		
		
		//PUZZLE GAMES ANALYSIS
//		gda.analyzeMutationDifference(new String[]{"puzzlegamedata/sampleMCTS200ticks_mutation_", "puzzlegamedata/dontDie200ticks_mutation_", "puzzlegamedata/sampleGA200ticks_mutation_", 
//				"puzzlegamedata/randomOneStep200ticks_mutation_", "puzzlegamedata/sampleOneStepLookAhead200ticks_mutation_", "puzzlegamedata/sampleRandom200ticks_mutation_"}, 20);

//		gda.analyzeDifference(new String[]{"puzzlegamedata/sampleMCTS200ticks_all", "puzzlegamedata/dontDie200ticks_all", "puzzlegamedata/sampleGA200ticks_all", 
//		"puzzlegamedata/randomOneStep200ticks_all", "puzzlegamedata/sampleOneStepLookAhead200ticks_all", "puzzlegamedata/random200ticks_all"});
	
//		gda.analyzeMutationAverages(new String[]{"puzzlegamedata/sampleMCTS200ticks_mutation_", "puzzlegamedata/dontDie200ticks_mutation_", "puzzlegamedata/sampleGA200ticks_mutation_", 
//				"puzzlegamedata/randomOneStep200ticks_mutation_", "puzzlegamedata/sampleOneStepLookAhead200ticks_mutation_", "puzzlegamedata/sampleRandom200ticks_mutation_"}, 10);
	
	
		
		
//		gda.showScores(new String[]{"gamedata/mcts200ticks_gengames"});
//		gda.showScores(new String[]{"gamedata/mcts200ticks_gengames", "gamedata/dontdie200ticks_gengames", "gamedata/ga200ticks_gengames", "gamedata/randomonestep200ticks_gengames", 
//				"gamedata/onestep200ticks_gengames", "gamedata/random200ticks_gengames", "gamedata/doNothing200ticks_gengames"});
	}
	
	
	private void evolveFitnessWeights(String[] goodGamesDataFolders, String[] badGamesDataFolders) {
		ArrayList<GameData[]> goodGameDatas = ExtractGameData.extractGameDatas(goodGamesDataFolders, false);
		ArrayList<GameData[]> badGameDatas = ExtractGameData.extractGameDatas(badGamesDataFolders, false);
		
		goodGameDatas = getAcceptedGames(goodGameDatas);
		badGameDatas = getAcceptedGames(badGameDatas);
		
		ArrayList<GameData[]> goodGameAverages = getAverages(goodGameDatas);
		ArrayList<GameData[]> badGameAverages = getAverages(badGameDatas);
		
		Random r = new Random();
		int iterations = 1000, mutations = 1000, survivors = 20;
		double[][] weightsList = new double[mutations][];
		double[][] survivedWeightsList = new double[survivors][];
		ArrayList<GoodToBadFitness> gtbFitnessList = new ArrayList<GoodToBadFitness>(); 
		int[] bestIdxs = null; 
		for (int i = 0; i < iterations; i++) {
			System.out.println("---------ITERATION: " + i + "---------------------");
			
			gtbFitnessList.clear();
			
			//Create initial values (weights)
			if (i==0){
				for (int j = 0; j < mutations; j++) {
					weightsList[j] = new double[FitnessType.class.getEnumConstants().length];
					
					for (int j2 = 0; j2 < weightsList[j].length; j2++) {
						weightsList[j][j2] = r.nextDouble() * 2f - 1f;
						if (j == 0) weightsList[j][j2] = 1; //<--- one original inserted
					}
					
				}
			}
			
			
			//Calculate fitness and make lists
			for (int j = 0; j < mutations; j++) {
				ArrayList<GameFitness> goodFitnessValues = getGamesFitness(goodGameAverages, weightsList[j]);
				ArrayList<GameFitness> badFitnessValues = getGamesFitness(badGameAverages, weightsList[j]);
				gtbFitnessList.add(new GoodToBadFitness(j, goodFitnessValues, badFitnessValues));
			}
			
			//Pick best values (weights)
			Collections.sort(gtbFitnessList);
			for (int j = 0; j < survivors; j++) {
				survivedWeightsList[j] = weightsList[gtbFitnessList.get(j).idx].clone();
				System.out.println(Arrays.toString(survivedWeightsList[j]));
				System.out.println("GTB-Fitness: " + gtbFitnessList.get(j).gtbFitness + ", Top 5 good games fitness: ");
				for (int j2 = 0; j2 < 5; j2++) {
					System.out.println(gtbFitnessList.get(j).goodFitnessValues.get(j2).gameTitle + ": "+ gtbFitnessList.get(j).goodFitnessValues.get(j2).fitness + ", Best bad game fitness: " + gtbFitnessList.get(j).badFitnessValues.get(j2).gameTitle + ": "+gtbFitnessList.get(j).badFitnessValues.get(j2).fitness);
				}
			}
			
						
			//Mutate best values (weights)
			for (int j = 0; j < survivors; j++) {
				double[] weightsToMutate = survivedWeightsList[j];
				for (int j2 = 0; j2 < mutations/survivors; j2++) {
					
					if (j2==0){
						weightsList[j + j2 * survivors] = weightsToMutate.clone();
						continue; //<--one survivor remains!
					}
					
					double[] newWeights = new double[FitnessType.class.getEnumConstants().length];
					for (int k = 0; k < newWeights.length; k++) {
						double newVal = weightsToMutate[k] + ((r.nextGaussian() * 0.1));
						if (newVal > 1) newVal = 1; if (newVal < -1) newVal = -1;
						newWeights[k] = newVal;
					}
					weightsList[j + j2 * survivors] = newWeights;
				}
			}
		}
	}


	static double[] manualWeights = new double[]{-0.4153384388856199, 0.32268699980710247, 0.21596321809594157, 0.06460745649175328, -0.0040225842971198245, 0.7554545968139259, -0.45903570728671733, 0.845139862853348, -1.0, -0.1168660110256575, 0.9324833361722796, 0.1027779947879512, 0.7028153739118276, -0.013611602864438813};



	private void analyzeFitness(String[] dataFolders) {
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractGameDatas(dataFolders, false);
		gameDatas = getAcceptedGames(gameDatas);
		ArrayList<GameData[]> gameDatasAverages = getAverages(gameDatas);
		ArrayList<GameFitness> fitnessValues = getGamesFitness(gameDatasAverages, manualWeights);
		
		
		for (GameFitness gameFitness : fitnessValues) {
			System.out.println(gameFitness.gameTitle + " has fitness:\t" + gameFitness.fitness);
		}
	}
	



	private void analyzeMutationsFitness(String[] dataFolders, int numberMutations) {
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractMutatedGameDatas(dataFolders, numberMutations, false);
		gameDatas = getAcceptedGames(gameDatas);
		ArrayList<GameData[]> gameAverages = getAverages(gameDatas);
		ArrayList<GameFitness> fitnessValues = getGamesFitness(gameAverages, manualWeights);
		
		for (GameFitness gameFitness : fitnessValues) {
			System.out.println(gameFitness.gameTitle + " has fitness:\t" + gameFitness.fitness);
		}
	}

	private ArrayList<GameFitness> getGamesFitness(ArrayList<GameData[]> gameDatas, double[] featureWeights) {
		int n = gameDatas.get(0).length;

		FitnessCalculator.setWeights(featureWeights);
		ArrayList<GameFitness> fitnessValues = new ArrayList<GameFitness>();
		for (int g = 0; g < gameDatas.size(); g++) {  //For each game
			GameFitness gf = FitnessCalculator.calculateGameFitness(gameDatas.get(g));
			fitnessValues.add(gf);
		}
		
		Collections.sort(fitnessValues);
		
		return fitnessValues;
	}


	private ArrayList<GameData[]> getAverages(ArrayList<GameData[]> gameDatas) {
		int n = gameDatas.get(0).length;
		ArrayList<GameData[]> result = new ArrayList<GameData[]>();
		for (int g = 0; g < gameDatas.size(); g++) {  //For each game
			ArrayList<GameData[]> singleInputList = new ArrayList<GameData[]>();
			singleInputList.add(gameDatas.get(g));
			
			GameData[] aveGameDatas = getAverageGameDatas(singleInputList, gameDatas.get(g)[0].gameTitle + " average");
			result.add(aveGameDatas);
		}
		return result;
	}

	private void analyzeAllGamesAverage(String[] dataFolders, boolean readActionFiles) {
		int n = dataFolders.length;		
		//Extract data from data folders
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractGameDatas(dataFolders, readActionFiles);
		//Don't accept bad games
		ArrayList<GameData[]> acceptedGameDatas = getAcceptedGames(gameDatas);
		//Generate average gamedatas:
		GameData[] aveGameDatas = getAverageGameDatas(acceptedGameDatas, dataFolders[0].split("_")[1] + " average");		
		//Compare average gamedatas:
		compareGameDatas(aveGameDatas, dataFolders);
	}
	
	private void analyzeAllMutationsAverage(String[] dataFolders, int numberMutations) {
		int n = dataFolders.length;
		//Extract data from data folders
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractMutatedGameDatas(dataFolders, numberMutations, false);
		//Don't accept bad games
		ArrayList<GameData[]> acceptedGameDatas = getAcceptedGames(gameDatas);
		//Generate average gamedatas:
		GameData[] aveGameDatas = getAverageGameDatas(acceptedGameDatas, "all mutations average");
		//Compare average gamedatas:
		compareGameDatas(aveGameDatas, dataFolders);
	}
	
	private void analyzeEachMutationAverage(String[] dataFolders, int numberMutations) {
//		int n = dataFolders.length;
//		
//		ArrayList<GameData>[][] gameDatasListMutationsFirst = ExtractGameData.extractMutatedGameDatas(dataFolders, numberMutations);
//		int numberGames = gameDatasListMutationsFirst[0][0].size(); //amount of games
//		
//		//Flip list around / group by original games
//		ArrayList<GameData>[][] gameDatasList = new ArrayList[numberGames][];
//		for (int g = 0; g < numberGames; g++) {
//			ArrayList<GameData>[] gameDatas = new ArrayList[n];
//			for (int c = 0; c < n; c++) {
//				gameDatas[c] = new ArrayList<GameData>();
//				for (int m = 0; m < numberMutations; m++) {
//					gameDatas[c].add(gameDatasListMutationsFirst[m][c].get(g));
//				}
//			}
//			gameDatasList[g] = gameDatas;
//		}
//
//		ArrayList<GameData>[][] acceptedGameDatasList = new ArrayList[numberGames][];
//		
//		for (int g = 0; g < numberGames; g++) { //for each game orignal (aliens, boulderdash ..)
//			ArrayList<GameData>[] acceptedGameDatas = new ArrayList[n];
//			for (int c = 0; c < n; c++) acceptedGameDatas[c] = new ArrayList<GameData>();
//			for (int m = 0; m < numberMutations; m++) {
//				boolean acceptedGame = true; 
//				for (int c = 0; c < n; c++) { //for each controller
//					ArrayList<LevelPlay> levelsPlayed = gameDatasList[g][c].get(m).levelsPlayed;
//					if (hasDisqualified(levelsPlayed) 
//							|| allScoresEqual(levelsPlayed, c, n)
//							){
//						acceptedGame = false;
//					}
//				}
//				if (acceptedGame){
//					for (int c = 0; c < n; c++) { //for each controller
//						acceptedGameDatas[c].add(gameDatasList[g][c].get(m));
//					}
//				}
//			}
//			acceptedGameDatasList[g] = acceptedGameDatas;
//		}
//		
//		//Generate average gamedatas:
//		GameData[][] aveGameDatasList = new GameData[numberGames][];
//		for (int g = 0; g < numberGames; g++) { //for each game orignal (aliens, boulderdash ..)
//			aveGameDatasList[g] = getAverageGameDatas(acceptedGameDatasList[g], acceptedGameDatasList[g][0].get(0).gameTitle.split("_")[0] + " mutations average");
//		}
//		
//		//Compare average gamedatas:
//		for (int g = 0; g < numberGames; g++) {  //For each game
//			GameData[] gds = aveGameDatasList[g];		
//			compareGameDatas(gds, dataFolders);
//		}
	}
	
		
	public void analyzeGameDifference(String[] dataFolders){
		int n = dataFolders.length;
		//Extract data from data folders
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractGameDatas(dataFolders, false);		
		//Don't accept bad games
		gameDatas = getAcceptedGames(gameDatas);
		
		for (int g = 0; g < gameDatas.size(); g++) {  //For each game
			ArrayList<GameData[]> gdsList = new ArrayList<GameData[]>(); 
			gdsList.add(gameDatas.get(g));

			GameData[] aveGameDatas = getAverageGameDatas(gdsList, gameDatas.get(g)[0].gameTitle + " average");
			compareGameDatas(aveGameDatas, dataFolders);
		}
	}
	

	public void analyzeMutationDifference(String[] dataFolders, int numberMutations){
		int n = dataFolders.length;
		//Extract data from data folders
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractMutatedGameDatas(dataFolders, numberMutations, false);
		//Don't accept bad games
		gameDatas = getAcceptedGames(gameDatas);
		
		int numberGames = gameDatas.size() / numberMutations;
		for (int g = 0; g < numberGames; g++) { //for each game orignal (aliens, boulderdash ..)
			for (int m = 0; m < numberMutations; m++) {
				ArrayList<GameData[]> gdsList = new ArrayList<GameData[]>();
				gdsList.add(gameDatas.get(g + m * numberGames));
				
				GameData[] aveGameDatas = getAverageGameDatas(gdsList, gameDatas.get(g + m * numberGames)[0].gameTitle + " average");
				compareGameDatas(aveGameDatas, dataFolders);
			}
		}
	}

	private void compareGameDatas(GameData[] gds, String[] dataFolders) {
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
			valLines[c] += getAgentName(dataFolders[c]) + "\t";
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
	
	
//    games = new String[]{"aliens", "boulderdash", "butterflies", "chase", "frogs",
//            "missilecommand", "portals", "sokoban", "survivezombies", "zelda",
//            "camelRace", "digdug", "firestorms", "infection", "firecaster",
//            "overload", "pacman", "seaquest", "whackamole", "eggomania"};
	
//  games = new String[]{"aliens", "boulderdash", "frogs",
//  "missilecommand", "portals", "survivezombies", "zelda",
//  "digdug",
//  "overload", "pacman", "seaquest", "whackamole", "eggomania"};
	
	private ArrayList<GameData[]> getAcceptedGames(ArrayList<GameData[]> gameDatas) {
		int n = gameDatas.get(0).length;
		
		ArrayList<GameData[]> acceptedGameDatas = new ArrayList<GameData[]>();
		
//		for (int c = 0; c < n; c++) acceptedGameDatas.get(0)[c] = new ArrayList<GameData>();
		
		for (int g = 0; g < gameDatas.size(); g++) { //for each game		
			if (gameDatas.get(g)[0].gameTitle.contains("camelRace") 
					|| gameDatas.get(g)[0].gameTitle.contains("sokoban")
//					|| gameDatas[0].get(g).gameTitle.contains("chase")
//					|| gameDatas[0].get(g).gameTitle.contains("firestorms")
//					|| gameDatas[0].get(g).gameTitle.contains("infection")
//					|| gameDatas[0].get(g).gameTitle.contains("firecaster")
//					|| gameDatas[0].get(g).gameTitle.contains("butterflies")
				)
				continue;
			
			
			boolean acceptedGame = true; 
			// Per game
			for (int c = 0; c < n; c++) { //for each controller
				ArrayList<LevelPlay> levelsPlayed = gameDatas.get(g)[c].levelsPlayed; 
				if (hasDisqualified(levelsPlayed)
						|| allScoresEqual(levelsPlayed, c, n)
						|| allwaysLowTime(levelsPlayed, c, n)
				)
					acceptedGame = false;
			}
			
			if (acceptedGame){
				acceptedGameDatas.add(gameDatas.get(g));
			}
		}
		
		return acceptedGameDatas;
	}
	
	private String getAgentName(String string) {
		return string.split("/")[string.split("/").length-1].split("\\d")[0];
	}

	boolean scoresEqual = true;
	float lastScore = 0;
	private boolean allScoresEqual(ArrayList<LevelPlay> levelsPlayed, int ctrlIdx, int n) {
		if (ctrlIdx == 0) scoresEqual = true;
		else if (!scoresEqual) return false;
		
		for (int i = 0; i < levelsPlayed.size(); i++) {
			LevelPlay lp = levelsPlayed.get(i);
			if ((i != 0 || ctrlIdx != 0) && lp.score != lastScore){
				scoresEqual = false;
				return false;
			}
			lastScore = lp.score;
		}
		if (ctrlIdx == n-1 && scoresEqual) return true;
		return false;
	}
	
	boolean allwaysLowTime = true;
	private boolean allwaysLowTime(ArrayList<LevelPlay> levelsPlayed, int ctrlIdx, int n) {
		if (ctrlIdx == 0) allwaysLowTime = true;
		
		for (int i = 0; i < levelsPlayed.size(); i++) {
			LevelPlay lp = levelsPlayed.get(i);
			if (lp.timesteps > LOWEST_TICKS_ALLOWED) allwaysLowTime = false;
		}
		
		if (ctrlIdx == n-1 && allwaysLowTime) return true;
		return false;
	}

	private boolean hasDisqualified(ArrayList<LevelPlay> levelsPlayed) {
		for (LevelPlay lp : levelsPlayed) {
			if (lp.won < 0) return true;
		}
		return false;
	}
	
	private boolean scoresAbove(ArrayList<LevelPlay> levelsPlayed, float maxAbsScore) { 		
		for (LevelPlay lp : levelsPlayed) {
			if (Math.abs(lp.score) > maxAbsScore) return true;
		}
		return false;
	}

	private GameData[] getAverageGameDatas(ArrayList<GameData[]> gameDatas, String name) {
		int n = gameDatas.get(0).length;
		int amountGames = gameDatas.size();
		int amountPlaythroughs = gameDatas.get(0)[0].levelsPlayed.size();
		
		GameData[] aveGameDatas = new GameData[n];
		
		float[][] allControllersGamePlaythroughMins = new float[amountGames][];
		float[][] allControllersGamePlaythroughMaxs = new float[amountGames][];
		float[][] allControllersGamePlaythroughAves = new float[amountGames][];
		float[][] allControllersGamePlaythroughStds = new float[amountGames][];
		for (int g = 0; g < amountGames; g++) {
			allControllersGamePlaythroughMins[g] = new float[amountPlaythroughs];
			allControllersGamePlaythroughMaxs[g] = new float[amountPlaythroughs];
			allControllersGamePlaythroughAves[g] = new float[amountPlaythroughs];
			allControllersGamePlaythroughStds[g] = new float[amountPlaythroughs];
			for (int i = 0; i < amountPlaythroughs; i++) {
				float min = Float.POSITIVE_INFINITY;
				float max = Float.NEGATIVE_INFINITY;
				float sum = 0, sumsq = 0, ave = 0, sd = 0;
				
				for (int c = 0; c < n; c++) { //for each controller
					LevelPlay lp = gameDatas.get(g)[c].levelsPlayed.get(i);
					if (lp.score > max) max = lp.score;
					if (lp.score < min) min = lp.score;
					sum += lp.score;
					sumsq += lp.score * lp.score;
				}
				ave = sum / n;
				double num = sumsq - (n * ave * ave);
		        if (num < 0) num = 0;
		        sd = (float) Math.sqrt(num / (n - 1));
				allControllersGamePlaythroughMins[g][i] = min;
				allControllersGamePlaythroughMaxs[g][i] = max;
				allControllersGamePlaythroughAves[g][i] = ave;
				allControllersGamePlaythroughStds[g][i] = sd;
			}
		}
		
		
		for (int c = 0; c < n; c++) { //for each controller
			GameData[] aveGds = new GameData[n];
//			System.out.println("---Controller: " + c);
			
			int count = amountGames * amountPlaythroughs;
			
			float min = Float.POSITIVE_INFINITY;
			float max = Float.NEGATIVE_INFINITY;
			float sum = 0;
			float sumsq = 0;
			float ave = 0;
			float sd = 0;
			float se = 0;
			float nsum = 0;
			float nsumsq = 0;
			float nave = 0;
			float nsd = 0;
			float nse = 0;
			float znsum = 0;
			float znave = 0;
			float sumtic = 0, sumsqtic = 0, avtic = 0, sdtic = 0, setic = 0;
			float winsum = 0;
			float wrate = 0;
			float wrsd = 0;
			float wrse = 0;
			float mmsum = 0, mmsumsq = 0, mmave = 0, mmsd = 0, mmse = 0, mmpecentile15p865 = 0, mmpecentile84p135 = 0;
			float median = 0, quartile1 = 0, quartile3 = 0;
			float actensum = 0;
			float acten = 0;
			


			float[] scoreArray = new float[count];
			float[] mmScoreArray = new float[count];

			for (int g = 0; g < amountGames; g++) { //for each game	
				GameData gd = gameDatas.get(g)[c];
				
				int amountOfActions = 0;
				HashMap<String, Integer> actcounter = new HashMap<String, Integer>();
				actcounter.put("ACTION_NIL", 0);actcounter.put("ACTION_LEFT", 0);actcounter.put("ACTION_RIGHT", 0);
				actcounter.put("ACTION_UP", 0);actcounter.put("ACTION_DOWN", 0);actcounter.put("ACTION_USE", 0);
				
				
				float game_sum = 0;
				float game_sumsq = 0;
				float game_ave = 0;
				float game_sd = 0;
				float game_max = Float.NEGATIVE_INFINITY;
				float game_min = Float.POSITIVE_INFINITY;
				for (int i = 0; i < gd.levelsPlayed.size(); i++) {
					LevelPlay lp = gd.levelsPlayed.get(i);
					game_sum += lp.score;
					game_sumsq += lp.score * lp.score;
					sum += lp.score;
					nsum += lp.score > 0 ? 1 : 0;
					sumsq += lp.score * lp.score;
					nsumsq += (lp.score > 0 ? 1 : 0) * (lp.score > 0 ? 1 : 0);
					sumtic += lp.timesteps;
					sumsqtic += lp.timesteps * lp.timesteps;
					winsum += lp.won;
					if (lp.score > max) max = lp.score;
					if (lp.score < min) min = lp.score;
					scoreArray[i + g * gd.levelsPlayed.size()] = lp.score;
					
					if (lp.score > game_max) game_max = lp.score;
					if (lp.score < game_min) game_min = lp.score;
										
					for (String act : lp.actionSeq) {
						if (act.contains("NIL")) continue;
						actcounter.put(act, actcounter.get(act) + 1);
						amountOfActions++;
					}
					
//			        ZNMean:
					float zNormalizeVal = lp.score - allControllersGamePlaythroughAves[g][i] == 0 ? 0 : (lp.score - allControllersGamePlaythroughAves[g][i]) / allControllersGamePlaythroughStds[g][i];
					znsum += zNormalizeVal;// * lp.score;
				
//			        MMMean:
		        	float maxMinNormalizedScore = allControllersGamePlaythroughMaxs[g][i] - allControllersGamePlaythroughMins[g][i]== 0 ? 0.5f : (lp.score - allControllersGamePlaythroughMins[g][i]) / (allControllersGamePlaythroughMaxs[g][i] - allControllersGamePlaythroughMins[g][i]);
		        	mmsum += maxMinNormalizedScore;
		        	mmsumsq += maxMinNormalizedScore * maxMinNormalizedScore;
		        	mmScoreArray[i + g * gd.levelsPlayed.size()] = maxMinNormalizedScore;			        	
				}
				
				game_ave = game_sum / (float)gd.levelsPlayed.size();
				double num = game_sumsq - (gd.levelsPlayed.size() * game_ave * game_ave);
		        if (num < 0)  num = 0;
		        game_sd = (float) Math.sqrt(num / (gd.levelsPlayed.size() - 1));
		        
		        
				float H = 0;
//				System.out.println("Probs for game: " + gd.gameTitle);
				for (String string : actcounter.keySet()) {
					float actionProb = actcounter.get(string) / (float)amountOfActions;
					H += actionProb == 0 ? 0 : actionProb * Utility.logOfBase(Utility.getAmountOfActionsInGame(gd.gameTitle), actionProb);
//					System.out.println("Probability of " + string + ": " + actionProb);
				}
				actensum += -H;
//				System.out.println("amount of actions: " + amountOfActions);
//				System.out.println("entropy: " + -H + ", count: " + count);
		   
			}
			
			Arrays.sort(scoreArray);
			if (scoreArray.length % 2 == 0){
				median = ((float)scoreArray[scoreArray.length/2] + (float)scoreArray[scoreArray.length/2 - 1])/2;
				quartile1 = ((float)scoreArray[scoreArray.length/4] + (float)scoreArray[scoreArray.length/4 - 1])/2;
				quartile3 = ((float)scoreArray[scoreArray.length*3/4] + (float)scoreArray[scoreArray.length*3/4 - 1])/2;
			}else{
				median = (float) scoreArray[scoreArray.length/2];
				quartile1 = (float) scoreArray[scoreArray.length/4];
				quartile3 = (float) scoreArray[scoreArray.length*3/4];
			}

			
			ave = sum / (float)count;
			avtic = sumtic / (float)count;
			nave = nsum / (float)count;
			wrate = winsum / (float)count;
			
			double num = sumsq - (count * ave * ave);
	        if (num < 0) {
	            num = 0;
	        }
	        sd = (float) Math.sqrt(num / (count - 1));
	        se = sd / (float)Math.sqrt(count);
	       
	        wrse = (float) Math.sqrt(count * wrate * (1 - wrate)) / count;		//	        sqrt(np(1-p))
	        
			num = nsumsq - (count * nave * nave);
	        if (num < 0) {
	            num = 0;
	        }
	        nsd = (float) Math.sqrt(num / (count - 1));
	        nse = nsd / (float)Math.sqrt(count);
	        
			num = sumsqtic - (count * avtic * avtic);
	        if (num < 0) {
	            num = 0;
	        }
	        sdtic = (float) Math.sqrt(num / (count - 1));
	        setic = sdtic / (float)Math.sqrt(count);
	        
	        znave = znsum / (float)count;
	        mmave = mmsum / (float)count;
			num = mmsumsq - (count * mmave * mmave);
	        if (num < 0) {
	            num = 0;
	        }
	        mmsd = (float) Math.sqrt(num / (count - 1));
	        mmse =   mmsd / (float)Math.sqrt(count);
	        Arrays.sort(mmScoreArray);
			mmpecentile15p865 = (float) mmScoreArray[(int)(mmScoreArray.length * 0.15865)];
			mmpecentile84p135 = (float) mmScoreArray[(int)(mmScoreArray.length * 0.84135)];
	        
			acten = actensum / (float)amountGames; 
			
			
//			float H = 0;
////			System.out.println("Probs for try nr: " + lp.levelNumberTry);
//			for (String string : actcounter.keySet()) {
//				float actionProb = actcounter.get(string) / (float)amountOfActions;
//				H += actionProb == 0 ? 0 : actionProb * Utility.logOfBase(2, actionProb);
////				System.out.println("Probability of " + string + ": " + actionProb);
//			}
//			acten = -H;
			
			aveGds[c] = new GameData();
			aveGds[c].gameTitle = name + (amountGames > 1 ? (" -- amount of games: " + amountGames) : "") + " -- playthroughs: " + amountPlaythroughs;
			
			aveGds[c].gameValues.put(DataTypes.AVE, ave); 
			aveGds[c].gameValues.put(DataTypes.NAVE, nave); 
			aveGds[c].gameValues.put(DataTypes.ZNAVE, znave); 
			aveGds[c].gameValues.put(DataTypes.MMAVE, mmave); 
			aveGds[c].gameValues.put(DataTypes.MMSD, mmsd); 
			aveGds[c].gameValues.put(DataTypes.MMSE, mmse); 
			aveGds[c].gameValues.put(DataTypes.MEDI, median); 
			aveGds[c].gameValues.put(DataTypes.QUAR1, quartile1); 
			aveGds[c].gameValues.put(DataTypes.QUAR3, quartile3); 
			aveGds[c].gameValues.put(DataTypes.ERRLO, mmpecentile15p865); 
			aveGds[c].gameValues.put(DataTypes.ERRHI, mmpecentile84p135); 
			aveGds[c].gameValues.put(DataTypes.MAX, max); 
			aveGds[c].gameValues.put(DataTypes.MIN, min); 
			aveGds[c].gameValues.put(DataTypes.SD, sd); 
			aveGds[c].gameValues.put(DataTypes.SE, se); 
			aveGds[c].gameValues.put(DataTypes.WRATE, wrate); 
			aveGds[c].gameValues.put(DataTypes.WRSD, wrsd); 
			aveGds[c].gameValues.put(DataTypes.WRSE, wrse); 
			aveGds[c].gameValues.put(DataTypes.AVTIC, avtic); 
			aveGds[c].gameValues.put(DataTypes.SDTIC, sdtic); 
			aveGds[c].gameValues.put(DataTypes.ACTEN, acten); 
	        
	        aveGameDatas[c] = aveGds[c];
		}
		
		return aveGameDatas;
	}
}


