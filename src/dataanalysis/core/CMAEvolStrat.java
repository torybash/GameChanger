package dataanalysis.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import dataanalysis.controller.Controller;
import dataanalysis.controller.Controller.ControllerType;
import dataanalysis.fitness.FitnessCalculator;
import dataanalysis.fitness.FitnessCalculator.FeatureDataType;
import dataanalysis.fitness.GameFitness;
import dataanalysis.fitness.GoodToBadFitness;
import dataanalysis.tools.ExtractGameData;
import dataanalysis.tools.GameDataCalculator;
import fr.inria.optimization.cmaes.CMAEvolutionStrategy;



public class CMAEvolStrat {

	
	
	public void evolveFitnessWeights(Controller[] goodDataControllers, Controller[] badDataControllers) {
		int n = goodDataControllers.length;
		ArrayList<GameData[]> goodGameDatas = ExtractGameData.extractGameDatas(goodDataControllers, false);
		ArrayList<GameData[]> badGameDatas = ExtractGameData.extractGameDatas(badDataControllers, false);
		
		goodGameDatas = GameDataCalculator.getAcceptedGames(goodGameDatas);
		badGameDatas = GameDataCalculator.getAcceptedGames(badGameDatas);
		
		ArrayList<GameData[]> goodGameAverages = GameDataCalculator.getAverageForEachGame(goodGameDatas);
		ArrayList<GameData[]> badGameAverages = GameDataCalculator.getAverageForEachGame(badGameDatas);
		
		int amountWeights = 0;
		for (int i = 0; i < ControllerType.class.getEnumConstants().length; i++) {
			for (int j = 0; j < ControllerType.class.getEnumConstants().length; j++) {
				if (i > j){
					for (int k = 0; k < FeatureDataType.class.getEnumConstants().length; k++) {
						amountWeights++;
					}

				}
			}
		}
		
		
		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		cma.readProperties(); // read options, see file CMAEvolutionStrategy.properties
		cma.setDimension(amountWeights); // overwrite some loaded properties
		cma.setInitialX(0.05); // in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(0.2); // also a mandatory setting 
		
		
//		System.out.println("cma.parameters.getDamps(): " + cma.parameters.getDamps());
		
		//		cma.options.stopFitness = -1e14;       // optional setting

		// initialize cma and get fitness array to fill in later
		double[] fitness = cma.init();  // new double[cma.parameters.getPopulationSize()];

		// initial output to files
		cma.writeToDefaultFilesHeaders(0); // 0 == overwrites old files

		// iteration loop
		while(cma.stopConditions.getNumber() == 0) {

            // --- core iteration step ---
			double[][] pop = cma.samplePopulation(); // get a new population of solutions
			for(int i = 0; i < pop.length; ++i) {    // for each candidate solution i
            	// a simple way to handle constraints that define a convex feasible domain  
            	// (like box constraints, i.e. variable boundaries) via "blind re-sampling" 
            	                                       // assumes that the feasible domain is convex, the optimum is  
//				while (!fitfun.isFeasible(pop[i]))     //   not located on (or very close to) the domain boundary,  
//					pop[i] = cma.resampleSingle(i);    //   initialX is feasible and initialStandardDeviations are  
                                                       //   sufficiently small to prevent quasi-infinite looping here
				boolean allValuesBelowOne = false;
				while (!allValuesBelowOne){
					allValuesBelowOne = true;
					for (int j = 0; j < pop[i].length; j++) {
//						if (pop[i][j] > 1) pop[i][j] = 1;
//						if (pop[i][j] < -1) pop[i][j] = -1;
						
						if (Math.abs(pop[i][j]) > 1){
							cma.resampleSingle(i);
							allValuesBelowOne = false;
							break;
						}
					}
				}
//				System.out.println(Arrays.toString(pop[i]));
				
				
                // compute fitness/objective value	
//				fitness[i] = fitfun.valueOf(pop[i]); // fitfun.valueOf() is to be minimized
				fitness[i] = - calculateGtbFitness(pop[i], goodDataControllers, badDataControllers, goodGameAverages, badGameAverages);
//				fitness[i] = goodGameAverages.size() * badGameAverages.size() - calculateGtbFitness(pop[i], goodDataControllers, badDataControllers, goodGameAverages, badGameAverages);
//				fitness[i] = calculateGtbFitness(pop[i], goodDataControllers, badDataControllers, goodGameAverages, badGameAverages);
			}
			cma.updateDistribution(fitness);         // pass fitness array to update search distribution
            // --- end core iteration step ---

			// output to files and console 
			cma.writeToDefaultFiles();
			int outmod = 150;
			if (cma.getCountIter() % (15*outmod) == 1){
				cma.printlnAnnotation(); // might write file as well
				System.out.println(Arrays.toString(cma.getBestX()));
			}
			if (cma.getCountIter() % outmod == 1)
				cma.println(); 
			
			
			
			
			
		}
		// evaluate mean value as it is the best estimator for the optimum
//		cma.setFitnessOfMeanX(fitfun.valueOf(cma.getMeanX())); // updates the best ever solution 

		// final output
		cma.writeToDefaultFiles(1);
		cma.println();
		cma.println("Terminated due to");
		for (String s : cma.stopConditions.getMessages())
			cma.println("  " + s);
//		cma.println("best function value " + (cma.getBestFunctionValue() 
				cma.println("best function value " + (goodGameAverages.size() * badGameAverages.size() - cma.getBestFunctionValue())
				+ " at evaluation " + cma.getBestEvaluationNumber());
			
		// we might return cma.getBestSolution() or cma.getBestX()
		
		System.out.println(Arrays.toString(cma.getBestX()));
	}
	
