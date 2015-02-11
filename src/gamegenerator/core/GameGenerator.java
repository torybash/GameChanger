package gamegenerator.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

import core.ArcadeMachine;

import fastVGDL.tools.IO;
import gamechanger.core.GameChanger;
import dataanalysis.controller.Controller;
import dataanalysis.controller.ControllerHelper;
import dataanalysis.controller.Controller.ControllerType;
import dataanalysis.core.FitnessAnalysis;
import dataanalysis.core.GameData;

public class GameGenerator {

	final static String mainFolder = "game_generator/";
	final static String gameFolder = "game_generator/game_descs/";
	final static String gameDataFolder = "game_generator/game_data/";
	
	static FitnessAnalysis fa = new FitnessAnalysis();
	
	static Random r = new Random();
	
	public static void main(String[] args) {
		
		evolveGame("../gvgai/examples/gridphysics/", "zelda");
	}
	
	
	public static void evolveGames(){
		String[] games = new String[]{"aliens", "boulderdash", "butterflies", "chase", "frogs",
                "missilecommand", "portals", "sokoban", "survivezombies", "zelda",
                "camelRace", "digdug", "firestorms", "infection", "firecaster",
                "overload", "pacman", "seaquest", "whackamole", "eggomania"};
                
        
		for (int i = 0; i < games.length; i++) {
			String gameFolder = "../gvgai/examples/gridphysics/";
			
			
			evolveGame(gameFolder, games[i]);

			
		}
	}
	
	
	public static void evolveGame(String gameFolder, String gameTitle){
		int iterations = 100, mutations = 50, mutationsSurvive = 30;


		String origGamePath = gameFolder + gameTitle + ".txt";
	
		String origGameDesc = new IO().getDescFromFile(origGamePath);
//		System.out.println(origGameDesc);
		EvolveGameData origData = playGameGetData(origGameDesc, gameTitle, gameFolder);
		
		
		String[] gameDescs = new String[mutations];
		EvolveGameData[] evolveGameDatas = new EvolveGameData[mutations];
		
		for (int m = 0; m < mutations; m++) {
			gameDescs[m] = GameChanger.changeGameByPath(origGamePath, false, true, false);
		}
		
		
		
		for (int i = 0; i < iterations; i++) {
			
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
							newGameDesc = GameChanger.changeGameByPath(origGamePath, false, true, false);
						}
					}
					gameDescs[m] = newGameDesc;
				}
			}
			
			
			//Play through games and get fitness data
			for (int m = 0; m < mutations; m++) {
				if (m < mutationsSurvive){
					if (evolveGameDatas[m] == null){
						evolveGameDatas[m] = playGameGetData(gameDescs[m], gameTitle, gameFolder);
					}
				}else{
					evolveGameDatas[m] = playGameGetData(gameDescs[m], gameTitle, gameFolder);
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
	

	private static EvolveGameData playGameGetData(String gameDesc, String gameTitle, String origGameFolder) {
		storeGameDesc(gameDesc, gameTitle);
		
        System.out.println("Playing game..");
        long tim = System.currentTimeMillis();
		playGame(gameTitle, origGameFolder);
		System.out.println("Finished playing game - calculating fitness..");
		double fitness = getGameData();
        System.out.println("Fitness: " + fitness + ", time: " + (System.currentTimeMillis()-tim));

		
		
		return new EvolveGameData(fitness, gameDesc);
	}
	
	private static double getGameData() {
		Controller dontDieController = new Controller("Explorer", "dontDie", ControllerType.INTELLIGENT);
		Controller mctsController = new Controller("MCTS", "sampleMCTS", ControllerType.INTELLIGENT);
		Controller gaController = new Controller("GA", "sampleGA", ControllerType.INTELLIGENT);
		Controller rosController = new Controller("Onestep-S", "randomOneStep", ControllerType.SEMI_INTELLIGENT);
		Controller onestepController = new Controller("Onestep-H", "sampleonesteplookahead", ControllerType.INTELLIGENT);
		Controller randomController = new Controller("Random", "random", ControllerType.RANDOM);
		Controller doNothingController = new Controller("Do Nothing", "doNothing", ControllerType.DO_NOTHING);
		
		Controller mctsishController = new Controller("MCTSish", "MCTSish", ControllerType.INTELLIGENT);
		
//		Controller[] controllers = new Controller[]{dontDieController, mctsController, rosController, randomController, doNothingController};
		Controller[] controllers = new Controller[]{dontDieController, mctsController, gaController, rosController, randomController, doNothingController};

		ControllerHelper.setControllerDataFolders(controllers, gameDataFolder, "game_gen_test");
		
		
		return fa.getFitnessForSingleGame(controllers);
		
	}


	private static void playGame(String gameTitle, String origGameFolder){

		
		int seed = new Random().nextInt();
		
        String[] controllers = new String[]{"controllers.sampleMCTS.Agent", "controllers.dontDie.Agent", "controllers.sampleGA.Agent", 
        		"controllers.randomOneStep.Agent", "controllers.sampleonesteplookahead.Agent", "controllers.random.Agent",
        		"controllers.doNothing.Agent"};
		
        int N = 1, L = 1, M = 10;
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
                levels[j] = origGameFolder +gameTitle + "_lvl" + j +".txt";
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



}
