package gamegenerator.core;

import fastVGDL.tools.IO;
import gamechanger.core.GameChanger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

import core.ArcadeMachine;
import dataanalysis.controller.Controller;
import dataanalysis.controller.Controller.ControllerType;
import dataanalysis.controller.ControllerHelper;
import dataanalysis.core.FitnessAnalysis;
import dataanalysis.fitness.FitnessCalculator;
import dataanalysis.fitness.GameFitness;

public class GameGenerator {

	final static String mainFolder = "game_generator/";
	final static String gameFolder = "game_generator/game_descs/";
	final static String gameDataFolder = "game_generator/game_data/";
	
	static FitnessAnalysis fa = new FitnessAnalysis();
	
	static Random r = new Random();
	
	public static void main(String[] args) {
		evolveGameFromExisting("../gvgai/examples/gridphysics/", "overload");
//		evolveGameFromScratch("coolgame");
	}
	
	
	public static void evolveGames(){
		String[] games = new String[]{"aliens", "boulderdash", "butterflies", "chase", "frogs",
                "missilecommand", "portals", "sokoban", "survivezombies", "zelda",
                "camelRace", "digdug", "firestorms", "infection", "firecaster",
                "overload", "pacman", "seaquest", "whackamole", "eggomania"};
                
        
		for (int i = 0; i < games.length; i++) {
			String gameFolder = "../gvgai/examples/gridphysics/";
			
			
//			evolveGame(gameFolder, games[i]);

			
		}
	}
	
	public static void evolveGameFromExisting(String gameFolder, String gameTitle){
		String origGamePath = gameFolder + gameTitle + ".txt";
		String origGameDesc = new IO().getDescFromFile(origGamePath);
		
		evolveGame(origGameDesc, gameTitle, gameFolder);
	}
	
	public static void evolveGameFromScratch(String gameTitle){
		String gameDesc = GameChanger.makeGame();
		String lvlDesc = GameChanger.makeLevel(gameDesc);
		
		storeLvlDesc(lvlDesc, gameTitle);
		
		evolveGame(gameDesc, gameTitle, gameFolder);
	}
	
	
	public static void evolveGame(String gameDesc, String gameTitle, String levelFolder){
		int iterations = 100, mutations = 50, mutationsSurvive = 30;

		EvolveGameData origData = playGameGetData(gameDesc, gameTitle, levelFolder);
		
		
		String[] gameDescs = new String[mutations];
		EvolveGameData[] evolveGameDatas = new EvolveGameData[mutations];
		
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
					if (evolveGameDatas[m] == null){
						evolveGameDatas[m] = playGameGetData(gameDescs[m], gameTitle, levelFolder);
					}
				}else{
					evolveGameDatas[m] = playGameGetData(gameDescs[m], gameTitle, levelFolder);
				}
			}
			
			//Sort by fitness
			Arrays.sort(evolveGameDatas);
			
			
			System.out.println("Top 3 fitness: ");
			for (int j = 0; j < 3; j++) System.out.println(evolveGameDatas[j].fitness);
			System.out.println("(Orig fitness: " + origData.fitness + ")");
			System.out.println("Best game desc:");
			System.out.println(evolveGameDatas[0].gameDesc);
			System.out.println();

		}
	}
	

	private static EvolveGameData playGameGetData(String gameDesc, String gameTitle, String levelFolder) {
		storeGameDesc(gameDesc, gameTitle);
		
        System.out.println("Playing game..");
        
//        System.out.println(gameDesc);
//        System.out.println();
        
        long tim = System.currentTimeMillis();
		playGame(gameTitle, levelFolder);
		System.out.println("Finished playing game - calculating fitness..");
		GameFitness gf = getGameData();
        System.out.println("Fitness: " + gf.fitness + ", time: " + (System.currentTimeMillis()-tim));
        System.out.println(FitnessCalculator.getFitnessTopString(", "));
        System.out.println(Arrays.toString(gf.fitnessVals));
        System.out.println(Arrays.toString(gf.fitnessValsString));
        System.out.println();
		
		
		return new EvolveGameData(gf.fitness, gameDesc);
	}
	
	private static GameFitness getGameData() {
		Controller[] controllers = ControllerHelper.getMainControllers();
		ControllerHelper.setControllerDataFolders(controllers, gameDataFolder, "game_gen_test");
		
		return fa.getFitnessForSingleGame(controllers);
	}


	private static void playGame(String gameTitle, String levelFolder){

		
		int seed = new Random().nextInt();
		
//        String[] controllers = new String[]{"controllers.sampleMCTS.Agent", "controllers.dontDie.Agent", "controllers.sampleGA.Agent", 
//        		"controllers.randomOneStep.Agent", "controllers.sampleonesteplookahead.Agent", "controllers.random.Agent",
//        		"controllers.doNothing.Agent"};
        
        String[] controllers = new String[]{"controllers.dontDie.Agent","controllers.randomOneStep.Agent","controllers.random.Agent","controllers.doNothing.Agent"};
		
        int N = 1, L = 1, M = 6;
        boolean saveActions = true;
        String[] levels = new String[L];
        String[] actionFiles = new String[L*M];
        
        PrintStream origStream = System.out;
        
        for (int c = 0; c < controllers.length; c++) {
        	String foldername = gameDataFolder + controllers[c].split("\\.")[1] + "game_gen_test";
	        try {
				File dir = new File(foldername);
				dir.mkdir();
				System.setOut(new PrintStream(new FileOutputStream(foldername+"/gamedata.txt")));
	 		} catch (FileNotFoundException e) {
	 			e.printStackTrace();
	 		}
       	
            int actionIdx = 0;
            String game = gameFolder + gameTitle + ".txt";
            for(int j = 0; j < L; ++j){
                levels[j] = levelFolder +gameTitle + "_lvl" + j +".txt";
                if(saveActions) for(int k = 0; k < M; ++k)
                    actionFiles[actionIdx++] = foldername + "/" + "actions_game_" + 0 + "_level_" + j + "_" + k + ".txt";
            }
            ArcadeMachine.runGames(game, levels, M, controllers[c], saveActions? actionFiles:null, seed);
	          
        }
        
        System.setOut(origStream);
	}


	private static void storeGameDesc(String gameDesc, String gameTitle) {
		
		PrintWriter writer;
		String path = gameFolder + gameTitle + ".txt";
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

	private static void storeLvlDesc(String lvlDesc, String gameTitle) {
		
		PrintWriter writer;
		String path = gameFolder + gameTitle + "_lvl0" + ".txt";
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