	double calculateGtbFitness(double[] weights, Controller[] goodDataControllers, Controller[] badDataControllers, 
			ArrayList<GameData[]> goodGameAverages, ArrayList<GameData[]> badGameAverages){
		
		int counter = 0;
		double[][][] ctrlMatrixWeights = new double[ControllerType.class.getEnumConstants().length][][];
		for (int c1 = 0; c1 < ctrlMatrixWeights.length; c1++) {
			ctrlMatrixWeights[c1] = new double[ControllerType.class.getEnumConstants().length][];
			for (int c2 = 0; c2 < ctrlMatrixWeights[c1].length; c2++) {
				if (c1 >= c2) continue;
				ctrlMatrixWeights[c1][c2] = new double[FeatureDataType.class.getEnumConstants().length];
				for (int t = 0; t < ctrlMatrixWeights[c1][c2].length; t++) {
					ctrlMatrixWeights[c1][c2][t] = weights[counter];
					counter++;
				}
			}
		}
		
		FitnessCalculator.setCtrlMatrixWeights(ctrlMatrixWeights);

		ArrayList<GameFitness> goodFitnessValues = FitnessCalculator.getFitnessForEachGame(goodGameAverages, goodDataControllers, false);
		ArrayList<GameFitness> badFitnessValues = FitnessCalculator.getFitnessForEachGame(badGameAverages, badDataControllers, false);
		GoodToBadFitness gtbf = new GoodToBadFitness(-1, goodFitnessValues, badFitnessValues);
		
		Collections.sort(goodFitnessValues);
		Collections.sort(badFitnessValues);
		
		System.out.println("---");
		for (int i = 0; i < goodFitnessValues.size(); i++) {
			System.out.println(goodFitnessValues.get(i).gameTitle + " : " + goodFitnessValues.get(i).fitness);
		}
		
		for (int i = 0; i < 5; i++) {
			System.out.println(badFitnessValues.get(i).gameTitle + " : " + badFitnessValues.get(i).fitness);
		}
		System.out.println();
		
//		System.out.println("weights!!!!: " + Arrays.toString(weights));
//		System.out.println("gtbf.gtbFitness:::: " + gtbf.gtbFitness);
		
		return gtbf.gtbFitness;
	}
}
