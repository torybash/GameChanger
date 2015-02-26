package gamegenerator.core;

import fastVGDL.tools.IO;
import gamechanger.core.GameChanger;
import gamechanger.core.InteractionsChanger;
import gamechanger.writer.Writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import core.ArcadeMachine;
import dataanalysis.controller.Controller;
import dataanalysis.controller.Controller.ControllerType;
import dataanalysis.controller.ControllerHelper;
import dataanalysis.core.FitnessAnalysis;
import dataanalysis.core.GameDataAnalysis;
import dataanalysis.fitness.FitnessCalculator;
import dataanalysis.fitness.GameFitness;

public class GameGenerator {

//	final static String mainFolder = "game_generator/";
	final static String gameDescFolder = "game_descs/";
	final static String gameDataFolder = "game_data/";
	final static String resultsFolder = "results/";
	
	static FitnessAnalysis fa = new FitnessAnalysis();
	static GameDataAnalysis gda = new GameDataAnalysis();
	
	static Random r = new Random();
	

	public static void evolveGameFromExisting(String gameFolder, String gameTitle, String outputFolderPath, int id){
		String origGamePath = gameFolder + gameTitle + ".txt";
		String origGameDesc = new IO().getDescFromFile(origGamePath);
		InteractionsChanger.setFunctions(false);
		
		String title = gameTitle + "_mut" + id;
		String levelPath = gameFolder + gameTitle + "_lvl0.txt";
		
	    File folder = new File(outputFolderPath + gameDescFolder);
	    folder.mkdirs();		
	    folder = new File(outputFolderPath + gameDataFolder);
	    folder.mkdirs();	
	    
		EvolveGameData bestGameData = evolveGame(origGameDesc, title, levelPath, outputFolderPath);
		

	    folder = new File(outputFolderPath + resultsFolder);
	    folder.mkdirs();		
		
		
		Writer.storeString(bestGameData.gameDesc, outputFolderPath + resultsFolder, title);
		Writer.storeString(bestGameData.gf.toString(), outputFolderPath + resultsFolder, title + "_results");	
	}
	
