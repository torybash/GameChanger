package dataanalysis.core;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import dataanalysis.controller.Controller;
import dataanalysis.controller.Controller.ControllerType;
import dataanalysis.fitness.FitnessCalculator;
import dataanalysis.fitness.FitnessCalculator.FeatureDataType;
import dataanalysis.fitness.GameFitness;
import dataanalysis.fitness.GoodToBadFitness;
import dataanalysis.tools.ExtractGameData;
import dataanalysis.tools.GameDataCalculator;

public class FitnessAnalysis {

//	double[] test_feature_weights = new double[]{0.18008472296270414, -0.09748455015065097, -0.02398953515878227, -0.03975886486770416, 0.09117258290828545, -0.08603872942732953, 0.23909855672682728, 0.9894440716269293, -1.0, 0.06408592081641204, 0.272783987527952, 0.2522656205318279, 0.7555901663856714, 0.28636469712234935, 0.8482326435437321, -0.3648640460665813, -0.3668461846841874, -1.0};

	double[] init_feature_weights = new double[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	
	double[] test_feature_weights = new double[]{0.18008472296270414, -0.09748455015065097, -0.02398953515878227, -0.03975886486770416, 0.09117258290828545, -0.08603872942732953, 0.23909855672682728, 0.9894440716269293, -1.0, 0.06408592081641204, 0.272783987527952, 0.2522656205318279, 0.7555901663856714, 0.28636469712234935, 0.8482326435437321, -0.3648640460665813, -0.3668461846841874, -1.0};
	
	//goal: combination
	double[] test_feature_weights2 = new double[]{0.22810640779364252, 0.44346444846779065, -0.9529402038747338, 0.4065957235804854, 0.7041582396265695, -0.7991213718936194, -0.9998498114608703, -0.46221730573528097, 0.366350455497741, -0.011319320417942317, 0.8673776252747439, 0.9287654535043032, 0.11558259091880338, 0.00948694757805945, 0.462269679802316, -1.150995141884938, 3.1203018051233795, 0.9309767401197254, 0.447734256488427, 0.47096569681555006, 1.0986108979263272, 0.2970187890290669, -0.9868157370811648, 0.4067542363754116, 0.6081034753448745, 0.14244527499391776, 0.5653985451129862, -1.6451264776568157, -0.3473358697584487, -1.1683785778348352, 0.2725002016487154, 0.18185986240457805, -1.0256871851586442, 0.9055835147521094, -1.155394574151964, 1.650904147162838};
	
	//goal: g > 0.0, b < 0.0
	double[] test_feature_weights3 = new double[]{0.48655461712085846, -0.5285149978566213, 0.14288898117091275, 0.38957784614686136, 0.6130830673372448, -0.5181310014606579, -0.11815744932772326, 0.7861076983560249, 0.44577767439907745, 0.024884598359517944, 0.06021405860045709, 0.6957870293303161, -0.546547957815543, -0.7556928393394774, 0.4059670258950569, 0.30411438540342595, 0.09633296422249477, 0.40220011081093215, 0.02724960652472521, -0.3639732323715164, 1.248285511552112, -0.9038779047856457, -0.8069696427362683, -0.930678833843134, 0.29442191642534643, -1.1691335037344424, -0.047748732956500325, -0.9465040407996107, 0.45404920001925453, 0.6582986052986441, 0.6420409379659263, -0.6994402934604916, -0.1680840123583704, -0.2942018651106353, -0.8834644041770893, 1.3007468599755987};

        
    double[] test_feature_weights4 = new double[]{-1.0, -1.0, 1.0, 0.4197096563424653, 1.0, 0.977760356601113, -1.0, -0.7607187651654225, -0.019461817156662176, -0.22457540735359527, 1.0, 0.9459583856032282, 0.9677229752084824, -1.0, 0.28555396141287276, 0.42234143348903236, 1.0, 0.9636667610698653, 1.0, 1.0, 0.39445240839345597, 0.6311755044535656, 1.0, 1.0, 0.714386945966264, -1.0, -0.4960971069190489, -1.0, -1.0, 0.91255090218011, -0.41105802822060516, -1.0, -1.0, -0.0266739240720498, -1.0, 0.6732985424496798};
	
    
    double[] test_feature_weights5 = new double[]{0.47122818299548697, -0.9999999999995856, -0.5196610836789665, -0.7272536340536335, 0.9999999999752031, 0.999999999994899, 0.8135234263787241, -0.9999999999984274, 0.9999999995093256, 0.9999999999956907, 0.999999790201857, -0.9999999999990212, 0.999999999843373, -0.0663177365011005, 0.9999999999957074, 0.9999999999988536, 0.9999999999985438, 0.9999999997543857, 0.999999999998833, -0.451767986905702, 0.9999999999871393, -0.5105294422740858, 0.9999999999955425, -0.7337195181897531, 0.9113339228315717, 0.9049283100190848, -0.849047263967509, -0.9986800058334577, 0.9999999999477512, 0.9999999999487784, 0.1813094245690538, -0.9999999999976864, -0.9999999994577699, -0.9999999998096374, -0.999999999980716, 0.4788647059631101};

    
    double[] test_feature_weights6 = new double[]{-0.999999999900408, -0.34984920850320544, -0.22260241182807766, 0.5722092430415723, 0.9999999999949643, 0.6316533324646831, 0.5878492196495587, -0.9999999997533239, 0.9999999999728028, 0.9999999999990488, 0.9999999999987808, -0.9999999999917272, 0.5330314084301037, 0.9999999999856031, 0.9999999999892688, 0.999999882721834, 0.9999999999996816, 0.9999999999976473, 0.9999999999988467, 0.21564211581749065, 0.9999999999995636, 0.9999999999988249, 0.9999999999952035, -0.49956680227577044, -0.05428597278787771, 0.9999999999912151, -0.9999999999866348, -0.9999999999967432, 0.9999999999662218, 0.9999999358743064, 0.9999999999992557, 0.9999999999804007, -0.9999999999927789, -0.9999999999906525, -0.524347580116094, 0.9999999999969006};

    double[] test_feature_weights48 = new double[]{7.859456671106872E-13, 0.4830251738336509, 0.9999999999816537, -1.4315654279449245E-12, 0.9628053521180413, 0.9999999999988323, 0.7496159989598317, -0.7768701677527983, 0.9903398834071386, 7.844136539322498E-13, 0.9233255216982408, 0.9999999999998429, 0.8746339340365913, -0.7756177276199255, -0.999999999947285, 0.7279654593509312, -0.2657996101005413, -0.9999999999992467};
    
    
	public void analyzeFitness(Controller[] controllers, boolean matrixApproach) {
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractGameDatas(controllers, false);
		gameDatas = GameDataCalculator.getAcceptedGames(gameDatas);
		ArrayList<GameData[]> gameDatasAverages = GameDataCalculator.getAverageForEachGame(gameDatas);
		

		FitnessCalculator.setWeights(null, null);
		ArrayList<GameFitness> fitnessValues = FitnessCalculator.getFitnessForEachGame(gameDatasAverages, controllers, null);
		
		Collections.sort(fitnessValues);
		
		for (GameFitness gameFitness : fitnessValues) {
			System.out.println(gameFitness.gameTitle + " has fitness:\t" + gameFitness.fitness);
		}
	}
	



	public void analyzeMutationsFitness(Controller[] controllers, int numberMutations, boolean matrixApproach) {
		ArrayList<GameData[]> gameDatas = ExtractGameData.extractMutatedGameDatas(controllers, numberMutations, false);
		gameDatas = GameDataCalculator.getAcceptedGames(gameDatas);
		ArrayList<GameData[]> gameAverages = GameDataCalculator.getAverageForEachGame(gameDatas);
		
		FitnessCalculator.setWeights(init_feature_weights, null);
		ArrayList<GameFitness> fitnessValues = FitnessCalculator.getFitnessForEachGame(gameAverages, controllers, null);
		
		Collections.sort(fitnessValues);
		
		for (GameFitness gameFitness : fitnessValues) {
			System.out.println(gameFitness.gameTitle + " has fitness:\t" + gameFitness.fitness);
		}
	}
	
	
	public void evolveFitnessWeights(Controller[] goodDataControllers, Controller[] badDataControllers, boolean[] dataTypesToUse) {
		int n = goodDataControllers.length;
		ArrayList<GameData[]> goodGameDatas = ExtractGameData.extractGameDatas(goodDataControllers, false);
		ArrayList<GameData[]> badGameDatas = ExtractGameData.extractGameDatas(badDataControllers, false);
		
		goodGameDatas = GameDataCalculator.getAcceptedGames(goodGameDatas);
		badGameDatas = GameDataCalculator.getAcceptedGames(badGameDatas);
		
		ArrayList<GameData[]> goodGameAverages = GameDataCalculator.getAverageForEachGame(goodGameDatas);
		ArrayList<GameData[]> badGameAverages = GameDataCalculator.getAverageForEachGame(badGameDatas);
		
		Random r = new Random();
		final int iterations = 100000, mutations = 100, survivors = 10;
		boolean keep_survivors = true;
		double mutate_factor_sd = 0.1;
		double[][][][] ctrlMatrixWeightList = new double[mutations][][][];
		double[][][][] ctrlSurvivedMatrixWeightList = new double[survivors][][][];
		
		int ctrlTypCnt = ControllerType.class.getEnumConstants().length;
		int dataTypCnt = FeatureDataType.class.getEnumConstants().length;
		
		ArrayList<GoodToBadFitness> gtbFitnessList = new ArrayList<GoodToBadFitness>(); 
		for (int i = 0; i < iterations; i++) {
			System.out.println("---------ITERATION: " + i + "---------------------");
		
			gtbFitnessList.clear();
			
			//Create initial values (weights)
			if (i==0){
				for (int m = 0; m < mutations; m++) {						
					ctrlMatrixWeightList[m] = new double[ctrlTypCnt][][];
					for (int c1 = 0; c1 < ctrlMatrixWeightList[m].length; c1++) {
						ctrlMatrixWeightList[m][c1] = new double[ctrlTypCnt][];
						for (int c2 = 0; c2 < ctrlMatrixWeightList[m].length; c2++) {
							if (c1 >= c2) continue;
							ctrlMatrixWeightList[m][c1][c2] = new double[dataTypCnt];
							for (int t = 0; t < dataTypCnt; t++) {
								ctrlMatrixWeightList[m][c1][c2][t] = r.nextDouble() * 2f - 1f;
							}
						}
						
					}
				}
			}
			
			
			//Calculate fitness and make lists
			for (int m = 0; m < mutations; m++) {
				FitnessCalculator.setWeights(ctrlMatrixWeightList[m]);

				ArrayList<GameFitness> goodFitnessValues = FitnessCalculator.getFitnessForEachGame(goodGameAverages, goodDataControllers, dataTypesToUse);
				ArrayList<GameFitness> badFitnessValues = FitnessCalculator.getFitnessForEachGame(badGameAverages, badDataControllers, dataTypesToUse);
				gtbFitnessList.add(new GoodToBadFitness(m, goodFitnessValues, badFitnessValues));
			}
			
			//Pick best values (weights)
			Collections.sort(gtbFitnessList);
			for (int s = 0; s < survivors; s++) {
				ctrlSurvivedMatrixWeightList[s] = ctrlMatrixWeightList[gtbFitnessList.get(s).idx].clone();
				
				//PRINTOUT
				if (s > 10) continue;
				System.out.println("GTB-Fitness: " + gtbFitnessList.get(s).gtbFitness);
				
				if (s > 0) continue;
				System.out.println(Arrays.toString(FeatureDataType.values()));
				for (int c1 = 0; c1 < ctrlTypCnt; c1++) {
					for (int c2 = 0; c2 < ctrlTypCnt; c2++) {
						if (c1 >= c2) continue;
						System.out.println(ControllerType.values()[c1] + ">" + ControllerType.values()[c2] + ", " +Arrays.toString(ctrlSurvivedMatrixWeightList[s][c1][c2]));
					}
				}
				System.out.println(Arrays.toString(FitnessCalculator.matrixWeightsToArray(ctrlSurvivedMatrixWeightList[s])));
				
				Collections.sort(gtbFitnessList.get(s).goodFitnessValues);
				Collections.sort(gtbFitnessList.get(s).badFitnessValues);
				for (int j2 = 0; j2 < gtbFitnessList.get(s).goodFitnessValues.size(); j2++) {
					System.out.println(gtbFitnessList.get(s).goodFitnessValues.get(j2).gameTitle + ": "+ gtbFitnessList.get(s).goodFitnessValues.get(j2).fitness + ", Best bad game fitness: " + gtbFitnessList.get(s).badFitnessValues.get(j2).gameTitle + ": "+gtbFitnessList.get(s).badFitnessValues.get(j2).fitness);
				}
			}
			
						
			//Mutate best values (weights)
			for (int m = 0; m < mutations; m++) {
				if (m >= survivors || !keep_survivors){
					double[][][] newWeights = new double[ctrlTypCnt][][];
					for (int c1 = 0; c1 < ctrlTypCnt; c1++) {
						newWeights[c1] = new double[ctrlTypCnt][];
						for (int c2 = 0; c2 < ctrlTypCnt; c2++) {
							if (c1 >= c2) continue;
							newWeights[c1][c2] = new double[dataTypCnt];
							for (int t = 0; t < dataTypCnt; t++) {
//								System.out.println("m: " + m + ", surv: "+ survivors + ". reuslt " + m/(mutations/survivors));
//								System.out.println("c1 " + c1 + " c2: "+ c2  +", t: " + t);
								double newVal = ctrlSurvivedMatrixWeightList[m/(mutations/survivors)][c1][c2][t] + ((r.nextGaussian() * mutate_factor_sd));
								if (newVal > 1) newVal = 1; if (newVal < -1) newVal = -1;
								newWeights[c1][c2][t] = newVal;
							}
						}
					}
					ctrlMatrixWeightList[m] = newWeights;
				}else{
					ctrlMatrixWeightList[m] = ctrlSurvivedMatrixWeightList[m];
				}
			}
		}
	}




	public void printFeautureData(Controller[] designedDataControllers, Controller[] generatedDataControllers) {
		int n = designedDataControllers.length;
		ArrayList<GameData[]> designedGameDatas = ExtractGameData.extractGameDatas(designedDataControllers, false);
		ArrayList<GameData[]> generatedGameDatas = ExtractGameData.extractGameDatas(generatedDataControllers, false);
		
		designedGameDatas = GameDataCalculator.getAcceptedGames(designedGameDatas);
		generatedGameDatas = GameDataCalculator.getAcceptedGames(generatedGameDatas);
		
		ArrayList<GameData[]> designedGameAverages = GameDataCalculator.getAverageForEachGame(designedGameDatas);
		ArrayList<GameData[]> generatedGameAverages = GameDataCalculator.getAverageForEachGame(generatedGameDatas);
		
		FitnessCalculator.setWeights(null, null);
		ArrayList<GameFitness> designedFitnessValues = FitnessCalculator.getFitnessForEachGame(designedGameAverages, designedDataControllers, null);
		ArrayList<GameFitness> generatedFitnessValues = FitnessCalculator.getFitnessForEachGame(generatedGameAverages, generatedDataControllers, null);
		
		
		int ctrlTypCnt = ControllerType.class.getEnumConstants().length;
		int dataTypCnt = FeatureDataType.class.getEnumConstants().length;
		
		String topString = "";
		for (int c1 = 0; c1 < ctrlTypCnt; c1++) {
			for (int c2 = 0; c2 < ctrlTypCnt; c2++) {
				if (c1 >= c2) continue;
				for (int t = 0; t < dataTypCnt; t++) {
					topString += ControllerType.values()[c1] + ">" + ControllerType.values()[c2] + "::" + FeatureDataType.values()[t] + ",";
				}
			}
		}
		topString += "class";
				
		String gameStrings = "";
		for (GameFitness gameFitness : designedFitnessValues) {
			for (int i = 0; i < gameFitness.fitnessVals.length; i++) {
				gameStrings += gameFitness.fitnessVals[i] + ",";
			}
			gameStrings += "designed\n";
		}
		
		for (GameFitness gameFitness : generatedFitnessValues) {
			for (int i = 0; i < gameFitness.fitnessVals.length; i++) {
				gameStrings += gameFitness.fitnessVals[i] + ",";
			}
			gameStrings += "generated\n";
		}
		
		String dataFileName = "feature_data_";
		dataFileName += designedDataControllers[0].dataFolder.replaceFirst("(?!\\d)t(?=\\d)", ",").replaceAll("[a-zA-Z/_]*", "");
		dataFileName += " (" + generatedFitnessValues.get(0).fitnessVals.length + ")" + ".csv";
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(dataFileName, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		writer.flush();
		writer.print(topString + "\n" + gameStrings);
		writer.close();
		
		System.out.println("Written CSV file: " + dataFileName + " to disk. Amount of games: Designed=" + designedFitnessValues.size() + ", generated=" + generatedFitnessValues.size() );
	}




	public void tryDifferentFitnessValues(Controller[] goodDataControllers, Controller[] badDataControllers) {
		
		CMAEvolStrat cma = new CMAEvolStrat();
		
//		0 --> [false, false,.., false]
//		1 --> [false, false,.., true]
		
		int dataTypesCnt = FeatureDataType.class.getEnumConstants().length;
		ArrayList<FitnessTryResults> results = new ArrayList<FitnessTryResults>((int) Math.pow(2, dataTypesCnt));
		
		for (int i = 1; i < Math.pow(2, dataTypesCnt); i++) {
			
			boolean[] dataTypesToUse = toBinary(i, dataTypesCnt);
			System.out.println(" using: " + Arrays.toString(dataTypesToUse));
//			evolveFitnessWeights(goodDataControllers, badDataControllers, dataTypesToUse);
			double result = 1 - cma.evolveFitnessWeights(goodDataControllers, badDataControllers, dataTypesToUse);
			
			results.add(new FitnessTryResults(i, result));
			System.out.println(result);
		}
		
		Collections.sort(results);
//		Arrays.toString(sortedResults);

		for (FitnessTryResults fitnessTryResults : results) {
			System.out.println(Arrays.toString(toBinary(fitnessTryResults.idx, dataTypesCnt)) + " = " + fitnessTryResults.result);
		}	
	}



	private static boolean[] toBinary(int number, int base) {
	    final boolean[] ret = new boolean[base];
	    for (int i = 0; i < base; i++) {
	        ret[base - 1 - i] = (1 << i & number) != 0;
	    }
	    return ret;
	}

	
	class FitnessTryResults implements Comparable{
		int idx;
		double result;
		
		public FitnessTryResults(int idx, double result){
			this.idx = idx;
			this.result = result;
		}

		@Override
		public int compareTo(Object o) {
			double otherValue = ((FitnessTryResults) o).result;			
			if (result-otherValue == 0) return 0;
			return result-otherValue > 0 ? -1 : 1;
		}
	}
}
