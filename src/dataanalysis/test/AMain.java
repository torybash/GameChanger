package dataanalysis.test;

import dataanalysis.controller.Controller;
import dataanalysis.controller.ControllerHelper;
import dataanalysis.controller.Controller.ControllerType;
import dataanalysis.core.CMAEvolStrat;
import dataanalysis.core.DataTypes;
//import dataanalysis.core.CMAEvolStrat;
import dataanalysis.core.FitnessAnalysis;
import dataanalysis.core.GameDataAnalysis;

public class AMain {
	public static void main(String[] args) {
		
		
		
		GameDataAnalysis gda = new GameDataAnalysis();
		FitnessAnalysis fa = new FitnessAnalysis();
		CMAEvolStrat cmaes = new CMAEvolStrat();
		
		String dataFolder = "gamedata/";
		Controller[] controllers = ControllerHelper.getOldControllers();
		
		
		
		
		
		
		
		
		
		
		
//        Controller[] designedDataControllers = ControllerHelper.copyControllers(controllers);
//        Controller[] mutatedDataControllers = ControllerHelper.copyControllers(controllers);
//        Controller[] generatedDataControllers = ControllerHelper.copyControllers(controllers);
//        ControllerHelper.setControllerDataFolders(designedDataControllers, dataFolder, "800t25pt_designed");
//        ControllerHelper.setControllerDataFolders(mutatedDataControllers, dataFolder, "800t25pt_mutation_");
//        ControllerHelper.setControllerDataFolders(generatedDataControllers, dataFolder, "800t25pt_gengames");
		
        
//        gda.makeFeatureTypeCountCSV(designedDataControllers, mutatedDataControllers, generatedDataControllers);
		
		
		//*********************************
		//******GEN GAMES TESTT*****
		//*********************************
		
		
		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "500_6wellformedgames_test");
//		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "500_initial_test");
//		gda.printWellFormedGamesIdx(controllers);
//		gda.analyzeGameDifference(controllers, false, true);
//		gda.analyzeAllGamesAverage(controllers, false);
//		fa.analyzeFitness(controllers, false);
		
//		
		
		//*********************************
		//******10 P.T./2000 TICKS TEST*****
		//*********************************
		controllers = ControllerHelper.getMainControllers();
//		controllers = ControllerHelper.getOtherFourControllers();
//		controllers = ControllerHelper.getTheTwoControllers();
		ControllerHelper.setControllerDataFolders(controllers, dataFolder + "2000ticks,10pt test/", "_2000ticks_designed");
//		gda.countGamesHaveCondition(controllers, true);
		gda.analyzeEachGameDifference(controllers, true, false);
//		gda.analyzeAllGamesAverage(controllers, true);
//		fa.analyzeFitness(controllers, false, false);
		
		
		ControllerHelper.setControllerDataFolders(controllers, dataFolder + "2000ticks,10pt test/", "2000ticks_mutation_");
//		gda.analyzeAllMutationsAverage(controllers, 10, true);
//		gda.analyzeEachMutationDifference(controllers, 10, false, false);
//		fa.analyzeMutationsFitness(controllers, 10, false, false);

		ControllerHelper.setControllerDataFolders(controllers, dataFolder + "2000ticks,10pt test/", "_2000ticks_rndgen");
//		gda.countGamesHaveCondition(controllers, true);
//		gda.analyzeGameDifference(controllers, false, false);
		gda.analyzeAllGamesAverage(controllers, true);
//		fa.analyzeFitness(controllers, false, false);
		
        Controller[] designedDataControllers = ControllerHelper.copyControllers(controllers);
        Controller[] mutatedDataControllers = ControllerHelper.copyControllers(controllers);
        Controller[] generatedDataControllers = ControllerHelper.copyControllers(controllers);
        ControllerHelper.setControllerDataFolders(designedDataControllers, dataFolder + "2000ticks,10pt test/", "_2000ticks_designed");
        ControllerHelper.setControllerDataFolders(mutatedDataControllers, dataFolder + "2000ticks,10pt test/", "2000ticks_mutation_");
        ControllerHelper.setControllerDataFolders(generatedDataControllers, dataFolder + "2000ticks,10pt test/", "_2000ticks_rndgen");
		
        
//        gda.makeFeatureTypeCountCSV(designedDataControllers, mutatedDataControllers, generatedDataControllers);
		
		
		//*********************************
		//******25 P.T./800 TICKS TEST*****
		//*********************************
//		controllers = ControllerHelper.getOldControllers();
		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "800t25pt_designed");