	public static void evolveGameFromScratch(String gameTitle, String outputFolderPath){
	    File folder = new File(outputFolderPath + gameDescFolder);
	    folder.mkdirs();		
	    folder = new File(outputFolderPath + gameDataFolder);
	    folder.mkdirs();		

		
		String gameDesc = GameChanger.makeArcadeGame();
		
		String lvlDesc = GameChanger.makeLevel(gameDesc);
		storeLvlDesc(lvlDesc, gameTitle, outputFolderPath);
	
		String levelFolderPath = outputFolderPath + gameDescFolder + gameTitle + "_lvl0.txt";
		
		

		
		boolean gameHasProblems = true;
		int problemCount = 0;
		long tim = System.currentTimeMillis();
		while(gameHasProblems){
			EvolveGameData gd = playGameGetData(gameDesc, gameTitle, levelFolderPath, outputFolderPath);
			
			int INDEX_FOR_WINRATE_OVER_DONOTHING = 1;
			
			if (gd.gf.fitness == -1 || gd.gf.fitnessVals[INDEX_FOR_WINRATE_OVER_DONOTHING] <= 0){
				gameDesc = GameChanger.makeArcadeGame();
				lvlDesc = GameChanger.makeLevel(gameDesc);
				storeLvlDesc(lvlDesc, gameTitle, outputFolderPath);
				problemCount++;
			}else{
				System.out.println("Found well-formed game description after " + problemCount + " tries, and " + ((System.currentTimeMillis()-tim)*1000) + "s -- evolving..");
				gameHasProblems = false;
			}
		}		
		
	    folder = new File(outputFolderPath + resultsFolder);
	    folder.mkdirs();	
		
		EvolveGameData bestGameData = evolveGame(gameDesc, gameTitle, levelFolderPath, outputFolderPath);
		
		Writer.storeString(bestGameData.gameDesc, outputFolderPath + resultsFolder, gameTitle);
		Writer.storeString(lvlDesc, outputFolderPath + resultsFolder, gameTitle + "_lvl0");
		Writer.storeString(bestGameData.gf.toString(), outputFolderPath + resultsFolder, gameTitle + "_results");
	}
	
	
	public static EvolveGameData evolveGame(String gameDesc, String gameTitle, String levelPath, String outputFolderPath){
		int iterations = 15, mutations = 50, mutationsSurvive = 25;

		EvolveGameData origData = playGameGetData(gameDesc, gameTitle, levelPath, outputFolderPath);
		
		
		String[] gameDescs = new String[mutations];
		EvolveGameData[] evolveGameDatas = new EvolveGameData[mutations];
		EvolveGameData[] survivedGameDatas = new EvolveGameData[mutationsSurvive];
		
		for (int m = 0; m < mutations; m++) {
			gameDescs[m] = GameChanger.changeGameByDesc(gameDesc, false, true, false);
		}
		
		
		
		for (int i = 0; i < iterations; i++) {
			
			System.out.println("----Iteration " +i  + "-----");
			
			//Mutate game descriptions
			if (i>0){
				for (int m = 0; m < mutations; m++) {
					String newGameDesc = "";
					
					if (m < mutationsSurvive){
						newGameDesc = evolveGameDatas[m].gameDesc;
					}else{
						float rnd = r.nextFloat();
						if (rnd > 0.65){ //Mutate  --  35%
							int idx = r.nextInt(mutationsSurvive);
							newGameDesc =  GameChanger.changeGameByDesc(evolveGameDatas[idx].gameDesc, false, true, false);
						}else if (rnd > 0.1){ //Crossover -- 55%
							int idx1 = r.nextInt(mutationsSurvive);
							int idx2 = r.nextInt(mutations);
							newGameDesc =  GameChanger.crossOverGame(evolveGameDatas[idx1].gameDesc, evolveGameDatas[idx2].gameDesc);
						}else{ //New  --  10%
							newGameDesc = GameChanger.changeGameByDesc(gameDesc, false, true, false);
						}
					}
					gameDescs[m] = newGameDesc;
				}
			}
			
			
			//Play through games and get fitness data
			for (int m = 0; m < mutations; m++) {
				if (m < mutationsSurvive){
					if (evolveGameDatas[m] == null){ //first iteratino
						evolveGameDatas[m] = playGameGetData(gameDescs[m], gameTitle, levelPath, outputFolderPath);
					}else{
						evolveGameDatas[m] = survivedGameDatas[m].copy();
					}
				}else{
					evolveGameDatas[m] = playGameGetData(gameDescs[m], gameTitle, levelPath, outputFolderPath);
				}
			}
			
			//Sort by fitness
			Arrays.sort(evolveGameDatas);
			
			for (int j = 0; j < survivedGameDatas.length; j++) {
				survivedGameDatas[j] = evolveGameDatas[j].copy();
			}
			
			System.out.println("Top 3 fitness: ");
			for (int j = 0; j < 3; j++) System.out.println(evolveGameDatas[j].gf.fitness);
			System.out.println("(Orig fitness: " + origData.gf.fitness + ")");
			System.out.println("Best game desc:");
			System.out.println(evolveGameDatas[0].gameDesc);
			System.out.println("---fitness vals:");
	        System.out.println(FitnessCalculator.getFitnessTopString(", "));
	        System.out.println(Arrays.toString(evolveGameDatas[0].gf.fitnessVals));
	        System.out.println(Arrays.toString(evolveGameDatas[0].gf.fitnessValsString));
			System.out.println();
			
			
			//Deciding if should stop
			if (evolveGameDatas[0].gf.fitness > 0.98){
				break;
			}

		}
		
		return evolveGameDatas[0];
	}
	

