package aMain;

import dataanalysis.controller.Controller;
import dataanalysis.controller.ControllerHelper;
import dataanalysis.controller.Controller.ControllerType;
import dataanalysis.core.CMAEvolStrat;
import dataanalysis.core.FitnessAnalysis;
import dataanalysis.core.GameDataAnalysis;

public class AMain {
	public static void main(String[] args) {
		GameDataAnalysis gda = new GameDataAnalysis();
		FitnessAnalysis fa = new FitnessAnalysis();
		CMAEvolStrat cmaes = new CMAEvolStrat();
		
		String dataFolder = "gamedata/";
		Controller dontDieController = new Controller("Explorer", "dontdie", ControllerType.INTELLIGENT);
		Controller mctsController = new Controller("MCTS", "mcts", ControllerType.INTELLIGENT);
		Controller gaController = new Controller("GA", "ga", ControllerType.INTELLIGENT);
		Controller rosController = new Controller("Onestep-S", "randomonestep", ControllerType.SEMI_INTELLIGENT);
		Controller onestepController = new Controller("Onestep-H", "onestep", ControllerType.INTELLIGENT);
		Controller randomController = new Controller("Random", "random", ControllerType.RANDOM);
		Controller doNothingController = new Controller("Do Nothing", "doNothing", ControllerType.DO_NOTHING);
		
		Controller mctsishController = new Controller("MCTSish", "MCTSish", ControllerType.INTELLIGENT);
		
//		Controller[] controllers = new Controller[]{dontDieController, mctsController, rosController, randomController, doNothingController};
//		Controller[] controllers = new Controller[]{dontDieController, mctsController, gaController, rosController, randomController, doNothingController};
		Controller[] controllers = new Controller[]{dontDieController, mctsController, gaController, rosController, onestepController, randomController, doNothingController};
//		Controller[] controllers = new Controller[]{dontDieController, mctsishController, mctsController, gaController, rosController, onestepController, randomController, doNothingController};
		
		
		//*********************************
		//******25 P.T./800 TICKS TEST*****
		//*********************************
//		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "800t25pt_designed");
//		gda.analyzeGameDifference(controllers, false);
//		gda.analyzeAllGamesAverage(controllers, false);
//		fa.analyzeFitness(controllers, false);
		
		//*********************************
		//**********2K TICKS TEST**********
		//*********************************
//		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "2000ticks_all");
//		gda.analyzeGameDifference(controllers, false);
//		gda.analyzeAllGamesAverage(controllers, false);
//		fa.analyzeFitness(controllers, false);

		
		//*********************************
		//**********200 TICKS TEST*********
		//*********************************
		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "200ticks_all");
//		gda.analyzeGameDifference(controllers, false);
//		gda.analyzeAllGamesAverage(controllers, false);
		fa.analyzeFitness(controllers, false);
		
		
//		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "200ticks_all2");
//		gda.analyzeGameDifference(controllers, false);
//		gda.analyzeAllGamesAverage(controllers, false);
//		fa.analyzeFitness(controllers, false);
		
//		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "200ticks_mutation_");
//		gda.analyzeMutationDifference(controllers, 20, false);
//		gda.analyzeAllMutationsAverage(controllers, 20, false);
//		fa.analyzeMutationsFitness(controllers, 20, false);
		
		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "200ticks_gengames");
//		gda.analyzeGameDifference(controllers, false);
//		gda.analyzeAllGamesAverage(controllers, false);
		fa.analyzeFitness(controllers, false);
		
		/*Fitness feauture weights evolution:*/
		Controller[] goodDataControllers = ControllerHelper.copyControllers(controllers);
		Controller[] badDataControllers = ControllerHelper.copyControllers(controllers);
		ControllerHelper.setControllerDataFolders(goodDataControllers, dataFolder, "200ticks_all");
		ControllerHelper.setControllerDataFolders(badDataControllers, dataFolder, "200ticks_gengames");
//		fa.evolveFitnessWeights(goodDataControllers, badDataControllers, false);
		
		
//		cmaes.evolveFitnessWeights(goodDataControllers, badDataControllers);
		
		//*********************************
		//**********2 GAMES TEST***********
		//*********************************
//		ControllerHelper.setControllerDataFolders(controllers, dataFolder + "2gamestest/", "games_examples");
//		gda.analyzeGameDifference(controllers, false);
//		gda.analyzeAllGamesAverage(controllers, false);
//		fa.analyzeFitness(controllers, false);
		
//		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "games_mutation_");
//		gda.analyzeMutationDifference(controllers, 20, false);
//		gda.analyzeAllMutationsAverage(controllers, 20, false);
//		fa.analyzeMutationsFitness(controllers, 20, false);
		
		
		
		
		
		//*********************************
		//**********PUZZLE GAMES***********
		//*********************************
//		gda.analyzeMutationDifference(new String[]{"puzzlegamedata/sampleMCTS200ticks_mutation_", "puzzlegamedata/dontDie200ticks_mutation_", "puzzlegamedata/sampleGA200ticks_mutation_", 
//				"puzzlegamedata/randomOneStep200ticks_mutation_", "puzzlegamedata/sampleOneStepLookAhead200ticks_mutation_", "puzzlegamedata/sampleRandom200ticks_mutation_"}, 20);

//		gda.analyzeDifference(new String[]{"puzzlegamedata/sampleMCTS200ticks_all", "puzzlegamedata/dontDie200ticks_all", "puzzlegamedata/sampleGA200ticks_all", 
//		"puzzlegamedata/randomOneStep200ticks_all", "puzzlegamedata/sampleOneStepLookAhead200ticks_all", "puzzlegamedata/random200ticks_all"});
	
//		gda.analyzeMutationAverages(new String[]{"puzzlegamedata/sampleMCTS200ticks_mutation_", "puzzlegamedata/dontDie200ticks_mutation_", "puzzlegamedata/sampleGA200ticks_mutation_", 
//				"puzzlegamedata/randomOneStep200ticks_mutation_", "puzzlegamedata/sampleOneStepLookAhead200ticks_mutation_", "puzzlegamedata/sampleRandom200ticks_mutation_"}, 10);
	
	}
	
}