//		gda.countGamesHaveCondition(controllers, true);
//		gda.analyzeGameDifference(controllers, true, true);
//		gda.analyzeAllGamesAverage(controllers, true);
//		fa.analyzeFitness(controllers, false);

		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "800t25pt_mutation_");
//		gda.analyzeAllMutationsAverage(controllers, 10, true);
//		fa.analyzeFitness(controllers, false);              
		
		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "800t25pt_gengames");
//		gda.countGamesHaveCondition(controllers, true);
//		gda.analyzeGameDifference(controllers, true, true);
//		gda.analyzeAllGamesAverage(controllers, true);
//		fa.analyzeFitness(controllers, false);
		
		
		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "800t25pt_designed_genlvls");
//		gda.analyzeGameDifference(controllers, false, true );
//		gda.analyzeAllGamesAverage(controllers, false);
//		fa.analyzeFitness(controllers, false);
		
                
                
        Controller[] goodDataControllers = ControllerHelper.copyControllers(controllers);
		Controller[] badDataControllers = ControllerHelper.copyControllers(controllers);
        ControllerHelper.setControllerDataFolders(goodDataControllers, dataFolder, "800t25pt_designed");
		ControllerHelper.setControllerDataFolders(badDataControllers, dataFolder, "800t25pt_gengames");
                
//                gda.analyzeGoodToBad(goodDataControllers, badDataControllers);
                
		//*********************************
		//**********2K TICKS TEST**********
		//*********************************
		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "2000ticks_all");
//		gda.analyzeGameDifference(controllers, false);
//		gda.analyzeAllGamesAverage(controllers, false);
	
//		fa.analyzeFitness(controllers, false);


		
//		controllers = new Controller[]{dontDieController};
//		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "50t10pt_gengames");
//		gda.analyzeAllGamesAverage(controllers, false);
//		gda.analyzeGameDifference(controllers, false, false);
		
		
		//*********************************
		//**********200 TICKS TEST*********
		//*********************************
		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "200ticks_all");
//		gda.analyzeGameDifference(controllers, false);
//		gda.analyzeAllGamesAverage(controllers, false);
//		fa.analyzeFitness(controllers, false);
		
		
//		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "200ticks_all2");
//		gda.analyzeGameDifference(controllers, false);
//		gda.analyzeAllGamesAverage(controllers, false);
//		fa.analyzeFitness(controllers, false);
		
//		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "200ticks_mutation_");
//		gda.analyzeMutationDifference(controllers, 20, false);
//		gda.analyzeAllMutationsAverage(controllers, 20, false);
//		fa.analyzeMutationsFitness(controllers, 20, false);
		
//		ControllerHelper.setControllerDataFolders(controllers, dataFolder, "200ticks_gengames");
//		gda.analyzeGameDifference(controllers, false);
//		gda.analyzeAllGamesAverage(controllers, false);
//		fa.analyzeFitness(controllers, false);
		
		/*Fitness feauture weights evolution:*/
//		Controller[] goodDataControllers = ControllerHelper.copyControllers(controllers);
//		Controller[] badDataControllers = ControllerHelper.copyControllers(controllers);
		ControllerHelper.setControllerDataFolders(goodDataControllers, dataFolder, "800t25pt_designed");
		ControllerHelper.setControllerDataFolders(badDataControllers, dataFolder, "800t25pt_gengames");
//		ControllerHelper.setControllerDataFolders(goodDataControllers, dataFolder, "200t10pt_designed");
//		ControllerHelper.setControllerDataFolders(badDataControllers, dataFolder, "200t10pt_gengames");
		
//		ControllerHelper.setControllerDataFolders(goodDataControllers, dataFolder, "800t25pt_designed_genlvls");
//		ControllerHelper.setControllerDataFolders(badDataControllers, dataFolder, "800t25pt_gengames");
		
//		fa.evolveFitnessWeights(goodDataControllers, badDataControllers, false);
		
//		fa.printFeautureData(goodDataControllers, badDataControllers);
		
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