	private static EvolveGameData playGameGetData(String gameDesc, String gameTitle, String levelPath, String outputFolderPath) {
		storeGameDesc(gameDesc, gameTitle, outputFolderPath);
		
//        System.out.println("Playing game..");
        
//        System.out.println(gameDesc);
//        System.out.println();
        
        long tim = System.currentTimeMillis();
		playGame(gameTitle, levelPath, outputFolderPath);
//		System.out.println("Finished playing game - calculating fitness..");
		GameFitness gf = getGameData(outputFolderPath);
//        System.out.println("Fitness: " + gf.fitness + ", time: " + (System.currentTimeMillis()-tim));
//        System.out.println(FitnessCalculator.getFitnessTopString(", "));
//        System.out.println(Arrays.toString(gf.fitnessVals));
//        System.out.println(Arrays.toString(gf.fitnessValsString));
//        System.out.println();
        
		System.out.println("Finished playing game.. Fitness: " + gf.fitness + ", time: " + (System.currentTimeMillis()-tim));
		
		
		return new EvolveGameData(gf, gameDesc);
	}
	
	private static GameFitness getGameData(String outputFolderPath) {
		Controller[] controllers = ControllerHelper.getTheTwoControllers();
		ControllerHelper.setControllerDataFolders(controllers, outputFolderPath + gameDataFolder, "game_gen_test");
		
//		gda.analyzeEachGameDifference(controllers, false, false);
		
		return fa.getFitnessForSingleGame(controllers);
	}


	private static void playGame(String gameTitle, String levelPath, String outputFolderPath){

		
		
//        String[] controllers = new String[]{"controllers.sampleMCTS.Agent", "controllers.dontDie.Agent", "controllers.sampleGA.Agent", 
//        		"controllers.randomOneStep.Agent", "controllers.sampleonesteplookahead.Agent", "controllers.random.Agent",
//        		"controllers.doNothing.Agent"};
        
        String[] controllers = new String[]{"controllers.MCTSish.Agent","controllers.doNothing.Agent"};
		
        int N = 1, L = 1, M = 6;
        boolean saveActions = true;
        String[] levels = new String[L];
        String[] actionFiles = new String[L*M];
        
        PrintStream origStream = System.out;
        
        for (int c = 0; c < controllers.length; c++) {
        	String foldername = outputFolderPath + gameDataFolder + controllers[c].split("\\.")[1] + "game_gen_test";
	        try {
				File dir = new File(foldername);
				dir.mkdir();
				System.setOut(new PrintStream(new FileOutputStream(foldername+"/gamedata.txt")));
	 		} catch (FileNotFoundException e) {
	 			e.printStackTrace();
	 		}
       	
            int actionIdx = 0;
            String game = outputFolderPath + gameDescFolder + gameTitle + ".txt";
            for(int j = 0; j < L; ++j){
                levels[j] = levelPath; //gameTitle + "_lvl" + j +".txt";
                if(saveActions) for(int k = 0; k < M; ++k)
                    actionFiles[actionIdx++] = foldername + "/" + "actions_game_" + 0 + "_level_" + j + "_" + k + ".txt";
            }
            ArcadeMachine.runGames(game, levels, M, controllers[c], saveActions? actionFiles:null);
	          
        }
        
        System.setOut(origStream);
	}


	private static void storeGameDesc(String gameDesc, String gameTitle, String outputFolderPath) {
		
		PrintWriter writer;
		String path = outputFolderPath + gameDescFolder + gameTitle + ".txt";
		try {
			writer = new PrintWriter(path, "UTF-8");
			writer.println(gameDesc);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static void storeLvlDesc(String lvlDesc, String gameTitle, String outputFolderPath) {
		
		PrintWriter writer;
		String path = outputFolderPath + gameDescFolder + gameTitle + "_lvl0" + ".txt";
		try {
			writer = new PrintWriter(path, "UTF-8");
			writer.println(lvlDesc);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
